package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits32; //TODO c'est assez bizarre ces import
import static ch.epfl.javass.bits.Bits32.extract;

//TODO: handle access rights
//TODO: check where we assert
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
        System.out.println("extracted rank" + extract(pkCard, 0, 4));
        return (extract(pkCard, 0, 4) < 9 &&  //The rank is valid
                extract(pkCard, 6, 26) == 0); //The 26 "bigger" bits are "0s"
    }


    public static int pack(Card.Color c, Card.Rank r) {
        return Bits32.pack(r.type - 6, 4, c.type, 2);
    }

    
    public static Card.Color color(int pkCard) {
        assert isValid(pkCard);
        /*
        Clearer but (slightly) slower version:
        int intColor = extract(pkCard, 4, 2);
        return Card.Color.toType(intColor);
        */

        return Card.Color.toType(extract(pkCard, 4, 2));
    }
    
    public static Card.Rank rank(int pkCard) {
        assert isValid(pkCard);
        /*
        Clearer but (slightly) slower version:
        int intRank = extract(pkCard, 0, 4);
        return Card.Rank.toTyper(intRank);
         */
        return Card.Rank.toType(extract(pkCard + 6, 0, 4));
    }


     /** returns true if and only if the card represented by the first int is better than the second
     * @param trump
     * @param pkCardL
     * @param pkCardR
     * @return boolean , true in case the card represented by pkCardL is better than the one represented by pkcardR
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
     * IMPORTANT : return false in case an int is not valid
    */
    //TODO  : check whether we have to throw or not , and how we should manage the exceptions.
    public static boolean isBetter(Card.Color trump, int pkCardL, int pkCardR) {
        //if ( !(isValid(pkCardL) && isValid(pkCardR)) ) {
        //    return false;
        //}
        assert (isValid(pkCardL)  &&  isValid(pkCardR));

        Card.Color colorOfL = color(pkCardL);
        Card.Color colorOfR = color(pkCardR);

        Card.Rank rankOfL = rank(pkCardL);
        Card.Rank rankOfR = rank(pkCardR);

        if (colorOfL.equals(trump)) { //TODO: "==" works too i think ? and is less greedy in operation
            if (colorOfR.equals(trump)) {
                return rankOfL.trumpOrdinal() > rankOfR.trumpOrdinal();
            }

            return true;
        }

        if (colorOfL.equals(colorOfR)) {
            return rankOfL.ordinal() > rankOfR.ordinal();
        }

        return false;
    }


    /** returns the value of points of the card corresponding to the int pkCard.
     * @param trump
     * @param pkCard
     * @return the number of points a card worth
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static int points(Card.Color trump, int pkCard) {
        Card.Color color = color(pkCard); 
        Card.Rank rank = rank(pkCard);
        // TODO: switch ?
        /*if((rank.equals(Card.Rank.SIX)||rank.equals(Card.Rank.SEVEN)||rank.equals(Card.Rank.EIGHT))) {
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
        }*/

        switch(rank) {
        case SIX:
        case SEVEN:
        case EIGHT:
            return 0;
        case NINE:
            return color.equals(trump) ? 14 : 0;
        case TEN:
            return 10;
        case JACK:
            return color.equals(trump) ? 20 : 2;
        case QUEEN:
            return 3;
        case KING:
            return 4;
        case ACE:
            return 11;
        default: throw new IllegalArgumentException();
        }
    }
    /**
     * returns the representation of the card empacked, as a string.
     * @param pkCard (int)
     * @return the string of the symbol of the color and the character of the rank
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static String toString(int pkCard) {
        Card.Color color = color(pkCard); 
        Card.Rank rank = rank(pkCard);        
        return color.toString()+rank.toString();
    }
}

