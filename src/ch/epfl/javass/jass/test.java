package ch.epfl.javass.jass;

import ch.epfl.javass.jass.Card.Color;

import java.util.Random;

public class test {
    public static void main(String[] args) {
        Random rng = new Random(2019);
        for (int i = 0; i < 5; ++i)
            System.out.println(rng.nextInt(1000));


        System.out.println(1 << 31 >> 2);
        System.out.println(1 << 31 >>> 2);
    }
}
