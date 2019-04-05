package ch.epfl.javass.net;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringJoiner;


public final class StringSerializer {

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/   
    
    // cannot be instantiated
    /**
     * 
     */
    private StringSerializer() {};
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
     
    public  static String serializeInt(int i) {
        return Integer.toUnsignedString(i, 16);
    }

    public static int deserializeInt(String s) {
        return Integer.parseUnsignedInt(s, 16);
    }
    
    public static String serializeLong(long l ) {
        return Long.toUnsignedString(l, 16);
    }

    public static long deserializeLong(String s) {
        return Long.parseUnsignedLong(s, 16);
    }
    
    public static String serializeString(String s) {
        return Base64.getEncoder().encodeToString((s.getBytes(StandardCharsets.UTF_8)));
    }
    
    public static String deserializeString(String s) {
        return new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8);
    }
    
    public static String combine(char  c, String ... s ) {
        //TODO: suppr
//        StringJoiner j = new StringJoiner(",", "", "");
//        for( String str : s) {
//             j.add(str);
//        }
//        return j.toString();
        return String.join(",", s);
    }
    
    public static String[] split(String s, char c) {
        return s.split(Character.toString(c));
    }
    
}
