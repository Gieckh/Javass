/**
 * 
 */
package ch.epfl.javass.jass;

import java.util.Arrays;
import java.util.SplittableRandom;

import ch.epfl.javass.Preconditions;

/**
 * @author Thomas Bienaimé (296485)
 * @author Lucas Nkok (297111)
 */
public final class MctsPlayerThomabenmato implements Player {
    private final PlayerId ownId;
    private final long rngSeed;
    private final int iterations;
    private static final int MCTS_BIAS = 40;
    private static final int NULL_BIAS = 0;

    /**
     * Construit un MctsPlayer avec les paramètres donnés qui construit un
     * joueur simulé avec l'identité, la graine aléatoire et le nombre
     * d'itérations donnés, ou lève IllegalArgumentException si le nombre
     * d'itérations est inférieur à 9.
     * 
     * 
     * 
     * @param ownId
     *            Identité du joueur
     * @param rngSeed
     *            graine de l'algorithme monte Carlo
     * @param iterations
     *            nombre d'itérations
     */
    public MctsPlayerThomabenmato(PlayerId ownId, long rngSeed, int iterations) {
        this.ownId = ownId;
        this.rngSeed = rngSeed;
        Preconditions.checkArgument(iterations >= 9);
        this.iterations = iterations;

    }

    /**
     * @author Thomas Bienaimé (296485)
     * @author Lucas Nkok (297111)
     * 
     *         Cette classe a pour but de gérer la logique de la séléction de
     *         carte du joueur simulé, pour ce faire, les noeuds disposent de
     *         quatre méthodes qui sont appelées successivement dès la créatio
     *         du noeud puisqu'elles sont nécessaires à l'algorithme. Un appel à
     *         expand() suffit à appeler toutes les méthodes internes à la
     *         classe et donc à compléter une itération de l'algorithme.
     * 
     *         la classe dispose de deux constructeurs, l'un pour construire la
     *         racine
     */
    private static class CardNode {

        private final SplittableRandom rng;
        private final PlayerId ownId;
        private final TeamId team;
        private final TurnState nodeTurn;
        private final CardNode[] children;
        private final CardNode parent;
        private long unexploredCards;
        private long cardsLeftToPlay;
        private int totalPointsWon;
        private int numberOfTurnsFinished;

        /**
         * Construit un CardNode qui sera une racine de l'arbre et n'a donc pas
         * de parent avec les paramètres donnés
         * 
         * @param nodeTurn
         * @param hand
         * @param ownId
         * @param rngSeed
         */
        private CardNode(TurnState nodeTurn, CardSet hand, PlayerId ownId,
                long rngSeed) {
            this.ownId = ownId;
            this.team = ownId.team().other();
            this.nodeTurn = nodeTurn;
            this.unexploredCards = getNextPlayableCards(nodeTurn,
                    hand.packed());
            this.children = new CardNode[PackedCardSet.size(unexploredCards)];
            parent = null;
            this.cardsLeftToPlay = hand.packed();
            this.rng = new SplittableRandom(rngSeed);

        }

        /**
         * Construit un CardNode qui sera l'un des enfant du noeud parent donné
         * en argument
         * 
         * @param parent
         */
        private CardNode(CardNode parent) {

            this.parent = parent;
            rng = parent.rng;
            int lastCardPlayed = PackedCardSet.get(parent.unexploredCards, 0);

            this.ownId = parent.ownId;
            this.cardsLeftToPlay = PackedCardSet.remove(parent.cardsLeftToPlay,
                    lastCardPlayed);
            this.team = parent.nodeTurn.nextPlayer().team();
            this.nodeTurn = parent.nodeTurn.withNewCardPlayedAndTrickCollected(
                    Card.ofPacked(lastCardPlayed));

            this.unexploredCards = getNextPlayableCards(nodeTurn,
                    cardsLeftToPlay);
            this.children = new CardNode[PackedCardSet.size(unexploredCards)];

            simulateScore();

        }

        private long getNextPlayableCards(TurnState turn, long hand) {
            if (turn.nextPlayer() == ownId) {
                return PackedTrick.playableCards(turn.packedTrick(), hand);
            } else
                return PackedTrick.playableCards(turn.packedTrick(),
                        PackedCardSet.difference(turn.packedUnplayedCards(),
                                hand));
        }

        private CardNode bestChild(int bias) {
            CardNode bestEvaluatedNode = children[0];
            float bestNodeEvaluation = bestEvaluatedNode.evaluate(bias);
            for (CardNode node : children) {
                float nodeEvaluation = node.evaluate(bias);
                if (nodeEvaluation > bestNodeEvaluation) {
                    bestEvaluatedNode = node;
                    bestNodeEvaluation = nodeEvaluation;
                }

            }
            return bestEvaluatedNode;
        }

        private float evaluate(int c) {
            // expand gère le cas où N(n) == 0

            return (float) (totalPointsWon / (double) numberOfTurnsFinished
                    + c * Math.sqrt(2 * Math.log(parent.numberOfTurnsFinished)
                            / numberOfTurnsFinished));
        }

        private void expand() {
            if (children.length == 0)
                return;
            if (children[children.length - 1] == null) {
                for (int i = 0; i < children.length; ++i) {
                    if (children[i] == null) {
                        // lorsque l'on trouve le noeud vide, on crée le noeud
                        // qui doit être créé et on supprime la carte que
                        // représente le noeud des cartes non explorées du
                        // parent de ce dernier
                        children[i] = new CardNode(this);
                        unexploredCards = PackedCardSet.remove(unexploredCards,
                                PackedCardSet.get(unexploredCards, 0));
                        break;
                    }
                }
            } else {

                bestChild(MCTS_BIAS).expand();
            }
        }

        private void simulateScore() {

            TurnState simulatedTurn = nodeTurn;
            long hand = cardsLeftToPlay;
            // on simule jusqu'à la fin du tour en prenant compte de nos cartes
            while (!simulatedTurn.isTerminal()) {
                long playableCards = getNextPlayableCards(simulatedTurn, hand);
                int cardPlayed = PackedCardSet.get(playableCards,
                        rng.nextInt(PackedCardSet.size(playableCards)));
                hand = PackedCardSet.remove(hand, cardPlayed);
                simulatedTurn = simulatedTurn
                        .withNewCardPlayedAndTrickCollected(
                                Card.ofPacked(cardPlayed));
            }
            // on met à jour récursivement le haut de l'arbre
            backPropagate(
                    PackedScore.turnPoints(simulatedTurn.packedScore(),
                            TeamId.TEAM_1),
                    PackedScore.turnPoints(simulatedTurn.packedScore(),
                            TeamId.TEAM_2));

        }

        /**
         * Propage les points obtenue par la partie simuler d'un node à ses
         * parents
         * 
         * @param pointsTeam1
         *            les points gagné par l'equipe 1
         * @param pointsTeam2
         *            les points gagné par l'equipe 2
         */
        private void backPropagate(int pointsTeam1, int pointsTeam2) {

            ++numberOfTurnsFinished;

            totalPointsWon += team == TeamId.TEAM_2 ? pointsTeam1 : pointsTeam2;
            if (parent == null) {
                return;
            }
            parent.backPropagate(pointsTeam1, pointsTeam2);
        }

        /**
         * retourne la carte associée au meilleur noeud de la racine sur
         * laquelle la fonction est appelée
         * 
         */
        private Card evaluateBest() {
            return Card.ofPacked(PackedCardSet.get(cardsLeftToPlay,
                    Arrays.asList(children).indexOf(bestChild(NULL_BIAS))));

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.epfl.javass.jass.Player#cardToPlay(ch.epfl.javass.jass.TurnState,
     * ch.epfl.javass.jass.CardSet)
     */
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        if (state.trick().playableCards(hand).size() == 1)
            return state.trick().playableCards(hand).get(0);
        final CardNode root = new CardNode(state, hand, ownId, rngSeed);
        for (int i = 0; i < iterations; ++i) {
            root.expand();
        }

        return root.evaluateBest();
    }
}