package ch.epfl.javass.jass;


import ch.epfl.javass.jass.TeamId;

public class test {
    public static void main(String[] args) {
        //NOT WORKING
        displayGet(0b1101L, 0);
        displayGet(0b1101L, 1);
        displayGet(0b1111L, 1);
        displayGet(0b1101L, 2);
        displayGet(0b0101_1111_1011L, 2);
    }

    static void displayGet(long pkCardSet, int index) {
        System.out.println(PackedCard.toString(PackedCardSet.get(pkCardSet, index)));
        System.out.println("-----------------------------------------------------------");
    }
}
