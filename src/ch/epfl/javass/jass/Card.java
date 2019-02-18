package ch.epfl.javass.jass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Card {
    
    
    
    public enum   Color{
        SPADE   (1),
        HEART   (2),
        DIAMOND (3), 
        CLUB    (4);
        
        final int type;
        final static int COUNT = 4; 
        final static List<Color> list = Arrays.asList(SPADE,HEART,DIAMOND,CLUB);
        final static List<Color>  ALL = Collections.unmodifiableList(list);
        
        Color(int type){
            this.type = type ;
        }
        
        static Color toType(int type) {
            switch(type) {
                case 1:return SPADE;
                case 2:return HEART;
                case 3:return DIAMOND;
                case 4:return CLUB;
            }
            return null;
        }
        @Override
        public String toString() {
            switch(type) {
                case 1:return "\u2660";
                case 2:return "\u2661";
                case 3:return "\u2662";
                case 4:return "\u2663";
            }
            return null;
        }
        
        
    }
    
    public enum   Rank{
        SIX     (6),
        SEVEN   (7),
        EIGHT   (8), 
        NINE    (9),
        TEN     (10),
        JACK    (11),
        QUEEN   (12),
        KING    (13), 
        ACE     (14);

        final int type;
        final static int COUNT = 9; 
        final static List<Rank> list = Arrays.asList(SIX,SEVEN,EIGHT,NINE,TEN,JACK,QUEEN,KING,ACE);
        final static List<Rank>  ALL = Collections.unmodifiableList(list);
        
        Rank(int type){
            this.type = type ;
        }
        
        static Rank toType(int type) {
            switch(type) {
                case 6:return SIX;
                case 7:return SEVEN;
                case 8:return EIGHT;
                case 9:return NINE;
                case 10:return TEN;
                case 11:return JACK;
                case 12:return QUEEN;
                case 13:return KING;
                case 14:return ACE;
                
            }
            return null;
        }

        static int trumpOrdinal(Rank rank) {
            switch(rank) {
                case SIX:return 0;
                case SEVEN:return 1;
                case EIGHT:return 2;
                case NINE:return 7;
                case TEN:return 3;
                case JACK:return 8;
                case QUEEN:return 4;
                case KING:return 5;
                case ACE:return 6;
                
            }
            return -1;
        }

        
        @Override
        public String toString() {
            switch(type) {
                case 6:return "6";
                case 7:return "7";
                case 8:return "8";
                case 9:return "9";
                case 10:return "10";
                case 11:return "J";
                case 12:return "Q";
                case 13:return "K";
                case 14:return "A";
            }
            return null;
        }
        
        
    }


}
