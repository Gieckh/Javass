package ch.epfl.javass.jass;

import java.util.StringJoiner;

import ch.epfl.javass.bits.Bits32;
import ch.epfl.javass.jass.Card.Color;

/**
 * @brief Non-instantiable
 *        Contains the methods used to manipulate the Tricks
 *
 * @author - Marin Nguyen (288260)
 * @author - Antoine Scardigli (299905)
 */
public final class PackedTrick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final int INVALID = ~0;

    private static final int CARD_SIZE = 6;
    private static final int CARD_1_START = 0;
    private static final int CARD_2_START = CARD_1_START + CARD_SIZE; //6
    private static final int CARD_3_START = CARD_2_START + CARD_SIZE; //12
    private static final int CARD_4_START = CARD_3_START + CARD_SIZE; //18
    private static final int CARD_COLOR_START = 4;
    
    private static final int CARD_MASK_1 = 0b111111;
    private static final int CARD_MASK_2 = CARD_MASK_1 << CARD_SIZE;
    private static final int CARD_MASK_3 = CARD_MASK_2 << CARD_SIZE;
    private static final int CARD_MASK_4 = CARD_MASK_3 << CARD_SIZE;

    private final static int MAX_RANK = 8;
    private final static int RANK_MASK_1 = 0b001111;
    private final static int RANK_MASK_2 = RANK_MASK_1 << CARD_SIZE;
    private final static int RANK_MASK_3 = RANK_MASK_2 << CARD_SIZE;
    private final static int RANK_MASK_4 = RANK_MASK_3 << CARD_SIZE;

    private static final int INDEX_START = CARD_4_START + CARD_SIZE; //24
    private static final int INDEX_SIZE = 4;
    private static final int MAX_INDEX = 8; //the index starts at 0

    private static final int PLAYER_START = INDEX_START + INDEX_SIZE; //28
    private static final int PLAYER_SIZE = 2;

    private static final int TRUMP_START = PLAYER_START + PLAYER_SIZE; //30
    private static final int TRUMP_SIZE = 2;

    private static final int EMPTY  = (PackedCard.INVALID << CARD_1_START) |
            (PackedCard.INVALID << CARD_2_START) |
            (PackedCard.INVALID << CARD_3_START) |
            (PackedCard.INVALID << CARD_4_START);

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    private PackedTrick() {
        // not to be instantiated
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    //Here we are ready to face all kinds of invalid cards
    /**
     * @brief Indicates whether the given "pkTrick" is packed correctly
     *
     * @param pkTrick (int) - the [packed] trick to check
     * @return (boolean) - true if "pkTrick" encodes a valid trick
     */
    public static boolean isValid(int pkTrick) {
        if (Bits32.extract(pkTrick, INDEX_START, INDEX_SIZE) > MAX_INDEX) {
            return false;
        }

        int masked1 = pkTrick & CARD_MASK_1;
        int masked2 = pkTrick & CARD_MASK_2;
        int masked3 = pkTrick & CARD_MASK_3;
        int masked4 = pkTrick & CARD_MASK_4;

        boolean isRank1Valid = ((masked1 & RANK_MASK_1) >>> CARD_1_START) <= MAX_RANK;
        boolean isRank2Valid = ((masked2 & RANK_MASK_2) >>> CARD_2_START) <= MAX_RANK;
        boolean isRank3Valid = ((masked3 & RANK_MASK_3) >>> CARD_3_START) <= MAX_RANK;
        boolean isRank4Valid = ((masked4 & RANK_MASK_4) >>> CARD_4_START) <= MAX_RANK;

        if (!isRank1Valid) {
            return  (masked1 == CARD_MASK_1) && (masked2 == CARD_MASK_2) &&
                    (masked3 == CARD_MASK_3) && (masked4 == CARD_MASK_4);
        }

        if (!isRank2Valid) {
            return (masked2 == CARD_MASK_2) && (masked3 == CARD_MASK_3) &&
                   (masked4 == CARD_MASK_4);
        }

        if (!isRank3Valid) {
            return (masked3 == CARD_MASK_3) && (masked4 == CARD_MASK_4);
        }

        return (isRank4Valid || masked4 == CARD_MASK_4);
    }

    /**
     * @brief returns the (int) corresponding to the first [packed] trick of a
     *        turn, given the trump and the Id of first Player.
     *
     * @param trump (Color) - the Color of the trump
     * @param firstPlayer - (PlayerId) the first Player of the turn
     * @return (int) - The first [packed] trick of a turn, given the specified parameters.
     */
    public static int firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        int player = (firstPlayer.ordinal()) << PLAYER_START;
        int color  = trump.ordinal() << TRUMP_START;

        return EMPTY | player | color;
    }

    /**
     * @brief Given a full [packed] trick, returns the next empty one. If
     *        the specified [packed] trick was the last of the turn, returns "INVALID"
     *        instead.
     * @see PackedTrick#INVALID
     *
     * @param pkTrick (int) - a full [packed] trick
     * @return (int) - the next PackedTrick, or "INVALID" if there is none.
     */
    public static int nextEmpty(int pkTrick) {
        if (isLast(pkTrick)) {
            return INVALID;
        }

        //incrementing the index
        pkTrick += (1 << INDEX_START);

        int winningPlayerOrdinal = winningPlayer(pkTrick).ordinal();
        pkTrick &= ~Bits32.mask(PLAYER_START, PLAYER_SIZE);
        pkTrick |= (winningPlayerOrdinal << PLAYER_START);

        return pkTrick | EMPTY;
    }

    /**
     * @brief Indicates whether the specified [packed] trick is the last of the turn
     *
     * @param pkTrick (int) - the [packed] trick to check
     * @return (boolean) - true if the specified [packed] trick is the 9-th of the turn
     */
    public static boolean isLast(int pkTrick) {
        return index(pkTrick) == MAX_INDEX;
    }

    /**
     * @brief Indicates whether the given [packed] trick contains any Card.
     *
     * @param pkTrick (int) - the given [packed] trick
     * @return (boolean) - true if the specified [packed] trick is empty.
     */
    public static boolean isEmpty (int pkTrick) {
        return (pkTrick & EMPTY) == EMPTY;
    }


    /**
     * @brief Indicates whether the given [packed] trick contains 4 Cards.
     *
     * @param pkTrick (int) - the given [packed] trick
     * @return (boolean) true if the specified [packed] trick is full.
     */
    public static boolean isFull(int pkTrick) {
        return Bits32.extract(pkTrick, CARD_4_START, CARD_SIZE) != PackedCard.INVALID;
    }


    /**
     * @brief Indicates whether the cardNo-th Card of the trick is a valid card
     *        (otherwise, it should be INVALID)
     *
     * @param pkTrick (int) - the specifies [packed] trick
     * @return (boolean) - true if the cardNo-th Card of the trick is not "INVALID"
     */
    private static boolean containsValidCard(int pkTrick, int cardNo) {
        switch(cardNo) {
        case 1:
            return ((RANK_MASK_1 & pkTrick) != RANK_MASK_1);
        case 2:
            return ((RANK_MASK_2 & pkTrick) != RANK_MASK_2);
        case 3:
            return ((RANK_MASK_3 & pkTrick) != RANK_MASK_3);
        case 4:
            return ((RANK_MASK_4 & pkTrick) != RANK_MASK_4);
        default: //wrong cardNo
            throw new IllegalArgumentException();
        }
    }


    /**
     * @brief The numbers of Cards played in this [packed] trick.
     *
     * @param pkTrick (int) - a [packed] trick
     * @return (int) - the number of cards in "pkTrick"
     */
    public static int size(int pkTrick) {
        int size = 0;
        for (int cardNo = 1 ; cardNo <= 4 ; ++cardNo) {
            if (containsValidCard(pkTrick, cardNo))
                size++;
            else
                return size;
        }

        return size;
    }

    /**
     * @brief Indicates the color of the trump, which is specified by the encoding
     *        of "pkTrick"
     *
     * @param pkTrick (int) - the given [packed] trick
     * @return (Color) - the color of the trump in pkTrick
     */
    public static Card.Color trump (int pkTrick) {
        int pkColor = Bits32.extract(pkTrick, TRUMP_START, TRUMP_SIZE);
        return Color.ALL.get(pkColor);
    }

    /**
     * @brief returns the Id of the player that will play at the index-th position
     *        of the given [packed] trick.
     *
     * @param pkTrick (int) - the given [packed] trick
     * @param index (int) - the index in this [packed] trick, hence between 0 and 3
     * @return (PlayerId) - the Id of the player that will play at the index-th position
     *                      of the given [packed] trick.
     */
    public static PlayerId player (int pkTrick, int index) {
        assert (0 <= index && index < PlayerId.COUNT);

        int firstPlayer = Bits32.extract(pkTrick, PLAYER_START, PLAYER_SIZE);
        return PlayerId.ALL.get((firstPlayer + index) % PlayerId.COUNT);
    }

    /**
     * @brief Indicates the ordinal of this [packed] trick in the current turn
     *        (between 0 and MAX_INDEX = 8)
     *
     * @param pkTrick (int) - the given [packed] trick
     * @return (int) - the ordinal of this [packed] trick in the current turn.
     */
    public static int index (int pkTrick) {
        return Bits32.extract(pkTrick, INDEX_START, INDEX_SIZE);
    }


    //assuming the card is indeed there.
    /**
     * @brief returns the Card at the index-th position (from 0 to 3).
     *
     * @param pkTrick (int) - the given [packed] trick
     * @param index (int) - the index, in this trick, of the Card to get
     * @return (Card) - the card at the index'th position
     */
    public static int card (int pkTrick, int index) {
        assert (isValid(pkTrick));
        assert (0 <= index  &&  index < PlayerId.COUNT);
        assert (!isEmpty(pkTrick));

        switch(index) {
        case 0:
            return pkTrick & CARD_MASK_1;
        case 1:
            return (pkTrick & CARD_MASK_2) >>> CARD_2_START;
        case 2:
            return (pkTrick & CARD_MASK_3) >>> CARD_3_START;
        case 3:
            return (pkTrick & CARD_MASK_4) >>> CARD_4_START;
        default: //wrong index
            throw new IllegalArgumentException();
        }
    }

    //assuming not full.
    /**
     * @brief returns a new pkTrick where the specified card has been added.
     *
     * @param pkTrick (int) - the given [packed] trick - not full.
     * @param pkCard (int) - the [packed] card to add
     * @return (int) - the given [packed] trick, except the specified [packed] card
     *                 "pkCard" has been added.
     */
    public static int withAddedCard(int pkTrick, int pkCard) {
        assert (PackedCard.isValid(pkCard));
        assert (PackedTrick.isValid(pkTrick));
        assert (!isFull(pkTrick));

        if ((pkTrick & CARD_MASK_1) == CARD_MASK_1) {
            return (pkTrick & (~CARD_MASK_1)) | pkCard;
        }

        if ((pkTrick & CARD_MASK_2) == CARD_MASK_2) {
            return (pkTrick & (~CARD_MASK_2)) | (pkCard << CARD_2_START);
        }

        if ((pkTrick & CARD_MASK_3) == CARD_MASK_3) {
            return (pkTrick & (~CARD_MASK_3)) | (pkCard << CARD_3_START);
        }

        return (pkTrick & (~CARD_MASK_4)) | (pkCard << CARD_4_START);
    }

    /**
     * @brief The color of the first card of this trick. [will return CLUB if the
     *        trick is empty]
     *
     * @param pkTrick (int) - the given [packed] trick
     * @return (Color) - the Color of the first Card of this [packed] trick.
     */
    public static Card.Color baseColor(int pkTrick) {
        assert (isValid(pkTrick));

        //cuz i dont want to re-extract.
        int firstCardColor = (pkTrick & 0b110000) >>> CARD_COLOR_START;
        return Color.ALL.get(firstCardColor);
    }


    //assumed not full
    /**
     * @brief Return the Cards which can be played in the [packed] trick "pkTrick"
     *        given the hand "pkHand".
     *
     * @param pkTrick (int) - the given [packed] trick
     * @param pkHand (long) - the given hand [i.e. [packed] CardSet)
     * @return (long) - the cards that can be played given the hand and the trick.
     */
    public static long playableCards(int pkTrick, long pkHand) {
        assert(!isFull(pkTrick));

        if (isEmpty(pkTrick)) { //If you are the first player to play.
            return pkHand;
        }

        Card.Color trump = trump(pkTrick);
        Card.Color baseColor = baseColor(pkTrick);
        long myTrumps = PackedCardSet.subsetOfColor(pkHand, trump);

        //If the pli is fonded trump, then you don't have to play a higher card.
        if (trump == baseColor) {
            if (myTrumps == PackedCardSet.EMPTY |
                myTrumps == PackedCardSet.singleton(PackedCard.pack(trump, Card.Rank.JACK))) { //The special rule
                return pkHand;
            }
            else {
                return myTrumps;
            }
        }

        //trump != baseColor
        int winningCard = winningCard(pkTrick, trump);
        boolean isBestTrump = PackedCard.color(winningCard) == trump;
        long playableNotTrump = PackedCardSet.subsetOfColor(pkHand, baseColor);
        if (isBestTrump) {
            long betterTrumps = PackedCardSet.trumpAbove(winningCard) & myTrumps;
            if (playableNotTrump != PackedCardSet.EMPTY) {
                return playableNotTrump | betterTrumps;
            }

            //We got no card of the base color
            long inferiorTrumps = PackedCardSet.difference(myTrumps, betterTrumps);
            long defaultCase = PackedCardSet.difference(pkHand, inferiorTrumps);
            if (defaultCase != PackedCardSet.EMPTY) {
                return defaultCase;
            }

            //We only got inferior trumps
            return pkHand; //== inferiorTrumps
        }

        //The best card is not (yet) a trump
        if (playableNotTrump != PackedCardSet.EMPTY) {
            return playableNotTrump | myTrumps;
        }

        return pkHand;
    }

    

    //only called with a valid, full trick
    /**
     * @brief The value of the given [packed] trick, when won
     *
     * @param pkTrick (int) - the given full [packed] trick
     * @return (int) the value of the specified [packed] trick
     */
    public static int points(int pkTrick) {
        assert (isValid(pkTrick));
        assert (isFull(pkTrick));

        int total = (isLast(pkTrick)) ? Jass.LAST_TRICK_ADDITIONAL_POINTS : 0;
        Card.Color trump = trump(pkTrick);
        total += PackedCard.points(trump, pkTrick & CARD_MASK_1);
        total += PackedCard.points(trump, (pkTrick & CARD_MASK_2) >>> CARD_2_START);
        total += PackedCard.points(trump, (pkTrick & CARD_MASK_3) >>> CARD_3_START);
        total += PackedCard.points(trump, (pkTrick & CARD_MASK_4) >>> CARD_4_START);

        return total;
    }


    // We built 2 different methods "winningCard" and "winningCardIndex" (instead of using "card(winningCardIndex)")
    // cuz' it will reduce chain errors and also enable our program to be a bit faster.

    //Here we don't assume 4 cards have been played during this trick.
    //But we assume at least one has been
    private static int winningCard(int pkTrick, Card.Color trump) {
        assert (!isEmpty(pkTrick));

        int winningCard = pkTrick & CARD_MASK_1;
        for (int i = 2 ; i <= 4 ; ++i) {
            if (containsValidCard(pkTrick, i)) {
                int pkCard = card(pkTrick, i-1);
                if (PackedCard.isBetter(trump, pkCard, winningCard)) {
                    winningCard = pkCard;
                }
            }
            else {
                return winningCard;
            }
        }

        return winningCard;
    }

    //Here we don't assume 4 cards have been played during this trick.
    //But we assume at least one has been
    private static int winningCardIndex(int pkTrick, Card.Color trump) {
        assert (!isEmpty(pkTrick));

        int winningCard = pkTrick & CARD_MASK_1;
        int winningIndex = 0;
        for (int i = 2; i <= 4; ++i) {
            if (containsValidCard(pkTrick, i)) {
                int pkCard = card(pkTrick, i - 1);
                if (PackedCard.isBetter(trump, pkCard, winningCard)) {
                    winningCard = pkCard;
                    winningIndex = i - 1;
                }
            } else {
                return winningIndex;
            }
        }
        return winningIndex;
    }

    /**
     * @brief The Id of the player currently winning the given [packed] trick.
     *
     * @param pkTrick (int) - the given [packed] trick
     * @return (PlayerId) the Id of the player currently winning this [packed] trick
     */
    public static PlayerId winningPlayer(int pkTrick) {
        Card.Color trump = trump(pkTrick);

        return player(pkTrick, winningCardIndex(pkTrick, trump));
    }

    /**
     * @brief The textual representation of the given [packed] trick
     *
     * @param pkTrick (int) the given [packed] trick
     * @return (String) the textual representation of the given [packed] trick
     */
    public static String toString(int pkTrick) {
        String str1 = "";
        if (isValid(pkTrick)) {
            str1 = "started by " + player(pkTrick, 0) + "\n";
        }
        str1 += "trump " + Card.Color.ALL.get(Bits32.extract(pkTrick, TRUMP_START, TRUMP_SIZE)) + "\n";
        if (!isEmpty(pkTrick)) {
            str1 += "base color : " + baseColor(pkTrick) + "\n";
        }
        else {
            str1 += "base color : none\n";
        }

        if (isEmpty(pkTrick)) {
            return str1 + "{_,_,_,_}";
        }
        StringJoiner j = new StringJoiner(",", "{", "}");

        for (int i = 0 ; i < 4 ; ++i) {
            int pkCard = card(pkTrick, i);
            if (pkCard == PackedCard.INVALID) {
                j.add("_");
            }
            else {
                j.add(PackedCard.toString(pkCard));
            }
        }

        return str1 + j.toString();
    }
}
