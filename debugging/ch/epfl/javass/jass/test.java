package ch.epfl.javass.jass;

import bonus.JassReductorOfSet;
import ch.epfl.javass.gui.HandBean;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) {
       Trick coucou = Trick.firstEmpty(Color.CLUB, PlayerId.PLAYER_1);
       coucou = coucou.withAddedCard(Card.of(Color.DIAMOND, Rank.KING));
       coucou = coucou.withAddedCard(Card.of(Color.HEART,Rank.ACE));
       coucou= coucou.withAddedCard(Card.of(Color.CLUB, Rank.QUEEN));
       coucou = coucou.withAddedCard(Card.of(Color.CLUB, Rank.SIX));
       System.out.println(JassReductorOfSet.CardsThePlayerHavnt(coucou));
       
    }
}
