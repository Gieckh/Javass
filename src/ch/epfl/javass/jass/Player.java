package ch.epfl.javass.jass;

import java.util.Map;

public interface Player {
    
    abstract public Card cardToPlay(TurnState state, CardSet hand);

    default public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        //default is empty
    }

    default public void updateHand(CardSet newHand) {
        //default is empty
    }

    default public void setTrump(Card.Color trump) {
        //default is empty
    }

    default public void updateTrick(Trick newTrick) {
        //default is empty
    }

    default public void updateScore(Score score) {
        //default is empty
    }

    default public void setWinningTeam(TeamId winningTeam) {
        //default is empty
    }
}
