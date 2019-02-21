package ch.epfl.javass.bits;

import static ch.epfl.javass.Preconditions.checkArgument;

public class Bits64 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    private Bits64() {
        // Not to be instantiated
    }
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @brief creates a mask of "size" 1 bits, starting at position "start"
     *        ex : mask(1, 3) = 0...01110
     *
     * @param start (int) the position of the first 1
     * @param size  (int) the length of the 1-sequence
     * @return (long) the mask
     * @throws IllegalArgumentException
     *
     * @author - Marin Nguyen (288260)
     */
    public static long mask(int start, int size) { //TODO: tester
        checkArgument(start >= 0  &&  size >= 0);
        checkArgument(start + size <= Long.SIZE);

        if (size == 0) {
            return 0L;
        }

        if (size == Long.SIZE) {
            return -1L;  // = 111...111 with Long.SIZE = 64 ones.
        }

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
     * @throws IllegalArgumentException
     *
     * @author - Marin Nguyen (288260)
     */
    public static long
    extract(long bits, int start, int size) throws IllegalArgumentException {
        // The call to mask checks the exceptions
        long mask = mask(start, size);
        return (mask & bits) >>> start;
    }





    /**
     * @brief finds the position of the MSB of the long value
     *
     * @see checkPack(int, int)
     * @param value the bit whose MSB position we want to know, NOT 0.
     * @return (int) the MSB position (an int between 0 and 31)
     *
     * @author - Marin Nguyen (288260)
     */
    //TODO: can i do better ? -> log search, but more comparisons ?
    private static int msbPosition(long value) {
        int msbPos = Long.SIZE - 1; // = 63
        long mask = 1L << msbPos; // 1 followed by sixty-three 0

        while ((mask & value) >>> msbPos != 1) {
            mask >>>= 1;
            msbPos--;
        }

        return msbPos;
    }

    /**
     * @brief checks whether "size" and "value" are compatible
     *
     * @see pack(int, int, int, int)
     * @param value
     * @param size
     * @return (boolean) true if
     *         - 1 <= size <= 31
     *         - and "value" doesn't use more bits than specified by "size"
     *
     * @author - Marin Nguyen (288260)
     */
    private static boolean checkPack(long value, int size) {
        if (size < 1 || size >= Long.SIZE) { //Firstly, the size must be legal
            return false;
        }

        // first check whether there is a MSB
        if (value == 0L) {
            return true;
        }

        // If there is one, we find it.
        int msbPosition = msbPosition(value);

        return (msbPosition < size); // Secondly, there mustn't be more bits than indicated by the "size"
    }


    /**
     * @brief concatenates the binary expressions of v1 and v2 (where v2's bits are more significant than v1's ones)
     *
     * @param v1 (int) the first (int) to concatenate
     * @param s1 (int) the size of v1
     * @param v2 (int) the second (int) to concatenate
     * @param s2 (int) the size of v2
     * @return (int) the number formed by the concatenation of v1 and v2 (in binary)
     * @throws IllegalArgumentException
     *
     * @author - Marin Nguyen (288260)
     */
    public static long
    pack(long v1, int s1, long v2, int s2) throws IllegalArgumentException {
        checkArgument(checkPack(v1, s1) && checkPack(v2, s2));

        // Seems better (to us) to have a new if statement for that condition
        checkArgument(s1 + s2 <= Long.SIZE);
        return (v2 << s1) + v1;
    }


    // I decided not to call "pack(int v1, int s1, int v2, int s2)" because
    // it would do many unnecessary size checks

    /**
     * @brief concatenates the binary expressions of v1, v2 and v3
     *        (where v3's bits are the most significant ones and v1's's the least)
     *
     * @see "pack(int v1, int s1, int v2, int s2)"
     * @return the number formed by the concatenation (in binary form) of v1, v2 and v3
     * @throws IllegalArgumentException
     *
     * @author - Marin Nguyen (288260)
     */
    public static long
    pack(long v1, int s1, long v2, int s2, long v3, int s3) throws IllegalArgumentException {
        boolean compatibleSizes = checkPack(v1, s1) && checkPack(v2, s2) && checkPack(v3, s3);
        checkArgument(compatibleSizes);
        checkArgument(s1 + s2 +s3 <= Long.SIZE);

        return (((v3 << s2) + v2) << s1) + v1;
    }

    /**
     * @brief concatenates the binary expressions of v1, v2, ... v6 and v7
     *        (where v7's bits are the most significant ones and v1's's the least)
     *
     * @see "pack(int v1, int s1, int v2, int s2)"
     * @return the number formed by the concatenation (in binary form) of all the "v_i", (where 1 <= i <= 7)
     * @throws IllegalArgumentException
     *
     * @author - Marin Nguyen (288260)
     */
    public static long
    pack(long v1, int s1, long v2, int s2, long v3, int s3, long v4, int s4,
            long v5, int s5, long v6, int s6, long v7, int s7) throws IllegalArgumentException {
        boolean compatibleSizes = checkPack(v1, s1) && checkPack(v2, s2) &&
                checkPack(v3, s3) && checkPack(v4, s4) &&
                checkPack(v5, s5) && checkPack(v6, s6) &&
                checkPack(v7, s7);

        checkArgument(compatibleSizes);
        checkArgument(s1 + s2 + s3 + s4 + s5 + s6 + s7 <= Long.SIZE);

        // Same principle as before, but with more parenthesises.
        return (((((((((((v7 << s6) + v6) << s5) + v5) << s4) + v4) << s3) + v3) << s2) + v2) << s1) + v1;
    }
}
