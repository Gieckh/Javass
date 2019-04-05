package ch.epfl.javass.jass;
import java.util.SplittableRandom;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import ch.epfl.javass.net.StringSerializer;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) throws Base64DecodingException {
        String s = "42";
        String u = StringSerializer.serializeString(s);
        String v = StringSerializer.deserializeString(u);
        System.out.println(u);
        System.out.println(v);
    }
}
