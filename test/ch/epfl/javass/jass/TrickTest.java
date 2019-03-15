/*
 *  Author:      Joshua Bernimoulin
 *  Date:        5 Mar. 2019
 */
package ch.epfl.javass.jass;

import static ch.epfl.test.TestRandomizer.newRandom;
import static org.junit.jupiter.api.Assertions.*;

import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;

import ch.epfl.javass.bits.Bits32;

class TrickTest {
    
    public final static int INVALID=-1;

    long mask = 0b0000000_111111111_0000000_111111111_0000000_111111111_0000000_111111111L;

    private final static int NUMBER_DIFFERENT_CARDS=4;
    private final static int CARD_SIZE=6;
    private final static int ALL_CARD_SIZE=NUMBER_DIFFERENT_CARDS*CARD_SIZE;
    private final static int CARD_START=0;
    private final static int INDEX_SIZE=4;
    private final static int INDEX_START=ALL_CARD_SIZE+CARD_START;
    private final static int PLAYER_SIZE=2;
    private final static int PLAYER_START=INDEX_START+INDEX_SIZE;
    private final static int TRUMP_SIZE=2;
    private final static int TRUMP_START=PLAYER_START+PLAYER_SIZE;

    
    @Test
    void trickCreationWorks() throws Exception {
        for(PlayerId p: PlayerId.ALL) {
            for(Card.Color c:Card.Color.ALL) {
                assertEquals(Trick.ofPacked(PackedTrick.firstEmpty(c, p)),Trick.firstEmpty(c, p));
            }
        }
    }
    
    @Test
    void packedWorks() throws Exception {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            assertEquals(c,Trick.ofPacked(c).packed());
        }
    }
    
    @Test
    void lotsOfTests() throws Exception {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            if(PackedTrick.isEmpty(c)) {
                assertTrue(Trick.ofPacked(c).isEmpty());
                assertFalse(Trick.ofPacked(c).isFull());
            }else if(PackedTrick.isFull(c)){
                assertFalse(Trick.ofPacked(c).isEmpty());
                assertTrue(Trick.ofPacked(c).isFull());
            }
            
            if(PackedTrick.isLast(c)) {
                assertTrue(Trick.ofPacked(c).isLast());
            }else {
                assertFalse(Trick.ofPacked(c).isLast());
            }
            
            assertEquals(PackedTrick.index(c),Trick.ofPacked(c).index());
            assertEquals(PackedTrick.trump(c),Trick.ofPacked(c).trump());
            assertEquals(PackedTrick.size(c),Trick.ofPacked(c).size());
            System.out.println("coucou");
            System.out.println(Integer.toBinaryString(c));
            System.out.println(PackedTrick.points(c));
            System.out.println(Trick.ofPacked(c).points());
            
            assertEquals(PackedTrick.points(c),Trick.ofPacked(c).points());


        }
    }
    
    @Test
    void equalsWorks() throws Exception {
        SplittableRandom rng= newRandom();
        for(PlayerId p: PlayerId.ALL) {
            for(Card.Color c:Card.Color.ALL) {
                assertTrue(Trick.ofPacked(PackedTrick.firstEmpty(c, p)).equals(Trick.firstEmpty(c, p)));
                
                int trick=generateRandomValidTrick(rng);
                int trick2=generateRandomValidTrick(rng);
                
                assertTrue(Trick.ofPacked(trick).equals(Trick.ofPacked(trick)));
                if(trick!=trick2) {
                    assertFalse(Trick.ofPacked(trick).equals(Trick.ofPacked(trick2)));
                    assertFalse(Trick.ofPacked(trick2).equals(Trick.ofPacked(trick)));
                    assertFalse(Trick.ofPacked(trick).equals(null));
                    assertFalse(Trick.ofPacked(trick).equals("Hello"));
                }
                
            }
        }
    }
    
    //Throws Errors
    
    
    @Test
    void ofPackedThrowsException() {
        SplittableRandom rng= newRandom();
        for(int i=0;i<30;++i) {
            assertThrows(IllegalArgumentException.class, () -> {Trick.ofPacked(generateRandomTrickHead(rng)+(generateRandomValidCard(rng)<<18)+Bits32.mask(0, 18));});
            assertThrows(IllegalArgumentException.class, () -> {Trick.ofPacked(generateRandomTrickHead(rng)+(generateRandomValidCard(rng)<<18)+(generateRandomValidCard(rng)<<18)+Bits32.mask(0, 12));});
            assertThrows(IllegalArgumentException.class, () -> {Trick.ofPacked(generateRandomTrickHead(rng)+(generateRandomValidCard(rng)<<18)+(generateRandomValidCard(rng)<<12)+(generateRandomValidCard(rng)<<6)+Bits32.mask(0, 6));});
            assertThrows(IllegalArgumentException.class, () -> {Trick.ofPacked((rng.nextInt(4)<<30)+(rng.nextInt(4)<<28)+(10<<24)+Bits32.mask(0, 24));});
            assertThrows(IllegalArgumentException.class, () -> {Trick.ofPacked(generateRandomTrickHead(rng)+(generateRandomValidCard(rng))+(generateRandomValidCard(rng)<<6)+(generateRandomValidCard(rng)<<12)+(generateRandomInvalidCard(rng)<<18));});

        }
    }

    @Test
    void playerThrowsException() {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            int index=rng.nextInt(-2,5);
            if(index>=4) {
                assertThrows(IndexOutOfBoundsException.class, () -> {Trick.ofPacked(c).player(index);});
            }else if(index<0){
                assertThrows(IndexOutOfBoundsException.class, () -> {Trick.ofPacked(c).player(index);});
            }else{
                assertEquals(PackedTrick.player(c,index),Trick.ofPacked(c).player(index));            
            }
        }
    } 
    
    @Test
    void cardThrowsException() {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            int index=rng.nextInt(-2,5);
            if(index>=PackedTrick.size(c)) {
                assertThrows(IndexOutOfBoundsException.class, () -> {Trick.ofPacked(c).card(index);});
            }else if(index<0){
                assertThrows(IndexOutOfBoundsException.class, () -> {Trick.ofPacked(c).card(index);});
            }else{
                assertEquals(PackedTrick.card(c,index),Trick.ofPacked(c).card(index).packed());            
            }
        }
    }
    
    @Test
    void withAddedCardThrowsException() {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            int card=generateRandomValidCard(rng);
            if(PackedTrick.isFull(c)) {
                assertThrows(IllegalStateException.class, () -> {Trick.ofPacked(c).withAddedCard(Card.ofPacked(card));});
            }else{
                assertEquals(Trick.ofPacked(PackedTrick.withAddedCard(c,card)),Trick.ofPacked(c).withAddedCard(Card.ofPacked(card)));            
            }
        }
    }
    

    @Test
    void baseColorThrowsException() {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            if(PackedTrick.isEmpty(c)) {
                assertThrows(IllegalStateException.class, () -> {Trick.ofPacked(c).baseColor();});
            }else{
                assertEquals(PackedTrick.baseColor(c),Trick.ofPacked(c).baseColor());            
            }
        }
    }
    
    @Test
    void playableCardsThrowsException() {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            if(PackedTrick.isFull(c)) {
                assertThrows(IllegalStateException.class, () -> {Trick.ofPacked(c).playableCards(CardSet.ofPacked(mask));});
            }
            long cardSet=rng.nextLong()&mask;
            while(PackedCardSet.size(cardSet)>9) {
                cardSet=rng.nextLong()&mask;
            }
            if(!PackedTrick.isFull(c)){
                assertEquals(CardSet.ofPacked(PackedTrick.playableCards(c,cardSet)),Trick.ofPacked(c).playableCards(CardSet.ofPacked(cardSet)));
            }
        }
    }
    
    @Test
    void winningPlayerThrowsException() {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            if(PackedTrick.isEmpty(c)) {
                assertThrows(IllegalStateException.class, () -> {Trick.ofPacked(c).winningPlayer();});
            }else{
                assertEquals(PackedTrick.winningPlayer(c), Trick.ofPacked(c).winningPlayer());            
            }
        }
    }
    
    @Test
    void nextEmptyThrowsException() {
        SplittableRandom rng= newRandom();
        for(int i=0;i<30;++i) {
            int c=generateRandomValidTrick(rng);
            if(!PackedTrick.isFull(c)) {
                assertThrows(IllegalStateException.class, () -> {Trick.ofPacked(c).nextEmpty();});
            }else if(PackedTrick.isLast(c)){
                //Trick.ofPacked(PackedTrick.nextEmpty(c)) fait un IllegalArgument car PackedTrick.nextEmpty(c) me donne INVALID
                assertEquals(PackedTrick.nextEmpty(c),Trick.ofPacked(c).nextEmpty().packed());
            }else{
                assertEquals(Trick.ofPacked(PackedTrick.nextEmpty(c)),Trick.ofPacked(c).nextEmpty());
            }
        }
    } 
    
    @Test
    void hashCodeWorks() throws Exception {
        SplittableRandom rng= newRandom();
        for(int i=0;i<3000;++i) {
            int c=generateRandomValidTrick(rng);
            assertEquals(c,Trick.ofPacked(c).hashCode());
        }
    }
    
    @Test
    void toStringWorks() {
        SplittableRandom rng = newRandom();
        for (int i = 0; i < 2; i += 1) {
        
            int c = generateRandomValidTrick(rng);
            //System.out.println(Trick.ofPacked(c).toString());
        }
    }
    
    
    private int generateRandomValidTrick(SplittableRandom rng) {
        switch(rng.nextInt(5)) {
        case 0:
            return generateRandomTrickHead(rng)+(generateRandomValidCard(rng)<<18)+(generateRandomValidCard(rng)<<12)
                    +(generateRandomValidCard(rng)<<6)+generateRandomValidCard(rng);
        case 1:
            return generateRandomTrickHead(rng)+(generateRandomValidCard(rng))+(generateRandomValidCard(rng)<<6)+(generateRandomValidCard(rng)<<12)+Bits32.mask(18, 6);
        case 2:
            return generateRandomTrickHead(rng)+(generateRandomValidCard(rng))+(generateRandomValidCard(rng)<<6)+Bits32.mask(12, 12);
        case 3:
            return generateRandomTrickHead(rng)+(generateRandomValidCard(rng))+Bits32.mask(6, 18);
        case 4:
            return generateRandomTrickHead(rng)+Bits32.mask(0, 24);
        default: 
            System.out.println("Error in generateRandomValidTrick");
            return 0;
        }
        
    }
    
    private int generateRandomValidFullTrick(SplittableRandom rng) {        
        return generateRandomTrickHead(rng)+generateRandomTrickTail(rng);
    }
    
    private int generateRandomValidCard(SplittableRandom rng) {
        
        return (rng.nextInt(4)<<4)+rng.nextInt(9);
    }
    private int generateRandomInvalidCard(SplittableRandom rng) {
        return(rng.nextInt(4)<<4)+rng.nextInt(9,15);
        
    }
    private int generateRandomTrickHead(SplittableRandom rng) {        
        return (rng.nextInt(4)<<30)+(rng.nextInt(4)<<28)+(rng.nextInt(9)<<24);
    }
    private int generateRandomTrickTail(SplittableRandom rng) {
        
        return (generateRandomValidCard(rng)<<18)+(generateRandomValidCard(rng)<<12)
                +(generateRandomValidCard(rng)<<6)+generateRandomValidCard(rng);
    }
    
}