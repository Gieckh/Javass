package ch.epfl.javass.jass;


import ch.epfl.javass.jass.TeamId;

import java.util.Map;

import static ch.epfl.javass.jass.TeamId.TEAM_1;
import static ch.epfl.javass.jass.TeamId.TEAM_2;

public class test {
    public static void main(String[] args) {
        long s = PackedScore.INITIAL;
        System.out.println(PackedScore.toString(s));
        for (int i = 0; i < Jass.TRICKS_PER_TURN; ++i) {
            int p = (i == 0 ? 13 : 18);
            TeamId w = (i % 2 == 0 ? TEAM_1 : TEAM_2);
            s = PackedScore.withAdditionalTrick(s, w, p);
            System.out.println(PackedScore.toString(s));
        }
        s = PackedScore.nextTurn(s);
        System.out.println(PackedScore.toString(s));
    }
}
