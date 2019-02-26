package ch.epfl.javass.jass;


import ch.epfl.javass.jass.TeamId;

public abstract class test {
    public static void main(String[] args) {
        System.out.println(PackedCard.toString(PackedCardSet.get(0b1101L, 2)));
    }
}
