package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits64;

import static ch.epfl.javass.bits.Bits64.extract;

/**
 * manipulates scores of a jass game
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class PackedScore {

    public final static long INITIAL = 0;

    private final static int TEAM_INFO_SIZE = 32;
    private final static int TEAM_ONE_START = 0;
    private final static int TEAM_TWO_START = TEAM_INFO_SIZE;

    private final static int TRICKS_START = 0;
    private final static int TRICKS_SIZE = 4;

    private final static int POINTS_PER_TURN_START = 4;
    private final static int POINTS_PER_TURN_SIZE = 9;

    private final static int POINTS_PER_GAME_SIZE = 11;
    private final static int POINTS_PER_GAME_START = TRICKS_SIZE + POINTS_PER_TURN_SIZE;

    private final static int EMPTY_BIT_SIZE = 8;


    private final static int MAX_TRICKS_PER_TURN = 9;
    private final static int MAX_POINTS_PER_TURN = 257;
    private final static int MAX_POINTS_PER_GAME = 2000;


    //so the class is not instantiable
    //TODO do same for some other classes
    private PackedScore() {};

    /**
     * returns true if pkScore is packed correctly.
     *
     * @param  pkScore (long) the long encoding the points and tricks of the game
     * @return true if pkScore is packed correctly
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static boolean isValid(long pkScore) {

       return(
              //TODO: suppr coms on the right ?
              extract(pkScore, TRICKS_START, TRICKS_SIZE) <= MAX_TRICKS_PER_TURN && // number of tricks is valid (<=10)
              extract(pkScore, POINTS_PER_TURN_START, POINTS_PER_TURN_SIZE) <= MAX_POINTS_PER_TURN && // number of points per turn is valid (<258)
              extract(pkScore, POINTS_PER_GAME_START, POINTS_PER_GAME_SIZE + EMPTY_BIT_SIZE) <= MAX_POINTS_PER_GAME && // number of points of the game is valid (<=2000)
              //We want only 0 from the 24th to the 31th bit thus the "+ EMPTY_BIT_SIZE"
              extract(pkScore, TEAM_INFO_SIZE, TRICKS_SIZE) <= MAX_TRICKS_PER_TURN && // number of tricks is valid (<=9)
              extract(pkScore, TEAM_TWO_START + POINTS_PER_TURN_START, POINTS_PER_TURN_SIZE) <= MAX_POINTS_PER_TURN && // number of points per turn is valid (<258)
              extract(pkScore, TEAM_TWO_START + POINTS_PER_GAME_START, POINTS_PER_GAME_SIZE + EMPTY_BIT_SIZE) <= MAX_POINTS_PER_GAME ); // number of points of the game is valid (<=000)
              //We want only 0 from the 56th to the 63th bit thus the "+ EMPTY_BIT_SIZE"
    }

    /**
     * returns the packed long with all of these information packed on 64bits.
     *
     * @param turnTricks1 (team1)
     * @param turnPoints1 (team1)
     * @param gamePoints1 (team1)
     * @param turnTricks2 (team2)
     * @param turnPoints2 (team2)
     * @param gamePoints2 (team2)
     * @return the packed long with all of these information packed on 64bits
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static long //TODO: pas comme Antoine a fait - Sami Abuzakuk
    pack (int turnTricks1, int turnPoints1, int gamePoints1,
          int turnTricks2, int turnPoints2, int gamePoints2)
    {
        long team1 = Bits64.pack(turnTricks1, TRICKS_SIZE, turnPoints1, POINTS_PER_TURN_SIZE, gamePoints1, POINTS_PER_GAME_SIZE);
        long team2 = Bits64.pack(turnTricks2, TRICKS_SIZE, turnPoints2, POINTS_PER_TURN_SIZE, gamePoints2, POINTS_PER_GAME_SIZE);

        return Bits64.pack(team1, TEAM_INFO_SIZE, team2, TEAM_INFO_SIZE);
    }


    /**
     * @brief The number of tricks the team "t" won during the current turn.
     *
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param t (TeamId) The team we're interested in.
     * @return (int) How many tricks this team won during this turn
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int turnTricks(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);

        int shift = (t == TeamId.TEAM_1) ? TEAM_ONE_START : TEAM_TWO_START;
        return (int) extract(pkScore, shift, TRICKS_SIZE );
    }


    /**
     * @brief the number of points won in the current turn in function of the team.
     *
     * @param pkScore (long) the long encoding the scores
     * @param t (TeamId) the team we're interested in.
     * @return int : the number of points won in the current turn
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int turnPoints(long pkScore, TeamId t)  {
        assert isValid(pkScore);

        int shift = (t == TeamId.TEAM_1) ? TEAM_ONE_START : TEAM_TWO_START;

        return (int) extract(pkScore, shift + TRICKS_SIZE, POINTS_PER_TURN_SIZE);
    }

    /**
     * returns the number of points the team won in the whole game, except the current turn.
     *
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param t, the TeamId
     * @return int : the total number of points won depending on the team
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int gamePoints(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);

        int shift = (t == TeamId.TEAM_1) ? TEAM_ONE_START : TEAM_TWO_START;
        return (int) extract(pkScore, shift + POINTS_PER_GAME_START, POINTS_PER_GAME_SIZE);
    }

    /**
     * returns the total number of points won in the whole game except the current turn.
     *
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param t, the TeamId
     * @return (int): the total number of points won depending on the team
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int totalPoints(long pkScore, TeamId t) {
        assert isValid(pkScore);

        int shift = (t == TeamId.TEAM_1) ? TEAM_ONE_START : TEAM_TWO_START;
        return (int) (
                extract(pkScore, shift + POINTS_PER_GAME_START, POINTS_PER_GAME_SIZE ) +
                extract(pkScore, shift + POINTS_PER_TURN_START, POINTS_PER_TURN_SIZE )
        );
    }

    /**
     * returns a new long with one new trick won for the winningTeam, and the corresponding points
     *
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param winningTeam
     * @param trickPoints
     * @return the updated pkScore, taking in consideration a team just won a trick thus some points
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static long
    withAdditionalTrick(long pkScore, TeamId winningTeam, int trickPoints) {
        int shift = (winningTeam == TeamId.TEAM_1) ? TEAM_ONE_START : TEAM_TWO_START;
        int numberOfTricksOfWinningTeam = (int) extract(pkScore, shift, TRICKS_SIZE);
        if (numberOfTricksOfWinningTeam == MAX_TRICKS_PER_TURN - 1) {
            trickPoints += Jass.MATCH_ADDITIONAL_POINTS;
        }

        long trickPointsL = trickPoints;
        trickPointsL <<= (shift + POINTS_PER_TURN_START);
        long wonTrick = 1L << (shift + TRICKS_START);

        return (pkScore + trickPointsL + wonTrick);
    }


    /**
     * returns a long with global points updated with adding the currents points,
     * and 0 as number of Tricks won and 0 current points for both teams.
     *
     * @param  pkScore (long) the long encoding the points and tricks of the game
     * @return a new long with the datas updated as it becomes next turn
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static long nextTurn(long pkScore) { //TODO: moins empaqueter.
        assert isValid(pkScore);

        int turnPointsOf1 = (int) extract(pkScore, POINTS_PER_TURN_START, POINTS_PER_TURN_SIZE);
        int turnPointsOf2 = (int) extract(pkScore, TEAM_TWO_START + POINTS_PER_TURN_START, POINTS_PER_TURN_SIZE);

        int previousGlobalPointsOf1 = (int) extract(pkScore, POINTS_PER_GAME_START, POINTS_PER_GAME_SIZE);
        int previousGlobalPointsOf2 = (int) extract(pkScore, TEAM_TWO_START + POINTS_PER_GAME_START, POINTS_PER_GAME_SIZE);

        return pack(0, 0, turnPointsOf1 + previousGlobalPointsOf1, 0, 0, turnPointsOf2+previousGlobalPointsOf2);
    }


    /**
     * returns a paragraph with all informations packed in pkScore
     *
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @return the String with all informations about points and tricks of both teams
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static String toString(long pkScore) {
        assert isValid(pkScore);
        int tricksOf1 = turnTricks(pkScore, TeamId.TEAM_1);
        int tricksOf2 = turnTricks(pkScore, TeamId.TEAM_2);

        int turnPointsOf1 = turnPoints(pkScore, TeamId.TEAM_1);
        int turnPointsOf2 = turnPoints(pkScore, TeamId.TEAM_2);

        int totalPointsOf1 = totalPoints(pkScore, TeamId.TEAM_1);
        int totalPointsOf2 = totalPoints(pkScore, TeamId.TEAM_2);

        String string1 = "(" + tricksOf1 + ", " + turnPointsOf1 + ", " + totalPointsOf1 + ")";
        String string2 = "(" + tricksOf2 + ", " + turnPointsOf2 + ", " + totalPointsOf2 + ")";

        return string1 + "/" + string2;
    }
}
