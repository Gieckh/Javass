package ch.epfl.javass.jass;

import java.util.Arrays;
import java.util.SplittableRandom;

import ch.epfl.javass.Preconditions;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;



public class mctsplayerWorksTest {
    public static void main(String[] args) {
        CardSet hand = CardSet.EMPTY
                .add(Card.of(Color.SPADE, Rank.EIGHT))
                .add(Card.of(Color.SPADE, Rank.NINE))
                .add(Card.of(Color.SPADE, Rank.TEN))
                .add(Card.of(Color.HEART, Rank.SIX))
                .add(Card.of(Color.HEART, Rank.SEVEN))
                .add(Card.of(Color.HEART, Rank.EIGHT))
                .add(Card.of(Color.HEART, Rank.NINE))
                .add(Card.of(Color.HEART, Rank.TEN))
                .add(Card.of(Color.HEART, Rank.JACK));
        Player p = new MctsPlayer(PlayerId.PLAYER_1, 0, 100_000);
        TurnState t = TurnState.initial(Color.SPADE, Score.INITIAL,
                PlayerId.PLAYER_4);
        t = t.withNewCardPlayed(Card.of(Color.SPADE, Rank.JACK));
//        System.out.println(t.trick().playableCards(hand));
        System.out.println(p.cardToPlay(t, hand));

    }
}

