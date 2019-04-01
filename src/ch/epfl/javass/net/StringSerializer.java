package ch.epfl.javass.net;

import java.util.StringJoiner;

public final class StringSerializer {

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/   
    
    // cannot be instantiated
    private StringSerializer() {};
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
     
    public static String serializeInt(int i) {
        
    }
    
    public static int deserializeInt(String s) {
        return String.
    }
    
    public static String serializeLong(long l ) {
        
    }
    
    public static int deserializeLong(String s) {
        
    }
    
    public static String serializeString(String s) {
        
    }
    
    public static String serializeString(String s) {
        
    }
    
    public static String combine(char  c, String ... s ) {
        StringJoiner j = new StringJoiner(",", "", "");
        for( String ss : s) {
             j.add(ss);
        }
        return j.toString();
        
    }
    
    public static String[] split(String s, char c) {
        return s.split(Character.toString(c));
    }
    
}
