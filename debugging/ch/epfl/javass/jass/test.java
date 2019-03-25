package ch.epfl.javass.jass;
import java.util.SplittableRandom;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) {
        SplittableRandom rng =new SplittableRandom(2);
        System.out.println(rng.nextInt(63));
    }
}
