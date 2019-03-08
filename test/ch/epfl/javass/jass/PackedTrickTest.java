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

//TODO: void isValidWorks()
public class PackedTrickTest {

    @Test
    void winningPlayerTest() {
        int counter = 0;
        int[] cards = {};
        for (int i = 0 ; i != -1 ; ++i) {
            if ( PackedTrick.isValid(i)&&((Bits32.extract(i, 0, 6) != 111111)||Bits32.extract(i, 6, 6) != 111111||Bits32.extract(i, 12, 6) != 111111||Bits32.extract(i, 18, 6) != 111111)){
               counter =0;
                for (int j = 0; j< 4 ; ++j) {
                    
                    if (Bits32.extract(i, 6*j, 6)!=111111) {
                        counter +=1;
                    }
                }
            }
        }
    }

        @Test
        void playableCardTestUnit() {
            int pkTrick1 = PackedTrick.firstEmpty(Card.Color.SPADE,
                    PlayerId.PLAYER_1);
            long pkHand1 = 0b0000_0000_0010_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000L;
            int pkTrick2 = Bits32.pack(0b110, 6, PackedCard.INVALID, 6,
                    PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
            long pkHand2 = 0b0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_1010_0000_0000_0010_0000L;
            int pkTrick3 = Bits32.pack(0b110, 6, PackedCard.INVALID, 6,
                    PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
            long pkHand3 = 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_1010_0000L;
            int pkTrick4 = Bits32.pack(0b10_0110, 6, 0b0, 6, PackedCard.INVALID, 6,
                    PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
            long pkHand4 = 0b0000_0000_0010_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0010L;
            int pkTrick5 = Bits32.pack(0b11_0001, 6, 0b1_0011, 6,
                    PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b1,
                    2);
            long pkHand5 = 0b0000_0000_0000_0000_0000_0000_1000_0000_0000_0000_0000_0100_0000_0000_1000_0001L;
            int pkTrick6 = Bits32.pack(0b1_0101, 6, PackedCard.INVALID, 6,
                    PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
            long pkHand6 = 0b0000_0000_0000_0001_0000_0000_0001_0000_0000_0000_1000_0000_0000_0000_0000_1000L;
            int pkTrick7 = Bits32.pack(0b11_0011, 6, PackedCard.INVALID, 6,
                    PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b11,
                    2);
            long pkHand7 = 0b0000_0000_0000_0000_0000_0000_1000_1000_0000_0000_0100_0000_0000_0001_0000_0000L;
            int pkTrick8 = Bits32.pack(0b1000, 6, PackedCard.INVALID, 6,
                    PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b10,
                    2);
            long pkHand8 = 0b0000_0000_0000_0000_0000_0000_0010_0010_0000_0000_0100_0000_0000_0000_1000_0000L;
            int pkTrick9 = Bits32.pack(0b11_0100, 6, PackedCard.INVALID, 6,
                    PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b10,
                    2);
            long pkHand9 = 0b0000_0000_0000_0010_0000_0000_0000_1000_0000_0000_0001_0001_0000_0000_0000_0000L;
            int pkTrick10 = Bits32.pack(0b10_0100, 6, 0b0100, 6, PackedCard.INVALID,
                    6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
            long pkHand10 = 0b0000_0000_1000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0000L;
            int pkTrick11 = Bits32.pack(0b10_0100, 6, 0b0100, 6, PackedCard.INVALID,
                    6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
            long pkHand11 = 0b0000_0000_0000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0100L;
            assertEquals("{\u266110,\u2663J}" ,PackedCardSet
                    .toString(PackedTrick.playableCards(pkTrick1, pkHand1)));
            assertEquals("{\u2660J,\u26617,\u26619,\u266310}",PackedCardSet.toString(
                            PackedTrick.playableCards(pkTrick2, pkHand2)));
            assertEquals("{\u2660J,\u2660K}" ,PackedCardSet
                    .toString(PackedTrick.playableCards(pkTrick3, pkHand3)));
            assertEquals("{\u26607,\u266210}" ,PackedCardSet
                    .toString(PackedTrick.playableCards(pkTrick4, pkHand4)));
            assertEquals("{\u26606,\u2660K,\u2662K}",
                     PackedCardSet.toString(
                            PackedTrick.playableCards(pkTrick5, pkHand5)));
            assertEquals("{\u26609,\u2661K}" ,PackedCardSet
                    .toString(PackedTrick.playableCards(pkTrick6, pkHand6)));
            assertEquals("{\u2660A,\u2661Q,\u26629,\u2662K}",
                    PackedCardSet.toString(
                            PackedTrick.playableCards(pkTrick7, pkHand7)));
            assertEquals(
                   "{\u2660K,\u26627,\u2662J}" ,PackedCardSet.toString(
                            PackedTrick.playableCards(pkTrick8, pkHand8)));
            assertEquals("{\u26629,\u26637}" ,PackedCardSet
                    .toString(PackedTrick.playableCards(pkTrick9, pkHand9)));
            assertEquals("{\u2662J,\u2662K}" ,PackedCardSet
                    .toString(PackedTrick.playableCards(pkTrick10, pkHand10)));
            assertEquals("{\u2662J,\u2662K}" ,PackedCardSet
                    .toString(PackedTrick.playableCards(pkTrick11, pkHand11)));
        }
    



    
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
                    int nextTrick = PackedTrick.nextEmpty(i);

                    assertEquals(Bits32.extract(nextTrick, 0, 24), 0b111111111111111111111111);
                    assertEquals(Bits32.extract(i, 30, 2), Bits32.extract(nextTrick, 30, 2));
                    assertEquals(PackedTrick.winningPlayer(i), PlayerId.ALL.get(Bits32.extract(nextTrick, 28, 2)));
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
    
        

    
    
    
  
    
 
    

