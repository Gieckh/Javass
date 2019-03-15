package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.jass.PackedScore.isValid;

import ch.epfl.javass.jass.Card.Color;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.Preconditions.checkIndex;

public class Trick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final Trick INVALID = new Trick(PackedTrick.INVALID);
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
    
    //TODO faire des tests ou passer des tests, ou s'assurer que tout cela est correct vis a vis de l'énnoncé , car vite fait
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
        if (!PackedTrick.isFull(pkTrick)) {
            throw new IllegalStateException();
        }

        return new Trick(PackedTrick.nextEmpty(pkTrick));
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

    //TODO: test checkIndex
    public PlayerId player(int index) {
        checkIndex(index, PlayerId.COUNT);
        return PackedTrick.player(pkTrick, index);
    }
    
    public Card card (int index) { //TODO: test
        checkIndex(index, PackedTrick.size(pkTrick));
        return Card.ofPacked(PackedTrick.card(pkTrick, index));
    }
    
    public Trick withAddedCard(Card c) {
        if (PackedTrick.isFull(pkTrick)){
            throw new IllegalStateException();
        }

        return new Trick ( PackedTrick.withAddedCard(pkTrick, c.packed()) );
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
            return (thatOTrick.pkTrick == this.pkTrick);
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
