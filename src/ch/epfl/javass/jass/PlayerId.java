package ch.epfl.javass.jass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * PlayerId
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public enum PlayerId {
    PLAYER_1,
    PLAYER_2,
    PLAYER_3,
    PLAYER_4;

    public final static int COUNT = 4;
    public final static List<PlayerId> ALL =
            Collections.unmodifiableList(Arrays.asList(values()));

    /**
     * @brief Indicates a player's team.
     *
     * @return (TeamId) TEAM_1 for players 1 and 3
     *                  TEAM_2 for players 2 and 4.
     *
     */
    public TeamId team() {
        switch (this) {
            case PLAYER_1:
            case PLAYER_3:
                return TeamId.TEAM_1;
            case PLAYER_2:
            case PLAYER_4:
                return TeamId.TEAM_2;
            default: // unreachable statement
                throw new Error();
        }
    }
}
