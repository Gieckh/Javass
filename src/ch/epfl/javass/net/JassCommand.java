package ch.epfl.javass.net;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.javass.jass.TeamId;

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
