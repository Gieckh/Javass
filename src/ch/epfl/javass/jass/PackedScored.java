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
    
    //so the class is not instanciable
    //TODO do same for some other classes
    private PackedScored() {};
    
    /** returns true if pkScore is packed correctly.
     * @param pkScore
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
     * @param pkScore
     * @param t, the TeamId
     * @return int : the number of tricks won 
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int turnTricks(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);
        if(t.ordinal() == 1) {
            return (int) extract( pkScore , 0 , 4 );
        }
        if(t.ordinal() == 2) {
            return (int) extract( pkScore , 32 , 4 );
        }
        else {
            throw new IllegalArgumentException("Bad Team Inmput");
        }
    }
    
    
    /** returns the number of points won in the current turn in function of the team.
     * @param pkScore
     * @param t, the TeamId
     * @return int : the number of points won in the current turn 
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int turnPoints(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);
        if(t.ordinal() == 1) {
            return (int) extract( pkScore , 4 , 9 );
        }
        if(t.ordinal() == 2) {
            return (int) extract( pkScore , 36 ,9 );
        }
        else {
            throw new IllegalArgumentException("Bad Team Inmput");
        }
    }
    
    /** returns the number of points won in the whole game except the current turn. 
     * @param pkScore
     * @param t, the TeamId
     * @return int : the total number of points won depending on the team 
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int gamePoints(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);
        if(t.ordinal() == 1) {
            return (int) extract( pkScore , 13 , 11 );
        }
        if(t.ordinal() == 2) {
            return (int) extract( pkScore , 45 , 11 );
        }
        else {
            throw new IllegalArgumentException("Bad Team Inmput");
        }
    }
    
    /** returns the total number of points won in the whole game except the current turn. 
     * @param pkScore
     * @param t, the TeamId
     * @return int : the total number of points won depending on the team 
     * @throws IllegalArgumentException
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int totalPoints(long pkScore, TeamId t) throws IllegalArgumentException{
        assert isValid(pkScore);
        if(t.ordinal() == 1) {
            return (int) (extract( pkScore , 13 , 11 ) + extract( pkScore, 4 , 9 ));
        }
        if(t.ordinal() == 2) {
            return (int) (extract( pkScore , 45 , 11 ) + extract( pkScore, 4 , 9 ));
        }
        else {
            throw new IllegalArgumentException("Bad Team Inmput");
        }
    }
        
        //TODO : de with aditional a next turn + lire les "conseils"
       
    
    
    
    public static String toString(long pkScore) {
        assert isValid(pkScore);    
        String Tricksof1 = "Tricks of Team 1 : " + extract(pkScore,0,4);
        String Tricksof2 = "Tricks of Team 2 : " + extract(pkScore,32,4);
        String CurrentPointsOf1 = "Current points of Team 1 : " + extract(pkScore,4,9);
        String CurrentPointsOf2 = "Current points of Team 2 : " + extract(pkScore,36,9);
        String GlobalPointsOf1 = "Global points of Team 1 : " + extract(pkScore,13,11);
        String GlobalPointsOf2 = "Global points of Team 1 : " + extract(pkScore,45,11);
        return Tricksof1 + "\n" + CurrentPointsOf1 + "\n"+ GlobalPointsOf1 + "\n"+ Tricksof2 + "\n"+ CurrentPointsOf2 + "\n"+ GlobalPointsOf2;
    }
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
