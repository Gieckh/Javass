package ch.epfl.javass.jass;

import java.util.LinkedList;
import java.util.List;
import java.util.SplittableRandom;

import ch.epfl.javass.Preconditions;

public final class MctsPlayer implements Player {

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    //nombre de fois qu'on execute l'algorithme
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
        if(hand.size() <= 1) {
            return hand.get(0);
        }
        //System.out.println(hand.size());
        //System.out.println(state);
        int packedCard = isIteratingNodes(state, hand.packed(), iterations);
        //System.out.println(Integer.toBinaryString(packedCard));
        return Card.ofPacked(packedCard);
    }


    private int isIteratingNodes(TurnState state , long setOfPossibleCards, int iterations){
        //System.out.println(state);
        Node bigBrother = new Node(state, setOfPossibleCards, ownId);
        for ( int i = 0 ; i< iterations ; ++i) {
            MonteCarloAlgorithm(bigBrother);
        }
        return BestChoice(bigBrother);
    }


    private int BestChoice(Node node) {
        return node.cardWeWannaPlay();
    }

  //does as written in 3.4
    private void MonteCarloAlgorithm(Node bigBrotherNode) {
        //SELECTION
        //tableau du chemin parcouru par l'alg : donc les references des nodes de haut en bas.
        List<Node> visited = new LinkedList<Node>();
        //le node tout en haut
        Node current = bigBrotherNode;
        visited.add(bigBrotherNode);
        //tant que le node a des enfant, on choisit le meilleur et on l'ajoute au tableau
        while (!current.isLeaf()) {
                current = current.selectChild();
                visited.add(current);
        }
        //EXPENSION
        // le node le plus prometteur a des enfants
        current.expand(ownId);
        Node newChildren = current.selectChild();
        visited.add(newChildren);
        //SIMULATION
        // ON calcule la valeur d'un des enfants ( tour aleatoire)
        long score = (simulateToEndOfTurn(newChildren.turnstate, CardSet.ofPacked(newChildren.setOfPossibleCards))).packed();
        //UPDATING
        //On parcourt tout le tableau et update les scores respectifs
        for (Node node : visited) {
            node.updateAttributes(PackedScore.turnTricks(score,node.playerId.team()));
        }
    }

    private static CardSet playableCards(TurnState turnState, PlayerId playerId, CardSet hand) {
        if (turnState.nextPlayer() == playerId) {
            return turnState.trick().playableCards(hand);
        }

        return turnState.unplayedCards().difference(hand);
    }

    private Score simulateToEndOfTurn(TurnState turnState, CardSet hand) {
        //We simulate with a starting score of ZER000000000000000000000000000000000
        TurnState copyOfTurnState = TurnState.ofPackedComponents(PackedScore.INITIAL, turnState.packedUnplayedCards(), turnState.packedTrick());

        if (turnState.trick().isFull()) {
            copyOfTurnState = copyOfTurnState.withTrickCollected();
        }

        CardSet copyOfHand = CardSet.ofPacked(hand.packed());
        while (! copyOfTurnState.isTerminal()) {
            CardSet playableCards = playableCards(copyOfTurnState, ownId, copyOfHand);
            Card randomCardToPlay;
            if(playableCards.size()>0) {
            randomCardToPlay = playableCards.get(rng.nextInt(playableCards.size()));
            copyOfHand = copyOfHand.remove(randomCardToPlay);
            }
            else {
                //System.out.println("here");
                return copyOfTurnState.score();
            }

            copyOfTurnState = copyOfTurnState.withNewCardPlayedAndTrickCollected(randomCardToPlay);
        }

        return copyOfTurnState.score();
    }

    private static class Node{
        /** ============================================== **/
        /** ==============    ATTRIBUTES    ============== **/
        /** ============================================== **/
        //TODO: on pourrait simplement stocker les attributs comme des entiers, pas de raison d'avoir des log
        private int CONSTANT = 40;
        private TurnState turnstate;
        private Node[] childrenOfNode;
        private long setOfPossibleCards;
        private float selfTotalPoints = 1;
        private int finishedRandomTurn = 1;
        private boolean hasChild ;
        private int card;
        private PlayerId playerId;
        //private long cardWeWannaPlay;


        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/
        private Node(TurnState turnstate , long setOfPossibleCards, PlayerId ownID) {
            this.turnstate = turnstate;
            this.setOfPossibleCards = setOfPossibleCards;
            this.selfTotalPoints = 0f;
            // 1 car sinon on a une division par 0 :/ peut mieux faire
            this.finishedRandomTurn = 1;
            this.hasChild = false;
            this.playerId = ownID;
        }


        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/


        private float twoLnOfNOfP(){
            return (float) (2 * Math.log(finishedRandomTurn));
        }

        // la difference du set de cartes pas jouées du pere vs celle du fils.
        private int cardWeWannaPlay() {
            return this.selectChild().card;
//            System.out.println(Long.toBinaryString(PackedCardSet.difference(this.turnstate.packedUnplayedCards(), this.selectChild().turnstate.packedUnplayedCards())));
//            return PackedCardSet.get(PackedCardSet.difference(this.turnstate.packedUnplayedCards(), this.selectChild().turnstate.packedUnplayedCards()),0);
        }


        private void expand(PlayerId p) {
            if(this.turnstate.trick().isFull()) {
                this.turnstate = this.turnstate.withTrickCollected();
            }
            if(PackedCardSet.size(setOfPossibleCards) >1) {
                if(this.turnstate.nextPlayer() == p ) {
                    childrenOfNode = new Node[PackedCardSet.size(setOfPossibleCards)];
                    hasChild = true;
                    for (int i=0; i<PackedCardSet.size(setOfPossibleCards); i++) {
                        int playedCard = PackedCardSet.get(setOfPossibleCards,i);
                        long newSetOfPossibleCards = PackedCardSet.difference(this.setOfPossibleCards, PackedCardSet.singleton(playedCard));
                        //System.out.println(Integer.toBinaryString(PackedCardSet.get(playedCard, 1)));
                        TurnState turnstate = this.turnstate;

                        if(PackedTrick.isFull(this.turnstate.packedTrick())) {
                            turnstate = turnstate.withTrickCollected();

                        }
                        turnstate = turnstate.withNewCardPlayed(Card.ofPacked(playedCard));
                        //nouveau turnstate et setofpossiblecard different pour chaque enfant en theorie
                        this.childrenOfNode[i] = new Node(turnstate, newSetOfPossibleCards,this.turnstate.nextPlayer());
                        this.childrenOfNode[i].card = playedCard;

                        
                        }
                    }
                    else {
                        long cs = PackedCardSet.difference(turnstate.packedUnplayedCards(),setOfPossibleCards);
                        childrenOfNode = new Node[PackedCardSet.size(cs)];
                        hasChild = true;
                        for (int i=0; i<PackedCardSet.size(cs); i++) {
                            int playedCard = PackedCardSet.get(cs,i);
                            TurnState turnstate = this.turnstate;

                            if(PackedTrick.isFull(turnstate.packedTrick())) {
                                turnstate = turnstate.withTrickCollected();

                            }
                            turnstate = turnstate.withNewCardPlayed(Card.ofPacked(playedCard));
                            //nouveau turnstate et setofpossiblecard different pour chaque enfant en theorie
                            this.childrenOfNode[i] = new Node(turnstate, setOfPossibleCards, this.turnstate.nextPlayer());
                            this.childrenOfNode[i].card = playedCard;
                            }
                    }
            }
            else {
                hasChild = true;
                childrenOfNode = new Node[PackedCardSet.size(1)];
                TurnState turnstate =this.turnstate;
                if(PackedTrick.isFull(this.turnstate.packedTrick())) {
                    turnstate = this.turnstate.withTrickCollected();
                }
                turnstate = turnstate.withNewCardPlayed(Card.ofPacked(PackedCardSet.get(setOfPossibleCards,0)));
                this.childrenOfNode[0] =  new Node(turnstate, setOfPossibleCards, this.turnstate.nextPlayer());
            }
            
        }

        

        //select the best children
        private Node selectChild() {
            Node selected = null;
            float bestValue = 0;
            if(!this.isLeaf()) {
                for (Node children : childrenOfNode) {
                    float value = getVForSon(children.selfTotalPoints,
                            children.finishedRandomTurn, CONSTANT,
                            twoLnOfNOfP());
                    //float value = children.finishedRandomTurn;
                    if (value >= bestValue) {
                        selected = children;
                        bestValue = value;
                    }
                }
            }
            return selected;
        }

        private  boolean isLeaf() {
            return (this.childrenOfNode==null )
                    ||(!this.hasChild);

        }

//        //select the best children
//        private Node selectChild() {
//            Node selected = null;
//            float bestValue = 0;
//            if(!this.isLeaf()) {
//                for (Node children : childrenOfNode) {
//                    float value = getVForSon(children.selfTotalPoints,
//                            children.finishedRandomTurn, CONSTANT,
//                            twoLnOfNOfP());
//                    //float value = children.finishedRandomTurn;
//                    if (value >= bestValue) {
//                        selected = children;
//                        bestValue = value;
//                    }
//                }
//            }
//            return selected;
//        }

//        private  boolean isLeaf() {
//            return !this.hasChild;

//            if(childrenOfNode == null) {
//                return true;
//            }
//            else {
//                return false;
//            }
        

        private void updateAttributes(int newScore) {
            selfTotalPoints = selfTotalPoints +  newScore;
          finishedRandomTurn++;
//            selfTotalPoints = selfTotalPoints*finishedRandomTurn +  newScore;
//            finishedRandomTurn++;
//            selfTotalPoints =  selfTotalPoints / finishedRandomTurn;
//
        }

        private float getVForSon(float SofSon , int NofSon, int c , float ln) {
            return (float) (SofSon/NofSon + (float)c*Math.sqrt(twoLnOfNOfP()/ NofSon));
        }



    }

}

