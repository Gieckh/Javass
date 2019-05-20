package ch.epfl.javass.jass;

import java.util.Collections;

import bonus.JassReductorOfSet;
import ch.epfl.javass.gui.HandBean;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) {
        Score b = Score.INITIAL;
        System.out.println(b);
        b = b.addPoints(TeamId.TEAM_1, 22);
        System.out.println(b);
    }
     
}
