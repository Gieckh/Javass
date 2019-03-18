package ch.epfl.javass.jass;

import java.util.Map;

/**@Brief represents a player.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public interface Player {



    abstract public Card cardToPlay(TurnState state, CardSet hand);

    
    
    /**
     * @Brief informs the player his Id and all player names.
     * 
     * @param ownId
     * @param playerNames
     *
     */
    default public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        //default is empty
    }


    /**
     * @brief informs the player about updating his hand.
     *
     * @param newHand
     *
     */
    default public void updateHand(CardSet newHand) {
        //default is empty
    }

    /**
     * @Brief informs the player with setting the trumpColor.
     * 
     * @param trump
     *
     */
    default public void setTrump(Card.Color trump) {
        //default is empty
    }

    /**
     * @Brief informs the player about the updated trick.
     * 
     * @param newTrick
     *
     */
    default public void updateTrick(Trick newTrick) {
        //default is empty
    }

    /**
     * @Brief informs the player about the updated score.
     * 
     * @param score
     *
     */
    default public void updateScore(Score score) {
        //default is empty
    }

    /**
     * @Brief informs the player about which team won.
     * 
     * @param winningTeam
     *
     */
    default public void setWinningTeam(TeamId winningTeam) {
        //default is empty
    }
}
