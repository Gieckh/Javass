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

    @Override public Card cardToPlay(TurnState state, CardSet hand) {
        //TODO
        return null;
    }

    private Score simulateToEndOfTurn(TurnState turnState, CardSet hand) {
        if (turnState.isTerminal()) { //TODO: check if that's necessary
            return turnState.score();
        }

        TurnState copyOfTurnState = TurnState.ofPackedComponents
                (turnState.packedScore(), turnState.packedUnplayedCards(), turnState.packedTrick());

        if (turnState.trick().isFull()) {
            copyOfTurnState = copyOfTurnState.withTrickCollected();
        }

        CardSet copyOfHand = CardSet.ofPacked(hand.packed());
        SplittableRandom rng = new SplittableRandom(rngSeed);
        while (! copyOfTurnState.isTerminal()) {
            CardSet playableCards = playableCards(copyOfTurnState, ownId, copyOfHand);
            Card randomCardToPlay = playableCards.get(rng.nextInt(playableCards.size()));
            copyOfHand = copyOfHand.remove(randomCardToPlay);

            copyOfTurnState = copyOfTurnState.withNewCardPlayedAndTrickCollected(randomCardToPlay);
        }

        return copyOfTurnState.score();
    }

    //Assuming trick not full
    private CardSet playableCards(TurnState turnState, PlayerId playerId, CardSet hand) {
        if (turnState.nextPlayer() == playerId) {
            return turnState.trick().playableCards(hand);
        }

        return turnState.unplayedCards().difference(hand);
    }

    private static class Node {
        /** ============================================== **/
        /** ==============    ATTRIBUTES    ============== **/
        /** ============================================== **/
        private TurnState turnState;
        private Node[] directChildrenOfNode;
        //private CardSet unplayedDirectChildrenCards;
        private CardSet hand;
        private CardSet playableCards;
        private int totalPointsFromNode;
        private int randomTurnsPlayed;
        private Node parent;

        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/
        private Node(TurnState turnState, CardSet playableCards, CardSet hand, Node parent) {
            this.turnState = turnState;
            this.hand = hand;
            this.playableCards = turnState.trick().playableCards(hand);
            this.directChildrenOfNode = new Node[playableCards.size()];
            this.totalPointsFromNode = 0;
            this.randomTurnsPlayed = 0;
            this.parent = parent;
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
                float V =(float)tmpNode.totalPointsFromNode / tmpNode.randomTurnsPlayed +
                         explorationParameter * (float)Math.sqrt(2 * Math.log(randomTurnsPlayed) / tmpNode.randomTurnsPlayed);
                if (V > priority) {
                    priority = V;
                    index = i;
                }
            }

            return index;
        }
    }
}
