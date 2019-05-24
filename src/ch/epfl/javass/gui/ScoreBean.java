package ch.epfl.javass.gui;

import ch.epfl.javass.jass.TeamId;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * ScoreBean is a JavaFx bean containing the properties of the scores.
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */

public final class ScoreBean {
    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    private IntegerProperty turnPointsT1 = new SimpleIntegerProperty();
    private IntegerProperty gamePointsT1 = new SimpleIntegerProperty();
    private IntegerProperty totalPointsT1 = new SimpleIntegerProperty();

    private IntegerProperty turnPointsT2 = new SimpleIntegerProperty();
    private IntegerProperty gamePointsT2 = new SimpleIntegerProperty();
    private IntegerProperty totalPointsT2 = new SimpleIntegerProperty();

    private ObjectProperty<TeamId> winningTeam = new SimpleObjectProperty<>();
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    //default constructor per default is enough askip
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @brief It is a public getter for the turnPoints property of the chosen team.
     *
     * @param team (TeamId) - the team we want the information from
     * @return (ReadOnlyIntegerProperty) - the turnPoints property of the chosen team
     */
    public ReadOnlyIntegerProperty turnPointsProperty(TeamId team) {
        return (team == TeamId.TEAM_1) ? turnPointsT1: turnPointsT2;
    }
    
    /**
     * @brief It is a public getter for the gamePoints property of the chosen team.
     *
     * @param team (TeamId) - the team we want the information from
     * @return (ReadOnlyIntegerProperty) - the gamePoints property of the chosen team
     */
    public ReadOnlyIntegerProperty gamePointsProperty(TeamId team) {
        return (team == TeamId.TEAM_1) ? gamePointsT1: gamePointsT2;
    }
    
    /**
     * @Brief It is a public getter for the totalPoints property of the chosen team.
     *
     * @param team (TeamId) - the team we want the information from
     * @return the totalPoints property of the chosen team
     */
    public ReadOnlyIntegerProperty totalPointsProperty(TeamId team) {
        return (team == TeamId.TEAM_1) ? totalPointsT1: totalPointsT2;
    }
    

    /**
     * @brief It is a public getter for the winningTeam property.
     *
     * @return the winningTeam property
     */
    public ReadOnlyObjectProperty<TeamId> winningTeamProperty() {
        return winningTeam;
    }
    
    
    /**
     * @brief public setter for the turnPoints given the new turnPoints(integer)
     * and the chosen team.
     *
     * @param team (TeamId) - the TeamId of the chosen team
     * @param newTurnPoints (int) - the value we set the turnPoints of "team" at.
     */
    public void setTurnPoints(TeamId team, int newTurnPoints) {
        if (team == TeamId.TEAM_1)
            turnPointsT1.set(newTurnPoints);
        else
            turnPointsT2.set(newTurnPoints);
    }
    
    /**
     * @brief public setter for the gamePoints given the new gamePoints(integer)
     * and the chosen team.
     *
     * @param team (TeamId) - the TeamId of the chosen team
     * @param newGamePoints (int) - the value we set the game points of "team" at.
     */
    public void setGamePoints(TeamId team, int newGamePoints) {
        if (team == TeamId.TEAM_1)
            gamePointsT1.set(newGamePoints);
        else
            gamePointsT2.set(newGamePoints);
    }
    
    /**
     * @brief public setter for the totalPoints given the new totalPoints(integer)
     * and the chosen team.
     *
     * @param team (TeamId) - the TeamId of the chosen team
     * @param newTotalPoints (int) - the value we set the total points of "team" at.
     */
    public void setTotalPoints(TeamId team, int newTotalPoints) {
        if (team == TeamId.TEAM_1)
            totalPointsT1.set(newTotalPoints);
        else
            totalPointsT2.set(newTotalPoints);
    }


    /**
     * @brief public setter for the chosen winning team.
     *
     * @param team (TeamId) - the TeamId of the winning team
     */
    public void setWinningTeam(TeamId team) {
            winningTeam.set(team);
    }
}
