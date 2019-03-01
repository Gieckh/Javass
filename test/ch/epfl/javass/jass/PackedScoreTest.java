package ch.epfl.javass.jass;


import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static ch.epfl.javass.jass.PackedScore.isValid;
import static ch.epfl.javass.jass.PackedScore.pack;
import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import ch.epfl.javass.bits.Bits32;
import ch.epfl.javass.bits.Bits64;
import ch.epfl.javass.jass.TeamId;

//TODO ???????????????????????????????????????????????????????????????????????????????????????????
interface TestValid {
    void test(int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints);
}

interface TestInvalid {
    void test(int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long rd, long invalidCard, int trickPoints);
}


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
  //  @Test
   /* void isValidReturnsFalseWithTooBigValuesForTeam1() {
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
    */
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
    void isValidWorksForSomeInvalidScores1() {
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
     * turnPointsWorks : deja testé donc marche
     */
    /*@Test 
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
   */ 
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
    
    




    void WorksForSomeValidScores(TestValid t) {
        SplittableRandom rng = newRandom();
        for (int gamePoints1 = 0; gamePoints1 <= 2000; gamePoints1 += rng.nextInt(500) + 1)
            for (int turnPoints1 = 0; turnPoints1 <= 257; turnPoints1 += rng.nextInt(50) + 1)
                for (int turnTricks1 = 0; turnTricks1 <= 9; ++turnTricks1)
                    for (int gamePoints2 = 0; gamePoints2 <= 2000; gamePoints2 += rng.nextInt(500) + 1)
                        for (int turnPoints2 = 0; turnPoints2 <= 257; turnPoints2 += rng.nextInt(50) + 1)
                            for (int turnTricks2 = 0; turnTricks2 <= 9; ++turnTricks2)
                                t.test(gamePoints1, turnPoints1, turnTricks1, gamePoints2, turnPoints2, turnTricks2,
                                        ((((long)gamePoints2 << 9 | (long)turnPoints2) << 4 | (long)turnTricks2) <<32 |
                                                (((long)gamePoints1<< 9 | (long)turnPoints1) << 4 | (long)turnTricks1)),
                                        rng.nextInt(10));
    }

    void WorksForSomeInvalidScores(TestInvalid t) {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int gamePoints1 = rng.nextInt(1<<11);
            int gamePoints2 = rng.nextInt(1<<11);
            int turnPoints1 = rng.nextInt(1<<9);
            int turnPoints2 = rng.nextInt(1<<9);
            int turnTricks1 = rng.nextInt(1<<4);
            int turnTricks2 = rng.nextInt(1<<4);
            int trickPoints = rng.nextInt(10);
            long rd = 0;
            do rd = (rng.nextLong() & ~(Bits64.mask(0, 24)) << 32 | Bits64.mask(0, 24));
            while (rd == 0);
            long invalidCard = Bits64.pack(
                    (long) Bits32.pack(turnTricks1, 4, turnPoints1, 9 , gamePoints1, 11), 32,
                    (long) Bits32.pack(turnTricks2, 4, turnPoints2, 9 , gamePoints2, 11), 32);
            invalidCard |= (gamePoints1 <= 2000 && gamePoints2 <= 2000 && turnPoints1 <= 257 && turnPoints2 <= 257 && turnTricks1 <= 9 && turnTricks2 <= 9) ?  rd : 0L;
            t.test(gamePoints1, turnPoints1, turnTricks1, gamePoints2, turnPoints2, turnTricks2, rd, invalidCard, trickPoints);
        }
    }

    @Test
    void packedScoreScoreConstantsAreCorrect() throws Exception {
        assertEquals(0L, PackedScore.INITIAL);
    }

    @Test
    void isValidWorksForSomeValidScores() {
        WorksForSomeValidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints) -> {
                    assertTrue(PackedScore.isValid(Bits64.pack(
                            (long) Bits32.pack(turnTricks1, 4, turnPoints1, 9 , gamePoints1, 11), 32,
                            (long) Bits32.pack(turnTricks2, 4, turnPoints2, 9 , gamePoints2, 11), 32)));
                }
                );
    }

    @Test
    void isValidWorksForSomeInvalidScores() {
        WorksForSomeInvalidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long rd, long invalidCard, int trickPoints) -> {
                    assertFalse(PackedScore.isValid(invalidCard));
                });
    }

    @Test
    void packWorksForSomeValidScores() {
        WorksForSomeValidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints) -> {
                    assertEquals(validCard, PackedScore.pack(turnTricks1, turnPoints1, gamePoints1, turnTricks2, turnPoints2, gamePoints2));
                });
    }

    @Test
    void packWorksForSomeInvalidScores() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            assertThrows(AssertionError.class, () -> {
                int gamePoints1, gamePoints2, turnPoints1, turnPoints2, turnTricks1, turnTricks2;
                do {
                    gamePoints1 = rng.nextInt(1<<11);
                    gamePoints2 = rng.nextInt(1<<11);
                    turnPoints1 = rng.nextInt(1<<9);
                    turnPoints2 = rng.nextInt(1<<9);
                    turnTricks1 = rng.nextInt(1<<4);
                    turnTricks2 = rng.nextInt(1<<4);
                }
                while (gamePoints1 <= 2000 && gamePoints2 <= 2000 && turnPoints1 <= 257 && turnPoints2 <= 257 && turnTricks1 <= 9 && turnTricks2 <= 9);
                PackedScore.pack(turnTricks1, turnPoints1, gamePoints1, turnTricks2, turnPoints2, gamePoints2);
            });
        }
    }

    @Test
    void turnTricksWorksForSomeValidScores() {
        WorksForSomeValidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints) -> {
                    assertEquals(turnTricks1,
                            PackedScore.turnTricks(validCard, TeamId.TEAM_1));
                    assertEquals(turnTricks2,
                            PackedScore.turnTricks(validCard, TeamId.TEAM_2));
                });
    }

    @Test
    void turnTricksWorksForSomeInvalidScores() {
        WorksForSomeInvalidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long rd, long invalidCard, int trickPoints) -> {
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.turnTricks(invalidCard, TeamId.TEAM_1);
                    });
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.turnTricks(invalidCard, TeamId.TEAM_2);
                    });
                });
    }

    @Test
    void turnPointsWorksForSomeValidScores() {
        WorksForSomeValidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints) -> {
                    assertEquals(turnPoints1,
                            PackedScore.turnPoints(validCard, TeamId.TEAM_1));
                    assertEquals(turnPoints2,
                            PackedScore.turnPoints(validCard, TeamId.TEAM_2));
                });
    }

    @Test
    void turnPointsWorksForSomeInvalidScores() {
        WorksForSomeInvalidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long rd, long invalidCard, int trickPoints) -> {
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.turnPoints(invalidCard, TeamId.TEAM_1);
                    });
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.turnPoints(invalidCard, TeamId.TEAM_2);
                    });
                });
    }

    @Test
    void gamePointsWorksForSomeValidScores() {
        WorksForSomeValidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints) -> {
                    assertEquals(gamePoints1,
                            PackedScore.gamePoints(validCard, TeamId.TEAM_1));
                    assertEquals(gamePoints2,
                            PackedScore.gamePoints(validCard, TeamId.TEAM_2));
                });
    }

    @Test
    void gamePointsWorksForSomeInvalidScores() {
        WorksForSomeInvalidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long rd, long invalidCard, int trickPoints) -> {
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.gamePoints(invalidCard, TeamId.TEAM_1);
                    });
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.gamePoints(invalidCard, TeamId.TEAM_2);
                    });
                });
    }

    @Test
    void totalPointsWorksForSomeValidScores() {
        WorksForSomeValidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints) -> {
                    assertEquals(gamePoints1 + turnPoints1,
                            PackedScore.totalPoints(validCard, TeamId.TEAM_1));
                    assertEquals(gamePoints2 + turnPoints2,
                            PackedScore.totalPoints(validCard, TeamId.TEAM_2));
                });
    }

    @Test
    void totalPointsWorksForSomeInvalidScores() {
        WorksForSomeInvalidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long rd, long invalidCard, int trickPoints) -> {
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.totalPoints(invalidCard, TeamId.TEAM_1);
                    });
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.totalPoints(invalidCard, TeamId.TEAM_2);
                    });
                });
    }

    @Test
    void withAdditionalTrickWorksForSomeValidScores() {
        WorksForSomeValidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints) -> {
                    if(turnTricks1 != 9 && turnPoints1 + trickPoints + (turnTricks1 == 8 ? 100 : 0) <= 257)
                        assertEquals(PackedScore.pack(turnTricks1 + 1, turnPoints1 + trickPoints + (turnTricks1 == 8 ? 100 : 0), gamePoints1, turnTricks2, turnPoints2, gamePoints2),
                                PackedScore.withAdditionalTrick(validCard, TeamId.TEAM_1, trickPoints));
                    if(turnTricks2 != 9 && turnPoints2 + trickPoints + (turnTricks2 == 8 ? 100 : 0) <= 257)
                        assertEquals(PackedScore.pack(turnTricks1, turnPoints1, gamePoints1, turnTricks2 + 1, turnPoints2 + trickPoints + (turnTricks2 == 8 ? 100 : 0), gamePoints2),
                                PackedScore.withAdditionalTrick(validCard, TeamId.TEAM_2, trickPoints));
                });
    }

    @Test
    void withAdditionalTrickWorksForSomeInvalidScores() {
        WorksForSomeInvalidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long rd, long invalidCard, int trickPoints) -> {
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.withAdditionalTrick(invalidCard, TeamId.TEAM_1, trickPoints);
                    });
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.withAdditionalTrick(invalidCard, TeamId.TEAM_2, trickPoints);
                    });
                });
    }

    @Test
    void nextTurnWorksForSomeValidScores() {
        WorksForSomeValidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long validCard, int trickPoints) -> {
                    if(gamePoints1 + turnPoints1 <= 2000 && gamePoints2 + turnPoints2 <= 2000)
                    {
                        long l = PackedScore.pack(0, 0, gamePoints1 + turnPoints1, 0, 0, gamePoints2 + turnPoints2);
                        long l2= PackedScore.nextTurn(validCard);
                        assertEquals(l,l2 );
                    }
                }
                );
    }

    @Test
    void nextTurnWorksForSomeInvalidScores() {
        WorksForSomeInvalidScores(
                (int gamePoints1, int turnPoints1, int turnTricks1, int gamePoints2, int turnPoints2, int turnTricks2, long rd, long invalidCard, int trickPoints) -> {
                    assertThrows(AssertionError.class, () -> {
                        PackedScore.nextTurn(invalidCard);
                    });
                });
    }





    @Test
    void checkIsValidWorks(){
        for(int tT = 0 ;tT <= 9 ; tT++) {
            for(int tP = 0 ; tP <= 257 ; tP++) {
                for(int gP = 0; gP <= 2000 ; gP++) {
                    assertTrue(PackedScore.isValid(Bits64.pack(Bits32.pack(tT, 4, tP, 9, gP, 11), Integer.SIZE, Bits32.pack(tT ,4 , tP, 9, gP, 11), Integer.SIZE)));
                }
            }
        }
    }

    @Test
    void cehckIsValidFailsIfAddOneBeforeScoreValue(){
        for(int tT = 0 ;tT <= 9 ; tT++) {
            for(int tP = 0 ; tP <= 257 ; tP++) {
                for(int gP = 0; gP <= 2000 ; gP++) {
                    for(int i=1;i<=255;i++) {
                        assertFalse(PackedScore.isValid(((i<<24) + (gP<<13) + (tP << 4) + tT) | ((long)((i<<24) + (gP<<13) + (tP << 4) + tT))<<Integer.SIZE));
                    }
                }
            }
        }   
    }

    @Test
    void checkIsValidFailsForValuesTooBig(){
        SplittableRandom rng = newRandom();
        for(int i=0; i< RANDOM_ITERATIONS ; i++) {
            int tT = rng.nextInt(6) + 10;
            int tP = rng.nextInt(254) + 258;
            int gP = rng.nextInt(47) + 2001;
            assertFalse(PackedScore.isValid(tT));
            assertFalse(PackedScore.isValid(tP<<4));
            assertFalse(PackedScore.isValid(gP<<13));
            assertFalse(PackedScore.isValid((gP<<13) + (tP <<4) + tT));
            assertFalse(PackedScore.isValid(((long)tP)<<36));
        }
        
    }
    
    @Test
    void checkPackWorks() {
        for(int tT = 0 ;tT <= 9 ; tT++) {
            for(int tP = 0 ; tP <= 257 ; tP++) {
                for(int gP = 0; gP <= 2000 ; gP++) {
                    assertEquals(((gP<<13) + (tP << 4) + tT) | ((long)((gP<<13) + (tP << 4) + tT)<<Integer.SIZE) , PackedScore.pack(tT, tP, gP, tT, tP, gP));
                }
            }
        }
    
    }

    @Test
    void turnTrickWorks() {
        SplittableRandom rng = newRandom();
        for(int i=0 ; i<RANDOM_ITERATIONS ; i++) {
            long tT = rng.nextLong(10);
            long pkScore = (tT<<Integer.SIZE) + tT;
            assertEquals(tT , PackedScore.turnTricks(pkScore, TeamId.TEAM_1));
            assertEquals(tT , PackedScore.turnTricks(pkScore, TeamId.TEAM_2));
            
        }
    }

    @Test
    void turnPointsWorks() {
        SplittableRandom rng = newRandom();
        for(int i=0 ; i<RANDOM_ITERATIONS ; i++) {
            long tP = rng.nextLong(258);
            long pkScore = (tP<<Integer.SIZE+4) + (tP<<4);
            assertEquals(tP , PackedScore.turnPoints(pkScore, TeamId.TEAM_1));
            assertEquals(tP , PackedScore.turnPoints(pkScore, TeamId.TEAM_2));
        }
    }

    @Test
    void gamePointWorks() {
        SplittableRandom rng = newRandom();
        for(int i=0 ; i<RANDOM_ITERATIONS ; i++) {
            long gP = rng.nextLong(2001);
            long pkScore = (gP<<Integer.SIZE+13) + (gP<<13);
            assertEquals(gP , PackedScore.gamePoints(pkScore, TeamId.TEAM_1));
            assertEquals(gP , PackedScore.gamePoints(pkScore, TeamId.TEAM_2));
        }
    }

    @Test
    void totalPointsWorks() {
        SplittableRandom rng = newRandom();
        for(int i=0 ; i<RANDOM_ITERATIONS ; i++) {
            long tP = rng.nextLong(258);
            long gP = rng.nextLong(2001);
            long pkScore = (tP<<Integer.SIZE+4) + (tP<<4) + (gP<<Integer.SIZE+13) + (gP<<13);
            assertEquals( (int)(gP+tP) , PackedScore.totalPoints(pkScore, TeamId.TEAM_1));
            assertEquals( (int)(gP+tP) , PackedScore.totalPoints(pkScore, TeamId.TEAM_2));
        }
    }

    @Test
    void withAdditionalTrickWorksWithoutExtraPoints() {
        long s = PackedScore.INITIAL;
        int countTeam1 = 0, countTeam2 = 0;
        for(int i=0 ; i<Jass.TRICKS_PER_TURN ; i++) {
            int p = (i==0 ? 13 : 18);
            TeamId w = ( i%2 == 0 ? TeamId.TEAM_1 : TeamId.TEAM_2);
            s = PackedScore.withAdditionalTrick(s, w, p);
            if(i%2==0) {
                ++countTeam1;
                assertEquals(countTeam1, Bits64.extract(s, 0, 4));
            }else {
                ++countTeam2;
                assertEquals(countTeam2, Bits64.extract(s, 32, 4));
            }
        }
    }

    @Test
    void wiithAdditionalTrickWorksWithExtraMatchPoints() {
        long v1 = PackedScore.INITIAL;
        int countTeam1 = 0, countTeam2 = 0;
        for(int i=0 ; i<Jass.TRICKS_PER_TURN ; i++) {
            int p = (i==0 ? 13 : 18);
            v1 = PackedScore.withAdditionalTrick(v1, TeamId.TEAM_1, p);
            v1 = PackedScore.withAdditionalTrick(v1, TeamId.TEAM_2, p);
            countTeam1 += p;
            countTeam2 += p;
            if(i!=8) {
                assertEquals(countTeam1, Bits64.extract(v1, 4, 9));
                assertEquals(countTeam2, Bits64.extract(v1, 36,9));
            }else {
                assertEquals((countTeam1+100), Bits64.extract(v1, 4, 9));
                assertEquals((countTeam2+100), Bits64.extract(v1, 36, 9));
            }
        }
    }

    @Test
    void nextTurnWorks() {
        for(int tT = 0 ;tT <= 9 ; tT++) {
            for(int tP = 0 ; tP <= 257 ; tP++) {
                for(int gP = 0; gP <= 1700 ; gP++) {
                    long pkScore = Bits64.pack(Bits32.pack(tT, 4, tP, 9, gP, 11), Integer.SIZE, Bits32.pack(tT ,4 , tP, 9, gP, 11), Integer.SIZE);
                    assertEquals(0L, PackedScore.turnPoints(PackedScore.nextTurn(pkScore), TeamId.TEAM_1));
                    assertEquals(0L, PackedScore.turnPoints(PackedScore.nextTurn(pkScore), TeamId.TEAM_2));
                    assertEquals(0, PackedScore.turnTricks(PackedScore.nextTurn(pkScore), TeamId.TEAM_2));
                    assertEquals(0, PackedScore.turnTricks(PackedScore.nextTurn(pkScore), TeamId.TEAM_2));
                    assertEquals((tP+gP), PackedScore.totalPoints(pkScore, TeamId.TEAM_1));
                    assertEquals((tP+gP), PackedScore.totalPoints(pkScore, TeamId.TEAM_2));
                }
            }
        }
    }
}







