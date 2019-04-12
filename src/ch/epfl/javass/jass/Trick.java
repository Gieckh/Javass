package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.Preconditions.checkIndex;

import ch.epfl.javass.jass.Card.Color;

//TODO: javaDoc pour antoine..
/**
 * @brief
 *
 * @see PackedTrick
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public class Trick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final Trick INVALID = new Trick(PackedTrick.INVALID); //unused?
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
    
    
    /**
     * @brief returns the first Trick of a turn,
     *        depending on the trumpColor and the starting player.
     *
     * @param trump the Color of the trump
     * @param firstPlayer the Id of the fist Player to play
     * @return the first PackedTrick of a turn
     */
    public static Trick firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        return new Trick(PackedTrick.firstEmpty(trump, firstPlayer));
    }

    /**returns a Trick from a PackedTrick.
     * 
     * @param packed
     * @return a Trick
     * 
     */
    public static Trick ofPacked(int packed) {
        checkArgument(PackedTrick.isValid(packed));
        return new Trick(packed);
    }
    
    /**@brief returns the PackedTrick of this Trick.
     * 
     * @return the PackedTrick of this Trick
     * 
     */
    public int packed() {
        return pkTrick;
    }
    
    
    /**
     * @brief returns the n+1th PackedTrick of a turn
     * return invalid if the last trick was the 9th.
     *
     * @param pkTrick
     * @return the n+1th PackedTrick of a trick
     */
    public Trick nextEmpty() {
        if (!PackedTrick.isFull(pkTrick)) {
            throw new IllegalStateException();
        }

        return new Trick(PackedTrick.nextEmpty(pkTrick));
    }

    /**
     * @brief returns true iff this trick is empty : if it has no card.
     *
     * @return a boolean that is true iff this trick has 0 card
     */
    public boolean isEmpty() {
        return PackedTrick.isEmpty(pkTrick);
    }
    
    /**
     * @brief returns true iff this trick is full : it has 4 cards
     *
     * @return boolean that is true iff this trick has 4 card
     */
    public boolean isFull() {
        return PackedTrick.isFull(pkTrick);
    }

    /**
     * @brief returns true iff this trick is the 9th.
     *
     * @return a boolean that is true iff this trick is the 9th
     */
    public boolean isLast() {
        return PackedTrick.isLast(pkTrick);
    }

    /**
     * @brief returns the number of cards in this Trick.
     *
     * @return the number of cards in this Trick
     */
    public int size() {
        return PackedTrick.size(pkTrick);
    }
    
    /**
     * @brief returns the trump color in this Trick.
     *
     * @return the color of the trump in this Trick
     */
    public Color trump() {
        return PackedTrick.trump(pkTrick);
    }
    
    /**
     * @brief returns the number of this trick.
     *
     * @return int from 0 to 8 
     */
    public int index() {
        return PackedTrick.index(pkTrick);
    }

    //TODO: test checkIndex
    /**
     * @brief returns the player that will play at the index'th position .
     *
     * @param index
     * @return playerId of the index'th player to play at this trick
     */
    public PlayerId player(int index) {
        checkIndex(index, PlayerId.COUNT);
        return PackedTrick.player(pkTrick, index);
    }
    
    /**
     * @brief returns the Card at the index'th position (from 0 to 3).
     *
     * @param index
     * @return the card at the index'th position of this trick
     */
    public Card card (int index) { //TODO: test
        checkIndex(index, PackedTrick.size(pkTrick));
        return Card.ofPacked(PackedTrick.card(pkTrick, index));
    }
    
    /**
     * @brief returns a new pkTrick whose difference is that a card has been added.
     *
     * @param pkTrick
     * @param pkCard
     * @return a packedTrick with a card added
     */
    public Trick withAddedCard(Card c) {
        if (PackedTrick.isFull(pkTrick)){
            throw new IllegalStateException();
        }

        return new Trick ( PackedTrick.withAddedCard(pkTrick, c.packed()) );
    }
    
    /**
     * @brief returns the base color of this trick.
     *
     * @return the color of the 1st card
     */
    public Color baseColor() {
        if(PackedTrick.isEmpty(pkTrick)) {
            throw new IllegalStateException();
        }

        return PackedTrick.baseColor(pkTrick);
    }
    
    /**
     * @brief returns all playable cards from a set of Cards ( pkHand)
     *  in the current trick.
     *
     * @param pkHand the set with all the cards you have
     * @return a PackedCardSet with only the cards you had ,and you can play in current case
     */
    public CardSet playableCards(CardSet hand) {
        if( PackedTrick.isFull(pkTrick)) {
            throw new IllegalStateException();
        }

        return CardSet.ofPacked(PackedTrick.playableCards(pkTrick, hand.packed()));
    }
    
    /**
     * @brief returns the points of this trick.
     *
     * @return int : the sum of all cards in this trick
     */
    public int points() {
        return PackedTrick.points(pkTrick);
    }
    
    /**
     * @brief returns the Id of the player winning this trick.
     *
     * @return PlayerId : the Id of the player which have played the best card in this trick
     */
    public PlayerId winningPlayer() {
        if(PackedTrick.isEmpty(pkTrick)) {
            throw new IllegalStateException();
        }

        return PackedTrick.winningPlayer(pkTrick);
    }


    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object thatO) {
        if (!(thatO instanceof Trick)) {
            return false;
        }

        Trick thatOTrick = (Trick) thatO; // Or do 2 "conversions, idk"
            return (thatOTrick.pkTrick == this.pkTrick);
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() { //TODO: assistant
        return pkTrick;
    }
    
    /**
    * @brief returns a String representation of this trick.
    *
    * @return String : a string with all informations about this trick
    * 
    * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
        return PackedTrick.toString(pkTrick);
    }
    
}
