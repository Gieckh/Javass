package ch.epfl.javass.gui;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import static javafx.beans.binding.Bindings.*;
import static javafx.collections.FXCollections.observableMap;
import static javafx.collections.FXCollections.unmodifiableObservableMap;

public class GraphicalPlayer2 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
//    private PlayerId myId;
//    private Map<PlayerId, String> playerNames;
//    private ScoreBean scoreBean;
//    private TrickBean trickBean;

    private final ObservableMap<Card, Image> cards160 = cards(160);
    private final ObservableMap<Card, Image> cards240 = cards(240);
    private final ObservableMap<Card.Color, Image> trumps = trumps();
    private ArrayBlockingQueue<Card> queueOfCommunication;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    public GraphicalPlayer2(PlayerId myId, Map<PlayerId, String> playerNames,
            ScoreBean scoreBean, TrickBean trickBean, HandBean handBean,
            ArrayBlockingQueue<Card> queueOfCommunication)
    {
        this.queueOfCommunication = queueOfCommunication;

        GridPane scorePane = createScorePane(scoreBean, playerNames);

        GridPane trickPane = createTrickPane(trickBean, myId, playerNames);
        HBox handPane = createHandPane(handBean);

        BorderPane team1Pane = createTeamPane(scoreBean, TeamId.TEAM_1, TeamId.TEAM_2, playerNames);
        BorderPane team2Pane = createTeamPane(scoreBean, TeamId.TEAM_2, TeamId.TEAM_1, playerNames);

    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     *
     * @return
     */
    public Stage createStage(StackPane finalPane, PlayerId myId, Map<PlayerId, String> playerNames) {
        Stage stage = new Stage();
        stage.setTitle("Javass - " + playerNames.get(myId));
        stage.setScene(new Scene(finalPane));
        return stage;
    }

    //Didn't feel like making a private method to initialize a row. TODO
    /**
     * @brief //TODO
     *
     * @param scoreBean
     * @return
     */
    //TODO: array
    private GridPane createScorePane(ScoreBean scoreBean, Map<PlayerId, String> playerNames) {
        // https://piazza.com/class/jrhvyjm5czn4f?cid=372 //TODO: suppr
        //First row
        Text textL1C1 = new Text(playerNames.get(PlayerId.PLAYER_1) + " et " + playerNames.get(PlayerId.PLAYER_3) + " : ");

        Text textL1C2 = new Text();
        textL1C2.textProperty().bind(Bindings.convert(scoreBean.turnPointsProperty(TeamId.TEAM_1)));

        Text textL1C3 = new Text();
        scoreBean.turnPointsProperty(TeamId.TEAM_1).addListener((observable, oldValue, newValue) ->
                textL1C3.setText("(+" + Math.max(0, newValue.intValue() - oldValue.intValue()) + ")"));

        Text textL1C4 = new Text("/ Total : ");

        Text textL1C5 = new Text();
        textL1C5.textProperty().bind(Bindings.convert(scoreBean.gamePointsProperty(TeamId.TEAM_1)));


        //Second row
        Text textL2C1 = new Text(playerNames.get(PlayerId.PLAYER_2) + " et " + playerNames.get(PlayerId.PLAYER_4) + " : ");

        Text textL2C2 = new Text();
        textL2C2.textProperty().bind(Bindings.convert(scoreBean.turnPointsProperty(TeamId.TEAM_2)));

        Text textL2C3 = new Text();
        scoreBean.turnPointsProperty(TeamId.TEAM_2).addListener((observable, oldValue, newValue) ->
                textL2C3.setText("(+" + Math.max(0, newValue.intValue() - oldValue.intValue()) + ")"));

        Text textL2C4 = new Text("/ Total : ");

        Text textL2C5 = new Text();
        textL2C5.textProperty().bind(Bindings.convert(scoreBean.gamePointsProperty(TeamId.TEAM_2)));


        GridPane scoreGrid = new GridPane();
        scoreGrid.addRow(0, textL1C1, textL1C2, textL1C3, textL1C4, textL1C5);
        scoreGrid.addRow(1, textL2C1, textL2C2, textL2C3, textL2C4, textL2C5);

        //Setting the alignments
        GridPane.setHalignment(textL1C1, HPos.RIGHT);
        GridPane.setHalignment(textL1C2, HPos.RIGHT);
        GridPane.setHalignment(textL1C3, HPos.LEFT); //not needed, but more explicit.
        GridPane.setHalignment(textL1C4, HPos.LEFT); //not needed, but more explicit.
        GridPane.setHalignment(textL1C5, HPos.RIGHT);

        GridPane.setHalignment(textL2C1, HPos.RIGHT);
        GridPane.setHalignment(textL2C2, HPos.RIGHT);
        GridPane.setHalignment(textL2C3, HPos.LEFT); //not needed, but more explicit.
        GridPane.setHalignment(textL2C4, HPos.LEFT); //not needed, but more explicit.
        GridPane.setHalignment(textL2C5, HPos.RIGHT);

        scoreGrid.setStyle("-fx-font: 16 Optima; -fx-background-color: lightgray; -fx-padding: 5px; -fx-alignment: center;");
        return scoreGrid;
    }

    /**
     *
     * @param trickBean
     * @return
     */
    private GridPane createTrickPane(TrickBean trickBean, PlayerId myId, Map<PlayerId, String> playerNames) {
        int COL_SPAN = 1;
        int ROW_SPAN = 1;
        //TODO

        VBox middleLeft  = setTrickVBox (trickBean, myId.previousPlayer(), 3, playerNames.get(myId.previousPlayer()));
        VBox upCenter    = setTrickVBox (trickBean, myId.nextPlayer().nextPlayer(), 2, playerNames.get(myId.nextPlayer().nextPlayer()));
        VBox middleRight = setTrickVBox (trickBean, myId.nextPlayer(), 1, playerNames.get(myId.nextPlayer()));
        VBox downCenter  = setTrickVBox (trickBean, myId, 0, playerNames.get(myId));

        ImageView trumpImage = new ImageView();
        trumpImage.imageProperty().bind(valueAt(trumps, trickBean.trumpProperty()));
        trumpImage.setFitHeight(101);
        trumpImage.setFitWidth(101);
        //TODO VBox trumpVBox = new VBox(trumpImage);
        //TODO trumpVBox.setAlignment(Pos.CENTER);

        GridPane trickGrid = new GridPane();
        trickGrid.add(middleLeft , 0, 1, COL_SPAN, ROW_SPAN);
        trickGrid.add(upCenter   , 1, 0, COL_SPAN, ROW_SPAN);
        trickGrid.add(middleRight, 2, 1, COL_SPAN, ROW_SPAN);
        trickGrid.add(downCenter , 1, 2, COL_SPAN, ROW_SPAN);
        trickGrid.add(trumpImage , 1, 1, COL_SPAN, ROW_SPAN);

        trickGrid.setStyle("-fx-background-color: whitesmoke; -fx-padding: 5px; -fx-border-width: 3px 0px; -fx-border-style: solid; -fx-border-color: gray; -fx-alignment: center;");
        return trickGrid;
    }

    private VBox setTrickVBox(TrickBean trickBean, PlayerId pId, int pos, String playerName) {
        ImageView cardImage = new ImageView();
        cardImage.imageProperty().bind(valueAt(cards240, valueAt(trickBean.trick(), pId)));
        cardImage.setFitHeight(180);
        cardImage.setFitWidth(120);

        Rectangle rectangle = new Rectangle(120, 180);
        BooleanBinding rectangleVisibleProperty = createBooleanBinding(
                () -> pId.equals(trickBean.winningPlayerProperty().get()),
                trickBean.winningPlayerProperty()
        );
        rectangle.visibleProperty().bind(rectangleVisibleProperty);
        rectangle.setStyle("-fx-arc-width: 20; -fx-arc-height: 20; -fx-fill: transparent; -fx-stroke: lightpink; -fx-stroke-width: 5; -fx-opacity: 0.5;");

        StackPane imageAndHalo = new StackPane(cardImage, rectangle);

        VBox vBox = (pos == 0) ? new VBox(new Text(playerName), imageAndHalo) : new VBox(imageAndHalo, new Text(playerName));
        //TODO vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-padding: 5px; -fx-alignment: center;");
        return vBox;
    }

    /**
     * @brief The victory panes.
     *
     * @param scoreBean
     * @param winningTeam
     * @param losingTeam
     * @param playerNames
     * @return
     */
    private BorderPane
    createTeamPane(ScoreBean scoreBean, TeamId winningTeam, TeamId losingTeam, Map<PlayerId, String> playerNames)
    {
        //TODO: test
        String[] winningPlayerNames = findWinningPlayerNames(winningTeam, playerNames);

        Text victoryText = new Text();
        victoryText.textProperty().bind(Bindings.format(
                 "%s et %s ont gagné avec %d points contre %d.",
                winningPlayerNames[0],
                winningPlayerNames[1],
                scoreBean.totalPointsProperty(winningTeam),
                scoreBean.totalPointsProperty(losingTeam)
                )
        );

        BorderPane victoryPane = new BorderPane(victoryText);
        victoryPane.visibleProperty().bind(scoreBean.winningTeamProperty().isEqualTo(winningTeam));

        victoryPane.setStyle("-fx-font: 16 Optima; -fx-background-color: white;");
        return victoryPane;
    }

    @SuppressWarnings("Duplicates")
    private HBox createHandPane(HandBean handBean) {

        ImageView[] hand = new ImageView[9];
        for(int i = 0 ; i < hand.length ; ++i) {
            ImageView child = new ImageView();
            ObjectBinding<Card> correspondingCard = valueAt(handBean.hand(), i);
            child.imageProperty().bind(valueAt(cards160, correspondingCard));
            BooleanBinding isPlayable = createBooleanBinding(
                    () -> handBean.playableCards().contains(correspondingCard.get()),
                    handBean.playableCards(),handBean.hand()
            );
            child.opacityProperty().bind(when(isPlayable).then(1.0).otherwise(0.2) );
            child.disableProperty().bind(isPlayable.not());
            child.setOnMouseClicked((e)-> {
                try {
                    queueOfCommunication.put(correspondingCard.get());
                } catch (InterruptedException e1) {
                    throw new Error(e1);
                }
            });

            child.setFitHeight(120);
            child.setFitWidth(80);
            hand[i] = child;
        }

        HBox handPane = new HBox(hand);
        handPane.setStyle("-fx-background-color: lightgray; -fx-spacing: 5px; -fx-padding: 5px;");
        return handPane;
    }

    /**
     * @brief
     *
     * @param winningTeam
     * @param playerNames
     * @return
     */
    private String[] findWinningPlayerNames(TeamId winningTeam, Map<PlayerId, String> playerNames) {
        String[] winningPlayerNames = new String[2];
        if (winningTeam == TeamId.TEAM_1) {
            winningPlayerNames[0] = playerNames.get(PlayerId.PLAYER_1);
            winningPlayerNames[1] = playerNames.get(PlayerId.PLAYER_3);
        }
        else {
            winningPlayerNames[0] = playerNames.get(PlayerId.PLAYER_2);
            winningPlayerNames[1] = playerNames.get(PlayerId.PLAYER_4);
        }

        return winningPlayerNames;
    }

    /**
     * @brief
     *
     * @param width
     * @return
     */
    private ObservableMap<Card, Image> cards(int width) {
        ObservableMap<Card, Image> map = FXCollections.observableMap(new HashMap<>());

        for (int c = 0; c < Card.Color.COUNT; ++c) {
            for (int r = 0; r < Card.Rank.COUNT; ++r) {
                map.put(Card.of(Card.Color.ALL.get(c), Card.Rank.ALL.get(r)),
                                  toCardName(c, r, width)
                );
            }
        }

        return unmodifiableObservableMap(map);
    }

    /**
     * @brief
     *
     * @param c
     * @param r
     * @param width
     * @return
     */
    private Image toCardName(int c, int r, int width) {
        return new Image("/card_" + c + "_" + r + "_" + width + ".png");
    }

    private ObservableMap<Card.Color,Image> trumps() {
        ObservableMap<Card.Color,Image> map = observableMap(new HashMap<>());

        for (Card.Color c: Card.Color.ALL)
            map.put(c, new Image(toTrumpName(c.ordinal())));
        return  unmodifiableObservableMap(map);
    }

    private String toTrumpName(int i) {
        return "/"+"trump_"+i+".png";
    }
}
