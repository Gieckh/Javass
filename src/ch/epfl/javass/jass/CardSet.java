package ch.epfl.javass.jass;
import java.util.ArrayList;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.jass.PackedScore.isValid;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
/**
 *  //TODO : this com. sucks
 * manipulates sets of cards of a jass game.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class CardSet {

    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    private long packedSet;
    public final static CardSet EMPTY = new CardSet(PackedCardSet.EMPTY);
    public final static CardSet ALL_CARDS = new CardSet(PackedCardSet.ALL_CARDS);
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
            
    private CardSet(long packed) {
        packedSet = packed;
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
        return packedSet;
    }
    
    public boolean isEmpty () {
        return PackedCardSet.isEmpty(packedSet);
    }
    
    public int size() {
        return PackedCardSet.size(packedSet);
    }
    
    public Card get( int index) {
        return Card.ofPacked(PackedCardSet.get(packedSet, index));
    }
    
    public CardSet add(Card card) {
        return CardSet.ofPacked(PackedCardSet.add(packedSet, card.packed()));
    }
    
    public CardSet remove(Card card) {
        return CardSet.ofPacked(PackedCardSet.remove(packedSet, card.packed()));
    }
    
    public boolean contains(Card card) {
        return PackedCardSet.contains(packedSet, card.packed());
    }
    
    public CardSet complement() {
        return CardSet.ofPacked(PackedCardSet.complement(packedSet));
    }
    
    public CardSet union(CardSet that) {
        return CardSet.ofPacked(PackedCardSet.union(packedSet, that.packedSet));
    }
    
    public CardSet intersection(CardSet that) {
        return CardSet.ofPacked(PackedCardSet.intersection(packedSet, that.packedSet));
    }
    
    public CardSet difference(CardSet that) {
        return CardSet.ofPacked(PackedCardSet.difference(packedSet, that.packedSet));
    }
    public CardSet subsetOfColor(Card.Color color) {
        return CardSet.ofPacked(PackedCardSet.subsetOfColor(packedSet, color));
    }
    
    @Override
    public boolean equals(Object thatO) {
        if (thatO == null  ||  thatO.getClass() != getClass()) { // getClass same as instance of since final ?
            return false;
        }

        CardSet thatOSet= (CardSet) thatO; // Or do 2 "conversions, idk"
            return (thatOSet.packedSet == this.packedSet);
    }
    //hashcode n'est pas verifié par signcheck
    @Override
    public int hashCode() {
        return Long.hashCode(packedSet);
    }
    
    @Override
    public String toString() {
        return PackedScore.toString(packedSet);
    }
}
