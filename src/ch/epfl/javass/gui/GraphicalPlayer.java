package ch.epfl.javass.gui;

import static javafx.beans.binding.Bindings.createBooleanBinding;
import static javafx.beans.binding.Bindings.valueAt;
import static javafx.beans.binding.Bindings.when;
import static javafx.collections.FXCollections.observableMap;
import static javafx.collections.FXCollections.unmodifiableObservableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import src.cs108.MeldSet;

/**
 * GraphicalPlayer is class creating a JavaFx Stage which is the graphical interface of a complete
 * jass virtual game. It is being adapted by graphicalPlayerAdapter to represent a player. 
 * 
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public class GraphicalPlayer {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    private PlayerId thisId;
    private Map<PlayerId, String> playerNames;
    private ScoreBean score;
    private TrickBean trick; 
    private HandBean handBean;
    private static ObservableMap<Card,Image> cardImpage240 = GraphicalPlayer.cardImage240();
    private static ObservableMap<Card,Image> cardImpage160 = GraphicalPlayer.cardImage160();
    private static ObservableMap<Color,Image> trumpImage = GraphicalPlayer.trumpImage();
    private ArrayBlockingQueue<Card> queueOfCommunication;
    private BorderPane victoryPaneForTeam[] = new BorderPane[2];
    private StackPane finalPane;
    private ArrayBlockingQueue<Integer> cheatingQueue;


    
        
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /**
     * @Brief a lot of properties are shared between the graphicalPlayerAdapter and the graphicalPlayer thanks to this constructor. 
     * The graphical Stage is created in this constructor.
     * 
     * @param thisId
     * @param playerNames
     * @param score
     * @param trick
     * @param handBean
     * @param queueOfCommunication
     */
    public GraphicalPlayer(PlayerId thisId , Map<PlayerId, String> playerNames,ScoreBean score, 
            TrickBean trick, HandBean handBean, ArrayBlockingQueue<Card> queueOfCommunication,
            ArrayBlockingQueue<Integer> cheatingQueue ,
            ObjectProperty<ListView<Text>> listOfAnnounces ) {
        this.thisId = thisId; 
        this.playerNames = playerNames; 
        this.score = score; 
        this.trick =trick;
        this.handBean = handBean;
        this.queueOfCommunication = queueOfCommunication;
        this.cheatingQueue = cheatingQueue;
        GridPane scorePane = createScorePane();
        GridPane trickPane = createTrickPane();
        HBox handPane = createHandPane();
        for(TeamId t : TeamId.ALL) {
             this.victoryPaneForTeam[t.ordinal()] = createVictoryPanes(t);
             BooleanBinding shouldDisplay =  createBooleanBinding( () ->t.equals(score.winningTeamProperty().get()),score.winningTeamProperty() );
             this.victoryPaneForTeam[t.ordinal()].visibleProperty().bind(shouldDisplay);
        }
        
        GridPane announcesPane = new GridPane();
        listOfAnnounces.addListener( (object , old , New ) -> { 
            announcesPane.getChildren().clear();
            announcesPane.getChildren().add(New);
            New.minHeightProperty().bind(trickPane.heightProperty());
        }  );
        BooleanBinding b =  createBooleanBinding( () -> {   
        return handBean.annouces().size()!=0;
        },handBean.annouces());
        

        BorderPane main= new BorderPane(trickPane, scorePane , announcesPane,handPane, new HBox());
        main.rightProperty().bind(when(b).then(announcesPane).otherwise(new GridPane()));
        this.finalPane = new StackPane(main, victoryPaneForTeam[0] , victoryPaneForTeam[1] );
        cheatingManager();

    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    private void cheatingManager() {
        finalPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode().isDigitKey() && cheatingQueue.isEmpty()) {
                    try {
                        int code = Integer.parseInt(keyEvent.getCode().toString().substring(
                                keyEvent.getCode().toString().length()-1));
                        if(code<10)
                            cheatingQueue.put(code);

                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                 keyEvent.consume();
                
               }
            }
        });
    }
    
    
    /**
     * @Brief create the javaFx stage of a graphic Jass game.
     *
     * @return the stage corresponding to a complete graphic Jass game.
    */
    public Stage createStage() {
        Stage stage = new Stage();
        stage.setTitle("Javass - "+playerNames.get(thisId).toString() );
        Scene finalScene = new Scene(finalPane);
        finalScene.getRoot().requestFocus();
        stage.setScene(finalScene);
        
        return stage;
    }
    
   
    
    /**
     * @Brief generates an observable map from Card to Image of width 160
     *
    */
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
    
    /**
     * @Brief generates an observable map from Card to Image of width 240
     *
    */
    private static ObservableMap<Card,Image> cardImage240() {
        ObservableMap<Card,Image> map = observableMap(new HashMap<>()); 
        for(int i = 0 ; i<Color.ALL.size(); ++i) {
            for(int j = 0 ; j < Rank.ALL.size(); ++j) {
                String s = "/card_" + i + "_" + j + "_" + 240 + ".png";
                map.put(Card.of(Color.ALL.get(i),Rank.ALL.get(j)), new Image(s));
            }
        }
        return  unmodifiableObservableMap(map);
    }
    
    /**
     * @brief generates an observable map from trump to Image
     *
     */
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
            playerNames.get(PlayerId.PLAYER_3) + " : ");
        Text namesOfTeam2 = new Text(playerNames.get(PlayerId.PLAYER_2).toString()
            +" et "+
            playerNames.get(PlayerId.PLAYER_4) + " : ");
        
        Text totalString = new Text("/Total : ");
        Text totalStringBis = new Text("/Total : ");
        
        ReadOnlyIntegerProperty turnPointsT1 = score.turnPointsProperty(TeamId.TEAM_1);
        ReadOnlyIntegerProperty turnPointsT2 = score.turnPointsProperty(TeamId.TEAM_2);
        
        SimpleStringProperty trickOf1 = new SimpleStringProperty();
        score.turnPointsProperty(TeamId.TEAM_1).addListener((object, old, New ) ->  trickOf1.set( " ( + "+ Math.max(0, New.intValue()-old.intValue())+" ) " ));
        SimpleStringProperty trickOf2 = new SimpleStringProperty();
        score.turnPointsProperty(TeamId.TEAM_2).addListener((object, old, New ) ->  trickOf2.set( " ( + "+ Math.max(0, New.intValue()-old.intValue())+" ) " ));
        
        Text gamePointsOfTeam1 = new Text();
        gamePointsOfTeam1.textProperty().bind(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_1)));
        Text turnPointsOfTeam1 = new Text();
        turnPointsOfTeam1.textProperty().bind(Bindings.convert(turnPointsT1));
        Text trickPointsOfTeam1 = new Text();        
        trickPointsOfTeam1.textProperty().bind(trickOf1);
        
        Text gamePointsOfTeam2 = new Text();
        gamePointsOfTeam2.textProperty().bind(Bindings.convert(score.gamePointsProperty(TeamId.TEAM_2)));
        Text turnPointsOfTeam2 = new Text();
        turnPointsOfTeam2.textProperty().bind(Bindings.convert(turnPointsT2));
        Text trickPointsOfTeam2 = new Text();        
        trickPointsOfTeam2.textProperty().bind(trickOf2);
        
        GridPane grid = new GridPane();
        
        grid.addRow(0,namesOfTeam1  , turnPointsOfTeam1 , trickPointsOfTeam1,totalString , gamePointsOfTeam1);
        grid.addRow(1,namesOfTeam2 , turnPointsOfTeam2  , trickPointsOfTeam2 ,totalStringBis , gamePointsOfTeam2);
        grid.setStyle("-fx-font: 16 Optima;\n" + 
                    "-fx-background-color: lightgray;\n" + 
                    "-fx-padding: 5px;\n" + 
                    "-fx-alignment: center;"); 
        return grid;
    }
    

    private GridPane createTrickPane() {
        BooleanBinding b =  createBooleanBinding( () -> {   
            return handBean.hand().stream().noneMatch(c -> c == null);
            },handBean.hand());
      VBox couple[] = new VBox[4];
      for(int i = 0 ; i < 4 ; ++i) {
            PlayerId playerIdForCouple = PlayerId.ALL.get((thisId.ordinal()+2+i)%4);
            Text textForCouple = new Text();
            textForCouple.textProperty().set(playerNames.get(playerIdForCouple));
            textForCouple.setStyle("-fx-font: 14 Optima;"); 
            Text announcementForCouple = new Text();
            SimpleStringProperty str = new SimpleStringProperty();
            str.bind(handBean.announcesPerPlayerToString().get(playerIdForCouple.ordinal()));
            announcementForCouple.textProperty().bind(str);
            announcementForCouple.setStyle("-fx-font: 16 Optima;");
            announcementForCouple.visibleProperty().bind(b);
            ImageView imageForCouple = new ImageView();
            imageForCouple.imageProperty().bind(valueAt(GraphicalPlayer.cardImpage240,valueAt(trick.trick(), playerIdForCouple)));
            imageForCouple.setFitHeight(180);
            imageForCouple.setFitWidth(120);
            Rectangle rectangle = new Rectangle(120, 180);
            rectangle.setStyle("-fx-arc-width: 20;\n" + 
                    "-fx-arc-height: 20;\n" + 
                    "-fx-fill: transparent;\n" + 
                    "-fx-stroke: lightpink;\n" + 
                    "-fx-stroke-width: 5;\n" + 
                    "-fx-opacity: 0.5;");
            BooleanBinding shouldDisplay =  createBooleanBinding( () ->playerIdForCouple.equals(trick.winningPlayerProperty().get()),
                    trick.winningPlayerProperty() );
            rectangle.visibleProperty().bind(shouldDisplay);
            rectangle.setEffect(new GaussianBlur(4));
            StackPane imageWithHalo  = new StackPane(imageForCouple, rectangle);
            couple[i] = new VBox(textForCouple,imageWithHalo, announcementForCouple
                    );
            couple[i].setAlignment(Pos.TOP_CENTER);

      }

        ImageView ImageTrump = new ImageView();
        ImageTrump.imageProperty().bind(valueAt(GraphicalPlayer.trumpImage,trick.trumpProperty()));
        ImageTrump.setFitHeight(101);   
        ImageTrump.setFitWidth(101);
        VBox trump = new VBox(ImageTrump);
        trump.setAlignment(Pos.CENTER);
        
        
        GridPane grid = new GridPane();
        grid.add(couple[1], 0, 1, 1, 1);
        grid.add(couple[3], 2, 1, 1, 1);
        grid.add(couple[0], 1,0,1,1);
        grid.add(trump, 1,1,1,1);
        grid.add(couple[2], 1,2,1,1);
        grid.setStyle("-fx-background-color: whitesmoke;\n" + 
                "-fx-padding: 5px;\n" + 
                "-fx-border-width: 3px 0px;\n" + 
                "-fx-border-style: solid;\n" + 
                "-fx-border-color: gray;\n" + 
                "-fx-alignment: center;");
        
        return grid;
    }
    
    
    private BorderPane createVictoryPanes(TeamId winningTeam) {
        
        String namesOfWinningTeam = winningTeam.equals(TeamId.TEAM_1)? 
                playerNames.get(PlayerId.PLAYER_1).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_3).toString() : 
                playerNames.get(PlayerId.PLAYER_2).toString()
                +" et "+
                playerNames.get(PlayerId.PLAYER_4).toString();   
                
        StringExpression upDatingString = Bindings.createStringBinding(() -> namesOfWinningTeam+
                " ont gagné avec "+
                score.totalPointsProperty(winningTeam).get()+
                " points contre "+
                score.totalPointsProperty(winningTeam.other()).get()+
                ".", score.totalPointsProperty(winningTeam),score.totalPointsProperty(winningTeam.other())); 
                       
        Text finalString= new Text();
        finalString.textProperty().bind(Bindings.convert(upDatingString));
        finalString.setStyle("-fx-font: 16 Optima;\n");
        BorderPane border = new BorderPane(finalString);
        border.centerProperty().set(finalString);        
        border.setStyle("-fx-font: 16 Optima;\n" + 
                "-fx-background-color: white;");
        
        return border;
    }
    
    
    
    private HBox createHandPane() {
        
        ImageView nineChildrens[] = new ImageView[9];
        for(int i = 0 ; i < 9 ; ++i) {
            ImageView children = new ImageView();
            ObjectBinding<Card> correspondingCard = valueAt( handBean.hand(), i);
            children.imageProperty().bind(valueAt(cardImpage160, correspondingCard));
            BooleanBinding isPlayable =  createBooleanBinding( () -> handBean.playableCards().contains(correspondingCard.get()),handBean.playableCards(),handBean.hand());
            children.opacityProperty().bind(when(isPlayable).then(1.0).otherwise(0.2) );
            children.disableProperty().bind(isPlayable.not());
            children.setOnMouseClicked((e)-> {
                try {
                    queueOfCommunication.put(correspondingCard.get());
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });
            
            children.setFitHeight(120);
            children.setFitWidth(80);
            nineChildrens[i] = children;
        }
        HBox handPane = new HBox(nineChildrens);
        handPane.setStyle("-fx-background-color: lightgray;\n" + 
                "-fx-spacing: 5px;\n" + 
                "-fx-padding: 5px;");
        return handPane;
        
    }
}
