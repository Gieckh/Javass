package ch.epfl.javass;

public final class Preconditions {
    private Preconditions() {}

    /**
     * @brief checks whether "b" (boolean) is true. If not, throws an exception.
     *
     * @param b (boolean)
     * @throws IllegalArgumentException if b is false
     *
     * @author - Marin Nguyen (288260)
     */
    public static void
    checkArgument(boolean b) throws IllegalArgumentException {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @brief checks whether the index "index" fits in the array of size "size"
     *
     * @param index (int), the index to be checked.
     * @param size (int), the size of the array the index lives in.
     * @return the index if there is no problem.
     * @throws IndexOutOfBoundsException if the index is negative, or
     *         if the index is greater than (or equal to) the size
     *
     * @author - Marin Nguyen (288260)
     */
    public static int
    checkIndex(int index, int size) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // if there is no error
        return index;
    }
}
