package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.Preconditions.checkIndex;

import ch.epfl.javass.jass.Card.Color;

/**
 * @brief this class represents a Trick. A trick indicates:
 *        - the Cards which have been played in it
 *        - the first player of the trick
 *        - the trump [of the turn]
 *        - its ordinal in the current turn
 *
 * @see PackedTrick
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public class Trick {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public static final Trick INVALID = new Trick(PackedTrick.INVALID);
    private final int pkTrick;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /**
     * @brief PRIVATE constructor of the class Trick. Called by the following method:
     * @see #ofPacked(int)
     *
     * @param aTrick (int) an encoded trick.
     */
    private Trick(int aTrick) {
        pkTrick = aTrick;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/


    /**
     * @brief returns the (int) corresponding to the first [packed] trick of a
     *        turn, given the trump and the Id of first Player.
     *
     * @param trump (Color) - the Color of the trump
     * @param firstPlayer - (PlayerId) the first Player of the turn
     * @return (int) - The first [packed] trick of a turn, given the specified parameters.
     */
    public static Trick firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        return new Trick(PackedTrick.firstEmpty(trump, firstPlayer));
    }

    /**
     * @brief Given a [packed] trick creates the corresponding object of type Trick.
     *
     * @param packed (int) - the given [packed] trick
     * @return (Trick) - the (Trick) corresponding to the given [packed] trick.
     */
    public static Trick ofPacked(int packed) {
        checkArgument(PackedTrick.isValid(packed));
        return new Trick(packed);
    }

    /**
     * @brief The [packed] version of "this" Trick
     *
     * @return (int) - The [packed] version of "this" Trick
     */
    public int packed() {
        return pkTrick;
    }


    /**
     * @brief Returns the next empty trick.
     *        If "this" was the last of the turn, returns "INVALID" instead.
     * @see Trick#INVALID
     *
     *
     * @return (int) - the next PackedTrick, or "INVALID" if there is none.
     * @throws IllegalArgumentException if the (Trick) "this" is full.
     */
    public Trick nextEmpty() {
        if (!PackedTrick.isFull(pkTrick)) {
            throw new IllegalStateException();
        }

        return new Trick(PackedTrick.nextEmpty(pkTrick));
    }

    /**
     * @brief Indicates whether "this" (Trick) contains any Card.
     *
     * @return (boolean) - true if "this" is empty.
     */
    public boolean isEmpty() {
        return PackedTrick.isEmpty(pkTrick);
    }

    /**
     * @brief Indicates whether "this" (Trick) contains 4 Cards.
     *
     * @return (boolean) true if "this" is full.
     */
    public boolean isFull() {
        return PackedTrick.isFull(pkTrick);
    }

    /**
     * @brief Indicates whether "this" (Trick) is the last of the turn
     *
     * @return (boolean) - true if "this" (Trick) is the 9-th of the turn
     */
    public boolean isLast() {
        return PackedTrick.isLast(pkTrick);
    }

    /**
     * @brief The numbers of Cards played in this "this" (Trick).
     *
     * @return (int) - the number of cards in "this"
     */
    public int size() {
        return PackedTrick.size(pkTrick);
    }

    /**
     * @brief Indicates the color of the trump in "this" (Trick)
     *
     * @return (Color) - the color of the trump in "this"
     */
    public Color trump() {
        return PackedTrick.trump(pkTrick);
    }

    /**
     * @brief Indicates the ordinal of "this" (Trick) in the current turn
     *        (between 0 and MAX_INDEX = 8)
     *
     * @return (int) - the ordinal of "this" (Trick) in the current turn.
     */
    public int index() {
        return PackedTrick.index(pkTrick);
    }

    /**
     * @brief returns the Id of the player that will play at the index-th position
     *        of "this" (Trick)
     *
     * @param index (int) - the index in this (Trick), hence between 0 and 3
     * @return (PlayerId) - the Id of the player that will play at the index-th position
     *                      of "this".
     * @throws IndexOutOfBoundsException if the index is negative, or not smaller than 4 = PlayerId.COUNT
     */
    public PlayerId player(int index) {
        checkIndex(index, PlayerId.COUNT);
        return PackedTrick.player(pkTrick, index);
    }

    /**
     * @brief returns the Card at the index-th position (from 0 to 3).
     *
     * @param index (int) - the index, in this (Trick), of the Card to get
     * @return (Card) - the card at the index'th position
     * @throws IndexOutOfBoundsException if the there is no Card at the given index
     */
    public Card card (int index) {
        checkIndex(index, PackedTrick.size(pkTrick));
        return Card.ofPacked(PackedTrick.card(pkTrick, index));
    }

    /**
     * @brief returns a new pkTrick where the specified card has been added.
     *
     * @param pkTrick (int) - the given [packed] trick - not full.
     * @param pkCard (int) - the [packed] card to add
     * @return (int) - the given [packed] trick, except the specified [packed] card
     *                 "pkCard" has been added.
     * @throws IllegalStateException if "this" (Trick) is full.
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
