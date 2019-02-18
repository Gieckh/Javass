package ch.epfl.javass.jass;

import static ch.epfl.javass.bits.Bits32.extract;

public final class PackedCard {
    public final static int INVALID = 111111;
    
    
    public static boolean isValid(int pkCard) {
        /*
        Clearer but slower version:
        int rank = extract(pkCard, 0, 4);
        int restOfTheCard = extract(pkCard, 6, 26); //26 = 31 - 6 + 1
        return (rank < 9 && restOfTheCard == 0);
        */

        // Since we want to be fast
        return (extract(pkCard, 0, 4) <= 4 &&
                extract(pkCard, 6, 26) == 0);
    }
    
    public static int pack(Card.Color c, Card.Rank r) {
        int rank =

        return 0;
    }
    
    public static Card.Color color(int pkCard) {
        return null;
    }
    
    public static Card.Rank rank(int pkCard) {
        return null;
    }
    
    public static boolean isBetter (Card.Color trump, int pkCardL, int pkCardR) {
        return false;
    }
    
    public static int points(Card.Color trump, int pkCard) {
        return 0;
    }
    
    public static String toString(int pkCard) {
        return "";
    }
    
}

