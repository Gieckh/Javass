package ch.epfl.javass.jass;
import java.util.ArrayList;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.jass.PackedScore.isValid;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class CardSet {

    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    private long packedSet;
    public final static CardSet EMPTY = new CardSet(PackedCardSet.EMPTY);
    public final static CardSet All_CARDS = new CardSet(PackedCardSet.ALL_CARDS);
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
            
    private CardSet(long packed) {
        packedSet = packed;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
   // coucou les musulmans
    // quelle est le compble pour un noir insomniaque
    
    
    public static CardSet of(List<Card> cards) {
        CardSet flashMacQueen = new CardSet(0L);
        for (Card card: cards ) {
            flashMacQueen.add(card);
        }
        return flashMacQueen;
    }
    
    //passer une nuit blanche
    public static CardSet ofPacked(long packed) {
        checkArgument(isValid(packed));
        return new CardSet(packed);
    }
           

    public long packed() {
        return packedSet;
    }
    
    public boolean isEmpty () {
        return PackedCardSet.isEmpty(packedSet);
    }
    
    public int size() {
        return PackedCardSet.size(packedSet);
    }
    
    public int get( int index) {
        return PackedCardSet.get(packedSet, index);
    }
    
    public long add(Card card) {
        return PackedCardSet.add(packedSet, card.packed());
    }
    
    public long remove(Card card) {
        return PackedCardSet.remove(packedSet, card.packed());
    }
    
    public boolean contains(Card card) {
        return PackedCardSet.contains(packedSet, card.packed());
    }
    
    public long complement() {
        return PackedCardSet.complement(packedSet);
    }
    
    public long union(CardSet that) {
        return PackedCardSet.union(packedSet, that.packedSet);
    }
    
    public long intersection(CardSet that) {
        return PackedCardSet.intersection(packedSet, that.packedSet);
    }
    
    public long difference(CardSet that) {
        return PackedCardSet.difference(packedSet, that.packedSet);
    }
    public long subsetOfColor(Card.Color color) {
        return PackedCardSet.subsetOfColor(packedSet, color);
    }
    
    @Override
    public boolean equals(Object thatO) {
        if (thatO == null  ||  thatO.getClass() != getClass()) { // getClass same as instance of since final ?
            return false;
        }

        CardSet thatOSet= (CardSet) thatO; // Or do 2 "conversions, idk"
            return (thatOSet.packedSet == this.packedSet);
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(packedSet);
    }
    
    @Override
    public String toString() {
        return PackedScore.toString(packedSet);
    }
}
