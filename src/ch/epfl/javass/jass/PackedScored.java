package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits64;
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
       return(extract(pkScore,24,7) == 0 &&
              extract(pkScore,56,7) == 0 &&
              extract(pkScore,0,4) < 10 &&
              extract(pkScore,4,9) < 258 &&
              extract(pkScore,13,11) < 2001 &&
              extract(pkScore,32,4) < 10 &&
              extract(pkScore,36,9) < 258 &&
              extract(pkScore,45,11) < 2001 );
              
    }
    
    
}
