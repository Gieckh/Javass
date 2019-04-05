package ch.epfl.javass.net;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

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
        return Base64.encode(s.getBytes(StandardCharsets.UTF_8));
    }
    
    public static String deserializeString(String s) throws Base64DecodingException {
        return new String(Base64.decode(s), StandardCharsets.UTF_8);
    }
    
    public static String combine(char  c, String ... s ) {
        StringJoiner j = new StringJoiner(",", "", "");
        for( String str : s) {
             j.add(str);
        }
        return j.toString();        
    }
    
    public static String[] split(String s, char c) {
        return s.split(Character.toString(c));
    }
    
}
