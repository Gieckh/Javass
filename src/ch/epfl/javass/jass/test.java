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
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_1, 52);
        pkScore = PackedScored.withAdditionalTrick(pkScore, TeamId.TEAM_2, 64);
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
