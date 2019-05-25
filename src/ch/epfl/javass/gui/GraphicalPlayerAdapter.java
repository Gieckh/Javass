package ch.epfl.javass.gui;

import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Score;
import ch.epfl.javass.jass.TeamId;
import ch.epfl.javass.jass.Trick;
import ch.epfl.javass.jass.TurnState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import src.cs108.MeldSet;

/**
 * @Brief This class extends Player and is an Adapter of a graphicalPlayer.
 *        As said in the patron Adapter ,the graphicalPlayerAdapter shares his scoreBean, trickBean, and handBean
 *        with the GraphicalPlayer. Most methods are about updating the corresponding Bean on the JavaFx thread (thanks to runLater).
 *        The SetPlayer's particularity is that it is the method creating the graphicalPlayer instance.
 *        CardToPlay returns the single card contained in "queueOfCommunication", which is an array shared with the graphicalPlayer.
 *        
 *
 * @see Player
 * @see GraphicalPlayer
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public class GraphicalPlayerAdapter implements Player {
    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    ScoreBean scoreBean;
    TrickBean trickBean;
    HandBean handBean;
    GraphicalPlayer graphicalPlayer;
    ArrayBlockingQueue<Card> queueOfCommunication ;
    ArrayBlockingQueue<Integer> CheatingQueue;
    ArrayBlockingQueue<MeldSet> meldQueue;
    ObjectProperty<ListView<Text>> listOfAnnounces = new SimpleObjectProperty<ListView<Text>>();
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    /**
     * @Brief the only constructor of this class. It takes no parameters
     *        and in particular no underlying player for the obvious reason that
     *        the graphicalPlayerAdapter will play what a human wants.
     * 
     */
    public GraphicalPlayerAdapter() {
        this.handBean = new HandBean();
        this.scoreBean = new ScoreBean();
        this.trickBean = new TrickBean();
        this.queueOfCommunication =  new ArrayBlockingQueue<>(1);
        this.CheatingQueue = new ArrayBlockingQueue<>(1);
        this.meldQueue = new ArrayBlockingQueue<>(1);
        
    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /* 
     * @see ch.epfl.javass.jass.Player#cardToPlay(ch.epfl.javass.jass.TurnState, ch.epfl.javass.jass.CardSet)
     */
    @Override public Card cardToPlay(TurnState state, CardSet hand) {
        try {
            handBean.setPlayableCards(state.trick().playableCards(hand));
            Card card = queueOfCommunication.take();
            handBean.setPlayableCards(CardSet.EMPTY);
            return card;
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            throw new Error();
        }
//        return state.trick().playableCards(hand).get(0);
    }
    

    
    
    /* 
     * @see ch.epfl.javass.jass.Player#cheat(int)
     */
    @Override
    public int cheat() {
        try {
            return CheatingQueue.isEmpty() ? 0 : CheatingQueue.take() ;
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            throw new Error();
        }
    }
    
    /* 
     * @see ch.epfl.javass.jass.Player#announcement(ch.epfl.javass.jass.CardSet)
     */
    @Override
    public MeldSet announcement(CardSet hand) {
        try {
            runLater(() -> { 
                handBean.setannounces(hand);
                listOfAnnounces.setValue(createAnnouncesPane());});
            MeldSet meldSet = meldQueue.take() ;
            runLater(() -> { handBean.setannounces(CardSet.EMPTY);
            });
            
            
            return meldSet;
            
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            throw new Error();
        }

    }

    private ListView<Text> createAnnouncesPane() {
        ObservableList<MeldSet> meldSet = handBean.annouces();
        ObservableList<Text> AllAnnouncesSet =  observableArrayList();
        for(MeldSet m : meldSet) {
            Text children = new Text();
            SimpleStringProperty str = new SimpleStringProperty();
            str.setValue(m.toString());
            children.textProperty().bind(str);
            children.setStyle("-fx-font: 16 Optima;\n");
            children.setOnMouseClicked((e) -> { 
                try {
                    meldQueue.put(m);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });
           AllAnnouncesSet.add(children);
        }
        ListView<Text> announcesPane = new ListView<>(AllAnnouncesSet);
        return announcesPane;
    }
    
    
    /* 
     * @see ch.epfl.javass.jass.Player#setPlayers(ch.epfl.javass.jass.PlayerId, java.util.Map)
     */
    @Override
    public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        this.graphicalPlayer = new GraphicalPlayer(ownId, playerNames, this.scoreBean, this.trickBean, 
                this.handBean, this.queueOfCommunication, this.CheatingQueue,this.listOfAnnounces);
        runLater(() -> { graphicalPlayer.createStage().show(); });
    
    }

    /* 
     * @see ch.epfl.javass.jass.Player#updateHand(ch.epfl.javass.jass.CardSet)
     */
    @Override public void updateHand(CardSet newHand) {
        runLater(() -> {handBean.setHand(newHand);});
    }

    @Override
    public void updateAnnouncement(List<MeldSet> m) {
        runLater(()->{handBean.setannouncesPerPlayer(m);});
    }
    
    /* 
     * @see ch.epfl.javass.jass.Player#setTrump(ch.epfl.javass.jass.Card.Color)
     */
    @Override public void setTrump(Card.Color trump) {
        runLater(()-> {trickBean.setTrump(trump);});
    }

    /* 
     * @see ch.epfl.javass.jass.Player#updateTrick(ch.epfl.javass.jass.Trick)
     */
    @Override public void updateTrick(Trick newTrick) {
        runLater(()-> {trickBean.setTrick(newTrick);});
    }

    /* 
     * @see ch.epfl.javass.jass.Player#updateScore(ch.epfl.javass.jass.Score)
     */
    @Override public void updateScore(Score score) {
        runLater(()-> {for(TeamId teamId :TeamId.ALL ) {
            scoreBean.setGamePoints(teamId, score.gamePoints(teamId));
            scoreBean.setTotalPoints(teamId, score.totalPoints(teamId));
            scoreBean.setTurnPoints(teamId, score.turnPoints(teamId));
            }
        });
    }

    /* 
     * @see ch.epfl.javass.jass.Player#setWinningTeam(ch.epfl.javass.jass.TeamId)
     */
    @Override public void setWinningTeam(TeamId winningTeam) {
        runLater(()-> {scoreBean.setWinningTeam(winningTeam);});
    }
}
