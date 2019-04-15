package ch.epfl.javass.gui;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.javass.jass.PlayerId;import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GraphicalPlayer {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    PlayerId thisId;
    Map<PlayerId, String> playerNames;
    ScoreBean score;
    TrickBean trick; 
        
        
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    public GraphicalPlayer(PlayerId thisId , Map<PlayerId, String> playerNames, ScoreBean score, TrickBean trick) {
        this.thisId = thisId; 
        this.playerNames = playerNames; 
        this.score = score; 
        this.trick =trick;
            
    }
    
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    

    private void createScorePane() {
        Label namesOfTeam1 = new Label(playerNames.get(PlayerId.PLAYER_1).toString()
            +" et "+
            playerNames.get(PlayerId.PLAYER_3).toString());
        
        Label namesOfTeam2 = new Label(playerNames.get(PlayerId.PLAYER_2).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_4).toString());
        //j'ai fais n'importe nawak au niveau des points, le trick c'est pas ce luila
        Label totalString = new Label("/Total : ");
        
        Label gamePointsOfTeam1 = new Label(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_1)).toString());
        
        Label turnPointOfTeam1 = new Label(Bindings.convert(score.turnPointsProperty(TeamId.TEAM_1)).toString());
        
        Label totalPointOfTeam1 = new Label(Bindings.convert(score.totalPointsProperty(TeamId.TEAM_1)).toString());
        
        Label gamePointsOfTeam2 = new Label(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_2)).toString());
        
        Label turnPointOfTeam2 = new Label(Bindings.convert(score.turnPointsProperty(TeamId.TEAM_2)).toString());
        
        Label totalPointOfTeam2 = new Label(Bindings.convert(score.totalPointsProperty(TeamId.TEAM_2)).toString());

        GridPane grid = new GridPane();
        
        grid.addRow(0,namesOfTeam1 , turnPointOfTeam1 , gamePointsOfTeam1,totalString , totalPointOfTeam1);

        
        grid.addRow(1,namesOfTeam2 , turnPointOfTeam2 , gamePointsOfTeam2,totalString , totalPointOfTeam2);

        grid.setStyle("-fx-font: 16 Optima;\n" + 
                "-fx-background-color: lightgray;\n" + 
                "-fx-padding: 5px;\n" + 
                "-fx-alignment: center;");
    }
    private void createTrickPane() {
        
    }
    private void createVictoryPanes() {
        
        BorderPane border = new BorderPane();
        String namesOfWinningTeam = score.winningTeamProperty().get().equals(TeamId.TEAM_1)? 
                playerNames.get(PlayerId.PLAYER_1).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_3).toString() : 
                playerNames.get(PlayerId.PLAYER_2).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_4).toString();   
                
                TeamId winningTeam = score.winningTeamProperty().get();
                
        String scoreOfwinnigTeam = Bindings.convert(score.totalPointsProperty(winningTeam)).toString();
        
        String scoreOfloosingTeam = Bindings.convert(score.totalPointsProperty(winningTeam.other())).toString();

        Text finalString= new Text(namesOfWinningTeam +" ont gagné avec " + scoreOfwinnigTeam + " points contre " + scoreOfloosingTeam + ".");
        BorderPane.setAlignment(finalString, Pos.CENTER);
        
    }
}
