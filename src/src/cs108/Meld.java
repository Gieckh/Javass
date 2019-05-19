package src.cs108;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;


public final class Meld {
    public static List<Meld> ALL = computeAll();

    private static List<Meld> computeAll() {
        List<Meld> all = new ArrayList<>();
        addAllQuartetsInto(all);
        addAllSuitsInto(all);
        return Collections.unmodifiableList(all);
    }

    private static int quartetPoints(ch.epfl.javass.jass.Card.Rank rank) {
        switch (rank) {
        case NINE:
            return 150;
        case JACK:
            return 200;
        case TEN:
        case QUEEN:
        case KING:
        case ACE:
            return 100;
        default:
            throw new Error();
        }
    }

    private static void addAllQuartetsInto(List<Meld> melds) {
        List<Rank> ranksFrom9 = Rank.ALL.subList(Rank.NINE.ordinal(), Rank.COUNT);
        for (Rank rank: ranksFrom9) {
            Set<Card> quartet =   new HashSet<Card>();
            for (Color color: Color.ALL)
                quartet.add(Card.ALL_OF.get(color).get(rank.ordinal()));
            melds.add(new Meld(quartet, quartetPoints(rank)));
        }
    }

    private static int suitPoints(int size) {
        switch (size) {
        case 3: return 20;
        case 4: return 50;
        case 5: return 100;
        default: throw new Error();
        }
    }

    private static void addAllSuitsInto(List<Meld> melds) {
        for (Color color: Color.ALL) {
            for (int size = 3; size <= 5; size += 1) {
                List<Card> cards = Card.ALL_OF.get(color);
                for (int i1 = 0, i2 = size; i2 <= cards.size(); i1 += 1, i2 += 1) {
                    Set<Card> suit = new HashSet<Card>(cards.subList(i1, i2));
                    melds.add(new Meld(suit, suitPoints(size)));
                }
            }
        }
    }

    public static List<Meld> allIn(Collection<Card> hand) {
        List<Meld> allIn = new ArrayList<>();
        for (Meld m: ALL) {
            if (hand.containsAll(m.cards()))
                allIn.add(m);
        }
        return allIn;
    }

    private final Set<Card> cards;
    private final int points;

    private Meld(Set<Card> cards, int points) {
        if (! (0 < points))
            throw new IllegalArgumentException("invalid points: " + points);

        this.cards = Collections.unmodifiableSet(new HashSet<Card>(cards));
        this.points = points;
    }

    public Set<Card> cards() {
        return cards;
    }

    public int points() {
        return points;
    }

    @Override
    public String toString() {
        return String.format("%3d: %s", points, cards);
    }
}
