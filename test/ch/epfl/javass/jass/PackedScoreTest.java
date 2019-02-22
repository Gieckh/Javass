package ch.epfl.javass.jass;

import org.junit.jupiter.api.Test;

import java.util.SplittableRandom;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;

import static org.junit.jupiter.api.Assertions.*;

//TODO: Fill the methods
class PackedScoreTest {
    @Test
    void isValidReturnsTrueWithValidPacked() {

    }

    @Test
    void isValidReturnsFalseWithTooBigValuesForTeam1() {

    }
    @Test
    void isValidReturnsFalseWithTooBigValuesForTeam2() {

    }

    @Test
    void isValidReturnsFalseWithNegativeValuesForTeam1() {
        SplittableRandom rng = newRandom();
        for (int i = 0 ; i < RANDOM_ITERATIONS ; ++i) {

        }
    }
    @Test
    void isValidReturnsFalseWithNegativeValuesForTeam2() {

    }


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
