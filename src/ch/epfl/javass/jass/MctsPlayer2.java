package ch.epfl.javass.jass;

import java.util.SplittableRandom;

import static ch.epfl.javass.Preconditions.checkArgument;

public final class MctsPlayer2 implements Player {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private final static int DEFAULT_EXPLORATION_PARAMETER = 40;
    private final int iterations;
    private final PlayerId ownId;
    private final long rngSeed;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    public MctsPlayer2(PlayerId ownId, long rngSeed, int iterations) {
        checkArgument(iterations >= 9);
        this.iterations = iterations;
        this.ownId = ownId;
        this.rngSeed = rngSeed;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    //Assuming the trick of this state is not full.
    @Override public Card cardToPlay(TurnState state, CardSet hand) {
        //TODO: c'est bien mais on ne devrait pas en avoir besoin
//        if(state.trick().playableCards(hand).size() ==1) {
//            return state.trick().playableCards(hand).get(0);
//        }
        if (hand.size() == 1) {
            return hand.get(0);
        }
        Node root = new Node(state, state.trick().playableCards(hand), hand, null, ownId);
        iterate(root);
        int i = root.selectNode(0);
        return root.playableCards.get(i);
    }

    private void iterate(Node root) {
        for (int i = 0; i < iterations; ++i) {
            Node toSimulate = expand(root);
            if (toSimulate != null) {
                Score simulatedScore = simulateToEndOfTurn(toSimulate.turnState,
                        toSimulate.ownHand);

                int pointsMyTeam = simulatedScore.turnPoints(ownId.team());
                while (toSimulate.father != null) {
                    toSimulate.totalPointsFromNode += pointsMyTeam;
                    toSimulate = toSimulate.father;
                }
                root.totalPointsFromNode += pointsMyTeam;
            }
        }
    }

    private Score simulateToEndOfTurn(TurnState turnState, CardSet hand) {
        //We simulate with a starting score of ZER000000000000000000000000000000000
        TurnState copyOfTurnState = TurnState.ofPackedComponents
                (PackedScore.INITIAL, turnState.packedUnplayedCards(), turnState.packedTrick());

        if (turnState.trick().isFull()) {
            copyOfTurnState = copyOfTurnState.withTrickCollected();
        }

        CardSet copyOfHand = CardSet.ofPacked(hand.packed());
        SplittableRandom rng = new SplittableRandom(rngSeed);
        while (! copyOfTurnState.isTerminal()) {
            System.out.println("unplayed cards : " + copyOfTurnState.unplayedCards());
            System.out.println("hand : " + copyOfHand.toString());
            CardSet playableCards = playableCards(copyOfTurnState, ownId, copyOfHand);
            Card randomCardToPlay = playableCards.get(rng.nextInt(playableCards.size()));
            copyOfHand = copyOfHand.remove(randomCardToPlay);

            copyOfTurnState = copyOfTurnState.withNewCardPlayedAndTrickCollected(randomCardToPlay);
        }

        return copyOfTurnState.score();
    }

    //Assuming trick not full
    private static CardSet playableCards(TurnState turnState, PlayerId playerId, CardSet hand) {
        if (turnState.nextPlayer() == playerId) {
            return turnState.trick().playableCards(hand);
        }

        return turnState.unplayedCards().difference(hand);
    }

    //Given the root of the tree, adds a new Node and returns it
    private Node expand(Node root) {
        System.out.println("expansion...");
        Node father = root;
       
        int index = father.selectNode();
        father.randomTurnsPlayed++;
        while (father.directChildrenOfNode[index] != null) {
            //System.out.println(father + ", " + father.tooString());
            father = father.directChildrenOfNode[index];
            father.randomTurnsPlayed++;
            index = father.selectNode();
//            if (father.directChildrenOfNode.length == 0) { equivalent, first one maybe faster ?
            if (father.playableCards.isEmpty()) {
                System.out.println(father.tooString());
                System.out.println("childrenless father : " + father);
                //We resimulate from here
                System.out.println("actual leaf reached");
                father.randomTurnsPlayed--;
                return father;
            }
        }

        if (father.turnState.isTerminal()) {
            System.out.println("terminal father : " + father);
            return null;
        }
        Card cardToPlay = father.playableCards.get(index);
        CardSet ownHand = father.ownHand.remove(cardToPlay);
        CardSet playableCards;
        TurnState turnState;
        PlayerId playerId;
        if (father.turnState.trick().isFull()) {
            turnState = father.turnState.withTrickCollected().withNewCardPlayed(cardToPlay);
            playableCards = playableCards(turnState, ownId, ownHand);
            playerId = turnState.trick().player(0);
        }
        else {
            turnState = father.turnState.withNewCardPlayed(cardToPlay);
            playerId = father.turnState.nextPlayer();
            playableCards = (turnState.trick().isFull()) ?
                    playableCards(turnState.withTrickCollected(), ownId, ownHand):
                    playableCards(turnState, ownId, ownHand);

        }


        Node newNode = new Node(turnState, playableCards, ownHand, father, playerId);
        newNode.randomTurnsPlayed++;
        father.directChildrenOfNode[index] = newNode;
        return newNode;
    }

    private static class Node {
        /** ============================================== **/
        /** ==============    ATTRIBUTES    ============== **/
        /** ============================================== **/
        private TurnState turnState; //The turnState corresponding to the node
        private Node[] directChildrenOfNode; //the node's children
        private CardSet playableCards; //cards playable from this node, i.e. from the player with Id playerId of a child of this Node.
        private CardSet ownHand; //the hand of the mctsPlayer
        private int totalPointsFromNode; //nb of points earned from this node
        private int randomTurnsPlayed; //number of turns played from this node
        private Node father; //the father of this node, null if it is the root
        private PlayerId playerId; //the id of the player who played to get to this node, i.e. the last player to have played.

        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/
        private Node(TurnState turnState, CardSet playableCards, CardSet ownHand, Node father, PlayerId playerId) {
            this.turnState = turnState;
            this.ownHand = ownHand;
            this.playableCards = playableCards;
            this.directChildrenOfNode = new Node[playableCards.size()];
            this.totalPointsFromNode = 0;
            this.randomTurnsPlayed = 0;
            this.father = father;
            this.playerId = playerId;
        }

        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/
        private int selectNode() {
            return selectNode(DEFAULT_EXPLORATION_PARAMETER);
        }
        private int selectNode(int explorationParameter) {
            int index = 0;
            for (Node node : directChildrenOfNode) {
                if (node == null) {
                    return index;
                }
                ++index;
            }

            float priority = 0f;
            for (int i = 0; i < directChildrenOfNode.length; ++i) {
                Node tmpNode = directChildrenOfNode[i];
                float nodeValue =(float)tmpNode.totalPointsFromNode / tmpNode.randomTurnsPlayed +
                         explorationParameter * (float)Math.sqrt(2 * Math.log(randomTurnsPlayed) / tmpNode.randomTurnsPlayed);
                if (nodeValue > priority) {
                    priority = nodeValue;
                    index = i;
                }
            }

            return index;
        }

        private String tooString() {
            String str = "   playableCards : " + playableCards.toString() + "\n";
            str += "                                            random turns played : " + randomTurnsPlayed + "\n";
            str += "                                         total points from node : " + totalPointsFromNode + "\n";
            str += "                                                      node team : " + playerId.team();
            return str;
        }
    }
}
