package ch.epfl.javass.gui;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.javass.jass.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.GridPane;

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
        Label namesOfTeam1 = new Label(playerNames.get(PlayerId.PLAYER_1).toString();
            +"+"+
            playerNames.get(PlayerId.PLAYER_3).toString());

        
        GridPane PaniniPouletPané = new GridPane();
        
    }
    private void createTrickPane() {
        
    }
    private void createVictoryPanes() {
        
    }
}
