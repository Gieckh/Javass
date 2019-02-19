package ch.epfl.javass.jass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ch.epfl.javass.jass.PackedCard.*;

//TODO: check access rights
/**
 * represents any card of the deck.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class Card {
    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    Color color;
    Rank rank;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    private Card(Color c, Rank r) {
        color = c;
        rank = r;
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/


    public enum Color {
        SPADE  (1), //pique
        HEART  (2), //coeur
        DIAMOND(3), //carreau
        CLUB   (4); //trèfle

        public final int type;
        public final static int COUNT = 4;
        public final static List<Color> ALL =
          Collections.unmodifiableList(Arrays.asList(values()));

        Color(int type) {
            this.type = type;
        }

        /**
         * return the color.
         *
         * @param number
         * @return color
         * @author Antoine Scardigli - (299905)
         * @author Marin Nguyen - (288260)
         */
        public static Color toType(int number) {
            // for all the switches which returns something for any value, it is not necessary to use break
            switch (number) {
            case 1:
                return SPADE;
            case 2:
                return HEART;
            case 3:
                return DIAMOND;
            case 4:
                return CLUB;
            default:
                throw new IllegalArgumentException("this input (" + number + ") doesn't correspond to a color");
            }
        }

        /**
         * returns the character corresponding to the rank of the card
         *
         * @throws IllegalArgumentException
         */
        @Override public String toString() throws IllegalArgumentException {
            switch (this) {
            case SPADE:
                return "\u2660";
            case HEART:
                return "\u2665";
            case DIAMOND:
                return "\u2666";
            case CLUB:
                return "\u2663";
            default:
                throw new IllegalArgumentException(
                        "this thing does not correspond to a color");
            }
        }

    }

    /**
     * manages the ranks of the cards.
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
     */
    public enum Rank {
        /** =============================================== **/
        /** ===============    ATTRIBUTES    ============== **/
        /** =============================================== **/

        SIX   (6),
        SEVEN (7),
        EIGHT (8),
        NINE  (9),
        TEN   (10),
        JACK  (11),
        QUEEN (12),
        KING  (13),
        ACE   (14);

        public int type;
        public final static int COUNT = 9;
        public final static List<Rank> ALL =
          Collections.unmodifiableList(Arrays.asList(values()));


        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/

        private Rank(int type) {
            this.type = type;
        }


        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/

        /**
         * gives the rank corresponding to the number
         *
         * @param number
         * @return Rank
         * @author Antoine Scardigli - (299905)
         * @author Marin Nguyen - (288260)
         */
        static Rank toType(int number) {
            switch (number) {
            case 6:
                return SIX;
            case 7:
                return SEVEN;
            case 8:
                return EIGHT;
            case 9:
                return NINE;
            case 10:
                return TEN;
            case 11:
                return JACK;
            case 12:
                return QUEEN;
            case 13:
                return KING;
            case 14:
                return ACE;
            default:
                //TODO: what to put there?
                throw new IllegalArgumentException("this input (" + number + ") doesn't correspond to a rank");
            }
        }

        /**
         * @return (int) the position of the trump card which has the rank "this"
         *         in the list of trump cards, based on their strength //TODO: better
         * @throws IllegalArgumentException
         * @author Antoine Scardigli - (299905)
         * @author Marin Nguyen - (288260)
         */
        int trumpOrdinal() throws IllegalArgumentException {
            switch (this) {
            case SIX:
                return 0;
            case SEVEN:
                return 1;
            case EIGHT:
                return 2;
            case NINE:
                return 7;
            case TEN:
                return 3;
            case JACK:
                return 8;
            case QUEEN:
                return 4;
            case KING:
                return 5;
            case ACE:
                return 6;
            default:
                //TODO : what to put there ?
                throw new IllegalArgumentException("this input (" + type + ") doesn't correspond to a rank");

            }
        }

        /**
         * returns the character corresponding to the rank of the card
         */
        @Override public String toString() {
            switch (this) {
            case SIX:
                return "6";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            case TEN:
                return "10";
            case JACK:
                return "J";
            case QUEEN:
                return "Q";
            case KING:
                return "K";
            case ACE:
                return "A";
            default:
                throw new IllegalArgumentException(
                        "this thing does not correspond to a rank");
            }
        }
    }

    //TODO: take care of public/private/none
    static Card of(Color c, Rank r) {
        return new Card(c, r);
    }

    static Card ofPacked(int packed) throws IllegalArgumentException {
        if (!isValid(packed)) {
            throw new IllegalArgumentException();
        }

        return new Card(PackedCard.color(packed), PackedCard.rank(packed));
    }


    int packed() {
        return pack(color, rank);
    }

    Color color() {
        return color;
    }

    Rank rank() {
        return rank;
    }

    boolean isBetter(Color trump, Card that) {
        return PackedCard.isBetter(trump, this.packed(), that.packed());
    }

    int points(Color trump) {
        return PackedCard.points(trump, this.packed());
    }


    //TODO
    @Override
    public boolean equals(Object thatO) {
        if (thatO == null  ||  thatO.getClass() != getClass()) { // getClass same as instance of since final ?
            return false;
        }

        Card thatOCard = (Card) thatO; // Or do 2 "conversions, idk"
        return (thatOCard.color == color  &&  thatOCard.rank == rank);
    }

    @Override
    public int hashCode() {
        return packed();
    }

    @Override
    public String toString() {
        return PackedCard.toString(packed());
    }
}
