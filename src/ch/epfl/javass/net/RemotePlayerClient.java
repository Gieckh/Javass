package ch.epfl.javass.net;

import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.Map;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Score;
import ch.epfl.javass.jass.TeamId;
import ch.epfl.javass.jass.Trick;
import ch.epfl.javass.jass.TurnState;



/**
 * RemotePlayerClient instance is a local "teleguided" player which exchanges informations with a RemotePlayerServer instance (which plays in remote).
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
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
    

    /**
     * @param nameOfHost
     * @param port
     * @throws IOException
     */
    public RemotePlayerClient(String nameOfHost, int port) throws IOException {
        Socket s = new Socket(nameOfHost, PORT_NUMBER);
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

    
    /* 
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        System.out.println("closed");
        s.close();
        r.close();
        w.close();
    }
    
    
    /**
     * @Brief writes and flushes the string in the BufferedReader.
     *
     * @param s a String
    */
    private void forceWritting(String s) {
        System.out.println("writed :" + s);
        try {
            w.write(s+"\n");
          //  w.write();
            w.flush();
        } catch (IOException e1) {
            throw new UncheckedIOException(e1);
        }
        
    }
    
    /* 
     * @see ch.epfl.javass.jass.Player#cardToPlay(ch.epfl.javass.jass.TurnState, ch.epfl.javass.jass.CardSet)
     */
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        try {
            forceWritting(StringSerializer.combine(',', 
                    JassCommand.ALL.get(4).toString(),
                    StringSerializer.serializeLong(state.packedScore()),
                    StringSerializer.serializeLong(state.packedUnplayedCards()),
                    StringSerializer.serializeInt(state.packedTrick()), 
                    StringSerializer.serializeLong(hand.packed())));
            System.out.println("just informed about cardToplay");
            return Card.ofPacked(StringSerializer.deserializeInt(r.readLine()));
        } catch (IOException e) {
            System.out.println("exception ?");
            throw new UncheckedIOException(e);
        }
    }

    /* 
     * @see ch.epfl.javass.jass.Player#setPlayers(ch.epfl.javass.jass.PlayerId, java.util.Map)
     */
    @Override
    public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String s =  Integer.toString(ownId.ordinal());
        String str = "";
        for(PlayerId p : PlayerId.ALL) {
            if(p!=PlayerId.PLAYER_1) {
                str+=",";
            }
            str+= StringSerializer.serializeString(playerNames.get(p));
        }
        forceWritting(StringSerializer.combine(',',JassCommand.ALL.get(0).toString(),s ,str)); 
        System.out.println("justinformedaboutsetplayers");
    }
        

    /* 
     * @see ch.epfl.javass.jass.Player#setTrump(ch.epfl.javass.jass.Card.Color)
     */
    @Override
    public void setTrump(Card.Color trump) {
        forceWritting(StringSerializer.combine(',',
                JassCommand.ALL.get(1).toString(),
                StringSerializer.serializeInt(trump.ordinal())));     
        System.out.println("justinformedaboutsettrump");
   }
    
    /* 
     * @see ch.epfl.javass.jass.Player#updateHand(ch.epfl.javass.jass.CardSet)
     */
    @Override
     public void updateHand(CardSet newHand) {
        forceWritting(StringSerializer.combine(',',
                JassCommand.ALL.get(2).toString(),
                StringSerializer.serializeLong(newHand.packed())));
        System.out.println("justinformedaboutupdatehand");
    }

  

    /* 
     * @see ch.epfl.javass.jass.Player#updateTrick(ch.epfl.javass.jass.Trick)
     */
    @Override
     public void updateTrick(Trick newTrick) {
         forceWritting(StringSerializer.combine(',',
                 JassCommand.ALL.get(3).toString(),
                 StringSerializer.serializeInt(newTrick.packed())));
         System.out.println("justinformedaboutupdatetrick");
}

    /* 
     * @see ch.epfl.javass.jass.Player#updateScore(ch.epfl.javass.jass.Score)
     */
    @Override
     public void updateScore(Score score) {
         forceWritting(StringSerializer.combine(',',
                 JassCommand.ALL.get(5).toString(),
                 StringSerializer.serializeLong(score.packed())));
         System.out.println("justinformedaboutupdatescore");
  }

    /* 
     * @see ch.epfl.javass.jass.Player#setWinningTeam(ch.epfl.javass.jass.TeamId)
     */
    @Override
     public void setWinningTeam(TeamId winningTeam) {
         forceWritting(StringSerializer.combine(',',
                 JassCommand.ALL.get(6).toString(),
                 StringSerializer.serializeLong(winningTeam.ordinal())));
         System.out.println("justupdateaboutwinningteam");
    }
}



    
