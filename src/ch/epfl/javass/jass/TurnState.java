package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;

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
    public static TurnState initial(Card.Color trump, Score score, PlayerId firstPlayer) {
        return new TurnState(score.packed(), PackedCardSet.ALL_CARDS, PackedTrick.firstEmpty(trump, firstPlayer)); 
    }

    public static TurnState ofPackedComponents(long pkScore, long pkUnplayedCards, int pkCurrentTrick) {
        checkArgument(PackedScore.isValid(pkScore) &&
                         PackedCardSet.isValid(pkUnplayedCards) &&
                         PackedTrick.isValid(pkCurrentTrick));

        return new TurnState(pkScore, pkUnplayedCards, pkCurrentTrick);
    }

    //accessors
    public long packedScore() {
        return pkScore;
    }

    public long packedUnplayedCards() {
        return pkUnplayedCards;
    }

    public int packedTrick() {
        return pkCurrentTrick;
    }

    public Score score() {
        return Score.ofPacked(pkScore);
    }

    public CardSet unplayedCards() {
        return CardSet.ofPacked(pkScore);
    }

    public Trick trick() {
        return Trick.ofPacked(pkCurrentTrick);
    }


    public boolean isTerminal() {
        return (PackedTrick.nextEmpty(pkCurrentTrick) == PackedTrick.INVALID);
    }

    public PlayerId nextPlayer() {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        return PackedTrick.player(pkCurrentTrick, PackedTrick.size(pkCurrentTrick));
    }

    public TurnState withNewCardPlayed(Card card) {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }
        else {
            return new TurnState(pkScore, PackedCardSet.remove(pkUnplayedCards, card.packed()),PackedTrick.withAddedCard(pkCurrentTrick, card.packed()));
        }
    }

    public TurnState withTrickCollected() {
        if(!PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }
        else {
            return new TurnState(PackedScore.nextTurn(pkScore), pkUnplayedCards , pkCurrentTrick);
        }
        
    }

    public TurnState withNewCardPlayedAndTrickCollected(Card card) {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }
        else {
            if(!PackedTrick.isFull(pkCurrentTrick)) {
                throw new IllegalStateException();
            }
            else {
                return new TurnState(PackedScore.nextTurn(pkScore), PackedCardSet.remove(pkUnplayedCards, card.packed()),PackedTrick.withAddedCard(pkCurrentTrick, card.packed()));
            }
        }
    }
}
