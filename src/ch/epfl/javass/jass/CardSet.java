package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;

import java.util.List;

/**
 * CardSet
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class CardSet {

    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    
    private long pkCardSet;
    public final static CardSet EMPTY = new CardSet(PackedCardSet.EMPTY);
    public final static CardSet ALL_CARDS = new CardSet(PackedCardSet.ALL_CARDS);
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    private CardSet(long packed) {
        pkCardSet = packed;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    
    /**
     * @brief constructs a cardSet from a list of cards.
     * 
     * @param cards ( a list)
     * @return a CardSet which contains all cards of the list
     * 
    */
    public static CardSet of(List<Card> cards) {
        long packed = 0L;
        for (Card card : cards) {
            packed = PackedCardSet.add(packed, card.packed());
        }

        return new CardSet(packed);
    }


    /**
     * @brief constructs a cardset from a packCardSet.
     * 
     * @param packed ( long ) a packedCardSet
     * @return a CardSet from the packedCardSet "packed"
     * 
    */
    public static CardSet ofPacked(long packed) {
        checkArgument(PackedCardSet.isValid(packed));
        return new CardSet(packed);
    }
           

    /**
     * @brief returns the packedCardSet corresponding to this cardSet .
     * 
     * @return (long) the packedCardSet of this cardSet
     * 
    */
    public long packed() {
        return pkCardSet;
    }
    
    /**
     * @brief returns true iff this set is empty
     *
     * @return (boolean) true iff the set of packed cards is empty [i.e. pkCardSet == 00...000]
     *
     */
    public boolean isEmpty () {
        return PackedCardSet.isEmpty(pkCardSet);
    }
    
    /**
     * @brief Indicates how many cards are in this set.
     *
     * @return (int) the number of cards in the set
     *
     */
    public int size() {
        return PackedCardSet.size(pkCardSet);
    }
    
    /**
     * @brief returns the index-th packed card from this set of packed cards.
     *        if [index == 0], then the card given by the least significant 1-bit
     *        of the set of cards is returned
     *
     * @param index (int) //TODO
     * @return (Card) the index-th <em> card</em> from this set of cards.
     */
    public Card get(int index) {
        return Card.ofPacked(PackedCardSet.get(pkCardSet, index));
    }
    /**
     * @brief If the packed card "pkCard" is not already in the set "pkCardSet",
     *        this method puts it in [the corresponding bit is shifted from 0 to 1].
     *        Otherwise, does nothing.
     *
     * @param card (Card) the Card we want to add in the set
     * @return (long) The previous set, where the bit corresponding to the packed card
     *         'pkCard" is at 1.
     *
     */
    public CardSet add(Card card) {
        return new CardSet( PackedCardSet.add(pkCardSet, card.packed()) );
    }
    /**
     * @brief If the card "card" is already in this set,
     *        this method remove it[the corresponding bit is shifted from 1 to 0].
     *        Otherwise, does nothing.
     *
     * @param card (Card) the card we want to remove from the set
     * @return (long) The previous set, where the bit corresponding to the packed card
     *         'pkCard" is at 0. [i.e. where we have "removed" "pkCard"]
     *
     */
    public CardSet remove(Card card) {
        return new CardSet( PackedCardSet.remove(pkCardSet, card.packed()) );
    }

    /**
     * @brief Indicates whether this set of card contains the
     *        given card [Card].
     *
     * @param card (Card)
     * @return (boolean) true if this set contains "card".
     *
     */
    public boolean contains(Card card) {
        return PackedCardSet.contains(pkCardSet, card.packed());
    }
    /**
     * @brief The complement of this set of packed cards.
     *
     * @return (long) the complement of this pack of cards
     * 
     */
    public CardSet complement() {
        return new CardSet (PackedCardSet.complement(pkCardSet));
    }

    /**
    * @brief the union of this set with one in the parameters.
    *
    * @param pkCardSet2 (long) the second set of packed cards.
    * @return the union of this set of cards with the second one
    *
    */
    public CardSet union(CardSet that) {
        return new CardSet (PackedCardSet.union(pkCardSet, that.pkCardSet));
    }

    /**
     * @brief the intersection of this set of cards with one in the parameters.
     *
     * @param pkCardSet2 (long) the second set of packed cards.
     * @return the intersection of the this set of cards with the second one ( the parameter's one).
     *
     */
    public CardSet intersection(CardSet that) {
        return new CardSet (PackedCardSet.intersection(pkCardSet, that.pkCardSet));
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
     */
    public CardSet difference(CardSet that) {
        return new CardSet (PackedCardSet.difference(pkCardSet, that.pkCardSet));
    }
    
    /**
     * @brief returns the set with only the remaining cards of the chosen color from this set of cards.
     *
     * @param pkCardSet (long)
     * @param color (Color)
     * @return (long) the set with only the cards of this cardSet from a chosen color
     *
     */
    public CardSet subsetOfColor(Card.Color color) {
        return new CardSet (PackedCardSet.subsetOfColor(pkCardSet, color));
    }
    
    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object thatO) {
        //using "instanceof", since final class
        if (!(thatO instanceof CardSet)) { //the call to instanceof handles the case (thatO == null)
            return false;
        }

        return this.pkCardSet == ((CardSet) thatO).pkCardSet;
    }


    /* 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Long.hashCode(pkCardSet);
    }

    /**
     * @brief returns the string with the cards included in the set .
     * 
     * @return a (String) with the cards in the set
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    @Override
    public String toString() {
        return PackedCardSet.toString(pkCardSet);
    }
}
