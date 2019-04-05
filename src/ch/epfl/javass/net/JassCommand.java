package ch.epfl.javass.net;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.javass.jass.TeamId;

public enum JassCommand {
    PLRS    (0),
    TRMP    (1),
    HAND    (2),
    TRCK    (3),
    CARD    (4),
    SCOR    (5),
    WINR    (6);

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private final int type;
    public final static int COUNT = 7;
    public final static List<JassCommand> ALL =
            Collections.unmodifiableList(Arrays.asList(values()));

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    private JassCommand(int type) {
        this.type = type;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
}
