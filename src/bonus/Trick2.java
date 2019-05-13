package bonus;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.Preconditions.checkIndex;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.PackedTrick;
import ch.epfl.javass.jass.PlayerId;

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
public class Trick2 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/  
    public static final Trick2 INVALID = new Trick2(PackedTrick.INVALID);
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
    private Trick2(int aTrick) {
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
    public static Trick2 firstEmpty(Card.Color trump, PlayerId firstPlayer) {
        return new Trick2(PackedTrick.firstEmpty(trump, firstPlayer));
    }

    /**
     * @brief Given a [packed] trick creates the corresponding object of type Trick.
     *
     * @param packed (int) - the given [packed] trick
     * @return (Trick) - the (Trick) corresponding to the given [packed] trick.
     */
    public static Trick2 ofPacked(int packed) {
        checkArgument(PackedTrick.isValid(packed));
        return new Trick2(packed);
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
     * @see Trick2#INVALID
     *
     *
     * @return (int) - the next PackedTrick, or "INVALID" if there is none.
     * @throws IllegalArgumentException if the (Trick) "this" is full.
     */
    public Trick2 nextEmpty() {
        if (!PackedTrick.isFull(pkTrick)) {
            throw new IllegalStateException();
        }

        return new Trick2(PackedTrick.nextEmpty(pkTrick));
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
     * @brief returns a new (Trick) where the specified card has been added.
     *
     * @param c (Card) - the Card to add
     * @return (Trick) - the given (Trick), except the specified Card "c" has been added.
     * @throws IllegalStateException if "this" (Trick) is full.
     */
    public Trick2 withAddedCard(Card c) {
        if (PackedTrick.isFull(pkTrick)){
            throw new IllegalStateException();
        }

        return new Trick2 ( PackedTrick.withAddedCard(pkTrick, c.packed()) );
    }

    /**
     * @brief The color of the first card of "this" (Trick).
     *
     * @return (Color) - the Color of the first Card of this (Trick).
     * @throws IllegalStateException if "this" is empty.
     */
    public Color baseColor() {
        if(PackedTrick.isEmpty(pkTrick)) {
            throw new IllegalStateException();
        }

        return PackedTrick.baseColor(pkTrick);
    }

    /**
     * @brief Return the Cards which can be played in the "this" (Trick),
     *        given the hand "pkHand".
     *
     * @param hand (CardSet) - the given hand
     * @return (CardSet) - the cards that can be played in "this" (Trick), given
     *                     the hand "hand" (CardSet).
     * @throws IllegalArgumentException if "this" is full.
     */
    public CardSet playableCards(CardSet hand) {
        if( PackedTrick.isFull(pkTrick)) {
            throw new IllegalStateException();
        }

        return CardSet.ofPacked(PackedTrick.playableCards(pkTrick, hand.packed()));
    }

    //should only be called with a valid, full trick - idk why we don't test it.
    /**
     * @brief The value of "this" Trick, when won.
     *
     * @return (int) the value of "this".
     */
    public int points() {
        return PackedTrick.points(pkTrick);
    }

    /**
     * @brief The Id of the player currently winning "this" (Trick).
     *
     * @return (PlayerId) the Id of the player currently winning "this" (Trick).
     * @throws IllegalStateException if "this" is empty.
     */
    public PlayerId winningPlayer() {
        if(PackedTrick.isEmpty(pkTrick)) {
            throw new IllegalStateException();
        }

        return PackedTrick.winningPlayer(pkTrick);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object thatO) {
        if (!(thatO instanceof Trick2)) {
            return false;
        }

        Trick2 thatOTrick = (Trick2) thatO; // Or do 2 "conversions, idk"
            return (thatOTrick.pkTrick == this.pkTrick);
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() { 
        return pkTrick;
    }

    /**
     * @brief The textual representation of "this" (Trick)
     *
     * @return (String) The textual representation of "this" (Trick)
     */
    @Override
    public String toString() {
        return PackedTrick.toString(pkTrick);
    }
    
}
