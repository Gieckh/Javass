package ch.epfl.javass;

public final class Preconditions {
    private Preconditions() {}



    /**
     * (@brief)
     * @param b
     * @return none
     * @throws IllegalArgumentException if b is false
     * @author - Marin Nguyen (288260)
     */
    public static void
    checkArgument(boolean b) throws IllegalArgumentException {
        if (!b) {
            throw new IllegalArgumentException();
        }
        // ne fait rien sinon
    }

    /**
     *
     * @param index
     * @param size
     * @return
     * @throws IndexOutOfBoundsException if the index is negative, or
     *         if the index is greater (or equal) than the size
     * @author - Marin Nguyen (288260)
     */
    public static int
    checkIndex(int index, int size) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // S'il n'y a pas d'erreur
        return index;
    }
}
