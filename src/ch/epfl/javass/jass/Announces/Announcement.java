package ch.epfl.javass.jass.Announces;

import ch.epfl.javass.jass.CardSet;

import java.util.List;

public final class Announcement {
    public static List<MeldSet> getAnnounces(CardSet hand) {
        List<MeldSet> melds = MeldSet.allIn((hand));
        melds.sort((m1, m2) -> Integer.compare(m1.points(), m2.points()));
        return melds;
        }
    }

