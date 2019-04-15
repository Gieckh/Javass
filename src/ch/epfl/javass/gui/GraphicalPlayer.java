package ch.epfl.javass.gui;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.javass.jass.PlayerId;import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
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
        Label totalString = new Label("/Total : ");
        
        Label gamePointsOfTeam1 = new Label(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_1)).toString());
        
        Label turnPointsOfTeam1 = new Label(Bindings.convert(score.turnPointsProperty(TeamId.TEAM_1)).toString());
        
        Label trickPointsOfTeam1 = new Label( " ( + " + Bindings.convert(score.totalPointsProperty(TeamId.TEAM_1)).toString() + " ) " );
        
        Label gamePointsOfTeam2 = new Label(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_2)).toString());
        
        Label turnPointsOfTeam2 = new Label(Bindings.convert(score.turnPointsProperty(TeamId.TEAM_2)).toString());
        
        Label trickPointsOfTeam2 = new Label( " ( + " + Bindings.convert(score.totalPointsProperty(TeamId.TEAM_2)).toString() + " ) " );

        GridPane grid = new GridPane();
        
        grid.addRow(0,namesOfTeam1 , trickPointsOfTeam1 , turnPointsOfTeam1,totalString , gamePointsOfTeam1);

        
        grid.addRow(1,namesOfTeam2 , trickPointsOfTeam2 , turnPointsOfTeam2,totalString , gamePointsOfTeam2);

        grid.setStyle("-fx-font: 16 Optima;\n" + 
                    "-fx-background-color: lightgray;\n" + 
                    "-fx-padding: 5px;\n" + 
                    "-fx-alignment: center;");
    }
    private void createTrickPane() {
        
    }
    private void createVictoryPanes(TeamId winningTeam) {
        
        BorderPane border = new BorderPane();
        String namesOfWinningTeam = winningTeam.equals(TeamId.TEAM_1)? 
                playerNames.get(PlayerId.PLAYER_1).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_3).toString() : 
                playerNames.get(PlayerId.PLAYER_2).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_4).toString();   
                
              // pas certain   
        StringExpression upDatingString = Bindings.format("-fx-font: 16 Optima;\n",
                namesOfWinningTeam,
                " ont gagné avec ",
                score.totalPointsProperty(winningTeam),
                " points contre ",
                score.totalPointsProperty(winningTeam.other()),
                ".");
                
        
        Text finalString= new Text(upDatingString.get());
        BorderPane.setAlignment(finalString, Pos.CENTER);
        
        border.setStyle("-fx-font: 16 Optima;\n" + 
                "-fx-background-color: white;");
    }
}
