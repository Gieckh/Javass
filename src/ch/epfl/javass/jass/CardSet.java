package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;

import java.util.List;

/**
 * The class used to represent a CardSet.
 * A CardSet is exactly what is seems: a set of cards.
 *
 * @see PackedCardSet
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
    /**
     * @brief PRIVATE constructor of the class Score. Called by the method
     *        "ofPacked(int packed)".
     *
     * @param packed (int) an encoded CardSet.
     */
    private CardSet(long packed) {
        pkCardSet = packed;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    
    /**
     * @brief Creates a new instance of (CardSet), corresponding to the given
     *        list of Cards.
     * 
     * @param cards (List of Card)
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
     * @brief Creates a new instance of CardSet from the specified long "packed".
     * 
     * @param packed (long) a packedCardSet
     * @return The unpacked version of the specified [packed] set of cards: "packed".
     * 
    */
    public static CardSet ofPacked(long packed) {
        checkArgument(PackedCardSet.isValid(packed));
        return new CardSet(packed);
    }
           

    /**
     * @brief the [packed] version of "this" CardSet.
     * 
     * @return (long) the [packed] version of "this" CardSet.
     */
    public long packed() {
        return pkCardSet;
    }
    
    /**
     * @brief Indicates whether 'this" CardSet is empty.
     *
     * @return (boolean) true iff the set of packed cards is empty [i.e. pkCardSet == 00...000]
     */
    public boolean isEmpty () {
        return PackedCardSet.isEmpty(pkCardSet);
    }
    
    /**
     * @brief Indicates the number of Cards in this set.
     *
     * @return (int) the number of cards in the set
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
     * @return (long) The previous set, except the bit corresponding to the packed card
     *         'pkCard" must be at 1.
     */
    public CardSet add(Card card) {
        return new CardSet( PackedCardSet.add(pkCardSet, card.packed()) );
    }

    /**
     * @brief If the card "card" is already in this set,
     *        this method removes it[the corresponding bit is shifted from 1 to 0].
     *        Otherwise, does nothing.
     *
     * @param card (Card) the card we want to remove from the set
     * @return (long) The previous set, except the bit corresponding to the packed card
     *         'pkCard" must be at 0. [i.e. where we have "removed" "pkCard"]
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
     */
    public boolean contains(Card card) {
        return PackedCardSet.contains(pkCardSet, card.packed());
    }
    /**
     * @brief The CardSet made of all the cards not in "this" CardSet
     *
     * @return (long) the complement of "this" CardSet
     * 
     */
    public CardSet complement() {
        return new CardSet (PackedCardSet.complement(pkCardSet));
    }

    /**
    * @brief The CardSet containing all the Cards in either "this" or "that".
    *
    * @param that (CardSet) another CardSet.
    * @return (CardSet) the union of "this" and "that"
    *
    */
    public CardSet union(CardSet that) {
        return new CardSet (PackedCardSet.union(pkCardSet, that.pkCardSet));
    }

    /**
     * @brief The CardSet containing all the Cards in both "this" and "that".
     *
     * @param that (CardSet) another CardSet.
     * @return (CardSet) the intersection of "this" and  "that"
     */
    public CardSet intersection(CardSet that) {
        return new CardSet (PackedCardSet.intersection(pkCardSet, that.pkCardSet));
    }
    
    /**
     * @brief the CardSet containing all the Cards that are in "this"
     *        but not in "that".
     *
     * <p>
     *     If we interpret a set of packed cards "pkCS" the following way :
     *     pkCS = <em>{</em>b_63 * (2**63), ..., b_0 * (2 ** 0)<em>]</em>, where b_i represents
     *     the i-th bit of pkCS.
     *     Then this method simply returns <em>pkCardSet1 \ pkCardSet2</em>
     *</p>
     *
     * @param that (CardSet) another CardSet.
     * @return (CardSet) the CardSet containing all the Cards that are in "this"
     *         but not in "that".
     */
    public CardSet difference(CardSet that) {
        return new CardSet (PackedCardSet.difference(pkCardSet, that.pkCardSet));
    }
    
    /**
     * @brief The CardSet containing only the cards that are both in "this" and of
     *        the specified Color "color"
     *
     * @param color (Color)
     * @return (CardSet) The CardSet containing only the cards that are both in "this" and of
     *         the specified Color "color"
     */
    public CardSet subsetOfColor(Card.Color color) {
        return new CardSet (PackedCardSet.subsetOfColor(pkCardSet, color));
    }
    
    /**
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


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Long.hashCode(pkCardSet);
    }

    /**
     * @brief A CardSet corresponds to a number of Cards [c1, ..., cn].
     *       This method returns the textual representation of "this".
     *       It takes the form"{c1, ..., cn}
     * 
     * @return (String) the textual representation of "this".
     */
    @Override
    public String toString() {
        return PackedCardSet.toString(pkCardSet);
    }
}
