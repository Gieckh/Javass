package ch.epfl.javass.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sun.javafx.collections.UnmodifiableObservableMap;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import ch.epfl.javass.jass.PackedCard;
import ch.epfl.javass.jass.PlayerId;import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GraphicalPlayer {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    PlayerId thisId;
    Map<PlayerId, String> playerNames;
    ScoreBean score;
    TrickBean trick; 
    private static Map<Card,Image> cardImpage240 = GraphicalPlayer.cardImage240();
    private static Map<Card,Image> cardImpage160 = GraphicalPlayer.cardImage160();
    private static Map<Color,Image> trumpImage = GraphicalPlayer.trumpImage();


    
        
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    public GraphicalPlayer(PlayerId thisId , Map<PlayerId, String> playerNames, ScoreBean score, TrickBean trick) {
        this.thisId = thisId; 
        this.playerNames = playerNames; 
        this.score = score; 
        this.trick =trick;
        createScorePane();
        createTrickPane();
        createVictoryPanes(TeamId.TEAM_1);
        createVictoryPanes(TeamId.TEAM_2);
    }
    
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    @SuppressWarnings("unchecked")
    private static Map<Card,Image> cardImage160() {
        Map<Card,Image> map = new HashMap<>();
        for(int i = 0 ; i<Color.ALL.size(); ++i) {
            for(int j = 0 ; j < Rank.ALL.size(); ++j) {
                String s = "/"+"card_"+i+"_"+j+"_"+160+".png";
                map.put(Card.of(Color.ALL.get(i),Rank.ALL.get(j)), new Image(s));
            }
        }
        //unsure transtypage askip
        return  Collections.unmodifiableMap(new HashMap(map));
    }
    
    @SuppressWarnings("unchecked")
    private static Map<Card,Image> cardImage240() {
        Map<Card,Image> map = new HashMap<>();
        for(int i = 0 ; i<Color.ALL.size(); ++i) {
            for(int j = 0 ; j < Rank.ALL.size(); ++j) {
                String s = "/"+"card_"+i+"_"+j+"_"+240+".png";
                map.put(Card.of(Color.ALL.get(i),Rank.ALL.get(j)), new Image(s));
            }
        }
        //unsure transtypage askip
        return  Collections.unmodifiableMap(new HashMap(map));
    }
    
    @SuppressWarnings("unchecked")
    private static Map<Color,Image> trumpImage() {
        Map<Color,Image> map = new HashMap<>();
        for(int i = 0 ; i<Color.ALL.size(); ++i) {
            String s = "/"+"card_"+i+"_"+".png";
            map.put(Color.ALL.get(i),new Image(s));
        }
        
        //unsure transtypage askip
        return  Collections.unmodifiableMap(new HashMap(map));
    }

    private void createScorePane() {
        Label namesOfTeam1 = new Label(playerNames.get(PlayerId.PLAYER_1).toString()
            +" et "+
            playerNames.get(PlayerId.PLAYER_3).toString());
        
        Label namesOfTeam2 = new Label(playerNames.get(PlayerId.PLAYER_2).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_4).toString());
        Label totalString = new Label("/Total : ");
        
        ReadOnlyIntegerProperty turnPointsT1 = score.turnPointsProperty(TeamId.TEAM_1);
        ReadOnlyIntegerProperty turnPointsT2 = score.turnPointsProperty(TeamId.TEAM_2);
        IntegerProperty TrickPoints1= new SimpleIntegerProperty(0);
        IntegerProperty temp1 = new SimpleIntegerProperty(0);
        IntegerProperty TrickPoints2=new SimpleIntegerProperty(0);
        IntegerProperty temp2 = new SimpleIntegerProperty(0);
        if(temp1.get()!=turnPointsT1.get()) {
            TrickPoints1 = new SimpleIntegerProperty(Math.max(0, turnPointsT1.get()-temp1.get()));
            temp1 = new SimpleIntegerProperty(turnPointsT1.get());
        }
        if(temp2.get()!=turnPointsT2.get()) {
            TrickPoints2 = new SimpleIntegerProperty(Math.max(0, turnPointsT2.get()-temp2.get()));
            temp2 = new SimpleIntegerProperty(turnPointsT2.get());
        }

        
        Label gamePointsOfTeam1 = new Label(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_1)).get());
        
        Label turnPointsOfTeam1 = new Label(Bindings.convert(score.turnPointsProperty(TeamId.TEAM_1)).get());
        
        Label trickPointsOfTeam1 = new Label( " ( + " + Bindings.convert(TrickPoints1).get() + " ) " );
        
        Label gamePointsOfTeam2 = new Label(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_2)).get());
        
        Label turnPointsOfTeam2 = new Label(Bindings.convert(score.turnPointsProperty(TeamId.TEAM_2)).get());
        
        Label trickPointsOfTeam2 = new Label( " ( + " + Bindings.convert(TrickPoints2).get() + " ) " );

        GridPane grid = new GridPane();
        
        grid.addRow(0,namesOfTeam1 , trickPointsOfTeam1 , turnPointsOfTeam1,totalString , gamePointsOfTeam1);

        
        grid.addRow(1,namesOfTeam2 , trickPointsOfTeam2 , turnPointsOfTeam2,totalString , gamePointsOfTeam2);

        grid.setStyle("-fx-font: 16 Optima;\n" + 
                    "-fx-background-color: lightgray;\n" + 
                    "-fx-padding: 5px;\n" + 
                    "-fx-alignment: center;");        
    }
    private void createTrickPane() {
        StackPane stackpane = new StackPane(new Rectangle(120, 180));
        stackpane.setStyle("-fx-arc-width: 20;\n" + 
                "-fx-arc-height: 20;\n" + 
                "-fx-fill: transparent;\n" + 
                "-fx-stroke: lightpink;\n" + 
                "-fx-stroke-width: 5;\n" + 
                "-fx-opacity: 0.5;");
        stackpane.setEffect(new GaussianBlur(4));
        
        Text textForCouple1 = 
        textForCouple1.setStyle("-fx-font: 14 Optima;");      
        ImageView ImageForCouple1 =
        VBox couple1 = 
        GridPane grid = new GridPane();
        
        
        grid.setStyle("-fx-background-color: whitesmoke;\n" + 
                "-fx-padding: 5px;\n" + 
                "-fx-border-width: 3px 0px;\n" + 
                "-fx-border-style: solid;\n" + 
                "-fx-border-color: gray;\n" + 
                "-fx-alignment: center;");
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
