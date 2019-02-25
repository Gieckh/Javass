package ch.epfl.javass.jass;
import static ch.epfl.javass.jass.PackedScore.*;

import static ch.epfl.javass.Preconditions.*;

public final class Score {
    
    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    private long packedScore;
    public final static Score INITIAL = new Score(PackedScore.INITIAL);

    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    private Score(long packed) {
     packedScore = packed;   
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    public static Score ofPacked(long packed) throws IllegalArgumentException{
        checkArgument(isValid(packed));
        return new Score(packed);
    }
    
    public long packed() {
        return packedScore;
    }
    
    public int turnTricks(TeamId t) {
        return PackedScore.turnTricks(packedScore, t);
    }
    
    public int turnPoints(TeamId t) {
        return PackedScore.turnPoints(packedScore, t);
    }
    
    public int gamePoints(TeamId t) {
        return PackedScore.gamePoints(packedScore, t);
    }
    
    public int totalPoints(TeamId t) {
        return PackedScore.totalPoints(packedScore, t);
    }
    
    public Score withAdditionalTrick(TeamId winningTeam, int trickPoints) {
        if (trickPoints<0) {
            throw new IllegalArgumentException("your int input is negative");
        }
        else {
            return ofPacked(PackedScore
                    .withAdditionalTrick(packedScore, winningTeam, trickPoints));
        }
        
    }
    
    public Score nextTurn() {
        return ofPacked(PackedScore.nextTurn(packedScore));
    }

    
    @Override
    public boolean equals(Object thatO) {
        if (thatO == null  ||  thatO.getClass() != getClass()) { // getClass same as instance of since final ?
            return false;
        }

        Score thatOScore= (Score) thatO; // Or do 2 "conversions, idk"
            return (thatOScore.packedScore == this.packedScore);
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(packedScore);
    }
    
    @Override
    public String toString() {
        return PackedScore.toString(packedScore);
    }
}
