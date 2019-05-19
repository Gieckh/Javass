package ch.epfl.javass.jass;

import bonus.JassReductorOfSet;
import ch.epfl.javass.PlayerType;

import java.util.Arrays;
import java.util.List;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) {
        String s = "a::b";
        List<String> ls = Arrays.asList(s.split(":"));
        System.out.println(ls);
    }
}
