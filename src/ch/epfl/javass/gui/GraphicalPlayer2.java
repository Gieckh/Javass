package ch.epfl.javass.gui;

import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Score;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Map;

public class GraphicalPlayer2 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private PlayerId myId;
    private Map<PlayerId, String> playerNames;
    private ScoreBean scoreBean;
    private TrickBean trickBean;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    public GraphicalPlayer2(PlayerId myId, Map<PlayerId, String> playerNames,
                            ScoreBean scoreBean, TrickBean trickBean)
    {
        this.myId = myId;
        this.playerNames = playerNames;
        this.scoreBean = scoreBean;
        this.trickBean = trickBean;

        GridPane scorePane = createScorePane();
        scorePane.setStyle("-fx-font: 16 Optima; -fx-background-color: lightgray; -fx-padding: 5px; -fx-alignment: center;");

        GridPane trickPane = createTrickPane();
        trickPane.setStyle("-fx-background-color: whitesmoke; -fx-padding: 5px; -fx-border-width: 3px 0px; -fx-border-style: solid; -fx-border-color: gray; -fx-alignment: center;");

        BorderPane team1Pane = createTeam1Pane();
        team1Pane.setStyle("-fx-font: 16 Optima; -fx-background-color: white;");
        BorderPane team2Pane = createTeam2Pane();
        team2Pane.setStyle("-fx-font: 16 Optima; -fx-background-color: white;");

    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    public Stage createStage() {
        //TODO
        return null;
    }


    private GridPane createScorePane() {
        //TODO
        return null;
    }


    private GridPane createTrickPane() {
        //TODO
        return null;
    }


    private BorderPane createTeam1Pane() {
        //TODO
        return null;
    }

    private BorderPane createTeam2Pane() {
        //TODO
        return null;
    }
}
