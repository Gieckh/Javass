package ch.epfl.javass;

public final class Preconditions {
    private Preconditions() {}
    // ...méthodes

    /**
     * (@brief)
     * @param b
     * @return none
     * @throws IllegalArgumentException if b is false
     */
    public static void checkArgument(boolean b) {
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
     */
    public static int checkIndex(int index, int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // S'il n'y a pas d'erreur
        return index;
    }
}
