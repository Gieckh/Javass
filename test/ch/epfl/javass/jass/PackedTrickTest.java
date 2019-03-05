package ch.epfl.javass.jass;

import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static ch.epfl.javass.jass.PackedTrick.isValid;
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
    void isValidWorks() {
        for(int i = 0; i != -1 ; ++i) {
            assertEquals(IsValidTest(i),PackedTrick.isValid(i));
        }
    }
    
    private boolean IsValidTest(int pkTrick) {
        if((Bits32.extract(pkTrick, 24, 4)<9)) {
            int Card0 = Bits32.extract(pkTrick, 0, 6);
            int Card1 = Bits32.extract(pkTrick, 6, 6);
            int Card2 = Bits32.extract(pkTrick, 12, 6);
            int Card3 = Bits32.extract(pkTrick, 18, 6);
            if ((PackedCard.isValid(Card0)&&PackedCard.isValid(Card1)&&PackedCard.isValid(Card2)&&PackedCard.isValid(Card3))){
                return true;
            }
            if ((PackedCard.isValid(Card0)&&PackedCard.isValid(Card1)&&PackedCard.isValid(Card2)&&(Card3==0b111111))){
                return true;
            }
            if ((PackedCard.isValid(Card0)&&PackedCard.isValid(Card1)&&(Card2==0b111111)&&(Card3==0b111111))){
                return true;
            }
            if ((PackedCard.isValid(Card0)&&(Card1==0b111111)&&(Card2==0b111111))&&(Card3==0b111111)){
                return true;
            }
            if ((Card0==0b111111)&&(Card1==0b111111)&&(Card2==0b111111)&&(Card3==0b111111)){
                return true;
            }
        }
        return false;
    }
    
    @Test
    void firstEmptyWorks() {
        for(int j = 0; j<4; ++j) {
                for (int i = 0; i<4; ++i) {
                    int trick =  PackedTrick.firstEmpty(Color.ALL.get(j), PlayerId.ALL.get(i));
                    int shouldBe1= Bits32.extract(trick, 0, 24);
                    int shouldBe0= Bits32.extract(trick, 24, 4);
                    int shouldBePlayer = Bits32.extract(trick, 28, 2);
                    int shouldBeColor = Bits32.extract(trick, 30, 2);
                    assertTrue( (shouldBeColor == (j)));
                    assertTrue((shouldBePlayer == (i)));
                    assertTrue(shouldBe0 ==0);
                    assertTrue(shouldBe1 == 0b111111111111111111111111);
                }
        }
    }

// marchera des que winnig player marchera
    @Test
    void nextEmptyWorks() {
        for (int i = 0 ; i != -1; ++i ) {
            if (PackedTrick.isValid(i) && (Bits32.extract(i, 0, 6)!=0b111111) &&
                    (Bits32.extract(i, 6, 6)!=0b111111) &&
                    (Bits32.extract(i, 12, 6)!=0b111111) &&
                    (Bits32.extract(i, 18, 6)!=0b111111))
            {
                if(Bits32.extract(i, 24, 4) == 8) {
                    assertEquals(PackedTrick.INVALID, PackedTrick.nextEmpty(i));
                }
                else {
                    System.out.println(Integer.toBinaryString(i));
                    int nextTrick = PackedTrick.nextEmpty(i);
                    assertEquals(Bits32.extract(nextTrick, 0, 24), 0b111111111111111111111111);
                    assertEquals(Bits32.extract(i, 30, 2), Bits32.extract(nextTrick, 30, 2));

//                    System.out.println("i : " + Integer.toBinaryString(i));
//                    System.out.println("winningPlayer : " + PackedTrick.winningPlayer(i) );
//                    System.out.println("nextTrick : " + Integer.toBinaryString(nextTrick));
//                    System.out.println("nextEmptyWinner : " + PlayerId.ALL.get(Bits32.extract(nextTrick, 28, 2)));
//                    System.out.println();

                    assertEquals(PackedTrick.winningPlayer(i), PlayerId.ALL.get(Bits32.extract(nextTrick, 28, 2)));
                    //System.out.println(Integer.toBinaryString(i));
                    // System.out.println(Integer.toBinaryString(nextTrick));

                    assertEquals(Bits32.extract(i, 24, 4) +1  , Bits32.extract(nextTrick, 24, 4));
                }
            }
        }
    }
    
    @Test
    void sizeWorks() {
        int shouldBeThisSize ;
        for(int i = 0; i!= -1 ; ++i) {
            shouldBeThisSize = 0;
            if ( PackedTrick.isValid(i)) {
                for (int j = 0 ; j<4 ;++j ) {
                    if (Bits32.extract(i, 6*j, 6)!= 0b111111) {
                        shouldBeThisSize +=1;
                    }
                }
                assertEquals(shouldBeThisSize, PackedTrick.size(i));
            }
        }
    }
    
    @Test
    void cardWorks() {
        for(int i = 0; i!= -1 ; ++i) {
            if ( PackedTrick.isValid(i)) {
                for (int j = 0 ; j<4 ;++j ) {
                    int shouldBeThatCard = Bits32.extract(i, 6*j, 6); 
                    if(PackedCard.isValid(shouldBeThatCard)) {
                        assertEquals(shouldBeThatCard, PackedTrick.card(i,j));

                    }
                }
            }
        }
    }
    
    @Test
    void withAddedCardWorks() {
        int k;
        int index;
        int ones = 0b111111;
        for (int i = 0; i!=-1; ++i) {
            if(PackedTrick.isValid(i)&&((i&0b111111000000000000000000)==(0b111111<<18))) {
                for (int j = 0 ; j < 64; ++j) {
                    if (PackedCard.isValid(j)) {
                        k = 0;
                        index = 0;
                        while(k<4) {
                            if(Bits32.extract(i, k*6, 6)==0b111111) {
                                index = k;
                                k=5;
                            }
                            else {
                                k++;
                            }
                        }
                        
                       assertEquals((i-(ones<<6*index))+(j<<6*index), PackedTrick.withAddedCard(i, j));
                    }
                }
            }
        }
    }
  
    @Test
    void isLastWorks(){
        for (int i = 0 ; i != -1; ++i ) {
            if (PackedTrick.isValid(i)) {
                if(Bits32.extract(i, 24,4)==8) {
                    assertTrue(PackedTrick.isLast(i));
                }
                else {
                    assertFalse(PackedTrick.isLast(i));
                }
            }
        }
    }
    
    @Test
    void isEmptyWorks(){
        for (int i = 0 ; i != -1; ++i ) {
            if (PackedTrick.isValid(i)) {
                if(Bits32.extract(i, 0,24)==0b111111111111111111111111) {
                    assertTrue(PackedTrick.isEmpty(i));
                }
                else {
                    assertFalse(PackedTrick.isEmpty(i));
                }
            }
        }
    }

    // ce bon vieux marin assume que la carte est correcte ...
    // suppose en Français, jeune homme :-) //todo
    @Test
    void isFullWorks(){
        for (int i = 0 ; i != -1; ++i ) {
            if (PackedTrick.isValid(i)) {
                //TODO [highlight]: tu avais oublié la couleur : size 6 -> 4 (@author : Marin)
                if((Bits32.extract(i, 0 ,4) < 9) && (Bits32.extract(i, 6 ,4) < 9) &&
                   (Bits32.extract(i, 12,4) < 9) && (Bits32.extract(i, 18,4) < 9)) {
                    assertTrue(PackedTrick.isFull(i));
                }

                else {
                    assertFalse(PackedTrick.isFull(i));
                }
            }
        }
    }
    
    @Test
    void trumpWorks(){
        for (int i = 0 ; i != -1 ; ++i){
            if (PackedTrick.isValid(i)) {
                int colorIndex = Bits32.extract(i, 30, 2);
                assertEquals(Color.ALL.get(colorIndex), PackedTrick.trump(i));
            }
        }
    }
    
        @Test
        void playerWorks(){
            for ( int i = 0 ; i != -1 ; ++i){
                if (PackedTrick.isValid(i)) {
                    int playerIndex = Bits32.extract(i, 28, 2);
                    for ( int j = 0; j < 4; ++j) {
                            assertEquals(PlayerId.ALL.get((playerIndex + j)%4), PackedTrick.player(i,j));
                        
    
                   }
                }
            }
        }
    
    
    void indexWorks(){
        for ( int i = 0 ; i != -1 ; ++i){
            if (PackedTrick.isValid(i)) {
                //s'arrete a 111110100001000000011001 : bizarre TODO
                int index = Bits32.extract(i, 24, 4);
                    assertEquals(index , PackedTrick.index(i));
            }
        }
   }
    @Test
    void baseColorWorks(){
        for ( int i = 0 ; i != -1 ; ++i){
            if (PackedTrick.isValid(i)) {
                int shouldBeThatColor = Bits32.extract(i, 4, 2);
                assertEquals(shouldBeThatColor +1, PackedTrick.baseColor(i).type);
            }
        }
   }
    
    @Test
    void pointsWorks(){
        int Card0,Card1,Card2,Card3, trump,points0,points1,points2,points3,pointBonus;
        for ( int i = 0 ; i != -1 ; ++i){
            if (PackedTrick.isValid(i)){
                 Card0 = Bits32.extract(i, 0, 6);
                 Card1 = Bits32.extract(i, 6, 6);
                 Card2 = Bits32.extract(i, 12, 6);
                 Card3 = Bits32.extract(i, 18, 6);
                 trump = Bits32.extract(i, 30, 2);
                 pointBonus = 0;
                 points0 = 0;
                 points1 = 0;
                 points2=0;
                 points3 = 0;
                 if (PackedCard.isValid(Card0)) {
                 points0 = PackedCard.points(Color.ALL.get(trump), Card0);
                 }
                 if(PackedCard.isValid(Card1)) {
                 points1 = PackedCard.points(Color.ALL.get(trump), Card1);
                 }
                 if(PackedCard.isValid(Card2)) {
                 points2 = PackedCard.points(Color.ALL.get(trump), Card2);
                 }
                 if(PackedCard.isValid(Card3)) {
                 points3 = PackedCard.points(Color.ALL.get(trump), Card3);      
                 }
                if (Bits32.extract(i, 24, 4) == 8) {
                    pointBonus += 5;
                }
                // les cartes entrée dans points sont toutes valides
                // si on entre des mauvaises cartes , assertError
                if(PackedCard.isValid(Card0)&&PackedCard.isValid(Card1)&&PackedCard.isValid(Card2)&&PackedCard.isValid(Card3)) {
                int sum = points0 + points1+ points2 + points3 + pointBonus;
                assertEquals(sum , PackedTrick.points(i));
                }
            }
        }
   }
}
    
        

    
    
    
  
    
 
    

