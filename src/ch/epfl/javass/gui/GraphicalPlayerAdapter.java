package ch.epfl.javass.gui;

import ch.epfl.javass.jass.*;

import java.util.Map;

public class GraphicalPlayerAdapter implements Player {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

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
