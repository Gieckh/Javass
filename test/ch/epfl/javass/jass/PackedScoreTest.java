




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
            // lance l'exception sur le 1er assert quand tooBigTricks = 16 > 2^4 -1 , mais est ce que ca doit lancer false ou exception ?
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
    
    @Test
    void isValidWorksForAllValidScores() {
        for (int r = 0; r <= 9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertTrue(PackedScore.isValid((long)j << 45 | (long)c << 36 | (long)r <<32 | (long)j<< 13 | (long)c << 4 | (long)r)); 
                }
            }
        }
        assertTrue(PackedScore.isValid(2000L << 45 | 257L << 36 | 9L <<32 | 2000L<< 13 | 257L << 4 | 9L));
    }

    @Test
    void isValidWorksForSomeInvalidScores() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertFalse(PackedScore.isValid(1L << 56 | (long)j << 45 | (long)c << 36 | (long)r <<32 | (long)j<< 13 | (long)c << 4 | (long)r));
                    assertFalse(PackedScore.isValid((long)j << 45 | (long)c << 36 | (long)r <<32 | 1L <<24 | (long)j<< 13 | (long)c << 4 | (long)r));
                }
            }
        }
    }
    
    /**
     * pack test
     */
    @Test
    void packWorks() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals((long)j << 45 | (long)c << 36 | (long)r <<32 | (long)j<< 13 | (long)c << 4 | (long)r, 
                    PackedScore.pack(r, c, j, r, c, j));
                }
            }
        }
    }
    @Test
    void turnTricksWorks() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals(r, 
                            PackedScore.turnTricks(PackedScore.pack(r, c, j, r, c, j),TeamId.TEAM_1));
                    assertEquals(r, 
                            PackedScore.turnTricks(PackedScore.pack(r, c, j, r, c, j),TeamId.TEAM_2));
                }
            }
        }
    }
    /*
     * turnPointsWorks
     */
    @Test 
    void turnPointsWorks() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals(c, 
                            PackedScore.turnPoints(PackedScore.pack(r, c, j, r, c, j),TeamId.TEAM_1));
                    assertEquals(c, 
                            PackedScore.turnPoints(PackedScore.pack(r, c, j, r, c, j),TeamId.TEAM_2));
                }
            }
        }
    }
    /*
     * gamePoints
     */
    @Test
    void gamePointsWorks() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals(j, 
                            PackedScore.gamePoints(PackedScore.pack(r, c, j, r, c, j),TeamId.TEAM_1));
                    assertEquals(j, 
                            PackedScore.gamePoints(PackedScore.pack(r, c, j, r, c, j),TeamId.TEAM_2));
                }
            }
        }
    }
    /*
     * totalPoints
     */
    @Test
    void totalPointWorks() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals(j+c, 
                            PackedScore.totalPoints(PackedScore.pack(r, c, j, r, c, j),TeamId.TEAM_1));
                    assertEquals(j+c, 
                            PackedScore.totalPoints(PackedScore.pack(r, c, j, r, c, j),TeamId.TEAM_2));
                }
            }
        } 
    }
    /**
     * 
     */
    @Test
    void PackedScoreWorks() {
        long s = PackedScore.INITIAL;
        String score = PackedScore.toString(s);
        for (int i = 0; i < Jass.TRICKS_PER_TURN; ++i) {
          int p = (i == 0 ? 13 : 18);
          TeamId w = (i % 2 == 0 ? TeamId.TEAM_1 : TeamId.TEAM_2);
          s = PackedScore.withAdditionalTrick(s, w, p);
          score += PackedScore.toString(s);
        }
        s = PackedScore.nextTurn(s);
        score += PackedScore.toString(s);
        assertEquals("(0,0,0)/(0,0,0)(1,13,0)"
                + "/(0,0,0)(1,13,0)/(1,18,0)(2,31,0)"
                + "/(1,18,0)(2,31,0)/(2,36,0)(3,49,0)"
                + "/(2,36,0)(3,49,0)/(3,54,0)(4,67,0)"
                + "/(3,54,0)(4,67,0)/(4,72,0)(5,85,0)"
                + "/(4,72,0)(0,0,85)/(0,0,72)",
                score);
    }
    
}



