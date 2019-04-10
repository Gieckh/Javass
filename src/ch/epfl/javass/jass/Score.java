package ch.epfl.javass.jass;
import static ch.epfl.javass.Preconditions.checkArgument;

/**
 * The class used to represent a Score.
 * A Score indicates, for each team:
 *     - the number of tricks they've won this turn,
 *     - the number of points they've won this turn,
 *     - the number of points they've won this game (current turn not included)
 *     - the number of points they've won this game (current turn included)
 *       [this is just the sum of the 2 previous points].
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class Score {
    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    private final long packedScore;
    public final static Score INITIAL = new Score(PackedScore.INITIAL);

    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /**
     * @brief PRIVATE constructor of the class Score. Called by the method
     *        "ofPacked(int packed)".
     *
     * @param packed (long) an encoded score
     */
    private Score(long packed) {
        packedScore = packed;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @brief Create a new instance of Score from the chosen long "packed".
     * 
     * @param packed (long) - the given [packed] score
     * @return a new Card with the chosen int "packed"
     * @throws IllegalArgumentException if the specified [packed] score is invalid
     */
    public static Score ofPacked(long packed) {
        checkArgument(PackedScore.isValid(packed));
        return new Score(packed);
    }
    
    /**
     * @brief The packed version of this score.
     * 
     * @return (long) the packedScore of this card
     */
    public long packed() {
        return packedScore;
    }
    
    /**
     * @brief The number of tricks the team "t" won during the current turn.
     *
     * @param t (TeamId) The team we're interested in.
     * @return (int) How many tricks this team won during this turn
     */
    public int turnTricks(TeamId t) {
        return PackedScore.turnTricks(packedScore, t);
    }
    
    /**
     * @brief the number of points won in the current turn in function of the team.
     *
     * @param t (TeamId) the team we're interested in.
     * @return (int) the number of points won in the current turn
     */
    
    public int turnPoints(TeamId t) {
        return PackedScore.turnPoints(packedScore, t);
    }
    
    /**
     * @brief the number of points the team won in the whole game, <em>except</em> the current turn.
     *
     * @param t, the TeamId
     * @return int : the total number of points won depending on the team
     */
    public int gamePoints(TeamId t) {
        return PackedScore.gamePoints(packedScore, t);
    }
    
    /**
     * returns the total number of points won in the whole game.
     *
     * @param t, the TeamId
     * @return (int): the total number of points won depending on the team
     */
    public int totalPoints(TeamId t) {
        return PackedScore.totalPoints(packedScore, t);
    }
    
    /**
     * @brief updates this pkScore, knowing that the team "winningTeam" just won
     *        a trick of value "trickPoints". takes into consideration the <em>additional trick</em>,
     *        but <em>not</em> the <em>last trick</em> bonus.
     *
     * @param winningTeam (TeamId) the team winning the points
     * @param trickPoints (int) the value of the won trick
     * @return (long) this updated pkScore.
     * @throws IllegalArgumentException if the trickPoints are < 0.
     */
    public Score withAdditionalTrick(TeamId winningTeam, int trickPoints) {
        checkArgument(trickPoints >= 0);
        return ofPacked(PackedScore.withAdditionalTrick(packedScore, winningTeam, trickPoints));
    }
    
    /**
     * @brief a long with global points updated with adding the currents points,
     * and 0 as number of Tricks won and 0 current points for both teams.
     *
     * @return a new long with the data updated as it becomes next turn
     */
    public Score nextTurn() {
        return ofPacked(PackedScore.nextTurn(packedScore));
    }

    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object thatO) {
        if (!(thatO instanceof Score))
            return false;

        Score thatOScore = (Score) thatO;
        return (thatOScore.packedScore == this.packedScore);
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Long.hashCode(packedScore);
    }
    
    /**
     * @brief a String with all the information packed in pkScore. Takes the form:
     *        (trickThisTurn1, pointsThisTurn1, pointsThisGame1)/(trickThisTurn2, pointsThisTurn2, pointsThisGame2)
     *        where the first set of parenthesis corresponds to "Team1", and
     *        the second to "Team2".
     *
     * @return (String) the String representation of this Score
     */
    @Override
    public String toString() {
        return PackedScore.toString(packedScore);
    }
}
