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

    //packed accessors
    public long packedScore() {
        return pkScore;
    }

    public long packedUnplayedCards() {
        return pkUnplayedCards;
    }

    public int packedTrick() {
        return pkCurrentTrick;
    }

    //unpacked accessors
    public Score score() {
        return Score.ofPacked(pkScore);
    }

    public CardSet unplayedCards() {
        return CardSet.ofPacked(pkScore);
    }

    public Trick trick() {
        return Trick.ofPacked(pkCurrentTrick);
    }


    //the real methods:
    public boolean isTerminal() {
        assert (PackedTrick.isFull(pkCurrentTrick));
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

    public TurnState withTrickCollected() {
        if(!PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        return collectTrick(pkScore, pkUnplayedCards, pkCurrentTrick);
    }

    public TurnState withNewCardPlayedAndTrickCollected(Card card) {
        if(PackedTrick.isFull(pkCurrentTrick)) {
            throw new IllegalStateException();
        }

        int pkCard = card.packed();
        long newUnplayedCards = PackedCardSet.remove(pkUnplayedCards, pkCard);
        int newTrick = PackedTrick.withAddedCard(pkCurrentTrick, pkCard);

        if (!PackedTrick.isFull(newTrick)) {
            throw new IllegalStateException();
        }

        return collectTrick(pkScore, newUnplayedCards, newTrick);
    }
}
