package ch.epfl.javass.net;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.TurnState;

public final class RemotePlayerClient implements Player , AutoCloseable {
    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub
        
    }

}

    
