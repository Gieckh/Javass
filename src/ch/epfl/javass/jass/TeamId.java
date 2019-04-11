package ch.epfl.javass.jass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * manages the the teams' Id.
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public enum TeamId {
    TEAM_1,
    TEAM_2;

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    public final static int COUNT = 2;
    public final static List<TeamId> ALL =
            Collections.unmodifiableList(Arrays.asList(values()));

    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * returns the other team than the one we apply the function on.
     *
     * @return the other team than the one we apply the function on
     *
     */
    public  TeamId other() {
        switch (this) {
            case TEAM_1:
                return TEAM_2;
            case TEAM_2:
                return TEAM_1;
            default: // unreachable statement
                throw new Error();
        }
    }
}
