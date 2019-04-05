package ch.epfl.javass.bits;

import org.junit.jupiter.api.Test;

import java.util.SplittableRandom;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int[] s = getSizes(rng, 2);
            long[] v = getValues(rng, s);
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
   
        @Test
        void maskProducesCorrectMasks1() {
            for (int size = 0; size <= Long.SIZE; ++size) {
                for (int start = 0; start <= Long.SIZE - size; ++start) {
                    long m = Bits64.mask(start, size);
                    assertEquals(size, Long.bitCount(m));
                    assertEquals(size, Long.bitCount(m >>> start));
                    if (start + size < Long.SIZE)
                        assertEquals(0, m >>> (start + size));
                }
            }
        }

        @Test
        void maskFailsWithNegativeStart2() {
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
        void maskFailsWithNegativeSize3() {
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
        void maskFailsWithTooBigSize4() {
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
        void extractCanExtractAllBitsInOneGo5() {
            SplittableRandom rng = newRandom();
            for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
                long bits = rng.nextLong();
                assertEquals(bits, Bits64.extract(bits, 0, Long.SIZE));
            }
        }

        @Test
        void extractCanExtractSubgroupsOfBits6() {
            SplittableRandom rng = newRandom();
            for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
                long bits = rng.nextLong();
                for (int size = 1; size <= Long.SIZE; size *= 2) {
                    long reComputedBits = 0;
                    for (int start = Long.SIZE - size; start >= 0; start -= size)
                        reComputedBits = (reComputedBits << size) | Bits64.extract(bits, start, size);
                    assertEquals(bits, reComputedBits);
                }
            }
        }

        @Test
        void extractFailsWithNegativeStart7() {
            SplittableRandom rng = newRandom();
            for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
                int start = -(i + 1);
                int size = rng.nextInt(Integer.SIZE + 1);
                assertThrows(IllegalArgumentException.class, () -> {
                    Bits64.extract(rng.nextLong(), start, size);
                });
            }
        }

        @Test
        void extractFailsWithNegativeSize8() {
            SplittableRandom rng = newRandom();
            for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
                int start = rng.nextInt(Long.SIZE + 1);
                int size = -(i + 1);
                assertThrows(IllegalArgumentException.class, () -> {
                    Bits64.extract(rng.nextLong(), start, size);
                });
            }
        }

        @Test
        void extractFailsWithTooBigSize9() {
            SplittableRandom rng = newRandom();
            for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
                int start = rng.nextInt(Long.SIZE + 1);
                int size = Long.SIZE - start + rng.nextInt(1, 10);
                assertThrows(IllegalArgumentException.class, () -> {
                    Bits64.extract(rng.nextLong(), start, size);
                });
            }
        }

        private int[] getSizes10(SplittableRandom rng, int n) {
            int[] sizes = new int[n];
            int remainingBits = Long.SIZE;
            for (int i = 0; i < n; ++i) {
                sizes[i] = rng.nextInt(1, remainingBits - (n - 1 - i) + 1);
                remainingBits -= sizes[i];
            }
            return sizes;
        }

        private long[] getValues11(SplittableRandom rng, int[] sizes) {
            long[] values = new long[sizes.length];
            for (int i = 0; i < sizes.length; ++i) {
                long maxSize = 1L << sizes[i];
                values[i] = rng.nextLong(maxSize == Long.MIN_VALUE ? Long.MAX_VALUE : maxSize);
            }
            return values;
        }

        @Test
        void pack2Works12() {
            SplittableRandom rng = newRandom();
            for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
                int[] s = getSizes(rng, 2);
                long[] v = getValues(rng, s);
                long packed = Bits64.pack(v[0], s[0], v[1], s[1]);
                for (int j = 0; j < s.length; ++j) {
                  assertEquals(v[j], packed & ((1L << s[j]) - 1));
                  packed >>>= s[j];
                }
                assertEquals(0, packed);
            }
        }

        @Test
        void pack2FailsWithTooBigValues13() throws Exception {
            assertThrows(IllegalArgumentException.class, () -> {
                Bits64.pack(0b100, 2, 0b11, 2);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                Bits64.pack(0b100, 3, 0b11, 1);
            });
        }

        @Test
        void pack2FailsForTooManyBits14() throws Exception {
            assertThrows(IllegalArgumentException.class, () -> {
                Bits64.pack(0, 32, 0, 33);
            });
        }
    }