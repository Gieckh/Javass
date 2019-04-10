package ch.epfl.javass.jass;
import java.util.SplittableRandom;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import ch.epfl.javass.gui.HandBean;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import ch.epfl.javass.net.StringSerializer;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) throws Base64DecodingException {
        HandBean hb = new HandBean();
        SetChangeListener<Card> listener = e -> System.out.println(e);
        hb.playableCards().addListener(listener);

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
        hb.setPlayableCards(h);
        while (! h.isEmpty()) {
          h = h.remove(h.get(0));
          hb.setPlayableCards(h);
        }
    }
}
