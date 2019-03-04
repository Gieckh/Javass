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
                    int shouldBe1= Bits32.extract(trick, 0, 24);
                    int shouldBe0= Bits32.extract(trick, 24, 4);
                    int shouldBePlayer = Bits32.extract(trick, 28, 2);
                    int shouldBeColor = Bits32.extract(trick, 30, 2);
                    //System.out.println(i);
                    //System.out.println(shouldBeColor);
                    //System.out.println(Integer.toBinaryString(trick) );
                    assertTrue( (shouldBeColor == (j)));
                    //System.out.println("color");
                    assertTrue(shouldBePlayer == (i));
                    //System.out.println("player");
                    assertTrue(shouldBe0 ==0);
                    //System.out.println("0");
                    assertTrue(shouldBe1 == 0b111111111111111111111111);
                  //  System.out.println("1");
                }
        }
    }

    
  @Test
    void nextEmptyWorks() {
        for(int i =0; i < 4;++i) {
            for (int j=0; j<4; ++j) {
                int trick =  PackedTrick.firstEmpty(Color.toType(i), PlayerId.ALL.get(j));
                for (int k = 0; k< (1 <<29) ; ++k) {               
                       int newTrick = trick|k;
                       if(Bits32.extract(trick, 24, 4) == 8) {
                           System.out.println("on passe bien par ici ?");
                           assertEquals(PackedTrick.INVALID, PackedTrick.nextEmpty(newTrick));
                       
                       }
                       else {
                           int nextTrick = PackedTrick.nextEmpty(newTrick);
                           assertEquals(Bits32.extract(nextTrick, 0, 24), 0);
                           assertEquals(i, Bits32.extract(nextTrick, 30, 2));
                           assertEquals(PackedTrick.winningPlayer(nextTrick), Bits32.extract(nextTrick, 28, 2));
                           assertEquals(Bits32.extract(trick, 24, 4) + 1 , Bits32.extract(nextTrick, 24, 4));
                    }
                }
            }
        }
    }
    @Test
    void isLastWorks(){
        for (int i = 0 ; i< (1<<33); ++i ) {
            if(Bits32.extract(i, 24,4)==8) {
                assertEquals(true, PackedTrick.isLast(i));
            }
            else {
                assertEquals(false, PackedTrick.isLast(i));
            }
        }
    }
    
    @Test
    void isEmptyWorks(){
        for (int i = 0 ; i< (1<<33); ++i ) {
            if(Bits32.extract(i, 0,24)==0) {
                assertEquals(true, PackedTrick.isEmpty(i));
            }
            else {
                assertEquals(false, PackedTrick.isEmpty(i));
            }
        }
    }
    
    @Test
    void isFullWorks(){
        for (int i = 0 ; i< (1<<33); ++i ) {
            if((Bits32.extract(i, 0,6)!=0)&&(Bits32.extract(i, 6,6)!=0)&&(Bits32.extract(i, 12,6)!=0)&&(Bits32.extract(i, 18,6)!=0)) {
                assertEquals(true, PackedTrick.isFull(i));
            }
            else {
                assertEquals(false, PackedTrick.isFull(i));
            }
        }
    }
    
    @Test
    void trumpWorks(){
        for ( int i = 0 ; i< (1<<33) ; ++i){
            int colorIndex = Bits32.extract(i, 30, 2);
            assertEquals(Color.ALL.get(colorIndex), PackedTrick.trump(i));
            }
        }
    
    @Test
    void playerWorks(){
        for ( int i = 0 ; i< (1<<33) ; ++i){
            int playerIndex = Bits32.extract(i, 28, 2);
            for ( int j = 0; j < 4; ++j) {
                assertEquals(PlayerId.ALL.get((playerIndex + j)%4), PackedTrick.player(i));

            }
        }
    }
    
    @Test
    void indexWorks(){
        for ( int i = 0 ; i< (1<<33) ; ++i){
            int index = Bits32.extract(i, 24, 4);
                assertEquals(index , PackedTrick.index(i));
        }
   }
    
    @Test
    void indexWorks(){
        for ( int i = 0 ; i< (1<<33) ; ++i){
            int index = Bits32.extract(i, 24, 4);
                assertEquals(index , PackedTrick.index(i));
        }
   }








}
    
        

    
    
    
  
    
 
    

