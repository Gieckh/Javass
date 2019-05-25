package ch.epfl.javass.gui;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableSet;
import static javafx.collections.FXCollections.unmodifiableObservableList;
import static javafx.collections.FXCollections.unmodifiableObservableSet;

import java.util.Collections;
import java.util.List;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import src.cs108.Announcement;
import src.cs108.MeldSet;

//TODO https://piazza.com/class/jrhvyjm5czn4f?cid=346 ?
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
    private ObservableList<MeldSet> listOfAnnouncementPerPlayer = observableArrayList();
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
    
    
    public ObservableList<MeldSet> announcesPerPlayer(){
        
        return listOfAnnouncementPerPlayer.isEmpty() ?
                unmodifiableObservableList(observableArrayList(Collections.nCopies(4, MeldSet.EMPTY_SET))):
                unmodifiableObservableList(listOfAnnouncementPerPlayer);
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
            //TODO: newHand is included in current hand right ?

            for(int i = 0 ; i < 9 ; ++i) {
                Card cardToReplace = hand.get(i);
                if ( (cardToReplace != null)  &&  !newHand.contains(cardToReplace) ) {
                    hand.set(i, null);
                }
            }
       }
   }
    
    public void setannounces(CardSet hand) {
       List<MeldSet> announces=  Announcement.getAnnounces(hand);//CardSet.of(this.hand
       this.announces.clear();
       if(hand!=CardSet.EMPTY) {
           for(int i = 0 ; i < announces.size(); ++i) {
               this.announces.add(announces.get(i));
           }
       }
   }
    
    public void setannouncesPerPlayer(List<MeldSet> listOfAnnounces) {
        this.listOfAnnouncementPerPlayer.clear();
        for(int i = 0 ; i < announces.size(); ++i) {
            this.listOfAnnouncementPerPlayer.add(listOfAnnounces.get(i));
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
