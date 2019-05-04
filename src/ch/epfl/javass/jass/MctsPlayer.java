package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;

import java.util.SplittableRandom;

/**
 * @brief This class extends Player and only overrides the method "cardToPlay".
 *        To decide which card to play, the computer uses a Monte-Carlo tree search
 *        algorithm - hence the "MCTS".
 *
 * @see Player
 *
 * @author - Marin Nguyen (288260)
 */
public class MctsPlayer implements Player {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private static final int DEFAULT_EXPLORATION_PARAMETER = 40;
    private final int iterations;
    private final PlayerId ownId;
//    private final long rngSeed;
    private SplittableRandom rng;


    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    //TODO: why no JDoc there ?
    public MctsPlayer(PlayerId ownId, long rngSeed, int iterations) {
        checkArgument(iterations >= 9);
        this.iterations = iterations;
        this.ownId = ownId;
//        this.rngSeed = rngSeed;
        this.rng = new SplittableRandom(rngSeed);
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    @SuppressWarnings("Duplicates")
    @Override
    /**
     * @brief The card "this" [the Player] should play, in order to maximize its points.
     *
     * @param state (TurnState) state - the current TurnState.
     * @param hand (CardSet) - the Player's hand
     *
     * @return (Card) the best card to play
     *                [according to the Monte-Carlo tree search algorithm]
     */
    public Card cardToPlay(TurnState state, CardSet hand) {
        //default, the root teamId is this player's and its father is null.
        Node root;

        //The trick corresponding to the TurnState of a Node should NEVER be full -using our implementation
        if (state.trick().isFull()) {
            assert (! state.trick().isLast()); //We should never call cardToPlay when the last Trick of the turn is full
            root = new Node(state.withTrickCollected(), playableCards(state, hand), hand,  null, ownId.team());
        }
        else
            root = new Node(state, playableCards(state, hand), hand, null, ownId.team());

        iterate(root);

        return root.playableCardsFromTurnState.get(root.selectSon(0));
    }

    /**
     * @brief Quite straightforward: will call the method "expand" on the root
     *        the number of iterations specified in this class's constructor and
     *        update the scores of each node of the tree
     *
     * @param root (Node) the root of the tree.
     */
    private void iterate(Node root) {
        for (int i = 0; i < iterations; ++i) {
            Node newNode = expand(root);
            Score toAdd = (newNode.isLeaf()) ?
                    newNode.state.withTrickCollected().score(): simulateToEndOfTurn(newNode.state, newNode.hand);
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
     * @return (Node) - either the (Node) which has been added to the tree [if there was one]
     *                - or the last (Node) reached during the descent [if no Node was added]
     */
    private Node expand(Node root) {
        Node node = root;
        int index = root.selectSon();
        while (!(node.isLeaf() || node.directChildrenOfNode[index] == null)) {
            //The 1st condition is there cuz we dont wanna call selectSon() with a "true leaf" (last trick of the game)
            node = node.directChildrenOfNode[index];
            index = node.selectSon();
        }

        if (node.isLeaf()) {
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
     * @param father (Node) - [not full] the (Node) whose son we wish to create.
     * @return (Node) - a son of the (Node) father.
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
     * @param node (Node) - the node from which we want to start adding the Score.
     * @param score (Score) - the score to propagate back.
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
     * @brief Simulates randomly a turn until the end, given the specified (TurnState) "state"
     *        and (CardSet) 'hand" - the player's hand.
     *        Then returns the obtained Score.
     *
     * @param state (TurnState) - the current TurnState.
     * @param hand (CardSet) - the player's hand.
     * @return (Score) - the Score at the end of the simulation.
     */
    private Score simulateToEndOfTurn(TurnState state, CardSet hand) {
        assert (! state.unplayedCards().equals(CardSet.EMPTY));
        assert (! state.trick().isFull());

        TurnState copyState = state;
        CardSet copyHand = hand;
//        SplittableRandom rng = new SplittableRandom(rngSeed);
        while(! copyState.isTerminal()) {
            CardSet playableCards = playableCards(copyState, copyHand);
            Card randomCardToPlay = playableCards.get(rng.nextInt(playableCards.size()));

            copyHand = copyHand.remove(randomCardToPlay);
            copyState = copyState.withNewCardPlayedAndTrickCollected(randomCardToPlay);
        }
        return copyState.score();
    }

    /**
     * @brief Indicates the Cards the next Player to play
     *          - can play, if it is "this"
     *          - could play if it is any of the other 3. [actually there are some
     *          card that could be played that won't be taken into account, but
     *          it has be decided to just ignore this case].
     *
     * @param state (TurnState) - the current TurnState.
     * @param hand (CardSet) - The player's hand.
     * @return (CardSet) - The Cards the next Player may play.
     */
    private CardSet playableCards(TurnState state, CardSet hand) {
        assert (! state.unplayedCards().equals(CardSet.EMPTY));
        assert (! state.trick().isFull());

        if (state.nextPlayer() == ownId) {
            assert (! hand.equals(CardSet.EMPTY));
            return state.trick().playableCards(hand);
        }

//        return state.unplayedCards().difference(hand);
        return state.trick().playableCards(state.unplayedCards().difference(hand));
    }


    /**
     * @brief The Nodes will be used to construct the tree used by the Monte-Carlo
     *        algorithm.
     *
     *        - Each Node has knowledge of its father <em>"father"</em> and its children <em>"directChildrenOfNode"</em>.
     *        - Each Node contains an attribute <em>"totalPointsFromNode"</em> which represents
     *          the sum of the points its team -given by <em>"teamId"</em>- obtained after
     *          simulating games from either this node or one of his children.
     *        - It also has an attribute <em>"randomTurnsPlayed"</em> indicating
     *          how many turns were played starting from this Node or one of its children.
     *        - The last remarkable attribute is <em>"playableCardsFromTurnState"</em>
     *          which correspond to the cards the method "playableCards"
     */
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
         * @brief Called by another Node -"father"- to create one of his sons.
         *
         * @param state (TurnState) - see constructor
         * @param playableCards (CardSet) - see constructor
         * @param hand (CardSet) - see constructor
         * @param teamId (TeamId) - see constructor
         * @return (Node) - see constructor
         */
        private Node of(TurnState state, CardSet playableCards, CardSet hand, TeamId teamId) {
            return new Node(state, playableCards, hand, this, teamId);
        }

        /**
         * @brief Called on a (Node) "node" explored at least once.
         *        Indicates this node value, according to the evaluation function
         *        of our Monte Carlo algorithm, and given the specified "explorationParameter"
         *
         * @param node (Node) - The (Node) to evaluate
         * @param explorationParameter (int) - quantifies the likeliness of our algorithm to wander horizontally,
         *                             i.e. to explore rarely explored branches.
         * @return (double) - the value of the (Node) "node"
         */
        private double evaluate(Node node, int explorationParameter) {
            return (float)node.totalPointsFromNode / node.randomTurnsPlayed +
                    explorationParameter * (float)Math.sqrt(2 * Math.log(randomTurnsPlayed) / node.randomTurnsPlayed);
        }

        /**
         * @see #selectSon(int)
         */
        private int selectSon() {
            return selectSon(DEFAULT_EXPLORATION_PARAMETER);
        }

        /**
         * @brief Never called with a [true] leaf.
         *        Indicates which son, of the (Node) it is called by, should be explored.
         *
         * @param explorationParameter (int) - quantifies the likeliness of our algorithm to wander horizontally,
         *                             i.e. to explore rarely explored branches.
         * @return (int) - the index of a son for which "evaluate" is maximal.
         *                 [If a (Node) hasn't been explored yet, its value is considered infinite, and therefore maximal]
         */
        private int selectSon(int explorationParameter) {
            if (directChildrenOfNode.length == 0) {
                return -1;
            }

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

        /**
         * @brief Indicates whether the (Node) "this" is a [true] leaf in the tree.
         *        [i.e. it corresponds to the last Card of the last Trick of the turn]
         *
         * @return (boolean) true if "this" is a leaf.
         */
        private boolean isLeaf() {
            return directChildrenOfNode.length == 0;
        }
    }
}
