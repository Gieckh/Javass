package ch.epfl.javass.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sun.javafx.collections.UnmodifiableObservableMap;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import ch.epfl.javass.jass.PackedCard;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import static javafx.beans.binding.Bindings.valueAt;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableMap;
import static javafx.collections.FXCollections.observableMap;
import static javafx.collections.FXCollections.unmodifiableObservableMap;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

public class GraphicalPlayer {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    private PlayerId thisId;
    private Map<PlayerId, String> playerNames;
    private ScoreBean score;
    private TrickBean trick; 
    private static ObservableMap<Card,Image> cardImpage240 = GraphicalPlayer.cardImage240();
    private static Map<Card,Image> cardImpage160 = GraphicalPlayer.cardImage160();
    private static ObservableMap<Color,Image> trumpImage = GraphicalPlayer.trumpImage();
    public BorderPane mainPane;


    
        
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    public GraphicalPlayer(PlayerId thisId , Map<PlayerId, String> playerNames, ScoreBean score, TrickBean trick) {
        this.thisId = thisId; 
        this.playerNames = playerNames; 
        this.score = score; 
        this.trick =trick;
        GridPane scorePane = createScorePane();
        GridPane trickPane = createTrickPane();
        
        BorderPane victoryPaneForTeam1 = createVictoryPanes(TeamId.TEAM_1);
        BorderPane victoryPaneForTeam2 = createVictoryPanes(TeamId.TEAM_2);
        this.mainPane = new BorderPane(trickPane, scorePane , new GridPane(),new GridPane(), new GridPane());
//        StackPane s = new StackPane(mainPane , victoryPaneForTeam1,victoryPaneForTeam2);
//        
//        StackPane PaneInCaseTeam1Win = new StackPane(victoryPaneForTeam1);
//        StackPane PaneInCaseTeam2Win = new StackPane(victoryPaneForTeam2);
      //  mainPane.addEventHandler(score.winningTeamProperty().get().equals(TeamId.TEAM_1), mainPane.PaneInCaseTeam1Win);
        
    }
    
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    public Stage createStage() {
        Stage stage = new Stage();
        stage.setTitle("Javass - "+playerNames.get(thisId).toString() );
        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        return stage;
    }
    
    private static ObservableMap<Card,Image> cardImage160() {
        ObservableMap<Card,Image> map =  observableMap( new HashMap<>());
        for(int i = 0 ; i<Color.ALL.size(); ++i) {
            for(int j = 0 ; j < Rank.ALL.size(); ++j) {
                String s = "/"+"card_"+i+"_"+j+"_"+160+".png";
                map.put(Card.of(Color.ALL.get(i),Rank.ALL.get(j)), new Image(s));
            }
        }
        return  unmodifiableObservableMap(map);
    }
    
    private static ObservableMap<Card,Image> cardImage240() {
        ObservableMap<Card,Image> map = observableMap(new HashMap<>()); 
        for(int i = 0 ; i<Color.ALL.size(); ++i) {
            for(int j = 0 ; j < Rank.ALL.size(); ++j) {
                String s = "/"+"card_"+i+"_"+j+"_"+240+".png";
                map.put(Card.of(Color.ALL.get(i),Rank.ALL.get(j)), new Image(s));
            }
        }
        return  unmodifiableObservableMap(map);
    }
    
    private static ObservableMap<Color,Image> trumpImage() {
        ObservableMap<Color,Image> map = observableMap(new HashMap<>());
        for(int i = 0 ; i<Color.ALL.size(); ++i) {
            String s = "/"+"trump_"+i+".png";
            map.put(Color.ALL.get(i),new Image(s));
        }
        
        return  unmodifiableObservableMap(map);
    }

    private GridPane createScorePane() {
        Text namesOfTeam1 = new Text(playerNames.get(PlayerId.PLAYER_1).toString()
            +" et "+
            playerNames.get(PlayerId.PLAYER_3).toString());
        
        Text namesOfTeam2 = new Text(playerNames.get(PlayerId.PLAYER_2).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_4).toString());
        Text totalString = new Text("/Total : ");
        Text totalStringBis = new Text("/Total : ");
        
        ReadOnlyIntegerProperty turnPointsT1 = score.turnPointsProperty(TeamId.TEAM_1);
        ReadOnlyIntegerProperty turnPointsT2 = score.turnPointsProperty(TeamId.TEAM_2);
        
        SimpleStringProperty trickOf1 = new SimpleStringProperty();
        score.turnPointsProperty(TeamId.TEAM_1).addListener((object, old, New ) ->  trickOf1.set( " ( + "+ Math.max(0, New.intValue()-old.intValue())+" ) " ));
        
        Text gamePointsOfTeam1 = new Text();
        gamePointsOfTeam1.textProperty().bind(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_1)));
        
        Text turnPointsOfTeam1 = new Text();
        turnPointsOfTeam1.textProperty().bind(Bindings.convert(turnPointsT1));

        
        Text trickPointsOfTeam1 = new Text();        
        
        trickPointsOfTeam1.textProperty().bind(  trickOf1);
        
        Text gamePointsOfTeam2 = new Text(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_1)).get());
        
        Text turnPointsOfTeam2 = new Text(Bindings.convert(score.turnPointsProperty(TeamId.TEAM_2)).get());
        
        Text trickPointsOfTeam2 = new Text( " ( + " +" ) " );

        GridPane grid = new GridPane();
        
        grid.addRow(0,namesOfTeam1 , trickPointsOfTeam1 , turnPointsOfTeam1,totalString , gamePointsOfTeam1);

        
        grid.addRow(1,namesOfTeam2 , trickPointsOfTeam2 , turnPointsOfTeam2,totalStringBis , gamePointsOfTeam2);

        grid.setStyle("-fx-font: 16 Optima;\n" + 
                    "-fx-background-color: lightgray;\n" + 
                    "-fx-padding: 5px;\n" + 
                    "-fx-alignment: center;"); 
        
        return grid;
    }
    private GridPane createTrickPane() {
        
        StackPane stackpane = new StackPane(new Rectangle(120, 180));
        stackpane.setStyle("-fx-arc-width: 20;\n" + 
                "-fx-arc-height: 20;\n" + 
                "-fx-fill: transparent;\n" + 
                "-fx-stroke: lightpink;\n" + 
                "-fx-stroke-width: 5;\n" + 
                "-fx-opacity: 0.5;");
        stackpane.setEffect(new GaussianBlur(4));
        
        PlayerId playerIdForCouple1 = PlayerId.ALL.get((thisId.ordinal()+2)%4);
        PlayerId playerIdForCouple2 = PlayerId.ALL.get((thisId.ordinal()+3)%4);
        PlayerId playerIdForCouple3 = thisId;
        PlayerId playerIdForCouple4 = PlayerId.ALL.get((thisId.ordinal()+1)%4);
        
        Text textForCouple1 = new Text();
        Text textForCouple2 = new Text();
        Text textForCouple3 = new Text();
        Text textForCouple4 = new Text();
        textForCouple1.textProperty().set(playerNames.get(playerIdForCouple1));
        textForCouple2.textProperty().set(playerNames.get(playerIdForCouple2));
        textForCouple3.textProperty().set(playerNames.get(playerIdForCouple3));
        textForCouple4.textProperty().set(playerNames.get(playerIdForCouple4));

        
        textForCouple1.setStyle("-fx-font: 14 Optima;");      
        textForCouple2.setStyle("-fx-font: 14 Optima;");      
        textForCouple3.setStyle("-fx-font: 14 Optima;");      
        textForCouple4.setStyle("-fx-font: 14 Optima;");   
        
        ImageView imageForCouple1 = new ImageView();
        ImageView imageForCouple2 = new ImageView();
        ImageView imageForCouple3 = new ImageView();
        ImageView imageForCouple4 = new ImageView();
        
        imageForCouple1.imageProperty().bind(valueAt(GraphicalPlayer.cardImpage240,valueAt(trick.trick(), playerIdForCouple1)));
        imageForCouple2.imageProperty().bind(valueAt(GraphicalPlayer.cardImpage240,valueAt(trick.trick(), playerIdForCouple2)));
        imageForCouple3.imageProperty().bind(valueAt(GraphicalPlayer.cardImpage240,valueAt(trick.trick(), playerIdForCouple3)));
        imageForCouple4.imageProperty().bind(valueAt(GraphicalPlayer.cardImpage240,valueAt(trick.trick(), playerIdForCouple4)));
        
        imageForCouple1.setFitHeight(180);
        imageForCouple1.setFitWidth(120);
        imageForCouple2.setFitHeight(180);
        imageForCouple2.setFitWidth(120);
        imageForCouple3.setFitHeight(180);
        imageForCouple3.setFitWidth(120);
        imageForCouple4.setFitHeight(180);
        imageForCouple4.setFitWidth(120);
        
        VBox couple1 = new VBox(textForCouple1,imageForCouple1);
        VBox couple2 = new VBox(textForCouple2,imageForCouple2);
        VBox couple3 = new VBox(textForCouple3,imageForCouple3);
        VBox couple4 = new VBox(textForCouple4,imageForCouple4);
        
        ImageView ImageTrump = new ImageView();
        ImageTrump.imageProperty().bind(valueAt(GraphicalPlayer.trumpImage,trick.trumpProperty()));
        ImageTrump.setFitHeight(101);
        ImageTrump.setFitWidth(101);
        GridPane grid = new GridPane();
        grid.add(couple2, 0, 1, 1, 3);
        grid.add(couple4, 2, 1, 1, 3);
        grid.addColumn(1, couple1,ImageTrump, couple3);
//        grid.add(couple1, 1,0,1,1);
//        grid.add(ImageTrump, 1,1,1,1);
//        grid.add(couple3, 1,2,1,1);
        grid.setStyle("-fx-background-color: whitesmoke;\n" + 
                "-fx-padding: 5px;\n" + 
                "-fx-border-width: 3px 0px;\n" + 
                "-fx-border-style: solid;\n" + 
                "-fx-border-color: gray;\n" + 
                "-fx-alignment: center;");
        
        return grid;
    }
    private BorderPane createVictoryPanes(TeamId winningTeam) {
        
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
        
        return border;
    }
}
