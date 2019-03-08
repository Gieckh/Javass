package ch.epfl.javass.jass;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static ch.epfl.test.TestRandomizer.newRandom;
import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;
import ch.epfl.javass.bits.Bits64;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.javass.jass.Card.Color;

interface PackedCardSetTestValid {
    void test(Card[] lc, int size, long validCardSet);
}



class PackedCardSetTest {
    private static Card.Color[] getAllColors() {
        return new Card.Color[] {
                Card.Color.SPADE,
                Card.Color.HEART,
                Card.Color.DIAMOND,
                Card.Color.CLUB
        };
    }

    private static Card.Rank[] getAllRanks() {
        return new Card.Rank[] {
                Card.Rank.SIX,
                Card.Rank.SEVEN,
                Card.Rank.EIGHT,
                Card.Rank.NINE,
                Card.Rank.TEN,
                Card.Rank.JACK,
                Card.Rank.QUEEN,
                Card.Rank.KING,
                Card.Rank.ACE,
        };
    }

    private static Card.Rank[] getAllRanksTrumpOrdered() {
        return new Card.Rank[] {
                Card.Rank.SIX,
                Card.Rank.SEVEN,
                Card.Rank.EIGHT,
                Card.Rank.TEN,
                Card.Rank.QUEEN,
                Card.Rank.KING,
                Card.Rank.ACE,
                Card.Rank.NINE,
                Card.Rank.JACK,
        };
    }


    @Test
    void isValidReturnsTrueWithSomeValidCombinations() {
        long pkCardSet = PackedCardSet.EMPTY;
        for (Card.Color color : getAllColors()) {
            for (Card.Rank rank : getAllRanks()) {
                assertTrue(PackedCardSet.isValid(pkCardSet));

                // The add is there cuz we test with the empty set.
                int pkCard = PackedCard.pack(color, rank);
                PackedCardSet.add(pkCardSet, pkCard);
            }
        }

        //Test with the last card.
        assertTrue(PackedCardSet.isValid(pkCardSet));
    }

    @Test
    void isValidReturnsFalseWithSomeInvalidCombinations() {
        long pkCardSet = PackedCardSet.EMPTY;
        for (Card.Color color : getAllColors()) {
            for (Card.Rank rank : getAllRanks()) {
                for (int start = 0 ; start < 64 ; start += 16) { //MAX start = 48
                    int mask = 1 << (start + 9); //The empty bits start at "start + 9)
                    for (int i = 9 ; i < 16 ; ++i) {
                        assertFalse(PackedCardSet.isValid(pkCardSet | mask));
                        mask <<= 1;
                    }
                }
                // We put that after cuz we test the empty set
                int pkCard = PackedCard.pack(color, rank);
                PackedCardSet.add(pkCardSet, pkCard);
            }
        }

        // Test with the last card
        for (int start = 0 ; start < 64 ; start += 16) { //MAX start = 48
            int mask = 1 << (start + 9); //The empty bits start at "start + 9)
            for (int i = 9 ; i < 16 ; ++i) {
                assertFalse(PackedCardSet.isValid(pkCardSet | mask));
                mask <<= 1;
            }
        }
    }


    @Test
    void isEmptyWorksInSomeCases() {
        assertTrue(PackedCardSet.isEmpty(PackedCardSet.EMPTY));
        long maxRngLong = (1L << 63) - 1L;

        SplittableRandom rng = new SplittableRandom();
        for (int i = 0 ; i < RANDOM_ITERATIONS ; ++i) {
            long packedCardSet = rng.nextLong(1, maxRngLong);
            assertFalse(PackedCardSet.isEmpty(packedCardSet));
        }

        long minLong = 1L << 63;
        for (int i = 0 ; i < RANDOM_ITERATIONS ; ++i) {
            long packedCardSet = rng.nextLong(1, maxRngLong);
            assertFalse(PackedCardSet.isEmpty(packedCardSet | minLong));
        }
    }


    @Test
    void addDoesAddWithValidCards() { //First test
        Card.Color[] colors = getAllColors();
        Card.Rank[]   ranks = getAllRanks();
        long packedCardSet = PackedCardSet.EMPTY;
        for (int j = 0 ; j < colors.length ; ++j) {
            for (int i = 0; i < ranks.length; ++i) {
                int pkCard = PackedCard.pack(colors[j], ranks[i]);
                packedCardSet = PackedCardSet.add(packedCardSet, pkCard);
                long mask = 1L << (i + (16 * j));
                assertEquals(mask, packedCardSet & mask);
            }
        }
    }

    @Test
    void removeWorksWithSomeCorrectInputs() {
        long pkCardSet = PackedCardSet.ALL_CARDS;
        for (Card.Color c : getAllColors()) {
            for (Card.Rank r : getAllRanks()) {
                int pkCard = PackedCard.pack(c, r);

                assertTrue(PackedCardSet.contains(pkCardSet, pkCard));
                pkCardSet = PackedCardSet.remove(pkCardSet, pkCard);

                System.out.println();

                assertFalse(PackedCardSet.contains(pkCardSet, pkCard));
            }
        }
    }

    @Test
    void getWorksWithSomeCorrectInputs() {
        Card.Color[] colors = getAllColors();
        Card.Rank[] ranks = getAllRanks();
        int[] allCardsFromTheBeginning = new int[4 * 9];
        long packedCardSet = PackedCardSet.EMPTY;
        int size = 0;
        for (int j = 0 ; j < colors.length ; ++j) {
            for (int i = 0 ; i < ranks.length ; ++i) {
                int packedCard = PackedCard.pack(colors[j], ranks[i]);
                packedCardSet = PackedCardSet.add(packedCardSet, packedCard);

                allCardsFromTheBeginning[i + 9*j] = packedCard;
                ++size;

                for (int k = 0 ; k < size ; ++k) {
                    assertEquals(allCardsFromTheBeginning[k],
                                 PackedCardSet.get(packedCardSet, k));
                }
            }
        }
    }

    @Test
    void getWorksWithSomeOtherCorrectInputs() {
        Card.Color[] colors = getAllColors();
        Card.Rank[] ranks = getAllRanks();
        int[] allCardsFromTheBeginning = new int[4 * 9];
        long packedCardSet = PackedCardSet.EMPTY;
        int size = 0;
        for (int j = 0 ; j < colors.length ; ++j) {
            System.out.println("j = " + j);
            for (int i = 0 ; i < ranks.length ; i += 3) {
                int packedCard = PackedCard.pack(colors[j], ranks[i]);
                packedCardSet = PackedCardSet.add(packedCardSet, packedCard);

                allCardsFromTheBeginning[i + 9*j] = packedCard;
                ++size;

                for (int k = 0 ; k < 3 * size ; k += 3) {
                    assertEquals(allCardsFromTheBeginning[k],
                            PackedCardSet.get(packedCardSet, k / 3));
                }
            }
        }
    }

    @Test
    void getFailsWithNegativeIndex() {
        Card.Color[] colors = getAllColors();
        Card.Rank[] ranks = getAllRanks();
        SplittableRandom rng = new SplittableRandom();
        int[] allCardsFromTheBeginning = new int[4 * 9];
        long packedCardSet = PackedCardSet.EMPTY;
        int size = 0;
        for (int j = 0 ; j < colors.length ; ++j) {
            System.out.println("j = " + j);
            for (int i = 0 ; i < ranks.length ; ++i) {
                int packedCard = PackedCard.pack(colors[j], ranks[i]);
                packedCardSet = PackedCardSet.add(packedCardSet, packedCard);

                allCardsFromTheBeginning[i + 9*j] = packedCard;
                ++size;

                long copy = packedCardSet; //"should be final or effectively final" blah blah blah
                assertThrows(AssertionError.class, () -> {
                    PackedCardSet.get(copy, -rng.nextInt(1, Integer.MAX_VALUE));
                });

                assertThrows(AssertionError.class, () -> {
                    PackedCardSet.get(copy, -1);
                });
            }
        }
    }

    @Test
    void getFailsWithTooBigIndex() {
        Card.Color[] colors = getAllColors();
        Card.Rank[] ranks = getAllRanks();

        SplittableRandom rng = new SplittableRandom();

        int[] allCardsFromTheBeginning = new int[4 * 9];
        long packedCardSet = PackedCardSet.EMPTY;

        int size = 0;
        for (int j = 0 ; j < colors.length ; ++j) {
            System.out.println("j = " + j);
            for (int i = 0 ; i < ranks.length ; ++i) {
                int packedCard = PackedCard.pack(colors[j], ranks[i]);
                packedCardSet = PackedCardSet.add(packedCardSet, packedCard);

                allCardsFromTheBeginning[i + 9*j] = packedCard;
                ++size;

                long copyOfSet = packedCardSet; //"should be final or effectively final" blah blah blah
                int copyOfSize = size;          //"should be final or effectively final" blah blah blah
                assertThrows(AssertionError.class, () -> {
                    PackedCardSet.get(copyOfSet, -rng.nextInt(copyOfSize, Integer.MAX_VALUE));
                });

                assertThrows(AssertionError.class, () -> {
                    PackedCardSet.get(copyOfSet, copyOfSize);
                });
            }
        }
    }
    @Test
    void isValidWorksForAllValidSet() {
        for (int i = 1; i < 512; ++i) {
            for (int j = 1; j < 512; ++j) {
                for (int k = 1; k < 512; ++k) {
                    for (int l = 1; l < 512; ++l) {

                        assertTrue(PackedCardSet.isValid(i | j << 16 | k << 32 | l << 48));
                    }
                }
            }
        }
    }

    @Test //TODO
    void trumpAboveWorks() {
        Card.Color[] colors = getAllColors();
        Card.Rank[] ranks = getAllRanks();
        for (int c = 0; c < 4; ++c) {
            for (int r = 0; r < 9; ++r) {
                long cards = PackedCardSet.trumpAbove(c << 4 | r);
                for (int i = 0; i < PackedCardSet.size(cards); ++i) {
                    assertTrue(PackedCard.isBetter(colors[c], PackedCardSet.get(cards, i), c << 4 | r));
                }
            }
        }
    }

    @Test
    void singletonWorksForAllValidCards() {
        for (int c = 0; c < 4; ++c) {
            for (int r = 0; r < 9; ++r) {
                int pkCard = c << 4 | r;
                long singleton = PackedCardSet.singleton(pkCard);
                assertEquals(1L << c * 16 + r, singleton);

            }
        }

    }

    @Test
    void isEmptyWorks() {
        assertTrue(PackedCardSet.isEmpty(0L));
    }

    @Test
    void size() {
    }

    @Test
    void getWorks() {
        long l = PackedCardSet.ALL_CARDS;
        for (int c = 0; c < 4; ++c) {
            for (int r = 0; r < 9; ++r) {
                assertEquals(c << 4 | r, PackedCardSet.get(l, c * 9 + r));
            }
        }
    }


    @Test
    void containsWorks() {
    }
/*// does work 
   @Test
   void subsetOfColorWorks() {
        for (int c = 0; c < 4; ++c) {
            for (int i = 1; i < 512; ++i) {
                
                for (int j = 1; j < 512; ++j) {
                    for (int k = 1; k < 512; ++k) {
                        for (int l = 1; l < 512; ++l) {
                            long cardSet = i | j << 16 | k << 32 | l << 48;
                            assertEquals(cardSet & Bits64.mask(c * 16, 9), PackedCardSet.subsetOfColor(cardSet, Card.Color.values()[c]));
                        }
                    }
                }
            }
        }
    }
*/
//    @Test
//    void toStringWorks() { //TODO ???
//        long s = PackedCardSet.EMPTY;
//        int c1 = PackedCard.pack(Card.Color.HEART, Card.Rank.SIX);
//        int c2 = PackedCard.pack(Card.Color.SPADE, Card.Rank.ACE);
//        int c3 = PackedCard.pack(Card.Color.SPADE, Card.Rank.SIX);
//        s = PackedCardSet.add(s, c1);
//        s = PackedCardSet.add(s, c2);
//        s = PackedCardSet.add(s, c3);
//        assertEquals("{â™ 6,â™ A,â™¡6}", PackedCardSet.toString(s));
//    }
    long mask = 0b0000000_111111111_0000000_111111111_0000000_111111111_0000000_111111111L;
    int COLOR_SIZE = 16;

    @Test
    void PackedCardSetConstantsAreCorrect() {
        assertEquals(mask, PackedCardSet.ALL_CARDS);
        assertEquals(0L, PackedCardSet.EMPTY);
    }

    @Test
    void isValidWorksForSomeValidCardSet() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i)
            assertTrue(PackedCardSet.isValid(rng.nextLong() & mask));
    }

    @Test
    void isValidWorksForSomeInvalidCardSet() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long invalidCardSet;
            do
                invalidCardSet = rng.nextLong();
            while((invalidCardSet & mask) == invalidCardSet);
            assertFalse(PackedCardSet.isValid(invalidCardSet));
        }
    }

    Map<Integer, Long> trumpAboveList = new HashMap<Integer, Long>() {
        private static final long serialVersionUID = 1L;
        {
            put(0b00_0000, 0b111111110L); put(0b01_0000, 0b111111110_0000000000000000L); put(0b10_0000, 0b111111110_0000000000000000_0000000000000000L); put(0b11_0000, 0b111111110_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0001, 0b111111100L); put(0b01_0001, 0b111111100_0000000000000000L); put(0b10_0001, 0b111111100_0000000000000000_0000000000000000L); put(0b11_0001, 0b111111100_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0010, 0b111111000L); put(0b01_0010, 0b111111000_0000000000000000L); put(0b10_0010, 0b111111000_0000000000000000_0000000000000000L); put(0b11_0010, 0b111111000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0011, 0b000100000L); put(0b01_0011, 0b000100000_0000000000000000L); put(0b10_0011, 0b000100000_0000000000000000_0000000000000000L); put(0b11_0011, 0b000100000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0100, 0b111101000L); put(0b01_0100, 0b111101000_0000000000000000L); put(0b10_0100, 0b111101000_0000000000000000_0000000000000000L); put(0b11_0100, 0b111101000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0101, 0b000000000L); put(0b01_0101, 0b000000000_0000000000000000L); put(0b10_0101, 0b000000000_0000000000000000_0000000000000000L); put(0b11_0101, 0b000000000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0110, 0b110101000L); put(0b01_0110, 0b110101000_0000000000000000L); put(0b10_0110, 0b110101000_0000000000000000_0000000000000000L); put(0b11_0110, 0b110101000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0111, 0b100101000L); put(0b01_0111, 0b100101000_0000000000000000L); put(0b10_0111, 0b100101000_0000000000000000_0000000000000000L); put(0b11_0111, 0b100101000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_1000, 0b000101000L); put(0b01_1000, 0b000101000_0000000000000000L); put(0b10_1000, 0b000101000_0000000000000000_0000000000000000L); put(0b11_1000, 0b000101000_0000000000000000_0000000000000000_0000000000000000L); 
        }
    };

    @Test
    void trumpAboveWorksForAllValidCard() {
        for(int pkCard : trumpAboveList.keySet())
            assertEquals((long)trumpAboveList.get(pkCard),  PackedCardSet.trumpAbove(pkCard));
    }

    Map<Integer, Long> singletonList = new HashMap<Integer, Long>() {
        private static final long serialVersionUID = 1L;
        {
            put(0b00_0000, 0b000000001L); put(0b01_0000, 0b000000001_0000000000000000L); put(0b10_0000, 0b000000001_0000000000000000_0000000000000000L); put(0b11_0000, 0b000000001_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0001, 0b000000010L); put(0b01_0001, 0b000000010_0000000000000000L); put(0b10_0001, 0b000000010_0000000000000000_0000000000000000L); put(0b11_0001, 0b000000010_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0010, 0b000000100L); put(0b01_0010, 0b000000100_0000000000000000L); put(0b10_0010, 0b000000100_0000000000000000_0000000000000000L); put(0b11_0010, 0b000000100_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0011, 0b000001000L); put(0b01_0011, 0b000001000_0000000000000000L); put(0b10_0011, 0b000001000_0000000000000000_0000000000000000L); put(0b11_0011, 0b000001000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0100, 0b000010000L); put(0b01_0100, 0b000010000_0000000000000000L); put(0b10_0100, 0b000010000_0000000000000000_0000000000000000L); put(0b11_0100, 0b000010000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0101, 0b000100000L); put(0b01_0101, 0b000100000_0000000000000000L); put(0b10_0101, 0b000100000_0000000000000000_0000000000000000L); put(0b11_0101, 0b000100000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0110, 0b001000000L); put(0b01_0110, 0b001000000_0000000000000000L); put(0b10_0110, 0b001000000_0000000000000000_0000000000000000L); put(0b11_0110, 0b001000000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_0111, 0b010000000L); put(0b01_0111, 0b010000000_0000000000000000L); put(0b10_0111, 0b010000000_0000000000000000_0000000000000000L); put(0b11_0111, 0b010000000_0000000000000000_0000000000000000_0000000000000000L); 
            put(0b00_1000, 0b100000000L); put(0b01_1000, 0b100000000_0000000000000000L); put(0b10_1000, 0b100000000_0000000000000000_0000000000000000L); put(0b11_1000, 0b100000000_0000000000000000_0000000000000000_0000000000000000L); 
        }
    };

    @Test
    void singletonWorksForAllValidCard() {
        for(int pkCard : singletonList.keySet())
            assertEquals((long)singletonList.get(pkCard),  PackedCardSet.singleton(pkCard));
    }

    @Test
    void isEmptyWorksValidCardSet () {
        assertTrue(PackedCardSet.isEmpty(0L));
    }

    @Test
    void isEmptyWorksForSomeInvalidCardSet () {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet;
            do pkCardSet = rng.nextLong() & mask;
            while (pkCardSet == 0L);
            assertFalse(PackedCardSet.isEmpty(pkCardSet));
        }
    }

    @Test
    void sizeWorksForSomeValidCardSet() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet = rng.nextLong() & mask;
            assertEquals(Long.bitCount(pkCardSet), PackedCardSet.size(pkCardSet));
        }
    }

    @Test
    void getWorks1() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet = rng.nextLong() & mask;
            long pkCardSetRes = 0L;
            int size = Long.bitCount(pkCardSet);
            for(int j = 0; j < size; ++j)
                pkCardSetRes |= PackedCardSet.singleton(PackedCardSet.get(pkCardSet, j));
            assertEquals(pkCardSet, pkCardSetRes);
        }
    }

    @Test
    void addWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet = rng.nextLong() & mask;
            for(int pkCard : singletonList.keySet())
                assertEquals(pkCardSet | (long)singletonList.get(pkCard), PackedCardSet.add(pkCardSet, pkCard));
        }
    }

    @Test
    void removeWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet = rng.nextLong() & mask;
            for(int pkCard : singletonList.keySet())
                assertEquals(pkCardSet & ~(long)singletonList.get(pkCard), PackedCardSet.remove(pkCardSet, pkCard));
        }
    }

    @Test
    void containsWorks1() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet = rng.nextLong() & mask;
            for(int pkCard : singletonList.keySet())
                assertEquals((pkCardSet | (long)singletonList.get(pkCard)) == pkCardSet, PackedCardSet.contains(pkCardSet, pkCard));
        }
    }

    @Test
    void complementWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet = rng.nextLong() & mask;
            long pkCardSetRes = ~pkCardSet & mask;
            assertEquals(pkCardSetRes, PackedCardSet.complement(pkCardSet));
        }
    }

    @Test
    void unionWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet1 = rng.nextLong() & mask;
            long pkCardSet2 = rng.nextLong() & mask;
            long pkCardSetRes = pkCardSet1 | pkCardSet2;
            assertEquals(pkCardSetRes, PackedCardSet.union(pkCardSet1, pkCardSet2));
        }
    }

    @Test
    void intersectionWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet1 = rng.nextLong() & mask;
            long pkCardSet2 = rng.nextLong() & mask;
            long pkCardSetRes = pkCardSet1 & pkCardSet2;
            assertEquals(pkCardSetRes, PackedCardSet.intersection(pkCardSet1, pkCardSet2));
        }
    }

    @Test
    void differenceWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet1 = rng.nextLong() & mask;
            long pkCardSet2 = rng.nextLong() & mask;
            long pkCardSetRes = pkCardSet1 & ~pkCardSet2;
            assertEquals(pkCardSetRes, PackedCardSet.difference(pkCardSet1, pkCardSet2));
        }
    }

    @Test
    void subsetOfColorWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            long pkCardSet = rng.nextLong() & mask;
            long pkCardSetRes =
                    PackedCardSet.subsetOfColor(pkCardSet, Color.SPADE) |
                    PackedCardSet.subsetOfColor(pkCardSet, Color.HEART) |
                    PackedCardSet.subsetOfColor(pkCardSet, Color.DIAMOND) |
                    PackedCardSet.subsetOfColor(pkCardSet, Color.CLUB);
            assertEquals(pkCardSet, pkCardSetRes);
        }
    }

    Map<Long, String> toStringList = new HashMap<Long, String>() {
        private static final long serialVersionUID = 1L;
        {
            put(0b0000000000000000_0000000000000000_0000000000000000_0000000000000000L, "{}"); 
            put(0b0000000111111111_0000000111111111_0000000111111111_0000000111111111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000000001000_0000000100100101_0000000110010110_0000000110000101L, "{\u26606,\u26608,\u2660K,\u2660A,\u26617,\u26618,\u266110,\u2661K,\u2661A,\u26626,\u26628,\u2662J,\u2662A,\u26639}"); 
            put(0b0000000011110001_0000000101011010_0000000111000001_0000000000101000L, "{\u26609,\u2660J,\u26616,\u2661Q,\u2661K,\u2661A,\u26627,\u26629,\u266210,\u2662Q,\u2662A,\u26636,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000101010011_0000000001100011_0000000010100100_0000000001100100L, "{\u26608,\u2660J,\u2660Q,\u26618,\u2661J,\u2661K,\u26626,\u26627,\u2662J,\u2662Q,\u26636,\u26637,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000100110101_0000000110110110_0000000100000011_0000000111110001L, "{\u26606,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u2661A,\u26627,\u26628,\u266210,\u2662J,\u2662K,\u2662A,\u26636,\u26638,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000011100010_0000000110100101_0000000011001110_0000000100011100L, "{\u26608,\u26609,\u266010,\u2660A,\u26617,\u26618,\u26619,\u2661Q,\u2661K,\u26626,\u26628,\u2662J,\u2662K,\u2662A,\u26637,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000111010110_0000000011000001_0000000011111000_0000000001100010L, "{\u26607,\u2660J,\u2660Q,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u26626,\u2662Q,\u2662K,\u26637,\u26638,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110011010_0000000011001000_0000000000001011_0000000110111100L, "{\u26608,\u26609,\u266010,\u2660J,\u2660K,\u2660A,\u26616,\u26617,\u26619,\u26629,\u2662Q,\u2662K,\u26637,\u26639,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000111111110_0000000110001011_0000000001000110_0000000111000110L, "{\u26607,\u26608,\u2660Q,\u2660K,\u2660A,\u26617,\u26618,\u2661Q,\u26626,\u26627,\u26629,\u2662K,\u2662A,\u26637,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000000110011_0000000011110110_0000000000110100_0000000111000000L, "{\u2660Q,\u2660K,\u2660A,\u26618,\u266110,\u2661J,\u26627,\u26628,\u266210,\u2662J,\u2662Q,\u2662K,\u26636,\u26637,\u266310,\u2663J}"); 
            put(0b0000000110111101_0000000011011110_0000000000111101_0000000111111110L, "{\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26618,\u26619,\u266110,\u2661J,\u26627,\u26628,\u26629,\u266210,\u2662Q,\u2662K,\u26636,\u26638,\u26639,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000100110110_0000000011101101_0000000011011001_0000000110000100L, "{\u26608,\u2660K,\u2660A,\u26616,\u26619,\u266110,\u2661Q,\u2661K,\u26626,\u26628,\u26629,\u2662J,\u2662Q,\u2662K,\u26637,\u26638,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000111011100_0000000111010111_0000000000000111_0000000110110100L, "{\u26608,\u266010,\u2660J,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u26626,\u26627,\u26628,\u266210,\u2662Q,\u2662K,\u2662A,\u26638,\u26639,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000001111001_0000000011010110_0000000011111010_0000000101110111L, "{\u26606,\u26607,\u26608,\u266010,\u2660J,\u2660Q,\u2660A,\u26617,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u26627,\u26628,\u266210,\u2662Q,\u2662K,\u26636,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000011110001_0000000100000110_0000000101001010_0000000110010111L, "{\u26606,\u26607,\u26608,\u266010,\u2660K,\u2660A,\u26617,\u26619,\u2661Q,\u2661A,\u26627,\u26628,\u2662A,\u26636,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000011001011_0000000101000111_0000000000001001_0000000010010110L, "{\u26607,\u26608,\u266010,\u2660K,\u26616,\u26619,\u26626,\u26627,\u26628,\u2662Q,\u2662A,\u26636,\u26637,\u26639,\u2663Q,\u2663K}"); 
            put(0b0000000010011101_0000000101010101_0000000111000010_0000000001111110L, "{\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u26617,\u2661Q,\u2661K,\u2661A,\u26626,\u26628,\u266210,\u2662Q,\u2662A,\u26636,\u26638,\u26639,\u266310,\u2663K}"); 
            put(0b0000000111101100_0000000101111011_0000000101000000_0000000101110010L, "{\u26607,\u266010,\u2660J,\u2660Q,\u2660A,\u2661Q,\u2661A,\u26626,\u26627,\u26629,\u266210,\u2662J,\u2662Q,\u2662A,\u26638,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000001011011_0000000011000000_0000000000110010_0000000001001011L, "{\u26606,\u26607,\u26609,\u2660Q,\u26617,\u266110,\u2661J,\u2662Q,\u2662K,\u26636,\u26637,\u26639,\u266310,\u2663Q}"); 
            put(0b0000000111101100_0000000000010000_0000000001010010_0000000101011100L, "{\u26608,\u26609,\u266010,\u2660Q,\u2660A,\u26617,\u266110,\u2661Q,\u266210,\u26638,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000111110000_0000000101110001_0000000110011000_0000000101000011L, "{\u26606,\u26607,\u2660Q,\u2660A,\u26619,\u266110,\u2661K,\u2661A,\u26626,\u266210,\u2662J,\u2662Q,\u2662A,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110011111_0000000101111101_0000000001100001_0000000110100011L, "{\u26606,\u26607,\u2660J,\u2660K,\u2660A,\u26616,\u2661J,\u2661Q,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000010100110_0000000110111100_0000000101011111_0000000010101110L, "{\u26607,\u26608,\u26609,\u2660J,\u2660K,\u26616,\u26617,\u26618,\u26619,\u266110,\u2661Q,\u2661A,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u26637,\u26638,\u2663J,\u2663K}"); 
            put(0b0000000111110111_0000000101000110_0000000101001100_0000000101111001L, "{\u26606,\u26609,\u266010,\u2660J,\u2660Q,\u2660A,\u26618,\u26619,\u2661Q,\u2661A,\u26627,\u26628,\u2662Q,\u2662A,\u26636,\u26637,\u26638,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000101100110_0000000011011000_0000000111100100_0000000000001011L, "{\u26606,\u26607,\u26609,\u26618,\u2661J,\u2661Q,\u2661K,\u2661A,\u26629,\u266210,\u2662Q,\u2662K,\u26637,\u26638,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000110000011_0000000000001100_0000000101001010_0000000110100011L, "{\u26606,\u26607,\u2660J,\u2660K,\u2660A,\u26617,\u26619,\u2661Q,\u2661A,\u26628,\u26629,\u26636,\u26637,\u2663K,\u2663A}"); 
            put(0b0000000011010001_0000000000111100_0000000001100010_0000000000011011L, "{\u26606,\u26607,\u26609,\u266010,\u26617,\u2661J,\u2661Q,\u26628,\u26629,\u266210,\u2662J,\u26636,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000011000101_0000000001000101_0000000010101110_0000000110001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u2661J,\u2661K,\u26626,\u26628,\u2662Q,\u26636,\u26638,\u2663Q,\u2663K}"); 
            put(0b0000000110100010_0000000110100101_0000000100010001_0000000111010100L, "{\u26608,\u266010,\u2660Q,\u2660K,\u2660A,\u26616,\u266110,\u2661A,\u26626,\u26628,\u2662J,\u2662K,\u2662A,\u26637,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000101111001_0000000100010010_0000000111010100_0000000000010100L, "{\u26608,\u266010,\u26618,\u266110,\u2661Q,\u2661K,\u2661A,\u26627,\u266210,\u2662A,\u26636,\u26639,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000110000001_0000000100010100_0000000100110101_0000000001001100L, "{\u26608,\u26609,\u2660Q,\u26616,\u26618,\u266110,\u2661J,\u2661A,\u26628,\u266210,\u2662A,\u26636,\u2663K,\u2663A}"); 
            put(0b0000000100111101_0000000111111001_0000000110110001_0000000110100100L, "{\u26608,\u2660J,\u2660K,\u2660A,\u26616,\u266110,\u2661J,\u2661K,\u2661A,\u26626,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26638,\u26639,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000001111100_0000000000000101_0000000111001011_0000000001001000L, "{\u26609,\u2660Q,\u26616,\u26617,\u26619,\u2661Q,\u2661K,\u2661A,\u26626,\u26628,\u26638,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000100000110_0000000011000000_0000000000111101_0000000011000100L, "{\u26608,\u2660Q,\u2660K,\u26616,\u26618,\u26619,\u266110,\u2661J,\u2662Q,\u2662K,\u26637,\u26638,\u2663A}"); 
            put(0b0000000101000100_0000000100011100_0000000010011101_0000000001111001L, "{\u26606,\u26609,\u266010,\u2660J,\u2660Q,\u26616,\u26618,\u26619,\u266110,\u2661K,\u26628,\u26629,\u266210,\u2662A,\u26638,\u2663Q,\u2663A}"); 
            put(0b0000000101000100_0000000100000000_0000000010100111_0000000110011100L, "{\u26608,\u26609,\u266010,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u2661J,\u2661K,\u2662A,\u26638,\u2663Q,\u2663A}"); 
            put(0b0000000000110001_0000000101001010_0000000001001110_0000000000101111L, "{\u26606,\u26607,\u26608,\u26609,\u2660J,\u26617,\u26618,\u26619,\u2661Q,\u26627,\u26629,\u2662Q,\u2662A,\u26636,\u266310,\u2663J}"); 
            put(0b0000000011101001_0000000011000011_0000000000010000_0000000001110111L, "{\u26606,\u26607,\u26608,\u266010,\u2660J,\u2660Q,\u266110,\u26626,\u26627,\u2662Q,\u2662K,\u26636,\u26639,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000101010011_0000000111101001_0000000111111001_0000000100001000L, "{\u26609,\u2660A,\u26616,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000100001001_0000000111101010_0000000000011101_0000000111011110L, "{\u26607,\u26608,\u26609,\u266010,\u2660Q,\u2660K,\u2660A,\u26616,\u26618,\u26619,\u266110,\u26627,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26639,\u2663A}"); 
            put(0b0000000101100110_0000000111000011_0000000100110101_0000000111011110L, "{\u26607,\u26608,\u26609,\u266010,\u2660Q,\u2660K,\u2660A,\u26616,\u26618,\u266110,\u2661J,\u2661A,\u26626,\u26627,\u2662Q,\u2662K,\u2662A,\u26637,\u26638,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000000001110_0000000000110110_0000000110110001_0000000100110101L, "{\u26606,\u26608,\u266010,\u2660J,\u2660A,\u26616,\u266110,\u2661J,\u2661K,\u2661A,\u26627,\u26628,\u266210,\u2662J,\u26637,\u26638,\u26639}"); 
            put(0b0000000001001010_0000000010001110_0000000110100001_0000000111110101L, "{\u26606,\u26608,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u2661J,\u2661K,\u2661A,\u26627,\u26628,\u26629,\u2662K,\u26637,\u26639,\u2663Q}"); 
            put(0b0000000010100111_0000000110010101_0000000000101101_0000000110011000L, "{\u26609,\u266010,\u2660K,\u2660A,\u26616,\u26618,\u26619,\u2661J,\u26626,\u26628,\u266210,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u2663J,\u2663K}"); 
            put(0b0000000000000010_0000000010000010_0000000011001110_0000000110101101L, "{\u26606,\u26608,\u26609,\u2660J,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u2661Q,\u2661K,\u26627,\u2662K,\u26637}"); 
            put(0b0000000001010111_0000000010010110_0000000011101011_0000000011101010L, "{\u26607,\u26609,\u2660J,\u2660Q,\u2660K,\u26616,\u26617,\u26619,\u2661J,\u2661Q,\u2661K,\u26627,\u26628,\u266210,\u2662K,\u26636,\u26637,\u26638,\u266310,\u2663Q}"); 
            put(0b0000000001000101_0000000110010100_0000000100000001_0000000010100001L, "{\u26606,\u2660J,\u2660K,\u26616,\u2661A,\u26628,\u266210,\u2662K,\u2662A,\u26636,\u26638,\u2663Q}"); 
            put(0b0000000000111111_0000000110100111_0000000001000100_0000000100011001L, "{\u26606,\u26609,\u266010,\u2660A,\u26618,\u2661Q,\u26626,\u26627,\u26628,\u2662J,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663J}"); 
            put(0b0000000011111001_0000000010010011_0000000001000100_0000000100010110L, "{\u26607,\u26608,\u266010,\u2660A,\u26618,\u2661Q,\u26626,\u26627,\u266210,\u2662K,\u26636,\u26639,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000000000011_0000000100000000_0000000110101110_0000000111000001L, "{\u26606,\u2660Q,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u2661J,\u2661K,\u2661A,\u2662A,\u26636,\u26637}"); 
            put(0b0000000111000011_0000000101110000_0000000001110000_0000000011101001L, "{\u26606,\u26609,\u2660J,\u2660Q,\u2660K,\u266110,\u2661J,\u2661Q,\u266210,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110001011_0000000001101010_0000000000101010_0000000100011111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660A,\u26617,\u26619,\u2661J,\u26627,\u26629,\u2662J,\u2662Q,\u26636,\u26637,\u26639,\u2663K,\u2663A}"); 
            put(0b0000000111001100_0000000001010000_0000000011100001_0000000011101011L, "{\u26606,\u26607,\u26609,\u2660J,\u2660Q,\u2660K,\u26616,\u2661J,\u2661Q,\u2661K,\u266210,\u2662Q,\u26638,\u26639,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110001100_0000000111010101_0000000000001000_0000000010101011L, "{\u26606,\u26607,\u26609,\u2660J,\u2660K,\u26619,\u26626,\u26628,\u266210,\u2662Q,\u2662K,\u2662A,\u26638,\u26639,\u2663K,\u2663A}"); 
            put(0b0000000001111000_0000000001101111_0000000001100001_0000000110001110L, "{\u26607,\u26608,\u26609,\u2660K,\u2660A,\u26616,\u2661J,\u2661Q,\u26626,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000101010101_0000000001001000_0000000001100011_0000000110000111L, "{\u26606,\u26607,\u26608,\u2660K,\u2660A,\u26616,\u26617,\u2661J,\u2661Q,\u26629,\u2662Q,\u26636,\u26638,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000110011100_0000000110100101_0000000110110011_0000000111000100L, "{\u26608,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u266110,\u2661J,\u2661K,\u2661A,\u26626,\u26628,\u2662J,\u2662K,\u2662A,\u26638,\u26639,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000111100100_0000000011110000_0000000101010111_0000000010011100L, "{\u26608,\u26609,\u266010,\u2660K,\u26616,\u26617,\u26618,\u266110,\u2661Q,\u2661A,\u266210,\u2662J,\u2662Q,\u2662K,\u26638,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110110101_0000000111011101_0000000111101110_0000000111000101L, "{\u26606,\u26608,\u2660Q,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26628,\u26629,\u266210,\u2662Q,\u2662K,\u2662A,\u26636,\u26638,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000011001000_0000000011110110_0000000001100111_0000000000110111L, "{\u26606,\u26607,\u26608,\u266010,\u2660J,\u26616,\u26617,\u26618,\u2661J,\u2661Q,\u26627,\u26628,\u266210,\u2662J,\u2662Q,\u2662K,\u26639,\u2663Q,\u2663K}"); 
            put(0b0000000111000000_0000000010110001_0000000100110000_0000000001110100L, "{\u26608,\u266010,\u2660J,\u2660Q,\u266110,\u2661J,\u2661A,\u26626,\u266210,\u2662J,\u2662K,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000100011000_0000000111001011_0000000111011101_0000000011110001L, "{\u26606,\u266010,\u2660J,\u2660Q,\u2660K,\u26616,\u26618,\u26619,\u266110,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26629,\u2662Q,\u2662K,\u2662A,\u26639,\u266310,\u2663A}"); 
            put(0b0000000000101000_0000000011011111_0000000100000100_0000000000110100L, "{\u26608,\u266010,\u2660J,\u26618,\u2661A,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662Q,\u2662K,\u26639,\u2663J}"); 
            put(0b0000000000000111_0000000111101101_0000000010001011_0000000101011001L, "{\u26606,\u26609,\u266010,\u2660Q,\u2660A,\u26616,\u26617,\u26619,\u2661K,\u26626,\u26628,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26638}"); 
            put(0b0000000011010101_0000000010011000_0000000000100000_0000000111011110L, "{\u26607,\u26608,\u26609,\u266010,\u2660Q,\u2660K,\u2660A,\u2661J,\u26629,\u266210,\u2662K,\u26636,\u26638,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000000111001_0000000000000000_0000000110010111_0000000010001010L, "{\u26607,\u26609,\u2660K,\u26616,\u26617,\u26618,\u266110,\u2661K,\u2661A,\u26636,\u26639,\u266310,\u2663J}"); 
            put(0b0000000001011100_0000000000111110_0000000000101110_0000000110110111L, "{\u26606,\u26607,\u26608,\u266010,\u2660J,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u2661J,\u26627,\u26628,\u26629,\u266210,\u2662J,\u26638,\u26639,\u266310,\u2663Q}"); 
            put(0b0000000111101110_0000000010101100_0000000011101001_0000000010001001L, "{\u26606,\u26609,\u2660K,\u26616,\u26619,\u2661J,\u2661Q,\u2661K,\u26628,\u26629,\u2662J,\u2662K,\u26637,\u26638,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000011100111_0000000010111101_0000000011100101_0000000011001000L, "{\u26609,\u2660Q,\u2660K,\u26616,\u26618,\u2661J,\u2661Q,\u2661K,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u26636,\u26637,\u26638,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000010111001_0000000001000010_0000000110100111_0000000010110010L, "{\u26607,\u266010,\u2660J,\u2660K,\u26616,\u26617,\u26618,\u2661J,\u2661K,\u2661A,\u26627,\u2662Q,\u26636,\u26639,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000111100011_0000000000100110_0000000110100110_0000000001111010L, "{\u26607,\u26609,\u266010,\u2660J,\u2660Q,\u26617,\u26618,\u2661J,\u2661K,\u2661A,\u26627,\u26628,\u2662J,\u26636,\u26637,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000011010101_0000000100110011_0000000000100101_0000000001110101L, "{\u26606,\u26608,\u266010,\u2660J,\u2660Q,\u26616,\u26618,\u2661J,\u26626,\u26627,\u266210,\u2662J,\u2662A,\u26636,\u26638,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000101100000_0000000001010100_0000000001000100_0000000010111110L, "{\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660K,\u26618,\u2661Q,\u26628,\u266210,\u2662Q,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000001111000_0000000000100111_0000000100001001_0000000100110111L, "{\u26606,\u26607,\u26608,\u266010,\u2660J,\u2660A,\u26616,\u26619,\u2661A,\u26626,\u26627,\u26628,\u2662J,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000100001101_0000000101011000_0000000010010010_0000000100001110L, "{\u26607,\u26608,\u26609,\u2660A,\u26617,\u266110,\u2661K,\u26629,\u266210,\u2662Q,\u2662A,\u26636,\u26638,\u26639,\u2663A}"); 
            put(0b0000000011111100_0000000011111111_0000000111000101_0000000010101000L, "{\u26609,\u2660J,\u2660K,\u26616,\u26618,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000111000101_0000000100111101_0000000001000000_0000000001000100L, "{\u26608,\u2660Q,\u2661Q,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662A,\u26636,\u26638,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000011010101_0000000010010000_0000000000001000_0000000011111111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u26619,\u266210,\u2662K,\u26636,\u26638,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000101010011_0000000111010100_0000000111001010_0000000100100100L, "{\u26608,\u2660J,\u2660A,\u26617,\u26619,\u2661Q,\u2661K,\u2661A,\u26628,\u266210,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000100111100_0000000101101010_0000000101100010_0000000111110001L, "{\u26606,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26617,\u2661J,\u2661Q,\u2661A,\u26627,\u26629,\u2662J,\u2662Q,\u2662A,\u26638,\u26639,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000010000010_0000000010010001_0000000100000110_0000000110111011L, "{\u26606,\u26607,\u26609,\u266010,\u2660J,\u2660K,\u2660A,\u26617,\u26618,\u2661A,\u26626,\u266210,\u2662K,\u26637,\u2663K}"); 
            put(0b0000000111111000_0000000111101010_0000000001001001_0000000111111111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26619,\u2661Q,\u26627,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000000101010_0000000010111000_0000000111000000_0000000001100111L, "{\u26606,\u26607,\u26608,\u2660J,\u2660Q,\u2661Q,\u2661K,\u2661A,\u26629,\u266210,\u2662J,\u2662K,\u26637,\u26639,\u2663J}"); 
            put(0b0000000100010101_0000000111111101_0000000001000100_0000000101101011L, "{\u26606,\u26607,\u26609,\u2660J,\u2660Q,\u2660A,\u26618,\u2661Q,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26638,\u266310,\u2663A}"); 
            put(0b0000000010101101_0000000001011011_0000000111110001_0000000110101111L, "{\u26606,\u26607,\u26608,\u26609,\u2660J,\u2660K,\u2660A,\u26616,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26629,\u266210,\u2662Q,\u26636,\u26638,\u26639,\u2663J,\u2663K}"); 
            put(0b0000000100011110_0000000110000111_0000000000011011_0000000011110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660Q,\u2660K,\u26616,\u26617,\u26619,\u266110,\u26626,\u26627,\u26628,\u2662K,\u2662A,\u26637,\u26638,\u26639,\u266310,\u2663A}"); 
            put(0b0000000100110101_0000000000001111_0000000101000101_0000000101100110L, "{\u26607,\u26608,\u2660J,\u2660Q,\u2660A,\u26616,\u26618,\u2661Q,\u2661A,\u26626,\u26627,\u26628,\u26629,\u26636,\u26638,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000111010100_0000000001111010_0000000010000101_0000000011101101L, "{\u26606,\u26608,\u26609,\u2660J,\u2660Q,\u2660K,\u26616,\u26618,\u2661K,\u26627,\u26629,\u266210,\u2662J,\u2662Q,\u26638,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000001011100_0000000010101010_0000000110000110_0000000110110101L, "{\u26606,\u26608,\u266010,\u2660J,\u2660K,\u2660A,\u26617,\u26618,\u2661K,\u2661A,\u26627,\u26629,\u2662J,\u2662K,\u26638,\u26639,\u266310,\u2663Q}"); 
            put(0b0000000000110100_0000000110110111_0000000101110011_0000000010100010L, "{\u26607,\u2660J,\u2660K,\u26616,\u26617,\u266110,\u2661J,\u2661Q,\u2661A,\u26626,\u26627,\u26628,\u266210,\u2662J,\u2662K,\u2662A,\u26638,\u266310,\u2663J}"); 
            put(0b0000000001011111_0000000110000110_0000000101010111_0000000010011011L, "{\u26606,\u26607,\u26609,\u266010,\u2660K,\u26616,\u26617,\u26618,\u266110,\u2661Q,\u2661A,\u26627,\u26628,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663Q}"); 
            put(0b0000000100100000_0000000000110000_0000000111101000_0000000101011100L, "{\u26608,\u26609,\u266010,\u2660Q,\u2660A,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u266210,\u2662J,\u2663J,\u2663A}"); 
            put(0b0000000010100001_0000000111000100_0000000100100111_0000000111100111L, "{\u26606,\u26607,\u26608,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u2661J,\u2661A,\u26628,\u2662Q,\u2662K,\u2662A,\u26636,\u2663J,\u2663K}"); 
            put(0b0000000000110110_0000000011011111_0000000001000001_0000000011001010L, "{\u26607,\u26609,\u2660Q,\u2660K,\u26616,\u2661Q,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662Q,\u2662K,\u26637,\u26638,\u266310,\u2663J}"); 
            put(0b0000000111100010_0000000000011010_0000000010110111_0000000101101000L, "{\u26609,\u2660J,\u2660Q,\u2660A,\u26616,\u26617,\u26618,\u266110,\u2661J,\u2661K,\u26627,\u26629,\u266210,\u26637,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000101101011_0000000110011000_0000000100000110_0000000001011000L, "{\u26609,\u266010,\u2660Q,\u26617,\u26618,\u2661A,\u26629,\u266210,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000111111001_0000000101001101_0000000011011010_0000000100101000L, "{\u26609,\u2660J,\u2660A,\u26617,\u26619,\u266110,\u2661Q,\u2661K,\u26626,\u26628,\u26629,\u2662Q,\u2662A,\u26636,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000101001000_0000000000001100_0000000010001011_0000000101000111L, "{\u26606,\u26607,\u26608,\u2660Q,\u2660A,\u26616,\u26617,\u26619,\u2661K,\u26628,\u26629,\u26639,\u2663Q,\u2663A}"); 
            put(0b0000000111001110_0000000010100101_0000000110011110_0000000010010011L, "{\u26606,\u26607,\u266010,\u2660K,\u26617,\u26618,\u26619,\u266110,\u2661K,\u2661A,\u26626,\u26628,\u2662J,\u2662K,\u26637,\u26638,\u26639,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000001111001_0000000011001010_0000000111101100_0000000010110101L, "{\u26606,\u26608,\u266010,\u2660J,\u2660K,\u26618,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u26627,\u26629,\u2662Q,\u2662K,\u26636,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000001011110_0000000100101100_0000000111100100_0000000101000101L, "{\u26606,\u26608,\u2660Q,\u2660A,\u26618,\u2661J,\u2661Q,\u2661K,\u2661A,\u26628,\u26629,\u2662J,\u2662A,\u26637,\u26638,\u26639,\u266310,\u2663Q}"); 
            put(0b0000000101111001_0000000000101111_0000000110001010_0000000001110000L, "{\u266010,\u2660J,\u2660Q,\u26617,\u26619,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u26629,\u2662J,\u26636,\u26639,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000001000110_0000000110110011_0000000010001001_0000000111111010L, "{\u26607,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26619,\u2661K,\u26626,\u26627,\u266210,\u2662J,\u2662K,\u2662A,\u26637,\u26638,\u2663Q}"); 
            put(0b0000000100011010_0000000101010111_0000000110100100_0000000000100000L, "{\u2660J,\u26618,\u2661J,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u266210,\u2662Q,\u2662A,\u26637,\u26639,\u266310,\u2663A}"); 
            put(0b0000000011110010_0000000101111011_0000000000100111_0000000101001101L, "{\u26606,\u26608,\u26609,\u2660Q,\u2660A,\u26616,\u26617,\u26618,\u2661J,\u26626,\u26627,\u26629,\u266210,\u2662J,\u2662Q,\u2662A,\u26637,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000000110010_0000000011010011_0000000001111110_0000000110011100L, "{\u26608,\u26609,\u266010,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u26626,\u26627,\u266210,\u2662Q,\u2662K,\u26637,\u266310,\u2663J}"); 
            put(0b0000000100101000_0000000100111000_0000000110101110_0000000011100000L, "{\u2660J,\u2660Q,\u2660K,\u26617,\u26618,\u26619,\u2661J,\u2661K,\u2661A,\u26629,\u266210,\u2662J,\u2662A,\u26639,\u2663J,\u2663A}"); 
            put(0b0000000011011100_0000000000111001_0000000011100011_0000000001011100L, "{\u26608,\u26609,\u266010,\u2660Q,\u26616,\u26617,\u2661J,\u2661Q,\u2661K,\u26626,\u26629,\u266210,\u2662J,\u26638,\u26639,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000111101011_0000000101011110_0000000011010001_0000000111111001L, "{\u26606,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u266110,\u2661Q,\u2661K,\u26627,\u26628,\u26629,\u266210,\u2662Q,\u2662A,\u26636,\u26637,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000101001100_0000000000100100_0000000100001011_0000000100001110L, "{\u26607,\u26608,\u26609,\u2660A,\u26616,\u26617,\u26619,\u2661A,\u26628,\u2662J,\u26638,\u26639,\u2663Q,\u2663A}"); 
            put(0b0000000011101011_0000000100111001_0000000000000101_0000000000000101L, "{\u26606,\u26608,\u26616,\u26618,\u26626,\u26629,\u266210,\u2662J,\u2662A,\u26636,\u26637,\u26639,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000001110101_0000000100001111_0000000111101110_0000000100001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660A,\u26617,\u26618,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u26629,\u2662A,\u26636,\u26638,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000010001110_0000000001000101_0000000001000010_0000000011111100L, "{\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u26617,\u2661Q,\u26626,\u26628,\u2662Q,\u26637,\u26638,\u26639,\u2663K}"); 
            put(0b0000000000010011_0000000111000101_0000000001011110_0000000010000000L, "{\u2660K,\u26617,\u26618,\u26619,\u266110,\u2661Q,\u26626,\u26628,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u266310}"); 
            put(0b0000000001111111_0000000111010010_0000000110000011_0000000100000111L, "{\u26606,\u26607,\u26608,\u2660A,\u26616,\u26617,\u2661K,\u2661A,\u26627,\u266210,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000100111001_0000000100001110_0000000101110101_0000000001100101L, "{\u26606,\u26608,\u2660J,\u2660Q,\u26616,\u26618,\u266110,\u2661J,\u2661Q,\u2661A,\u26627,\u26628,\u26629,\u2662A,\u26636,\u26639,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000111111010_0000000110100100_0000000100111101_0000000010011110L, "{\u26607,\u26608,\u26609,\u266010,\u2660K,\u26616,\u26618,\u26619,\u266110,\u2661J,\u2661A,\u26628,\u2662J,\u2662K,\u2662A,\u26637,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000100010001_0000000011000101_0000000001110101_0000000001010001L, "{\u26606,\u266010,\u2660Q,\u26616,\u26618,\u266110,\u2661J,\u2661Q,\u26626,\u26628,\u2662Q,\u2662K,\u26636,\u266310,\u2663A}"); 
            put(0b0000000011111011_0000000111111100_0000000111111001_0000000010010110L, "{\u26607,\u26608,\u266010,\u2660K,\u26616,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000000111100_0000000011010111_0000000001101101_0000000011111010L, "{\u26607,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u26616,\u26618,\u26619,\u2661J,\u2661Q,\u26626,\u26627,\u26628,\u266210,\u2662Q,\u2662K,\u26638,\u26639,\u266310,\u2663J}"); 
            put(0b0000000001100111_0000000100110000_0000000101101000_0000000101100110L, "{\u26607,\u26608,\u2660J,\u2660Q,\u2660A,\u26619,\u2661J,\u2661Q,\u2661A,\u266210,\u2662J,\u2662A,\u26636,\u26637,\u26638,\u2663J,\u2663Q}"); 
            put(0b0000000101011111_0000000110101110_0000000001100111_0000000010010101L, "{\u26606,\u26608,\u266010,\u2660K,\u26616,\u26617,\u26618,\u2661J,\u2661Q,\u26627,\u26628,\u26629,\u2662J,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000011110001_0000000011001000_0000000011100110_0000000100110010L, "{\u26607,\u266010,\u2660J,\u2660A,\u26617,\u26618,\u2661J,\u2661Q,\u2661K,\u26629,\u2662Q,\u2662K,\u26636,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000001001101_0000000111100111_0000000010010101_0000000001111000L, "{\u26609,\u266010,\u2660J,\u2660Q,\u26616,\u26618,\u266110,\u2661K,\u26626,\u26627,\u26628,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26638,\u26639,\u2663Q}"); 
            put(0b0000000100001110_0000000100000110_0000000101100100_0000000111010001L, "{\u26606,\u266010,\u2660Q,\u2660K,\u2660A,\u26618,\u2661J,\u2661Q,\u2661A,\u26627,\u26628,\u2662A,\u26637,\u26638,\u26639,\u2663A}"); 
            put(0b0000000100011000_0000000101010001_0000000101000101_0000000100000000L, "{\u2660A,\u26616,\u26618,\u2661Q,\u2661A,\u26626,\u266210,\u2662Q,\u2662A,\u26639,\u266310,\u2663A}"); 
            put(0b0000000110011100_0000000011011010_0000000000110000_0000000000100100L, "{\u26608,\u2660J,\u266110,\u2661J,\u26627,\u26629,\u266210,\u2662Q,\u2662K,\u26638,\u26639,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000101111101_0000000110111010_0000000010000100_0000000111111100L, "{\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26618,\u2661K,\u26627,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u26636,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000101001011_0000000101100111_0000000110000110_0000000010010101L, "{\u26606,\u26608,\u266010,\u2660K,\u26617,\u26618,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u26639,\u2663Q,\u2663A}"); 
            put(0b0000000111010010_0000000111010101_0000000010000100_0000000100111010L, "{\u26607,\u26609,\u266010,\u2660J,\u2660A,\u26618,\u2661K,\u26626,\u26628,\u266210,\u2662Q,\u2662K,\u2662A,\u26637,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000100101000_0000000111101111_0000000001101101_0000000001111001L, "{\u26606,\u26609,\u266010,\u2660J,\u2660Q,\u26616,\u26618,\u26619,\u2661J,\u2661Q,\u26626,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26639,\u2663J,\u2663A}"); 
            put(0b0000000110111010_0000000111110110_0000000100101100_0000000011100111L, "{\u26606,\u26607,\u26608,\u2660J,\u2660Q,\u2660K,\u26618,\u26619,\u2661J,\u2661A,\u26627,\u26628,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26637,\u26639,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000001100100_0000000101011101_0000000011100110_0000000101101100L, "{\u26608,\u26609,\u2660J,\u2660Q,\u2660A,\u26617,\u26618,\u2661J,\u2661Q,\u2661K,\u26626,\u26628,\u26629,\u266210,\u2662Q,\u2662A,\u26638,\u2663J,\u2663Q}"); 
            put(0b0000000110111010_0000000101011011_0000000010011000_0000000100000011L, "{\u26606,\u26607,\u2660A,\u26619,\u266110,\u2661K,\u26626,\u26627,\u26629,\u266210,\u2662Q,\u2662A,\u26637,\u26639,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000100000111_0000000011110000_0000000000110011_0000000010010010L, "{\u26607,\u266010,\u2660K,\u26616,\u26617,\u266110,\u2661J,\u266210,\u2662J,\u2662Q,\u2662K,\u26636,\u26637,\u26638,\u2663A}"); 
            put(0b0000000100010001_0000000100110110_0000000000011110_0000000001000010L, "{\u26607,\u2660Q,\u26617,\u26618,\u26619,\u266110,\u26627,\u26628,\u266210,\u2662J,\u2662A,\u26636,\u266310,\u2663A}"); 
            put(0b0000000111001010_0000000000101101_0000000100100111_0000000101101000L, "{\u26609,\u2660J,\u2660Q,\u2660A,\u26616,\u26617,\u26618,\u2661J,\u2661A,\u26626,\u26628,\u26629,\u2662J,\u26637,\u26639,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110110011_0000000001110111_0000000101011001_0000000011111100L, "{\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u26616,\u26619,\u266110,\u2661Q,\u2661A,\u26626,\u26627,\u26628,\u266210,\u2662J,\u2662Q,\u26636,\u26637,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000000001101_0000000010001001_0000000011000011_0000000001100111L, "{\u26606,\u26607,\u26608,\u2660J,\u2660Q,\u26616,\u26617,\u2661Q,\u2661K,\u26626,\u26629,\u2662K,\u26636,\u26638,\u26639}"); 
            put(0b0000000110100010_0000000000011000_0000000011110010_0000000001000111L, "{\u26606,\u26607,\u26608,\u2660Q,\u26617,\u266110,\u2661J,\u2661Q,\u2661K,\u26629,\u266210,\u26637,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000001111101_0000000100110001_0000000110110010_0000000001110001L, "{\u26606,\u266010,\u2660J,\u2660Q,\u26617,\u266110,\u2661J,\u2661K,\u2661A,\u26626,\u266210,\u2662J,\u2662A,\u26636,\u26638,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000000011010_0000000110010001_0000000001011110_0000000001110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660Q,\u26617,\u26618,\u26619,\u266110,\u2661Q,\u26626,\u266210,\u2662K,\u2662A,\u26637,\u26639,\u266310}"); 
            put(0b0000000101110011_0000000001110011_0000000101110011_0000000101011110L, "{\u26607,\u26608,\u26609,\u266010,\u2660Q,\u2660A,\u26616,\u26617,\u266110,\u2661J,\u2661Q,\u2661A,\u26626,\u26627,\u266210,\u2662J,\u2662Q,\u26636,\u26637,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000011010011_0000000100010000_0000000100011000_0000000010100100L, "{\u26608,\u2660J,\u2660K,\u26619,\u266110,\u2661A,\u266210,\u2662A,\u26636,\u26637,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000110011011_0000000111000100_0000000000111110_0000000100000110L, "{\u26607,\u26608,\u2660A,\u26617,\u26618,\u26619,\u266110,\u2661J,\u26628,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000111101011_0000000110111110_0000000100011111_0000000011110100L, "{\u26608,\u266010,\u2660J,\u2660Q,\u2660K,\u26616,\u26617,\u26618,\u26619,\u266110,\u2661A,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000001101111_0000000101100101_0000000100111101_0000000000100101L, "{\u26606,\u26608,\u2660J,\u26616,\u26618,\u26619,\u266110,\u2661J,\u2661A,\u26626,\u26628,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u26638,\u26639,\u2663J,\u2663Q}"); 
            put(0b0000000000000001_0000000000111100_0000000100010110_0000000010001010L, "{\u26607,\u26609,\u2660K,\u26617,\u26618,\u266110,\u2661A,\u26628,\u26629,\u266210,\u2662J,\u26636}"); 
            put(0b0000000100001010_0000000010111110_0000000011010101_0000000010110000L, "{\u266010,\u2660J,\u2660K,\u26616,\u26618,\u266110,\u2661Q,\u2661K,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u26637,\u26639,\u2663A}"); 
            put(0b0000000010100100_0000000001100111_0000000011111100_0000000011001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660Q,\u2660K,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u26626,\u26627,\u26628,\u2662J,\u2662Q,\u26638,\u2663J,\u2663K}"); 
            put(0b0000000110101110_0000000100111000_0000000001000100_0000000011111010L, "{\u26607,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u26618,\u2661Q,\u26629,\u266210,\u2662J,\u2662A,\u26637,\u26638,\u26639,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000010110101_0000000101001011_0000000111110010_0000000100001010L, "{\u26607,\u26609,\u2660A,\u26617,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26629,\u2662Q,\u2662A,\u26636,\u26638,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000110101111_0000000101101011_0000000110101111_0000000111001011L, "{\u26606,\u26607,\u26609,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u26619,\u2661J,\u2661K,\u2661A,\u26626,\u26627,\u26629,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u26638,\u26639,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000010011100_0000000001101001_0000000111011110_0000000111111011L, "{\u26606,\u26607,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u266110,\u2661Q,\u2661K,\u2661A,\u26626,\u26629,\u2662J,\u2662Q,\u26638,\u26639,\u266310,\u2663K}"); 
            put(0b0000000010101001_0000000100101111_0000000101001001_0000000000110110L, "{\u26607,\u26608,\u266010,\u2660J,\u26616,\u26619,\u2661Q,\u2661A,\u26626,\u26627,\u26628,\u26629,\u2662J,\u2662A,\u26636,\u26639,\u2663J,\u2663K}"); 
            put(0b0000000100011011_0000000010001100_0000000101011110_0000000110100001L, "{\u26606,\u2660J,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u266110,\u2661Q,\u2661A,\u26628,\u26629,\u2662K,\u26636,\u26637,\u26639,\u266310,\u2663A}"); 
            put(0b0000000010110010_0000000001001111_0000000000011010_0000000001111101L, "{\u26606,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u26617,\u26619,\u266110,\u26626,\u26627,\u26628,\u26629,\u2662Q,\u26637,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000101000010_0000000111111110_0000000000010111_0000000100001101L, "{\u26606,\u26608,\u26609,\u2660A,\u26616,\u26617,\u26618,\u266110,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26637,\u2663Q,\u2663A}"); 
            put(0b0000000011101111_0000000100111111_0000000000000110_0000000000011010L, "{\u26607,\u26609,\u266010,\u26617,\u26618,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662A,\u26636,\u26637,\u26638,\u26639,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000111101000_0000000001000011_0000000111110100_0000000000010100L, "{\u26608,\u266010,\u26618,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u2662Q,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000101011001_0000000011111101_0000000010000010_0000000001001011L, "{\u26606,\u26607,\u26609,\u2660Q,\u26617,\u2661K,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u26636,\u26639,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000011101011_0000000110011100_0000000101100100_0000000110100111L, "{\u26606,\u26607,\u26608,\u2660J,\u2660K,\u2660A,\u26618,\u2661J,\u2661Q,\u2661A,\u26628,\u26629,\u266210,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000110001101_0000000110100000_0000000111010110_0000000101011101L, "{\u26606,\u26608,\u26609,\u266010,\u2660Q,\u2660A,\u26617,\u26618,\u266110,\u2661Q,\u2661K,\u2661A,\u2662J,\u2662K,\u2662A,\u26636,\u26638,\u26639,\u2663K,\u2663A}"); 
            put(0b0000000110000010_0000000000111110_0000000100100000_0000000100011000L, "{\u26609,\u266010,\u2660A,\u2661J,\u2661A,\u26627,\u26628,\u26629,\u266210,\u2662J,\u26637,\u2663K,\u2663A}"); 
            put(0b0000000100010111_0000000111100011_0000000001111001_0000000110001000L, "{\u26609,\u2660K,\u2660A,\u26616,\u26619,\u266110,\u2661J,\u2661Q,\u26626,\u26627,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u266310,\u2663A}"); 
            put(0b0000000101101100_0000000111010011_0000000010101100_0000000011110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660Q,\u2660K,\u26618,\u26619,\u2661J,\u2661K,\u26626,\u26627,\u266210,\u2662Q,\u2662K,\u2662A,\u26638,\u26639,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000111111010_0000000100010100_0000000111011010_0000000111100101L, "{\u26606,\u26608,\u2660J,\u2660Q,\u2660K,\u2660A,\u26617,\u26619,\u266110,\u2661Q,\u2661K,\u2661A,\u26628,\u266210,\u2662A,\u26637,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000010100101_0000000101010100_0000000010001110_0000000100111000L, "{\u26609,\u266010,\u2660J,\u2660A,\u26617,\u26618,\u26619,\u2661K,\u26628,\u266210,\u2662Q,\u2662A,\u26636,\u26638,\u2663J,\u2663K}"); 
            put(0b0000000011110011_0000000111101101_0000000100111000_0000000000100010L, "{\u26607,\u2660J,\u26619,\u266110,\u2661J,\u2661A,\u26626,\u26628,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000101111110_0000000000010000_0000000111101101_0000000101101100L, "{\u26608,\u26609,\u2660J,\u2660Q,\u2660A,\u26616,\u26618,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u266210,\u26637,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000010001010_0000000010110010_0000000101100001_0000000010110110L, "{\u26607,\u26608,\u266010,\u2660J,\u2660K,\u26616,\u2661J,\u2661Q,\u2661A,\u26627,\u266210,\u2662J,\u2662K,\u26637,\u26639,\u2663K}"); 
            put(0b0000000001110000_0000000000000101_0000000001000001_0000000001110100L, "{\u26608,\u266010,\u2660J,\u2660Q,\u26616,\u2661Q,\u26626,\u26628,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000101101111_0000000011011011_0000000010101100_0000000011101101L, "{\u26606,\u26608,\u26609,\u2660J,\u2660Q,\u2660K,\u26618,\u26619,\u2661J,\u2661K,\u26626,\u26627,\u26629,\u266210,\u2662Q,\u2662K,\u26636,\u26637,\u26638,\u26639,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000101010011_0000000110010011_0000000000011110_0000000010111001L, "{\u26606,\u26609,\u266010,\u2660J,\u2660K,\u26617,\u26618,\u26619,\u266110,\u26626,\u26627,\u266210,\u2662K,\u2662A,\u26636,\u26637,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000110010010_0000000010001001_0000000010111111_0000000000100100L, "{\u26608,\u2660J,\u26616,\u26617,\u26618,\u26619,\u266110,\u2661J,\u2661K,\u26626,\u26629,\u2662K,\u26637,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000110010100_0000000000000100_0000000000110001_0000000111111011L, "{\u26606,\u26607,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u266110,\u2661J,\u26628,\u26638,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000000101101_0000000001111011_0000000100100001_0000000110101100L, "{\u26608,\u26609,\u2660J,\u2660K,\u2660A,\u26616,\u2661J,\u2661A,\u26626,\u26627,\u26629,\u266210,\u2662J,\u2662Q,\u26636,\u26638,\u26639,\u2663J}"); 
            put(0b0000000110011111_0000000100101111_0000000001010011_0000000001001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660Q,\u26616,\u26617,\u266110,\u2661Q,\u26626,\u26627,\u26628,\u26629,\u2662J,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000011010010_0000000110111110_0000000111000010_0000000010001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660K,\u26617,\u2661Q,\u2661K,\u2661A,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u26637,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000011000101_0000000100011010_0000000111101100_0000000001101001L, "{\u26606,\u26609,\u2660J,\u2660Q,\u26618,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u26627,\u26629,\u266210,\u2662A,\u26636,\u26638,\u2663Q,\u2663K}"); 
            put(0b0000000110100011_0000000001010010_0000000111110110_0000000011000011L, "{\u26606,\u26607,\u2660Q,\u2660K,\u26617,\u26618,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26627,\u266210,\u2662Q,\u26636,\u26637,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000101111100_0000000011010100_0000000010001111_0000000101001100L, "{\u26608,\u26609,\u2660Q,\u2660A,\u26616,\u26617,\u26618,\u26619,\u2661K,\u26628,\u266210,\u2662Q,\u2662K,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000011100101_0000000111010010_0000000110110010_0000000000101110L, "{\u26607,\u26608,\u26609,\u2660J,\u26617,\u266110,\u2661J,\u2661K,\u2661A,\u26627,\u266210,\u2662Q,\u2662K,\u2662A,\u26636,\u26638,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000111101111_0000000100101111_0000000000011000_0000000000100101L, "{\u26606,\u26608,\u2660J,\u26619,\u266110,\u26626,\u26627,\u26628,\u26629,\u2662J,\u2662A,\u26636,\u26637,\u26638,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000100001010_0000000100111111_0000000000101111_0000000011000110L, "{\u26607,\u26608,\u2660Q,\u2660K,\u26616,\u26617,\u26618,\u26619,\u2661J,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662A,\u26637,\u26639,\u2663A}"); 
            put(0b0000000101011100_0000000000111111_0000000010100110_0000000101100011L, "{\u26606,\u26607,\u2660J,\u2660Q,\u2660A,\u26617,\u26618,\u2661J,\u2661K,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662J,\u26638,\u26639,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000101010101_0000000101000110_0000000010010000_0000000001111111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u266110,\u2661K,\u26627,\u26628,\u2662Q,\u2662A,\u26636,\u26638,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000001101001_0000000001011010_0000000011100010_0000000100110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660A,\u26617,\u2661J,\u2661Q,\u2661K,\u26627,\u26629,\u266210,\u2662Q,\u26636,\u26639,\u2663J,\u2663Q}"); 
            put(0b0000000010011001_0000000001001011_0000000100111110_0000000000111101L, "{\u26606,\u26608,\u26609,\u266010,\u2660J,\u26617,\u26618,\u26619,\u266110,\u2661J,\u2661A,\u26626,\u26627,\u26629,\u2662Q,\u26636,\u26639,\u266310,\u2663K}"); 
            put(0b0000000110001001_0000000101110011_0000000111110010_0000000010000001L, "{\u26606,\u2660K,\u26617,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u266210,\u2662J,\u2662Q,\u2662A,\u26636,\u26639,\u2663K,\u2663A}"); 
            put(0b0000000101000000_0000000011100110_0000000000011010_0000000110010111L, "{\u26606,\u26607,\u26608,\u266010,\u2660K,\u2660A,\u26617,\u26619,\u266110,\u26627,\u26628,\u2662J,\u2662Q,\u2662K,\u2663Q,\u2663A}"); 
            put(0b0000000100101100_0000000001100000_0000000100101001_0000000001000101L, "{\u26606,\u26608,\u2660Q,\u26616,\u26619,\u2661J,\u2661A,\u2662J,\u2662Q,\u26638,\u26639,\u2663J,\u2663A}"); 
            put(0b0000000100100101_0000000001101111_0000000011011011_0000000101110010L, "{\u26607,\u266010,\u2660J,\u2660Q,\u2660A,\u26616,\u26617,\u26619,\u266110,\u2661Q,\u2661K,\u26626,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u26636,\u26638,\u2663J,\u2663A}"); 
            put(0b0000000001011111_0000000010100011_0000000100101110_0000000100100100L, "{\u26608,\u2660J,\u2660A,\u26617,\u26618,\u26619,\u2661J,\u2661A,\u26626,\u26627,\u2662J,\u2662K,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663Q}"); 
            put(0b0000000000001110_0000000100010100_0000000100000111_0000000010000011L, "{\u26606,\u26607,\u2660K,\u26616,\u26617,\u26618,\u2661A,\u26628,\u266210,\u2662A,\u26637,\u26638,\u26639}"); 
            put(0b0000000011101001_0000000010101001_0000000110010110_0000000000001100L, "{\u26608,\u26609,\u26617,\u26618,\u266110,\u2661K,\u2661A,\u26626,\u26629,\u2662J,\u2662K,\u26636,\u26639,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000011111000_0000000110100011_0000000001000111_0000000010100001L, "{\u26606,\u2660J,\u2660K,\u26616,\u26617,\u26618,\u2661Q,\u26626,\u26627,\u2662J,\u2662K,\u2662A,\u26639,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000101011100_0000000011011000_0000000100110010_0000000001011100L, "{\u26608,\u26609,\u266010,\u2660Q,\u26617,\u266110,\u2661J,\u2661A,\u26629,\u266210,\u2662Q,\u2662K,\u26638,\u26639,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000100010001_0000000010000011_0000000100101010_0000000100011100L, "{\u26608,\u26609,\u266010,\u2660A,\u26617,\u26619,\u2661J,\u2661A,\u26626,\u26627,\u2662K,\u26636,\u266310,\u2663A}"); 
            put(0b0000000000110100_0000000000001011_0000000111001010_0000000101110001L, "{\u26606,\u266010,\u2660J,\u2660Q,\u2660A,\u26617,\u26619,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26629,\u26638,\u266310,\u2663J}"); 
            put(0b0000000111110110_0000000011110101_0000000111000110_0000000001101001L, "{\u26606,\u26609,\u2660J,\u2660Q,\u26617,\u26618,\u2661Q,\u2661K,\u2661A,\u26626,\u26628,\u266210,\u2662J,\u2662Q,\u2662K,\u26637,\u26638,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000100111011_0000000110110011_0000000100011110_0000000101100011L, "{\u26606,\u26607,\u2660J,\u2660Q,\u2660A,\u26617,\u26618,\u26619,\u266110,\u2661A,\u26626,\u26627,\u266210,\u2662J,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000111011010_0000000111011100_0000000000110011_0000000000000010L, "{\u26607,\u26616,\u26617,\u266110,\u2661J,\u26628,\u26629,\u266210,\u2662Q,\u2662K,\u2662A,\u26637,\u26639,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000011000011_0000000000101001_0000000101100110_0000000101010111L, "{\u26606,\u26607,\u26608,\u266010,\u2660Q,\u2660A,\u26617,\u26618,\u2661J,\u2661Q,\u2661A,\u26626,\u26629,\u2662J,\u26636,\u26637,\u2663Q,\u2663K}"); 
            put(0b0000000101110101_0000000010011101_0000000010110000_0000000001101011L, "{\u26606,\u26607,\u26609,\u2660J,\u2660Q,\u266110,\u2661J,\u2661K,\u26626,\u26628,\u26629,\u266210,\u2662K,\u26636,\u26638,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000100010110_0000000011010000_0000000011010010_0000000001000110L, "{\u26607,\u26608,\u2660Q,\u26617,\u266110,\u2661Q,\u2661K,\u266210,\u2662Q,\u2662K,\u26637,\u26638,\u266310,\u2663A}"); 
            put(0b0000000100111011_0000000111101110_0000000100101010_0000000011000101L, "{\u26606,\u26608,\u2660Q,\u2660K,\u26617,\u26619,\u2661J,\u2661A,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000111101010_0000000100111111_0000000001101111_0000000101010101L, "{\u26606,\u26608,\u266010,\u2660Q,\u2660A,\u26616,\u26617,\u26618,\u26619,\u2661J,\u2661Q,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662A,\u26637,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000010000011_0000000011001011_0000000111010100_0000000001100010L, "{\u26607,\u2660J,\u2660Q,\u26618,\u266110,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26629,\u2662Q,\u2662K,\u26636,\u26637,\u2663K}"); 
            put(0b0000000111011101_0000000111001100_0000000011001010_0000000111011111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660Q,\u2660K,\u2660A,\u26617,\u26619,\u2661Q,\u2661K,\u26628,\u26629,\u2662Q,\u2662K,\u2662A,\u26636,\u26638,\u26639,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000010110100_0000000110001101_0000000100101001_0000000100111011L, "{\u26606,\u26607,\u26609,\u266010,\u2660J,\u2660A,\u26616,\u26619,\u2661J,\u2661A,\u26626,\u26628,\u26629,\u2662K,\u2662A,\u26638,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000111011101_0000000000101110_0000000100001111_0000000011110100L, "{\u26608,\u266010,\u2660J,\u2660Q,\u2660K,\u26616,\u26617,\u26618,\u26619,\u2661A,\u26627,\u26628,\u26629,\u2662J,\u26636,\u26638,\u26639,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000111011001_0000000010011010_0000000011100110_0000000111101011L, "{\u26606,\u26607,\u26609,\u2660J,\u2660Q,\u2660K,\u2660A,\u26617,\u26618,\u2661J,\u2661Q,\u2661K,\u26627,\u26629,\u266210,\u2662K,\u26636,\u26639,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000011000010_0000000010101100_0000000010011100_0000000000001001L, "{\u26606,\u26609,\u26618,\u26619,\u266110,\u2661K,\u26628,\u26629,\u2662J,\u2662K,\u26637,\u2663Q,\u2663K}"); 
            put(0b0000000010000101_0000000101010110_0000000110111000_0000000110110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660K,\u2660A,\u26619,\u266110,\u2661J,\u2661K,\u2661A,\u26627,\u26628,\u266210,\u2662Q,\u2662A,\u26636,\u26638,\u2663K}"); 
            put(0b0000000001000111_0000000110111101_0000000111001110_0000000111000111L, "{\u26606,\u26607,\u26608,\u2660Q,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u2661Q,\u2661K,\u2661A,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u2663Q}"); 
            put(0b0000000111000110_0000000010101001_0000000110001100_0000000101101100L, "{\u26608,\u26609,\u2660J,\u2660Q,\u2660A,\u26618,\u26619,\u2661K,\u2661A,\u26626,\u26629,\u2662J,\u2662K,\u26637,\u26638,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000011010110_0000000001101110_0000000101101001_0000000111001011L, "{\u26606,\u26607,\u26609,\u2660Q,\u2660K,\u2660A,\u26616,\u26619,\u2661J,\u2661Q,\u2661A,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u26637,\u26638,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000000011011_0000000010101111_0000000110000100_0000000001110010L, "{\u26607,\u266010,\u2660J,\u2660Q,\u26618,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u26629,\u2662J,\u2662K,\u26636,\u26637,\u26639,\u266310}"); 
            put(0b0000000001100101_0000000011011001_0000000010000010_0000000100100101L, "{\u26606,\u26608,\u2660J,\u2660A,\u26617,\u2661K,\u26626,\u26629,\u266210,\u2662Q,\u2662K,\u26636,\u26638,\u2663J,\u2663Q}"); 
            put(0b0000000101010100_0000000101100111_0000000011000000_0000000011010111L, "{\u26606,\u26607,\u26608,\u266010,\u2660Q,\u2660K,\u2661Q,\u2661K,\u26626,\u26627,\u26628,\u2662J,\u2662Q,\u2662A,\u26638,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000100000110_0000000101110101_0000000010101010_0000000111011000L, "{\u26609,\u266010,\u2660Q,\u2660K,\u2660A,\u26617,\u26619,\u2661J,\u2661K,\u26626,\u26628,\u266210,\u2662J,\u2662Q,\u2662A,\u26637,\u26638,\u2663A}"); 
            put(0b0000000110010110_0000000100111100_0000000111111101_0000000110101000L, "{\u26609,\u2660J,\u2660K,\u2660A,\u26616,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26628,\u26629,\u266210,\u2662J,\u2662A,\u26637,\u26638,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000111010110_0000000010010000_0000000010000000_0000000101001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660Q,\u2660A,\u2661K,\u266210,\u2662K,\u26637,\u26638,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000011010000_0000000010110111_0000000110100001_0000000010110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660K,\u26616,\u2661J,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u266210,\u2662J,\u2662K,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000011111001_0000000110111101_0000000111101001_0000000100010001L, "{\u26606,\u266010,\u2660A,\u26616,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u26636,\u26639,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000110110111_0000000010011010_0000000010110001_0000000011110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660Q,\u2660K,\u26616,\u266110,\u2661J,\u2661K,\u26627,\u26629,\u266210,\u2662K,\u26636,\u26637,\u26638,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000100001011_0000000011001010_0000000010010111_0000000100100000L, "{\u2660J,\u2660A,\u26616,\u26617,\u26618,\u266110,\u2661K,\u26627,\u26629,\u2662Q,\u2662K,\u26636,\u26637,\u26639,\u2663A}"); 
            put(0b0000000101100101_0000000011011111_0000000011011110_0000000011010110L, "{\u26607,\u26608,\u266010,\u2660Q,\u2660K,\u26617,\u26618,\u26619,\u266110,\u2661Q,\u2661K,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662Q,\u2662K,\u26636,\u26638,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000110010001_0000000111011101_0000000001101110_0000000000010111L, "{\u26606,\u26607,\u26608,\u266010,\u26617,\u26618,\u26619,\u2661J,\u2661Q,\u26626,\u26628,\u26629,\u266210,\u2662Q,\u2662K,\u2662A,\u26636,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000110100001_0000000110111100_0000000000000100_0000000101111101L, "{\u26606,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660A,\u26618,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u26636,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000001011110_0000000111010000_0000000000000010_0000000110011001L, "{\u26606,\u26609,\u266010,\u2660K,\u2660A,\u26617,\u266210,\u2662Q,\u2662K,\u2662A,\u26637,\u26638,\u26639,\u266310,\u2663Q}"); 
            put(0b0000000101100101_0000000010110001_0000000000011001_0000000111101100L, "{\u26608,\u26609,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26619,\u266110,\u26626,\u266210,\u2662J,\u2662K,\u26636,\u26638,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000001111100_0000000010110011_0000000100111111_0000000011110001L, "{\u26606,\u266010,\u2660J,\u2660Q,\u2660K,\u26616,\u26617,\u26618,\u26619,\u266110,\u2661J,\u2661A,\u26626,\u26627,\u266210,\u2662J,\u2662K,\u26638,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000010001000_0000000101100100_0000000010010001_0000000011101010L, "{\u26607,\u26609,\u2660J,\u2660Q,\u2660K,\u26616,\u266110,\u2661K,\u26628,\u2662J,\u2662Q,\u2662A,\u26639,\u2663K}"); 
            put(0b0000000111001001_0000000000001010_0000000101100111_0000000110011010L, "{\u26607,\u26609,\u266010,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u2661J,\u2661Q,\u2661A,\u26627,\u26629,\u26636,\u26639,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000000100010_0000000001001001_0000000010100110_0000000111001100L, "{\u26608,\u26609,\u2660Q,\u2660K,\u2660A,\u26617,\u26618,\u2661J,\u2661K,\u26626,\u26629,\u2662Q,\u26637,\u2663J}"); 
            put(0b0000000101100111_0000000100011011_0000000001110010_0000000011001001L, "{\u26606,\u26609,\u2660Q,\u2660K,\u26617,\u266110,\u2661J,\u2661Q,\u26626,\u26627,\u26629,\u266210,\u2662A,\u26636,\u26637,\u26638,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000110010100_0000000101100010_0000000010100100_0000000110001110L, "{\u26607,\u26608,\u26609,\u2660K,\u2660A,\u26618,\u2661J,\u2661K,\u26627,\u2662J,\u2662Q,\u2662A,\u26638,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000101110001_0000000000011111_0000000100010110_0000000010000110L, "{\u26607,\u26608,\u2660K,\u26617,\u26618,\u266110,\u2661A,\u26626,\u26627,\u26628,\u26629,\u266210,\u26636,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000110110001_0000000110111011_0000000001001000_0000000010100101L, "{\u26606,\u26608,\u2660J,\u2660K,\u26619,\u2661Q,\u26626,\u26627,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u26636,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000011010011_0000000110101101_0000000101000010_0000000101110110L, "{\u26607,\u26608,\u266010,\u2660J,\u2660Q,\u2660A,\u26617,\u2661Q,\u2661A,\u26626,\u26628,\u26629,\u2662J,\u2662K,\u2662A,\u26636,\u26637,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000111110010_0000000000001001_0000000111100010_0000000100011001L, "{\u26606,\u26609,\u266010,\u2660A,\u26617,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26629,\u26637,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000100100100_0000000110110001_0000000110011001_0000000001110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660Q,\u26616,\u26619,\u266110,\u2661K,\u2661A,\u26626,\u266210,\u2662J,\u2662K,\u2662A,\u26638,\u2663J,\u2663A}"); 
            put(0b0000000001000110_0000000111001000_0000000000000110_0000000110011001L, "{\u26606,\u26609,\u266010,\u2660K,\u2660A,\u26617,\u26618,\u26629,\u2662Q,\u2662K,\u2662A,\u26637,\u26638,\u2663Q}"); 
            put(0b0000000110101000_0000000011011100_0000000100011101_0000000111111110L, "{\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26618,\u26619,\u266110,\u2661A,\u26628,\u26629,\u266210,\u2662Q,\u2662K,\u26639,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000011101100_0000000100000101_0000000111001010_0000000011001101L, "{\u26606,\u26608,\u26609,\u2660Q,\u2660K,\u26617,\u26619,\u2661Q,\u2661K,\u2661A,\u26626,\u26628,\u2662A,\u26638,\u26639,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000010111011_0000000111001110_0000000111001000_0000000111010101L, "{\u26606,\u26608,\u266010,\u2660Q,\u2660K,\u2660A,\u26619,\u2661Q,\u2661K,\u2661A,\u26627,\u26628,\u26629,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000011100000_0000000100111000_0000000100000001_0000000000001011L, "{\u26606,\u26607,\u26609,\u26616,\u2661A,\u26629,\u266210,\u2662J,\u2662A,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000111010100_0000000001011011_0000000000111101_0000000111110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26618,\u26619,\u266110,\u2661J,\u26626,\u26627,\u26629,\u266210,\u2662Q,\u26638,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000000110011_0000000111001101_0000000011010001_0000000010100010L, "{\u26607,\u2660J,\u2660K,\u26616,\u266110,\u2661Q,\u2661K,\u26626,\u26628,\u26629,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u266310,\u2663J}"); 
            put(0b0000000110000011_0000000101111100_0000000010110011_0000000111101110L, "{\u26607,\u26608,\u26609,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u266110,\u2661J,\u2661K,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u2663K,\u2663A}"); 
            put(0b0000000110010000_0000000110101110_0000000111010001_0000000011001011L, "{\u26606,\u26607,\u26609,\u2660Q,\u2660K,\u26616,\u266110,\u2661Q,\u2661K,\u2661A,\u26627,\u26628,\u26629,\u2662J,\u2662K,\u2662A,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000111001000_0000000110000001_0000000011100111_0000000011000010L, "{\u26607,\u2660Q,\u2660K,\u26616,\u26617,\u26618,\u2661J,\u2661Q,\u2661K,\u26626,\u2662K,\u2662A,\u26639,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110001100_0000000001110111_0000000110111111_0000000110000010L, "{\u26607,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u26619,\u266110,\u2661J,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u266210,\u2662J,\u2662Q,\u26638,\u26639,\u2663K,\u2663A}"); 
            put(0b0000000010011110_0000000000010011_0000000000011000_0000000110011000L, "{\u26609,\u266010,\u2660K,\u2660A,\u26619,\u266110,\u26626,\u26627,\u266210,\u26637,\u26638,\u26639,\u266310,\u2663K}"); 
            put(0b0000000011010100_0000000101100000_0000000011010010_0000000000000101L, "{\u26606,\u26608,\u26617,\u266110,\u2661Q,\u2661K,\u2662J,\u2662Q,\u2662A,\u26638,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000000001000_0000000111101111_0000000011000100_0000000101000110L, "{\u26607,\u26608,\u2660Q,\u2660A,\u26618,\u2661Q,\u2661K,\u26626,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26639}"); 
            put(0b0000000100111000_0000000001110000_0000000111101111_0000000101000100L, "{\u26608,\u2660Q,\u2660A,\u26616,\u26617,\u26618,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u266210,\u2662J,\u2662Q,\u26639,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000101101001_0000000001101101_0000000001110110_0000000101010001L, "{\u26606,\u266010,\u2660Q,\u2660A,\u26617,\u26618,\u266110,\u2661J,\u2661Q,\u26626,\u26628,\u26629,\u2662J,\u2662Q,\u26636,\u26639,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000110000110_0000000110001000_0000000110001010_0000000001101110L, "{\u26607,\u26608,\u26609,\u2660J,\u2660Q,\u26617,\u26619,\u2661K,\u2661A,\u26629,\u2662K,\u2662A,\u26637,\u26638,\u2663K,\u2663A}"); 
            put(0b0000000010000000_0000000110111111_0000000000110111_0000000011000111L, "{\u26606,\u26607,\u26608,\u2660Q,\u2660K,\u26616,\u26617,\u26618,\u266110,\u2661J,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662K,\u2662A,\u2663K}"); 
            put(0b0000000100100101_0000000110010001_0000000000100100_0000000011100101L, "{\u26606,\u26608,\u2660J,\u2660Q,\u2660K,\u26618,\u2661J,\u26626,\u266210,\u2662K,\u2662A,\u26636,\u26638,\u2663J,\u2663A}"); 
            put(0b0000000100110000_0000000010100101_0000000000101001_0000000110100100L, "{\u26608,\u2660J,\u2660K,\u2660A,\u26616,\u26619,\u2661J,\u26626,\u26628,\u2662J,\u2662K,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000111001000_0000000001010001_0000000101010110_0000000101110110L, "{\u26607,\u26608,\u266010,\u2660J,\u2660Q,\u2660A,\u26617,\u26618,\u266110,\u2661Q,\u2661A,\u26626,\u266210,\u2662Q,\u26639,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000001110010_0000000011101010_0000000011110000_0000000010100100L, "{\u26608,\u2660J,\u2660K,\u266110,\u2661J,\u2661Q,\u2661K,\u26627,\u26629,\u2662J,\u2662Q,\u2662K,\u26637,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000011001110_0000000101100100_0000000100110111_0000000001000001L, "{\u26606,\u2660Q,\u26616,\u26617,\u26618,\u266110,\u2661J,\u2661A,\u26628,\u2662J,\u2662Q,\u2662A,\u26637,\u26638,\u26639,\u2663Q,\u2663K}"); 
            put(0b0000000100100101_0000000011101110_0000000111101101_0000000010111001L, "{\u26606,\u26609,\u266010,\u2660J,\u2660K,\u26616,\u26618,\u26619,\u2661J,\u2661Q,\u2661K,\u2661A,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u2662K,\u26636,\u26638,\u2663J,\u2663A}"); 
            put(0b0000000111110101_0000000000110000_0000000001000011_0000000111100010L, "{\u26607,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u2661Q,\u266210,\u2662J,\u26636,\u26638,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000111101011_0000000110011011_0000000101111101_0000000011001010L, "{\u26607,\u26609,\u2660Q,\u2660K,\u26616,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u2661A,\u26626,\u26627,\u26629,\u266210,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000010101111_0000000000101001_0000000111010110_0000000100100111L, "{\u26606,\u26607,\u26608,\u2660J,\u2660A,\u26617,\u26618,\u266110,\u2661Q,\u2661K,\u2661A,\u26626,\u26629,\u2662J,\u26636,\u26637,\u26638,\u26639,\u2663J,\u2663K}"); 
            put(0b0000000010110101_0000000011111100_0000000101110011_0000000110011101L, "{\u26606,\u26608,\u26609,\u266010,\u2660K,\u2660A,\u26616,\u26617,\u266110,\u2661J,\u2661Q,\u2661A,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u26636,\u26638,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000111011100_0000000111000100_0000000100101010_0000000011001000L, "{\u26609,\u2660Q,\u2660K,\u26617,\u26619,\u2661J,\u2661A,\u26628,\u2662Q,\u2662K,\u2662A,\u26638,\u26639,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000000000110_0000000000011010_0000000001000000_0000000000111011L, "{\u26606,\u26607,\u26609,\u266010,\u2660J,\u2661Q,\u26627,\u26629,\u266210,\u26637,\u26638}"); 
            put(0b0000000111100111_0000000011110011_0000000001010100_0000000001000100L, "{\u26608,\u2660Q,\u26618,\u266110,\u2661Q,\u26626,\u26627,\u266210,\u2662J,\u2662Q,\u2662K,\u26636,\u26637,\u26638,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000010001000_0000000001101101_0000000110001001_0000000110111001L, "{\u26606,\u26609,\u266010,\u2660J,\u2660K,\u2660A,\u26616,\u26619,\u2661K,\u2661A,\u26626,\u26628,\u26629,\u2662J,\u2662Q,\u26639,\u2663K}"); 
            put(0b0000000110110111_0000000011100010_0000000000000110_0000000001000011L, "{\u26606,\u26607,\u2660Q,\u26617,\u26618,\u26627,\u2662J,\u2662Q,\u2662K,\u26636,\u26637,\u26638,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000010111111_0000000111000011_0000000001110111_0000000110001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u266110,\u2661J,\u2661Q,\u26626,\u26627,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000111110111_0000000000010110_0000000110000111_0000000000111011L, "{\u26606,\u26607,\u26609,\u266010,\u2660J,\u26616,\u26617,\u26618,\u2661K,\u2661A,\u26627,\u26628,\u266210,\u26636,\u26637,\u26638,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000101111001_0000000111110001_0000000000001100_0000000110000101L, "{\u26606,\u26608,\u2660K,\u2660A,\u26618,\u26619,\u26626,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26639,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000110011001_0000000000010101_0000000100111010_0000000001111001L, "{\u26606,\u26609,\u266010,\u2660J,\u2660Q,\u26617,\u26619,\u266110,\u2661J,\u2661A,\u26626,\u26628,\u266210,\u26636,\u26639,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000101101001_0000000111011010_0000000110000000_0000000101110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660Q,\u2660A,\u2661K,\u2661A,\u26627,\u26629,\u266210,\u2662Q,\u2662K,\u2662A,\u26636,\u26639,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000111000011_0000000011100000_0000000001010110_0000000100110110L, "{\u26607,\u26608,\u266010,\u2660J,\u2660A,\u26617,\u26618,\u266110,\u2661Q,\u2662J,\u2662Q,\u2662K,\u26636,\u26637,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000010100111_0000000010000100_0000000000010011_0000000101001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660Q,\u2660A,\u26616,\u26617,\u266110,\u26628,\u2662K,\u26636,\u26637,\u26638,\u2663J,\u2663K}"); 
            put(0b0000000111110011_0000000010000111_0000000010011100_0000000110000011L, "{\u26606,\u26607,\u2660K,\u2660A,\u26618,\u26619,\u266110,\u2661K,\u26626,\u26627,\u26628,\u2662K,\u26636,\u26637,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110000111_0000000010101001_0000000110110010_0000000001000101L, "{\u26606,\u26608,\u2660Q,\u26617,\u266110,\u2661J,\u2661K,\u2661A,\u26626,\u26629,\u2662J,\u2662K,\u26636,\u26637,\u26638,\u2663K,\u2663A}"); 
            put(0b0000000000111110_0000000110100000_0000000110101001_0000000001101001L, "{\u26606,\u26609,\u2660J,\u2660Q,\u26616,\u26619,\u2661J,\u2661K,\u2661A,\u2662J,\u2662K,\u2662A,\u26637,\u26638,\u26639,\u266310,\u2663J}"); 
            put(0b0000000100000000_0000000110110110_0000000001000111_0000000110001001L, "{\u26606,\u26609,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u2661Q,\u26627,\u26628,\u266210,\u2662J,\u2662K,\u2662A,\u2663A}"); 
            put(0b0000000111101111_0000000111111101_0000000111100011_0000000011010111L, "{\u26606,\u26607,\u26608,\u266010,\u2660Q,\u2660K,\u26616,\u26617,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26638,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000010001010_0000000111000011_0000000101000100_0000000100000001L, "{\u26606,\u2660A,\u26618,\u2661Q,\u2661A,\u26626,\u26627,\u2662Q,\u2662K,\u2662A,\u26637,\u26639,\u2663K}"); 
            put(0b0000000100100101_0000000011010110_0000000110110000_0000000111100111L, "{\u26606,\u26607,\u26608,\u2660J,\u2660Q,\u2660K,\u2660A,\u266110,\u2661J,\u2661K,\u2661A,\u26627,\u26628,\u266210,\u2662Q,\u2662K,\u26636,\u26638,\u2663J,\u2663A}"); 
            put(0b0000000101110110_0000000101110100_0000000000101010_0000000001110010L, "{\u26607,\u266010,\u2660J,\u2660Q,\u26617,\u26619,\u2661J,\u26628,\u266210,\u2662J,\u2662Q,\u2662A,\u26637,\u26638,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000110011011_0000000111110101_0000000011001011_0000000100010100L, "{\u26608,\u266010,\u2660A,\u26616,\u26617,\u26619,\u2661Q,\u2661K,\u26626,\u26628,\u266210,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u266310,\u2663K,\u2663A}"); 
            put(0b0000000010001101_0000000110011110_0000000111000010_0000000101000000L, "{\u2660Q,\u2660A,\u26617,\u2661Q,\u2661K,\u2661A,\u26627,\u26628,\u26629,\u266210,\u2662K,\u2662A,\u26636,\u26638,\u26639,\u2663K}"); 
            put(0b0000000010100010_0000000010110010_0000000011110001_0000000001101010L, "{\u26607,\u26609,\u2660J,\u2660Q,\u26616,\u266110,\u2661J,\u2661Q,\u2661K,\u26627,\u266210,\u2662J,\u2662K,\u26637,\u2663J,\u2663K}"); 
            put(0b0000000101000011_0000000101100100_0000000010011111_0000000100111000L, "{\u26609,\u266010,\u2660J,\u2660A,\u26616,\u26617,\u26618,\u26619,\u266110,\u2661K,\u26628,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u2663Q,\u2663A}"); 
            put(0b0000000111111100_0000000001000110_0000000101001110_0000000010001001L, "{\u26606,\u26609,\u2660K,\u26617,\u26618,\u26619,\u2661Q,\u2661A,\u26627,\u26628,\u2662Q,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000000100101_0000000100111111_0000000100110101_0000000110001011L, "{\u26606,\u26607,\u26609,\u2660K,\u2660A,\u26616,\u26618,\u266110,\u2661J,\u2661A,\u26626,\u26627,\u26628,\u26629,\u266210,\u2662J,\u2662A,\u26636,\u26638,\u2663J}"); 
            put(0b0000000111011011_0000000011100001_0000000110101111_0000000111000100L, "{\u26608,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u26619,\u2661J,\u2661K,\u2661A,\u26626,\u2662J,\u2662Q,\u2662K,\u26636,\u26637,\u26639,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000111101111_0000000010101011_0000000010001001_0000000110011111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660K,\u2660A,\u26616,\u26619,\u2661K,\u26626,\u26627,\u26629,\u2662J,\u2662K,\u26636,\u26637,\u26638,\u26639,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110000000_0000000110011010_0000000001111111_0000000000100100L, "{\u26608,\u2660J,\u26616,\u26617,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u26627,\u26629,\u266210,\u2662K,\u2662A,\u2663K,\u2663A}"); 
            put(0b0000000000111010_0000000110010100_0000000001111011_0000000001011100L, "{\u26608,\u26609,\u266010,\u2660Q,\u26616,\u26617,\u26619,\u266110,\u2661J,\u2661Q,\u26628,\u266210,\u2662K,\u2662A,\u26637,\u26639,\u266310,\u2663J}"); 
            put(0b0000000001001111_0000000101111100_0000000011111101_0000000001110001L, "{\u26606,\u266010,\u2660J,\u2660Q,\u26616,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u26638,\u26639,\u2663Q}"); 
            put(0b0000000001111011_0000000000001011_0000000101010001_0000000100110000L, "{\u266010,\u2660J,\u2660A,\u26616,\u266110,\u2661Q,\u2661A,\u26626,\u26627,\u26629,\u26636,\u26637,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000101111001_0000000111100110_0000000010101001_0000000000101100L, "{\u26608,\u26609,\u2660J,\u26616,\u26619,\u2661J,\u2661K,\u26627,\u26628,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26639,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000100101001_0000000101100011_0000000100001011_0000000111101001L, "{\u26606,\u26609,\u2660J,\u2660Q,\u2660K,\u2660A,\u26616,\u26617,\u26619,\u2661A,\u26626,\u26627,\u2662J,\u2662Q,\u2662A,\u26636,\u26639,\u2663J,\u2663A}"); 
            put(0b0000000011011110_0000000110001011_0000000010001111_0000000100010010L, "{\u26607,\u266010,\u2660A,\u26616,\u26617,\u26618,\u26619,\u2661K,\u26626,\u26627,\u26629,\u2662K,\u2662A,\u26637,\u26638,\u26639,\u266310,\u2663Q,\u2663K}"); 
            put(0b0000000010000100_0000000011000000_0000000111010101_0000000110011110L, "{\u26607,\u26608,\u26609,\u266010,\u2660K,\u2660A,\u26616,\u26618,\u266110,\u2661Q,\u2661K,\u2661A,\u2662Q,\u2662K,\u26638,\u2663K}"); 
            put(0b0000000000110100_0000000101101101_0000000011011100_0000000010111011L, "{\u26606,\u26607,\u26609,\u266010,\u2660J,\u2660K,\u26618,\u26619,\u266110,\u2661Q,\u2661K,\u26626,\u26628,\u26629,\u2662J,\u2662Q,\u2662A,\u26638,\u266310,\u2663J}"); 
            put(0b0000000001001111_0000000100100010_0000000101011101_0000000000011101L, "{\u26606,\u26608,\u26609,\u266010,\u26616,\u26618,\u26619,\u266110,\u2661Q,\u2661A,\u26627,\u2662J,\u2662A,\u26636,\u26637,\u26638,\u26639,\u2663Q}"); 
            put(0b0000000110101000_0000000010010011_0000000111111001_0000000001011011L, "{\u26606,\u26607,\u26609,\u266010,\u2660Q,\u26616,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u266210,\u2662K,\u26639,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000101011000_0000000111001011_0000000001010110_0000000010100010L, "{\u26607,\u2660J,\u2660K,\u26617,\u26618,\u266110,\u2661Q,\u26626,\u26627,\u26629,\u2662Q,\u2662K,\u2662A,\u26639,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000010001100_0000000101001000_0000000101111100_0000000011111110L, "{\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660Q,\u2660K,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u2661A,\u26629,\u2662Q,\u2662A,\u26638,\u26639,\u2663K}"); 
            put(0b0000000000110011_0000000100001011_0000000001111101_0000000100111110L, "{\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660A,\u26616,\u26618,\u26619,\u266110,\u2661J,\u2661Q,\u26626,\u26627,\u26629,\u2662A,\u26636,\u26637,\u266310,\u2663J}"); 
            put(0b0000000111111101_0000000111101110_0000000110100010_0000000011010000L, "{\u266010,\u2660Q,\u2660K,\u26617,\u2661J,\u2661K,\u2661A,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26636,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110000111_0000000001011000_0000000111001001_0000000101010001L, "{\u26606,\u266010,\u2660Q,\u2660A,\u26616,\u26619,\u2661Q,\u2661K,\u2661A,\u26629,\u266210,\u2662Q,\u26636,\u26637,\u26638,\u2663K,\u2663A}"); 
            put(0b0000000111000110_0000000000100010_0000000001010100_0000000011100110L, "{\u26607,\u26608,\u2660J,\u2660Q,\u2660K,\u26618,\u266110,\u2661Q,\u26627,\u2662J,\u26637,\u26638,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110101011_0000000110100110_0000000000101111_0000000001000011L, "{\u26606,\u26607,\u2660Q,\u26616,\u26617,\u26618,\u26619,\u2661J,\u26627,\u26628,\u2662J,\u2662K,\u2662A,\u26636,\u26637,\u26639,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000110000101_0000000100101110_0000000000011100_0000000001100000L, "{\u2660J,\u2660Q,\u26618,\u26619,\u266110,\u26627,\u26628,\u26629,\u2662J,\u2662A,\u26636,\u26638,\u2663K,\u2663A}"); 
            put(0b0000000000101111_0000000011010010_0000000000110001_0000000011000111L, "{\u26606,\u26607,\u26608,\u2660Q,\u2660K,\u26616,\u266110,\u2661J,\u26627,\u266210,\u2662Q,\u2662K,\u26636,\u26637,\u26638,\u26639,\u2663J}"); 
            put(0b0000000111100011_0000000111001000_0000000101000101_0000000001011111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660Q,\u26616,\u26618,\u2661Q,\u2661A,\u26629,\u2662Q,\u2662K,\u2662A,\u26636,\u26637,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000101110001_0000000000111011_0000000010110001_0000000000010110L, "{\u26607,\u26608,\u266010,\u26616,\u266110,\u2661J,\u2661K,\u26626,\u26627,\u26629,\u266210,\u2662J,\u26636,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000111100011_0000000101100101_0000000001001100_0000000100111000L, "{\u26609,\u266010,\u2660J,\u2660A,\u26618,\u26619,\u2661Q,\u26626,\u26628,\u2662J,\u2662Q,\u2662A,\u26636,\u26637,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000100110000_0000000010001000_0000000111000111_0000000001100001L, "{\u26606,\u2660J,\u2660Q,\u26616,\u26617,\u26618,\u2661Q,\u2661K,\u2661A,\u26629,\u2662K,\u266310,\u2663J,\u2663A}"); 
            put(0b0000000111110001_0000000001010001_0000000001001001_0000000100000110L, "{\u26607,\u26608,\u2660A,\u26616,\u26619,\u2661Q,\u26626,\u266210,\u2662Q,\u26636,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000111111110_0000000110000000_0000000100001000_0000000100001000L, "{\u26609,\u2660A,\u26619,\u2661A,\u2662K,\u2662A,\u26637,\u26638,\u26639,\u266310,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000110110101_0000000100011000_0000000110011100_0000000110101000L, "{\u26609,\u2660J,\u2660K,\u2660A,\u26618,\u26619,\u266110,\u2661K,\u2661A,\u26629,\u266210,\u2662A,\u26636,\u26638,\u266310,\u2663J,\u2663K,\u2663A}"); 
            put(0b0000000001011010_0000000010100100_0000000011001100_0000000110001011L, "{\u26606,\u26607,\u26609,\u2660K,\u2660A,\u26618,\u26619,\u2661Q,\u2661K,\u26628,\u2662J,\u2662K,\u26637,\u26639,\u266310,\u2663Q}"); 
            put(0b0000000101011011_0000000010110011_0000000010000000_0000000110111010L, "{\u26607,\u26609,\u266010,\u2660J,\u2660K,\u2660A,\u2661K,\u26626,\u26627,\u266210,\u2662J,\u2662K,\u26636,\u26637,\u26639,\u266310,\u2663Q,\u2663A}"); 
            put(0b0000000111011100_0000000100010100_0000000010010001_0000000100001010L, "{\u26607,\u26609,\u2660A,\u26616,\u266110,\u2661K,\u26628,\u266210,\u2662A,\u26638,\u26639,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000100010010_0000000011100111_0000000010110011_0000000101001100L, "{\u26608,\u26609,\u2660Q,\u2660A,\u26616,\u26617,\u266110,\u2661J,\u2661K,\u26626,\u26627,\u26628,\u2662J,\u2662Q,\u2662K,\u26637,\u266310,\u2663A}"); 
            put(0b0000000000001110_0000000001101110_0000000100100000_0000000111101111L, "{\u26606,\u26607,\u26608,\u26609,\u2660J,\u2660Q,\u2660K,\u2660A,\u2661J,\u2661A,\u26627,\u26628,\u26629,\u2662J,\u2662Q,\u26637,\u26638,\u26639}"); 
            put(0b0000000111010101_0000000100100110_0000000001101010_0000000110001001L, "{\u26606,\u26609,\u2660K,\u2660A,\u26617,\u26619,\u2661J,\u2661Q,\u26627,\u26628,\u2662J,\u2662A,\u26636,\u26638,\u266310,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000010110011_0000000000101000_0000000011101010_0000000000111111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660J,\u26617,\u26619,\u2661J,\u2661Q,\u2661K,\u26629,\u2662J,\u26636,\u26637,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000001110100_0000000100000111_0000000110110010_0000000111001111L, "{\u26606,\u26607,\u26608,\u26609,\u2660Q,\u2660K,\u2660A,\u26617,\u266110,\u2661J,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u2662A,\u26638,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000001111110_0000000100011010_0000000110101111_0000000101101101L, "{\u26606,\u26608,\u26609,\u2660J,\u2660Q,\u2660A,\u26616,\u26617,\u26618,\u26619,\u2661J,\u2661K,\u2661A,\u26627,\u26629,\u266210,\u2662A,\u26637,\u26638,\u26639,\u266310,\u2663J,\u2663Q}"); 
            put(0b0000000010101000_0000000001111101_0000000000001011_0000000001000000L, "{\u2660Q,\u26616,\u26617,\u26619,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662Q,\u26639,\u2663J,\u2663K}"); 
            put(0b0000000100101001_0000000101101101_0000000100101101_0000000100111111L, "{\u26606,\u26607,\u26608,\u26609,\u266010,\u2660J,\u2660A,\u26616,\u26618,\u26619,\u2661J,\u2661A,\u26626,\u26628,\u26629,\u2662J,\u2662Q,\u2662A,\u26636,\u26639,\u2663J,\u2663A}"); 
            put(0b0000000111100000_0000000000000010_0000000011110010_0000000010110011L, "{\u26606,\u26607,\u266010,\u2660J,\u2660K,\u26617,\u266110,\u2661J,\u2661Q,\u2661K,\u26627,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
            put(0b0000000000101000_0000000101011100_0000000101100110_0000000000101000L, "{\u26609,\u2660J,\u26617,\u26618,\u2661J,\u2661Q,\u2661A,\u26628,\u26629,\u266210,\u2662Q,\u2662A,\u26639,\u2663J}"); 
            put(0b0000000000000010_0000000111101000_0000000111111010_0000000011011110L, "{\u26607,\u26608,\u26609,\u266010,\u2660Q,\u2660K,\u26617,\u26619,\u266110,\u2661J,\u2661Q,\u2661K,\u2661A,\u26629,\u2662J,\u2662Q,\u2662K,\u2662A,\u26637}"); 
            put(0b0000000100001010_0000000100010010_0000000011101000_0000000111100101L, "{\u26606,\u26608,\u2660J,\u2660Q,\u2660K,\u2660A,\u26619,\u2661J,\u2661Q,\u2661K,\u26627,\u266210,\u2662A,\u26637,\u26639,\u2663A}"); 
            put(0b0000000011001101_0000000000011110_0000000001111000_0000000100100011L, "{\u26606,\u26607,\u2660J,\u2660A,\u26619,\u266110,\u2661J,\u2661Q,\u26627,\u26628,\u26629,\u266210,\u26636,\u26638,\u26639,\u2663Q,\u2663K}"); 
            put(0b0000000101110010_0000000000100010_0000000110100100_0000000010101111L, "{\u26606,\u26607,\u26608,\u26609,\u2660J,\u2660K,\u26618,\u2661J,\u2661K,\u2661A,\u26627,\u2662J,\u26637,\u266310,\u2663J,\u2663Q,\u2663A}"); 
            put(0b0000000010111001_0000000100011011_0000000001000011_0000000010100111L, "{\u26606,\u26607,\u26608,\u2660J,\u2660K,\u26616,\u26617,\u2661Q,\u26626,\u26627,\u26629,\u266210,\u2662A,\u26636,\u26639,\u266310,\u2663J,\u2663K}"); 
            put(0b0000000000011111_0000000100111101_0000000101001100_0000000110110010L, "{\u26607,\u266010,\u2660J,\u2660K,\u2660A,\u26618,\u26619,\u2661Q,\u2661A,\u26626,\u26628,\u26629,\u266210,\u2662J,\u2662A,\u26636,\u26637,\u26638,\u26639,\u266310}"); 
            put(0b0000000011111000_0000000101000111_0000000111100111_0000000110010101L, "{\u26606,\u26608,\u266010,\u2660K,\u2660A,\u26616,\u26617,\u26618,\u2661J,\u2661Q,\u2661K,\u2661A,\u26626,\u26627,\u26628,\u2662Q,\u2662A,\u26639,\u266310,\u2663J,\u2663Q,\u2663K}"); 
            put(0b0000000111100000_0000000101111000_0000000010011110_0000000110111011L, "{\u26606,\u26607,\u26609,\u266010,\u2660J,\u2660K,\u2660A,\u26617,\u26618,\u26619,\u266110,\u2661K,\u26629,\u266210,\u2662J,\u2662Q,\u2662A,\u2663J,\u2663Q,\u2663K,\u2663A}"); 
        }
    };

    @Test
    void toStringWorksSomeValidCardSet() {
        for(long pkCardSet : toStringList.keySet())
            assertEquals(toStringList.get(pkCardSet),  PackedCardSet.toString(pkCardSet));
    }
}

