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
        assert isValid(pkCard);
        /*
        Clearer but (slightly) slower version:
        int color = extract(pkCard, 4, 2);
        return Card.Color.toType(color);
        */

        return Card.Color.toType(extract(pkCard, 4, 2));
    }
    
    public static Card.Rank rank(int pkCard) {
        assert isValid(pkCard);
        int rank = extract(pkCard, 0, 4);
        return Card.Rank.toType(rank + 6);
    }
    
    public static boolean isBetter(Card.Color trump, int pkCardL, int pkCardR) {
        if (!(isValid(pkCardL) || (!isValid(pkCardR)))) {
            return false;
        }
        Card.Color colorOfL = color(pkCardL); 
        Card.Rank rankOfL = rank(pkCardL);
        Card.Color colorOfR = color(pkCardR);
        Card.Rank  rankOfR = rank(pkCardR);
        if ((colorOfL.equals(trump)&&(!(colorOfR.equals(trump))))){
            return true;
        }
        if ((!(colorOfL.equals(trump))&&(colorOfR.equals(trump)))){
            return false;
        }
        if ((colorOfL.equals(trump)&&(colorOfR.equals(trump)))){
            return (rankOfL.trumpOrdinal() > rankOfR.trumpOrdinal());
        }
        else {
            return rankOfL.type>rankOfR.type;
        }
        
    }

    public static int points(Card.Color trump, int pkCard) {
        Card.Color color = color(pkCard); 
        Card.Rank rank = rank(pkCard);
        // TODO: switch ?
        if((rank.equals(Card.Rank.SIX)||rank.equals(Card.Rank.SEVEN)||rank.equals(Card.Rank.EIGHT))) {
            return 0;
        }
        if((rank.equals(Card.Rank.TEN))) {
            return 10;
        }
        if((rank.equals(Card.Rank.QUEEN))) {
            return 3;
        }
        if((rank.equals(Card.Rank.KING))) {
            return 4;
        }
        if((rank.equals(Card.Rank.ACE))) {
            return 11;
        }


        if(color.equals(trump)) {
            return (rank.equals(Card.Rank.NINE))? 14 : 20;
        }

        else {
            return (rank.equals(Card.Rank.NINE)) ? 0 : 2;
        }
    }

    public static String toString(int pkCard) {
        Card.Color color = color(pkCard); 
        Card.Rank rank = rank(pkCard);        
        return color.toString()+rank.toString();
    }
}

