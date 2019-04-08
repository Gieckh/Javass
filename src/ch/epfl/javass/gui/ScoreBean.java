package ch.epfl.javass.gui;

import java.beans.Beans;

import ch.epfl.javass.jass.TeamId;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

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

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    public ReadOnlyIntegerProperty turnPointsProperty(TeamId team) {
        if(team.equals(TeamId.TEAM_1)) {
            return turnPointsT1;
        }
        else {
            return turnPointsT2;
        }
    }
    public ReadOnlyIntegerProperty gamePointsProperty(TeamId team) {
        if(team.equals(TeamId.TEAM_1)) {
            return gamePointsT1;
        }
        else {
            return gamePointsT2;
        }
    }
    
    public ReadOnlyIntegerProperty totalPointsProperty(TeamId team) {
        if(team.equals(TeamId.TEAM_1)) {
            return totalPointsT1;
        }
        else {
            return totalPointsT2;
        }
    }
    
    public ReadOnlyObjectProperty<TeamId> winningTeamProperty() {
        return winningTeam;
    }
    
    
    public void setTurnPoints(TeamId team, int newTurnPoints) {
        if(team.equals(TeamId.TEAM_1)) {
            turnPointsT1 = new SimpleIntegerProperty(newTurnPoints);
        }else {
            turnPointsT2 = new SimpleIntegerProperty(newTurnPoints);
            
        }
      }
    
    public void setGamePoints(TeamId team, int newGamePoints) {
        if(team.equals(TeamId.TEAM_1)) {
            gamePointsT1 = new SimpleIntegerProperty(newGamePoints);
        }else {
            gamePointsT2 = new SimpleIntegerProperty(newGamePoints);
            
        }
      }
    
    public void setTotalPoints(TeamId team, int newTotalPoints) {
        if(team.equals(TeamId.TEAM_1)) {
            totalPointsT1 = new SimpleIntegerProperty(newTotalPoints);
        }else {
            totalPointsT2 = new SimpleIntegerProperty(newTotalPoints);
            
        }
      }
    
    
    public void setWinningTeam(TeamId team) {
            winningTeam = new SimpleObjectProperty<TeamId>(team);
      }
    
    
}
