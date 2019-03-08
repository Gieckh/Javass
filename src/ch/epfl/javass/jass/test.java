package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.fxml.expression.BinaryExpression;

import ch.epfl.javass.jass.Card.Color;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;

public class test {
    public static void main(String[] args) {
        //        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.SPADE, PlayerId.PLAYER_1)));
        //        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.SPADE, PlayerId.PLAYER_2)));
        //        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.SPADE, PlayerId.PLAYER_3)));
        //        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.SPADE, PlayerId.PLAYER_4)));
        //        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.CLUB, PlayerId.PLAYER_1)));
        //        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.CLUB, PlayerId.PLAYER_2)));
        //        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.CLUB, PlayerId.PLAYER_3)));
        //        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.CLUB, PlayerId.PLAYER_4)));

//        for (int i = 0; i < 8; ++i) {
//            System.out.println(i);
//            System.out.println(Integer.toBinaryString(PackedTrick.nextEmpty(8 | (i << 24))));
//            System.out.println();
//        }
        System.out.println(PackedCard.toString(49));
        System.out.println(PackedTrick.winningPlayer(0b1000));
        System.out.println(PackedTrick.winningPlayer(0b10000));
        System.out.println( Integer.toBinaryString( PackedTrick.nextEmpty(0b1000) ) );
        System.out.println();
        System.out.println( Integer.toBinaryString( PackedTrick.nextEmpty(0b10000) ) );
    }
}
