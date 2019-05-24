package ch.epfl.javass.gui;

import ch.epfl.javass.jass.*;
import static javafx.application.Platform.runLater;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @brief This class extends Player and is an Adapter of a graphicalPlayer.
 *        As said in the patron Adapter, the graphicalPlayerAdapter shares his scoreBean, trickBean, and handBean
 *        with the GraphicalPlayer. Most methods are about updating the corresponding Bean on the JavaFx thread (thanks to runLater).
 *        The SetPlayer's particularity is that it is the method creating the graphicalPlayer instance.
 *        CardToPlay returns the single card contained in "queueOfCommunication", which is an array shared with the graphicalPlayer.
 *        
 *
 * @see Player
 * @see GraphicalPlayer
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class GraphicalPlayerAdapter implements Player {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private final ScoreBean scoreBean;
    private final TrickBean trickBean;
    private final HandBean handBean;
    private GraphicalPlayer graphicalPlayer;
    static final ArrayBlockingQueue<Card> queueOfCommunication = new ArrayBlockingQueue<>(1);
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    /**
     * @brief the only constructor of this class. It takes no parameters
     *        and in particular no underlying player for the obvious reason that
     *        the graphicalPlayerAdapter will play what a human wants.
     */
    public GraphicalPlayerAdapter() {
        this.handBean = new HandBean();
        this.scoreBean = new ScoreBean();
        this.trickBean = new TrickBean();
    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @see ch.epfl.javass.jass.Player#cardToPlay(ch.epfl.javass.jass.TurnState, ch.epfl.javass.jass.CardSet)
     */
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        try {
            handBean.setPlayableCards(state.trick().playableCards(hand));
            Card card = queueOfCommunication.take();
            handBean.setPlayableCards(CardSet.EMPTY);
            return card;
            
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    /**
     * @see ch.epfl.javass.jass.Player#setPlayers(ch.epfl.javass.jass.PlayerId, java.util.Map)
     */
    @Override
    public void
    setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        this.graphicalPlayer = new GraphicalPlayer(ownId, playerNames, this.scoreBean, this.trickBean, this.handBean);
        runLater( () -> graphicalPlayer.createStage(ownId, playerNames).show() );
    }

    /**
     * @see ch.epfl.javass.jass.Player#updateHand(ch.epfl.javass.jass.CardSet)
     */
    @Override
    public void updateHand(CardSet newHand) {
        runLater( () -> handBean.setHand(newHand) );
    }

    /**
     * @see ch.epfl.javass.jass.Player#setTrump(ch.epfl.javass.jass.Card.Color)
     */
    @Override
    public void setTrump(Card.Color trump) {
        runLater( () -> trickBean.setTrump(trump) );
    }

    /**
     * @see ch.epfl.javass.jass.Player#updateTrick(ch.epfl.javass.jass.Trick)
     */
    @Override
    public void updateTrick(Trick newTrick) {
        runLater( ()-> trickBean.setTrick(newTrick) );
    }

    /**
     * @see ch.epfl.javass.jass.Player#updateScore(ch.epfl.javass.jass.Score)
     */
    @Override
    public void updateScore(Score score) {
        runLater( () -> {
            for (TeamId teamId: TeamId.ALL) {
                scoreBean.setGamePoints (teamId, score.gamePoints (teamId));
                scoreBean.setTotalPoints(teamId, score.totalPoints(teamId));
                scoreBean.setTurnPoints (teamId, score.turnPoints (teamId));
            }
        });
    }

    /**
     * @see ch.epfl.javass.jass.Player#setWinningTeam(ch.epfl.javass.jass.TeamId)
     */
    @Override
    public void setWinningTeam(TeamId winningTeam) {
        runLater(()-> scoreBean.setWinningTeam(winningTeam));
    }
}
