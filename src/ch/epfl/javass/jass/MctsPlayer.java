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
        private float selfTotalPoints;
        private int finishedRandomTurn;
        private float twoLnOfNOfP;
        //private Float[] valuesOfSons;
        private int size ;
        private long cardWeWannaPlay;
        private PlayerId ownId;
        private SplittableRandom rng ;
        
        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/
        private Node(TurnState turnstate , long setOfPossibleCards, int selfTotalPoints, int finishRandomTurns, long singleCardSet, PlayerId ownId , SplittableRandom rng ) {
            this.childrenOfNode = new Node[size];
            this.turnstate = turnstate;
            this.setOfPossibleCards = setOfPossibleCards;
            this.selfTotalPoints = selfTotalPoints;
            this.finishedRandomTurn = finishRandomTurns;
            this.twoLnOfNOfP = (float) (2 * Math.log(finishedRandomTurn));
            //this.valuesOfSons = new Float[size];
            this.size =  PackedCardSet.size(setOfPossibleCards);
            this.cardWeWannaPlay = singleCardSet;
            this.ownId = ownId;
            this.rng = rng;
        }
        
        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/
        
        //does as written in 3.4
        private void selectBest() {
            //selection
            List<Node> visited = new LinkedList<Node>();
            Node current = this;
            visited.add(this);
            while (!current.isLeaf()) {
                current = current.selectChild();
                visited.add(current);
            }
            //expansion
            current.expand();
            Node newChildren = current.selectChild();
            visited.add(newChildren);
            //Simulation
            int value = SimulateScoreForNode(newChildren);
            //updating
            for (Node node : visited) {
                node.updateAttributes(value);
            }
        }


        private void expand() {
            if(size >=1) {
                childrenOfNode = new Node[this.size-1];
                for (int i=0; i<setOfPossibleCards; i++) {
                    long playedCard = PackedCardSet.get(setOfPossibleCards,i);
                    long newSetOfPossibleCards = PackedCardSet.difference(this.setOfPossibleCards, playedCard);
                    childrenOfNode[i] = new Node(this.turnstate, newSetOfPossibleCards, 0, 0, playedCard, this.ownId, this.rng);
                }
            }
        }
        //select the best children
        private Node selectChild() {
            Node selected = null;
            float bestValue = 0;
            for (Node children : childrenOfNode) {
                float Value = VForSon(children);
                if (Value > bestValue) {
                    selected = children;
                    bestValue = Value;
                }
            }
            return selected;
        }

        private boolean isLeaf() {
            return (childrenOfNode == null);
        }

        private int SimulateScoreForNode(Node node) {
            while (!node.turnstate.isTerminal() ) {
                while(!PackedTrick.isFull(node.turnstate.packedTrick())){
                    long card=0l;
                    if(node.turnstate.nextPlayer().equals(ownId)&&
                            (PackedCardSet.intersection(node.cardWeWannaPlay, node.turnstate.packedUnplayedCards())==node.cardWeWannaPlay)) {
                       card = node.cardWeWannaPlay;  
                    }
                    else {
                        
                        card = node.setOfPossibleCards;
                        card = PackedCardSet.get(card, rng.nextInt(PackedCardSet.size(card)));
                        node.setOfPossibleCards = PackedCardSet.difference(node.setOfPossibleCards, card);
                    }
                   
                    node.turnstate.withNewCardPlayed(Card.ofPacked(PackedCardSet.get(card, 0)));
                }
                node.turnstate.withTrickCollected();
            }
            return PackedScore.turnPoints(turnstate.packedScore(),ownId.team());
        }

        private void updateAttributes(int newScore) {
            selfTotalPoints = selfTotalPoints*finishedRandomTurn +  newScore;
            finishedRandomTurn++;
            selfTotalPoints =  selfTotalPoints / finishedRandomTurn;
            ;
        }

        private int numberOfChildrens() {
            if(childrenOfNode == null) {
                return 0;
            }
            else {
            return childrenOfNode.length;
        }
    }

    
    
        private float getSelfTotPoints() {
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

        
        private float getVForSon(float SofSon , int NofSon, int c ) {
            return (float) (SofSon/NofSon + (float)c*Math.sqrt(twoLnOfNOfP/ NofSon));
        }
        
        
        
    }
    
}

   