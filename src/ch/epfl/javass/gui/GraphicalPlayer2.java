package ch.epfl.javass.gui;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.TeamId;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
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
        GridPane scorePane = createScorePane(scoreBean);
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
    public Stage createStage() {
        //TODO
        return null;
    }


    private GridPane createScorePane(ScoreBean scoreBean) {
        // https://piazza.com/class/jrhvyjm5czn4f?cid=372
        //TODO
        return null;
    }


    private GridPane createTrickPane(TrickBean trickBean) {
        //TODO
        return null;
    }


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

        return FXCollections.unmodifiableObservableMap(map);
    }

    private Image toCardName(int c, int r, int width) {
        return new Image("/card_" + c + "_" + r + "_" + width + ".png");
    }
}
