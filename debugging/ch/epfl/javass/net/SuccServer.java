package ch.epfl.javass.net;

import static java.nio.charset.StandardCharsets.US_ASCII;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public final class SuccServer {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /** ============================================== **/
    /** ==================   MAIN   ================== **/
    /** ============================================== **/

    public static void main(String args[]) {
        try (ServerSocket s0 = new ServerSocket(5108);
                Socket s = s0.accept();
                BufferedReader r =
                        new BufferedReader(
                                new InputStreamReader(s.getInputStream(),
                                        US_ASCII));
                BufferedWriter w =
                        new BufferedWriter(
                                new OutputStreamWriter(s.getOutputStream(),
                                        US_ASCII))) {
            int i = Integer.parseInt(r.readLine());
            int i1 = i + 1;
            w.write(String.valueOf(i1));
            w.write('\n');
            w.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
