//    package ch.epfl.javass.jass;
//
//    import java.util.ArrayList;
//    import java.util.SplittableRandom;
//
//    import static org.junit.jupiter.api.Assertions.assertEquals;
//    import static org.junit.jupiter.api.Assertions.assertFalse;
//    import static org.junit.jupiter.api.Assertions.assertTrue;
//
//    import static ch.epfl.javass.jass.PackedTrick.isValid;
//    import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
//    import static ch.epfl.test.TestRandomizer.newRandom;
//
//    import static org.junit.jupiter.api.Assertions.*;
//    import org.junit.jupiter.api.Test;
//
//    import ch.epfl.javass.bits.Bits32;
//    import ch.epfl.javass.bits.Bits64;
//    import ch.epfl.javass.jass.Card.Color;
//    import ch.epfl.javass.jass.Card.Rank;
//    import ch.epfl.javass.jass.TeamId;
//
//
//public class TrickTest {
//
//
//
//
//        @Test
//        void winningPlayerTest() {
//            int playerId = 0;
//            int counter = 0;
//            ArrayList<Integer> cards =  new ArrayList<>();
//            Color trump ;     
//            int noIdea = 0;
//            boolean winnter = false;
//            boolean is = false;
//            boolean comming = false;
//            int winningCard;
//            int nbCard = -1;
//            for (int i = 0 ; i != -1 ; ++i) {
//                if ( PackedTrick.isValid(i)&&((Bits32.extract(i, 0, 6) != 0b111111)||Bits32.extract(i, 6, 6) != 0b111111||Bits32.extract(i, 12, 6) != 0b111111||Bits32.extract(i, 18, 6) != 0b111111)){
//                   cards.removeAll(cards);
//                    counter = 0;
//                    playerId = Bits32.extract(i, 28, 2);
//                    trump = Card.Color.ALL.get(Bits32.extract(i, 30, 2));
//                    for (int j = 0; j< 4 ; ++j) {
//                       if (Bits32.extract(i, 6*j, 6)!=0b111111) {
//                          cards.add(Bits32.extract(i, 6*j, 6));
//                          counter +=1;
//                       }
//                    }
//                    winningCard = cards.get(0);
//                    for (int k = 0; k< counter-1 ; ++k) {
//                        if(PackedCard.isBetter(trump, cards.get(k+1), winningCard)) {
//                            winningCard = cards.get(k+1);
//                        }
//                    }
//                    noIdea = cards.indexOf(winningCard);
//                    
//                    assertEquals((noIdea+playerId) %4, i.winningPlayer().ordinal());
//
//                    }
//                }
//            }
//        
//
//      
//
//            @Test
//            void playableCardTestUnit2() {
//                int pkTrick1 = Trick.firstEmpty(Card.Color.SPADE,
//                        PlayerId.PLAYER_1);
//                long pkHand1 = 0b0000_0000_0010_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000L;
//                int pkTrick2 = Bits32.pack(0b110, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand2 = 0b0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_1010_0000_0000_0010_0000L;
//                int pkTrick3 = Bits32.pack(0b110, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand3 = 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_1010_0000L;
//                int pkTrick4 = Bits32.pack(0b10_0110, 6, 0b0, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand4 = 0b0000_0000_0010_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0010L;
//                int pkTrick5 = Bits32.pack(0b11_0001, 6, 0b1_0011, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0b1,
//                        2);
//                long pkHand5 = 0b0000_0000_0000_0000_0000_0000_1000_0000_0000_0000_0000_0100_0000_0000_1000_0001L;
//                int pkTrick6 = Bits32.pack(0b1_0101, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand6 = 0b0000_0000_0000_0001_0000_0000_0001_0000_0000_0000_1000_0000_0000_0000_0000_1000L;
//                int pkTrick7 = Bits32.pack(0b11_0011, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0b11,
//                        2);
//                long pkHand7 = 0b0000_0000_0000_0000_0000_0000_1000_1000_0000_0000_0100_0000_0000_0001_0000_0000L;
//                int pkTrick8 = Bits32.pack(0b1000, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0b10,
//                        2);
//                long pkHand8 = 0b0000_0000_0000_0000_0000_0000_0010_0010_0000_0000_0100_0000_0000_0000_1000_0000L;
//                int pkTrick9 = Bits32.pack(0b11_0100, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0b10,
//                        2);
//                long pkHand9 = 0b0000_0000_0000_0010_0000_0000_0000_1000_0000_0000_0001_0001_0000_0000_0000_0000L;
//                int pkTrick10 = Bits32.pack(0b10_0100, 6, 0b0100, 6, Card.INVALID,
//                        6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand10 = 0b0000_0000_1000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0000L;
//                int pkTrick11 = Bits32.pack(0b10_0100, 6, 0b0100, 6, Card.INVALID,
//                        6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand11 = 0b0000_0000_0000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0100L;
//                System.out.println("{\u266110 \u2663J} expected\n" + CardSet
//                        .toString(Trick.playableCards(pkTrick1, pkHand1)));
//                System.out.println();
//                System.out.println("{\u2660J \u26617 \u26619 \u266310} expected\n"
//                        + CardSet.toString(
//                                Trick.playableCards(pkTrick2, pkHand2)));
//                System.out.println();
//                System.out.println("{\u2660J \u2660K} expected\n" + CardSet
//                        .toString(Trick.playableCards(pkTrick3, pkHand3)));
//                System.out.println();
//                System.out.println("{\u26607 \u266210} expected\n" + CardSet
//                        .toString(Trick.playableCards(pkTrick4, pkHand4)));
//                System.out.println();
//                System.out.println("{\u26606 \u2660K \u2662K} expected\n"
//                        + CardSet.toString(
//                                Trick.playableCards(pkTrick5, pkHand5)));
//                System.out.println();
//                System.out.println("{\u26609 \u2661K} expected\n" + CardSet
//                        .toString(Trick.playableCards(pkTrick6, pkHand6)));
//                System.out.println();
//                System.out.println("{\u2660A \u2661Q \u26629 \u2662K} expected\n"
//                        + CardSet.toString(
//                                Trick.playableCards(pkTrick7, pkHand7)));
//                System.out.println();
//                System.out.println(
//                        "{\u2660K \u26627 \u2662J} expected\n" + CardSet.toString(
//                                Trick.playableCards(pkTrick8, pkHand8)));
//                System.out.println();
//                System.out.println("{\u26629 \u26637} expected\n" + CardSet
//                        .toString(Trick.playableCards(pkTrick9, pkHand9)));
//                System.out.println();
//                System.out.println("{\u2662J \u2662K} expected\n" + CardSet
//                        .toString(Trick.playableCards(pkTrick10, pkHand10)));
//                System.out.println();
//                System.out.println("{\u2662J \u2662K} expected\n" + CardSet
//                        .toString(Trick.playableCards(pkTrick11, pkHand11)));
//            }
//        
//        
//        @Test
//        void playableCardsWorks() {
//            // deux cartes jouables: valet de pique, as de coeur
//            // en main: 8 de pique, valet de pique, as de coeur, 7 de trèfle
//            // premiere carte jouée: 6 de coeur, as de pique, 6 de trèfle, atout pique
//            assertEquals(0b100000000_0000000000100000L, Trick.playableCards(0b00_11_0000_111111_110000_001000_010000, 0b10_0000000000000000_0000000100000000_0000000000100100L));
//            
//            // en main: 9 de pique, valet de pique, 6 de coeur, 7 coeur;
//            // premiere carte jouée: 10 de coeur, atout carreau
//            assertEquals(0b11_0000000000000000L, Trick.playableCards(0b10_11_0000_111111_111111_111111_010100, 0b00_0000000000000000_0000000000000011_0000000000101000L));
//            
//            // en main: 6 de pique, 7 de pique, 8 de pique, 6 de coeur
//            // premiere carte jouée: 9 de pique, 2nd: 10 de pique, atout pique
//            assertEquals(0b0_0000000000000111L, Trick.playableCards(0b00_11_0000_111111_111111_000100_000011,0b00_0000000000000000_0000000000000001_0000000000000111L));
//            
//            // en main: 6 de pique, 7 de pique, 8 de pique, 6 de coeur
//            // premiere carte jouée: 9 de carreau, 2nd: 10 de pique, atout carreau
//            assertEquals(0b1_0000000000000111L, Trick.playableCards(0b10_11_0000_111111_111111_000100_100011,0b00_0000000000000000_0000000000000001_0000000000000111L));
//            
//            // en main: 6 de pique, 7 de pique, 8 de pique, as de pique
//            // premiere carte jouée: 9 de carreau, 2nd: 10 de pique, atout pique
//            assertEquals(0b0_0000000100000000L, Trick.playableCards(0b00_11_0000_111111_111111_000100_100011,0b00_0000000000000000_0000000000000000_0000000100000111L));
//            
//            //atout pas demandé, mais pas coupé, et peut pas suivre
//            assertEquals(0b1000000000000000000000001000000000000000000100100L, Trick.playableCards(0b00_11_0000_111111_110001_010000_100000, 0b1000000000000000000000001000000000000000000100100L));
//            assertEquals(0b1000000000000000000100000L, Trick.playableCards(0b00_10_0000_111111_111111_001000_010000, 0b1000000000000000000000001000000000000000000100100L));
//        
//            // que des atouts dans la main, pas atout demandé, mais qqn coupe et pas moyen de surcouper
//            // en main: 6, 7 et 8 de pique
//            // première carte jouée: 9 de carreau, deuxieme carte jouée: 10 de pique, atout pique
//            assertEquals(0b0_0000000000000111L, Trick.playableCards(0b00_11_0000_111111_111111_000100_100011,0b00_0000000000000000_0000000000000000_0000000000000111L));
//            
//            // quand qqn a couper, tu peux pas sous couper, tu peux pas suivre, et t'as une carte qui n'est pas d'atout
//            // 6, 7 ,8 de pique et 6 de coeur, atout pique,
//            // premiere carte jouee 9 de carreau, deuxieme carte 10 de pique
//            assertEquals(0b1_0000000000000000L, Trick.playableCards(0b00_11_0000_111111_111111_000100_100011,0b00_0000000000000000_0000000000000001_0000000000000111L));
//            
//            // qqn a coupé, peut surcouper, peut pas suivre
//            // 6, 7 ,9 de pique et 6 de coeur, atout pique,
//            // premiere carte jouee 9 de carreau, deuxieme carte 10 de pique
//            assertEquals(0b1_0000000000001000L, Trick.playableCards(0b00_11_0000_111111_111111_000100_100011,0b00_0000000000000000_0000000000000001_0000000000001011L));
//            
//            // qqn a coupé, pas surcouper, peut suivre
//            // 6, 7 ,8 de pique et 6 de coeur, atout pique,
//            // premiere carte jouee 9 de coeur, deuxieme carte 10 de pique
//            assertEquals(0b1_0000000000000000L, Trick.playableCards(0b00_11_0000_111111_111111_000100_010011,0b00_0000000000000000_0000000000000001_0000000000000111L));
//            
//            // on est le quatrieme joueur, les deux joueurs precedents ont coupe
//            // 6, 7 ,8 de pique et 6 de coeur, atout pique,
//            // premiere carte jouee 9 de coeur, deuxieme carte 10 de pique, troisième carte bourre
//            assertEquals(0b1_0000000000000000L, Trick.playableCards(0b00_11_0000_111111_000101_000100_010011,0b00_0000000000000000_0000000000000001_0000000000000111L));
//            
//            // on est le quatrieme joueur, les deux joueurs precedents ont coupe
//            // 6, 7 ,9 de pique et 6 de coeur, atout pique,
//            // premiere carte jouee 9 de coeur, deuxieme carte 10 de pique, troisième carte bourre
//            assertEquals(0b1_0000000000000000L, Trick.playableCards(0b00_11_0000_111111_000101_000100_010011,0b00_0000000000000000_0000000000000001_0000000000001011L));
//            
//            // qqn a coupé, peut surcouper, peut suivre
//            // 6, 7 ,9 de pique et 6 de coeur, atout pique
//            // premiere carte jouee 9 de coeur, deuxieme carte 10 de pique
//            assertEquals(0b00_0000000000000000_0000000000000001_0000000000001000L, Trick.playableCards(0b00_11_0000_111111_111111_000100_010011, 0b00_0000000000000000_0000000000000001_0000000000001011L));
//            
//            // qqn a coupé, pas d'atout plus haut, soit autre couleur, pas de couleur de base
//            // 6, 7 ,8 de pique et 6 de coeur, atout pique
//            // premiere carte jouee 9 de carreau, deuxieme carte 10 de pique
//            assertEquals(0b1_0000000000000000L, Trick.playableCards(0b00_11_0000_111111_111111_000100_100011, 0b00_0000000000000000_0000000000000001_0000000000000111L));
//            
//            
//            //cas normal: atout demandé, tout le monde a atout, atout pique
//            // en main: 6, 7, 8 de pique, 6 de coeur
//            // première carte jouée: 9 de pique, après 10 de pique, après bourre
//            assertEquals(0b0_0000000000000111L, Trick.playableCards(0b00_11_0000_111111_000101_000100_000011, 0b00_0000000000000000_0000000000000001_0000000000000111L));
//            
//            //personne n'a coupé, on a de l'atout
//            // en main: 6, 7, 8 de pique
//            // première carte jouée: 9 de coeur, après 10 de coeur, après valet de coeur
//            assertEquals(0b0_0000000000000111L, Trick.playableCards(0b00_11_0000_111111_010101_010100_010011, 0b00_0000000000000000_0000000000000000_0000000000000111L));
//            
//            // peut couper mais peut pas suivre
//            // en main: 6, 7, 8 de pique, 6 de coeur
//            // première carte jouée: 9 de coeur, après 10 de coeur, après valet de coeur
//            assertEquals(0b1_0000000000000111L, Trick.playableCards(0b00_11_0000_111111_010101_010100_010011, 0b00_0000000000000000_0000000000000001_0000000000000111L));
//            
//            // cas de la dernière pli, on peut jouer tout ce qu'on a en main
//            // en main: 6 de pique
//            // première carte jouée: 7 de coeur, atout coeur
//            assertEquals(0b0_0000000000000001L, Trick.playableCards(0b01_11_0000_111111_111111_111111_010001, 0b00_0000000000000000_0000000000000000_0000000000000001L));
//            
//            // cas de la dernière pli, on peut jouer tout ce qu'on a en main
//            // en main: 6 de pique
//            // première carte jouée: 7 de coeur, deuxieme carte: 8 de carreau atout coeur
//            assertEquals(0b0_0000000000000001L, Trick.playableCards(0b01_11_0000_111111_111111_100011_010001, 0b00_0000000000000000_0000000000000000_0000000000000001L));
//            
//            // cas de la dernière pli, on peut jouer tout ce qu'on a en main
//            // en main: 6 de pique
//            // première carte jouée: 7 de coeur, deuxieme carte: 8 de carreau, troisieme carte: as de trefle, atout coeur
//            assertEquals(0b0_0000000000000001L, Trick.playableCards(0b01_11_0000_111111_111000_100011_010001, 0b00_0000000000000000_0000000000000000_0000000000000001L));
//     
//            
//            // jamais une main vide, set retourné toujours un subset de la main donnée
//            
//            ArrayList<Integer> cards = new ArrayList<>();
//            for(int i = 0; i<Card.Rank.COUNT; ++i) {
//                for(int j = 0; j<Card.Color.COUNT; ++j) {
//                    cards.add(Card.pack(Color.ALL.get(j), Rank.ALL.get(i)));
//                }
//            }
//            for(int i = 1; i<CardSet.subsetOfColor(CardSet.ALL_CARDS, Color.SPADE); ++i) {
//                for(int j = 0; j<cards.size(); ++j) {
//                    for(int m = 0; m<cards.size(); ++m) {
//                    assertTrue(Trick.playableCards((0b00_11_0000_111111_111111 << 12)|(cards.get(j)<<6)|cards.get(m), ((i<<48)|(i<<32)|(i<<16)|i))>0);
//                    assertTrue((Trick.playableCards((0b00_11_0000_111111_111111 << 12)|(cards.get(j)<<6)|cards.get(m), ((i<<48)|(i<<32)|(i<<16)|i)) & ((i<<48)|(i<<32)|(i<<16)|i)) > 0);
//                    }
//                }
//            }
//        }
//            @Test
//            void playableCardTestUnit() {
//                int pkTrick1 = Trick.firstEmpty(Card.Color.SPADE,
//                        PlayerId.PLAYER_1);
//                long pkHand1 = 0b0000_0000_0010_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000L;
//                int pkTrick2 = Bits32.pack(0b110, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand2 = 0b0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_1010_0000_0000_0010_0000L;
//                int pkTrick3 = Bits32.pack(0b110, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand3 = 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_1010_0000L;
//                int pkTrick4 = Bits32.pack(0b10_0110, 6, 0b0, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand4 = 0b0000_0000_0010_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0010L;
//                int pkTrick5 = Bits32.pack(0b11_0001, 6, 0b1_0011, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0b1,
//                        2);
//                long pkHand5 = 0b0000_0000_0000_0000_0000_0000_1000_0000_0000_0000_0000_0100_0000_0000_1000_0001L;
//                int pkTrick6 = Bits32.pack(0b1_0101, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand6 = 0b0000_0000_0000_0001_0000_0000_0001_0000_0000_0000_1000_0000_0000_0000_0000_1000L;
//                int pkTrick7 = Bits32.pack(0b11_0011, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0b11,
//                        2);
//                long pkHand7 = 0b0000_0000_0000_0000_0000_0000_1000_1000_0000_0000_0100_0000_0000_0001_0000_0000L;
//                int pkTrick8 = Bits32.pack(0b1000, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0b10,
//                        2);
//                long pkHand8 = 0b0000_0000_0000_0000_0000_0000_0010_0010_0000_0000_0100_0000_0000_0000_1000_0000L;
//                int pkTrick9 = Bits32.pack(0b11_0100, 6, Card.INVALID, 6,
//                        Card.INVALID, 6, Card.INVALID, 6, 0, 4, 0, 2, 0b10,
//                        2);
//                long pkHand9 = 0b0000_0000_0000_0010_0000_0000_0000_1000_0000_0000_0001_0001_0000_0000_0000_0000L;
//                int pkTrick10 = Bits32.pack(0b10_0100, 6, 0b0100, 6, Card.INVALID,
//                        6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand10 = 0b0000_0000_1000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0000L;
//                int pkTrick11 = Bits32.pack(0b10_0100, 6, 0b0100, 6, Card.INVALID,
//                        6, Card.INVALID, 6, 0, 4, 0, 2, 0, 2);
//                long pkHand11 = 0b0000_0000_0000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0100L;
//                assertEquals("{\u266110,\u2663J}" ,CardSet
//                        .toString(Trick.playableCards(pkTrick1, pkHand1)));
//                assertEquals("{\u2660J,\u26617,\u26619,\u266310}",CardSet.toString(
//                                Trick.playableCards(pkTrick2, pkHand2)));
//                assertEquals("{\u2660J,\u2660K}" ,CardSet
//                        .toString(Trick.playableCards(pkTrick3, pkHand3)));
//                assertEquals("{\u26607,\u266210}" ,CardSet
//                        .toString(Trick.playableCards(pkTrick4, pkHand4)));
//                assertEquals("{\u26606,\u2660K,\u2662K}",
//                         CardSet.toString(
//                                Trick.playableCards(pkTrick5, pkHand5)));
//                assertEquals("{\u26609,\u2661K}" ,CardSet
//                        .toString(Trick.playableCards(pkTrick6, pkHand6)));
//                assertEquals("{\u2660A,\u2661Q,\u26629,\u2662K}",
//                        CardSet.toString(
//                                Trick.playableCards(pkTrick7, pkHand7)));
//                assertEquals(
//                       "{\u2660K,\u26627,\u2662J}" ,CardSet.toString(
//                                Trick.playableCards(pkTrick8, pkHand8)));
//                assertEquals("{\u26629,\u26637}" ,CardSet
//                        .toString(Trick.playableCards(pkTrick9, pkHand9)));
//                assertEquals("{\u2662J,\u2662K}" ,CardSet
//                        .toString(Trick.playableCards(pkTrick10, pkHand10)));
//                assertEquals("{\u2662J,\u2662K}" ,CardSet
//                        .toString(Trick.playableCards(pkTrick11, pkHand11)));
//            }
//        
//
//
//
//        
//        @Test
//        void isValidWorks() {
//            for(int i = 0; i != -1 ; ++i) {
//                assertEquals(IsValidTest(i),PackedTrick.isValid(i));
//            }
//        }
//
//        private boolean IsValidTest(int pkTrick) {
//            if((Bits32.extract(pkTrick, 24, 4)<9)) {
//                int Card0 = Bits32.extract(pkTrick, 0, 6);
//                int Card1 = Bits32.extract(pkTrick, 6, 6);
//                int Card2 = Bits32.extract(pkTrick, 12, 6);
//                int Card3 = Bits32.extract(pkTrick, 18, 6);
//                if ((Card.isValid(Card0)&&Card.isValid(Card1)&&Card.isValid(Card2)&&Card.isValid(Card3))){
//                    return true;
//                }
//                if ((Card.isValid(Card0)&&Card.isValid(Card1)&&Card.isValid(Card2)&&(Card3==0b111111))){
//                    return true;
//                }
//                if ((Card.isValid(Card0)&&Card.isValid(Card1)&&(Card2==0b111111)&&(Card3==0b111111))){
//                    return true;
//                }
//                if ((Card.isValid(Card0)&&(Card1==0b111111)&&(Card2==0b111111))&&(Card3==0b111111)){
//                    return true;
//                }
//                if ((Card0==0b111111)&&(Card1==0b111111)&&(Card2==0b111111)&&(Card3==0b111111)){
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        @Test
//        void firstEmptyWorks() {
//            for(int j = 0; j<4; ++j) {
//                    for (int i = 0; i<4; ++i) {
//                        int trick =  Trick.firstEmpty(Color.ALL.get(j), PlayerId.ALL.get(i));
//                        int shouldBe1= Bits32.extract(trick, 0, 24);
//                        int shouldBe0= Bits32.extract(trick, 24, 4);
//                        int shouldBePlayer = Bits32.extract(trick, 28, 2);
//                        int shouldBeColor = Bits32.extract(trick, 30, 2);
//                        assertTrue( (shouldBeColor == (j)));
//                        assertTrue((shouldBePlayer == (i)));
//                        assertTrue(shouldBe0 ==0);
//                        assertTrue(shouldBe1 == 0b111111111111111111111111);
//                    }
//            }
//        }
//
//        @Test
//        void nextEmptyWorks() {
//            for (int i = 0 ; i != -1; ++i ) {
//                if (PackedTrick.isValid(i) && (Bits32.extract(i, 0, 6)!=0b111111) &&
//                        (Bits32.extract(i, 6, 6)!=0b111111) &&
//                        (Bits32.extract(i, 12, 6)!=0b111111) &&
//                        (Bits32.extract(i, 18, 6)!=0b111111))
//                {
//                    if(Bits32.extract(i, 24, 4) == 8) {
//                        assertEquals(Trick.INVALID, Trick.nextEmpty(i));
//                    }
//                    else {
//                        int nextTrick = Trick.nextEmpty(i);
//
//                        assertEquals(Bits32.extract(nextTrick, 0, 24), 0b111111111111111111111111);
//                        assertEquals(Bits32.extract(i, 30, 2), Bits32.extract(nextTrick, 30, 2));
//                        assertEquals(Trick.winningPlayer(i), PlayerId.ALL.get(Bits32.extract(nextTrick, 28, 2)));
//                        assertEquals(Bits32.extract(i, 24, 4) +1  , Bits32.extract(nextTrick, 24, 4));
//                    }
//                }
//            }
//        }
//        
//        @Test
//        void sizeWorks() {
//            int shouldBeThisSize ;
//            for(int i = 0; i!= -1 ; ++i) {
//                shouldBeThisSize = 0;
//                if ( PackedTrick.isValid(i)) {
//                    for (int j = 0 ; j<4 ;++j ) {
//                        if (Bits32.extract(i, 6*j, 6)!= 0b111111) {
//                            shouldBeThisSize +=1;
//                        }
//                    }
//                    assertEquals(shouldBeThisSize, Trick.size(i));
//                }
//            }
//        }
//        
//        @Test
//        void cardWorks() {
//            for(int i = 0; i!= -1 ; ++i) {
//                if ( PackedTrick.isValid(i)) {
//                    for (int j = 0 ; j<4 ;++j ) {
//                        int shouldBeThatCard = Bits32.extract(i, 6*j, 6); 
//                        if(Card.isValid(shouldBeThatCard)) {
//                            assertEquals(shouldBeThatCard, Trick.card(i,j));
//
//                        }
//                    }
//                }
//            }
//        }
//
//        @Test
//        void withAddedCardWorks() {
//            int k;
//            int index;
//            int ones = 0b111111;
//            for (int i = 0; i!=-1; ++i) {
//                if(PackedTrick.isValid(i)&&((i&0b111111000000000000000000)==(0b111111<<18))) {
//                    for (int j = 0 ; j < 64; ++j) {
//                        if (Card.isValid(j)) {
//                            k = 0;
//                            index = 0;
//                            while(k<4) {
//                                if(Bits32.extract(i, k*6, 6)==0b111111) {
//                                    index = k;
//                                    k=5;
//                                }
//                                else {
//                                    k++;
//                                }
//                            }
//
//                           assertEquals((i-(ones<<6*index))+(j<<6*index), Trick.withAddedCard(i, j));
//                        }
//                    }
//                }
//            }
//        }
//
//        @Test
//        void isLastWorks(){
//            for (int i = 0 ; i != -1; ++i ) {
//                if (PackedTrick.isValid(i)) {
//                    if(Bits32.extract(i, 24,4)==8) {
//                        assertTrue(Trick.isLast(i));
//                    }
//                    else {
//                        assertFalse(Trick.isLast(i));
//                    }
//                }
//            }
//        }
//        
//        @Test
//        void isEmptyWorks(){
//            for (int i = 0 ; i != -1; ++i ) {
//                if (PackedTrick.isValid(i)) {
//                    if(Bits32.extract(i, 0,24)==0b111111111111111111111111) {
//                        assertTrue(Trick.isEmpty(i));
//                    }
//                    else {
//                        assertFalse(Trick.isEmpty(i));
//                    }
//                }
//            }
//        }
//
//        @Test
//        void isFullWorks(){
//            for (int i = 0 ; i != -1; ++i ) {
//                if (PackedTrick.isValid(i)) {
//                    if((Bits32.extract(i, 0 ,4) < 9) && (Bits32.extract(i, 6 ,4) < 9) &&
//                       (Bits32.extract(i, 12,4) < 9) && (Bits32.extract(i, 18,4) < 9)) {
//                        assertTrue(Trick.isFull(i));
//                    }
//
//                    else {
//                        assertFalse(Trick.isFull(i));
//                    }
//                }
//            }
//        }
//        
//        @Test
//        void trumpWorks(){
//            for (int i = 0 ; i != -1 ; ++i){
//                if (PackedTrick.isValid(i)) {
//                    int colorIndex = Bits32.extract(i, 30, 2);
//                    assertEquals(Color.ALL.get(colorIndex), Trick.trump(i));
//                }
//            }
//        }
//        
//            @Test
//            void playerWorks(){
//                for ( int i = 0 ; i != -1 ; ++i){
//                    if (PackedTrick.isValid(i)) {
//                        int playerIndex = Bits32.extract(i, 28, 2);
//                        for ( int j = 0; j < 4; ++j) {
//                                assertEquals(PlayerId.ALL.get((playerIndex + j)%4), Trick.player(i,j));
//                            
//        
//                       }
//                    }
//                }
//            }
//        
//        @Test
//        void indexWorks(){
//            for ( int i = 0 ; i != -1 ; ++i){
//                if (PackedTrick.isValid(i)) {
//                    int index = Bits32.extract(i, 24, 4);
//                        assertEquals(index , Trick.index(i));
//                }
//            }
//       }
//        @Test
//        void baseColorWorks(){
//            for ( int i = 0 ; i != -1 ; ++i){
//                if (PackedTrick.isValid(i)) {
//                    int shouldBeThatColor = Bits32.extract(i, 4, 2);
//                    assertEquals(shouldBeThatColor +1, Trick.baseColor(i).type);
//                }
//            }
//       }
//        
//        @Test
//        void pointsWorks(){
//            int Card0,Card1,Card2,Card3, trump,points0,points1,points2,points3,pointBonus;
//            for ( int i = 0 ; i != -1 ; ++i){
//                if (PackedTrick.isValid(i)){
//                     Card0 = Bits32.extract(i, 0, 6);
//                     Card1 = Bits32.extract(i, 6, 6);
//                     Card2 = Bits32.extract(i, 12, 6);
//                     Card3 = Bits32.extract(i, 18, 6);
//                     trump = Bits32.extract(i, 30, 2);
//                     pointBonus = 0;
//                     points0 = 0;
//                     points1 = 0;
//                     points2=0;
//                     points3 = 0;
//                     if (Card.isValid(Card0)) {
//                     points0 = Card.points(Color.ALL.get(trump), Card0);
//                     }
//                     if(Card.isValid(Card1)) {
//                     points1 = Card.points(Color.ALL.get(trump), Card1);
//                     }
//                     if(Card.isValid(Card2)) {
//                     points2 = Card.points(Color.ALL.get(trump), Card2);
//                     }
//                     if(Card.isValid(Card3)) {
//                     points3 = Card.points(Color.ALL.get(trump), Card3);      
//                     }
//                    if (Bits32.extract(i, 24, 4) == 8) {
//                        pointBonus += 5;
//                    }
//                    // les cartes entrée dans points sont toutes valides
//                    // si on entre des mauvaises cartes , assertError
//                    if(Card.isValid(Card0)&&Card.isValid(Card1)&&Card.isValid(Card2)&&Card.isValid(Card3)) {
//                    int sum = points0 + points1+ points2 + points3 + pointBonus;
//                    assertEquals(sum , Trick.points(i));
//                    }
//                }
//            }
//       }
//    
//        
//}
