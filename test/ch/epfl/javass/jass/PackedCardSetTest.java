package ch.epfl.javass.jass;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    void getWorksWithSomeCorrectInputs() {
        Card.Color[] colors = getAllColors();
        Card.Rank[] ranks = getAllRanks();
        int[] allCardsFromTheBeginning = new int[4 * 9];
        int cardsPerRank = 9;
        long packedCardSet = PackedCardSet.EMPTY;
        for (int j = 0 ; j < colors.length ; ++j) {
            System.out.println("j = " + j);
            for (int i = 0 ; i < ranks.length ; ++i) {
                System.out.println("i = " + i);
                int pkCard = PackedCard.pack(colors[j], ranks[i]);
                System.out.println(PackedCard.toString(pkCard));
                allCardsFromTheBeginning[(cardsPerRank * j) + i] = pkCard;

                packedCardSet = PackedCardSet.add(packedCardSet, pkCard);

                for (int k = 0 ; k <= cardsPerRank * i + j ; ++k) {
                    System.out.println("k");
                    System.out.println(PackedCard.toString(PackedCardSet.get(packedCardSet, k)));
                }
                System.out.println();
            }
        }
    }
}