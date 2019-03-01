package ch.epfl.javass.bits;

import static ch.epfl.javass.Preconditions.checkArgument;

public class Bits64 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /**
     * ==============================================
     **/
    private Bits64() {
        // Not to be instantiated
    }
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @param start (int) the position of the first 1
     * @param size  (int) the length of the 1-sequence
     * @return (long) the mask
     * @throws IllegalArgumentException
     * @brief creates a mask of "size" 1 bits, starting at position "start"
     * ex : mask(1, 3) = 0...01110
     * @author - Marin Nguyen (288260)
     */
    public static long mask(int start, int size) {
        checkArgument(start >= 0 && size >= 0);
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
     * @param bits  (int)
     * @param start (int)
     * @param size  (int)
     * @return (int) the int formed from the bits [start] to [start + size - 1] of "bits"
     * @throws IllegalArgumentException
     * @brief creates the int formed from the bits [start] to [start + size - 1] of "bits"
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
    //TODO: can i do better ? -> log search, but more comparisons ?
    private static int msbPosition(long value) { //Or use method bitCount //TODO
        int msbPos = Long.SIZE - 1; // = 63
        long mask = 1L << msbPos; // 1 followed by sixty-three 0

        while ((mask & value) == 0) {
            mask >>>= 1;
            msbPos--;
        }

        return msbPos;
    }

    /**
     * @param value
     * @param size
     * @return (boolean) true if
     * - 1 <= size <= 31
     * - and "value" doesn't use more bits than specified by "size"
     * @brief checks whether "size" and "value" are compatible
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

        // If there is one, we find it.
        int msbPosition = msbPosition(value);

        return (msbPosition < size); // Secondly, there mustn't be more bits than indicated by the "size"
    }

    /**
     * @param v1 (int) the first (int) to concatenate
     * @param s1 (int) the size of v1
     * @param v2 (int) the second (int) to concatenate
     * @param s2 (int) the size of v2
     * @return (int) the number formed by the concatenation of v1 and v2 (in binary)
     * @throws IllegalArgumentException
     * @brief concatenates the binary expressions of v1 and v2 (where v2's bits are more significant than v1's ones)
     * @author - Marin Nguyen (288260)
     */
    public static long pack(long v1, int s1, long v2, int s2) {
        checkArgument(checkPack(v1, s1) && checkPack(v2, s2));

        // Seems better (to us) to have a new if statement for that condition
        checkArgument(s1 + s2 <= Long.SIZE);
        return (v2 << s1) | v1;
    }
}