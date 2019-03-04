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

    public static boolean isValid(int pkTrick) {
        if (Bits32.extract(pkTrick, INDEX_START, INDEX_SIZE) > MAX_INDEX) {
            return false;
        }

        boolean isRank1Valid = ((pkTrick & RANK_MASK_1) >>> CARD_1_START) <= MAX_RANK;
        boolean isRank2Valid = ((pkTrick & RANK_MASK_2) >>> CARD_2_START) <= MAX_RANK;
        boolean isRank3Valid = ((pkTrick & RANK_MASK_3) >>> CARD_3_START) <= MAX_RANK;
        boolean isRank4Valid = ((pkTrick & RANK_MASK_4) >>> CARD_4_START) <= MAX_RANK;

        if (!isRank1Valid) {
            return !isRank2Valid && !isRank3Valid && !isRank4Valid;
        }

        if (!isRank2Valid) {
            return !isRank3Valid && !isRank4Valid;
        }

        if (!isRank3Valid) {
            return !isRank4Valid;
        }

        return true;
    }

    public static int firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        int player = (firstPlayer.type + PLAYER_SHIFT) << PLAYER_START;
        int color  = (trump.type + TRUMP_SHIFT) << TRUMP_START;

        return EMPTY | player | color;
    }

    public static int nextEmpty(int pkTrick) {
        int winner;


        return 0;
    }

    public static boolean isLast(int pkTrick) {
        return Bits32.extract(pkTrick, INDEX_START, INDEX_SIZE) == MAX_INDEX;
    }

    public static boolean isEmpty (int pkTrick) {
        return (pkTrick & ALL_CARDS_SIZE) == EMPTY;
    }

    public static boolean isFull(int pkTrick) {
        return false;
    }

    public static int size(int pkTrick) {
        return 0;
    }

    public static Card.Color trump (int pkTrick) {
        return null;
    }

    public static PlayerId player (int pkTrick) {
        return null;
    }

    public static int index (int pkTrick) {
        return 0;
    }

    public static int card (int pkTrick, int pkCard) {
        return 0;
    }

    public static int withAddedCard(int pkTrick, int pkCard) {
        return 0;
    }

    public static Card.Color baseColor(int pkTrick) {
        return null;
    }

    public static int points(int pkTrick) {
        return 0;
    }

    public static PlayerId winningPlayer(int pkTrick) {
        return null;
    }
}
