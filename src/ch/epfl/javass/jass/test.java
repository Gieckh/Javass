package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class test {
    public static void main(String[] args) {
        List<Integer> l1 = new ArrayList<>();
        List<Integer> l2 = new ArrayList<>();

        for (int i = 0 ; i < 8 ; ++i) {
            l1.add(i);
            l2.add(i);
        }

        List<Integer> subL1 = l1.subList(0, 3);
        List<Integer> subL2 = l1.subList(0, 3);

        System.out.println("      l1 == l2 ? : " + (l1 == l2));
        System.out.println("subL1 == subL2 ? : " + (subL1 == subL2));
        System.out.println();
        System.out.println("      l1 == l1 ? : " + (l1 == l1));
        System.out.println("subL1 == subL1 ? : " + (subL1 == subL1));
        System.out.println();
        System.out.println("   l1 : " + l1);
        System.out.println("subL1 : " + subL1);
    }
}
