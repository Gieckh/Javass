package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import src.cs108.MeldSet;

/**@brief This interface is implemented by the players, whether they are simulated
 *        or real.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public interface Player {
    
    
    /**
     * @brief Returns the Card the [underlying] Player wants to play, given its hand
     *        and the actual TurnState.
     *
     * @param state (TurnState) the state of the game.
     * @param hand (CardSet) the hand of Player.
     *
     * @return (Card) the Card the player wants to play.
     */
    abstract public Card cardToPlay(TurnState state, CardSet hand);
    
    /**
     * @brief Called at the beginning of the game [at its creation].
     *        Indicates to the player who he is, and the names of all the players
     *        in the game (himself included).
     *
     * 
     * @param ownId (PlayerId) this Player's Id.
     * @param playerNames (Map of [PlayerId, Player])
     */
    default public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        //default is empty
    }

    /**
     * @Brief
     *
     * @param hand
     * @return
    */
    default public MeldSet announcement(CardSet hand){
        return MeldSet.EMPTY_SET;
    }
    
    /**
     * @Brief Called during a TurnState, will provoke some specific 
     * cheating events in function of the int case.
     *
     * @return  an int which will cause a specific cheating event 
    */
    default public int cheat() {
        return 0;
    }

    /**
     * @brief Called when the hand of the Player is updated [distribution, or when he plays].
     *        Informs him of his new hand.
     *
     * @param newHand (CardSet) the new hand of the Player.
     */
    default public void updateHand(CardSet newHand) {
        //default is empty
    }
    
    default public void updateAnnouncement(List<MeldSet> m) {
        //default is empty
    }

    /**
     * @brief Called when the trump changes [or at the beginning of the game].
     *        Indicates the new trump to the Player.
     * 
     * @param trump (Color) the new trump
     */
    default public void setTrump(Card.Color trump) {
        //default is empty
    }

    /**
     * @brief Called when the trick changes [a player has played, or a new trick's been created]
     *        Indicates the new trick to the Player.
     * 
     * @param newTrick (Trick) The new trick.
     */
    default public void updateTrick(Trick newTrick) {

    }
    

    /**
     * @brief Called when the [tota] Score changes [at the beginning of the game, or when a
     *        trick is collected].
     *        Indicates the new Score (an object containing the scores of TEAM_1 and TEAM_2).
     * 
     * @param score (Score) the new Score.
     */
    default public void updateScore(Score score) {
        //default is empty
    }

    /**
     * @brief Called when a team has won the game.
     *        Indicates the winning team.
     * 
     * @param winningTeam (TeamId) the team who has won.
     */
    default public void setWinningTeam(TeamId winningTeam) {
        //default is empty
    }
}
