package ch.epfl.javass.gui;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Trick;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;
import static javafx.collections.FXCollections.observableHashMap;


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

    private ObjectProperty<Color> trump = new SimpleObjectProperty<>();
    private ObservableMap<PlayerId,Card> trick = observableHashMap();
    private ObjectProperty<PlayerId> winningPlayer = new SimpleObjectProperty<>();
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    //default constructor per default is enough
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @brief public getter of the trick property - each player is linked to the
     *        {@code Card} he has played during the trick, or to {@code null} if
     *        he hasn't played yet.
     *
     * @return (ObservableMap<PlayerId, Card>) - the trick property
    */
    public ObservableMap<PlayerId, Card> trick() {
        return trick;
    }
    
    /**
     * @brief public getter of the trump property - indicates the current trump
     *
     * @return (ReadOnlyObjectProperty<Color>) - the trump property
    */
    public ReadOnlyObjectProperty<Color> trumpProperty() {
        return trump;
    }

    //TODO: never used for now ?
    /**
     * @brief It is a public getter of the winningPlayer property - the player
     *        <em>currently</em> winning the trick.
     *
     * @return (ReadOnlyObjectProperty<PlayerId>) - the winningPlayer property
    */
    public ReadOnlyObjectProperty<PlayerId> winningPlayerProperty() {
        return winningPlayer;
    }
    
    
    /**
     * @brief public setter of the winningPlayer property.
     *
     * @param pId (PlayerId) - the Id of the player <em>currently</em> winning the trick.
    */
    private void setWinningPlayer(PlayerId pId) {
            winningPlayer.set(pId);
    }
        
    /**
     * @brief public setter of the trump property.
     *
     * @param trump (Color) - the trump of the current turn.
    */
    public void setTrump(Color trump) {
        this.trump.set(trump);
    }
    
    /**
     * @brief public setter of the trick property.
     *
     * @param newTrick (Trick) - the current trick.
    */
    public void setTrick(Trick newTrick) {
        PlayerId winnerId;
        winnerId = newTrick.isEmpty() ? null : newTrick.winningPlayer() ;
        setWinningPlayer(winnerId);
        ObservableMap<PlayerId,Card> thisTrick = observableHashMap();

        for(int i = 0 ; i < newTrick.size() ; ++i)
            thisTrick.put(newTrick.player(i), newTrick.card(i));

        for (PlayerId pId: PlayerId.ALL) {
            if (!thisTrick.containsKey(pId))
                thisTrick.put(pId, null);
        }

        assert (thisTrick.size() == 4);
        this.trick.clear();
        this.trick.putAll(thisTrick);
    }
}
