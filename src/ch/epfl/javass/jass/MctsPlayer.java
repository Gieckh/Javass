package ch.epfl.javass.jass;

import java.util.SplittableRandom;

import ch.epfl.javass.Preconditions;

public final class MctsPlayer implements Player {

    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    int iterations;
    SplittableRandom rng ;
    PlayerId ownId;
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    public MctsPlayer(PlayerId ownId, long rngSeed, int iterations){
        this.rng = new SplittableRandom(rngSeed);
        this.ownId = ownId;
        Preconditions.checkArgument(iterations>=9);
        this.iterations = iterations;
    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    

    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    private long finalScoreRandomTurn(TurnState turnstate) {
        return 0;  
    }
    
    private long playablesCards(TurnState turnstate, long hand) {
       return PackedCardSet.intersection(turnstate.packedUnplayedCards(), PackedCardSet.complement(hand) );
    }
    
    
    private static class Node{
        
        /** ============================================== **/
        /** ==============    ATTRIBUTES    ============== **/
        /** ============================================== **/
        private TurnState turnstate;
        private Node[] childrenOfNode ;
        private long setOfPossibleCards;
        private int selfTotalPoints;
        private int finishedRandomTurn;
        
        
        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/
        
        private Node(TurnState turnstate , long setOfPossibleCards, int selfTotalPoints, int finishRandomTurns) {
            this.childrenOfNode = null;
            this.turnstate = turnstate;
            this.setOfPossibleCards = setOfPossibleCards;
            this.selfTotalPoints = selfTotalPoints;
            this.finishedRandomTurn = finishRandomTurns;
        }
        
        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/
//
//        private int bestSonIndex( int c) {
//            for(int i = 0 ; childrenOfNode.length ; ++i) {
//
//            }
//        }
        
        private float getV(int numberP , int c) {
            return (float) (selfTotalPoints/finishedRandomTurn+ (float)c*Math.sqrt(2*Math.log(numberP)/ finishedRandomTurn));
        }
        
    }
    
}
