package ch.epfl.javass;


/**
 * Preconditions
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class Preconditions {
    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    // private so we can't instantiate
    private Preconditions() {
        // empty
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/


    /**
     * @brief checks whether "b" (boolean) is true. If not, throws an exception.
     *
     * @param b (boolean) - the condition that needs to be verified.
     * @throws IllegalArgumentException if b is false
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
     * @param index (int) - the index to be checked.
     * @param size (int) - the size of the array the index lives in.
     * @return (int) - the index if there is no problem.
     * @throws IndexOutOfBoundsException if the index is negative, or
     *         if the index is greater than (or equal to) the size
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
