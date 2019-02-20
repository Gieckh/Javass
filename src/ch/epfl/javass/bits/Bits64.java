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
    long mask(int start, int size) {
        checkArgument(start >= 0  &&  size >= 0);
        checkArgument(start + size <= Long.SIZE);
        //TODO: ...
        return (long) 0;
    }


    long extract(long bits, int start, int size) {
        //TODO: ...
        return (long) 0;
    }



    long pack(long v1, int s1, long v2, int s2) {
        //TODO: ...
        return (long) 0;
    }
}
