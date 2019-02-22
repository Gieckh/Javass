package ch.epfl.javass.jass;


import ch.epfl.javass.jass.Jass.TeamId;

public abstract class test {

    public static void main(String[] args) {
        long pkScore = PackedScored.pack(0, 0, 0, 0, 0, 0);
        System.out.println(Long.SIZE);
        System.out.println(PackedScored.turnTricks(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.turnPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.gamePoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.totalPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.turnTricks(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.turnPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.gamePoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.totalPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.toString(pkScore));
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 10);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 15);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 3);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 4);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 8);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 12);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 21);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 1);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 0);
        System.out.println(PackedScored.turnTricks(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.turnPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.gamePoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.totalPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.turnTricks(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.turnPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.gamePoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.totalPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.toString(pkScore));
        pkScore = PackedScored.nextTurn(pkScore);
        System.out.println(PackedScored.turnTricks(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.turnPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.gamePoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.totalPoints(pkScore, TeamId.TEAM_1));
        System.out.println(PackedScored.turnTricks(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.turnPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.gamePoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.totalPoints(pkScore, TeamId.TEAM_2));
        System.out.println(PackedScored.toString(pkScore));
    }

}
