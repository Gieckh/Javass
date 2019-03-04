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
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.TeamId;

public class PackedTrickTest {
    
    @Test
    void firstEmptyWorks() {
        SplittableRandom rng = newRandom();
        for(int i = 0; i<4; ++i) {
                for (int j = 0; j<4; ++j) {
                    int set =  PackedTrick.firstEmpty(Color.toType(i), PlayerId.ALL.get(j));
                    int shouldBeZero= Bits32.extract(set, 0, 28);
                    int shouldBePlayer = Bits32.extract(set, 28, 2);
                    int shouldBeColor = Bits32.extract(set, 30, 2);
                    assertTrue(shouldBeZero==0 && shouldBePlayer == j && shouldBeColor == i);
                }
        }
    }
    
}
