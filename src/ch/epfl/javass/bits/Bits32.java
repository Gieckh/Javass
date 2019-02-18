package ch.epfl.javass.bits;

public class Bits32 {
    /** ============================================== **/
    /** ==============   CONSTRUCTEURS   ============= **/
    /** ============================================== **/
    private Bits32() {}


    /** ============================================== **/
    /** ===============    METHODES    =============== **/
    /** ============================================== **/

    /**
     * @brief creates a mask of "size" 1 bits, starting at position "start"
     *        ex : mask(1, 3) = 0...01110
     * @param start (int) the position of the first 1
     * @param size (int) the length of the 1-sequence
     * @return (int) the mask
     * @throws IllegalArgumentException
     * @author - Marin Nguyen (288260)
     */
    public static int
    mask (int start, int size) throws IllegalArgumentException {
        if (start < 0 || size < 0) {
            throw new IllegalArgumentException();
        }
        if (start + size >= Integer.SIZE) { //The bits are numbered from 0 to Integer.SIZE - 1 (== 31)
            throw new IllegalArgumentException();
        }
        int mask = 1 << (size) - 1; // (theoretically) also works when size == 31 || size == 0

        return mask << start;
    }

    /**
     * @brief creates the int formed from the bits [start] to [start + size - 1] of "bits"
     * @param bits (int)
     * @param start (int)
     * @param size (int)
     * @return (int) the int formed from the bits [start] to [start + size - 1] of "bits"
     * @throws IllegalArgumentException
     * @author - Marin Nguyen (288260)
     */
    public static int
    extract (int bits, int start, int size)  throws IllegalArgumentException {
        int mask = mask(start, size);
        return (mask & bits) >>> size;
    }

    /**
     * @brief concatenates the binary expressions of v1 and v2 (where v2's bits are significant than v1's ones)
     * @param v1 (int) the first (int) to concatenate
     * @param s1 (int) the size of v1
     * @param v2 (int) the second (int) to concatenate
     * @param s2 (int) the size of v2
     * @return (int) the number formed by the concatenation of v1 and v2
     * @throws IllegalArgumentException
     * @author - Marin Nguyen (288260)
     */
    public static int
    pack(int v1, int s1, int v2, int s2) throws IllegalArgumentException {
        if (!packCheck(v1, s1)  ||  !packCheck(v2, s2)) {
            throw new IllegalArgumentException();
        }

        // Seems better (to us) to have a new if statement for that condition
        if (s1 + s2 > Integer.SIZE) {
            throw new IllegalArgumentException();
        }

        return (s2 << v1) + s1;
    }

    /**
     * checks whether "size" and "value" are compatible
     * @param value
     * @param size
     * @return (boolean) true if
     *         - 1 <= size <= 31
     *         - and "value" doesn't use more bits than specified by "size"
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
     * @brief finds the position of the MSB of the int value
     * @param value the bit whose MSB position we want to know, NOT 0.
     * @return (int) the MSB position (an int between 0 and 31)
     * @author - Marin Nguyen (288260)
     */
    // finds the msb position of an int, assuming there is one (i.e. value != 0)
    private static int msbPosition (int value) {
        int mask = 1 << 30;
        int msbPos = 31;

        while ((mask & value) != 1) {
            msbPos--;
        }

        return msbPos;
    }
}
