package ch.epfl.javass.net;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * JassCommand is an enumeration of the possibles commands that can be thrown between a remotePlayerClient and a remotePlayerServer
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public enum JassCommand {
    PLRS,
    TRMP,
    HAND, 
    TRCK,
    CARD,
    SCOR,
    WINR;

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public final static int COUNT = 7;
    public final static List<JassCommand> ALL =
            Collections.unmodifiableList(Arrays.asList(values()));

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
}
