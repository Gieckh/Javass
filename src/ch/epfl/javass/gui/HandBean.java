package ch.epfl.javass.gui;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableSet;
import static javafx.collections.FXCollections.unmodifiableObservableList;
import static javafx.collections.FXCollections.unmodifiableObservableSet;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
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
    
    private ObservableList<Card> hand =  observableArrayList();
    private ObservableSet<Card> playableCards  = observableSet();

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
    
    //TODO : why isn't the test of the prof printing correctly ? (althought works)
    
    
    /**
     * @brief It is a public setter for the hand property, given the CardSet newHand.
     *  If the new CardSet newHand is of size 9, we replace the previous hand property 
     *  with the new one, otherwise we simply put to null the cards that are not in newHand
     *  but were in the hand attribute.  
     *
     * @param newHand the new CardSet
     */
    public void setHand(CardSet newHand) {
        ObservableList<Card> hand = observableArrayList();
        if(newHand.size() == 9) {
            for(int i = 0 ; i < 9 ; ++i) {
                System.out.println( "null replaced by " + newHand.get(i));
                hand.add(newHand.get(i));
            }
            this.hand.clear();
            this.hand.addAll(hand);
        } else {
            for(int i = 0 ; i < 9 ; ++i) {
                Card cardToReplace = this.hand.get(i);
                if ( (cardToReplace != null)  &&  !newHand.contains(cardToReplace) ) {
                    System.out.println( this.hand.get(i) + " replaced by null" );
                    this.hand.set(i, null);
                }
            }
       }
   }
    

    /**
     * @brief It is a public setter for the playableCards property given the CardSet
     *  newPlayableCards. If the new CardSet newPlayableCards is of size 9, we replace
     *  the previous PlayableCards property with the new one, otherwise we simply put to
     *  null the cards that are not in newPlayableCards but were in the playableCards
     *  attribute.
     *
     * @param newPlayableCards the new CardSet
     */
    public void setPlayableCards(CardSet newPlayableCards) {
        this.playableCards.clear();
        for(int i = 0 ; i < newPlayableCards.size() ; ++i) {
            System.out.println( "null replaced by " + newPlayableCards.get(i));
            playableCards.add(newPlayableCards.get(i));
        }
    }
}
