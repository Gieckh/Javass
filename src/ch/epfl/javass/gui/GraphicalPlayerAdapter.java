package ch.epfl.javass.gui;

import ch.epfl.javass.jass.*;
import static javafx.application.Platform.runLater;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class GraphicalPlayerAdapter implements Player {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    ScoreBean scoreBean;
    TrickBean trickBean;
    HandBean handBean;
    GraphicalPlayer graphicalPlayer;
    ArrayBlockingQueue<Card> queueOfCommunication ;
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    public GraphicalPlayerAdapter() {
        this.handBean = new HandBean();
        this.scoreBean = new ScoreBean();
        this.trickBean = new TrickBean();
        this.queueOfCommunication =  new ArrayBlockingQueue<>(1);
        
    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

// remplacer par take?
    @Override public Card cardToPlay(TurnState state, CardSet hand) {
        return queueOfCommunication.poll();
        
    }

    @Override public void setPlayers(PlayerId ownId,
            Map<PlayerId, String> playerNames) {
        this.graphicalPlayer = new GraphicalPlayer(ownId, playerNames, this.scoreBean, this.trickBean, this.handBean);
        runLater(() -> { graphicalPlayer.createStage().show(); });
    
    }

    @Override public void updateHand(CardSet newHand) {
        runLater(() -> {handBean.setHand(newHand);});
    }

    @Override public void setTrump(Card.Color trump) {
        runLater(()-> {trickBean.setTrump(trump);});
    }

    @Override public void updateTrick(Trick newTrick) {
        runLater(()-> {trickBean.setTrick(newTrick);});
    }

    @Override public void updateScore(Score score) {
        runLater(()-> {for(TeamId teamId :TeamId.ALL ) {
            scoreBean.setGamePoints(teamId, score.gamePoints(teamId));
            scoreBean.setTotalPoints(teamId, score.totalPoints(teamId));
            scoreBean.setTurnPoints(teamId, score.turnPoints(teamId));
            }
        });
    }

    @Override public void setWinningTeam(TeamId winningTeam) {
        runLater(()-> {scoreBean.setWinningTeam(winningTeam);});
    }
}
