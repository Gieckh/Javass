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
        private float twoLnOfNOfP;
        private Float[] valuesOfSons;
        private int size = PackedCardSet.size(setOfPossibleCards);
        
        
        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/
        
        private Node(TurnState turnstate , long setOfPossibleCards, int selfTotalPoints, int finishRandomTurns) {
            this.childrenOfNode = new Node[size];
            this.turnstate = turnstate;
            this.setOfPossibleCards = setOfPossibleCards;
            this.selfTotalPoints = selfTotalPoints;
            this.finishedRandomTurn = finishRandomTurns;
            this.twoLnOfNOfP = (float) (2 * Math.log(finishedRandomTurn));
            this.valuesOfSons = new Float[size];
        }
        
        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/
        
        protected int getSelfTotPoints() {
            return selfTotalPoints;
        }
        
        private int bestSonIndex( int c) {
            for(int i = 0 ; i< childrenOfNode.length ; ++i) {
                
            }
            return 0;
        }
        
        private void VForSons() {
            for(int i = 0 ; i < size ; ++i) {
                if(childrenOfNode[i] != null) {
                    valuesOfSons[i] = getVForSon(childrenOfNode[i].selfTotalPoints, childrenOfNode[i].finishedRandomTurn , 40);
                }
            }
        }
        
        
        
        private float getVForSon(int NofSon ,int SofSon, int c) {
            return (float) (SofSon/NofSon + (float)c*Math.sqrt(twoLnOfNOfP/ NofSon));
        }
        
    }
    
}
