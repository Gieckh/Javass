package ch.epfl.javass.jass;


import ch.epfl.javass.jass.TeamId;

import java.util.Map;

public class test {
    public static void main(String[] args) {
        dispMap(PackedCardSet.pkCardsForTrump);
    }

    static void dispMap(Map<Integer, Long> myMap) {
        for (Map.Entry<Integer, Long> entry : myMap.entrySet()) {
            System.out.println(PackedCard.toString(entry.getKey()) + "/" + PackedCardSet.toString(entry.getValue()));
        }
    }
}
