package src.cs108;

import static java.util.Collections.unmodifiableSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import ch.epfl.javass.jass.Card;

public final class MeldSet {
    private final Set<Meld> melds;

    public static boolean mutuallyDisjoint(Collection<Meld> melds) {
        List<Set<Card>> allSetsOfCards = new ArrayList<>();
        for (Meld m: melds)
            allSetsOfCards.add(m.cards());
        return Sets.mutuallyDisjoint(allSetsOfCards);
    }

    public static List<MeldSet> allIn(Collection<Card> hand) {
        List<MeldSet> r = new ArrayList<>();
        for (Set<Meld> melds: Sets.powerSet(Meld.allIn(hand))) {
            if (mutuallyDisjoint(melds))
                r.add(new MeldSet(melds));
        }
        return r;
    }

    public static MeldSet of(Collection<Meld> melds) {
        if (! mutuallyDisjoint(melds))
            throw new IllegalArgumentException();
        return new MeldSet(melds);
    }

    private MeldSet(Collection<Meld> melds) {
        this.melds = unmodifiableSet(new HashSet<>(melds));
    }

    public int points() {
        int points = 0;
        for (Meld m: melds)
            points += m.points();
        return points;
    }

    @Override
    public String toString() {
        StringJoiner s = new StringJoiner(", ", "{", "}");
        for (Meld m: melds)
            s.add(m.cards().toString());
        return String.format("%3d: %s", points(), s);
    }
}
