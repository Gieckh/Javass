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

/**
 * TrickBean is a JavaFx bean containing the current trick property. 
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
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

    //default constructor per default is enough
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    //TODO demander a marin le bilingue si on dit "setter of" ,"setter on" ou "setter for" ?.
    
    /**
     * @brief It is a public getter of the trick property.
     *
     * @return the trick property
    */
    public ObservableMap<PlayerId, Card> trick() {
        return trick;
    }
    
    /**
     * @Brief It is a public getter of the trump property.
     *
     * @return the trump property
    */
    public ReadOnlyObjectProperty<Color> trumpProperty() {
        return trump;
    }
    
    /**
     * @Brief It is a public getter of the winningPlayer property.
     *
     * @return the winningPlayer property
    */
    public ReadOnlyObjectProperty<PlayerId> winningPlayerProperty() {
        return winningPlayer;
    }
    
    
    /**
     * @Brief public setter of the winningPlayer property given the winning player's Id.
     *
     * @param pId the PlayerId of the winning player
    */
    private void setWinningPlayer(PlayerId pId) {
            winningPlayer = new SimpleObjectProperty<PlayerId>(pId);
    }
        
    /**
     * @Brief public setter of the trump property given the trump's Color.
     *
     * @param trump the color of the trump
    */
    public void setTrump(Color trump) {
        this.trump = new SimpleObjectProperty<Color>(trump);
    }
    
    /**
     * @Brief public setter of the trick property given a trick.
     *
     * @param newTrick a Trick
    */
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
