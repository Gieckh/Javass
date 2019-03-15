package ch.epfl.javass.jass;

import java.util.Map;

/**@Brief represents a player.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public interface Player {
    
    
    /**
     * @Brief returns the card the player wants to play, knowing the current turnState state
     * and all the cards he currently has.
     * 
     * @param the TurnState state
     * @param the CardSet hand
     * @return the card the player wants to play, knowing the current turnState state
     * and all the cards he currently has.
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    abstract public Card cardToPlay(TurnState state, CardSet hand);

    
    
    /**
     * @Brief informs the player his Id and all player names.
     * 
     * @param ownId
     * @param playerNames
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    default public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        //default is empty
    }


    /**
     * @Brief informs the player about updating his hand .
     *
     * @param newHand
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    default public void updateHand(CardSet newHand) {
        //default is empty
    }

    /**
     * @Brief informs the player with setting the trumpColor.
     * 
     * @param trump
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    default public void setTrump(Card.Color trump) {
        //default is empty
    }

    /**
     * @Brief informs the player about the updated trick.
     * 
     * @param newTrick
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    default public void updateTrick(Trick newTrick) {
        //default is empty
    }

    /**
     * @Brief informs the player about the updated score.
     * 
     * @param score
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    default public void updateScore(Score score) {
        //default is empty
    }

    /**
     * @Brief informs the player about which team won.
     * 
     * @param winningTeam
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    default public void setWinningTeam(TeamId winningTeam) {
        //default is empty
    }
}
