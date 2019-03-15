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
     * @Brief returns a new TurnState representing the stating of a turn with 
     * the specified parameters.
     * 
     * @param trump : the trump color
     * @param score : the Score
     * @param firstPlayer
     * @return a new TurnState representing the stating of a turn with the specified parameters
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static TurnState initial(Card.Color trump, Score score, PlayerId firstPlayer) {
        return new TurnState(score.packed(), PackedCardSet.ALL_CARDS, PackedTrick.firstEmpty(trump, firstPlayer)); 
    }

    
    /**
     * @Brief returns a new TurnState with all
     *  informations needed given as packed parameters.
     * 
     * @param pkScore
     * @param pkUnplayedCards
     * @param pkCurrentTrick
     * @return a new TurnState with all informations needed given as packed parameters
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static TurnState ofPackedComponents(long pkScore, long pkUnplayedCards, int pkCurrentTrick) {
        checkArgument(PackedScore.isValid(pkScore) &&
                         PackedCardSet.isValid(pkUnplayedCards) &&
                         PackedTrick.isValid(pkCurrentTrick));

        return new TurnState(pkScore, pkUnplayedCards, pkCurrentTrick);
    }

    
    //packed accessors
    
    /**
     * @Brief act as a getter for the PackedScore.
     * 
     * @return the packed Score of this turnState
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public long packedScore() {
        return pkScore;
    }

    /**
     * @Brief act as a getter for the PackedUnplayedCards set.
     * 
     * @return the packed UnplayedCardSet of this turnState
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public long packedUnplayedCards() {
        return pkUnplayedCards;
    }

    /**
     * @Brief act as a getter for the PackedTrick.
     * 
     * @return the packed Trick of this turnState
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public int packedTrick() {
        return pkCurrentTrick;
    }

    //unpacked accessors
    /**
     * @Brief act as a getter for the Score.
     * 
     * @return the  Score of this turnState
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public Score score() {
        return Score.ofPacked(pkScore);
    }

    /**
     * @Brief act as a getter for the unplayedCardSet.
     * 
     * @return the UnplayedCardSet of this turnState
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public CardSet unplayedCards() {
        return CardSet.ofPacked(pkUnplayedCards);
    }

    /**
     * @Brief act as a getter for the Trick.
     * 
     * @return the Trick of this turnState
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public Trick trick() {
        return Trick.ofPacked(pkCurrentTrick);
    }


    //the real methods:
    /** 
     * @Brief returns true iff the last trick of this turn has been played.
     * 
     * @return true iff the last trick of this turn has been played
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public boolean isTerminal() {
        //assert (PackedTrick.isFull(pkCurrentTrick)); //TODO: pourquoi ce assert ?
        return (pkCurrentTrick == PackedTrick.INVALID);
    }

    /**
     * @Brief returns the PlayerId of the Player who has to play the next card.
     * 
     * @return the PlayerId of the Player who has to play the next card
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public PlayerId nextPlayer() {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        return PackedTrick.player(pkCurrentTrick, PackedTrick.size(pkCurrentTrick));
    }

    
    /**
     * @Brief returns a new TurnState in which the next player has played the Card card.
     * 
     * @param card
     * @return a new TurnState in which the next player has played the Card card
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
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

        return new TurnState(PackedScore.nextTurn(withAdditionalTrick), pkUnplayedCards,
                PackedTrick.nextEmpty(pkCurrentTrick));
    }
    /**
     * @Brief returns a new TurnState in which the current trick has been recolted.
     * 
     * @param card
     * @return a new TurnState in which the current trick has been recolted
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public TurnState withTrickCollected() {
        if(!PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        return collectTrick(pkScore, pkUnplayedCards, pkCurrentTrick);
    }

    
    /**
     * @Brief returns a new TurnState in which the Card card 
     * has been added, and the trick collected if possible.
     * 
     * @param card
     * @returna new TurnState in which the Card card 
     * has been added, and the trick collected if it was full at this point
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
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
