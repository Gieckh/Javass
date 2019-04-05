package ch.epfl.javass.net;
import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.epfl.javass.net.RemotePlayerClient;
import ch.epfl.javass.net.RemotePlayerServer;

public class serverNetGame {
    public static void main(String[] args) {
        try {
            RemotePlayerClient gali =  new RemotePlayerClient("localhost", RemotePlayerClient.PORT_NUMBER);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
