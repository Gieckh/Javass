package ch.epfl.javass.bits;

//TODO: Junit tests
//TODO: apprendre à utiliser les énumérations
public final class Bits32 {
    /** ============================================== **/
    /** ==============   CONSTRUCTEURS   ============= **/
    /** ============================================== **/
    private Bits32() {
    }

    /** ============================================== **/
    /** ===============    METHODES    =============== **/
    /** ============================================== **/

    /**
     * @brief creates a mask of "size" 1 bits, starting at position "start"
     * ex : mask(1, 3) = 0...01110
     * @param start (int) the position of the first 1
     * @param size  (int) the length of the 1-sequence
     * @return (int) the mask
     * @throws IllegalArgumentException
     *
     * @author - Marin Nguyen (288260)
     */
    public static int mask(int start, int size) throws IllegalArgumentException {
        if (start < 0 || size < 0) {
            throw new IllegalArgumentException();
        }
        if (start + size > Integer.SIZE) { //There are Integer.SIZE( == 32 bits)
            throw new IllegalArgumentException();
        }

        if (size == 0) { //Otherwise we'd have mask = 111...111 with the following formula
            return 0;
        }

        if (size == 32) { // Because 1 << 32 == 1, it goes back from the right hmmmm
            return -1;
        }

        int mask = (1 << size) - 1; // (theoretically) also works when size == 31 || size == 0

        return mask << start;
    }

    /**
     * @brief creates the int formed from the bits [start] to [start + size - 1] of "bits"
     * @param bits  (int)
     * @param start (int)
     * @param size  (int)
     * @return (int) the int formed from the bits [start] to [start + size - 1] of "bits"
     * @throws IllegalArgumentException
     *
     * @author - Marin Nguyen (288260)
     */
    public static int extract(int bits, int start, int size)
            throws IllegalArgumentException {
        // The call to mask checks the exceptions
        int mask = mask(start, size);
        return (mask & bits) >>> start;
    }



    /**
     * @brief finds the position of the MSB of the int value
     * @see packCheck
     * @param value the bit whose MSB position we want to know, NOT 0.
     * @return (int) the MSB position (an int between 0 and 31)
     *
     * @author - Marin Nguyen (288260)
     */
    // finds the msb position of an int, assuming there is one (i.e. value != 0)
    //TODO: can i do better ?
    private static int msbPosition(int value) {
        int mask = 1 << 31; // 1 followed by thirty 0
        int msbPos = 31;

        while ((mask & value)>>>msbPos != 1) {
            mask >>>= 1;
            msbPos--;
        }

        return msbPos;
    }

    /**
     * @brief checks whether "size" and "value" are compatible
     * @see pack
     * @param value
     * @param size
     * @return (boolean) true if
     *         - 1 <= size <= 31
     *         - and "value" doesn't use more bits than specified by "size"
     *
     * @author - Marin Nguyen (288260)
     */
    private static boolean packCheck(int value, int size) {
        if (size < 1 || size >= Integer.SIZE) {
            return false;
        }

        // first check whether there is a MSB
        if (value == 0) {
            return true;
        }

        int msbPosition = msbPosition(value);

        return (msbPosition < size); // the MSB of 2**size is the bit at the position (size + 1)
    }


    /**
     * @brief concatenates the binary expressions of v1 and v2 (where v2's bits are significant than v1's ones)
     * @param v1 (int) the first (int) to concatenate
     * @param s1 (int) the size of v1
     * @param v2 (int) the second (int) to concatenate
     * @param s2 (int) the size of v2
     * @return (int) the number formed by the concatenation of v1 and v2 (in binary)
     * @throws IllegalArgumentException
     *
     * @author - Marin Nguyen (288260)
     */
    public static int
    pack(int v1, int s1, int v2, int s2) throws IllegalArgumentException {
        if (!(packCheck(v1, s1) && packCheck(v2, s2))) {
            throw new IllegalArgumentException();
        }

        // Seems better (to us) to have a new if statement for that condition
        if (s1 + s2 > Integer.SIZE) {
            throw new IllegalArgumentException();
        }

        return (v2 << s1) + v1;
    }


    // I decided not to call "pack(int v1, int s1, int v2, int s2)" because
    // it would do many unnecessary size checks

    /**
     * @brief concatenates the binary expressions of v1, v2 and v3
     *        (where v3's bits are the most significant ones and v1's's the least)
     * @see "pack(int v1, int s1, int v2, int s2)"
     * @return the number formed by the concatenation (in binary form) of v1, v2 and v3
     * @throws IllegalArgumentException
     */
    public static int
    pack(int v1, int s1, int v2, int s2, int v3, int s3) throws IllegalArgumentException {
        boolean compatibleSizes = packCheck(v1, s1) && packCheck(v2, s2) && packCheck(v3, s3);
        if (!compatibleSizes) {
            throw new IllegalArgumentException();
        }

        if (s1 + s2 + s3 > Integer.SIZE) {
            throw new IllegalArgumentException();
        }

        return (((v3 << s2) + v2) << s1) + v1;
    }

    /**
     * @brief concatenates the binary expressions of v1, v2, ... v6 and v7
     *        (where v7's bits are the most significant ones and v1's's the least)
     * @see "pack(int v1, int s1, int v2, int s2)"
     * @return the number formed by the concatenation (in binary form) of all the "v_i", (where 1 <= i <= 7)
     * @throws IllegalArgumentException
     */
    public static int
    pack(int v1, int s1, int v2, int s2, int v3, int s3, int v4, int s4,
         int v5, int s5, int v6, int s6, int v7, int s7) throws IllegalArgumentException {
        boolean compatibleSizes = packCheck(v1, s1) && packCheck(v2, s2) &&
                                  packCheck(v3, s3) && packCheck(v4, s4) &&
                                  packCheck(v5, s5) && packCheck(v6, s6) &&
                                  packCheck(v7, s7);
        if (!compatibleSizes) {
            throw new IllegalArgumentException();
        }

        if (s1 + s2 + s3 + s4 + s5 + s6 + s7 > Integer.SIZE) {
            throw new IllegalArgumentException();
        }

        // Same principle as before, but with more parenthesises.
        return (((((((((((v7 << s6) + v6) << s5) + v5) << s4) + v4) << s3) + v3) << s2) + v2) << s1) + v1;
    }
}
