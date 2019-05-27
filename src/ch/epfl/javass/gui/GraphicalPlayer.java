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
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
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

/**
 * This class represents the graphical interface of a human player.
 *
 * @author - Marin Nguyen (288260)
 * @author - Antoine Scardigli (299905)
 */
public final class GraphicalPlayer {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    private final ObservableMap<Card, Image> cards160 = cards(160);
    private final ObservableMap<Card, Image> cards240 = cards(240);
    private final ObservableMap<Card.Color, Image> trumps = trumps();
    private final int TRICK_CARD_HEIGHT = 180;
    private final int TRICK_CARD_WIDTH = 120;
    private final int TRUMP_HEIGHT = 101;
    private final int TRUMP_WIDTH = 101;
    private StackPane finalPane;


    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    public GraphicalPlayer(PlayerId myId, Map<PlayerId, String> playerNames,
            ScoreBean scoreBean, TrickBean trickBean, HandBean handBean,
            ArrayBlockingQueue<Card> queueOfCommunication,
            ObjectProperty<ListView<Text>> listOfAnnounces)
    {
        GridPane scorePane = createScorePane(scoreBean, playerNames);

        GridPane trickPane = createTrickPane(trickBean, handBean, myId, playerNames);
        HBox handPane = createHandPane(handBean, queueOfCommunication);

        BorderPane team1Pane = createTeamPane(scoreBean, TeamId.TEAM_1, TeamId.TEAM_2, playerNames);
        BorderPane team2Pane = createTeamPane(scoreBean, TeamId.TEAM_2, TeamId.TEAM_1, playerNames);

        GridPane announcesPane = new GridPane();
        listOfAnnounces.addListener( (observable, oldValue, newValue) -> {
            announcesPane.getChildren().clear();
            announcesPane.getChildren().add(newValue);
            newValue.minHeightProperty().bind(trickPane.heightProperty());
        }  );
        BooleanBinding b =  createBooleanBinding( () -> {
            return handBean.annouces().size()!=0;
        },handBean.annouces());
        announcesPane.disableProperty().bind(b.not());

        BorderPane main = new BorderPane(
                trickPane, scorePane , announcesPane,
                handPane, new GridPane()
        );
        main.rightProperty().bind(when(b).then(announcesPane).otherwise(new GridPane()));
        this.finalPane = new StackPane(main, team1Pane, team2Pane);
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    private void cheatingManager(Scene scene, ArrayBlockingQueue<Integer> cheatingQueue) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(final KeyEvent keyEvent) {
                if (keyEvent.getCode().isDigitKey() && cheatingQueue.isEmpty()) {
                    try {
                        int code = Integer.parseInt(keyEvent.getCode().toString().substring(
                                keyEvent.getCode().toString().length() - 1));
                        if(code < 10)
                            cheatingQueue.put(code);

                    } catch (InterruptedException e1) {
                        throw new Error(e1);
                    }
                 keyEvent.consume();
               }
            }
        });
    }


    /**
     * @brief Create the javaFx stage of our JassGame.
     *
     * @param myId ({@code PlayerId})
     * @param playerNames ({Map<PlayerId, String>}) - associates the IDs of the
     *                    players to their names.
     * @return ({@code Stage}) - our game's stage.
     */
    public Stage createStage(PlayerId myId, Map<PlayerId, String> playerNames, ArrayBlockingQueue<Integer> cheatingQueue) {
        Stage stage = new Stage();
        stage.setTitle("Javass - " + playerNames.get(myId));
        Scene finalScene = new Scene(finalPane);
        finalScene.getRoot().requestFocus();
        cheatingManager(finalScene, cheatingQueue);

        stage.setScene(finalScene);
        return stage;
    }
    
    /**
     * @brief Creates the {@code GridPane} which displays the scores. [Will be at
     *        the top of the window]
     *
     * @param scoreBean ({@code ScoreBean})
     * @param playerNames ({@code Map<PlayerId, String>})
     * @return ({@code GridPane}) - the pane used to display the scores.
     */

    private GridPane createScorePane(ScoreBean scoreBean, Map<PlayerId, String> playerNames) {
        Text[] row1 = createScorePaneRow(PlayerId.PLAYER_1, PlayerId.PLAYER_3, playerNames, scoreBean);
        Text[] row2 = createScorePaneRow(PlayerId.PLAYER_2, PlayerId.PLAYER_4, playerNames, scoreBean);


        GridPane scoreGrid = new GridPane();
        scoreGrid.addRow(0, row1[0], row1[1], row1[2], row1[3], row1[4]);
        scoreGrid.addRow(1, row2[0], row2[1], row2[2], row2[3], row2[4]);

        scoreGrid.setStyle("-fx-font: 16 Optima; -fx-background-color: lightgray; -fx-padding: 5px; -fx-alignment: center;");
        return scoreGrid;
    }

    /**
     * @brief Creates one row of the ScorePane, and sets its HAlignment
     *
     * @param p1 ({@code PlayerId}) - the first player of the team.
     * @param p2 ({@code PlayerId}) - the second player of the team.
     *
     * @return A row of the ScorePane.
     */
    private Text[] createScorePaneRow(PlayerId p1, PlayerId p2, Map<PlayerId, String> playerNames, ScoreBean scoreBean) {
        Text[] scorePaneRow = new Text[5];
        scorePaneRow[0] = new Text(playerNames.get(p1) + " et " + playerNames.get(p2) + " : ");
        scorePaneRow[1] = new Text();
        scorePaneRow[1].textProperty().bind(Bindings.convert(scoreBean.turnPointsProperty(p1.team())));


        scorePaneRow[2] = new Text();
        scoreBean.turnPointsProperty(p1.team()).addListener((observable, oldValue, newValue) ->
                scorePaneRow[2].setText(
                        "(+" +
                        Math.max(0, newValue.intValue() - oldValue.intValue()) +
                        ")"
                )
        );

        scorePaneRow[3] = new Text("/ Total : ");

        scorePaneRow[4] = new Text();
        scorePaneRow[4].textProperty().bind(Bindings.convert(scoreBean.gamePointsProperty(p1.team())));


        GridPane.setHalignment(scorePaneRow[0], HPos.RIGHT);
        GridPane.setHalignment(scorePaneRow[1], HPos.RIGHT);
        GridPane.setHalignment(scorePaneRow[2], HPos.LEFT); //not needed, but more explicit.
        GridPane.setHalignment(scorePaneRow[3], HPos.LEFT); //not needed, but more explicit.
        GridPane.setHalignment(scorePaneRow[4], HPos.RIGHT);

        return scorePaneRow;
    }

    /**
     * @brief Creates the ({@code GridPane}) corresponding to the current trick.
     *
     * @param trickBean ({@code TrickBean})
     * @param myId ({@code PlayerId})
     * @param playerNames ({@code Map<PlayerId, String>})
     * @return ({@code GridPane}) - basically the current trick.
     */
    private GridPane createTrickPane(TrickBean trickBean, HandBean handBean, PlayerId myId, Map<PlayerId, String> playerNames) {
        BooleanBinding b = createBooleanBinding( () ->
                handBean.hand().stream().noneMatch(c -> c == null),
                handBean.hand(),
                trickBean.trick()
        );

        VBox middleLeft  = setTrickVBox (trickBean, handBean, myId.previousPlayer()         , 3, playerNames.get(myId.previousPlayer())         , b);
        VBox upCenter    = setTrickVBox (trickBean, handBean, myId.nextPlayer().nextPlayer(), 2, playerNames.get(myId.nextPlayer().nextPlayer()), b);
        VBox middleRight = setTrickVBox (trickBean, handBean, myId.nextPlayer()             , 1, playerNames.get(myId.nextPlayer())             , b);
        VBox downCenter  = setTrickVBox (trickBean, handBean, myId, 0                  , playerNames.get(myId)                                  , b);

        ImageView trumpImage = new ImageView();
        trumpImage.imageProperty().bind(valueAt(trumps, trickBean.trumpProperty()));
        trumpImage.setFitHeight(TRUMP_HEIGHT);
        trumpImage.setFitWidth(TRUMP_WIDTH);
        VBox trumpVBox = new VBox(trumpImage);
        trumpVBox.setAlignment(Pos.CENTER);

        GridPane trickGrid = new GridPane();
        trickGrid.setAlignment(Pos.CENTER);
        trickGrid.add(middleLeft , 0, 0, 1, 3);
        trickGrid.add(upCenter   , 1, 0, 1, 1);
        trickGrid.add(middleRight, 2, 0, 1, 3);
        trickGrid.add(downCenter , 1, 2, 1, 1);
        trickGrid.add(trumpVBox  , 1, 1, 1, 1);

        trickGrid.setStyle("-fx-background-color: whitesmoke; -fx-padding: 5px; -fx-border-width: 3px 0px; -fx-border-style: solid; -fx-border-color: gray; -fx-alignment: center;");
        return trickGrid;
    }

    /**
     * @brief Method used to fill the VBoxes corresponding to the cards which
     *        have been played during the current trick, and the names of the players
     *        who put them.
     *
     * @param trickBean ({@code TrickBean}) - the bean representing the trick.
     * @param pId ({@code PlayerId}) - the ID of the player corresponding to the
     *            card that will be displayed here.
     *
     * @param pos ({@code int}) - the position of the player on the board: '0' for yourself,
     *            '1' for the player on the right, and so on.
     * @param playerName ({@code String}) - the name of the corresponding player.
     * @return ({@code VBox}) - a card and the name of the player who put it.
     */
    private VBox setTrickVBox(TrickBean trickBean, HandBean handBean, PlayerId pId, int pos, String playerName, BooleanBinding b) {
        ImageView cardImage = new ImageView();
        cardImage.imageProperty().bind(valueAt(cards240, valueAt(trickBean.trick(), pId)));
        cardImage.setFitWidth(TRICK_CARD_WIDTH);
        cardImage.setFitHeight(TRICK_CARD_HEIGHT);

        Rectangle rectangle = new Rectangle(TRICK_CARD_WIDTH, TRICK_CARD_HEIGHT);
        BooleanBinding rectangleVisibleProperty = createBooleanBinding(
                () -> pId.equals(trickBean.winningPlayerProperty().get()),
                trickBean.winningPlayerProperty()
        );
        rectangle.visibleProperty().bind(rectangleVisibleProperty);
        rectangle.setStyle("-fx-arc-width: 20; -fx-arc-height: 20; -fx-fill: transparent; -fx-stroke: lightpink; -fx-stroke-width: 5; -fx-opacity: 0.5;");

        StackPane imageAndHalo = new StackPane(cardImage, rectangle);

        Text meld = new Text();
        SimpleStringProperty meldStr = new SimpleStringProperty();
        meldStr.bind(handBean.announcesPerPlayerToString().get(pId.ordinal()));
        meld.textProperty().bind(meldStr);
        meld.setStyle("-fx-font: 16 Optima;");
        meld.visibleProperty().bind(b);

        VBox vBox = (pos != 0) ? new VBox(new Text(playerName), imageAndHalo, meld) : new VBox(meld, imageAndHalo, new Text(playerName));
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-padding: 5px; -fx-alignment: center;");
        return vBox;
    }

    /**
     * @brief The victory panes.
     *
     * @param scoreBean ({@code ScoreBean})
     * @param winningTeam ({@code TeamId})
     * @param losingTeam ({@code TeamId})
     * @param playerNames ({@code Map<PlayerId, String>})
     * @return the victory panes.
     */
    private BorderPane
    createTeamPane(ScoreBean scoreBean, TeamId winningTeam, TeamId losingTeam, Map<PlayerId, String> playerNames)
    {
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

    private HBox createHandPane(HandBean handBean, ArrayBlockingQueue<Card> queueOfCommunication) {

        ImageView[] hand = new ImageView[9];
        for(int i = 0 ; i < hand.length ; ++i) {
            ImageView child = new ImageView();
            ObjectBinding<Card> correspondingCard = valueAt(handBean.hand(), i);
            child.imageProperty().bind(valueAt(cards160, correspondingCard));
            BooleanBinding isPlayable = createBooleanBinding(
                    () -> handBean.playableCards().contains(correspondingCard.get()),
                    handBean.playableCards(),
                    handBean.hand()
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
     * @brief the names of the players in the [actually] winning team.
     *
     * @param winningTeam ({@code TeamId})
     * @param playerNames ({@code Map<PlayerId, String>})
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
        return "/" + "trump_" +i+ ".png";
    }
}
