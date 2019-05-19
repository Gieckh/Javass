package src.cs108;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import ch.epfl.javass.jass.Card;

class MeldSetByPointsComparator implements Comparator<MeldSet> {
    @Override
    public int compare(MeldSet m1, MeldSet m2) {
        return Integer.compare(m1.points(), m2.points());
    }
}

public final class Example {
    public static void main(String[] args) {

        List<Card> hand = Card.ALL_OF.get(Card.Color.CLUB);

        System.out.printf("%nLes ensembles d'annonces valides parmi celles ci-dessus sont :%n");
        List<MeldSet> melds = MeldSet.allIn(hand);
        melds.sort(new MeldSetByPointsComparator());
         int i =0 ; 
        for (MeldSet m: melds) {
            System.out.printf(i++ + "  %s%n", m);
        }
    }
}
