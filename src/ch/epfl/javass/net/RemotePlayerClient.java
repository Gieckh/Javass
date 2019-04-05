//package ch.epfl.javass.net;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.UncheckedIOException;
//import java.net.Socket;
//import java.util.Map;
//
//import ch.epfl.javass.jass.Card;
//import ch.epfl.javass.jass.CardSet;
//import ch.epfl.javass.jass.Player;
//import ch.epfl.javass.jass.PlayerId;
//import ch.epfl.javass.jass.Score;
//import ch.epfl.javass.jass.TeamId;
//import ch.epfl.javass.jass.Trick;
//import ch.epfl.javass.jass.TurnState;
//import static java.nio.charset.StandardCharsets.US_ASCII;
//
//
//public final class RemotePlayerClient implements Player , AutoCloseable {
//
//    /** ============================================== **/
//    /** ==============    ATTRIBUTES    ============== **/
//    /** ============================================== **/
//    public final static int PORT_NUMBER = 5108;
//    String nameOfHost;
//    int port;
//    Socket s;
//    BufferedReader r;
//    BufferedWriter w;
//
//    /** ============================================== **/
//    /** ==============   CONSTRUCTORS   ============== **/
//    /** ==============================================
//     * @throws IOException **/
//
//    public RemotePlayerClient(String nameOfHost, int port) throws IOException {
//        Socket s = new Socket("localhost" , PORT_NUMBER);
//                BufferedReader r =
//                  new BufferedReader(
//                    new InputStreamReader(s.getInputStream(),
//                              US_ASCII));
//                BufferedWriter w =
//                  new BufferedWriter(
//                    new OutputStreamWriter(s.getOutputStream(),
//                               US_ASCII));
//        this.s = s;
//        this.r = r;
//        this.w = w;
//        this.nameOfHost = nameOfHost;
//        this.port = port;
//    }
//
//    /** ============================================== **/
//    /** ===============    METHODS    ================ **/
//    /** ============================================== **/
//
//    @Override
//    public Card cardToPlay(TurnState state, CardSet hand) {
//        return null;
//    }
//
//    @Override
//    public void close() throws Exception {
//        s.close();
//        r.close();
//        w.close();
//    }
//
//
//
//
//    /**
//     * @Brief informs the player his Id and all player names.
//     *
//     * @param ownId
//     * @param playerNames
//     *
//     */
//    default public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
//        //default is empty
//    }
//
//
//    /**
//     * @brief informs the player about updating his hand.
//     *
//     * @param newHand
//     *
//     */
//    default public void updateHand(CardSet newHand) {
//        //default is empty
//    }
//
//    /**
//     * @Brief informs the player with setting the trumpColor.
//     *
//     * @param trump
//     *
//     */
//    default public void setTrump(Card.Color trump) {
//        //default is empty
//    }
//
//    /**
//     * @Brief informs the player about the updated trick.
//     *
//     * @param newTrick
//     *
//     */
//    default public void updateTrick(Trick newTrick) {
//        //default is empty
//    }
//
//    /**
//     * @Brief informs the player about the updated score.
//     *
//     * @param score
//     *
//     */
//    default public void updateScore(Score score) {
//        //default is empty
//    }
//
//    /**
//     * @Brief informs the player about which team won.
//     *
//     * @param winningTeam
//     *
//     */
//    default public void setWinningTeam(TeamId winningTeam) {
//        //default is empty
//    }
//}
//
//
//}
//
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.Map;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Score;
import ch.epfl.javass.jass.TeamId;
import ch.epfl.javass.jass.Trick;
import ch.epfl.javass.jass.TurnState;
import static java.nio.charset.StandardCharsets.US_ASCII;


public final class RemotePlayerClient implements Player , AutoCloseable {
    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    public final static int PORT_NUMBER = 5108;
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
        Socket s = new Socket("localhost" , PORT_NUMBER);
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
    public void close() throws Exception {
        s.close();
        r.close();
        w.close();
    }
    
    private void forceWritting(String s) {
        try {
            w.write(s);
            w.flush();
        } catch (IOException e1) {
            throw new UncheckedIOException(e1);
        }
        
    }
    
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        try {
            forceWritting(StringSerializer.serializeString(StringSerializer.combine(',', 
                    JassCommand.ALL.get(4).toString(),
                    StringSerializer.serializeLong(state.packedScore()),
                    StringSerializer.serializeLong(state.packedUnplayedCards()),
                    StringSerializer.serializeInt(state.packedTrick()), 
                    StringSerializer.serializeLong(hand.packed()))));
            return Card.ofPacked(StringSerializer.deserializeInt(StringSerializer.deserializeString(r.readLine())));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String s =  Integer.toString(ownId.ordinal());
        String str = null;
        for(PlayerId p : PlayerId.ALL) {
            str+= StringSerializer.serializeString(playerNames.get(p));
        }
        forceWritting(StringSerializer.combine(',',JassCommand.ALL.get(1).toString(),s ,str));        
    }
        

    @Override
    public void setTrump(Card.Color trump) {
        forceWritting(StringSerializer.serializeString(StringSerializer.combine(',',
                JassCommand.ALL.get(1).toString(),
                StringSerializer.serializeInt(trump.ordinal()))));     
   }
    
    @Override
     public void updateHand(CardSet newHand) {
        forceWritting(StringSerializer.serializeString(StringSerializer.combine(',',
                JassCommand.ALL.get(2).toString(),
                StringSerializer.serializeLong(newHand.packed()))));
    }

  

    @Override
     public void updateTrick(Trick newTrick) {
         forceWritting(StringSerializer.serializeString(StringSerializer.combine(',',
                 JassCommand.ALL.get(3).toString(),
                 StringSerializer.serializeInt(newTrick.packed()))));
}

    @Override
     public void updateScore(Score score) {
         forceWritting(StringSerializer.serializeString(StringSerializer.combine(',',
                 JassCommand.ALL.get(5).toString(),
                 StringSerializer.serializeLong(score.packed()))));
  }

    @Override
     public void setWinningTeam(TeamId winningTeam) {
         forceWritting(StringSerializer.serializeString(StringSerializer.combine(',',
                 JassCommand.ALL.get(6).toString(),
                 StringSerializer.serializeLong(winningTeam.ordinal()))));
    }
}



    
