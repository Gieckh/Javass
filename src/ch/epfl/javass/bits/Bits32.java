package ch.epfl.javass.bits;

import static ch.epfl.javass.Preconditions.checkArgument;

/**
 * Bits32
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class Bits32 {
    
    /** ============================================== **/
    /** ==============   CONSTRUCTEURS   ============= **/
    /** ============================================== **/
    
    private Bits32() {
        // Not to be instantiated
    }

    /** ============================================== **/
    /** ===============    METHODES    =============== **/
    /** ============================================== **/

    /**
     * @brief creates a mask of "size" 1 bits, starting at position "start"
     *        ex : mask(1, 3) = 0...01110
     *
     * @param start (int) the position of the first 1
     * @param size  (int) the length of the 1-sequence
     * @return (int) the mask
     */
    public static int mask(int start, int size)  {
        checkArgument(start >= 0  &&  size >=0);
        checkArgument(start + size <= Integer.SIZE);

        if (size == Integer.SIZE) // Because 1 << m is actually 1 << (m%32)
            return -1;

        int mask = (1 << size) - 1;

        return mask << start;
    }

    /**
     * @brief creates the int formed from the bits [start] to [start + size - 1] of "bits"
     *
     * @param bits  (int)
     * @param start (int)
     * @param size  (int)
     * @return (int) the int formed from the bits [start] to [start + size - 1] of "bits"
     */
    public static int extract(int bits, int start, int size) {
        // The call to mask checks the exceptions
        int mask = mask(start, size);
        return (mask & bits) >>> start;
    }



    /**
     * @brief finds the position of the MSB of the int value
     *
     * @see checkPack(int, int)
     * @param value the bit whose MSB position we want to know, NOT 0.
     * @return (int) the MSB position (an int between 0 and 31)
     *
     */
    // finds the msb position of an int, assuming there is one (i.e. value != 0)
    private static int msbPosition(int value) {
        return Integer.SIZE - 1 - Integer.numberOfLeadingZeros(value);
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
     */
    private static boolean checkPack(int value, int size) {
        if (size < 1 || size >= Integer.SIZE) { //Firstly, the size must be legal
            return false;
        }

        if (value == 0) {
            return true;
        }

        //For sure there is a MSB (bc value != 0)
        int msbPosition = msbPosition(value);

        return (msbPosition < size); // There mustn't be more bits than indicated by the "size"
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
     */
    public static int
    pack(int v1, int s1, int v2, int s2) {
        checkArgument(checkPack(v1, s1) && checkPack(v2, s2));
        checkArgument(s1 + s2 <= Integer.SIZE);

        return (v2 << s1) | v1;
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
     */
    public static int
    pack(int v1, int s1, int v2, int s2, int v3, int s3) {
        boolean compatibleSizes = checkPack(v1, s1) && checkPack(v2, s2) && checkPack(v3, s3);
        checkArgument(compatibleSizes);
        checkArgument(s1 + s2 +s3 <= Integer.SIZE);

        return (((v3 << s2) | v2) << s1) | v1;
    }

    /**
     * @brief concatenates the binary expressions of v1, v2, ... v6 and v7
     *        (where v7's bits are the most significant ones and v1's's the least)
     *
     * @see "pack(int v1, int s1, int v2, int s2)"
     * @return the number formed by the concatenation (in binary form) of all the "v_i", (where 1 <= i <= 7)
     * @throws IllegalArgumentException
     *
     */
    public static int
    pack(int v1, int s1, int v2, int s2, int v3, int s3, int v4, int s4,
         int v5, int s5, int v6, int s6, int v7, int s7){
        boolean compatibleSizes = checkPack(v1, s1) && checkPack(v2, s2) &&
                checkPack(v3, s3) && checkPack(v4, s4) &&
                checkPack(v5, s5) && checkPack(v6, s6) &&
                checkPack(v7, s7);

        checkArgument(compatibleSizes);
        checkArgument(s1 + s2 + s3 + s4 + s5 + s6 + s7 <= Integer.SIZE);

        // Same principle as before, but with more parenthesises.
        return (((((((((((v7 << s6) | v6) << s5) | v5) << s4) | v4) << s3) | v3) << s2) | v2) << s1) | v1;
    }
}
