package ch.epfl.javass.net;
import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.RandomPlayer;
import ch.epfl.javass.net.RemotePlayerClient;
import ch.epfl.javass.net.RemotePlayerServer;

/**
 * test thus non Jdoc 
 * 
 * IMPORTANT : LANCER D'ABORD servernetgame PUIS netjassgame
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public class serverNetGame {
    public static void main(String[] args) throws IOException {
        Player player = new RandomPlayer(2019);
        RemotePlayerServer gali =  new RemotePlayerServer(player);
        gali.run();
        System.out.println("on devrais pas arriver la");
    }
}
