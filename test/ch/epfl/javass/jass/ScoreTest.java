package ch.epfl.javass.jass;
import ch.epfl.javass.Preconditions;
import ch.epfl.javass.bits.Bits32;
import ch.epfl.javass.bits.Bits64;
import org.junit.jupiter.api.Test;

import java.util.SplittableRandom;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    //TODO: to do
    @Test
    void checkArgumentFailsForFalse() {
        assertThrows(IllegalArgumentException.class, () -> {
            Preconditions.checkArgument(false);
        });
    }
    
    /**
     * ofPacked
     */
    @Test
    void ofPackedWorks() {
        assertEquals(Score.INITIAL, Score.ofPacked(0L));
        for (int r = 0; r <= 9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 2001; j<=2050; ++j) {
                    final int kj = j;
                    final int kc = c;
                    final int kr = r;
                    assertThrows(IllegalArgumentException.class, () -> {
                        Score.ofPacked((long)kj << 45 | (long)kc << 36 | (long)kr <<32 | (long)kj<< 13 | (long)kc << 4 | (long)kr);
                    });
                }
            }
        }
    }
    
    @Test
    void packedWorks() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals((long)j << 45 | (long)c << 36 | (long)r <<32 | (long)j<< 13 | (long)c << 4 | (long)r, 
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).packed());
                }
            }
        }
    }
    /*
     * turnTrick
     */
    @Test
    void turnTrickWorks() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals(r, 
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).turnTricks(TeamId.TEAM_1));
                    assertEquals(r,
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).turnTricks(TeamId.TEAM_2));
                }
            }
        }
    }
    /*
     * turnPoints
     */
    @Test 
    void turnPointsWorks(){
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals(c, 
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).turnPoints(TeamId.TEAM_1));
                    assertEquals(c,
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).turnPoints(TeamId.TEAM_2));
                }
            }
        }
    }
    
    /*
     * gamePoints
     */
    @Test
    void gamePoints() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                   
                    assertEquals(j, 
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).gamePoints(TeamId.TEAM_1));
                    assertEquals(j,
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).gamePoints(TeamId.TEAM_2));
                }
            }
        }
    }
    /*
     * totalPoints
     */
    @Test
    void totalPoints() {
        for (int r = 0; r <=9; ++r) {
            for (int c = 0; c <= 257; ++c) {
                for(int j = 0; j<=2000; ++j) {
                    assertEquals(j+c, 
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).totalPoints(TeamId.TEAM_1));
                    assertEquals(j+c,
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).totalPoints(TeamId.TEAM_2));
                }
            }
        }
    }
    /*
     * ScoreWorks
     */
    @Test 
    void ScoreWorks() {
        Score s = Score.INITIAL;
        String score = s.toString();
        for (int i = 0; i < Jass.TRICKS_PER_TURN; ++i) {
          int p = (i == 0 ? 13 : 18);
          TeamId w = (i % 2 == 0 ? TeamId.TEAM_1 : TeamId.TEAM_2);
          s = s.withAdditionalTrick(w, p);
          score += s.toString();
        }
        s = s.nextTurn();
        score += s.toString();
        assertEquals("(0,0,0)/(0,0,0)(1,13,0)"
                + "/(0,0,0)(1,13,0)/(1,18,0)(2,31,0)"
                + "/(1,18,0)(2,31,0)/(2,36,0)(3,49,0)"
                + "/(2,36,0)(3,49,0)/(3,54,0)(4,67,0)"
                + "/(3,54,0)(4,67,0)/(4,72,0)(5,85,0)"
                + "/(4,72,0)(0,0,85)/(0,0,72)",
                score);
    }
    
    /*
     * equals works
     */
    @Test
    void equalsWorks() {
        for (int r = 1; r <=9; ++r) {
            for (int c = 1; c <= 257; ++c) {
                for(int j = 1; j<=2000; ++j) {
                    assertEquals(true, 
                            Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).equals(Score.ofPacked(PackedScore.pack(r, c, j, r, c, j))));
                    assertEquals(false, Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).equals(null));
                    assertEquals(false, Score.ofPacked(PackedScore.pack(r, c, j, r, c, j)).equals(Score.ofPacked(PackedScore.pack(r-1, c-1, j-1, r, c, j))));
                }
            }
        }
    }
    @Test
    void withAdditionalTricksFailsWithNegativeValues() {
        Score s = Score.INITIAL;
        assertThrows(IllegalArgumentException.class, () ->{
            s.withAdditionalTrick(TeamId.TEAM_1, -1);
        });
    }

    @Test
    void equalsWorks2() {
        SplittableRandom rng = newRandom();
        for(int i=0 ; i<RANDOM_ITERATIONS ; ++i) {
            int tT = rng.nextInt(10);
            int tP = rng.nextInt(258);
            int gP = rng.nextInt(2001);
            Score s1 = Score.ofPacked(Bits64.pack(Bits32.pack(tT, 4, tP, 9, gP, 11), Integer.SIZE, Bits32.pack(tT ,4 , tP, 9, gP, 11), Integer.SIZE));
            Score s2 = Score.ofPacked(Bits64.pack(Bits32.pack(tT, 4, tP, 9, gP, 11), Integer.SIZE, Bits32.pack(tT ,4 , tP, 9, gP, 11), Integer.SIZE));
            assertTrue(s1.equals(s2));
        }
    }
    
    @Test
    void equalsFails() {
        SplittableRandom rng = newRandom();
        for(int i=0 ; i<RANDOM_ITERATIONS ; ++i) {
            int tT = rng.nextInt(9);
            int tP = rng.nextInt(258);
            int gP = rng.nextInt(2001);
            Score s1 = Score.ofPacked(
                    Bits64.pack(Bits32.pack(tT, 4, tP, 9, gP, 11), Integer.SIZE, Bits32.pack(tT ,4 , tP, 9, gP, 11), Integer.SIZE));
            Score s2 = Score.ofPacked(Bits64.pack(Bits32.pack(tT+1, 4, tP, 9, gP, 11), Integer.SIZE, Bits32.pack(tT+1 ,4 , tP, 9, gP, 11), Integer.SIZE));
            assertFalse(s1.equals(s2));
            assertFalse(s1.equals(new String()));
        }
    }
}




