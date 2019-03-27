package ch.epfl.javass.jass;

import java.util.SplittableRandom;

import static ch.epfl.javass.Preconditions.checkArgument;

public class MctsPlayer3 implements Player {
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

    private void expand(Node root) {
        Node node = root;
        int index = root.selectSon();
        while (!(node.directChildrenOfNode.length == 0 || node.directChildrenOfNode[index] == null)) {
            //The 2nd condition is there cuz we dont wanna call selectSon() with a "true leaf"
            node = node.directChildrenOfNode[index];
            index = node.selectSon();
        }

        if (node.directChildrenOfNode.length == 0) {
            addScores(node, node.state.score());
            return;
        }

        //a node was created, right ?
        assert(node.state == null);
        updateNode(node.father, node);
    }

    private void updateNode(Node father, Node son) {
        Card card = father.playableCardsFromTurnState.get(father.nextChildIllPlayIn);
        father.nextChildIllPlayIn++;
        CardSet hand = father.hand.remove(card);

        if (father.state.trick().isFull()) {
            son.state = father.state.withTrickCollected().withNewCardPlayed(card);
            son.playableCardsFromTurnState = playableCards(son.state, hand);
            son.teamId = son.state.trick().player(0).team();
            son.directChildrenOfNode = new Node[son.playableCardsFromTurnState.size()];
        }

        else {

        }
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
        assert (! state.unplayedCards().equals(CardSet.EMPTY));
        assert (! state.trick().isFull());

        TurnState copyState = state;
        CardSet copyHand = hand;
        SplittableRandom rng = new SplittableRandom(rngSeed);
        while(! copyState.isTerminal()) {
            CardSet playableCards = playableCards(copyState, hand);
            Card randomCardToPlay = playableCards.get(rng.nextInt(playableCards.size()));

            copyHand = copyHand.remove(randomCardToPlay);
            copyState = copyState.withNewCardPlayedAndTrickCollected(randomCardToPlay);
        }

        return copyState.score();
    }

    private CardSet playableCards(TurnState state, CardSet hand) {
        assert (! state.unplayedCards().equals(CardSet.EMPTY));

        if (state.nextPlayer() == ownId) {
            assert (! hand.equals(CardSet.EMPTY));
            return state.trick().playableCards(hand);
        }

        return state.unplayedCards().difference(hand);
    }




    private static final class Node {
        /** ============================================== **/
        /** ==============    ATTRIBUTES    ============== **/
        /** ============================================== **/
        private final TurnState state;
        private final Node[] directChildrenOfNode;
        private final CardSet playableCardsFromTurnState;
        private final CardSet hand;
        private int totalPointsFromNode;
        private int randomTurnsPlayed;
        private int nextChildIllPlayIn;
        private final Node father;
        private final TeamId teamId;


        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/

        private Node(TurnState state, CardSet playableCards, CardSet hand, Node father, TeamId teamId) {
            this.state = state;
            this.playableCardsFromTurnState = playableCards;
            this.hand = hand;
            this.teamId = teamId;

            this.directChildrenOfNode = new Node[playableCards.size()];
            this.totalPointsFromNode = 0;
            this.randomTurnsPlayed = 0;
            this.nextChildIllPlayIn = 0;
            this.father = father;
        }

        private Node(Node father) {
            this.state = null;
            this.directChildrenOfNode = null;
            this.playableCardsFromTurnState = null;
            this.hand = null;
            this.father = father;
            this.teamId = null;
        }


        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/
        //fake constructor
        private Node of(TurnState state, CardSet playableCards, CardSet hand, TeamId teamId) {
            return new Node(state, playableCards, hand, this, teamId);
        }


        private double evaluate(Node node, int explorationParameter) {
            return (float)node.totalPointsFromNode / node.randomTurnsPlayed +
                    explorationParameter * (float)Math.sqrt(2 * Math.log(randomTurnsPlayed) / node.randomTurnsPlayed);
        }

        //Never called with a "true leaf"
        private int selectSon() {
            return selectSon(DEFAULT_EXPLORATION_PARAMETER);
        }
        private int selectSon(int explorationParameter) {
            assert (directChildrenOfNode.length != 0);

            // There are cards left to play
            Node nodeToReturn = directChildrenOfNode[0];
            double value = 0f;
            int index = 0;
            for (int i = 0; i < directChildrenOfNode.length; ++i) {
                Node node = directChildrenOfNode[i];
                if (node == null) {
                    return (i | (1 << 5));
                }
                double tmpValue = evaluate(node, explorationParameter);
                if (tmpValue > value) {
                    nodeToReturn = node;
                    value = tmpValue;
                    index = i;
                }
            }

            return index;
        }

        //We encode selectSon with a boolean.
        private static boolean mustCreate(int index) {
            int mask = 1 << 5;
            return (index & mask) == mask;
        }
    }
}
