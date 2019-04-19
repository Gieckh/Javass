package ch.epfl.javass.net;

import static ch.epfl.javass.net.Net.PORT_NUMBER;
import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
 * @brief RemotePlayerServer instance is a remote server which particularity is that
 * it acts depending on the information a RemotePlayerClient sends to him;
 * this RemotePlayerClient is playing locally in the JassGame.
 * It tells the RemotePLayerServer everything that happens,
 * and waits for the RemotePlayerServer's decision for what card to play.
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class RemotePlayerServer {

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private Player underLyingPlayer;
    private final ServerSocket s0;
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    /**
     * @Brief This underlyingPlayer defines how the RemotePlayerServer will ask the RemotePlayerClient to play.
     * 
     * @param underLyingPlayer 
     */
    public RemotePlayerServer(Player underLyingPlayer) {
        this.underLyingPlayer = underLyingPlayer;
        try {
            s0 = new ServerSocket(Net.PORT_NUMBER);
        } catch (IOException e) {
            throw new UncheckedIOException("unchecked: " + e.getMessage(),e);
        }
    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    
    /**
     * @brief receive some information from the local player (client) and tells him what card to play.
     *
     */
    public void run() {
        System.out.println("run");
        try (
                Socket s = s0.accept();
                BufferedReader r = new BufferedReader(
                        new InputStreamReader(s.getInputStream(), US_ASCII));
                BufferedWriter w =
                        new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), US_ASCII))) {
            System.out.println("On passe le try");
            String string;
            System.out.println("just read");
            while((string = r.readLine()) !=  null) {
                
                // I decided to combine the whole line only with ',' (even the jassCommand) //TODO: change that
                List<String> words = new LinkedList<>(Arrays.asList(string.split("[ ,]"))); 
                JassCommand Command = JassCommand.valueOf(words.get(0));
                switch(Command) {
                    case PLRS:
                        System.out.println("players read");
                        Map<PlayerId, String> playerNames = new HashMap<>();
                        for(int i = 2; i < 6 ; ++i) {
                            playerNames.put(PlayerId.ALL.get(i-2), StringSerializer.deserializeString(words.get(i)));
                        }
                        this.underLyingPlayer.setPlayers(PlayerId.ALL.get(StringSerializer.deserializeInt(words.get(1))), playerNames);
                        break;
                        
                    case TRMP:
                        System.out.println("trump read");
                        this.underLyingPlayer.setTrump(Card.Color.ALL.get(StringSerializer.deserializeInt(words.get(1))));
                        break;
                        
                    case HAND:
                        System.out.println("hand read");
                        this.underLyingPlayer.updateHand(CardSet.ofPacked(StringSerializer.deserializeLong(words.get(1))));
                        break;
                        
                    case TRCK:
                        System.out.println("trick read");
                        this.underLyingPlayer.updateTrick(Trick.ofPacked(StringSerializer.deserializeInt(words.get(1))));
                        break;
                        
                    case CARD:
                        System.out.println("card read");
                        System.out.println();
                        w.write(StringSerializer.serializeInt(this.underLyingPlayer.cardToPlay(
                                TurnState.ofPackedComponents(
                                        StringSerializer.deserializeLong(words.get(1)),
                                        StringSerializer.deserializeLong(words.get(2)),
                                        StringSerializer.deserializeInt(words.get(3))
                                ),
                                CardSet.ofPacked(
                                        StringSerializer.deserializeLong(words.get(4)))).packed())
                        );

                        w.write("\n");
                        w.flush();
                        break;

                    case SCOR:
                        System.out.println("score read");
                        this.underLyingPlayer.updateScore(Score.ofPacked(
                                StringSerializer.deserializeLong(words.get(1))));
                        break;
                        
                    case WINR:
                        System.out.println("win read");
                        this.underLyingPlayer.setWinningTeam(TeamId.ALL.get(
                                StringSerializer.deserializeInt(words.get(1))));
                        break;
                        

                    default:
                        throw new Error("JassCommand enum has been changed?");
                }
            }
            System.out.println("end of while");
        }
        catch (IOException e) {
            System.out.println("coucou");
            throw new UncheckedIOException(e);
        }
        run();
    }
}
