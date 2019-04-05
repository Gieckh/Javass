package ch.epfl.javass.net;

import ch.epfl.javass.jass.Player;

public final class RemotePlayerServer {

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    Player underLyingPlayer;
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    public RemotePlayerServer(Player underLyingPlayer) {
        this.underLyingPlayer = underLyingPlayer;
    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    public void run() {
        while(true) {
            
        }
    }


}
