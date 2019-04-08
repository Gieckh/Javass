package ch.epfl.javass.gui;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableSet;

import com.sun.javafx.UnmodifiableArrayList;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

public final class HandBean {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    private ObservableList<Card> hand =  observableArrayList();
    private ObservableSet<Card> playableCards  = observableSet();

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    public ObservableList<Card> hand() {
        return hand;
    }
    
//    public void setHand(CardSet newHand) {
//        newHand.
//        ObservableList<Card> hand;
//        for(Card card : newHand) {
//            
//        }
//                if(newHand.size()==9) {
//            hand = new UnmodifiableObservableList<Card>();
//        }
//    }
    
    public ObservableSet<Card> playableCards() {
        return playableCards; 
    }
    
    public void setPlayableCards(CardSet newPlayableCards) {
        
    }
}
