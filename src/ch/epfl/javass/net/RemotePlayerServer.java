package ch.epfl.javass.net;

import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Score;
import ch.epfl.javass.jass.TeamId;
import ch.epfl.javass.jass.Trick;
import ch.epfl.javass.jass.TurnState;

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
        try (ServerSocket s0 = new ServerSocket(RemotePlayerClient.PORT_NUMBER);
                Socket s = s0.accept();
                BufferedReader r = new BufferedReader(
                        new InputStreamReader(s.getInputStream(), US_ASCII));
                BufferedWriter w =
                        new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), US_ASCII))) {
            while(!r.readLine().isEmpty()) {
                String words[] = (r.readLine()).split(",");
                JassCommand Command = JassCommand.valueOf(words[0]);
                switch(Command) {
                    case PLRS:
                        Map<PlayerId, String> playerNames = new HashMap<>();
                        for(int i = 2 ; i<6 ; ++i) {
                            playerNames.put(PlayerId.ALL.get(i-2), StringSerializer.deserializeString(words[i]));
                        }
                        this.underLyingPlayer.setPlayers(PlayerId.valueOf(words[1]), playerNames);

                    case TRMP:
                        this.underLyingPlayer.setTrump(Card.Color.ALL.get(StringSerializer.deserializeInt(words[1])));

                    case HAND:
                        this.underLyingPlayer.updateHand(CardSet.ofPacked(StringSerializer.deserializeLong(words[1])));

                    case TRCK:
                        this.underLyingPlayer.updateTrick(Trick.ofPacked(StringSerializer.deserializeInt(words[1])));

                    case CARD:
                        w.write(StringSerializer.serializeInt(this.underLyingPlayer.cardToPlay(
                                TurnState.ofPackedComponents(
                                        StringSerializer.deserializeLong(words[1]),
                                        StringSerializer.deserializeLong(words[2]),
                                        StringSerializer.deserializeInt(words[3])),
                                CardSet.ofPacked(
                                        StringSerializer.deserializeLong(words[3]))).packed()));
                        w.flush();

                    case SCOR:
                        this.underLyingPlayer.updateScore(Score.ofPacked(
                                StringSerializer.deserializeLong(words[1])));
                    case WINR:
                        this.underLyingPlayer.setWinningTeam(TeamId.ALL.get(
                                StringSerializer.deserializeInt(words[1])));

                    default:
                        throw new IllegalArgumentException("error in remote player ; wrong Jass Command");
                }
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
