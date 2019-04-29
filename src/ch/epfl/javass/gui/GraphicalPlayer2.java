package ch.epfl.javass.gui;

import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Score;
import com.sun.javafx.collections.UnmodifiableObservableMap;
import javafx.collections.ObservableMap;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Observable;

public class GraphicalPlayer2 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
//    private PlayerId myId;
//    private Map<PlayerId, String> playerNames;
//    private ScoreBean scoreBean;
//    private TrickBean trickBean;

    private ObservableMap cards160 = cards160();
    private ObservableMap cards240 = cards240();

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

        BorderPane team1Pane = createTeam1Pane(scoreBean);
        team1Pane.setStyle("-fx-font: 16 Optima; -fx-background-color: white;");
        BorderPane team2Pane = createTeam2Pane(scoreBean);
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
        //TODO
        return null;
    }


    private GridPane createTrickPane(TrickBean trickBean) {
        //TODO
        return null;
    }


    private BorderPane createTeam1Pane(ScoreBean scoreBean) {
        //TODO
        return null;
    }

    private BorderPane createTeam2Pane(ScoreBean scoreBean) {
        //TODO
        return null;
    }


    private UnmodifiableObservableMap cards160() {

    }

    private UnmodifiableObservableMap cards240() {

    }
}
