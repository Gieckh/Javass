package ch.epfl.javass.jass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * manages the players.
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public enum PlayerId {
    PLAYER_1  (1),
    PLAYER_2  (2),
    PLAYER_3  (3),
    PLAYER_4  (4);

    public final int type;
    public final static int COUNT = 4;
    public final static List<PlayerId> ALL =
            Collections.unmodifiableList(Arrays.asList(values()));

    // TODO : inutile pour le moment ( et pas demandé)
    PlayerId(int type) {
        this.type = type;
    }

    /** returns team 1 for players 1 and 3, and team 2 for players 2 and 4.
     * @return the team in function of the player
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
     */
    public  TeamId team() throws IllegalArgumentException{
        switch (this) {
        case PLAYER_1:
        case PLAYER_3:
            return TeamId.TEAM_1;
        case PLAYER_2:
        case PLAYER_4:
            return TeamId.TEAM_2;
        default:
            throw new IllegalArgumentException();
        }
    }
}
