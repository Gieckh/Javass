package ch.epfl.javass.jass;
import static ch.epfl.javass.jass.PackedScored.*;

import ch.epfl.javass.jass.Jass.TeamId;

import static ch.epfl.javass.Preconditions.*;

public final class Score {
    
    public final static Score INITIAL = new Score(0);
    private long packedScore;
    
    private Score(long packed) {
     packedScore = packed;   
    }
    
    public static Score ofPacked(long packed) throws IllegalArgumentException{
        checkArgument(isValid(packed));
        return new Score(packed);
    }
    
    public long packed() {
        return packedScore;
    }
    
    public int turnTricks(TeamId t) {
        return PackedScored.turnTricks(packedScore, t);
    }
    
    public int turnPoints(TeamId t) {
        return PackedScored.turnPoints(packedScore, t);
    }
    
    public int gamePoints(TeamId t) {
        return PackedScored.turnPoints(packedScore, t);
    }
    
    public int totalPoints(TeamId t) {
        return PackedScored.totalPoints(packedScore, t);    
    }
    
    public Score withAdditionalTrick(TeamId winningTeam, int trickPoints) {
        if (trickPoints<0) {
            throw new IllegalArgumentException("your int input is negative");
        }
        else {
            return ofPacked(PackedScored.withAdditionalTrick(packedScore, winningTeam, trickPoints));
        }
        
    }
    
    public Score nextTurn() {
        return ofPacked(PackedScored.nextTurn(packedScore));
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
        return PackedScored.toString(packedScore);
    }
}
