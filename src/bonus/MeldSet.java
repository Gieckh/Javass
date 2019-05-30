package bonus;

import static java.util.Collections.unmodifiableSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.PackedCardSet;

public final class MeldSet {
    private final Set<Meld> melds;

    public final static MeldSet EMPTY_SET= MeldSet.allIn(CardSet.EMPTY).get(0);
    
    public static boolean mutuallyDisjoint(Collection<Meld> melds) {
        List<Set<Card>> allSetsOfCards = new ArrayList<>();
        for (Meld m: melds)
            allSetsOfCards.add(m.cards());
        return Sets.mutuallyDisjoint(allSetsOfCards);
    }
    
    /**
     * @Brief BONUS : returns the set of cards contained in this meldSet.
     *
     * @return the set of cards contained in the meldSet
    */
    public CardSet cards() {
        long cardSet = 0l;
        for (Meld m : melds) {
            List<Card> listOfCards = new ArrayList<>();
            listOfCards.addAll(m.cards());
            cardSet = PackedCardSet.union(cardSet, CardSet.of(listOfCards).packed());
        }
        return CardSet.ofPacked(cardSet);
    }
    
    /**
     * @Brief pack this meldSet into an array of long. 
     * The main interest is for the remote player.
     *  
     *
     * @return an array of long.
    */
    public  long[] packed() {
        long packed[] = new long[2];
        for(int i =0 ; i< Meld.ALL.size() ; ++i) {
            Meld m=Meld.ALL.get(i);
            if (melds.contains(m)) {
                packed[i/64]+=((long)1<<(i<64? 63 : 13));
            }
            if(i<63) packed[0] >>>= 1;
            else if(i<77) packed[1] >>>= 1;
        }
        return packed;
    }
    
    /**
     * @Brief returns the meldSet corresponding to the long array.
     *
     * @param packed an array of long
     * @return a meldSet
    */
    public static MeldSet from(long packed[] ) {
        List<Meld> melds = new ArrayList<>();
       for(int i =0 ; i<Meld.ALL.size() ; ++i){
           if((packed[i/64]&((long)1<<(i%64)))==((long)1<<(i%64))) {
               melds.add(Meld.ALL.get(i));
           }
       }
        return new MeldSet(melds);
    }
    
    
    /**
     * @Brief BONUS : returns the List of all the possible MeldSet from a given hand.
     *
     * @param hand a cardSet
     * @return the List of all the possible MeldSet from a given hand.
    */
    public static List<MeldSet> allIn(CardSet hand) {
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
