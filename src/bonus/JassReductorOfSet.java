package bonus;

import ch.epfl.javass.jass.Card;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.javass.jass.CardSet;
import ch.epfl.javass.jass.PackedCard;
import ch.epfl.javass.jass.PackedCardSet;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.jass.Trick;

public class JassReductorOfSet {
public Trick trick;

    public static List<CardSet> CardsThePlayerHavnt(Trick trick) {
        assert(trick.isFull());
        Color baseColor = trick.baseColor();
        Color trumpColor = trick.trump();
        List<CardSet> cardsThePlayerDontHave  = new ArrayList<>(Collections.nCopies(4, CardSet.EMPTY));
        List<CardSet> cardsThePlayerHavnt = new ArrayList<>(Collections.nCopies(4, CardSet.EMPTY));
        List<Card> cardPlayed = new ArrayList<>(); 
        for(int i =0 ; i< trick.size() ; ++i ) {
            cardPlayed.add(i, trick.card(i));    
        }
        if(!baseColor.equals(trumpColor)) {
            boolean nobodyUseTrump = true;
            for(int i = 1 ; (i<4)&&nobodyUseTrump; ++i) {         
                if(!(cardPlayed.get(i).color().equals(trumpColor)||cardPlayed.get(i).color().equals(baseColor))) {
                    cardsThePlayerDontHave.set(i, CardSet.ofPacked(PackedCardSet.subsetOfColor(PackedCardSet.ALL_CARDS, baseColor)));
                }
                else {
                    nobodyUseTrump = false;
                }
            }
           
        }
        
        for(int i = 0 ; i <= 2 ; ++i) {
            if(cardPlayed.get(i).color().equals(trumpColor)) {
                for(int j =i ; j<=3 ; ++j) {
                    if(cardPlayed.get(j).color().equals(trumpColor)) {
                        if(cardPlayed.get(i).isBetter(trumpColor,  cardPlayed.get(j))) {
                            cardsThePlayerDontHave.set(j, cardsThePlayerDontHave.get(j).union(CardSet.ofPacked(PackedCardSet.trumpAbove(cardPlayed.get(i).packed()))));
                            
                        }
                    }
                }
            }
        }
        int shift = trick.player(0).ordinal();
        for(PlayerId p : PlayerId.ALL) {
            cardsThePlayerHavnt.set(p.ordinal(), CardSet.ofPacked(PackedCardSet.difference(cardsThePlayerDontHave.get((-shift+p.ordinal()+4 )%4).packed(), PackedCardSet.add(PackedCardSet.EMPTY, PackedCard.pack(trumpColor, Rank.JACK)))));
        }
        return cardsThePlayerHavnt;

    }
    
    
    public static List<CardSet> CardsThePlayerHavnt(Trick trick,List<CardSet> oldListOfCardSetNotPossessed) {
        if(trick.isEmpty()) {
            return oldListOfCardSetNotPossessed;
        }
        else {
            int index = trick.size();
        
            Color baseColor = trick.baseColor();
            Color trumpColor = trick.trump();
            List<CardSet> cardsThePlayerDontHave  = oldListOfCardSetNotPossessed;
            List<CardSet> cardsThePlayerHavnt = new ArrayList<>(Collections.nCopies(4, CardSet.EMPTY));
            List<Card> cardPlayed = new ArrayList<>(); 
            for(int i =0 ; i< index ; ++i ) {
                cardPlayed.add(i, trick.card(i));    
            }
            if(!baseColor.equals(trumpColor)) {
                boolean nobodyUseTrump = true;
                for(int i = 1 ; (i<index)&&nobodyUseTrump; ++i) {         
                    if(!(cardPlayed.get(i).color().equals(trumpColor)||cardPlayed.get(i).color().equals(baseColor))) {
                        cardsThePlayerDontHave.set(i, CardSet.ofPacked(PackedCardSet.subsetOfColor(PackedCardSet.ALL_CARDS, baseColor)).union(cardsThePlayerDontHave.get(i)));
                    }
                    else {
                        nobodyUseTrump = false;
                    }
                }
               
            }
            
            for(int i = 0 ; i < index-1 ; ++i) {
                if(cardPlayed.get(i).color().equals(trumpColor)) {
                    for(int j =i ; j< index ; ++j) {
                        if(cardPlayed.get(j).color().equals(trumpColor)) {
                            if(cardPlayed.get(i).isBetter(trumpColor,  cardPlayed.get(j))) {
                                cardsThePlayerDontHave.set(j, cardsThePlayerDontHave.get(j).union(CardSet.ofPacked(PackedCardSet.trumpAbove(cardPlayed.get(i).packed()))));
                                
                            }
                        }
                    }
                }
            }
            int shift = trick.player(0).ordinal();
            for(PlayerId p : PlayerId.ALL) {
                cardsThePlayerHavnt.set(p.ordinal(), CardSet.ofPacked(PackedCardSet.difference(cardsThePlayerDontHave.get((-shift+p.ordinal()+4 )%4).packed(), PackedCardSet.add(PackedCardSet.EMPTY, PackedCard.pack(trumpColor, Rank.JACK)))));
            }
            return cardsThePlayerHavnt;
        
        }
    }

}
