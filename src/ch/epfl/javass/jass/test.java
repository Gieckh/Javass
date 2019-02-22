package ch.epfl.javass.jass;


import ch.epfl.javass.jass.TeamId;

public abstract class test {
//
    public static void main(String[] args) {
        long pkScore = PackedScore.pack(2, 16, 32, 1, 64, 256);
        System.out.println(Long.SIZE);
        System.out.println(PackedScore.turnTricks(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.turnPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.gamePoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.totalPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.turnTricks(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.turnPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.gamePoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.totalPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.toString(pkScore));
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 10);
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 15);
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 3);
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 4);
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 8);
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 12);
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 21);
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 1);
        pkScore = PackedScore.withAdditionalTrick(pkScore, TeamId.TEAM_1, 0);
        System.out.println(PackedScore.turnTricks(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.turnPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.gamePoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.totalPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.turnTricks(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.turnPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.gamePoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.totalPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.toString(pkScore));
        pkScore = PackedScore.nextTurn(pkScore);
        System.out.println(PackedScore.turnTricks(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.turnPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.gamePoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.totalPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScore.turnTricks(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.turnPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.gamePoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.totalPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScore.toString(pkScore));
    }

}
