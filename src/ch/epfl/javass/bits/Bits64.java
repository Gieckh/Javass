package ch.epfl.javass.bits;

import static ch.epfl.javass.Preconditions.checkArgument;

//TODO: javadoc

/**
 * @brief Non-instantiable, this class contains the methods that will be used to
 *        manipulate vectors of 64 bits, here represented by (long) values.
 *
 * @author - Marin Nguyen (288260)
 * @author - Antoine Scardigli (299905)
 */
public class Bits64 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    // Private so we can't instantiate.
    private Bits64() {
        //empty
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     *
     * @brief creates a mask of "size" 1 bits, starting at position "start"
     *        ex : mask(1, 3) = 0...01110
     *
     * @param start (int) the position of the first 1
     * @param size  (int) the length of the 1-sequence
     * @return (long) the mask
     */
    public static long mask(int start, int size) {
        checkArgument(start >= 0 && size >= 0);
        checkArgument(start + size <= Long.SIZE);

        if (size == Long.SIZE)
            return -1L;  // = 111...111 with Long.SIZE = 64 ones.

        long mask = (1L << size) - 1L;

        return mask << start;
    }

    /**
     * @brief creates the int formed from the bits [start] to [start + size - 1] of "bits"
     *
     * @param bits  (int)
     * @param start (int)
     * @param size  (int)
     * @return (int) the int formed from the bits [start] to [start + size - 1] of "bits"
     * @author - Marin Nguyen (288260)
     */
    public static long extract(long bits, int start, int size) {
        // The call to mask checks the exceptions
        long mask = mask(start, size);
        return (mask & bits) >>> start;
    }

    /**
     * @param value the bit whose MSB position we want to know, NOT 0.
     * @return (int) the MSB position (an int between 0 and 31)
     * @brief finds the position of the MSB of the long value
     * @author - Marin Nguyen (288260)
     * @see checkPack(int, int)
     */
    private static int msbPosition(long value) {
        return Long.SIZE - 1 - Long.numberOfLeadingZeros(value);
    }


    /**
     * @brief checks whether "size" and "value" are compatible
     *
     * @param value (long)
     * @param size (int)
     * @return (boolean) true if
     *         - 1 <= size <= 31
     *         - and "value" doesn't use more bits than specified by "size"
     * @author - Marin Nguyen (288260)
     * @see pack(int, int, int, int)
     */
    private static boolean checkPack(long value, int size) {
        if (size < 1 || size >= Long.SIZE) { //Firstly, the size must be legal
            return false;
        }

        // first check whether there is a MSB
        if (value == 0L) {
            return true;
        }

        //For sure there is a MSB (bc value != 0L)
        int msbPosition = msbPosition(value);

        return (msbPosition < size); // Second, check there aren't more bits in "value" than indicated by "size"
    }

    /**
     * @brief concatenates the binary expressions of v1 and v2
     *       (where v2's bits are more significant than v1's ones)
     *
     * @param v1 (int) - the first (int) to concatenate
     * @param s1 (int) - the size of v1
     * @param v2 (int) - the second (int) to concatenate
     * @param s2 (int) - the size of v2
     * @return (int) - the number formed by the concatenation of v1 and v2 (in binary)
     * @throws IllegalArgumentException if:
     *         - size is not in {0, 1, ..., 31 = Integer.SIZE - 1}
     *         - or one (at least) of the values v_i occupies more bits than specified
     *           by the associated size s_i
     */
    public static long pack(long v1, int s1, long v2, int s2) {
        checkArgument(checkPack(v1, s1) && checkPack(v2, s2));
        checkArgument(s1 + s2 <= Long.SIZE);

        return (v2 << s1) | v1;
    }
}