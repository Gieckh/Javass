package ch.epfl.javass.jass;

import ch.epfl.javass.jass.Card.Color;

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
    
    //TODO faire des tests ou passer des tests, ou s'assurer que tout cela est correct vis a vis de l'ennoncé , car vite fait
    public static Trick firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        return new Trick(PackedTrick.firstEmpty(trump, firstPlayer));
    }

    public static Trick ofPacked(int packed) {
        //TODO isVALID1 ou isValid2 ?
        if(!PackedTrick.isValid(packed)) {
            throw new IllegalArgumentException();
        }
        return new Trick(packed);
    }


    public int packed() {
        return pkTrick;
    }

    public Trick nextEmpty() {
        int nextEmpty = PackedTrick.nextEmpty(pkTrick);
        if (nextEmpty == PackedTrick.INVALID) {
            throw new IllegalStateException();
        }

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
    
    public PlayerId player( int index) {
        if ( (index>=4)||(index<0)) {
            throw new IndexOutOfBoundsException();
        }
        return PackedTrick.player(pkTrick,index );
    }
    
    public Card card ( int index) {
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
    public int hashCode() {
        return Long.hashCode(pkTrick);
    }
    
    /**
     * @brief 
     *
     * @return 
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    @Override
    public String toString() {
        return PackedScore.toString(pkTrick);
    }
    
}
