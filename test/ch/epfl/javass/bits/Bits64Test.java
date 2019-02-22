package ch.epfl.javass.bits;

import org.junit.jupiter.api.Test;

import java.util.SplittableRandom;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//TODO: create test. Look at Bits32 Test for "inspiration". Study it.
//TODO: fix tests for packs
public class Bits64Test {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    @Test
    void maskProducesCorrectMasks() {
        for (int size = 0; size <= Long.SIZE; ++size) {
            for (int start = 0; start <= Long.SIZE - size; ++start) {
                long m = Bits64.mask(start, size);
                assertEquals(size, Long.bitCount(m));
                assertEquals(size, Long.bitCount(m >>> start));
                if (start + size < Long.SIZE)
                    assertEquals(0L, m >>> (start + size));
            }
        }
    }

    @Test
    void maskFailsWithNegativeStart() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int start = -(i + 1);
            int size = rng.nextInt(Long.SIZE + 1);
            assertThrows(IllegalArgumentException.class, () -> {
                Bits64.mask(start, size);
            });
        }
    }

    @Test
    void maskFailsWithNegativeSize() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int start = rng.nextInt(Long.SIZE + 1);
            int size = -(i + 1);
            assertThrows(IllegalArgumentException.class, () -> {
                Bits64.mask(start, size);
            });
        }
    }

    @Test
    void maskFailsWithTooBigSize() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int start = rng.nextInt(Long.SIZE + 1);
            int size = Long.SIZE - start + rng.nextInt(1, 10);
            assertThrows(IllegalArgumentException.class, () -> {
                Bits64.mask(start, size);
            });
        }
    }


    @Test
    void extractCanExtractAllBitsInOneGo() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long bits = rng.nextInt();
            assertEquals(bits, Bits64.extract(bits, 0, Long.SIZE));
        }
    }

    @Test
    void extractCanExtractSubgroupsOfBits() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long bits = rng.nextLong();
            for (int size = 1; size <= Long.SIZE; size *= 2) {
                long reComputedBits = 0L;
                for (int start = Long.SIZE - size; start >= 0; start -= size)
                    reComputedBits = (reComputedBits << size) | Bits64.extract(bits, start, size);
                assertEquals(bits, reComputedBits);
            }
        }
    }

    @Test
    void extractFailsWithNegativeStart() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int start = -(i + 1);
            int size = rng.nextInt(Long.SIZE + 1);
            assertThrows(IllegalArgumentException.class, () -> {
                Bits64.extract(rng.nextInt(), start, size);
            });
        }
    }

    @Test
    void extractFailsWithTooBigSize() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int start = rng.nextInt(Long.SIZE + 1);
            int size = Long.SIZE - start + rng.nextInt(1, 10);
            assertThrows(IllegalArgumentException.class, () -> {
                Bits64.extract(rng.nextLong(), start, size);
            });
        }
    }

    private int[] getSizes(SplittableRandom rng, int n) {
        int[] sizes = new int[n];
        int remainingBits = Long.SIZE;
        for (int i = 0; i < n; ++i) {
            sizes[i] = rng.nextInt(1, remainingBits - (n - 1 - i) + 1);
            remainingBits -= sizes[i];
        }
        return sizes;
    }

    private long[] getValues(SplittableRandom rng, int[] sizes) {
        long[] values = new long[sizes.length];
        for (int i = 0; i < sizes.length; ++i) {
            System.out.println("sizes[i] = " + sizes[i]); //TODO : suppr
            System.out.println("values[i] = " + (1L << sizes[i]));
            if (sizes[i] != 63)
                values[i] = rng.nextLong((1L << sizes[i]));
            else //(sizes[i] == 63)
                values[i] = rng.nextLong(1L << (62)); //TODO: the test may not rly be correct now
        }
        return values;
    }

    @Test
    void pack2Works() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) { //TODO: fail à la 173e itération de la boucle
            //            System.out.println("itération : " + i);
            int[] s = getSizes(rng, 2);
            long[] v = getValues(rng, s);
            //            System.out.println("v0 = " + v[0] + ", s0 = " + s[0]); //TODO: suppr
            //            System.out.println("v1 = " + v[1] + ", s1 = " + s[1]);
            System.out.println();
            long packed = Bits64.pack(v[0], s[0], v[1], s[1]);
            for (int j = 0; j < s.length; ++j) {
                assertEquals(v[j], packed & ((1L << s[j]) - 1));
                packed >>>= s[j];
            }
            assertEquals(0L, packed);
        }
    }

    @Test
    void pack2FailsWithTooBigValues() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            Bits64.pack(0b100L, 2, 0b11L, 2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Bits64.pack(0b100L, 3, 0b11L, 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Bits64.pack(0b10000L, 5, 0b11L, 1);
        });
    }

    @Test
    void pack2FailsForTooManyBits() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            Bits64.pack(0L, 32, 0L, 33);
        });
    }

//    @Test
//    void pack3Works() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            System.out.println("itération : " + i);
//            int[] s = getSizes(rng, 3);
//            long[] v = getValues(rng, s);
//            //Ca a l'air de marcher...
//            System.out.println("v0 = " + Long.toBinaryString(v[0]) + "( = " + v[0] + " )" + ", s0 = " + s[0]); //TODO: suppr
//            System.out.println("v1 = " + Long.toBinaryString(v[1]) + "( = " + v[1] + " )" + ", s1 = " + s[1]);
//            System.out.println("v2 = " + Long.toBinaryString(v[2]) + "( = " + v[2] + " )" + ", s2 = " + s[2]);
//            long packed = Bits64.pack(v[0], s[0], v[1], s[1], v[2], s[2]);
//            System.out.println("packed : " + Long.toBinaryString(packed));
//            for (int j = 0; j < s.length; ++j) {
//                System.out.println("j iteration = " + j);
//                System.out.println(Long.toBinaryString((1 << s[j]) - 1));
//                assertEquals(v[j], packed & ((1L << s[j]) - 1));
//                packed >>>= s[j];
//            }
//            assertEquals(0, packed);
//        }
//    }
//
//    @Test
//    void pack3FailsWithTooBigValues() throws Exception {
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b100L, 2, 0b11L, 2, 0b1111L, 4);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b100L, 3, 0b11L, 1, 0b1111L, 4);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b100L, 3, 0b11L, 2, 0b1111L, 3);
//        });
//    }
//
//    @Test
//    void pack3FailsForTooManyBits() throws Exception {
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0L, 22, 0L, 22, 0L, 22); //66 bits total
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0L, 22, 0L, 22, 0L, 21); //65 bits total
//        });
//    }
//
//    @Test
//    void pack7Works() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int[] s = getSizes(rng, 7);
//            long[] v = getValues(rng, s);
//            long packed = Bits64.pack(v[0], s[0], v[1], s[1], v[2], s[2], v[3], s[3], v[4], s[4], v[5], s[5], v[6], s[6]);
//            for (int j = 0; j < s.length; ++j) {
//                assertEquals(v[j], packed & ((1L << s[j]) - 1));
//                packed >>>= s[j];
//            }
//            assertEquals(0, packed);
//        }
//    }
//
//    @Test
//    void pack7FailsWithTooBigValues() throws Exception {
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b10L, 1, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b10L, 2, 0b10L, 1, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b10L, 2, 0b10L, 2, 0b10L, 1, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 1, 0b10L, 2, 0b10L, 2, 0b10L, 2);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 1, 0b10L, 2, 0b10L, 2);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 1, 0b10L, 2);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 2, 0b10L, 1);
//        });
//    }
//
//    @Test
//    void pack7FailsForTooManyBits() throws Exception {
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0L, 8, 0L, 8, 0L, 10, 0L, 10, 0L, 10, 0L, 10, 0L, 10); //66 bits total
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            Bits64.pack(0L, 8, 0L, 8, 0L, 10, 0L, 10, 0L, 10, 0L, 10, 0L, 9 ); //65 bits total
//        });
//    }
}
