package ch.epfl.javass.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.Socket;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.TurnState;
import static java.nio.charset.StandardCharsets.US_ASCII;


public final class RemotePlayerClient implements Player , AutoCloseable {
    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    String nameOfHost;
    int port;
    Socket s;
    BufferedReader r;
    BufferedWriter w;
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== 
     * @throws IOException **/

    public RemotePlayerClient(String nameOfHost, int port) throws IOException {
        Socket s = new Socket("localhost", 5108);
                BufferedReader r =
                  new BufferedReader(
                    new InputStreamReader(s.getInputStream(),
                              US_ASCII));
                BufferedWriter w =
                  new BufferedWriter(
                    new OutputStreamWriter(s.getOutputStream(),
                               US_ASCII));
        this.s = s;
        this.r = r;
        this.w = w;
        this.nameOfHost = nameOfHost;
        this.port = port;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        return null;
    }

    @Override
    public void close() throws Exception {
 
    }

}

    
