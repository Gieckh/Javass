package ch.epfl.javass.jass;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @brief This enum is used to distinguish the players.
 * @see TeamId
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public enum PlayerId {
    PLAYER_1 (TeamId.TEAM_1),
    PLAYER_2 (TeamId.TEAM_2),
    PLAYER_3 (TeamId.TEAM_1),
    PLAYER_4 (TeamId.TEAM_2);

    public final static int COUNT = 4;
    public final static List<PlayerId> ALL =
            Collections.unmodifiableList(Arrays.asList(values()));

    private final TeamId team;

    //A player belongs to a team.
    private PlayerId(TeamId team) {
        this.team = team;
    }


    /**
     * @brief Indicates a player's team.
     *
     * @return (TeamId) TEAM_1 for players 1 and 3
     *                  TEAM_2 for players 2 and 4.
     */
    public TeamId team() {
        return team;
    }
}
