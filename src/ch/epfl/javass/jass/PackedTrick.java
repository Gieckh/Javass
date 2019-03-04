package ch.epfl.javass.jass;

public final class PackedTrick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final int INVALID = 0b11111111_11111111_11111111_11111111;

    private static final int CARD_SIZE = 6;
    private static final int CARD_1_START = CARD_SIZE;
    private static final int CARD_2_START = CARD_1_START + CARD_1_START;


    private static final int PLAYER_SHIFT = -1;
    private static final int PLAYER_SIZE = 2;

    private static final int COLOR_SHIFT  = -1;
    private static final int COLOR_SIZE = 2;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    private PackedTrick() {

    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    public static boolean isValid(int pkTrick) {
        return false;
    }

    public static int firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        int player = firstPlayer.type + PLAYER_SHIFT;
        int color  = trump.type + COLOR_SHIFT;



        return 0;
    }

    public static int nextEmpty(int pkTrick) {
        return 0;
    }

    public static boolean isLast(int pkTrick) {
        return false;
    }

    public static boolean isEmpty (int pkTrick) {
        return false;
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
