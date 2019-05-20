package ch.epfl.javass.gui;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
public class GraphicalPlayer2 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
//    private PlayerId myId;
//    private Map<PlayerId, String> playerNames;
//    private ScoreBean scoreBean;
//    private TrickBean trickBean;

    private final ObservableMap cards160 = cards(160);
    private final ObservableMap cards240 = cards(240);

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    public GraphicalPlayer2(PlayerId myId, Map<PlayerId, String> playerNames,
                            ScoreBean scoreBean, TrickBean trickBean)
    {
        GridPane scorePane = createScorePane(scoreBean, playerNames);
        scorePane.setStyle("-fx-font: 16 Optima; -fx-background-color: lightgray; -fx-padding: 5px; -fx-alignment: center;");

        GridPane trickPane = createTrickPane(trickBean);
        trickPane.setStyle("-fx-background-color: whitesmoke; -fx-padding: 5px; -fx-border-width: 3px 0px; -fx-border-style: solid; -fx-border-color: gray; -fx-alignment: center;");

        BorderPane team1Pane = createTeamPane(scoreBean, TeamId.TEAM_1, TeamId.TEAM_2, playerNames);
        team1Pane.setStyle("-fx-font: 16 Optima; -fx-background-color: white;");
        BorderPane team2Pane = createTeamPane(scoreBean, TeamId.TEAM_2, TeamId.TEAM_1, playerNames);
        team2Pane.setStyle("-fx-font: 16 Optima; -fx-background-color: white;");

    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     *
     * @return
     */
    public Stage createStage() {
        //TODO
        return null;
    }

    //Didn't feel like making a private method to initialize a row.
    /**
     * @brief //TODO
     *
     * @param scoreBean
     * @return
     */
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

        return scoreGrid;
    }

    /**
     *
     * @param trickBean
     * @return
     */
    private GridPane createTrickPane(TrickBean trickBean) {
        //TODO
        return null;
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

        return victoryPane;
    }

    /**
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

        return FXCollections.unmodifiableObservableMap(map);
    }

    /**
     *
     * @param c
     * @param r
     * @param width
     * @return
     */
    private Image toCardName(int c, int r, int width) {
        return new Image("/card_" + c + "_" + r + "_" + width + ".png");
    }
}
