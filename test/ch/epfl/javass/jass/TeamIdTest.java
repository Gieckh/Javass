package ch.epfl.javass.jass;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class TeamIdTest {
    @Test
    void teamIdTeamsAreCorrect() throws Exception {
        TeamId team1 = TeamId.TEAM_1;
        TeamId team2 = TeamId.TEAM_2;
        
        assertEquals(TeamId.TEAM_2, team1.other());
        assertEquals(TeamId.TEAM_1, team2.other());
    }
   

        private static TeamId[] getAllTeamIds() {
            return new TeamId[] {
                    TeamId.TEAM_1,
                    TeamId.TEAM_2,
            };
        }

        @Test
        void teamIdsAreInRightOrder() {
            assertArrayEquals(getAllTeamIds(), TeamId.values());
        }

        @Test
        void teamIdAllIsCorrect() {
            assertEquals(Arrays.asList(TeamId.values()), TeamId.ALL);
        }

        @Test
        void teamIdAllIsUnmodifiable() {
            assertThrows(UnsupportedOperationException.class, () -> {
                TeamId.ALL.set(0, null);
            });
        }

        @Test
        void teamIdCountIsCorrect() {
            assertEquals(getAllTeamIds().length, TeamId.COUNT);
        }

        @Test
        void otherIsCorrect() {
            assertEquals(TeamId.TEAM_2, TeamId.TEAM_1.other());
            assertEquals(TeamId.TEAM_1, TeamId.TEAM_2.other());
        }
    }



