package ch.epfl.javass.jass;


import ch.epfl.javass.jass.TeamId;

public class test {
    public static void main(String[] args) {
        long s = PackedCardSet.EMPTY;
        int c1 = PackedCard.pack(Card.Color.HEART, Card.Rank.SIX);
        int c2 = PackedCard.pack(Card.Color.SPADE, Card.Rank.ACE);
        int c3 = PackedCard.pack(Card.Color.SPADE, Card.Rank.SIX);
        s = PackedCardSet.add(s, c1);
        s = PackedCardSet.add(s, c2);
        s = PackedCardSet.add(s, c3);
        System.out.println(PackedCardSet.toString(s));
    }
}
