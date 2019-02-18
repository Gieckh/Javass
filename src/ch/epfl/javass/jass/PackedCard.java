package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits32; //TODO c'est assez bizarre.
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
        return Bits32.pack(r.type, 4, c.type, 2);
    }

    
    public static Card.Color color(int pkCard) {
        /*
        Clearer but (slightly) slower version:
        int color = extract(pkCard, 4, 2);
        return Card.Color.toType(color);
        */

        return Card.Color.toType(extract(pkCard, 4, 2));
    }
    
    public static Card.Rank rank(int pkCard) {
        int rank = extract(pkCard, 0, 4);
        return Card.Rank.toType(rank + 6);
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

