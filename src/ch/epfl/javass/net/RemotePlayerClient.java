package ch.epfl.javass.net;

import static ch.epfl.javass.net.StringSerializer.combine;
import static ch.epfl.javass.net.StringSerializer.serializeString;
import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.Map;
import java.util.StringJoiner;

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
    public RemotePlayerClient(String hostName) throws IOException {
        Socket s = new Socket(hostName, Net.PORT_NUMBER); 
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
                    combine(' ',
                            combine(',',
                                    StringSerializer.serializeLong(state.packedScore()),
                                    StringSerializer.serializeLong(state.packedUnplayedCards()),
                                    StringSerializer.serializeInt(state.packedTrick())),
                            StringSerializer.serializeLong(hand.packed()))
            );

            return Card.ofPacked(StringSerializer.deserializeInt(reader.readLine()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * @see ch.epfl.javass.jass.Player#setPlayers(ch.epfl.javass.jass.PlayerId, java.util.Map)
     */
    @Override
    public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String s = Integer.toString(ownId.ordinal());

        //TODO: suppr after we've tested the StringJoiner
//        String str = "";
//        for(PlayerId p : PlayerId.ALL) {
//            if(p != PlayerId.PLAYER_1)
//                str+=",";
//
//            str+= StringSerializer.serializeString(playerNames.get(p));
//        }

        StringJoiner str = new StringJoiner(",");
        for (PlayerId pId: PlayerId.ALL)
            str.add(serializeString(playerNames.get(pId)));

        forceWrite(JassCommand.PLRS.toString(),
                   combine(' ', s, str.toString())
        );
    }
    
    /* 
     * @see ch.epfl.javass.jass.Player#cheat(int)
     */
    @Override
    public int cheat() {
        forceWrite(JassCommand.CHET.toString(),"");
        try {
            return StringSerializer.deserializeInt(reader.readLine());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Override
    public int announcement(CardSet hand) { 
        forceWrite(JassCommand.MELD.toString(),
            combine(' ',StringSerializer.serializeLong(hand.packed())));
        try {
            return StringSerializer.deserializeInt(reader.readLine());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
        

    /**
     * @see ch.epfl.javass.jass.Player#setTrump(ch.epfl.javass.jass.Card.Color)
     */
    @Override
    public void setTrump(Card.Color trump) {
        forceWrite(JassCommand.TRMP.toString(),
                      combine(',',
                      StringSerializer.serializeInt(trump.ordinal()))
        );
   }
    
    /**
     * @see ch.epfl.javass.jass.Player#updateHand(ch.epfl.javass.jass.CardSet)
     */
    @Override
     public void updateHand(CardSet newHand) {
        forceWrite(JassCommand.HAND.toString(),
                      combine(',',
                      StringSerializer.serializeLong(newHand.packed()))
        );
    }

    /**
     * @see ch.epfl.javass.jass.Player#updateTrick(ch.epfl.javass.jass.Trick)
     */
    @Override
     public void updateTrick(Trick newTrick) {
         forceWrite(JassCommand.TRCK.toString(),
                       combine(',',
                       StringSerializer.serializeInt(newTrick.packed()))
         );
}

    /**
     * @see ch.epfl.javass.jass.Player#updateScore(ch.epfl.javass.jass.Score)
     */
    @Override
     public void updateScore(Score score) {
         forceWrite(JassCommand.SCOR.toString(),
                       combine(',',
                       StringSerializer.serializeLong(score.packed()))
         );
    }

    /**
     * @see ch.epfl.javass.jass.Player#setWinningTeam(ch.epfl.javass.jass.TeamId)
     */
    @Override
     public void setWinningTeam(TeamId winningTeam) {
         forceWrite(JassCommand.WINR.toString(),
                       combine(',',
                       StringSerializer.serializeLong(winningTeam.ordinal()))
         );
    }
}



    
