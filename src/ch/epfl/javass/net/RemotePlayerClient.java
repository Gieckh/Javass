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


//TODO: does it actually work - without the "println(...)" ?

/**
 * RemotePlayerClient instance acts as a player in a JassGame although it exchanges information
 * with a RemotePlayerServer instance (which plays in remote) and which takes the decisions.
 * Thus we need exactly one RemotePlayerClient's instance for each RemotePlayerServer's instance.
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class RemotePlayerClient implements Player, AutoCloseable {
    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    String hostName;
    int port;
    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== 
    

    /**
     * @brief Constructs a socket, a reader, and an writer.
     * 
     * @param hostName (String) - the name or the IP string of the host in which the RemotePlayerServer is
     * @param port (int) - should be 5108 per default
     * @throws IOException
     */
    public RemotePlayerClient(String hostName, int port) throws IOException {
        Socket s = new Socket(hostName, Net.PORT_NUMBER); //TODO: why not just "port" ?
        BufferedReader r =
                new BufferedReader(
                        new InputStreamReader(s.getInputStream(), US_ASCII));
        BufferedWriter w =
                new BufferedWriter(
                        new OutputStreamWriter(s.getOutputStream(), US_ASCII));

        this.socket = s;
        this.reader = r;
        this.writer = w;

        this.hostName = hostName;
        this.port = port;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    
    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        socket.close();
        reader.close();
        writer.close();
        System.out.println("closed");
    }

    /**
     * @brief writes and flushes the {@code String} in the BufferedReader.
     *
     * @param command (String) - the {@code String} representation of a {@code JassCommand}
     * @param s (String) - specifies the actions associated to the given command
     */
    private void forceWrite(String command, String s) {
        try {
            String line = command + " " + s + "\n";
            writer.write(line);
            writer.flush();
            System.out.println("written: " + s);
        } catch (IOException e1) {
            throw new UncheckedIOException(e1);
        }
    }
    
    /**
     * @see ch.epfl.javass.jass.Player#cardToPlay(ch.epfl.javass.jass.TurnState, ch.epfl.javass.jass.CardSet)
     */
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        try {
            forceWrite(JassCommand.CARD.toString(),
                    StringSerializer.combine(' ',
                            StringSerializer.combine(',',
                                    StringSerializer.serializeLong(state.packedScore()),
                                    StringSerializer.serializeLong(state.packedUnplayedCards()),
                                    StringSerializer.serializeInt(state.packedTrick())),
                            StringSerializer.serializeLong(hand.packed()))
            );

            System.out.println("just informed about cardToPlay");
            return Card.ofPacked(StringSerializer.deserializeInt(reader.readLine()));
        } catch (IOException e) {
            System.out.println("exception ?");
            throw new UncheckedIOException(e);
        }
    }

    /**
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
        forceWrite(JassCommand.PLRS.toString(),StringSerializer.combine(' ',s ,str));
        System.out.println("justInformedAboutSetPlayers");
    }
        

    /**
     * @see ch.epfl.javass.jass.Player#setTrump(ch.epfl.javass.jass.Card.Color)
     */
    @Override
    public void setTrump(Card.Color trump) {
        forceWrite(JassCommand.TRMP.toString(),
                      StringSerializer.combine(',',
                      StringSerializer.serializeInt(trump.ordinal()))
        );
        System.out.println("justInformedAboutSetTrump");
   }
    
    /**
     * @see ch.epfl.javass.jass.Player#updateHand(ch.epfl.javass.jass.CardSet)
     */
    @Override
     public void updateHand(CardSet newHand) {
        forceWrite(JassCommand.HAND.toString(),
                      StringSerializer.combine(',',
                      StringSerializer.serializeLong(newHand.packed()))
        );
        System.out.println("just informed about updateHand");
    }

    /**
     * @see ch.epfl.javass.jass.Player#updateTrick(ch.epfl.javass.jass.Trick)
     */
    @Override
     public void updateTrick(Trick newTrick) {
         forceWrite(JassCommand.TRCK.toString(),
                       StringSerializer.combine(',',
                       StringSerializer.serializeInt(newTrick.packed()))
         );
         System.out.println("justInformedAboutUpdateTrick");
}

    /**
     * @see ch.epfl.javass.jass.Player#updateScore(ch.epfl.javass.jass.Score)
     */
    @Override
     public void updateScore(Score score) {
         forceWrite(JassCommand.SCOR.toString(),
                       StringSerializer.combine(',',
                       StringSerializer.serializeLong(score.packed()))
         );
         System.out.println("justInformedAboutUpdateScore");
    }

    /**
     * @see ch.epfl.javass.jass.Player#setWinningTeam(ch.epfl.javass.jass.TeamId)
     */
    @Override
     public void setWinningTeam(TeamId winningTeam) {
         forceWrite(JassCommand.WINR.toString(),
                       StringSerializer.combine(',',
                       StringSerializer.serializeLong(winningTeam.ordinal()))
         );
         System.out.println("justUpdateAboutWinningTeam");
    }
}



    
