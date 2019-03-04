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
        for(int i = 0; i<4; ++i) {
                for (int j = 0; j<4; ++j) {
                    int trick =  PackedTrick.firstEmpty(Color.ALL.get(j), PlayerId.ALL.get(i));
                    int shouldBeZero= Bits32.extract(trick, 0, 28);
                    int shouldBePlayer = Bits32.extract(trick, 28, 2);
                    int shouldBeColor = Bits32.extract(trick, 30, 2);
                    //System.out.println(i);
                    //System.out.println(shouldBeColor);
                    System.out.println(trick);
                    assertTrue( (shouldBeColor == j) && (shouldBePlayer == i) && (shouldBeZero == 0) );
                }
        }
    }
    
     
    void nextEmptyWorks() {
        for(int i =0; i < 4;++i) {
            for (int j=0; j<4; ++j) {
                int trick =  PackedTrick.firstEmpty(Color.toType(i), PlayerId.ALL.get(j));
                for (int k = 0; k< (1 <<28) ; ++k) {
                   int newTrick = trick|k;
                   int nextTrick = PackedTrick.nextEmpty(newTrick);
                   assertEquals(Bits32.extract(nextTrick, 0, 24), actual);
                    }
                }
            }
        }
    }
    
}
