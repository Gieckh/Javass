package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits32; //TODO c'est assez bizarre ces import
import static ch.epfl.javass.bits.Bits32.extract;

//TODO: handle access rights
//TODO: check where we assert (tests must not be done here)
//TODO: use class preconditions
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
        return (extract(pkCard, 0, 4) < 9 &&  //The rank is valid (we don't check color since it can only be valid)
                extract(pkCard, 6, 26) == 0); //The 26 "bigger" bits are "0s"
    }

    /**
     * @brief packs a card, given its color and rank
     * @param c (Color), the color of the card
     * @param r (Rank), the rank of the card
     * @return (int) the corresponding packed card.
     */
    public static int pack(Card.Color c, Card.Rank r) {
        return Bits32.pack(r.type - 6, 4, c.type - 1, 2);
        // "-6" because the rank ranges from 6 to 14 instead of 0 to 8
        // "-1" because the color ranges from 1 to 4 instead of 0 to 3
    }

    
    public static Card.Color color(int pkCard) {
        assert isValid(pkCard);
        /*
        Clearer but (slightly) slower version:
        int intColor = extract(pkCard, 4, 2);
        return Card.Color.toType(intColor);
        */

        return Card.Color.toType(extract(pkCard, 4, 2) + 1);
        // "+1" since toType expects arguments from 1 to 4
        // while our card is encoded from 0 to 3.
        // (We could also add the +1 in the extract) //TODO
    }

    /**
     * @brief Gives the rank of a packed card
     * @param pkCard (int), a card coded by an int (i.e. a packed card)
     * @return (Rank) the rank of this card
     */
    public static Card.Rank rank(int pkCard) { //TODO: erase unnecessary comments
        assert isValid(pkCard);
        /*
        Clearer but (slightly) slower version:
        int intRank = extract(pkCard, 0, 4);
        return Card.Rank.toTyper(intRank + 6);
         */
        return Card.Rank.toType(extract(pkCard, 0, 4) + 6);
        // "+6" since toType expects arguments from 6 to 14
        // while our card is encoded from 0 to 8.
        // (We could also add the +6 in the extract) //TODO
    }

    /**
     * @brief returns true if the packed card "pkCardL" is stronger than the
     *        packed card "pkCardR", false otherwise (weaker or not comparable).
     *
     * @param trump (Color) the color of the trump. Needed to evaluate the strength of a card
     * @param pkCardL (int) the int encoding the first card
     * @param pkCardR (int) the int encoding the second card
     * @return (boolean) true when pkCardL is better than pkCardR.
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static boolean isBetter(Card.Color trump, int pkCardL, int pkCardR) {
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

    /**
     * @brief return the value (nb of points a card is worth) of the (encoded)
     *        card "pkCard", given the trump "trump".
     *
     * @param trump (Color) the color of the trump
     * @param pkCard (int) the int representing a card
     *
     * @return the value of the (encoded) card "pkCard"
     *
     * @author - Antoine Scardigli (299905)
     * @author - Marin Nguyen (288260)
     */
    public static int points(Card.Color trump, int pkCard) {
        Card.Color color = color(pkCard); 
        Card.Rank rank = rank(pkCard);

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
     * @brief the (String) representation of the card :
     *        "[its suit]" then "[its rank]", without spaces in between.
     *
     * @param pkCard (int) the int encoding a card
     * @return the (String) representation of the card.
     *
     * @author - Antoine Scardigli (299905)
     * @author - Marin Nguyen (288260)
     */
    public static String toString(int pkCard) {
        return color(pkCard).toString() + rank(pkCard).toString();
    }
}

