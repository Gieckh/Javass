package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.jass.PackedScore.isValid;

import java.util.List;

public final class CardSet {

    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    private long pkCardSet;
    public final static CardSet EMPTY = new CardSet(PackedCardSet.EMPTY);
    public final static CardSet All_CARDS = new CardSet(PackedCardSet.ALL_CARDS);
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
            
    private CardSet(long packed) {
        pkCardSet = packed;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    public static CardSet of(List<Card> cards) {
        CardSet ofCardSet = new CardSet(0L);
        for (Card card: cards ) {
            ofCardSet.add(card);
        }
        return ofCardSet;
    }


    public static CardSet ofPacked(long packed) {
        checkArgument(isValid(packed));
        return new CardSet(packed);
    }
           

    public long packed() {
        return pkCardSet;
    }
    
    public boolean isEmpty () {
        return PackedCardSet.isEmpty(pkCardSet);
    }
    
    public int size() {
        return PackedCardSet.size(pkCardSet);
    }
    
    public int get( int index) {
        return PackedCardSet.get(pkCardSet, index);
    }
    
    public long add(Card card) {
        return PackedCardSet.add(pkCardSet, card.packed());
    }
    
    public long remove(Card card) {
        return PackedCardSet.remove(pkCardSet, card.packed());
    }
    
    public boolean contains(Card card) {
        return PackedCardSet.contains(pkCardSet, card.packed());
    }
    
    public long complement() {
        return PackedCardSet.complement(pkCardSet);
    }
    
    public long union(CardSet that) {
        return PackedCardSet.union(pkCardSet, that.pkCardSet);
    }
    
    public long intersection(CardSet that) {
        return PackedCardSet.intersection(pkCardSet, that.pkCardSet);
    }
    
    public long difference(CardSet that) {
        return PackedCardSet.difference(pkCardSet, that.pkCardSet);
    }
    public long subsetOfColor(Card.Color color) {
        return PackedCardSet.subsetOfColor(pkCardSet, color);
    }
    
    @Override
    public boolean equals(Object thatO) {
        if (thatO == null  ||  thatO.getClass() != getClass()) { // getClass same as instance of since final ?
            return false;
        }

        CardSet thatOSet= (CardSet) thatO; // Or do 2 "conversions, idk"
            return (thatOSet.pkCardSet == this.pkCardSet);
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(pkCardSet);
    }
    
    @Override
    public String toString() {
        return PackedScore.toString(pkCardSet);
    }
}
