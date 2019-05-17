package bonus;

import static ch.epfl.javass.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.PackedCardSet;
import ch.epfl.javass.jass.PackedScore;
import ch.epfl.javass.jass.PackedTrick;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Score;
import ch.epfl.javass.jass.Trick;
import ch.epfl.javass.jass.TurnState;

/**
 * @brief This class represents the [public] state of the game: what every Player
 *        can see/know. Namely the score of each team, the Cards left to play,
 *        and the current Trick
 *
 * @see Score
 * @see CardSet
 * @see Trick
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class TurnStateBonus {
    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    private final long pkScore;
    private final long pkUnplayedCards;
    private final int pkCurrentTrick;
    private List<CardSet> cardsThePlayersDontHave  = new ArrayList<>(Collections.nCopies(4, CardSet.EMPTY));

    


    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /**
     * @brief the only constructor of this class. It is private because static
     *        methods -calling this constructor- will be used to create instances
     *        of the class TurnState
     * @see #ofPackedComponents(long, long, int)
     *
     * @param pkScore (long) - the score
     * @param pkUnplayedCards (long) - the cards not played yet
     * @param pkCurrentTrick (int) - the current trick
     */
    private TurnStateBonus(long pkScore, long pkUnplayedCards, int pkCurrentTrick) {
        this.pkScore =  pkScore;
        this.pkUnplayedCards = pkUnplayedCards;
        this.pkCurrentTrick  = pkCurrentTrick;
        
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    public CardSet cardsAPlayerDoesntHave(PlayerId p) {
        return cardsThePlayersDontHave.get(p.ordinal());
    }
    
    private List<CardSet> actualizeCardsThePlayersDontHave(){
        this.cardsThePlayersDontHave = JassReductorOfSet.CardsThePlayerHavnt(trick(),cardsThePlayersDontHave);
    }

    //fake constructors
    /**
     * @brief Creates the initial TurnState, corresponding to turn where the trump color,
     *        the initial score and the first player are given.
     * 
     * @param trump (Color) - the trump color
     * @param score (Score) - the initial score
     * @param firstPlayer (PlayerId) - the first player of the turn
     *
     * @return (TurnState) a new TurnState, given the trump, initial score and first player.
     */
    public static TurnStateBonus initial(Card.Color trump, Score score, PlayerId firstPlayer) {
        return new TurnStateBonus(score.packed(), PackedCardSet.ALL_CARDS, PackedTrick.firstEmpty(trump, firstPlayer)); 
    }

    
    /**
     * @brief Tries to create a new TurnState, given its packed components.
     *        If one is incorrect, throws an IllegalArgumentException
     * 
     * @param pkScore (long) - the score
     * @param pkUnplayedCards (long) - the cards not played yet
     * @param pkCurrentTrick (int) - the current trick
     * @return (TurnState) a new TurnState, given its packed components.
     * @throws IllegalArgumentException if one [at least] of the components of
     *                                  the (TurnState) "this" is invalid.
     */
    public static TurnStateBonus ofPackedComponents(long pkScore, long pkUnplayedCards, int pkCurrentTrick) {
        checkArgument(PackedScore.isValid(pkScore) &&
                PackedCardSet.isValid(pkUnplayedCards)&&
                PackedTrick.isValid(pkCurrentTrick));


        return new TurnStateBonus(pkScore, pkUnplayedCards, pkCurrentTrick);
    }

    
    //packed accessors
    /**
     * @brief Acts as a getter for the packed version of the Score.
     * 
     * @return (long) - the packed Score of this TurnState.
     */
    public long packedScore() {
        return pkScore;
    }

    /**
     * @brief Acts as a getter for the packed version of the CardSet indicating the
     *        unplayed cards.
     * 
     * @return (long) - the packed CardSet of unplayed cards.
     */
    public long packedUnplayedCards() {
        return pkUnplayedCards;
    }

    /**
     * @brief Acts as a getter for the packed version of the current Trick.
     * 
     * @return (int) - the packed Trick of this turnState
     */
    public int packedTrick() {
        return pkCurrentTrick;
    }


    //unpacked accessors
    /**
     * @brief Act as a getter for the unpacked version of the Score.
     * 
     * @return (Score) - the [unpacked] Score of this turnState
     */
    public Score score() {
        return Score.ofPacked(pkScore);
    }

    /**
     * @brief Acts as a getter for the CardSet indicating the unplayed cards.
     * 
     * @return (CardSet) - the [unpacked] CardSet of unplayed cards.
     */
    public CardSet unplayedCards() {
        return CardSet.ofPacked(pkUnplayedCards);
    }

    /**
     * @brief Acts as a getter for the unpacked version of the Trick.
     * 
     * @return (Trick) - the [unpacked] Trick of this turnState
     */
    public Trick trick() {
        return Trick.ofPacked(pkCurrentTrick);
    }


    //the real methods:
    /** 
     * @brief Indicates whether the last trick of this turn has been played
     * 
     * @return (boolean) - true iff the last trick of this turn has been played AND COLLECTED
     */
    public boolean isTerminal() {
        return (pkCurrentTrick == PackedTrick.INVALID);
    }

    /**
     * @brief the PlayerId of the Player who has to play the next card. If the
     *        Trick of this state is full, throws an IllegalStateException
     * 
     * @return (PlayerId) - the ID of the Player who has to play the next card
     * @throws IllegalStateException if the (Trick) of the (TurnState) "this" is full.
     */
    public PlayerId nextPlayer() {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        return PackedTrick.player(pkCurrentTrick, PackedTrick.size(pkCurrentTrick));
    }

    
    /**
     * @brief returns a new TurnState in which the next player has played the Card card.
     * 
     * @param card (Card) - the (Card) to play
     * @return (TurnState) - a new TurnState in which the next player has played the Card card
     * @throws IllegalStateException if the (Trick) of the (TurnState) "this" is full.
     */
    public TurnStateBonus withNewCardPlayed(Card card) {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        int pkCard = card.packed();
        return new TurnStateBonus(pkScore, PackedCardSet.remove(pkUnplayedCards, pkCard),
                             PackedTrick.withAddedCard(pkCurrentTrick, pkCard) );
    }

    /**
     * @brief Method created to avoid duplicates.
     *        Given the [packed] arguments of a full TurnState, collects it and
     *        updates it accordingly [i.e. collects the trick and updates the score]
     *        Then returns the corresponding new TurnState.
     * @see #withTrickCollected()
     * @see #withNewCardPlayedAndTrickCollected(Card)
     */
    private TurnStateBonus collectTrick(long pkScore, long pkUnplayedCards, int pkCurrentTrick) {
        long withAdditionalTrick = PackedScore.withAdditionalTrick(pkScore,
                PackedTrick.winningPlayer(pkCurrentTrick).team(),
                PackedTrick.points(pkCurrentTrick));

        return new TurnStateBonus(withAdditionalTrick, pkUnplayedCards,
                PackedTrick.nextEmpty(pkCurrentTrick));
    }

    /**
     * @brief returns a new TurnState in which the current trick has been collected.
     *
     * @return (TurnState) a new TurnState in which the current trick has been collected
     * @throws IllegalStateException if the (Trick) of the (TurnState) "this" is not full
     */
    public TurnStateBonus withTrickCollected() {
        if(!PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        return collectTrick(pkScore, pkUnplayedCards, pkCurrentTrick);
    }

    
    /**
     * @brief returns a new TurnState in which the Card card
     * has been added, and the trick collected if possible.
     * 
     * @param card (Card)
     * @return (TurnState) a new TurnState in which the Card card
     *         has been added, and the trick collected if it was full at this point
     * @throws IllegalStateException if the (Trick) of the (TurnState) "this" is full
     */

    public TurnStateBonus withNewCardPlayedAndTrickCollected(Card card) {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        int pkCard = card.packed();
        long newUnplayedCards = PackedCardSet.remove(pkUnplayedCards, pkCard);
        int newTrick = PackedTrick.withAddedCard(pkCurrentTrick, pkCard);

        if (!PackedTrick.isFull(newTrick)) {
            return new TurnStateBonus(pkScore, newUnplayedCards, newTrick);
        }

        return collectTrick(pkScore, newUnplayedCards, newTrick);
    }
}
