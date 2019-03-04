package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.fxml.expression.BinaryExpression;

import ch.epfl.javass.jass.Card.Color;

public class test {
    public static void main(String[] args) {
        /*List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        for (int i = 0 ; i < 8 ; ++i) {
            l1.add(i);
            l2.add(i);
        }

        List<Integer> subL1_1 = l1.subList(0, 3);
        List<Integer> subL1_2 = l1.subList(0, 3);

        System.out.println("     l1 == l2 ? : " + (l1 == l2));
        System.out.println("subL1_1 == subL1_2 ? : " + (subL1_1 == subL1_2));
        System.out.println();
        System.out.println("     l1 == l1 ? : " + (l1 == l1));
        System.out.println("subL1_1 == subL1_1 ? : " + (subL1_1 == subL1_1));
        System.out.println();
        System.out.println("     l1 : " + l1);
        System.out.println("subL1_1 : " + subL1_1);
        */
        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.SPADE, PlayerId.PLAYER_1)));
        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.SPADE, PlayerId.PLAYER_2)));
        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.SPADE, PlayerId.PLAYER_3)));
        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.SPADE, PlayerId.PLAYER_4)));
        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.CLUB, PlayerId.PLAYER_1)));
        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.CLUB, PlayerId.PLAYER_2)));
        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.CLUB, PlayerId.PLAYER_3)));
        System.out.println(Integer.toBinaryString(PackedTrick.firstEmpty(Color.CLUB, PlayerId.PLAYER_4)));
    }
}
