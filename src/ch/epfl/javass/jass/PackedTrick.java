package ch.epfl.javass.jass;

public final class PackedTrick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final int INVALID = 0b11111111_11111111_11111111_11111111;

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

    public static Card.Color baseColor() {
        return null;
    }

    public static CardSet playableCards(CardSet hand) {
        return null;
    }

    public static int points(int pkTrick) {
        return 0;
    }

    public static PlayerId winningPlayer(int pkTrick) {
        return null;
    }
}
