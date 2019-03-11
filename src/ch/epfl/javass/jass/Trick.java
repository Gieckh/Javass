package ch.epfl.javass.jass;

import ch.epfl.javass.jass.Card.Color;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.Preconditions.checkIndex;

public class Trick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final Trick INVALID = ofPacked(PackedTrick.INVALID);
    private final int pkTrick;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    private Trick(int aTrick) {
        pkTrick = aTrick;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    //TODO faire des tests ou passer des tests, ou s'assurer que tout cela est correct vis a vis de l'énoncé , car vite fait
    public static Trick firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        return new Trick(PackedTrick.firstEmpty(trump, firstPlayer));
    }

    public static Trick ofPacked(int packed) {
        checkArgument(PackedTrick.isValid(packed));
        return new Trick(packed);
    }

    public int packed() {
        return pkTrick;
    }

    public Trick nextEmpty() {
        int nextEmpty = PackedTrick.nextEmpty(pkTrick);
        checkArgument(nextEmpty != PackedTrick.INVALID);

        return new Trick(nextEmpty);
    }

    public boolean isEmpty() {
        return PackedTrick.isEmpty(pkTrick);
    }

    public boolean isFull() {
        return PackedTrick.isFull(pkTrick);
    }

    public boolean isLast() {
        return PackedTrick.isLast(pkTrick);
    }

    public int size() {
        return PackedTrick.size(pkTrick);
    }
    
    public Color trump() {
        return PackedTrick.trump(pkTrick);
    }
    
    public int index() {
        return PackedTrick.index(pkTrick);
    }
    
    public PlayerId player(int index) {
        checkIndex(index, PlayerId.COUNT);
        return PackedTrick.player(pkTrick, index);
    }
    
    public Card card (int index) {
        if((index < 0) ||(index >= PackedTrick.size(pkTrick))) {
            throw new IndexOutOfBoundsException();
        }
        return Card.ofPacked(PackedTrick.card(pkTrick, index));
    }
    
    public Trick withAddedCard(Card c) {
        if (PackedTrick.isFull(pkTrick)){
            throw new IllegalStateException();
        }
        return Trick.ofPacked(PackedTrick.withAddedCard(pkTrick, c.packed()));
    }
    
    public Color baseColor() {
        if(PackedTrick.isEmpty(pkTrick)) {
            throw new IllegalStateException();
        }

        return PackedTrick.baseColor(pkTrick);
    }
    
    public CardSet playableCards(CardSet hand) {
        if( PackedTrick.isFull(pkTrick)) {
            throw new IllegalStateException();
        }
        return CardSet.ofPacked(PackedTrick.playableCards(pkTrick, hand.packed()));
    }
    
    public int points() {
        return PackedTrick.points(pkTrick);
    }
    
    public PlayerId winningPlayer() {
        if(PackedTrick.isEmpty(pkTrick)) {
            throw new IllegalStateException();
        }
        return PackedTrick.winningPlayer(pkTrick);
    }



    @Override
    public boolean equals(Object thatO) {
        if (thatO == null  ||  !(thatO instanceof Trick)) { //todo: test
            return false;
        }

        Trick thatOTrick = (Trick) thatO; // Or do 2 "conversions, idk"
            return (thatOTrick.pkTrick== this.pkTrick);
    }
    
    @Override
    public int hashCode() { //TODO: assistant
        return pkTrick;
    }
    
    /**
     * @brief 
     *
     * @return
    */
    @Override
    public String toString() {
        return PackedScore.toString(pkTrick);
    }
    
}
