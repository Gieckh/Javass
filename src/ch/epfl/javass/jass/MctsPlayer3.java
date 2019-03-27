package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;

public class MctsPlayer3 extends Player {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private static final int DEFAULT_EXPLORATION_PARAMETER = 40;
    private final int iterations;
    private final PlayerId ownId;
    private final long rngSeed;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    public MctsPlayer3(PlayerId ownId, long rngSeed, int iterations) {
        checkArgument(iterations >= 9);
        this.iterations = iterations;
        this.ownId = ownId;
        this.rngSeed = rngSeed;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        return null;
    }

    private void iterate(Node root) {
        for (int i = 0; i < iterations; ++i) {
            expand(root);
        }
    }

    private Node expand(Node root) {
        Node father = root;
        while (!(father.state == null || father.directChildrenOfNode.length == 0)) {
            father = father.selectSon();
        }


    }

    private void updateNode(Node father, Node son) {

    }

    private void addScores(Node node, Score score) {
        assert (node != null);
        assert(node.state != null);

        Node copy = node;
        while (copy.father != null) {
            copy.randomTurnsPlayed++;
            copy.totalPointsFromNode += score.turnPoints(copy.teamId);
            copy = copy.father;
        }
    }

    private Score simulateToEndOfTurn(TurnState state, CardSet hand) {

    }

    private CardSet playableCards(TurnState state, CardSet hand, PlayerId id) {

    }




    private static final class Node {
        /** ============================================== **/
        /** ==============    ATTRIBUTES    ============== **/
        /** ============================================== **/
        private TurnState state;
        private Node[] directChildrenOfNode;
        private CardSet playableCardsFromTurnState;
        private int totalPointsFromNode;
        private int randomTurnsPlayed;
        private int nextChildIllPlayIn;
        private Node father;
        private TeamId teamId;


        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/

        private Node() {
            this.state = null;
            this.directChildrenOfNode = null;
            this.playableCardsFromTurnState = null;
            this.father = null;
            this.teamId = null;
        }

        private Node(TurnState state, CardSet playableCards, Node father, TeamId teamId) {
            this.state = state;
            this.playableCardsFromTurnState = playableCards;
            this.teamId = teamId;

            this.directChildrenOfNode = new Node[playableCards.size()];
            this.totalPointsFromNode = 0;
            this.randomTurnsPlayed = 0;
            this.nextChildIllPlayIn = 0;
            this.father = father;
        }


        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/
        //fake constructor
        private Node of(TurnState state, CardSet playableCards) {
            return new Node(state, playableCards, this);
        }


        private double evaluate(Node node, int explorationParameter) {
            return (float)node.totalPointsFromNode / node.randomTurnsPlayed +
                    explorationParameter * (float)Math.sqrt(2 * Math.log(randomTurnsPlayed) / node.randomTurnsPlayed);
        }

        private Node selectSon() {
            return selectSon(DEFAULT_EXPLORATION_PARAMETER);
        }
        private Node selectSon(int explorationParameter) {
            assert (directChildrenOfNode.length != 0);

            // There are cards left to play
            Node nodeToReturn = directChildrenOfNode[0];
            double value = 0f;
            for (int i = 0; i < directChildrenOfNode.length; ++i) {
                Node node = directChildrenOfNode[i];
                if (node == null) {
                    return new Node();
                }
                double tmpValue = evaluate(node, explorationParameter);
                if (tmpValue > value) {
                    nodeToReturn = node;
                    value = tmpValue;
                }
            }

            return nodeToReturn;
        }
    }
}
