package ch.epfl.javass.gui;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.TeamId;
import ch.epfl.javass.jass.Trick;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import static javafx.collections.FXCollections.observableHashMap;

import com.sun.javafx.collections.UnmodifiableObservableMap;

public final class TrickBean {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    private ObservableMap<PlayerId,Card> trick = observableHashMap();
    private ObjectProperty<Color> trump = new SimpleObjectProperty<Color>();
    private ObjectProperty<PlayerId> winningPlayer = new SimpleObjectProperty<PlayerId>();
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    
    public ObservableMap<PlayerId, Card> trick() {
        return trick;
    }
    
    public ReadOnlyObjectProperty<Color> trumpProperty() {
        return trump;
    }
    
    public ReadOnlyObjectProperty<PlayerId> winningPlayerProperty() {
        return winningPlayer;
    }
    
    private void setWinningPlayer(PlayerId pId) {
            winningPlayer = new SimpleObjectProperty<PlayerId>(pId);
    }
    
    public void setTrump(Color trump) {
        this.trump = new SimpleObjectProperty<Color>(trump);
    }
    
    public void setTrick(Trick newTrick) {
        PlayerId pId = newTrick.winningPlayer();
        setWinningPlayer(pId);
        ObservableMap<PlayerId,Card> thisTrick = observableHashMap();

        for(int i = 0 ; i < 4 ; ++i) {
            thisTrick.put(newTrick.player(i), newTrick.card(i));
        }
        trick = new UnmodifiableObservableMap<>(thisTrick);
        
    }
    
}
