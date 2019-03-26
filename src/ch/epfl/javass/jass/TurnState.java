package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;

/**
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class TurnState {
    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    private final long pkScore;
    private final long pkUnplayedCards;
    private final int pkCurrentTrick;


    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /**
     * @brief the only constructor of this class. It is private because static
     *        methods -calling this constructor- will be used to create instances
     *        of the class TurnState

     * @param pkScore (long) - the score
     * @param pkUnplayedCards (long) - the cards not played yet
     * @param pkCurrentTrick (int) - the current trick
     */
    private TurnState(long pkScore, long pkUnplayedCards, int pkCurrentTrick) {
        this.pkScore =  pkScore;
        this.pkUnplayedCards = pkUnplayedCards;
        this.pkCurrentTrick  = pkCurrentTrick;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

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
    public static TurnState initial(Card.Color trump, Score score, PlayerId firstPlayer) {
        return new TurnState(score.packed(), PackedCardSet.ALL_CARDS, PackedTrick.firstEmpty(trump, firstPlayer)); 
    }

    
    /**
     * @brief Tries to create a new TurnState, given its packed components.
     *        If one is incorrect, throws an IllegalArgumentException
     * 
     * @param pkScore (long) - the score
     * @param pkUnplayedCards (long) - the cards not played yet
     * @param pkCurrentTrick (int) - the current trick
     * @return (TurnState) a new TurnState, given its packed components.
     */
    public static TurnState ofPackedComponents(long pkScore, long pkUnplayedCards, int pkCurrentTrick) {
        checkArgument(PackedScore.isValid(pkScore) &&
                PackedCardSet.isValid(pkUnplayedCards)&&
                PackedTrick.isValid(pkCurrentTrick));


        return new TurnState(pkScore, pkUnplayedCards, pkCurrentTrick);
    }

    
    //packed accessors
    /**
     * @brief Acts as a getter for the packed version of the Score.
     * 
     * @return (long) the packed Score of this TurnState.
    */
    public long packedScore() {
        return pkScore;
    }

    /**
     * @brief Acts as a getter for the packed version of the CardSet indicating the
     *        unplayed cards.
     * 
     * @return (long) the packed CardSet of unplayed cards.
     */
    public long packedUnplayedCards() {
        return pkUnplayedCards;
    }

    /**
     * @brief Acts as a getter for the packed version of the current Trick.
     * 
     * @return (int) the packed Trick of this turnState
     */
    public int packedTrick() {
        return pkCurrentTrick;
    }


    //unpacked accessors
    /**
     * @brief Act as a getter for the unpacked version of the Score.
     * 
     * @return (Score) the [unpacked] Score of this turnState
     */
    public Score score() {
        return Score.ofPacked(pkScore);
    }

    /**
     * @brief Acts as a getter for the CardSet indicating the unplayed cards.
     * 
     * @return (CardSet) the [unpacked] CardSet of unplayed cards.
     */
    public CardSet unplayedCards() {
        return CardSet.ofPacked(pkUnplayedCards);
    }

    /**
     * @brief Acts as a getter for the unpacked version of the Trick.
     * 
     * @return (Trick) the [unpacked] Trick of this turnState
     */
    public Trick trick() {
        return Trick.ofPacked(pkCurrentTrick);
    }


    //the real methods:
    /** 
     * @brief Indicates whether the last trick of this turn has been played
     * 
     * @return true iff the last trick of this turn has been played
     */
    public boolean isTerminal() {
        return (pkCurrentTrick == PackedTrick.INVALID);
    }

    /**
     * @brief the PlayerId of the Player who has to play the next card. If the
     *        Trick of this state is full, throws an IllegalStateException
     * 
     * @return (PlayerId) the ID of the Player who has to play the next card
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
     * @param card (Card)
     * @return a new TurnState in which the next player has played the Card card
     */
    public TurnState withNewCardPlayed(Card card) {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        int pkCard = card.packed();
        return new TurnState(pkScore, PackedCardSet.remove(pkUnplayedCards, pkCard),
                             PackedTrick.withAddedCard(pkCurrentTrick, pkCard) );
    }


    //TODO: check all that follows
   
    private TurnState collectTrick(long pkScore, long pkUnplayedCards, int pkCurrentTrick) {
        long withAdditionalTrick = PackedScore.withAdditionalTrick(pkScore,
                PackedTrick.winningPlayer(pkCurrentTrick).team(),
                PackedTrick.points(pkCurrentTrick));

        return new TurnState(withAdditionalTrick, pkUnplayedCards,
                PackedTrick.nextEmpty(pkCurrentTrick));
    }

    /**
     * @brief returns a new TurnState in which the current trick has been collected.
     * 
     * @param card
     * @return a new TurnState in which the current trick has been collected
    */
    public TurnState withTrickCollected() {
        if(!PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        return collectTrick(pkScore, pkUnplayedCards, pkCurrentTrick);
    }

    
    /**
     * @brief returns a new TurnState in which the Card card
     * has been added, and the trick collected if possible.
     * 
     * @param card
     * @return a new TurnState in which the Card card
     * has been added, and the trick collected if it was full at this point
     */
    public TurnState withNewCardPlayedAndTrickCollected(Card card) {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        int pkCard = card.packed();
        long newUnplayedCards = PackedCardSet.remove(pkUnplayedCards, pkCard);
        int newTrick = PackedTrick.withAddedCard(pkCurrentTrick, pkCard);

        if (!PackedTrick.isFull(newTrick)) {
            return new TurnState(pkScore, newUnplayedCards, newTrick);
        }

        return collectTrick(pkScore, newUnplayedCards, newTrick);
    }
}
