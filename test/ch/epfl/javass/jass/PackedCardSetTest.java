package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits64;
import ch.epfl.javass.jass.PackedCardSet;

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
}