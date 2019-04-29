package ch.epfl.javass.jass;

import ch.epfl.javass.gui.HandBean;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) {
        HandBean hb = new HandBean();
        ListChangeListener<Card> listener = e -> System.out.println(e);
        hb.hand().addListener(listener);

        CardSet h = CardSet.EMPTY
          .add(Card.of(Color.SPADE, Rank.SIX))
          .add(Card.of(Color.SPADE, Rank.NINE))
          .add(Card.of(Color.SPADE, Rank.JACK))
          .add(Card.of(Color.HEART, Rank.SEVEN))
          .add(Card.of(Color.HEART, Rank.ACE))
          .add(Card.of(Color.DIAMOND, Rank.KING))
          .add(Card.of(Color.DIAMOND, Rank.ACE))
          .add(Card.of(Color.CLUB, Rank.TEN))
          .add(Card.of(Color.CLUB, Rank.QUEEN));
        hb.setHand(h);
        while (! h.isEmpty()) {
          h = h.remove(h.get(0));
          hb.setHand(h);
        }
//        ObjectProperty<String> p1 = new SimpleObjectProperty<>();
//        ObjectProperty<String> p2 = new SimpleObjectProperty<>();
//
//        p1.addListener((o, oV, nV) -> System.out.println(nV));
//        p1.addListener((o, oV, nV) -> System.out.println(oV));
//        p1.addListener((o, oV, nV) -> System.out.println(""));
//
//        p2.set("hello");                // n'affiche rien
//        System.out.println("--- binding p1 to p2");
//        p1.bindBidirectional(p2);       // affiche "hello"
//        p2.set("world");                // affiche "world"
//        p1.set("bonjour");
    }
}
