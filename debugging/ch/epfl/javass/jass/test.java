package ch.epfl.javass.jass;

import ch.epfl.javass.jass.Card.Color;

import java.util.Random;
import java.util.SplittableRandom;

import javax.swing.plaf.synth.SynthSplitPaneUI;
//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) {
        SplittableRandom rng =new SplittableRandom(2);
        System.out.println(rng.nextInt(63));
    }
}
