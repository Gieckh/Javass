package ch.epfl.javass.gui;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableSet;
import static javafx.collections.FXCollections.unmodifiableObservableList;
import static javafx.collections.FXCollections.unmodifiableObservableSet;

import java.util.Collections;
import java.util.List;

import ch.epfl.javass.jass.Announces.Announcement;
import ch.epfl.javass.jass.Announces.MeldSet;
import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

/**
 * HandBean is a JavaFx bean containing the hand and playableCards properties.
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class HandBean {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private ObservableList<SimpleStringProperty> listOfAnnouncementPerPlayer = observableArrayList();
    private ObservableList<Card> hand =  observableArrayList();
    private ObservableSet<Card> playableCards  = observableSet();
    private ObservableList<MeldSet> announces = observableArrayList();

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    //default constructor per default is enough

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    /**
     * @brief It is a public getter of the hand property.
     *
     * @return the hand property
     */
    public ObservableList<Card> hand() {
        return unmodifiableObservableList(hand);
    }
    
    /**
     * @brief It is a public getter of the playableCards property.
     *
     * @return the playableCards property
     */
    public ObservableSet<Card> playableCards() {
        return unmodifiableObservableSet(playableCards); 
    }

    /**
     * @brief It is a public getter of the announcement property.
     *
     * @return the announcement property
     */
    public ObservableList<MeldSet> annouces(){
        return unmodifiableObservableList(announces);
    }


    /**
     * @Brief public getter of the list of stringProperty of the announces per player.
     *
     * @return the list of stringProperty announces per player
    */
    public ObservableList<SimpleStringProperty> announcesPerPlayerToString(){
        if(listOfAnnouncementPerPlayer.isEmpty()) {
            setannouncesPerPlayer(Collections.nCopies(4, MeldSet.EMPTY_SET));;
        }
        return unmodifiableObservableList(listOfAnnouncementPerPlayer);
    }


    /**
     * @brief It is a public setter for the hand property, given the CardSet newHand.
     *  If the new CardSet newHand is of size 9, we replace the previous hand property 
     *  with the new one, otherwise we simply put to null the cards that are not in newHand
     *  but were in the hand attribute.  
     *
     * @param newHand the new CardSet
     */
    public void setHand(CardSet newHand) {
        if(newHand.size() == 9) {
            hand.clear();
            for(int i = 0 ; i < 9 ; ++i) {
                hand.add(newHand.get(i));
            }
        }

        else { //newHand.size() < 9
            assert (newHand.size() < 9);
            for(int i = 0 ; i < 9 ; ++i) {
                Card cardToReplace = hand.get(i);
                if ( (cardToReplace != null)  &&  !newHand.contains(cardToReplace) ) {
                    hand.set(i, null);
                }
            }
       }
   }
    
    /**
     * @Brief public setter for the self-announcement
     *
     * @param hand
    */
    public void setannounces(CardSet hand) {
       List<MeldSet> announces=  Announcement.getAnnounces(hand);//CardSet.of(this.hand
       this.announces.clear();
       if(hand!=CardSet.EMPTY) {
           for(int i = 0 ; i < announces.size(); ++i) {
               this.announces.add(announces.get(i));
           }
       }
   }

    /**
     * @Brief public setter for the list of all announces.
     *
     * @param listOfAnnounces
    */
    public void setannouncesPerPlayer(List<MeldSet> listOfAnnounces) {
        if(listOfAnnouncementPerPlayer.isEmpty()) {
            for(int i =0 ; i<4 ;++i) {
                SimpleStringProperty s = new SimpleStringProperty();
                listOfAnnouncementPerPlayer.add(s);
            }
        }
        for(int i = 0 ; i < listOfAnnounces.size(); ++i) {
           listOfAnnouncementPerPlayer.get(i).set(listOfAnnounces.get(i).toString());
        }
    }


    /**
     * @brief public setter for the playableCards property. The current list of
     *        (Card) is cleared, and [re]filled with the list of (Card) indicated
     *        by the (CardSet) "newPlayableCards"
     *
     * @param newPlayableCards the new CardSet
     */
    public void setPlayableCards(CardSet newPlayableCards) {
        this.playableCards.clear();
        for(int i = 0 ; i < newPlayableCards.size() ; ++i) {
            playableCards.add(newPlayableCards.get(i));
        }
    }
}
