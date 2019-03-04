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
                    int trick =  PackedTrick.firstEmpty(Color.ALL.get(i), PlayerId.ALL.get(j));
                    int shouldBeZero= Bits32.extract(trick, 0, 28);
                    int shouldBePlayer = Bits32.extract(trick, 28, 2);
                    int shouldBeColor = Bits32.extract(trick, 30, 2);
                    assertTrue((shouldBeZero==0) && (shouldBePlayer == j) && (shouldBeColor == i));
                }
        }
    }
    
     
    void nextEmptyWorks() {
        for(int i =0; i < 4;++i) {
            for (int j=0; j<4; ++j) {
                int trick =  PackedTrick.firstEmpty(Color.toType(i), PlayerId.ALL.get(j));
                for (int k = 0; k < 9; ++k) {
                    for(int l = 0 ; l <4 ; ++l) {
                        for ( int m = 0; m < 4 ; ++m) {
                            for (int n = 0; n<9; ++n) {
                                
                            }
                        }
                    }
                }
            }
        }
    }
    
}
