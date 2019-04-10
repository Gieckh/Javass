package ch.epfl.javass.gui;

import com.sun.xml.internal.fastinfoset.sax.Properties;

import ch.epfl.javass.jass.TeamId;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * ScoreBean is a bean containing the properties of the scores.
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
/**
 * ScoreBean
 *
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
    private ObjectProperty<TeamId> winningTeam = new SimpleObjectProperty<TeamId>();
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    //default constructor per default is enough
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @Brief It is a public getter for the turnPoints property of the chosen team.
     *
     * @param team the TeamId of a team
     * @return the turnPoints property of the chosen team
    */
    public ReadOnlyIntegerProperty turnPointsProperty(TeamId team) {
        if(team.equals(TeamId.TEAM_1)) {
            return turnPointsT1;
        }
        else {
            return turnPointsT2;
        }
    }
    
    /**
     * @Brief It is a public getter for the gamePoints property of the chosen team.
     *
     * @param team the TeamId of a team
     * @return the gamePoints property of the chosen team
    */
    public ReadOnlyIntegerProperty gamePointsProperty(TeamId team) {
        if(team.equals(TeamId.TEAM_1)) {
            return gamePointsT1;
        }
        else {
            return gamePointsT2;
        }
    }
    
    /**
     * @Brief It is a public getter for the totalPoints property of the chosen team.
     *
     * @param team the TeamId of a team
     * @return the totalPoints property of the chosen team
    */
    public ReadOnlyIntegerProperty totalPointsProperty(TeamId team) {
        if(team.equals(TeamId.TEAM_1)) {
            return totalPointsT1;
        }
        else {
            return totalPointsT2;
        }
    }
    
    
    /**
     * @Brief It is a public getter for the winningTeam property.
     *
     * @return the winningTeam property
    */
    public ReadOnlyObjectProperty<TeamId> winningTeamProperty() {
        return winningTeam;
    }
    
    
    /**
     * @Brief public setter for the turnPoints given the new turnPoints(int) 
     * and the chosen team.
     *
     * @param team the TeamId of the chosen team
     * @param newTurnPoints an integer
    */
    public void setTurnPoints(TeamId team, int newTurnPoints) {
        if(team.equals(TeamId.TEAM_1)) {
            turnPointsT1 = new SimpleIntegerProperty(newTurnPoints);
        }else {
            turnPointsT2 = new SimpleIntegerProperty(newTurnPoints);
            
        }
      }
    
    /**
     * @Brief public setter for the gamePoints given the new gamePoints(int) 
     * and the chosen team.
     *
     * @param team the TeamId of the chosen team
     * @param newGamePoints an integer
    */
    public void setGamePoints(TeamId team, int newGamePoints) {
        if(team.equals(TeamId.TEAM_1)) {
            gamePointsT1 = new SimpleIntegerProperty(newGamePoints);
        }else {
            gamePointsT2 = new SimpleIntegerProperty(newGamePoints);
            
        }
      }
    
    /**
     * @Brief public setter for the totalPoints given the new totalPoints(int) 
     * and the chosen team.
     *
     * @param team the TeamId of the chosen team
     * @param newTotalPoints an integer
    */
    public void setTotalPoints(TeamId team, int newTotalPoints) {
        if(team.equals(TeamId.TEAM_1)) {
            totalPointsT1 = new SimpleIntegerProperty(newTotalPoints);
        }else {
            totalPointsT2 = new SimpleIntegerProperty(newTotalPoints);
            
        }
      }
    
    
    
    /**
     * @Brief public setter for the chosen winning team.
     *
     * @param team the TeamId of the winning team
    */
    public void setWinningTeam(TeamId team) {
            winningTeam = new SimpleObjectProperty<TeamId>(team);
      }
    
    
}
