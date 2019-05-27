package ch.epfl.javass.net;

import ch.epfl.javass.jass.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @brief JassCommand is an enumeration of the possibles commands that can be sent
 *        between a {@code remotePlayerClient} and a {@code remotePlayerServer}
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public enum JassCommand {
    PLRS, /** @see ch.epfl.javass.jass.Player#setPlayers(PlayerId, Map)  */
    TRMP, /** @see ch.epfl.javass.jass.Player#setTrump(Card.Color)  */
    HAND, /** @see ch.epfl.javass.jass.Player#updateHand(CardSet)  */
    TRCK, /** @see ch.epfl.javass.jass.Player#updateTrick(Trick)  */
    CARD, /** @see ch.epfl.javass.jass.Player#cardToPlay(TurnState, CardSet)  */
    SCOR, /** @see ch.epfl.javass.jass.Player#updateScore(Score)  */
    WINR, /** @see ch.epfl.javass.jass.Player#setWinningTeam(TeamId)  */
    CHET, 
    MELD,
    ANCM; 

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public final static int COUNT = 10;
    public final static List<JassCommand> ALL =
            Collections.unmodifiableList(Arrays.asList(values()));

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
}
