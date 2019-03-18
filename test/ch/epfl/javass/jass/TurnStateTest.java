package ch.epfl.javass.jass;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;

import ch.epfl.javass.bits.Bits32;
import ch.epfl.javass.jass.Card.Color;

class TurnStateTest {

    @Test
    void initialWorks() {
        for (Color trump : Color.ALL) {
            SplittableRandom rng = newRandom();
            for (int i = 0; i < RANDOM_ITERATIONS; i++) {
                long pkScore = randomValidPkScore(rng);
                Score score = Score.ofPacked(pkScore);
                for (PlayerId firstPlayer : PlayerId.ALL) {
                    int pkTrick = PackedTrick.firstEmpty(trump, firstPlayer);
                    TurnState turn = TurnState.initial(trump, score,
                            firstPlayer);
                    assertEquals(pkScore, turn.packedScore());
                    assertEquals(PackedCardSet.ALL_CARDS,
                            turn.packedUnplayedCards());
                    assertEquals(pkTrick, turn.packedTrick());

                }
            }
        }
    }

    /**
     * Disons que ce test vérifie aussi les methodes : - packedScore() -
     * packedUnplayedCards() - packedtrick()
     * 
     * Si quelqu'un a une idée pour les tester à part, je suis preneur !
     */
    @Test
    void ofPackedComponentWorksWithValidComponent() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
            long pkScore = randomValidPkScore(rng);
            long pkUnplayedCards = randomValidCardSet(rng);
            int pkTrick = randomValidPkTrick(rng);
            TurnState turn = TurnState.ofPackedComponents(pkScore,
                    pkUnplayedCards, pkTrick);
            assertEquals(pkScore, turn.packedScore());
            assertEquals(pkUnplayedCards, turn.packedUnplayedCards());
            assertEquals(pkTrick, turn.packedTrick());
        }
    }

    @Test
    void ofPackedComponentWorksWithInvalidComponent() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
            long pkScore = rng.nextLong();
            long pkUnplayedCards = rng.nextLong();
            int pkTrick = rng.nextInt();
            if (!PackedScore.isValid(pkScore)
                    || !PackedCardSet.isValid(pkUnplayedCards)
                    || !PackedTrick.isValid(pkTrick)) {
                assertThrows(IllegalArgumentException.class, () -> {
                    TurnState.ofPackedComponents(pkScore, pkUnplayedCards,
                            pkTrick);
                });
            }
        }
    }

    @Test
    void scoreAndUnplayedCardsAndTrickWork() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
            long pkScore = randomValidPkScore(rng);
            long pkUnplayedCards = randomValidCardSet(rng);
            int pkTrick = randomValidPkTrick(rng);
            TurnState turn = TurnState.ofPackedComponents(pkScore,
                    pkUnplayedCards, pkTrick);
            assertEquals(Score.ofPacked(pkScore), turn.score());
            assertEquals(CardSet.ofPacked(pkUnplayedCards),
                    turn.unplayedCards());
            assertEquals(Trick.ofPacked(pkTrick), turn.trick());
        }
    }

    @Test
    void isTerminalWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
            long pkScore = randomValidPkScore(rng);
            long pkUnplayedCards = randomValidCardSet(rng);
            int pkTrick = randomValidPkTrick(rng);
            TurnState turn = TurnState.ofPackedComponents(pkScore,
                    pkUnplayedCards, pkTrick);
            if (turn.trick().isFull()) {
                turn = turn.withTrickCollected();
            }
            assertEquals(turn.packedTrick() == PackedTrick.INVALID,
                    turn.isTerminal());
        }
    }

    @Test
    void nextPlayerWorks() {
        SplittableRandom rng = newRandom();
        int count = 0;
        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
            long pkScore = randomValidPkScore(rng);
            long pkUnplayedCards = randomValidCardSet(rng);
            int pkTrick = randomValidPkTrick(rng);
            TurnState turn = TurnState.ofPackedComponents(pkScore,
                    pkUnplayedCards, pkTrick);
            if (PackedTrick.isFull(pkTrick)) {
                assertThrows(IllegalStateException.class, () -> {
                    turn.nextPlayer();
                });
            } else {
                assertEquals(
                        turn.trick()
                                .player(PackedTrick.size(turn.packedTrick())),
                        turn.nextPlayer());
            }
        }
    }

    @Test
    void withNewCardPlayedWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
            long pkScore = randomValidPkScore(rng);
            long pkUnplayedCards = randomValidCardSet(rng);
            int pkTrick = randomValidPkTrick(rng);
            TurnState turn = TurnState.ofPackedComponents(pkScore,
                    pkUnplayedCards, pkTrick);
            int pkCard = randomValidPkCard(rng);
            if (PackedTrick.isFull(turn.packedTrick())) {
                assertThrows(IllegalStateException.class, () -> {
                    turn.withNewCardPlayed(Card.ofPacked(pkCard));
                });
            } else {
                TurnState turn2 = TurnState.ofPackedComponents(
                        turn.packedScore(),
                        PackedCardSet.remove(turn.packedUnplayedCards(),
                                pkCard),
                        PackedTrick.withAddedCard(turn.packedTrick(), pkCard));
                TurnState turn3 = turn.withNewCardPlayed(Card.ofPacked(pkCard));
                assertEquals(turn2.trick(), turn3.trick());
                assertEquals(turn2.score(), turn3.score());
                assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
            }
        }
    }

    @Test
    void withTrickCollectedWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
            long pkScore = randomValidPkScore(rng);
            long pkUnplayedCards = randomValidCardSet(rng);
            int pkTrick = randomValidPkTrick(rng);
            TurnState turn = TurnState.ofPackedComponents(pkScore,
                    pkUnplayedCards, pkTrick);
            if (!PackedTrick.isFull(turn.packedTrick())) {
                assertThrows(IllegalStateException.class, () -> {
                    turn.withTrickCollected();
                });
            } else {
                TurnState turn2 = TurnState
                        .ofPackedComponents(
                                PackedScore.withAdditionalTrick(
                                        turn.packedScore(),
                                        PackedTrick
                                                .winningPlayer(
                                                        turn.packedTrick())
                                                .team(),
                                        PackedTrick.points(turn.packedTrick())),
                                turn.packedUnplayedCards(), turn.packedTrick());
                TurnState turn3 = turn.withTrickCollected();
                assertEquals(PackedTrick.nextEmpty(turn2.packedTrick()),
                        turn3.packedTrick());
                assertEquals(turn2.score(), turn3.score());
                assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
            }
        }
    }

    @Test
    void withNewCardPlayedAndTrickCollectedWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
            long pkScore = randomValidPkScore(rng);
            long pkUnplayedCards = randomValidCardSet(rng);
            int pkTrick = randomValidPkTrick(rng);
            TurnState turn = TurnState.ofPackedComponents(pkScore,
                    pkUnplayedCards, pkTrick);
            int pkCard = randomValidPkCard(rng);
            if (PackedTrick.isFull(turn.packedTrick())) {
                assertThrows(IllegalStateException.class, () -> {
                    turn.withNewCardPlayedAndTrickCollected(
                            Card.ofPacked(pkCard));
                });
            } else {
                TurnState turn2 = turn.withNewCardPlayed(Card.ofPacked(pkCard));
                TurnState turn3 = turn.withNewCardPlayedAndTrickCollected(
                        Card.ofPacked(pkCard));
                if (PackedTrick.isFull(turn2.packedTrick())) {
                    turn2 = turn2.withTrickCollected();
                }
                assertEquals(Integer.toBinaryString(turn2.packedTrick()),
                        Integer.toBinaryString(turn3.packedTrick()));
                assertEquals(turn2.score(), turn3.score());
                assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
            }
        }

    }

    private static long randomValidPkScore(SplittableRandom rng) {
        int t1 = rng.nextInt(9);
        int p1 = rng.nextInt(100);
        int g1 = rng.nextInt(2000);
        int t2 = rng.nextInt(9 - t1);
        int p2 = rng.nextInt(100 - p1);
        int g2 = rng.nextInt(2000 - g1);
        return PackedScore.pack(t1, p1, g1, t2, p2, g2);
    }

    private static int randomValidPkTrick(SplittableRandom rng) {
        int card3 = (rng.nextInt() % 4 == 0) ? PackedCard.INVALID
                : randomValidPkCard(rng);
        int card2 = (card3 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
                ? PackedCard.INVALID
                : randomValidPkCard(rng);
        int card1 = (card2 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
                ? PackedCard.INVALID
                : randomValidPkCard(rng);
        int card0 = (card1 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
                ? PackedCard.INVALID
                : randomValidPkCard(rng);
        int index = rng.nextInt(9);
        int player = rng.nextInt(4);
        int trump = rng.nextInt(4);
        return Bits32.pack(card0, 6, card1, 6, card2, 6, card3, 6, index, 4,
                player, 2, trump, 2);
    }

    private static long randomValidCardSet(SplittableRandom rng) {
        return ((rng.nextLong(0b1000000000) << 16
                | rng.nextLong(0b1000000000)) << 16
                | rng.nextLong(0b1000000000)) << 16
                | rng.nextLong(0b1000000000);
    }

    private static int randomValidPkCard(SplittableRandom rng) {
        return rng.nextInt(4) << 4 | rng.nextInt(9);
    }

}




//package ch.epfl.javass.jass;
//
//import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
//import static ch.epfl.test.TestRandomizer.newRandom;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.SplittableRandom;
//
//import org.junit.jupiter.api.Test;
//
//import ch.epfl.javass.bits.Bits32;
//import ch.epfl.javass.jass.Card.Color;
//import org.opentest4j.AssertionFailedError;
//
//class TurnStateTest {
//
//    @Test
//    void initialWorks() {
//        for (Color trump : Color.ALL) {
//            SplittableRandom rng = newRandom();
//            for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//                long pkScore = randomValidPkScore(rng);
//                if (PackedScore.isValid(pkScore)) {
//                    Score score = Score.ofPacked(pkScore);
//                    for (PlayerId firstPlayer : PlayerId.ALL) {
//                        int pkTrick = PackedTrick.firstEmpty(trump,
//                                firstPlayer);
//                        TurnState turn = TurnState.initial(trump, score,
//                                firstPlayer);
//                        assertEquals(pkScore, turn.packedScore());
//                        assertEquals(PackedCardSet.ALL_CARDS,
//                                turn.packedUnplayedCards());
//                        assertEquals(pkTrick, turn.packedTrick());
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Disons que ce test vérifie aussi les methodes : - packedScore() -
//     * packedUnplayedCards() - packedtrick()
//     * 
//     * Si quelqu'un a une idée pour les tester à part, je suis preneur !
//     */
//    @Test
//    void ofPackedComponentWorksWithValidComponent() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            assertEquals(pkScore, turn.packedScore());
//            assertEquals(pkUnplayedCards, turn.packedUnplayedCards());
//            assertEquals(pkTrick, turn.packedTrick());
//        }
//    }
//
//    @Test
//    void ofPackedComponentWorksWithInvalidComponent() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = rng.nextLong();
//            long pkUnplayedCards = rng.nextLong();
//            int pkTrick = rng.nextInt();
//
//            if (!PackedScore.isValid(pkScore)           ||
//                !PackedCardSet.isValid(pkUnplayedCards) ||
//                !PackedTrick.isValid(pkTrick))
//            {
//                assertThrows(IllegalArgumentException.class, () -> {
//                    TurnState.ofPackedComponents(pkScore, pkUnplayedCards, pkTrick);
//                });
//            }
//        }
//    }
//
//    @Test
//    void scoreAndUnplayedCardsAndTrickWork() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//
//            TurnState turn = TurnState.ofPackedComponents(pkScore, pkUnplayedCards, pkTrick);
//
//            assertEquals(Score.ofPacked(pkScore), turn.score());
//            assertEquals(CardSet.ofPacked(pkUnplayedCards), turn.unplayedCards());
//            assertEquals(Trick.ofPacked(pkTrick), turn.trick());
//        }
//    }
//
//    @Test
//    void isTerminalWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            System.out.println(Integer.toBinaryString(turn.packedTrick()));
//            assertEquals(turn.packedTrick() == PackedTrick.INVALID,
//                    turn.isTerminal());
//        }
//    }
//
//    @Test
//    void nextPlayerWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            if (PackedTrick.isFull(pkTrick)) {
//                assertThrows(IllegalStateException.class, () -> {
//                    turn.nextPlayer();
//                });
//            } else {
//                assertEquals(
//                        turn.trick()
//                                .player(PackedTrick.size(turn.packedTrick())),
//                        turn.nextPlayer());
//            }
//        }
//    }
//
//    @Test
//    void withNewCardPlayedWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            int pkCard = randomValidPkCard(rng);
//            if (PackedTrick.isFull(turn.packedTrick())) {
//                assertThrows(IllegalStateException.class, () -> {
//                    turn.withNewCardPlayed(Card.ofPacked(pkCard));
//                });
//            } else {
//                TurnState turn2 = TurnState.ofPackedComponents(
//                        turn.packedScore(),
//                        PackedCardSet.remove(turn.packedUnplayedCards(),
//                                pkCard),
//                        PackedTrick.withAddedCard(turn.packedTrick(), pkCard));
//                TurnState turn3 = turn.withNewCardPlayed(Card.ofPacked(pkCard));
//                assertEquals(turn2.trick(), turn3.trick());
//                assertEquals(turn2.score(), turn3.score());
//                assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
//            }
//        }
//    }
//
////    @Test return true
////    void randomValidWOrks(){
////        SplittableRandom rng = newRandom();
////        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
////            long pkScore = randomValidPkScore(rng);
////            long pkUnplayedCards = randomValidCardSet(rng);
////            int pkTrick = randomValidPkTrick(rng);
////            assertTrue(PackedScore.isValid(pkScore));
////            assertTrue(PackedCardSet.isValid(pkUnplayedCards));
////            assertTrue(PackedTrick.isValid(pkTrick));
////        }
////    }
//    @Test
//    void withTrickCollectedWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            if (!PackedTrick.isFull(turn.packedTrick())) {
//                assertThrows(IllegalStateException.class, () -> {
//                    turn.withTrickCollected();
//                });
//            } else {
//                //TODO: on essaye d'empaqueter un truc invalide dans ce test
//                
//               
//                System.out.println(PackedTrick.isValid(PackedTrick.INVALID));
//                
//                TurnState turn2 = TurnState
//                        .ofPackedComponents(
//                                PackedScore.withAdditionalTrick(
//                                        turn.packedScore(),
//                                        PackedTrick
//                                                .winningPlayer(
//                                                        turn.packedTrick())
//                                                .team(),
//                                        PackedTrick.points(turn.packedTrick())),
//                                PackedCardSet.ALL_CARDS,
//                                PackedTrick.nextEmpty(turn.packedTrick()));
//
//                TurnState turn3 = turn.withTrickCollected();
//                assertEquals(turn2.trick(), turn3.trick());
//                assertEquals(turn2.score(), turn3.score());
//                assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
//            }
//        }
//    }
//
//    @Test
//    void withNewCardPlayedAndTrickCollectedWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            int pkCard = randomValidPkCard(rng);
//            Card card = Card.ofPacked(pkCard);
//
//            if (PackedTrick.isFull(turn.packedTrick())) {
//                assertThrows(IllegalStateException.class, () -> {
//                    turn.withNewCardPlayedAndTrickCollected(
//                            card);
//                });
//            } else {
//                if (PackedTrick.isFull(
//                        turn.withNewCardPlayed(card).packedTrick()) )
//                {
//                    TurnState turn2 = turn.withNewCardPlayed(card).withTrickCollected();
//                    TurnState turn3 = turn.withNewCardPlayedAndTrickCollected(card);
//
//                    assertEquals(turn2.trick(), turn3.trick());
//                    assertEquals(turn2.score(), turn3.score());
//                    assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
//                } else {
//                    //TODO: [done]: replaced because assert compares references.
////                    assertEquals(turn.withNewCardPlayed(card),
////                            turn.withNewCardPlayedAndTrickCollected(card));
//
//                    TurnState theoreticalTurn = turn.withNewCardPlayed(card);
//                    TurnState practicalTurn = turn.withNewCardPlayedAndTrickCollected(card);
//                    //assertTrue(equalForTurns(theoreticalTurn, practicalTurn));
//                    assertEquals(theoreticalTurn.trick(), practicalTurn.trick());
//                    assertEquals(theoreticalTurn.score(), practicalTurn.score());
//                    assertEquals(theoreticalTurn.unplayedCards(), practicalTurn.unplayedCards());
//                }
//            }
//        }
//
//    }
//
//    private static long randomValidPkScore(SplittableRandom rng) {
//        int t1 = rng.nextInt(10);
//        int p1 = rng.nextInt(258);
//        int g1 = rng.nextInt(2000);
//        int t2 = rng.nextInt(10 - t1);
//        int p2 = rng.nextInt(258 - p1);
//        int g2 = rng.nextInt(2000 - g1);
//        return PackedScore.pack(t1, p1, g1, t2, p2, g2);
//    }
//
//    private static int randomValidPkTrick(SplittableRandom rng) {
//        int card3 = (rng.nextInt() % 4 == 0) ? PackedCard.INVALID
//                : randomValidPkCard(rng);
//        int card2 = (card3 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
//                ? PackedCard.INVALID
//                : randomValidPkCard(rng);
//        int card1 = (card2 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
//                ? PackedCard.INVALID
//                : randomValidPkCard(rng);
//        int card0 = (card1 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
//                ? PackedCard.INVALID
//                : randomValidPkCard(rng);
//        int index = rng.nextInt(9);
//        int player = rng.nextInt(4);
//        int trump = rng.nextInt(4);
//        return Bits32.pack(card0, 6, card1, 6, card2, 6, card3, 6, index, 4,
//                player, 2, trump, 2);
//    }
//
//    private static long randomValidCardSet(SplittableRandom rng) {
//        return ((rng.nextLong(0b1000000000) << 16
//                | rng.nextLong(0b1000000000)) << 16
//                | rng.nextLong(0b1000000000)) << 16
//                | rng.nextLong(0b1000000000);
//    }
//
//    private static int randomValidPkCard(SplittableRandom rng) {
//        return rng.nextInt(4) << 4 | rng.nextInt(9);
//    }
//
//
//    //Jsp pk "assertTrue(equal...(turn1, turn2));" ne semble pas marcher
//    private boolean equalForTurns(TurnState expectedTurn, TurnState obtainedTurn) {
//        return (expectedTurn.trick() == obtainedTurn.trick() &&
//                expectedTurn.score() == obtainedTurn.score() &&
//                expectedTurn.unplayedCards() == obtainedTurn.unplayedCards());
//    }
//    @Test
//    void testReferences() {
//        TurnState     turn = TurnState.ofPackedComponents(0L, PackedCardSet.ALL_CARDS, 0);
//        TurnState sameTurn = TurnState.ofPackedComponents(0L, PackedCardSet.ALL_CARDS, 0);
//        //assertNotEquals(turn, sameTurn);
//
//        assertThrows(AssertionFailedError.class, () -> {
//            assertEquals(turn, sameTurn);
//        });
//    }
//}
//
///** 
//
//
//import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
//import static ch.epfl.test.TestRandomizer.newRandom;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.util.SplittableRandom;
//
//import org.junit.jupiter.api.Test;
//
//import ch.epfl.javass.bits.Bits32;
//import ch.epfl.javass.jass.Card.Color;
//
//class TurnStateTest {
//
//    @Test
//    void initialWorks() {
//        for (Color trump : Color.ALL) {
//            SplittableRandom rng = newRandom();
//            for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//                long pkScore = randomValidPkScore(rng);
//                Score score = Score.ofPacked(pkScore);
//                for (PlayerId firstPlayer : PlayerId.ALL) {
//                    int pkTrick = PackedTrick.firstEmpty(trump, firstPlayer);
//                    TurnState turn = TurnState.initial(trump, score,
//                            firstPlayer);
//                    assertEquals(pkScore, turn.packedScore());
//                    assertEquals(PackedCardSet.ALL_CARDS,
//                            turn.packedUnplayedCards());
//                    assertEquals(pkTrick, turn.packedTrick());
//
//                }
//            }
//        }
//    }
//
//    /**
//     * Disons que ce test vérifie aussi les methodes : - packedScore() -
//     * packedUnplayedCards() - packedtrick()
//     * 
//     * Si quelqu'un a une idée pour les tester à part, je suis preneur !
//     *
//    @Test
//    void ofPackedComponentWorksWithValidComponent() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            assertEquals(pkScore, turn.packedScore());
//            assertEquals(pkUnplayedCards, turn.packedUnplayedCards());
//            assertEquals(pkTrick, turn.packedTrick());
//        }
//    }
//
//    @Test
//    void ofPackedComponentWorksWithInvalidComponent() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = rng.nextLong();
//            long pkUnplayedCards = rng.nextLong();
//            int pkTrick = rng.nextInt();
//            if (!PackedScore.isValid(pkScore)
//                    || !PackedCardSet.isValid(pkUnplayedCards)
//                    || !PackedTrick.isValid(pkTrick)) {
//                assertThrows(IllegalArgumentException.class, () -> {
//                    TurnState.ofPackedComponents(pkScore, pkUnplayedCards,
//                            pkTrick);
//                });
//            }
//        }
//    }
//
//    @Test
//    void scoreAndUnplayedCardsAndTrickWork() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            assertEquals(Score.ofPacked(pkScore), turn.score());
//            assertEquals(CardSet.ofPacked(pkUnplayedCards),
//                    turn.unplayedCards());
//            assertEquals(Trick.ofPacked(pkTrick), turn.trick());
//        }
//    }
//
//    @Test
//    void isTerminalWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            if (turn.trick().isFull()) {
//                turn = turn.withTrickCollected();
//            }
//            assertEquals(turn.packedTrick() == PackedTrick.INVALID,
//                    turn.isTerminal());
//        }
//    }
//
//    @Test
//    void nextPlayerWorks() {
//        SplittableRandom rng = newRandom();
//        int count = 0;
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            if (PackedTrick.isFull(pkTrick)) {
//                assertThrows(IllegalStateException.class, () -> {
//                    turn.nextPlayer();
//                });
//            } else {
//                assertEquals(
//                        turn.trick()
//                                .player(PackedTrick.size(turn.packedTrick())),
//                        turn.nextPlayer());
//            }
//        }
//    }
//
//    @Test
//    void withNewCardPlayedWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            int pkCard = randomValidPkCard(rng);
//            if (PackedTrick.isFull(turn.packedTrick())) {
//                assertThrows(IllegalStateException.class, () -> {
//                    turn.withNewCardPlayed(Card.ofPacked(pkCard));
//                });
//            } else {
//                TurnState turn2 = TurnState.ofPackedComponents(
//                        turn.packedScore(),
//                        PackedCardSet.remove(turn.packedUnplayedCards(),
//                                pkCard),
//                        PackedTrick.withAddedCard(turn.packedTrick(), pkCard));
//                TurnState turn3 = turn.withNewCardPlayed(Card.ofPacked(pkCard));
//                assertEquals(turn2.trick(), turn3.trick());
//                assertEquals(turn2.score(), turn3.score());
//                assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
//            }
//        }
//    }
//
//    @Test
//    void withTrickCollectedWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            if (!PackedTrick.isFull(turn.packedTrick())) {
//                assertThrows(IllegalStateException.class, () -> {
//                    turn.withTrickCollected();
//                });
//            } else {
//                TurnState turn2 = TurnState
//                        .ofPackedComponents(
//                                PackedScore.withAdditionalTrick(
//                                        turn.packedScore(),
//                                        PackedTrick
//                                                .winningPlayer(
//                                                        turn.packedTrick())
//                                                .team(),
//                                        PackedTrick.points(turn.packedTrick())),
//                                turn.packedUnplayedCards(), turn.packedTrick());
//                TurnState turn3 = turn.withTrickCollected();
//                assertEquals(PackedTrick.nextEmpty(turn2.packedTrick()),
//                        turn3.packedTrick());
//                assertEquals(turn2.score(), turn3.score());
//                assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
//            }
//        }
//    }
//
//    @Test
//    void withNewCardPlayedAndTrickCollectedWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; i++) {
//            long pkScore = randomValidPkScore(rng);
//            long pkUnplayedCards = randomValidCardSet(rng);
//            int pkTrick = randomValidPkTrick(rng);
//            TurnState turn = TurnState.ofPackedComponents(pkScore,
//                    pkUnplayedCards, pkTrick);
//            int pkCard = randomValidPkCard(rng);
//            if (PackedTrick.isFull(turn.packedTrick())) {
//                assertThrows(IllegalStateException.class, () -> {
//                    turn.withNewCardPlayedAndTrickCollected(
//                            Card.ofPacked(pkCard));
//                });
//            } else {
//                TurnState turn2 = turn.withNewCardPlayed(Card.ofPacked(pkCard));
//                TurnState turn3 = turn.withNewCardPlayedAndTrickCollected(
//                        Card.ofPacked(pkCard));
//                if (PackedTrick.isFull(turn2.packedTrick())) {
//                    turn2 = turn2.withTrickCollected();
//                }
//                assertEquals(Integer.toBinaryString(turn2.packedTrick()),
//                        Integer.toBinaryString(turn3.packedTrick()));
//                assertEquals(turn2.score(), turn3.score());
//                assertEquals(turn2.unplayedCards(), turn3.unplayedCards());
//            }
//        }
//
//    }
//
//    private static long randomValidPkScore(SplittableRandom rng) {
//        int t1 = rng.nextInt(9);
//        int p1 = rng.nextInt(150);
//        int g1 = rng.nextInt(2000);
//        int t2 = rng.nextInt(9 - t1);
//        int p2 = rng.nextInt(150 - p1);
//        int g2 = rng.nextInt(2000 - g1);
//        return PackedScore.pack(t1, p1, g1, t2, p2, g2);
//    }
//
//    private static int randomValidPkTrick(SplittableRandom rng) {
//        int card3 = (rng.nextInt() % 4 == 0) ? PackedCard.INVALID
//                : randomValidPkCard(rng);
//        int card2 = (card3 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
//                ? PackedCard.INVALID
//                : randomValidPkCard(rng);
//        int card1 = (card2 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
//                ? PackedCard.INVALID
//                : randomValidPkCard(rng);
//        int card0 = (card1 == PackedCard.INVALID && rng.nextInt() % 4 == 0)
//                ? PackedCard.INVALID
//                : randomValidPkCard(rng);
//        int index = rng.nextInt(9);
//        int player = rng.nextInt(4);
//        int trump = rng.nextInt(4);
//        return Bits32.pack(card0, 6, card1, 6, card2, 6, card3, 6, index, 4,
//                player, 2, trump, 2);
//    }
//
//    private static long randomValidCardSet(SplittableRandom rng) {
//        return ((rng.nextLong(0b1000000000) << 16
//                | rng.nextLong(0b1000000000)) << 16
//                | rng.nextLong(0b1000000000)) << 16
//                | rng.nextLong(0b1000000000);
//    }
//
//    private static int randomValidPkCard(SplittableRandom rng) {
//        return rng.nextInt(4) << 4 | rng.nextInt(9);
//    }
//
//    }
//
//*/