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
     * @param start
     * @param size
     * @return
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
     * @param bits
     * @param start
     * @param size
     * @return
     * @throws IllegalArgumentException
     * @author - Marin Nguyen (288260)
     */
    public static int
    extract (int bits, int start, int size)  throws IllegalArgumentException {
        int mask = mask(start, size);
        return (mask & bits) >>> size;
    }

    /**
     * @param v1
     * @param s1
     * @param v2
     * @param s2
     * @return
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
     * indicates whether
     * @param value
     * @param size
     * @return (boolean) true if the size is be
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
     * finds the position of the MSB of the int value
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
