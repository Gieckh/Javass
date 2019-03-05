package ch.epfl.javass.jass;

public class Trick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final int INVALID = PackedTrick.INVALID;
    private final int pkTrick;


    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    private Trick(int aTrick) {
        pkTrick = aTrick;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    public static Trick firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        return new Trick(PackedTrick.firstEmpty(trump, firstPlayer));
    }

    public static Trick ofPacked(int packed) {
        return new Trick(packed);
    }


    public int packed() {
        return pkTrick;
    }

    public Trick nextEmpty() {
        int nextEmpty = PackedTrick.nextEmpty(pkTrick);
        if (nextEmpty == INVALID) {
            throw new IllegalStateException();
        }

        return new Trick(nextEmpty);
    }

    public boolean isEmpty() {
        return PackedTrick.isEmpty(pkTrick);
    }

    public boolean isFull() {
        return PackedTrick.isFull(pkTrick);
    }

    public boolean isLast() {
        return PackedTrick.isLast(pkTrick);
    }

    public int size() {
        return PackedTrick.size(pkTrick);
    }
}
