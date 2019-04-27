package ch.epfl.javass.jass;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

//this is  a test, thus no need of javadoc
public class test {
    public static void main(String[] args) {
        ObjectProperty<String> p1 = new SimpleObjectProperty<>();
        ObjectProperty<String> p2 = new SimpleObjectProperty<>();

        p1.addListener((o, oV, nV) -> System.out.println(nV));
        p1.addListener((o, oV, nV) -> System.out.println(oV));
        p1.addListener((o, oV, nV) -> System.out.println(""));

        p2.set("hello");                // n'affiche rien
        System.out.println("--- binding p1 to p2");
        p1.bindBidirectional(p2);       // affiche "hello"
        p2.set("world");                // affiche "world"
        p1.set("bonjour");
    }
}
