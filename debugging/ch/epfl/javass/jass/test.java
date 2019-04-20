package ch.epfl.javass.jass;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;

import ch.epfl.javass.gui.GraphicalPlayer;
import ch.epfl.javass.gui.HandBean;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import ch.epfl.javass.net.StringSerializer;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.scene.image.Image;

//this is  a test, thus no need of javadoc
public class test {
    private static Map<Card,Image> cardImpage160 = cardImage160();
    public static Map<Card,Image> cardImage160() {
        Map<Card,Image> map = new HashMap<>();
        for(int i = 0 ; i<Color.ALL.size(); ++i) {
            for(int j = 0 ; j < Rank.ALL.size(); ++j) {
                String s = "/"+"card_"+i+"_"+j+"_"+160+".png";
                map.put(Card.of(Color.ALL.get(i),Rank.ALL.get(j)),  new Image(s));
            }
        }
        //unsure transtypage askip
        return  map;
    }
    
    public static void main(String[] args) {
       // Image image = cardImpage160.get(Card.of(Color.CLUB, Rank.ACE));
        
    }
    
}
