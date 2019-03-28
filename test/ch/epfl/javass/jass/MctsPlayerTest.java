package ch.epfl.javass.jass;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MctsPlayerTest {
    @Test
    void professorCaseChoosesTheRightCard() {
        Player p = new MctsPlayer3(PlayerId.PLAYER_1, 0, 100_000);

        //{♠8,♠9,♠10,♡6,♡7,♡8,♡9,♡10,♡J}
        CardSet hand = CardSet.EMPTY
                .add(Card.of(Card.Color.SPADE, Card.Rank.EIGHT))
                .add(Card.of(Card.Color.SPADE, Card.Rank.NINE))
                .add(Card.of(Card.Color.SPADE, Card.Rank.TEN))
                .add(Card.of(Card.Color.HEART, Card.Rank.SIX))
                .add(Card.of(Card.Color.HEART, Card.Rank.SEVEN))
                .add(Card.of(Card.Color.HEART, Card.Rank.EIGHT))
                .add(Card.of(Card.Color.HEART, Card.Rank.NINE))
                .add(Card.of(Card.Color.HEART, Card.Rank.TEN))
                .add(Card.of(Card.Color.HEART, Card.Rank.JACK));

        TurnState t = TurnState.initial(Card.Color.SPADE, Score.INITIAL,
                PlayerId.PLAYER_4);
        t = t.withNewCardPlayed(Card.of(Card.Color.SPADE, Card.Rank.JACK));
        System.out.println(hand);
        System.out.println(t.trick());

//        trump ♠
//        {♠J,_,_,_}
        assertEquals(Card.of(Card.Color.SPADE, Card.Rank.EIGHT), p.cardToPlay(t, hand));
    }

    @Test
    void testObvious1() {
        Player p = new MctsPlayer3(PlayerId.PLAYER_4, 0, 100_000);

        //{♢Q,♣8}
        CardSet hand = CardSet.EMPTY
                .add(Card.of(Card.Color.DIAMOND, Card.Rank.QUEEN))
                .add(Card.of(Card.Color.CLUB, Card.Rank.EIGHT));

        //{♠Q,♠A,♢8,♢Q,♢K,♢A,♣8,♣J}
        CardSet unplayedCards = CardSet.EMPTY
                .add(Card.of(Card.Color.SPADE, Card.Rank.ACE))
                .add(Card.of(Card.Color.SPADE, Card.Rank.QUEEN))
                .add(Card.of(Card.Color.DIAMOND, Card.Rank.ACE))
                .add(Card.of(Card.Color.DIAMOND, Card.Rank.KING))
                .add(Card.of(Card.Color.DIAMOND, Card.Rank.QUEEN))
                .add(Card.of(Card.Color.DIAMOND, Card.Rank.EIGHT))
                .add(Card.of(Card.Color.CLUB, Card.Rank.JACK))
                .add(Card.of(Card.Color.CLUB, Card.Rank.EIGHT));
        System.out.println(unplayedCards);

        //trump ♣
        //TurnState state = TurnState.initial(Card.Color.CLUB, Score.INITIAL, PlayerId.PLAYER_1);
        TurnState state = TurnState.ofPackedComponents(PackedScore.pack(7, 0, 0, 0, 0, 0),
                unplayedCards.packed(),
                PackedTrick.firstEmpty(Card.Color.CLUB, PlayerId.PLAYER_1));

        state = state.withNewCardPlayed(Card.of(Card.Color.DIAMOND, Card.Rank.KING))
                .withNewCardPlayed(Card.of(Card.Color.SPADE, Card.Rank.QUEEN))
                .withNewCardPlayed(Card.of(Card.Color.DIAMOND, Card.Rank.EIGHT));

        System.out.println(state.unplayedCards());
        System.out.println(state.trick());

        System.out.println(p.cardToPlay(state, hand));
        assertEquals(Card.of(Card.Color.CLUB, Card.Rank.EIGHT), p.cardToPlay(state, hand));
    }
}
