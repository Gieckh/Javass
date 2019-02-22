package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits64;
import ch.epfl.javass.jass.Jass.TeamId;

import static ch.epfl.javass.bits.Bits64.extract;

/** manipulates scores of a jass game
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class PackedScored {
    
    public final static long INITIAL = 0;
    public final static int CODED_TRICKS_SIZE = 4;
    public final static int CODED_POINTS_PER_TURN_SIZE = 9;
    public final static int CODED_POINTS_PER_GAME_SIZE = 11;
    public final static int CODED_EMPTY_BIT_SIZE = 8;
    public final static int CODED_ONE_TEAM_INFO_SIZE = 32;
    public final static int MAX_TRICKS_PER_TURN = 9;
    public final static int MAX_POINTS_PER_TURN = 257;
    public final static int MAX_POINTS_PER_GAME = 2000;
    
    //so the class is not instanciable
    //TODO do same for some other classes
    private PackedScored() {};
    
    /** returns true if pkScore is packed correctly.
     * @param  pkScore (long) the long encoding the points and tricks of the game
     * @return true if pkScore is packed correctly
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static boolean isValid(long pkScore) {
       return(extract(pkScore,24,8) == 0 && //We want only 0 from the 24th to the 31th bit
              extract(pkScore,56,8) == 0 && //We want only 0 from the 56th to the 63th bit
              extract(pkScore,0,4) < 10 && // the number of tricks is valid (<10)
              extract(pkScore,4,9) < 258 && // the number of points per turn is valid (<258)
              extract(pkScore,13,11) < 2001 && // the number of points of the game is valid (<2000)
              extract(pkScore,32,4) < 10 && // the number of tricks is valid (<10)
              extract(pkScore,36,9) < 258 && // the number of points per turn is valid (<258)
              extract(pkScore,45,11) < 2001 ); // the number of points of the game is valid (<2000)
              
    }
    
    /** returns the packed long with all of these informations packed on 64bits.
     * @param turnTricks1 (team1)
     * @param turnPoints1 (team1)
     * @param gamePoints1 (team1)
     * @param turnTricks2 (team2)
     * @param turnPoints2 (team2)
     * @param gamePoints2 (team2)
     * @return the packed long with all of these informations packed on 64bits
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static long pack(int turnTricks1, int turnPoints1, int gamePoints1,
            int turnTricks2, int turnPoints2, int gamePoints2) {
        long lsb= Bits64.pack(turnTricks1, 4, turnPoints1, 9, gamePoints1, 11);
        long msb= Bits64.pack(turnTricks2, 4, turnPoints2, 9, gamePoints2, 11);
        return Bits64.pack(lsb, 32, msb, 32);       
        }
    
    
    /** returns the number of tricks won in function of the team.
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param t, the TeamId
     * @return int : the number of tricks won 
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int turnTricks(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);
        if(t.ordinal() == 0) {
            return (int) extract( pkScore , 0 , 4 );
        }
        if(t.ordinal() == 1) {
            return (int) extract( pkScore , 32 , 4 );
        }
        else {
            throw new IllegalArgumentException("Bad Team Input");
        }
    }
    
    
    /** returns the number of points won in the current turn in function of the team.
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param t, the TeamId
     * @return int : the number of points won in the current turn 
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int turnPoints(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);
        if(t.ordinal() == 0) {
            return (int) extract( pkScore , 4 , 9 );
        }
        if(t.ordinal() == 1) {
            return (int) extract( pkScore , 36 ,9 );
        }
        else {
            throw new IllegalArgumentException("Bad Team Inmput");
        }
    }
    
    /** returns the number of points won in the whole game except the current turn. 
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param t, the TeamId
     * @return int : the total number of points won depending on the team 
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int gamePoints(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);
        if(t.ordinal() == 0) {
            return (int) extract( pkScore , 13 , 11 );
        }
        if(t.ordinal() == 1) {
            return (int) extract( pkScore , 45 , 11 );
        }
        else {
            throw new IllegalArgumentException("Bad Team Inmput");
        }
    }
    
    /** returns the total number of points won in the whole game except the current turn. 
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param t, the TeamId
     * @return int : the total number of points won depending on the team 
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int totalPoints(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);
        if(t.ordinal() == 0) {
            return (int) (extract( pkScore , 13 , 11 ) + extract( pkScore, 4 , 9 ));
        }
        if(t.ordinal() == 1) {
            return (int) (extract( pkScore , 45 , 11 ) + extract( pkScore, 4 , 9 ));
        }
        else {
            throw new IllegalArgumentException("Bad Team Inmput");
        }
    }
    
    /** returns a new long with one new trick won for the winningTeam, and the corresponding points
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @param winningTeam
     * @param trickPoints
     * @return the updated pkScore, taking in consideration a team just won a trick thus some points
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static long withAdditionalTrick(long pkScore, TeamId winningTeam,int trickPoints)throws IllegalArgumentException {
        
        // better than an if() I think (either a shift of 0:Team 1, or of 32:Team 2)
        int shift = winningTeam.ordinal()*32; 
        int elseShift = (1-winningTeam.ordinal())*32;
        long unmodifiedPkScoreForLoosingTeam = extract(pkScore, elseShift, 32);
        int currentPointsOfWinningTeam = (int) extract(pkScore, 4+shift, 9);
        int numberOfTricksOfWinningTeam = (int) extract(pkScore, 0 + shift,4);
        int GlobalPointsOfWinnigTeam =  (int) extract(pkScore,45,11);
        if (numberOfTricksOfWinningTeam ==8) {
            currentPointsOfWinningTeam += Jass.MATCH_ADDITIONAL_POINTS; // 100 
        }
        long modifiedpkScoreForWinningTeam = Bits64.pack(numberOfTricksOfWinningTeam+1, 4, currentPointsOfWinningTeam+trickPoints, 9,GlobalPointsOfWinnigTeam,11);
        if(shift==32) {
            return Bits64.pack(unmodifiedPkScoreForLoosingTeam, 32, modifiedpkScoreForWinningTeam, 32);
        }
        if(shift ==0) {
            return Bits64.pack(modifiedpkScoreForWinningTeam, 32, unmodifiedPkScoreForLoosingTeam, 32);
        }
        else {
            throw new IllegalArgumentException("There is a mistake somewhere"); 
        }
        
        
    }
    
    
    
    /** returns a long with global points updated with adding the currents points, and  0 as number of Tricks won and 0 current points for both teams.
     * @param  pkScore (long) the long encoding the points and tricks of the game
     * @return a new long with the datas updated as it becomes next turn
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static long nextTurn(long pkScore) {
        assert isValid(pkScore);
        int currentPointsOfTeam1 = (int) extract(pkScore, 4, 9);
        int currentPointsOfTeam2 = (int) extract(pkScore, 36, 9);
        int GlobalPointsOf1 =  (int) extract(pkScore,13,11);
        int GlobalPointsOf2 =  (int) extract(pkScore,45,11);
        return pack(0, 0, currentPointsOfTeam1+GlobalPointsOf1, 0, 0, currentPointsOfTeam2+GlobalPointsOf2);
        
    }
    
    
    
    /** returns a paragraph with all informations packed in pkScore
     * @param pkScore (long) the long encoding the points and tricks of the game
     * @return the String with all informations about points and tricks of both teams 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static String toString(long pkScore) {
        assert isValid(pkScore);    
        String Tricksof1 = "Tricks of Team 1 : " + extract(pkScore,0,4);
        String Tricksof2 = "Tricks of Team 2 : " + extract(pkScore,32,4);
        String CurrentPointsOf1 = "Current points of Team 1 : " + extract(pkScore,4,9);
        String CurrentPointsOf2 = "Current points of Team 2 : " + extract(pkScore,36,9);
        String GlobalPointsOf1 = "Global points of Team 1 : " + extract(pkScore,13,11);
        String GlobalPointsOf2 = "Global points of Team 2 : " + extract(pkScore,45,11);
        return Tricksof1 + "\n" + CurrentPointsOf1 + "\n"+ GlobalPointsOf1 + "\n"+ Tricksof2 + "\n"+ CurrentPointsOf2 + "\n"+ GlobalPointsOf2;
    }
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
