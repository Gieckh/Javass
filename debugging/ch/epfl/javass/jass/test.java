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
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

//this is  a test, thus no need of javadoc
public class test extends Application {
    
    
    public static void main(String[] args) {
            for(int i = 0 ; i<Color.ALL.size(); ++i) {
                for(int j = 0 ; j < Rank.ALL.size(); ++j) {
                    String s = "/"+"card_"+i+"_"+j+"_"+160+".png";
                    System.out.println(new Image(s));
                }
            }
            for(int i = 0 ; i<Color.ALL.size(); ++i) {
                String s = "/"+"trump_"+i+".png";
                System.out.println(new Image(s));
            }
        }      
        
    

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
}
