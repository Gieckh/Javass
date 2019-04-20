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
    private static Map<Card,Image> cardImpage240 = GraphicalPlayer.cardImage240();
    private static Map<Card,Image> cardImpage160 = GraphicalPlayer.cardImage160();
    private static Map<Color,Image> trumpImage = GraphicalPlayer.trumpImage();
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
        GridPane empty = new GridPane();
        this.mainPane = new BorderPane(trickPane, empty, scorePane, empty, empty);
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
            String s = "/"+"trump_"+i+".png";
            map.put(Color.ALL.get(i),new Image(s));
        }
        
        //unsure transtypage askip
        return  Collections.unmodifiableMap(new HashMap(map));
    }

    private GridPane createScorePane() {
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
        
        Text textForCouple1 = new Text(playerNames.get(playerIdForCouple1.toString()));
        Text textForCouple2 = new Text(playerNames.get(playerIdForCouple2.toString()));
        Text textForCouple3 = new Text(playerNames.get(playerIdForCouple3.toString()));        
        Text textForCouple4 = new Text(playerNames.get(playerIdForCouple4.toString()));
        
        textForCouple1.setStyle("-fx-font: 14 Optima;");      
        textForCouple2.setStyle("-fx-font: 14 Optima;");      
        textForCouple3.setStyle("-fx-font: 14 Optima;");      
        textForCouple4.setStyle("-fx-font: 14 Optima;");   
        // j'ai pas compris ce passage la : e choix de l'image à afficher peut se faire au moyen 
        //d'une simple liaison de la propriété imageProperty du nœud ImageView à la propriété trick
        //du bean du pli, moyennant une utilisation judicieuse de la variante 1 et de la variante 2
        //de la méthode valueAt de Bindings.
        ImageView ImageForCouple1 = new ImageView(GraphicalPlayer.cardImpage240.get(trick.trick().get(playerIdForCouple1)));
        ImageView ImageForCouple2 = new ImageView(GraphicalPlayer.cardImpage240.get(trick.trick().get(playerIdForCouple2)));
        ImageView ImageForCouple3 = new ImageView(GraphicalPlayer.cardImpage240.get(trick.trick().get(playerIdForCouple3)));
        ImageView ImageForCouple4 = new ImageView(GraphicalPlayer.cardImpage240.get(trick.trick().get(playerIdForCouple4)));
        
        ImageForCouple1.setFitHeight(180);
        ImageForCouple1.setFitWidth(120);
        ImageForCouple2.setFitHeight(180);
        ImageForCouple2.setFitWidth(120);
        ImageForCouple3.setFitHeight(180);
        ImageForCouple3.setFitWidth(120);
        ImageForCouple4.setFitHeight(180);
        ImageForCouple4.setFitWidth(120);
        
        VBox couple1 = new VBox(textForCouple1,ImageForCouple1);
        VBox couple2 = new VBox(textForCouple1,ImageForCouple2);
        VBox couple3 = new VBox(textForCouple1,ImageForCouple3);
        VBox couple4 = new VBox(textForCouple1,ImageForCouple4);
        
        @SuppressWarnings("unlikely-arg-type")
        ImageView ImageTrump = new ImageView(GraphicalPlayer.trumpImage.get(trick.trumpProperty()));
        ImageTrump.setFitHeight(101);
        ImageTrump.setFitWidth(101);
        
        GridPane grid = new GridPane();
        grid.add(couple4, 0, 1, 1, 3);
        grid.add(couple2, 2, 1, 1, 3);
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
