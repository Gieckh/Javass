package ch.epfl.javass.gui;

import ch.epfl.javass.jass.*;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class GraphicalPlayerAdapter implements Player {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    ScoreBean scoreBean;
    TrickBean trickBean;
    HandBean handBean;
    GraphicalPlayer graphicalInterface;
    ArrayBlockingQueue<Card> queueOfCommunication = new ArrayBlockingQueue<>(1);
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    public GraphicalPlayerAdapter(ScoreBean scoreBean, TrickBean trickBean, HandBean handBean, GraphicalPlayer graphicalInterface ) {
        this.graphicalInterface = graphicalInterface;
        this.handBean = handBean;
        this.scoreBean = scoreBean;
        this.trickBean = trickBean;
        
    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/


    @Override public Card cardToPlay(TurnState state, CardSet hand) {
        return null;
    }

    @Override public void setPlayers(PlayerId ownId,
            Map<PlayerId, String> playerNames) {

    }

    @Override public void updateHand(CardSet newHand) {

    }

    @Override public void setTrump(Card.Color trump) {

    }

    @Override public void updateTrick(Trick newTrick) {

    }

    @Override public void updateScore(Score score) {

    }

    @Override public void setWinningTeam(TeamId winningTeam) {

    }
}
