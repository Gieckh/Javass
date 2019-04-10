package ch.epfl.javass.jass;

import java.util.StringJoiner;

import ch.epfl.javass.bits.Bits32;
import ch.epfl.javass.jass.Card.Color;

/**
 * @brief manipulates the packed representations of the tricks of a game.
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
     * @param pkTrick
     * @return true if "pkTrick" encodes a valid trick
     */
    public static boolean isValid(int pkTrick) {
        if (Bits32.extract(pkTrick, INDEX_START, INDEX_SIZE) > MAX_INDEX) {
            return false;
        }

        int masked1 = pkTrick & CARD_MASK_1;
        int masked2 = pkTrick & CARD_MASK_2;
        int masked3 = pkTrick & CARD_MASK_3;
        int masked4 = pkTrick & CARD_MASK_4;


        //TODO: simplify
        //We don't check whether we have the same card 2 times.
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
        // TODO : 111111 pour le rank 4 retourne true
        // test : 111111_000000_000000_000000 fait retourner true
        return (isRank4Valid || masked4 == CARD_MASK_4);
    }

    /**
     * @brief returns the first PackedTrick of a turn,
     *        depending on the trumpColor and the starting player.
     *
     * @param trump the Color of the trump
     * @param firstPlayer the Id of the fist Player to play
     * @return the first PackedTrick of a turn
     */
    public static int firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        int player = (firstPlayer.ordinal()) << PLAYER_START;
        int color  = trump.ordinal() << TRUMP_START;

        return EMPTY | player | color;
    }

    //TODO

    /**
     * @brief returns the n+1th PackedTrick of a turn
     * return invalid if the last trick was the 9th.
     *
     * @param pkTrick
     * @return the n+1th PackedTrick of a trick
     */
    public static int nextEmpty(int pkTrick) {
        if (isLast(pkTrick)) {
            return INVALID;
        }

        //incrementing the index
        pkTrick += 1 << INDEX_START;
        //TODO better
        int winningPlayer = winningPlayer(pkTrick).ordinal();
        pkTrick &= ~Bits32.mask(PLAYER_START, PLAYER_SIZE);
        pkTrick |= (winningPlayer << PLAYER_START);
        return pkTrick | EMPTY;
    }

    /**
     * @brief returns true iff this trick is the 9th.
     *
     * @param pkTrick
     * @return a boolean that is true iff this trick is the 9th
     */
    public static boolean isLast(int pkTrick) {
        return index(pkTrick) == MAX_INDEX;
    }

    /**
     * @brief returns true iff this trick is empty : if it has no card.
     *
     * @param pkTrick
     * @return a boolean that is true iff this trick has 0 card
     */
    public static boolean isEmpty (int pkTrick) {
        return (pkTrick & EMPTY) == EMPTY;
    }

    //Assuming the card is valid

    /**
     * @brief returns true iff this trick is full : it has 4 cards
     *
     * @param pkTrick
     * @return boolean that is true iff this trick has 4 card
     */
    public static boolean isFull(int pkTrick) {
        return Bits32.extract(pkTrick, CARD_4_START, CARD_SIZE) != PackedCard.INVALID;
    }

    //TODO: not a separate method, cuz it does a copy...

    /**
     * @brief returns true iff this trick is full : if it has 4 cards.
     *
     * @param pkTrick
     * @return a boolean that is true iff this trick has 4 card
     */
    private static boolean containsValidCard(int pkTrick, int cardNo) {
        switch(cardNo) {
        case 1:
            return ((RANK_MASK_1 & pkTrick) != RANK_MASK_1);
        case 2:
            return (((RANK_MASK_2 & pkTrick)) != RANK_MASK_2);
        case 3:
            return (((RANK_MASK_3 & pkTrick)) != RANK_MASK_3);
        case 4:
            return (((RANK_MASK_4 & pkTrick)) != RANK_MASK_4);
        default:
            throw new IllegalArgumentException();
        }
    }

    //TODO: better?

    /**
     * @brief returns the number of cards in the pkTrick.
     *
     * @param pkTrick a trick
     * @return the number of cards in pkTrick
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
     * @brief returns the trump color in the pkTrick.
     *
     * @param pkTrick a trick
     * @return the color of the trump in pkTrick
     */
    public static Card.Color trump (int pkTrick) {
        int pkColor = Bits32.extract(pkTrick, TRUMP_START, TRUMP_SIZE);
        return Color.ALL.get(pkColor);
    }

    /**
     * @brief returns the player that will play at the index'th position .
     *
     * @param pkTrick
     * @param index
     * @return playerId of the index'th player to play at this trick
     */
    public static PlayerId player (int pkTrick, int index) {
        int firstPlayer = Bits32.extract(pkTrick, PLAYER_START, PLAYER_SIZE);
        return PlayerId.ALL.get((firstPlayer + index) % PlayerId.COUNT);
    }

    /**
     * @brief returns the number of this trick.
     *
     * @param pkTrick
     * @return int from 0 to 8 
     */
    public static int index (int pkTrick) {
        return Bits32.extract(pkTrick, INDEX_START, INDEX_SIZE);
    }


    //assuming the card is indeed there.

    /**
     * @brief returns the Card at the index'th position (from 0 to 3).
     *
     * @param pkTrick
     * @param index
     * @return the card at the index'th position
     */
    public static int card (int pkTrick, int index) {
        assert (isValid(pkTrick));
        assert (0 <= index  &&  index <= 3);
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
        default:
            throw new IllegalArgumentException();
        }
    }

    //assuming not full.

    /**
     * @brief returns a new pkTrick whose difference is that a card has been added.
     *
     * @param pkTrick
     * @param pkCard
     * @return a packedTrick with a card added
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
     * @brief returns the base color of this trick.
     *
     * @param pkTrick
     * @return the color of the 1st card
     */
    public static Card.Color baseColor(int pkTrick) {
        assert (isValid(pkTrick));

        //cuz i dont want to re-extract.
        //TODO: when its empty, we get CLUB
        int firstCardColor = (pkTrick & 0b110000) >>> CARD_COLOR_START;
        return Color.ALL.get(firstCardColor);
    }


    //assumed not full
    //first implementation
    //easily simplifiable

    /**
     * @brief returns all playable cards from a set of Cards ( pkHand)
     *  in the current trick case.
     *
     * @param pkTrick this trick 
     * @param pkHand the set with all the cards you have
     * @return a PackedCardSet with only the cards you had ,and you can play in current case
     */
    public static long playableCards(int pkTrick, long pkHand) {
        assert(!isFull(pkTrick));

        //TODO: retest
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
     * @brief returns the points of this trick.
     *
     * @param pkTrick
     * @return int : the sum of all cards in this trick
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
    // cuz it will reduce chain errors and also enable our program to be a bit faster.

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
     * @brief returns the Id of the player winning this trick.
     *
     * @param pkTrick
     * @return PlayerId : the Id of the player which have played the best card in this trick
     */
    public static PlayerId winningPlayer(int pkTrick) {
        Card.Color trump = trump(pkTrick);

        return player(pkTrick, winningCardIndex(pkTrick, trump));
    }

    /**
     * @brief returns a String representation of the trick.
     *
     * @param pkTrick
     * @return String : a string with all informations about this trick
     */
    public static String toString(int pkTrick) {//TODO: Test again
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
