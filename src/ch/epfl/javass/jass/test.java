package ch.epfl.javass.jass;

import ch.epfl.javass.jass.Card.Color;

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

        if(PackedTrick.isValid(0)) {
            System.out.println("coucou");
        System.out.println(Trick.ofPacked(0).toString());
        }

    }
}
