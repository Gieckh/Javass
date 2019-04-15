package ch.epfl.javass.gui;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableSet;
import static javafx.collections.FXCollections.unmodifiableObservableList;
import static javafx.collections.FXCollections.unmodifiableObservableSet;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

// TODO : voir les TODO plus bas; il y a 2-3 ptites questions à poser à michmich , 
//mais rien d'urgent je peux faire ca lundi
// cette classe a été testée en theorie ; ce n'est pas le cas des 2 autres

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
     *  with the new one, overwise we simply put to null the cards that are not in newHand 
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
                if( (this.hand.get(i) != null) && !newHand.contains(this.hand.get(i)) ) {
                    System.out.println( this.hand.get(i) + " replaced by null " );
                    this.hand.remove(i);
                    this.hand.add(i, null);
                }
            }
       }
   }
    

    /**
     * @brief It is a public setter for the playableCards property given the CardSet
     *  newPlayableCards. If the new CardSet newPlayableCards is of size 9, we replace
     *  the previous PlayableCards property with the new one, overwise we simply put to 
     *  null the cards that are not in newPlayableCards but were in the playableCards
     *  attribute.
     *
     * @param newPlayableCards the new CardSet
     */
    public void setPlayableCards(CardSet newPlayableCards) {
        ObservableSet<Card> playableCards = observableSet();
        if(newPlayableCards.size()==9) {
            for(int i = 0 ; i < 9 ; ++i) {
                System.out.println( "null replaced by " + newPlayableCards.get(i));
                playableCards.add(newPlayableCards.get(i));
            }
            this.playableCards.clear();
            this.playableCards.addAll(playableCards);
        }else {
            ObservableSet<Card> delete = observableSet();
          for(Card c : this.playableCards) {
              if(!newPlayableCards.contains(c)) {
                  System.out.println( c + " replaced by null " );
                  delete.add(c);
              }
          }
          this.playableCards.removeAll(delete);
        }
    }
}
