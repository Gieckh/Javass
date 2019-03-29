package ch.epfl.javass.jass;

import java.util.SplittableRandom;

import static ch.epfl.javass.Preconditions.checkArgument;

public class MctsPlayer implements Player {
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
    public MctsPlayer(PlayerId ownId, long rngSeed, int iterations) {
        checkArgument(iterations >= 9);
        this.iterations = iterations;
        this.ownId = ownId;
        this.rngSeed = rngSeed;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    @SuppressWarnings("Duplicates")
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        //default, the root teamId is this player's and its father is null.
        Node root;

        //TODO: ternary operator
        if (state.trick().isFull()) {
            assert (! state.trick().isLast()); //We should never call cardToPlay when the last Trick of the turn is full
            root = new Node(state.withTrickCollected(), playableCards(state, hand), hand,  null, ownId.team());
        }
        else {
            root = new Node(state, playableCards(state, hand), hand, null, ownId.team());
        }

        iterate(root);

        return root.playableCardsFromTurnState.get(root.selectSon(0));
    }

    /**
     * @brief Quite straightforward: will call the method "expand" on the root
     *        the number of iterations specified in this class's constructor and
     *        update the scores of each node of the tree
     *
     * @param root
     */
    private void iterate(Node root) {
        for (int i = 0; i < iterations; ++i) {
            Node newNode = expand(root);
            Score toAdd = (newNode.directChildrenOfNode.length == 0) ?
                    newNode.state.score(): simulateToEndOfTurn(newNode.state, newNode.hand);
            addScores(newNode, toAdd);
            root.totalPointsFromNode += toAdd.turnPoints(root.teamId);
            root.randomTurnsPlayed++;
        }
    }

    /**
     * @brief Given the root of the tree, this method <em>expands</em> it by a node if it can,
     *        and returns it. If a leaf was reached, this method simply returns it.
     *
     * @param root (Node) the root of the tree
     *
     * @return
     */
    private Node expand(Node root) {
        Node node = root;
        int index = root.selectSon();
        while (!(node.directChildrenOfNode.length == 0 || node.directChildrenOfNode[index] == null)) {
            //The 1st condition is there cuz we dont wanna call selectSon() with a "true leaf" (last trick of the game)
            node = node.directChildrenOfNode[index];
            index = node.selectSon();
        }

        if (node.directChildrenOfNode.length == 0) {
            return node;
        }

        //a node was created, right ?
        Node son = createNode(node);
        node.directChildrenOfNode[index] = son;
        return son;
    }

    /**
     * @brief Given a (Node) "father" whose trick is not full --"father" is therefore not a leaf,
     *        creates his next son (indicated by the parameter "nextChildIllPlayIn".
     *        This son won't be full, unless it is a leaf [i.e. it corresponds to
     *        the last card of the turn.
     *
     * @param father (Node) [not full] the (Node) whose son we wish to create.
     * @return (Node) a son of the (Node) father.
     */
    private Node createNode(Node father) {
        Card card = father.playableCardsFromTurnState.get(father.nextChildIllPlayIn);
        father.nextChildIllPlayIn++;
        CardSet sonHand = father.hand.remove(card);
        TurnState sonState;
        CardSet sonPlayableCards;
        TeamId sonTeamId;

        assert (! father.state.trick().isFull());
        sonTeamId = father.state.nextPlayer().team();

        //We never wanna have a full trick in our turnState, unless it is the last trick of the turn
        if (father.state.trick().isLast()) {
            sonState = father.state.withNewCardPlayed(card);
            //PlayableCards should never be called at the last turn of the game
            sonPlayableCards = (sonState.trick().isFull()) ?
                    CardSet.EMPTY: playableCards(sonState, sonHand);
        }
        else {
            sonState = father.state.withNewCardPlayedAndTrickCollected(card);

            sonPlayableCards = playableCards(sonState, sonHand);
        }

        return father.of(sonState, sonPlayableCards, sonHand, sonTeamId);
    }

    /**
     * @brief Given a (Node) "node" and the (Score) "score" obtained after simulating
     *        a random turn starting from this Node's TurnState, updates
     *
     * @param node
     * @param score
     */
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

    /**
     *
     * @param state
     * @param hand
     * @return
     */
    private Score simulateToEndOfTurn(TurnState state, CardSet hand) {
        assert (! state.unplayedCards().equals(CardSet.EMPTY));
        assert (! state.trick().isFull());

        TurnState copyState = state;
        CardSet copyHand = hand;
        SplittableRandom rng = new SplittableRandom(rngSeed);
        while(! copyState.isTerminal()) {
            CardSet playableCards = playableCards(copyState, copyHand);
            Card randomCardToPlay = playableCards.get(rng.nextInt(playableCards.size()));

            copyHand = copyHand.remove(randomCardToPlay);
            copyState = copyState.withNewCardPlayedAndTrickCollected(randomCardToPlay);
        }

        return copyState.score();
    }

    /**
     *
     * @param state
     * @param hand
     * @return
     */
    private CardSet playableCards(TurnState state, CardSet hand) {
//        if (state.trick().isFull() && state.trick().isLast()) {
//            return CardSet.EMPTY;
//        }

        assert (! state.unplayedCards().equals(CardSet.EMPTY));
        assert(! state.trick().isFull());

        if (state.nextPlayer() == ownId) {
            assert (! hand.equals(CardSet.EMPTY));
            return state.trick().playableCards(hand);
        }

//        return state.unplayedCards().difference(hand);
        return state.trick().playableCards(state.unplayedCards().difference(hand));
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

        /**
         * @brief Creates a new Node, given all its necessary parameters
         *
         * @param state (TurnState) the state of the game at this Node
         * @param playableCards (CardSet) the Cards which can be played from this Node
         * @param hand (CardSet) the player's hand at this state of the game
         * @param father (Node) the father of this Node. null iff this Node is the root of the tree
         * @param teamId (TeamId) the team which played the last Card leading to this Node.
         *               Not rly defined for the root.
         */
        private Node(TurnState state, CardSet playableCards, CardSet hand, Node father, TeamId teamId) {
            this.state = state;
            this.playableCardsFromTurnState = playableCards;
            this.hand = hand;
            this.teamId = teamId;
            this.father = father;

            this.directChildrenOfNode = new Node[playableCards.size()];
            this.totalPointsFromNode = 0;
            this.randomTurnsPlayed = 0;
            this.nextChildIllPlayIn = 0;
        }


        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/
        //fake constructor
        /**
         *
         * @param state
         * @param playableCards
         * @param hand
         * @param teamId
         * @return
         */
        private Node of(TurnState state, CardSet playableCards, CardSet hand, TeamId teamId) {
            return new Node(state, playableCards, hand, this, teamId);
        }

        /**
         *
         * @param node
         * @param explorationParameter
         * @return
         */
        private double evaluate(Node node, int explorationParameter) {
            return (float)node.totalPointsFromNode / node.randomTurnsPlayed +
                    explorationParameter * (float)Math.sqrt(2 * Math.log(randomTurnsPlayed) / node.randomTurnsPlayed);
        }

        //Never called with a "true leaf"

        /**
         *
         * @return
         */
        private int selectSon() {
            return selectSon(DEFAULT_EXPLORATION_PARAMETER);
        }

        /**
         *
         * @param explorationParameter
         * @return
         */
        private int selectSon(int explorationParameter) {
            assert (directChildrenOfNode.length != 0);

            // There are cards left to play
            double value = 0f;
            int index = 0;
            for (int i = 0; i < directChildrenOfNode.length; ++i) {
                Node node = directChildrenOfNode[i];
                if (node == null) {
//                    return (i | (1 << 5));
                    return i;
                }
                double tmpValue = evaluate(node, explorationParameter);
                if (tmpValue > value) {
                    value = tmpValue;
                    index = i;
                }
            }

            return index;
        }
    }
}
