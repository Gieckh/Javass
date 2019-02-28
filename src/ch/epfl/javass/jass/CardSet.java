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
    /**
     * @brief If the packed card "pkCard" is not already in the set "pkCardSet",
     *        this method puts it in [the corresponding bit is shifted from 0 to 1]. //TODO: "shifted" ?
     *        Otherwise, does nothing.
     *
     * @param pkCardSet (long) the set we want to put the packed card in
     * @param pkCard (int) the packed card we want to put in the set
     * @return (long) The previous set, where the bit corresponding to the packed card
     *         'pkCard" is at 1.
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public CardSet add(Card card) {
        return CardSet.ofPacked(PackedCardSet.add(packedSet, card.packed()));
    }
    /**
     * @brief If the card "card" is already in this set,
     *        this method remove it[the corresponding bit is shifted from 1 to 0]. //TODO: "shifted" ?
     *        Otherwise, does nothing.
     *
     * @param card (Card) the card we want to remove from the set
     * @return (long) The previous set, where the bit corresponding to the packed card
     *         'pkCard" is at 0. [i.e. where we have "removed" "pkCard"]
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public CardSet remove(Card card) {
        return CardSet.ofPacked(PackedCardSet.remove(packedSet, card.packed()));
    }
    /**
     * @brief Indicates whether this set of card contains the
     *        given card [pkCard].
     *
     * @param card (Card)
     * @return (boolean) true if this set contains "card".
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public boolean contains(Card card) {
        return PackedCardSet.contains(packedSet, card.packed());
    }
    /**
     * @brief The complement of this set of packed cards.
     *
     * @return (long) the complement of this pack of cards
     * 
     * @author - Antoine Scardigli (299905)
     */
    public CardSet complement() {
        return CardSet.ofPacked(PackedCardSet.complement(packedSet));
    }
    /**
    * @brief the union of this set with one in the parameters.
    *
    * @param pkCardSet2 (long) the second set of packed cards.
    * @return the union of this set of cards with the second one
    *
    * @author - Antoine Scardigli (299905)
    */
    public CardSet union(CardSet that) {
        return CardSet.ofPacked(PackedCardSet.union(packedSet, that.packedSet));
    }
    /**
     * @brief the intersection of this set of cards with one in the parameters.
     *
     * @param pkCardSet2 (long) the second set of packed cards.
     * @return the intersection of the this set of cards with the second one ( the parameter's one).
     *
     * @author - Antoine Scardigli (299905)
     */
    public CardSet intersection(CardSet that) {
        return CardSet.ofPacked(PackedCardSet.intersection(packedSet, that.packedSet));
    }
    
    /**
     * @brief If we interpret a set of packed cards "pkCS" the following way :
     *        pkCS = <em>{</em>b_63 * (2**63), ..., b_0 * (2 ** 0)<em>]</em>, where b_i represents
     *        the i-th bit of pkCS.
     *        Then this method simply return <em>pkCardSet1 \ pkCardSet2</em>
     *
     * @param CardSet (long) the second set of packed cards
     * @return (long) the set of packed cards formed by all the cards in this
     *         set, but not in the second.
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public CardSet difference(CardSet that) {
        return CardSet.ofPacked(PackedCardSet.difference(packedSet, that.packedSet));
    }
    
    /**
     * @brief returns the set with only the remaining cards of the chosen color from this set of cards.
     *
     * @param pkCardSet (long)
     * @param color (Color)
     * @return (long) the set with only the cards of this cardSet from a chosen color
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
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
    /**
     * @brief returns the string with the cards included in the set .
     * 
     * @return a string with the cards in the set
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    @Override
    public String toString() {
        return PackedScore.toString(packedSet);
    }
}
