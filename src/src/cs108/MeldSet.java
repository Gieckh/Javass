package src.cs108;

import static java.util.Collections.unmodifiableSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;

public final class MeldSet {
    private final Set<Meld> melds;

    public static boolean mutuallyDisjoint(Collection<Meld> melds) {
        List<Set<Card>> allSetsOfCards = new ArrayList<>();
        for (Meld m: melds)
            allSetsOfCards.add(m.cards());
        return Sets.mutuallyDisjoint(allSetsOfCards);
    }

    public  long[] packed() {
        long packed[] = new long[2];
        int i= -1;
        int correction = 0;
        //long shift = 1;
        for(Meld m : Meld.ALL) {
            i=i+1;
        //    shift<<=1;
          //  System.out.println(Long.toBinaryString(packed[0]));
            if (melds.contains(m)) {
                if((int)i/64 == 0) {
                    correction=i;
                }
                packed[i/64]+=1;
            }
            packed[i/64] <<= 1;
        }
        packed[0]>>= (64 - correction);
        return packed;
    }
    
    public static MeldSet from(long packed[] ) {
        int size = Meld.ALL.size();
       // System.out.println(size);
        Meld[] allMeldsInversed = new Meld[size];
        List<Meld> melds = new ArrayList<>();
        int i = 0;
        for(Meld m : Meld.ALL) {
            allMeldsInversed[--size]= m;
        }
        for(Meld m : allMeldsInversed) {
            if((packed[i/64] & 1l )== 1) {
              //  System.out.println( i + "  " +  (int)i/64);
                melds.add(m);
            }
            packed[i/64]>>= 1;
            i++;

        }
        return new MeldSet(melds);
    }
    
    public static List<MeldSet> allIn(CardSet hand) {
        System.out.println(hand);
        List<MeldSet> r = new ArrayList<>();
        for (Set<Meld> melds: Sets.powerSet(Meld.allIn(hand))) {
            System.out.println("boucle ic ?");
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
