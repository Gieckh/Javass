package ch.epfl.javass.jass;
import java.util.SplittableRandom;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import ch.epfl.javass.net.StringSerializer;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) throws Base64DecodingException {
        System.out.println(Card.of(Card.Color.SPADE, Card.Rank.SIX).equals(null));
    }
}
