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

/**
 * JassReductorOfSet : contains a method to reduct the set of possible cards a player might have.
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public class JassReductorOfSet {


    /**
     * @Brief BONUS :  will deduce on some cases that some player can't have some specific set of cards according to the rules of jass : 
     * "Un joueur doit poser une carte de la même couleur que celle du joueur qui à la main(suivre). <b>S’il n’a pas de carte de la même couleur</b>, 
     *  il peut poser une carte quelconque. 
     *  Qu’il puisse ou non suivre, il peut également couper. Un joueur peut couper, c’est-à-dire poser une carte de la couleur atout alors que le joueur qui avait la main 
     *  était parti d’une autre couleur. <b> Néanmoins, si un joueur a déjà coupé, il n’est pas autorisé de sous-couper</b>,
     *  c’est-à-dire poser un atout de valeur inférieure à celui ayant été précédemment posé, <b>sauf s’il ne dispose plus d'autres cartes</b>.
     *  Un joueur qui possède le valet d’atout, <b>n’est pas obligé de le jouer</b>, même si la couleur de base du pli est celle d’atout
     *  et que c’est la seule carte de cette couleur qui reste dans sa main. " d'après wikipedia
     *  With in bold some set of cards we can deduce a player doesn't have thank's to the rules.
     *
     * @param trick
     * @param oldListOfCardSetNotPossessed
     * @return an updated list sorted by playerIds of each card a specific player can't have knowing the trick and the previous list
    */
    public static List<CardSet> CardsThePlayerHavnt(Trick trick,List<CardSet> oldListOfCardSetNotPossessed) {
        if(trick.isEmpty()) {
            return oldListOfCardSetNotPossessed;
        }
        else {
            int index = trick.size();
        
            Color baseColor = trick.baseColor();
            Color trumpColor = trick.trump();
            List<CardSet> cardsPlayerDontHave = new ArrayList<>(Collections.nCopies(4, CardSet.EMPTY));
            List<Card> cardPlayed = new ArrayList<>(); 
            for(int i =0 ; i< index ; ++i ) {
                cardPlayed.add(i, trick.card(i));    
            }
            if(!baseColor.equals(trumpColor)) {
                for(int i = 1 ; (i<index); ++i) {         
                    if(!(cardPlayed.get(i).color().equals(trumpColor)||cardPlayed.get(i).color().equals(baseColor))) {
                        cardsPlayerDontHave.set(i, CardSet.ofPacked(PackedCardSet.subsetOfColor(PackedCardSet.ALL_CARDS, baseColor)));
                    }
                }             
            }
            Card bestCard = null ;
            for(int i = 0 ; i < index-1 ; ++i) {
                if(bestCard == null || cardPlayed.get(i).isBetter(trumpColor, bestCard)) {
                    bestCard = cardPlayed.get(i);
                    if(cardPlayed.get(i).color().equals(trumpColor)) {
                        for(int j =i+1 ; j< index ; ++j) {
                            if(cardPlayed.get(j).color().equals(trumpColor)) {
                                if(cardPlayed.get(i).isBetter(trumpColor,  cardPlayed.get(j))) {
                                    cardsPlayerDontHave.set(j, cardsPlayerDontHave.get(j).union(CardSet.ofPacked(PackedCardSet.trumpAbove(cardPlayed.get(i).packed()))));
                                    
                                }
                            }
                        }
                    }
                }
            }
            int shift = trick.player(0).ordinal();
            List<CardSet> updatedListOfCardSetNotPossessed = new ArrayList<>(Collections.nCopies(4, CardSet.EMPTY));
            for(PlayerId p : PlayerId.ALL) {
                updatedListOfCardSetNotPossessed.set(p.ordinal(), oldListOfCardSetNotPossessed.get(p.ordinal()).union
                        (CardSet.ofPacked(PackedCardSet.difference
                                (cardsPlayerDontHave.get((-shift+p.ordinal()+4 )%4).packed(),
                                        PackedCardSet.add(PackedCardSet.EMPTY, PackedCard.pack(trumpColor, Rank.JACK))))));
            }
            return updatedListOfCardSetNotPossessed;
        
        }
    }
    
    /**
     * @Brief JUST A SMALL VARIATION OF THE UPPER METHOD : NEVER USED.
     *
     * @param trick
     * @return
    */
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

}
