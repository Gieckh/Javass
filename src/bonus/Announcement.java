package bonus;

import java.util.Comparator;
import java.util.List;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;

 class MeldSetByPointsComparator implements Comparator<MeldSet> {
    @Override
    public int compare(MeldSet m1, MeldSet m2) {
        return Integer.compare(m1.points(), m2.points());
    }
}

public final class Announcement {
    
    
    public static List<MeldSet> getAnnounces(CardSet hand) {
        List<MeldSet> melds = MeldSet.allIn((hand));
        melds.sort(new MeldSetByPointsComparator());
        return melds;
        }
    }

