package ch.epfl.javass.jass;

import org.junit.jupiter.api.Test;

import java.util.SplittableRandom;

import static ch.epfl.javass.jass.PackedScore.isValid;
import static ch.epfl.javass.jass.PackedScore.pack;
import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;

import static org.junit.jupiter.api.Assertions.*;

//TODO: Fill the methods
class PackedScoreTest {
    private final static int MAX_TRICKS_PER_TURN = 9;
    private final static int MAX_POINTS_PER_TURN = 257;
    private final static int MAX_POINTS_PER_GAME = 2000;

    @Test //Runs in around 8s. (for around 15.5M "pack" calls)
    void isValidReturnsTrueWithAllValidTricks() { //Symmetric
        SplittableRandom rng = newRandom();
        int j1Step = rng.nextInt(1, 150);
        int j2Step = rng.nextInt(1, 150);
        int k1Step = rng.nextInt(20, 150);
        int k2Step = rng.nextInt(20, 150);

        System.out.println("j1Step : " + j1Step);
        System.out.println("j2Step : " + j2Step);
        System.out.println("k1Step : " + k1Step);
        System.out.println("k2Step : " + k2Step);

        for (int i1 = 0 ; i1 <= MAX_TRICKS_PER_TURN ; ++i1) {
            for (int j1 = 0 ; j1 <= MAX_POINTS_PER_TURN ; j1+=j1Step) {
                for(int k1 = 0 ; k1 <= MAX_POINTS_PER_GAME ; k1+=k1Step) {
                    for (int i2 = 0 ; i2 <= MAX_TRICKS_PER_TURN; ++i2) {
                        for(int j2 = 0 ; j2 <= MAX_POINTS_PER_TURN ; j2 += j2Step) {
                            for(int k2 = 0; k2 <= MAX_POINTS_PER_GAME ; k2 += k2Step) {
                                long packed1 = pack(i1, j1, k1, i2, j2, k2);

                                //Extreme cases for j's
                                long packed2 = pack(i1, MAX_POINTS_PER_TURN, k1, i2, j2, k2);
                                long packed3 = pack(i1, j1, k1, i2, MAX_POINTS_PER_TURN, k2);

                                //Extreme cases for k's
                                long packed4 = pack(i1, j1, MAX_POINTS_PER_GAME, i2, j2, k2);
                                long packed5 = pack(i1, j1, k1, i2, j2, MAX_POINTS_PER_GAME);

                                assertTrue(isValid(packed1));
                                assertTrue(isValid(packed2));
                                assertTrue(isValid(packed3));
                                assertTrue(isValid(packed4));
                                assertTrue(isValid(packed5));
                            }
                        }
                    }
                }
            }
        }
    }


    @Test //Runs in around 5s (for around 4.8M "pack" calls)
    void isValidReturnsTrueWithAllValidPointsPerTurn() { //Symmetric
        SplittableRandom rng = newRandom();
        int i1Step = rng.nextInt(2, MAX_TRICKS_PER_TURN + 1);
        int i2Step = rng.nextInt(2, MAX_TRICKS_PER_TURN + 1);
        int k1Step = rng.nextInt(200, 1000);
        int k2Step = rng.nextInt(180, 1000);

        System.out.println("i1Step : " + i1Step);
        System.out.println("i2Step : " + i2Step);
        System.out.println("k1Step : " + k1Step);
        System.out.println("k2Step : " + k2Step);

        for (int i1 = 0 ; i1 <= MAX_TRICKS_PER_TURN ; i1 += i1Step) {
            for (int j1 = 0 ; j1 <= MAX_POINTS_PER_TURN ; ++j1) {
                for(int k1 = 0 ; k1 <= MAX_POINTS_PER_GAME ; k1 += k1Step) {
                    for (int i2 = 0 ; i2 <= MAX_TRICKS_PER_TURN; i2 += i2Step) {
                        for(int j2 = 0 ; j2 <= MAX_POINTS_PER_TURN ; ++j2) {
                            for(int k2 = 0; k2 <= MAX_POINTS_PER_GAME ; k2 += k2Step) {
                                long packed1 = pack(i1, j1, k1, i2, j2, k2);

                                //Extreme cases for i's
                                long packed2 = pack(MAX_TRICKS_PER_TURN, j1, k1, i2, j2, k2);
                                long packed3 = pack(i1, j1, k1, MAX_TRICKS_PER_TURN, j2, k2);

                                //Extreme cases for k's
                                long packed4 = pack(i1, j1, MAX_POINTS_PER_GAME, i2, j2, k2);
                                long packed5 = pack(i1, j1, k1, i2, j2, MAX_POINTS_PER_GAME);

                                assertTrue(isValid(packed1));
                                assertTrue(isValid(packed2));
                                assertTrue(isValid(packed3));
                                assertTrue(isValid(packed4));
                                assertTrue(isValid(packed5));
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO: modify
    @Test
    void isValidReturnsFalseWithTooBigValuesForTeam1() {
        SplittableRandom rng = newRandom();
        for (int i = 0 ; i < RANDOM_ITERATIONS ; ++i) {
            int tooBigTricks = MAX_TRICKS_PER_TURN + i + 1;
            int tooBigTurnPoints = MAX_POINTS_PER_TURN + i + 1;
            int tooBigGamePoints = MAX_POINTS_PER_GAME + i + 1;

            int tricks1 = rng.nextInt(MAX_TRICKS_PER_TURN);
            int tricks2 = rng.nextInt(MAX_TRICKS_PER_TURN);

            int turnPoints1 = rng.nextInt(MAX_POINTS_PER_TURN);
            int turnPoints2 = rng.nextInt(MAX_POINTS_PER_TURN);

            int gamePoints1 = rng.nextInt(MAX_POINTS_PER_GAME);
            int gamePoints2 = rng.nextInt(MAX_POINTS_PER_GAME);

            assertFalse(isValid(pack(tooBigTricks, turnPoints1, gamePoints1,
                    tricks2, turnPoints2, gamePoints2)));
            assertFalse(isValid(pack(tricks1, tooBigTurnPoints, gamePoints1,
                    tricks2, turnPoints2, gamePoints2)));
            assertFalse(isValid(pack(tricks1, turnPoints1, tooBigGamePoints,
                    tricks2, turnPoints2, gamePoints2)));
        }
    }
    @Test
    void isValidReturnsFalseWithTooBigValuesForTeam2() {

    }

    //TODO: the 2 following tests are already done by "pack" right ?
//    @Test
//    void isValidReturnsFalseWithNegativeValuesForTeam1() {
//
//    }
//    @Test
//    void isValidReturnsFalseWithNegativeValuesForTeam2() {
//
//    }


    @Test
    void packProducesCorrectPacked() {

    }


    @Test
    void turnTricksReturnsCorrectNumberOfTricks() {

    }

    @Test
    void turnPointsReturnsCorrectNumberOfPoints() {

    }

    @Test
    void gamePointsReturnsCorrectNumberOfPoints() {

    }

    @Test
    void totalPointsReturnsCorrectNumberOfPoints() {

    }


    @Test
    void withAdditionalTrickCalculatesTheRightScore() {

    }


    @Test
    void nextTurnPacksEverythingTheRightWay() {

    }
}
