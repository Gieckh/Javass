package ch.epfl.javass.gui;

import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableSet;

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

    //default constructor per default is enough

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    public ObservableList<Card> hand() {
        return hand;
    }
    
    public ObservableSet<Card> playableCards() {
        return playableCards; 
    }
    
    //TODO : why isn't the test of the prof printing correctly ? (althought works)
    public void setHand(CardSet newHand) {
        // TODO je suis un peu embêté de pas la mettre en unmodifiable list, mais ca me semble pas une 
        // bonne idée puisque justement on modifie la liste dans le cas ou newHand.size != 9.
        ObservableList<Card> hand = observableArrayList();
        if(newHand.size()==9) {
            for(int i = 0 ; i < 9 ; ++i) {
                System.out.println( "null replaced by " + newHand.get(i));
                hand.add(newHand.get(i));
            }
            this.hand = hand;
        }else {
          for(int i = 0 ; i < 9 ; ++i) {
              if((this.hand.get(i)!=null) && (!newHand.contains(this.hand.get(i)))) {
                  System.out.println( this.hand.get(i) + " replaced by null " );
                  this.hand.remove(i);
                  this.hand.add(i, null);
              }
          }
       }
   }
    

    
    public void setPlayableCards(CardSet newPlayableCards) {
        // TODO je suis un peu embêté de pas la mettre en unmodifiable set, mais ca me semble pas une 
        // bonne idée puisque justement on modifie le set dans le cas ou newHand.size != 9.
        ObservableSet<Card> playableCards = observableSet();
        if(newPlayableCards.size()==9) {
            for(int i = 0 ; i < 9 ; ++i) {
                System.out.println( "null replaced by " + newPlayableCards.get(i));
                playableCards.add(newPlayableCards.get(i));
            }
            this.playableCards = playableCards;
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
