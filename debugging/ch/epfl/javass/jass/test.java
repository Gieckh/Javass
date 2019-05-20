package ch.epfl.javass.jass;

import java.util.Collections;

import bonus.JassReductorOfSet;
import ch.epfl.javass.gui.HandBean;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import src.cs108.Meld;
import src.cs108.MeldSet;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) {
        long packed[] = {0b1110,0b11110101011};
        System.out.println(Long.toBinaryString(MeldSet.from(packed).packed()[0]));
    }
     
}
