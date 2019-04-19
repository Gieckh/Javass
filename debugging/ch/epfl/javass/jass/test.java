package ch.epfl.javass.jass;
import java.util.SplittableRandom;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import ch.epfl.javass.gui.HandBean;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import ch.epfl.javass.net.StringSerializer;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) throws Base64DecodingException {
        String s = "WLH deux,trois,quatre,cinq";
    
        String hello[] = s.split(",");
        for(int i = 0 ; i< hello.length ; ++i) {
            System.out.println(hello[i]);

        }
        String[] coucou = hello[0].split(" ",",");
        String[] hey = coucou + hello;
        for(int i = 0 ; i< hello.length ; ++i) {
            System.out.println(coucou[0]);
            System.out.println(coucou[1]);

        }
    }
}
