package ch.epfl.javass.jass;


import ch.epfl.javass.jass.TeamId;

public abstract class test {
    public static void main(String[] args) {
        //NOT WORKING
        System.out.println(PackedCard.toString(PackedCardSet.get(0b1101L, 0)));
        System.out.println();

        System.out.println(PackedCard.toString(PackedCardSet.get(0b1111L, 1)));
        System.out.println();

        System.out.println(PackedCard.toString(PackedCardSet.get(0b1101L, 1)));
        System.out.println();

        System.out.println(PackedCard.toString(PackedCardSet.get(0b1101L, 2)));
        System.out.println();

        System.out.println(PackedCard.toString(PackedCardSet.get(0b1111_1011L, 2)));
        System.out.println();



        //WORKING
        System.out.println(PackedCard.toString(PackedCardSet.get2(0b1101L, 0)));
        System.out.println();

        System.out.println(PackedCard.toString(PackedCardSet.get2(0b1111L, 1)));
        System.out.println();

        System.out.println(PackedCard.toString(PackedCardSet.get2(0b1101L, 1)));
        System.out.println();

        System.out.println(PackedCard.toString(PackedCardSet.get2(0b1101L, 2)));
        System.out.println();

        System.out.println(PackedCard.toString(PackedCardSet.get2(0b1111_1011L, 2)));
        System.out.println();
    }
}
