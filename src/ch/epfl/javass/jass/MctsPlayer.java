package ch.epfl.javass.jass;

import java.util.LinkedList;
import java.util.List;
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
        return null;
    }
    
//    private long finalScoreRandomTurn(TurnState turnstate) {
//        return 0;  
//    }
    
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
        //private Float[] valuesOfSons;
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
            //this.valuesOfSons = new Float[size];
        }
        
        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/
        
        private void selectBestCard() {
            //selection
            List<Node> visited = new LinkedList<Node>();
            Node current = this;
            visited.add(this);
            while (!current.isLeaf()) {
                current = current.select();
                visited.add(current);
            }
            //expansion
            current.expand();
            Node newChildren = current.select();
            visited.add(newChildren);
            //Simulation
            float value = SimulateScoreForNode(newChildren);
            //updating
            for (Node node : visited) {
                node.updateAttributes(value);
            }
        }


        private void expand() {
            childrenOfNode = new Node[this.size-1];
            for (int i=0; i<setOfPossibleCards; i++) {
                long playedCard = PackedCardSet.get(setOfPossibleCards,i);
                long newSetOfPossibleCards = PackedCardSet.difference(this.setOfPossibleCards, playedCard);
                childrenOfNode[i] = new Node(this.turnstate, newSetOfPossibleCards, 0, 0);
            }
        }

        private Node select() {
            Node selected = null;
            float bestScore = 0;
            for (Node children : childrenOfNode) {
                float ScoreValue = VForSon(children);
                if (ScoreValue > bestScore) {
                    selected = children;
                    bestScore = ScoreValue;
                }
            }
            return selected;
        }

        private boolean isLeaf() {
            return (childrenOfNode == null);
        }

        private Float SimulateScoreForNode(Node node) {
            return 0f;
        }

        private void updateAttributes(float newScore) {
            finishedRandomTurn++;
            selfTotalPoints += newScore;
        }

        private int numberOfChildrens() {
            if(childrenOfNode == null) {
                return 0;
            }
            else {
            return childrenOfNode.length;
        }
    }

    
    
        private int getSelfTotPoints() {
            return selfTotalPoints;
        }
        
        private int bestSonIndex( int c) {
            for(int i = 0 ; i< childrenOfNode.length ; ++i) {
                
            }
            return 0;
        }
        
        private float VForSon(Node childrenOfNode) {
              return getVForSon(childrenOfNode.selfTotalPoints, childrenOfNode.finishedRandomTurn , 40);
        }

        
        private float getVForSon(int SofSon , int NofSon, int c ) {
            return (float) (SofSon/NofSon + (float)c*Math.sqrt(twoLnOfNOfP/ NofSon));
        }
        
        
        
    }
    
}

   