package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits32;

public final class PackedTrick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final int INVALID = 0b11111111_11111111_11111111_11111111;

    private static final int CARD_SIZE = 6;
    private static final int CARD_1_START = 0;
    private static final int CARD_2_START = CARD_1_START + CARD_SIZE; //6
    private static final int CARD_3_START = CARD_2_START + CARD_SIZE; //12
    private static final int CARD_4_START = CARD_3_START + CARD_SIZE; //18
    private static final int ALL_CARDS_SIZE = 4 * CARD_SIZE;
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
    private static final int PLAYER_SHIFT = -1;

    private static final int TRUMP_START = PLAYER_START + PLAYER_SIZE; //30
    private static final int TRUMP_SIZE = 2;
    private static final int TRUMP_SHIFT = -1;

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

    //Here we suppose that an invalid card always is 111111
    public static boolean isValid2(int pkTrick) {
        boolean isRank1Invalid = (pkTrick & RANK_MASK_1) == RANK_MASK_1;
        boolean isRank2Invalid = (pkTrick & RANK_MASK_2) == RANK_MASK_2;
        boolean isRank3Invalid = (pkTrick & RANK_MASK_3) == RANK_MASK_3;
        boolean isRank4Invalid = (pkTrick & RANK_MASK_4) == RANK_MASK_4;

        if (isRank1Invalid)
            return isRank2Invalid && isRank3Invalid && isRank4Invalid;

        if (isRank2Invalid)
            return isRank3Invalid && isRank4Invalid;

        if (isRank3Invalid)
            return isRank4Invalid;

        return true;
    }

    public static int firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        int player = (firstPlayer.type + PLAYER_SHIFT) << PLAYER_START;
        int color  = (trump.type + TRUMP_SHIFT) << TRUMP_START;

        return EMPTY | player | color;
    }

    //TODO
    public static int nextEmpty(int pkTrick) {
        if (isLast(pkTrick)) {
            return INVALID;
        }

        //incrementing the index
        pkTrick += 1 << INDEX_START;

        //TODO better
        int winningPlayer = winningPlayer(pkTrick).type + PLAYER_SHIFT;
        pkTrick &= ~Bits32.mask(PLAYER_START, PLAYER_SIZE);
        pkTrick |= (winningPlayer << PLAYER_START);
        return pkTrick | EMPTY;
    }

    public static boolean isLast(int pkTrick) {
        return index(pkTrick) == MAX_INDEX;
    }

    public static boolean isEmpty (int pkTrick) {
        return (pkTrick & EMPTY) == EMPTY;
    }

// TODO : Antoine : du coup mon test fail
    //Assuming the card is valid
    public static boolean isFull(int pkTrick) {
        return Bits32.extract(pkTrick, CARD_4_START, CARD_SIZE) != PackedCard.INVALID;
    }

    //TODO: not a separate method, cuz it does a copy...
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

    //not using bits32 since it re-extracts.
    private static Card.Color pkColorToColor(int pkColor) {
        switch (pkColor) {
        case 0:
            return Card.Color.SPADE;
        case 1:
            return Card.Color.HEART;
        case 2:
            return Card.Color.DIAMOND;
        case 3:
            return Card.Color.CLUB;
        default:
            throw new IllegalArgumentException();
        }
    }
    public static Card.Color trump (int pkTrick) {
        int pkColor = Bits32.extract(pkTrick, TRUMP_START, TRUMP_SIZE);
        return pkColorToColor(pkColor);
    }

    //Where "index" ranges from 0 to 3
    private static PlayerId playerFromIndex(int playerIndex) {
        switch (playerIndex) {
            case 0:
                return PlayerId.PLAYER_1;
            case 1:
                return PlayerId.PLAYER_2;
            case 2:
                return PlayerId.PLAYER_3;
            case 3:
                return PlayerId.PLAYER_4;
            default: //unreachable statement (2 bits will always be between 0 and 4).
                throw new IllegalArgumentException();
        }
    }
    public static PlayerId player (int pkTrick, int index) {
        int firstPlayer = Bits32.extract(pkTrick, PLAYER_START, PLAYER_SIZE);
        return playerFromIndex((firstPlayer + index) % 4);
    }

    public static int index (int pkTrick) {
        return Bits32.extract(pkTrick, INDEX_START, INDEX_SIZE);
    }

    //assuming the card is indeed there.
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
    public static int withAddedCard(int pkTrick, int pkCard) {
        assert (isValid(pkTrick));
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

    public static Card.Color baseColor(int pkTrick) {
        assert (isValid(pkTrick));

        //cuz i dont want to re-extract.
        int firstCardColor = (pkTrick & 0b110000) >>> CARD_COLOR_START;
        return pkColorToColor(firstCardColor);
    }

    //Here we don't assume 4 cards have been played during this trick.
    //But we assume at least one has been
    private static int winningCard(int pkTrick, Card.Color trump) {
        assert (!isEmpty(pkTrick));

        int winningCard = pkTrick & CARD_MASK_1;
        for (int i = 2 ; i <= 4 ; ++i) {
            if (containsValidCard(pkTrick, i)) {
                int pkCard = card(pkTrick, i-1);
                if (PackedCard.isBetter(trump, winningCard, pkCard)) {
                    winningCard = pkCard;
                }
            }
            else {
                return winningCard;
            }
        }

        return winningCard;
    }

    //TODO: the hardest.
    //assumed not full
    //first implementation
    //easily simplifiable
    public static long playableCards(int pkTrick, long pkHand) {
        if ((pkTrick & CARD_MASK_1) == CARD_MASK_1) { //If you are the first player to play.
            return pkHand;
        }

        Card.Color trump = trump(pkTrick);
        Card.Color baseColor = baseColor(pkTrick);
        int winningCard = winningCard(pkTrick, trump);

        if (trump == baseColor) {
            long myTrumps = pkHand & PackedCardSet.subsetOfColor(pkHand, trump) {

            }
        }

        //trump != baseColor
        long playableNotTrump = PackedCardSet.subsetOfColor(pkHand, baseColor);
        boolean isBestTrump = PackedCard.color(winningCard) == trump;
        long trumpHand = (isBestTrump) ? (pkHand & PackedCardSet.trumpAbove(winningCard)) : 0L;

        if ((playableNotTrump | trumpHand) != PackedCardSet.EMPTY) {
            return pkHand | trumpHand;
        }

        long pkHandWOTrumps = PackedCardSet.difference(pkHand, PackedCardSet.subsetOfColor(pkHand, trump));
        //Only got trumps
        if (pkHandWOTrumps == PackedCardSet.EMPTY) {
            return pkHand;
        }

        return pkHandWOTrumps;
    }

    

    //only called with a valid, full trick
    public static int points(int pkTrick) {
        assert (isValid(pkTrick));
        assert (isFull(pkTrick));

        int total = (isLast(pkTrick)) ? 5 : 0;
        Card.Color trump = trump(pkTrick);
        total += PackedCard.points(trump, pkTrick & CARD_MASK_1);
        total += PackedCard.points(trump, (pkTrick & CARD_MASK_2) >>>CARD_2_START);
        total += PackedCard.points(trump, (pkTrick & CARD_MASK_3) >>>CARD_3_START);
        total += PackedCard.points(trump, (pkTrick & CARD_MASK_4) >>>CARD_4_START);

        return total;
    }


    //Here we don't assume 4 cards have been played during this trick.
    //But we assume at least one has been
    public static int winningCardIndex(int pkTrick, Card.Color trump) {
        assert (!isEmpty(pkTrick));

        int winningCard = pkTrick & CARD_MASK_1;
        int winningIndex = 0;
        for (int i = 2 ; i <= 4 ; ++i) {
            if (containsValidCard(pkTrick, i)) {
                int pkCard = card(pkTrick, i-1);
                if (PackedCard.isBetter(trump, pkCard, winningCard)) {
                    winningCard = pkCard;
                    winningIndex = i - 1;
                }
            }
            else {
                return winningIndex;
            }
        }

        return winningIndex;
    } //TODO private

    public static PlayerId winningPlayer(int pkTrick) {
        Card.Color trump = trump(pkTrick);

        return player(pkTrick, winningCardIndex(pkTrick, trump));
    }
}
