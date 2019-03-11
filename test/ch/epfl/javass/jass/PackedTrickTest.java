package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.Random;
import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static ch.epfl.javass.jass.PackedTrick.isValid;
import static ch.epfl.javass.jass.PackedScore.pack;
import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ch.epfl.javass.bits.Bits32;
import ch.epfl.javass.bits.Bits64;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;
import ch.epfl.javass.jass.TeamId;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class PackedTrickTest {

    private static boolean worksForAllValidTrick = false,
            isValidWorksForAllValidTrick = true,
            firstEmptyWorksForAllValidTrick = true,
            nextEmptyWorksForAllValidTrick = true,
            isLastWorksForAllValidTrick = true,
            isEmptyWorksForAllValidTrick = true,
            isFullWorksForAllValidTrick = true,
            sizeWorksForAllValidTrick = true,
            trumpWorksForAllValidTrick = true,
            playerWorksForAllValidTrick = true,
            indexWorksForAllValidTrick = true,
            cardWorksForAllValidTrick = true,
            withAddedCardWorksForAllValidTrick = true,
            baseColorForAllValidTrick = true,
            pointsForAllValidTrick = true,
            winningPlayerForAllValidTrick = true;

    Set<Integer> pkCardSet = new HashSet<Integer>() {
        private static final long serialVersionUID = 1L;
        {
            add(0b00_0000); add(0b01_0000); add(0b10_0000); add(0b11_0000); 
            add(0b00_0001); add(0b01_0001); add(0b10_0001); add(0b11_0001); 
            add(0b00_0010); add(0b01_0010); add(0b10_0010); add(0b11_0010); 
            add(0b00_0011); add(0b01_0011); add(0b10_0011); add(0b11_0011); 
            add(0b00_0100); add(0b01_0100); add(0b10_0100); add(0b11_0100); 
            add(0b00_0101); add(0b01_0101); add(0b10_0101); add(0b11_0101); 
            add(0b00_0110); add(0b01_0110); add(0b10_0110); add(0b11_0110); 
            add(0b00_0111); add(0b01_0111); add(0b10_0111); add(0b11_0111); 
            add(0b00_1000); add(0b01_1000); add(0b10_1000); add(0b11_1000); 
        }
    };

    Set<Integer> zt8Set = new HashSet<Integer>() {
        private static final long serialVersionUID = 1L;
        { add(0); add(1); add(2); add(3);add(4); add(5); add(6); add(7); add(8); }
    };

    void worksForAllValidTrick() {
        /*
        worksForAllValidTrick = true;
        return;
        /*/
        worksForAllValidTrick = true;
        for(Color trump : Color.values()) {
            for(PlayerId firstPlayer : PlayerId.values()) {
                for(int index : zt8Set) {
                    for(int pkCard0 : pkCardSet) {
                        if(isValid(pkCard0, PackedCard.INVALID, PackedCard.INVALID, PackedCard.INVALID)) {
                            for(int pkCard1 : pkCardSet) {
                                if(isValid(pkCard0, pkCard1, PackedCard.INVALID, PackedCard.INVALID)) {
                                    for(int pkCard2 : pkCardSet) {
                                        if(isValid(pkCard0, pkCard1, pkCard2, PackedCard.INVALID)) {
                                            for(int pkCard3 : pkCardSet) {
                                                if(isValid(pkCard0, pkCard1, pkCard2, pkCard3)) {
                                                    int pkTrick = Bits32.pack(pkCard0, 6, pkCard1, 6, pkCard2, 6, pkCard3, 6, index, 4, firstPlayer.ordinal(), 2, trump.ordinal(), 2);
                                                    try { isValidWorksForAllValidTrick(pkTrick); } catch (Error e) {
                                                        isValidWorksForAllValidTrick = false;}
                                                    try { nextEmptyWorksForAllValidTrick(trump, firstPlayer, index, pkCard0, pkCard1, pkCard2, pkCard3);} catch (Error e) {
                                                        nextEmptyWorksForAllValidTrick = false;}
                                                    try { isLastWorksForAllValidTrick(index, pkTrick); } catch (Error e) {
                                                        isLastWorksForAllValidTrick = false;}
                                                    try { isEmptyWorksForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
                                                        isEmptyWorksForAllValidTrick = false;}
                                                    try { isFullWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                                        isFullWorksForAllValidTrick = false;}
                                                    try { sizeWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                                        sizeWorksForAllValidTrick = false;}
                                                    try { withAddedCardWorksForAllValidTrick(trump, firstPlayer, index, pkCard0, pkCard1, pkCard2, pkCard3); } catch (Error e) {
                                                        withAddedCardWorksForAllValidTrick = false;}
                                                    try { baseColorForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
                                                        baseColorForAllValidTrick = false;}
                                                    try { pointsForAllValidTrick(trump, index, pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                                        pointsForAllValidTrick = false;}
                                                    try { winningPlayerForAllValidTrick(trump, firstPlayer, pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                                        winningPlayerForAllValidTrick = false;}
                                                }
                                            }
                                            int pkCard3 = PackedCard.INVALID;
                                            int pkTrick = Bits32.pack(pkCard0, 6, pkCard1, 6, pkCard2, 6, pkCard3, 6, index, 4, firstPlayer.ordinal(), 2, trump.ordinal(), 2);
                                            try { isValidWorksForAllValidTrick(pkTrick); } catch (Error e) {
                                                isValidWorksForAllValidTrick = false;}
                                            try { isEmptyWorksForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
                                                isEmptyWorksForAllValidTrick = false;}
                                            try { isFullWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                                isFullWorksForAllValidTrick = false;}
                                            try { sizeWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                                sizeWorksForAllValidTrick = false;}
                                            try { baseColorForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
                                                baseColorForAllValidTrick = false;}
                                            try { winningPlayerForAllValidTrick(trump, firstPlayer, pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                                winningPlayerForAllValidTrick = false;}
                                        }
                                    }
                                    int pkCard2 = PackedCard.INVALID,
                                            pkCard3 = PackedCard.INVALID;
                                    int pkTrick = Bits32.pack(pkCard0, 6, pkCard1, 6, pkCard2, 6, pkCard3, 6, index, 4, firstPlayer.ordinal(), 2, trump.ordinal(), 2);
                                    try { isValidWorksForAllValidTrick(pkTrick); } catch (Error e) {
                                        isValidWorksForAllValidTrick = false;}
                                    try { isEmptyWorksForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
                                        isEmptyWorksForAllValidTrick = false;}
                                    try { isFullWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                        isFullWorksForAllValidTrick = false;}
                                    try { sizeWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                        sizeWorksForAllValidTrick = false;}
                                    try { baseColorForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
                                        baseColorForAllValidTrick = false;}
                                    try { winningPlayerForAllValidTrick(trump, firstPlayer, pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                        winningPlayerForAllValidTrick = false;}
                                }
                            }
                            int pkCard1 = PackedCard.INVALID,
                                    pkCard2 = PackedCard.INVALID,
                                    pkCard3 = PackedCard.INVALID;
                            int pkTrick = Bits32.pack(pkCard0, 6, pkCard1, 6, pkCard2, 6, pkCard3, 6, index, 4, firstPlayer.ordinal(), 2, trump.ordinal(), 2);
                            try { isValidWorksForAllValidTrick(pkTrick); } catch (Error e) {
                                isValidWorksForAllValidTrick = false;}
                            try { isEmptyWorksForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
                                isEmptyWorksForAllValidTrick = false;}
                            try { isFullWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                isFullWorksForAllValidTrick = false;}
                            try { sizeWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                sizeWorksForAllValidTrick = false;}
                            try { trumpWorksForAllValidTrick(trump, pkTrick); } catch (Error e) {
                                trumpWorksForAllValidTrick = false;}
                            try { baseColorForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
                                baseColorForAllValidTrick = false;}
                            try { winningPlayerForAllValidTrick(trump, firstPlayer, pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
                                winningPlayerForAllValidTrick = false;}
                        }
                    }
                    int pkCard0 = PackedCard.INVALID,
                            pkCard1 = PackedCard.INVALID,
                            pkCard2 = PackedCard.INVALID,
                            pkCard3 = PackedCard.INVALID;
                    int pkTrick = Bits32.pack(pkCard0, 6, pkCard1, 6, pkCard2, 6, pkCard3, 6, index, 4, firstPlayer.ordinal(), 2, trump.ordinal(), 2);
//                    try { isValidWorksForAllValidTrick(pkTrick); } catch (Error e) {
//                        isValidWorksForAllValidTrick = false;}
//                    try { isEmptyWorksForAllValidTrick(pkCard0, pkTrick); } catch (Error e) {
//                        isEmptyWorksForAllValidTrick = false;}
//                    try { isFullWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
//                        isFullWorksForAllValidTrick = false;}
//                    try { sizeWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
//                        sizeWorksForAllValidTrick = false;}
//                    try { trumpWorksForAllValidTrick(trump, pkTrick); } catch (Error e) {
//                        trumpWorksForAllValidTrick = false;}
//                    try { playerWorksForAllValidTrick(firstPlayer, pkTrick); } catch (Error e) {
//                        playerWorksForAllValidTrick = false;}
//                    try { cardWorksForAllValidTrick(pkCard0, pkCard1, pkCard2, pkCard3, pkTrick); } catch (Error e) {
//                        cardWorksForAllValidTrick = false;}
                }
                try { firstEmptyWorksForAllValidTrick(trump, firstPlayer); } catch (Error e) {
                    firstEmptyWorksForAllValidTrick = false;}
            }
        }
        //*/
    }


    @Test
    void invalidIsCorrect() {
        assertEquals(0b11111111_11111111_11111111_11111111, PackedTrick.INVALID);
    }

    @Test
    void isValidWorksForInvalidTrick() {
        assertFalse(PackedScore.isValid(PackedTrick.INVALID));
    }

    boolean isValid(int pkCard0, int pkCard1, int pkCard2, int pkCard3) {
        return (pkCard1 == PackedCard.INVALID) || (pkCard0 != pkCard1 && (
                (pkCard2 == PackedCard.INVALID) || (pkCard0 != pkCard2 && pkCard1 != pkCard2 && (
                        (pkCard3 == PackedCard.INVALID) || (pkCard0 != pkCard3 &&  pkCard1 != pkCard3 && pkCard2 != pkCard3)))));
    }

    void isValidWorksForAllValidTrick(int pkTrick) {
        isValidWorksForAllValidTrick = isValidWorksForAllValidTrick && 
                PackedTrick.isValid(pkTrick);
    }

    @Test
    void isValidWorksForAllValidTrick1() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(isValidWorksForAllValidTrick);
    }

    void firstEmptyWorksForAllValidTrick(Color trump, PlayerId firstPlayer) {
        firstEmptyWorksForAllValidTrick = firstEmptyWorksForAllValidTrick &&
                Bits32.pack(
                        PackedCard.INVALID, 6,
                        PackedCard.INVALID, 6,
                        PackedCard.INVALID, 6,
                        PackedCard.INVALID, 6,
                        0, 4,
                        firstPlayer.ordinal(), 2,
                        trump.ordinal(), 2) ==
                        PackedTrick.firstEmpty(trump, firstPlayer);
    }

    @Test
    void firstEmptyWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(firstEmptyWorksForAllValidTrick);
    }

    int winningPlayer(Color trump, PlayerId firstPlayer, int pkCard0, int pkCard1, int pkCard2, int pkCard3) {
        int i = 0;
        int j = pkCard0;
        if(pkCard1 != PackedCard.INVALID && PackedCard.isBetter(trump, pkCard1, j)) {
            i = 1;
            j = pkCard1;
        }
        if(pkCard2 != PackedCard.INVALID && PackedCard.isBetter(trump, pkCard2, j)) {
            i = 2;
            j = pkCard2;
        }
        if(pkCard3 != PackedCard.INVALID && PackedCard.isBetter(trump, pkCard3, j)) {
            i = 3;
            j = pkCard3;
        }
        return (firstPlayer.ordinal() + i) % 4;
    }

    void nextEmptyWorksForAllValidTrick(Color trump, PlayerId firstPlayer, int index, int pkCard0, int pkCard1, int pkCard2, int pkCard3) {
        if(index < 8) {
            int pkTrick = PackedTrick.nextEmpty(Bits32.pack(
                    pkCard0, 6,
                    pkCard1, 6,
                    pkCard2, 6,
                    pkCard3, 6,
                    index, 4,
                    firstPlayer.ordinal(), 2,
                    trump.ordinal(), 2));
            nextEmptyWorksForAllValidTrick = nextEmptyWorksForAllValidTrick && (
                    Bits32.pack(
                            PackedCard.INVALID, 6,
                            PackedCard.INVALID, 6,
                            PackedCard.INVALID, 6,
                            PackedCard.INVALID, 6,
                            index + 1, 4,
                            winningPlayer(trump, firstPlayer, pkCard0, pkCard1, pkCard2, pkCard3), 2,
                            trump.ordinal(), 2) ==
                            pkTrick);
        }
    }

    @Test
    void nextEmptyWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(nextEmptyWorksForAllValidTrick);
    }

    void isLastWorksForAllValidTrick(int index, int pkTrick) {
        isLastWorksForAllValidTrick = isLastWorksForAllValidTrick &&
                (index == 8) == PackedTrick.isLast(pkTrick);
    }

    @Test
    void isLastWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(isLastWorksForAllValidTrick);
    }

    void isEmptyWorksForAllValidTrick(int pkCard0, int pkTrick) {
        isEmptyWorksForAllValidTrick = isEmptyWorksForAllValidTrick &&
                (pkCard0 == PackedCard.INVALID) == PackedTrick.isEmpty(pkTrick);
    }

    @Test
    void isEmptyWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(isEmptyWorksForAllValidTrick);
    }

    void isFullWorksForAllValidTrick(int pkCard0, int pkCard1, int pkCard2, int pkCard3, int pkTrick) {
        isFullWorksForAllValidTrick = isFullWorksForAllValidTrick &&
                (pkCard0 != PackedCard.INVALID && 
                pkCard1 != PackedCard.INVALID && 
                pkCard2 != PackedCard.INVALID && 
                pkCard3 != PackedCard.INVALID) == PackedTrick.isFull(pkTrick);
    }

    @Test
    void isFullWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(isFullWorksForAllValidTrick);
    }

    void sizeWorksForAllValidTrick(int pkCard0, int pkCard1, int pkCard2, int pkCard3, int pkTrick) {
        sizeWorksForAllValidTrick = sizeWorksForAllValidTrick &&
                ((pkCard0 == PackedCard.INVALID) ? 0 : 
                    (pkCard1 == PackedCard.INVALID) ? 1 :
                        (pkCard2 == PackedCard.INVALID) ? 2 :
                            (pkCard3 == PackedCard.INVALID) ? 3 : 4 ) ==
                            PackedTrick.size(pkTrick);
    }

    @Test
    void sizeWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(sizeWorksForAllValidTrick);
    }

    void trumpWorksForAllValidTrick(Color trump, int pkTrick) {
        trumpWorksForAllValidTrick = trumpWorksForAllValidTrick &&
                trump ==
                PackedTrick.trump(pkTrick);
    }

    @Test
    void trumpWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(playerWorksForAllValidTrick);
    }

    void playerWorksForAllValidTrick(PlayerId firstPlayer, int pkTrick) {
        for(int i = 0; i < 4; ++i)
            playerWorksForAllValidTrick = playerWorksForAllValidTrick &&
            (firstPlayer.ordinal() + i)%4 ==
            PackedTrick.player(pkTrick, i).ordinal();
    }

    @Test
    void playerWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(playerWorksForAllValidTrick);
    }

    void indexWorksForAllValidTrick(int index, int pkTrick) {
        indexWorksForAllValidTrick = indexWorksForAllValidTrick &&
                index ==
                PackedTrick.index(pkTrick);
    }

    @Test
    void indexWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(indexWorksForAllValidTrick);
    }

    void cardWorksForAllValidTrick(int pkCard0, int pkCard1, int pkCard2, int pkCard3, int pkTrick) {
        for(int i = 0; i < 4; ++i)
            cardWorksForAllValidTrick = cardWorksForAllValidTrick &&
            (i==0 ? pkCard0 :
                i==1 ? pkCard1 :
                    i==2 ? pkCard2 : pkCard3)
            ==
            PackedTrick.card(pkTrick,i);
    }

    @Test
    void cardWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(cardWorksForAllValidTrick);
    }

    void withAddedCardWorksForAllValidTrick(Color trump, PlayerId firstPlayer, int index, int pkCard0, int pkCard1, int pkCard2, int pkCard3) {
        int pkTrick = PackedTrick.withAddedCard(Bits32.pack(
                pkCard0, 6,
                pkCard1, 6,
                pkCard2, 6,
                PackedCard.INVALID, 6,
                index, 4,
                firstPlayer.ordinal(), 2,
                trump.ordinal(), 2), pkCard3);
        withAddedCardWorksForAllValidTrick = withAddedCardWorksForAllValidTrick && (
                Bits32.pack(
                        pkCard0, 6,
                        pkCard1, 6,
                        pkCard2, 6,
                        pkCard3, 6,
                        index, 4,
                        firstPlayer.ordinal(), 2,
                        trump.ordinal(), 2) ==
                        pkTrick);
        pkTrick = PackedTrick.withAddedCard(Bits32.pack(
                pkCard0, 6,
                pkCard1, 6,
                PackedCard.INVALID, 6,
                PackedCard.INVALID, 6,
                index, 4,
                firstPlayer.ordinal(), 2,
                trump.ordinal(), 2), pkCard2);
        withAddedCardWorksForAllValidTrick = withAddedCardWorksForAllValidTrick && (
                Bits32.pack(
                        pkCard0, 6,
                        pkCard1, 6,
                        pkCard2, 6,
                        PackedCard.INVALID, 6,
                        index, 4,
                        firstPlayer.ordinal(), 2,
                        trump.ordinal(), 2) ==
                        pkTrick);
        pkTrick = PackedTrick.withAddedCard(Bits32.pack(
                pkCard0, 6,
                PackedCard.INVALID, 6,
                PackedCard.INVALID, 6,
                PackedCard.INVALID, 6,
                index, 4,
                firstPlayer.ordinal(), 2,
                trump.ordinal(), 2), pkCard1);
        withAddedCardWorksForAllValidTrick = withAddedCardWorksForAllValidTrick && (
                Bits32.pack(
                        pkCard0, 6,
                        pkCard1, 6,
                        PackedCard.INVALID, 6,
                        PackedCard.INVALID, 6,
                        index, 4,
                        firstPlayer.ordinal(), 2,
                        trump.ordinal(), 2) ==
                        pkTrick);
        pkTrick = PackedTrick.withAddedCard(Bits32.pack(
                PackedCard.INVALID, 6,
                PackedCard.INVALID, 6,
                PackedCard.INVALID, 6,
                PackedCard.INVALID, 6,
                index, 4,
                firstPlayer.ordinal(), 2,
                trump.ordinal(), 2), pkCard0);
        withAddedCardWorksForAllValidTrick = withAddedCardWorksForAllValidTrick && (
                Bits32.pack(
                        pkCard0, 6,
                        PackedCard.INVALID, 6,
                        PackedCard.INVALID, 6,
                        PackedCard.INVALID, 6,
                        index, 4,
                        firstPlayer.ordinal(), 2,
                        trump.ordinal(), 2) ==
                        pkTrick);
    }

    @Test
    void withAddedCardWorksForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(withAddedCardWorksForAllValidTrick);
    }

    void baseColorForAllValidTrick(int pkCard0, int pkTrick) {
        baseColorForAllValidTrick = baseColorForAllValidTrick &&
                (pkCard0>>4) ==
                PackedTrick.baseColor(pkTrick).ordinal();
    }

    @Test
    void baseColorForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(baseColorForAllValidTrick);
    }

    void pointsForAllValidTrick(Color trump, int index, int pkCard0, int pkCard1, int pkCard2, int pkCard3, int pkTrick) {
        pointsForAllValidTrick = pointsForAllValidTrick &&
                (
                        ((pkCard0 != PackedCard.INVALID) ? PackedCard.points(trump, pkCard0) : 0 ) +
                        ((pkCard1 != PackedCard.INVALID) ? PackedCard.points(trump, pkCard1) : 0 ) +
                        ((pkCard2 != PackedCard.INVALID) ? PackedCard.points(trump, pkCard2) : 0 ) +
                        ((pkCard3 != PackedCard.INVALID) ? PackedCard.points(trump, pkCard3) : 0 ) +
                        ((index == 8) ? 5 : 0)
                        )==
                        PackedTrick.points(pkTrick);
    }

    @Test
    void pointsForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(pointsForAllValidTrick);
    }

    void winningPlayerForAllValidTrick(Color trump, PlayerId firstPlayer, int pkCard0, int pkCard1, int pkCard2, int pkCard3, int pkTrick) {
        winningPlayerForAllValidTrick = winningPlayerForAllValidTrick &&
                winningPlayer(trump, firstPlayer, pkCard0, pkCard1, pkCard2, pkCard3)
                ==
                PackedTrick.winningPlayer(pkTrick).ordinal();
    }

    @Test
    void winningPlayerForAllValidTrick() {
        if(!worksForAllValidTrick)
            worksForAllValidTrick();
        assertTrue(winningPlayerForAllValidTrick);
    }

    class P{
        public final int pkTrick;
        public final long pkHand;
        public P (int pkTrick, long pkHand) {
            this.pkTrick = pkTrick;
            this.pkHand = pkHand;
        }
    }

    Map<P, Long> playableCardsList = new HashMap<P, Long>() {
        private static final long serialVersionUID = 1L;
        {
            put(new P(0b01_00_0000_111111_111111_010001_000001,
                    0b0000000_000000000_0000000_000000000_0000000_000000001_0000000_000000001L),
                    0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_000000001L);
            put(new P(0b01_00_0000_111111_111111_010001_000001,
                    0b0000000_000000000_0000000_000000000_0000000_000000001_0000000_000000000L),
                    0b0000000_000000000_0000000_000000000_0000000_000000001_0000000_000000000L);
            put(new P(0b01_00_0000_111111_111111_010001_000001,
                    0b0000000_000000000_0000000_000000001_0000000_000000001_0000000_000000000L),
                    0b0000000_000000000_0000000_000000001_0000000_000000000_0000000_000000000L);
            put(new P(0b00_00_0000_111111_111111_111111_000001,
                    0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_000000001L),
                    0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_000000001L);
            put(new P(0b00_00_0000_111111_111111_111111_000001,
                    0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_100000001L),
                    0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_100000001L);
            
            put(new P(0b11_11_1000_111111_111111_111111_111111, 0b0000000_101000101_0000000_100000001_0000000_100000000_0000000_001000000L), 0b0000000_101000101_0000000_100000001_0000000_100000000_0000000_001000000L);
            put(new P(0b10_11_0101_111111_111111_111111_110110, 0b0000000_000010100_0000000_101010001_0000000_001000100_0000000_000000000L), 0b0000000_000010100_0000000_101010001_0000000_000000000_0000000_000000000L);
            put(new P(0b01_11_0001_111111_111111_010011_000010, 0b0000000_000000010_0000000_110010000_0000000_100101000_0000000_000000100L), 0b0000000_000000000_0000000_000000000_0000000_000100000_0000000_000000100L);
            put(new P(0b01_00_0101_111111_111111_111111_111111, 0b0000000_010000000_0000000_100000010_0000000_010100000_0000000_000010110L), 0b0000000_010000000_0000000_100000010_0000000_010100000_0000000_000010110L);
            put(new P(0b00_00_0001_111111_111111_111111_001000, 0b0000000_100100000_0000000_100000010_0000000_000000100_0000000_101000001L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_101000001L);
            put(new P(0b01_10_0100_111111_111111_110101_010110, 0b0000000_010000000_0000000_000000000_0000000_001001001_0000000_001110001L), 0b0000000_000000000_0000000_000000000_0000000_001001001_0000000_000000000L);
            put(new P(0b01_10_0111_111111_111111_111111_111111, 0b0000000_000000100_0000000_001000000_0000000_000100010_0000000_100100110L), 0b0000000_000000100_0000000_001000000_0000000_000100010_0000000_100100110L);
            put(new P(0b01_11_1000_111111_111111_111111_100000, 0b0000000_101000010_0000000_001010000_0000000_000000101_0000000_100000000L), 0b0000000_000000000_0000000_001010000_0000000_000000101_0000000_000000000L);
            put(new P(0b10_00_0011_111111_111111_000010_110100, 0b0000000_000101000_0000000_000010000_0000000_110100000_0000000_000100010L), 0b0000000_000101000_0000000_000010000_0000000_000000000_0000000_000000000L);
            put(new P(0b01_00_0111_111111_111111_111111_111111, 0b0000000_010000000_0000000_100001010_0000000_001000000_0000000_001000001L), 0b0000000_010000000_0000000_100001010_0000000_001000000_0000000_001000001L);
            put(new P(0b11_01_0001_111111_111111_111111_000001, 0b0000000_000000000_0000000_010000011_0000000_001000100_0000000_101000100L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_101000100L);
            put(new P(0b01_00_0001_111111_111111_010010_110000, 0b0000000_000000010_0000000_010100101_0000000_000010010_0000000_000000100L), 0b0000000_000000010_0000000_000000000_0000000_000010000_0000000_000000000L);
            put(new P(0b10_11_0000_111111_111111_111111_111111, 0b0000000_001100000_0000000_000001010_0000000_000100000_0000000_010011000L), 0b0000000_001100000_0000000_000001010_0000000_000100000_0000000_010011000L);
            put(new P(0b01_10_0110_111111_111111_111111_010001, 0b0000000_001001100_0000000_000100100_0000000_000000101_0000000_000000001L), 0b0000000_000000000_0000000_000000000_0000000_000000101_0000000_000000000L);
            put(new P(0b01_10_0101_111111_111111_110011_110101, 0b0000000_000001001_0000000_010010000_0000000_000000001_0000000_010000101L), 0b0000000_000001001_0000000_000000000_0000000_000000001_0000000_000000000L);
            put(new P(0b10_00_0111_111111_111111_111111_111111, 0b0000000_000000001_0000000_000011000_0000000_001011000_0000000_000001100L), 0b0000000_000000001_0000000_000011000_0000000_001011000_0000000_000001100L);
            put(new P(0b11_01_0001_111111_111111_111111_011000, 0b0000000_000001101_0000000_100000000_0000000_000000111_0000000_000000100L), 0b0000000_000001101_0000000_000000000_0000000_000000111_0000000_000000000L);
            put(new P(0b00_11_0100_111111_111111_011000_101000, 0b0000000_010001000_0000000_000100000_0000000_000000000_0000000_010100000L), 0b0000000_000000000_0000000_000100000_0000000_000000000_0000000_010100000L);
            put(new P(0b00_00_0010_111111_111111_111111_111111, 0b0000000_000010001_0000000_000000000_0000000_101000001_0000000_011000010L), 0b0000000_000010001_0000000_000000000_0000000_101000001_0000000_011000010L);
            put(new P(0b11_01_1000_111111_111111_111111_100000, 0b0000000_001001100_0000000_001010000_0000000_100000000_0000000_100000000L), 0b0000000_001001100_0000000_001010000_0000000_000000000_0000000_000000000L);
            put(new P(0b01_11_1000_111111_111111_000101_000111, 0b0000000_000000000_0000000_000100110_0000000_000000010_0000000_001001000L), 0b0000000_000000000_0000000_000000000_0000000_000000010_0000000_001001000L);
            put(new P(0b10_01_0001_111111_111111_111111_111111, 0b0000000_001001000_0000000_001000001_0000000_000011000_0000000_100000100L), 0b0000000_001001000_0000000_001000001_0000000_000011000_0000000_100000100L);
            put(new P(0b11_10_0111_111111_111111_111111_110000, 0b0000000_001000000_0000000_000101000_0000000_000010100_0000000_010000110L), 0b0000000_001000000_0000000_000000000_0000000_000000000_0000000_000000000L);
            put(new P(0b11_10_0001_111111_111111_111111_010011, 0b0000000_000001011_0000000_010100000_0000000_000001000_0000000_000000000L), 0b0000000_000001011_0000000_000000000_0000000_000001000_0000000_000000000L);
            put(new P(0b10_00_0111_111111_111111_111111_111111, 0b0000000_000100000_0000000_100011001_0000000_101000000_0000000_000100000L), 0b0000000_000100000_0000000_100011001_0000000_101000000_0000000_000100000L);
            put(new P(0b10_00_0100_111111_111111_111111_000010, 0b0000000_010000010_0000000_100010001_0000000_000100000_0000000_100000100L), 0b0000000_000000000_0000000_100010001_0000000_000000000_0000000_100000100L);
            put(new P(0b00_11_1000_111111_111111_111000_010000, 0b0000000_000000001_0000000_000000000_0000000_000010101_0000000_100100010L), 0b0000000_000000000_0000000_000000000_0000000_000010101_0000000_100100010L);
            put(new P(0b00_01_0100_111111_111111_111111_111111, 0b0000000_001110001_0000000_000100000_0000000_000000010_0000000_010000001L), 0b0000000_001110001_0000000_000100000_0000000_000000010_0000000_010000001L);
            put(new P(0b10_11_0110_111111_111111_111111_011000, 0b0000000_000000001_0000000_101000000_0000000_000100000_0000000_000011010L), 0b0000000_000000000_0000000_101000000_0000000_000100000_0000000_000000000L);
            put(new P(0b01_11_0101_111111_111111_010100_110010, 0b0000000_000110001_0000000_000000010_0000000_010010000_0000000_010000100L), 0b0000000_000110001_0000000_000000000_0000000_010000000_0000000_000000000L);
            put(new P(0b01_10_0110_111111_111111_111111_111111, 0b0000000_000000000_0000000_000010101_0000000_100001001_0000000_001100000L), 0b0000000_000000000_0000000_000010101_0000000_100001001_0000000_001100000L);
            put(new P(0b01_11_0100_111111_111111_111111_010001, 0b0000000_001000000_0000000_000000001_0000000_000101100_0000000_001100100L), 0b0000000_000000000_0000000_000000000_0000000_000101100_0000000_000000000L);
            put(new P(0b10_01_0010_111111_111111_100111_110100, 0b0000000_100001000_0000000_001000000_0000000_011000000_0000000_011000000L), 0b0000000_100001000_0000000_000000000_0000000_000000000_0000000_000000000L);
            put(new P(0b01_01_0101_111111_111111_111111_111111, 0b0000000_000000000_0000000_001101001_0000000_000010001_0000000_000001001L), 0b0000000_000000000_0000000_001101001_0000000_000010001_0000000_000001001L);
            put(new P(0b11_00_0110_111111_111111_111111_110011, 0b0000000_000010010_0000000_100001010_0000000_010000010_0000000_000001000L), 0b0000000_000010010_0000000_000000000_0000000_000000000_0000000_000000000L);
            put(new P(0b01_11_0111_111111_111111_010010_101000, 0b0000000_001010001_0000000_001000100_0000000_001000010_0000000_000000000L), 0b0000000_000000000_0000000_001000100_0000000_001000000_0000000_000000000L);
            put(new P(0b10_11_0011_111111_111111_111111_111111, 0b0000000_000100001_0000000_000000000_0000000_000001011_0000000_100010010L), 0b0000000_000100001_0000000_000000000_0000000_000001011_0000000_100010010L);
            put(new P(0b11_00_0110_111111_111111_111111_110010, 0b0000000_010000001_0000000_000001001_0000000_000000011_0000000_010000000L), 0b0000000_010000001_0000000_000000000_0000000_000000000_0000000_000000000L);
            put(new P(0b11_01_0010_111111_111111_000110_110011, 0b0000000_000000000_0000000_000001100_0000000_000011001_0000000_010000101L), 0b0000000_000000000_0000000_000001100_0000000_000011001_0000000_010000101L);
            put(new P(0b01_01_0111_111111_111111_111111_111111, 0b0000000_000010001_0000000_000001010_0000000_000100100_0000000_100100000L), 0b0000000_000010001_0000000_000001010_0000000_000100100_0000000_100100000L);
            put(new P(0b10_01_0101_111111_111111_111111_000010, 0b0000000_000000100_0000000_101100000_0000000_011000100_0000000_000001000L), 0b0000000_000000000_0000000_101100000_0000000_000000000_0000000_000001000L);
            put(new P(0b10_00_0011_111111_111111_000101_100110, 0b0000000_001011010_0000000_100000100_0000000_010000000_0000000_000000000L), 0b0000000_000000000_0000000_100000100_0000000_000000000_0000000_000000000L);
            put(new P(0b10_01_0101_111111_111111_111111_111111, 0b0000000_010000101_0000000_000010100_0000000_000001000_0000000_000010001L), 0b0000000_010000101_0000000_000010100_0000000_000001000_0000000_000010001L);
            put(new P(0b11_01_0100_111111_111111_111111_010111, 0b0000000_000000000_0000000_001011000_0000000_100101010_0000000_010000000L), 0b0000000_000000000_0000000_000000000_0000000_100101010_0000000_000000000L);
            put(new P(0b10_01_0010_111111_111111_110011_100110, 0b0000000_000010011_0000000_100000000_0000000_010001100_0000000_001000000L), 0b0000000_000000000_0000000_100000000_0000000_000000000_0000000_000000000L);
            put(new P(0b00_00_0100_111111_111111_111111_111111, 0b0000000_000000100_0000000_010100001_0000000_100001100_0000000_001000000L), 0b0000000_000000100_0000000_010100001_0000000_100001100_0000000_001000000L);
            put(new P(0b01_00_1000_111111_111111_111111_110101, 0b0000000_000010011_0000000_001000001_0000000_100100000_0000000_010000000L), 0b0000000_000010011_0000000_000000000_0000000_100100000_0000000_000000000L);
            put(new P(0b01_01_1000_111111_111111_000001_110100, 0b0000000_101000101_0000000_000010010_0000000_100000000_0000000_000100000L), 0b0000000_101000101_0000000_000000000_0000000_100000000_0000000_000000000L);
            put(new P(0b00_10_0101_111111_111111_111111_111111, 0b0000000_001000010_0000000_001010000_0000000_010100000_0000000_000010010L), 0b0000000_001000010_0000000_001010000_0000000_010100000_0000000_000010010L);
            put(new P(0b00_11_0110_111111_111111_111111_010101, 0b0000000_001100000_0000000_101001000_0000000_000000011_0000000_000000000L), 0b0000000_000000000_0000000_000000000_0000000_000000011_0000000_000000000L);
            put(new P(0b10_11_1000_111111_111111_100110_100100, 0b0000000_100010101_0000000_001000001_0000000_001010000_0000000_000000000L), 0b0000000_000000000_0000000_001000001_0000000_000000000_0000000_000000000L);
            put(new P(0b11_00_0110_111111_111111_111111_111111, 0b0000000_000100110_0000000_000001000_0000000_110000000_0000000_100000001L), 0b0000000_000100110_0000000_000001000_0000000_110000000_0000000_100000001L);
            put(new P(0b00_00_0100_111111_111111_111111_000101, 0b0000000_000100000_0000000_100100000_0000000_000100100_0000000_101100000L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_101100000L);
            put(new P(0b10_00_0001_111111_111111_110111_110010, 0b0000000_100001100_0000000_000010000_0000000_100100000_0000000_100000000L), 0b0000000_100001100_0000000_000010000_0000000_000000000_0000000_000000000L);
            put(new P(0b10_11_0101_111111_111111_111111_111111, 0b0000000_000001000_0000000_010010000_0000000_000000100_0000000_100000010L), 0b0000000_000001000_0000000_010010000_0000000_000000100_0000000_100000010L);
            put(new P(0b10_00_0011_111111_111111_111111_100001, 0b0000000_000100010_0000000_110000000_0000000_010010000_0000000_000011000L), 0b0000000_000000000_0000000_110000000_0000000_000000000_0000000_000000000L);
            put(new P(0b01_00_0000_111111_111111_111000_000010, 0b0000000_100100000_0000000_000001100_0000000_001000000_0000000_010010010L), 0b0000000_000000000_0000000_000000000_0000000_001000000_0000000_010010010L);
            put(new P(0b01_01_0001_111111_111111_111111_111111, 0b0000000_000001001_0000000_000001100_0000000_000000000_0000000_110101000L), 0b0000000_000001001_0000000_000001100_0000000_000000000_0000000_110101000L);
            put(new P(0b11_11_1000_111111_111111_111111_010001, 0b0000000_000000000_0000000_000100000_0000000_011101010_0000000_100010000L), 0b0000000_000000000_0000000_000000000_0000000_011101010_0000000_000000000L);
            put(new P(0b11_01_0101_111111_111111_110100_000111, 0b0000000_000100000_0000000_011001000_0000000_011000100_0000000_001000000L), 0b0000000_000100000_0000000_000000000_0000000_000000000_0000000_001000000L);
            put(new P(0b10_10_0110_111111_111111_111111_111111, 0b0000000_000010100_0000000_000000100_0000000_000011100_0000000_000100000L), 0b0000000_000010100_0000000_000000100_0000000_000011100_0000000_000100000L);
            put(new P(0b00_11_0001_111111_111111_111111_110010, 0b0000000_000001100_0000000_000010100_0000000_100010000_0000000_000010010L), 0b0000000_000001100_0000000_000000000_0000000_000000000_0000000_000010010L);
            put(new P(0b01_00_0101_111111_111111_000000_010010, 0b0000000_100000100_0000000_000000000_0000000_000000000_0000000_110000010L), 0b0000000_100000100_0000000_000000000_0000000_000000000_0000000_110000010L);
            put(new P(0b11_01_0110_111111_111111_111111_111111, 0b0000000_010001000_0000000_000000000_0000000_000110001_0000000_001100001L), 0b0000000_010001000_0000000_000000000_0000000_000110001_0000000_001100001L);
            put(new P(0b00_10_0010_111111_111111_111111_010011, 0b0000000_000110000_0000000_001001000_0000000_100001011_0000000_000000000L), 0b0000000_000000000_0000000_000000000_0000000_100001011_0000000_000000000L);
            put(new P(0b00_11_0010_111111_111111_010011_010101, 0b0000000_010000000_0000000_000101000_0000000_010101001_0000000_001000000L), 0b0000000_000000000_0000000_000000000_0000000_010101001_0000000_001000000L);
            put(new P(0b10_10_0101_111111_111111_111111_111111, 0b0000000_000010000_0000000_010000100_0000000_100000000_0000000_010101000L), 0b0000000_000010000_0000000_010000100_0000000_100000000_0000000_010101000L);
            put(new P(0b10_11_0111_111111_111111_111111_111000, 0b0000000_000001011_0000000_000000001_0000000_100000100_0000000_101000000L), 0b0000000_000001011_0000000_000000001_0000000_000000000_0000000_000000000L);
            put(new P(0b01_11_0110_111111_111111_000001_110111, 0b0000000_000000000_0000000_010000011_0000000_000100010_0000000_001000011L), 0b0000000_000000000_0000000_010000011_0000000_000100010_0000000_001000011L);
            put(new P(0b01_11_0010_111111_111111_111111_111111, 0b0000000_010011000_0000000_000110000_0000000_000010000_0000000_000010000L), 0b0000000_010011000_0000000_000110000_0000000_000010000_0000000_000010000L);
            put(new P(0b01_11_0011_111111_111111_111111_101000, 0b0000000_100100000_0000000_000000110_0000000_001110000_0000000_000010000L), 0b0000000_000000000_0000000_000000110_0000000_001110000_0000000_000000000L);
            put(new P(0b00_01_1000_111111_111111_101000_000110, 0b0000000_111000111_0000000_010000000_0000000_000000000_0000000_000000100L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_000000100L);
            put(new P(0b10_01_0100_111111_111111_111111_111111, 0b0000000_000000000_0000000_010101000_0000000_000001100_0000000_101000000L), 0b0000000_000000000_0000000_010101000_0000000_000001100_0000000_101000000L);
            put(new P(0b10_01_0110_111111_111111_111111_000110, 0b0000000_101000101_0000000_000100000_0000000_100000001_0000000_001000000L), 0b0000000_000000000_0000000_000100000_0000000_000000000_0000000_001000000L);
            put(new P(0b00_10_0100_111111_111111_110011_000100, 0b0000000_000000100_0000000_110000000_0000000_100001000_0000000_110001000L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_110001000L);
            put(new P(0b00_00_0100_111111_111111_111111_111111, 0b0000000_100000000_0000000_011000000_0000000_000000011_0000000_100000010L), 0b0000000_100000000_0000000_011000000_0000000_000000011_0000000_100000010L);
            put(new P(0b10_11_0100_111111_111111_111111_010011, 0b0000000_001000010_0000000_010001001_0000000_000110000_0000000_000000001L), 0b0000000_000000000_0000000_010001001_0000000_000110000_0000000_000000000L);
            put(new P(0b10_01_0110_111111_111111_010110_110001, 0b0000000_100000010_0000000_000001001_0000000_000110000_0000000_000001010L), 0b0000000_100000010_0000000_000001001_0000000_000000000_0000000_000000000L);
            put(new P(0b00_01_0111_111111_111111_111111_111111, 0b0000000_111000100_0000000_000001001_0000000_000000000_0000000_101000000L), 0b0000000_111000100_0000000_000001001_0000000_000000000_0000000_101000000L);
            put(new P(0b11_01_0110_111111_111111_111111_100100, 0b0000000_100000010_0000000_100000100_0000000_000100110_0000000_010000000L), 0b0000000_100000010_0000000_100000100_0000000_000000000_0000000_000000000L);
            put(new P(0b00_11_0100_111111_111111_100100_000100, 0b0000000_000000000_0000000_010010000_0000000_101100010_0000000_000000101L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_000000101L);
            put(new P(0b00_01_0010_111111_111111_111111_111111, 0b0000000_011010010_0000000_000000001_0000000_100001000_0000000_100000000L), 0b0000000_011010010_0000000_000000001_0000000_100001000_0000000_100000000L);
            put(new P(0b11_00_1000_111111_111111_111111_000011, 0b0000000_000000000_0000000_000000001_0000000_100010100_0000000_001001110L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_001001110L);
            put(new P(0b11_01_0010_111111_111111_010000_101000, 0b0000000_000000001_0000000_011000000_0000000_001010100_0000000_000100000L), 0b0000000_000000001_0000000_011000000_0000000_000000000_0000000_000000000L);
            put(new P(0b11_01_0010_111111_111111_111111_111111, 0b0000000_110010000_0000000_011000000_0000000_000100010_0000000_000000010L), 0b0000000_110010000_0000000_011000000_0000000_000100010_0000000_000000010L);
            put(new P(0b10_01_0100_111111_111111_111111_111111, 0b0000000_001001000_0000000_000000100_0000000_001001000_0000000_000000001L), 0b0000000_001001000_0000000_000000100_0000000_001001000_0000000_000000001L);
            put(new P(0b00_01_0010_111111_111111_010001_010010, 0b0000000_000101000_0000000_001000000_0000000_011110000_0000000_010000000L), 0b0000000_000000000_0000000_000000000_0000000_011110000_0000000_010000000L);
            put(new P(0b10_01_0000_111111_111111_111111_111111, 0b0000000_000000101_0000000_001010100_0000000_010000000_0000000_011000000L), 0b0000000_000000101_0000000_001010100_0000000_010000000_0000000_011000000L);
            put(new P(0b01_01_0110_111111_111111_111111_100111, 0b0000000_000100100_0000000_000000000_0000000_110000010_0000000_101010000L), 0b0000000_000100100_0000000_000000000_0000000_110000010_0000000_101010000L);
            put(new P(0b11_01_0010_111111_111111_001000_010010, 0b0000000_000110010_0000000_101000000_0000000_000001000_0000000_100000001L), 0b0000000_000110010_0000000_000000000_0000000_000001000_0000000_000000000L);
            put(new P(0b00_11_0000_111111_111111_111111_111111, 0b0000000_000110000_0000000_000010000_0000000_001000000_0000000_000010101L), 0b0000000_000110000_0000000_000010000_0000000_001000000_0000000_000010101L);
            put(new P(0b10_10_0111_111111_111111_111111_100001, 0b0000000_010000000_0000000_010001000_0000000_010011000_0000000_100000100L), 0b0000000_000000000_0000000_010001000_0000000_000000000_0000000_000000000L);
            put(new P(0b10_10_0000_111111_111111_100101_110100, 0b0000000_001000000_0000000_000010000_0000000_000000000_0000000_100001010L), 0b0000000_001000000_0000000_000000000_0000000_000000000_0000000_000000000L);
            put(new P(0b01_10_0100_111111_111111_111111_111111, 0b0000000_000000010_0000000_011000010_0000000_101001000_0000000_001000000L), 0b0000000_000000010_0000000_011000010_0000000_101001000_0000000_001000000L);
            put(new P(0b01_10_1000_111111_111111_111111_000110, 0b0000000_010001000_0000000_100000101_0000000_001000010_0000000_000000010L), 0b0000000_000000000_0000000_000000000_0000000_001000010_0000000_000000010L);
            put(new P(0b11_11_0001_111111_111111_001000_100000, 0b0000000_010001000_0000000_001001000_0000000_101000000_0000000_100100000L), 0b0000000_010001000_0000000_001001000_0000000_000000000_0000000_000000000L);
            put(new P(0b01_11_0000_111111_111111_111111_111111, 0b0000000_000100100_0000000_111000001_0000000_000000100_0000000_000100000L), 0b0000000_000100100_0000000_111000001_0000000_000000100_0000000_000100000L);
            put(new P(0b01_01_0110_111111_111111_111111_110110, 0b0000000_000000000_0000000_101000111_0000000_000000000_0000000_000100001L), 0b0000000_000000000_0000000_101000111_0000000_000000000_0000000_000100001L);
            put(new P(0b11_01_0110_111111_111111_001000_010010, 0b0000000_001001000_0000000_101000000_0000000_011000100_0000000_000010000L), 0b0000000_001001000_0000000_000000000_0000000_011000100_0000000_000000000L);
            put(new P(0b01_00_0010_111111_111111_111111_111111, 0b0000000_110010100_0000000_000000010_0000000_000000001_0000000_000011000L), 0b0000000_110010100_0000000_000000010_0000000_000000001_0000000_000011000L);
            put(new P(0b01_00_1000_111111_111111_111111_110100, 0b0000000_001100001_0000000_000000000_0000000_001100000_0000000_000010101L), 0b0000000_001100001_0000000_000000000_0000000_001100000_0000000_000000000L);
            put(new P(0b10_11_0001_111111_111111_100011_000010, 0b0000000_010010000_0000000_011000000_0000000_000100000_0000000_101000100L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_101000100L);
            put(new P(0b00_10_0101_111111_111111_111111_111111, 0b0000000_000000100_0000000_001000001_0000000_001000110_0000000_010000001L), 0b0000000_000000100_0000000_001000001_0000000_001000110_0000000_010000001L);
            put(new P(0b10_10_0001_111111_111111_111111_010000, 0b0000000_000000000_0000000_000101000_0000000_000010011_0000000_010000001L), 0b0000000_000000000_0000000_000101000_0000000_000010011_0000000_000000000L);
            put(new P(0b11_11_0111_111111_111111_010001_011000, 0b0000000_000000010_0000000_000010101_0000000_000010000_0000000_110000001L), 0b0000000_000000010_0000000_000000000_0000000_000010000_0000000_000000000L);
            put(new P(0b10_00_0101_111111_111111_111111_111111, 0b0000000_010000100_0000000_001010000_0000000_110000000_0000000_000001000L), 0b0000000_010000100_0000000_001010000_0000000_110000000_0000000_000001000L);
            put(new P(0b11_10_1000_111111_111111_111111_100111, 0b0000000_000000000_0000000_001000000_0000000_100001010_0000000_101101000L), 0b0000000_000000000_0000000_001000000_0000000_000000000_0000000_000000000L);
            put(new P(0b00_10_0010_111111_111111_000011_100010, 0b0000000_110000001_0000000_000001000_0000000_001110000_0000000_000000100L), 0b0000000_000000000_0000000_000001000_0000000_000000000_0000000_000000000L);
            put(new P(0b10_00_0100_111111_111111_111111_111111, 0b0000000_000000101_0000000_010000001_0000000_010000100_0000000_000001000L), 0b0000000_000000101_0000000_010000001_0000000_010000100_0000000_000001000L);
            put(new P(0b10_11_0100_111111_111111_111111_000110, 0b0000000_111000000_0000000_000100000_0000000_000000000_0000000_001110001L), 0b0000000_000000000_0000000_000100000_0000000_000000000_0000000_001110001L);
            put(new P(0b00_10_0011_111111_111111_100001_010100, 0b0000000_000100000_0000000_000000011_0000000_001100001_0000000_100000010L), 0b0000000_000000000_0000000_000000000_0000000_001100001_0000000_100000010L);
            put(new P(0b11_00_0011_111111_111111_111111_111111, 0b0000000_101000000_0000000_000100000_0000000_010001000_0000000_000110001L), 0b0000000_101000000_0000000_000100000_0000000_010001000_0000000_000110001L);
            put(new P(0b01_00_1000_111111_111111_111111_010011, 0b0000000_001010000_0000000_001000000_0000000_010000000_0000000_000011001L), 0b0000000_000000000_0000000_000000000_0000000_010000000_0000000_000000000L);
            put(new P(0b00_11_0000_111111_111111_100101_000011, 0b0000000_011000000_0000000_100010000_0000000_000000010_0000000_100001001L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_100001001L);
            put(new P(0b01_01_0101_111111_111111_111111_111111, 0b0000000_010000000_0000000_100110000_0000000_000101000_0000000_000000110L), 0b0000000_010000000_0000000_100110000_0000000_000101000_0000000_000000110L);
            put(new P(0b11_01_0100_111111_111111_111111_000110, 0b0000000_100001010_0000000_000000010_0000000_000001100_0000000_000000000L), 0b0000000_100001010_0000000_000000010_0000000_000001100_0000000_000000000L);
            put(new P(0b01_01_0000_111111_111111_100011_111000, 0b0000000_000000100_0000000_000000010_0000000_110100110_0000000_000000010L), 0b0000000_000000100_0000000_000000000_0000000_110100110_0000000_000000000L);
            put(new P(0b10_01_0000_111111_111111_111111_111111, 0b0000000_000001010_0000000_001000000_0000000_010000001_0000000_010010001L), 0b0000000_000001010_0000000_001000000_0000000_010000001_0000000_010010001L);
            put(new P(0b11_00_0010_111111_111111_111111_100110, 0b0000000_001000001_0000000_110010000_0000000_001001000_0000000_000000001L), 0b0000000_001000001_0000000_110010000_0000000_000000000_0000000_000000000L);
            put(new P(0b00_01_0001_111111_111111_000001_010010, 0b0000000_000100000_0000000_000001000_0000000_110000100_0000000_001010000L), 0b0000000_000000000_0000000_000000000_0000000_110000100_0000000_001010000L);
            put(new P(0b11_00_0101_111111_111111_111111_111111, 0b0000000_001100000_0000000_001000000_0000000_000001101_0000000_000001001L), 0b0000000_001100000_0000000_001000000_0000000_000001101_0000000_000001001L);
            put(new P(0b10_01_0010_111111_111111_111111_010001, 0b0000000_100011001_0000000_000000100_0000000_010000010_0000000_000100000L), 0b0000000_000000000_0000000_000000100_0000000_010000010_0000000_000000000L);
            put(new P(0b00_00_0000_111111_111111_100110_010011, 0b0000000_001010000_0000000_000000001_0000000_000010000_0000000_111000010L), 0b0000000_000000000_0000000_000000000_0000000_000010000_0000000_111000010L);
            put(new P(0b00_01_0010_111111_111111_111111_111111, 0b0000000_000010010_0000000_100000100_0000000_001000000_0000000_001010010L), 0b0000000_000010010_0000000_100000100_0000000_001000000_0000000_001010010L);
            put(new P(0b01_00_0100_111111_111111_111111_010010, 0b0000000_000010100_0000000_011000000_0000000_010000000_0000000_010100010L), 0b0000000_000000000_0000000_000000000_0000000_010000000_0000000_000000000L);
            put(new P(0b00_11_1000_111111_111111_111111_101000, 0b0000000_100010000_0000000_000000000_0000000_001000001_0000000_110100000L), 0b0000000_100010000_0000000_000000000_0000000_001000001_0000000_110100000L);
            put(new P(0b11_11_0010_111111_111111_111111_111111, 0b0000000_000000110_0000000_001000000_0000000_010000101_0000000_000100100L), 0b0000000_000000110_0000000_001000000_0000000_010000101_0000000_000100100L);
            put(new P(0b11_00_1000_111111_111111_111111_110010, 0b0000000_000100000_0000000_100001100_0000000_001001001_0000000_000000000L), 0b0000000_000100000_0000000_100001100_0000000_001001001_0000000_000000000L);
            put(new P(0b01_10_0100_111111_111111_010110_101000, 0b0000000_101000010_0000000_000000110_0000000_010000010_0000000_000000100L), 0b0000000_000000000_0000000_000000110_0000000_010000000_0000000_000000000L);
            put(new P(0b01_11_0101_111111_111111_111111_111111, 0b0000000_001100111_0000000_000000000_0000000_000000000_0000000_000101100L), 0b0000000_001100111_0000000_000000000_0000000_000000000_0000000_000101100L);
            put(new P(0b01_01_0011_111111_111111_111111_010001, 0b0000000_001000000_0000000_100000000_0000000_100111011_0000000_000000000L), 0b0000000_000000000_0000000_000000000_0000000_100111011_0000000_000000000L);
            put(new P(0b11_01_0010_111111_111111_001000_010101, 0b0000000_100000001_0000000_000010100_0000000_000100010_0000000_000000101L), 0b0000000_100000001_0000000_000000000_0000000_000100010_0000000_000000000L);
            put(new P(0b00_10_0001_111111_111111_111111_111111, 0b0000000_000100000_0000000_101000001_0000000_000000110_0000000_000000101L), 0b0000000_000100000_0000000_101000001_0000000_000000110_0000000_000000101L);
            put(new P(0b01_10_0001_111111_111111_111111_100100, 0b0000000_000010000_0000000_000010100_0000000_100000000_0000000_000001101L), 0b0000000_000000000_0000000_000010100_0000000_100000000_0000000_000000000L);
            put(new P(0b01_01_0111_111111_111111_100010_100011, 0b0000000_010110010_0000000_000000000_0000000_001001000_0000000_000000000L), 0b0000000_010110010_0000000_000000000_0000000_001001000_0000000_000000000L);
            put(new P(0b10_10_0111_111111_111111_111111_111111, 0b0000000_000000001_0000000_000100000_0000000_000000010_0000000_000011111L), 0b0000000_000000001_0000000_000100000_0000000_000000010_0000000_000011111L);
            put(new P(0b00_11_0101_111111_111111_111111_010111, 0b0000000_001001101_0000000_101000000_0000000_000000000_0000000_100100000L), 0b0000000_001001101_0000000_101000000_0000000_000000000_0000000_100100000L);
            put(new P(0b00_00_0011_111111_111111_100100_001000, 0b0000000_000001100_0000000_010000000_0000000_010010000_0000000_011001000L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_011001000L);
            put(new P(0b10_01_0000_111111_111111_111111_111111, 0b0000000_001010010_0000000_010010000_0000000_000000000_0000000_000100001L), 0b0000000_001010010_0000000_010010000_0000000_000000000_0000000_000100001L);
            put(new P(0b10_10_0111_111111_111111_111111_010010, 0b0000000_100101100_0000000_000000000_0000000_000000000_0000000_000111100L), 0b0000000_100101100_0000000_000000000_0000000_000000000_0000000_000111100L);
            put(new P(0b01_11_0110_111111_111111_010110_000010, 0b0000000_010000001_0000000_100010000_0000000_010010000_0000000_010000010L), 0b0000000_000000000_0000000_000000000_0000000_010000000_0000000_010000010L);
            put(new P(0b00_01_0100_111111_111111_111111_111111, 0b0000000_000011100_0000000_000000000_0000000_101000100_0000000_100000100L), 0b0000000_000011100_0000000_000000000_0000000_101000100_0000000_100000100L);
            put(new P(0b00_00_0111_111111_111111_111111_010111, 0b0000000_000000000_0000000_010001001_0000000_000000101_0000000_001010100L), 0b0000000_000000000_0000000_000000000_0000000_000000101_0000000_001010100L);
            put(new P(0b00_10_0011_111111_111111_010101_100001, 0b0000000_000101011_0000000_100001000_0000000_100100000_0000000_000000000L), 0b0000000_000000000_0000000_100001000_0000000_000000000_0000000_000000000L);
            put(new P(0b11_01_0000_111111_111111_111111_111111, 0b0000000_011100000_0000000_101010010_0000000_000000000_0000000_000010000L), 0b0000000_011100000_0000000_101010010_0000000_000000000_0000000_000010000L);
            put(new P(0b01_00_1000_111111_111111_111111_111111, 0b0000000_001100010_0000000_001010000_0000000_000000100_0000000_000100100L), 0b0000000_001100010_0000000_001010000_0000000_000000100_0000000_000100100L);
            put(new P(0b10_01_1000_111111_111111_010110_010001, 0b0000000_000001000_0000000_010010001_0000000_000000000_0000000_001010000L), 0b0000000_000001000_0000000_010010001_0000000_000000000_0000000_001010000L);
            put(new P(0b10_11_0010_111111_111111_111111_111111, 0b0000000_000000001_0000000_001000010_0000000_100001001_0000000_001000100L), 0b0000000_000000001_0000000_001000010_0000000_100001001_0000000_001000100L);
            put(new P(0b10_01_0101_111111_111111_111111_000110, 0b0000000_001001000_0000000_000000001_0000000_000000110_0000000_000001011L), 0b0000000_000000000_0000000_000000001_0000000_000000000_0000000_000001011L);
            put(new P(0b01_11_0000_111111_111111_110110_101000, 0b0000000_000010010_0000000_001000001_0000000_000101100_0000000_000000100L), 0b0000000_000000000_0000000_001000001_0000000_000101100_0000000_000000000L);
            put(new P(0b11_10_0110_111111_111111_111111_111111, 0b0000000_000000000_0000000_001011000_0000000_001000110_0000000_000001001L), 0b0000000_000000000_0000000_001011000_0000000_001000110_0000000_000001001L);
            put(new P(0b01_00_0000_111111_111111_111111_110110, 0b0000000_010101001_0000000_000000000_0000000_000100000_0000000_000001001L), 0b0000000_010101001_0000000_000000000_0000000_000100000_0000000_000000000L);
            put(new P(0b01_00_0010_111111_111111_001000_110101, 0b0000000_101000001_0000000_001000000_0000000_000001001_0000000_000100000L), 0b0000000_101000001_0000000_000000000_0000000_000001001_0000000_000000000L);
            put(new P(0b11_01_0100_111111_111111_111111_111111, 0b0000000_000000001_0000000_000000000_0000000_010010101_0000000_100010100L), 0b0000000_000000001_0000000_000000000_0000000_010010101_0000000_100010100L);
            put(new P(0b01_00_0101_111111_111111_111111_000111, 0b0000000_100100001_0000000_000000000_0000000_010000000_0000000_011101000L), 0b0000000_000000000_0000000_000000000_0000000_010000000_0000000_011101000L);
            put(new P(0b11_10_0000_111111_111111_010011_010110, 0b0000000_010000000_0000000_001010010_0000000_110000100_0000000_000010000L), 0b0000000_010000000_0000000_000000000_0000000_110000100_0000000_000000000L);
            put(new P(0b10_11_0001_111111_111111_111111_111111, 0b0000000_110000000_0000000_000000000_0000000_000110010_0000000_100100010L), 0b0000000_110000000_0000000_000000000_0000000_000110010_0000000_100100010L);
            put(new P(0b01_11_0110_111111_111111_111111_111111, 0b0000000_010000010_0000000_011000000_0000000_000010001_0000000_100000010L), 0b0000000_010000010_0000000_011000000_0000000_000010001_0000000_100000010L);
            put(new P(0b11_00_0011_111111_111111_110000_111000, 0b0000000_000100100_0000000_000000000_0000000_001001000_0000000_001100011L), 0b0000000_000100100_0000000_000000000_0000000_000000000_0000000_000000000L);
            put(new P(0b00_10_0000_111111_111111_111111_111111, 0b0000000_001100001_0000000_000101000_0000000_101000000_0000000_001000000L), 0b0000000_001100001_0000000_000101000_0000000_101000000_0000000_001000000L);
            put(new P(0b01_01_1000_111111_111111_111111_010110, 0b0000000_001000010_0000000_000001010_0000000_000000010_0000000_000001000L), 0b0000000_000000000_0000000_000000000_0000000_000000010_0000000_000000000L);
            put(new P(0b11_01_0100_111111_111111_110010_000001, 0b0000000_010000000_0000000_100000000_0000000_000010101_0000000_000001011L), 0b0000000_010000000_0000000_000000000_0000000_000000000_0000000_000001011L);
            put(new P(0b01_01_0111_111111_111111_111111_111111, 0b0000000_100000000_0000000_010000000_0000000_010000011_0000000_100000011L), 0b0000000_100000000_0000000_010000000_0000000_010000011_0000000_100000011L);
            put(new P(0b00_01_0110_111111_111111_111111_100101, 0b0000000_001010000_0000000_000001000_0000000_011001000_0000000_000001000L), 0b0000000_000000000_0000000_000001000_0000000_000000000_0000000_000001000L);
            put(new P(0b01_10_0000_111111_111111_110101_100001, 0b0000000_000000010_0000000_000000110_0000000_010100000_0000000_000101000L), 0b0000000_000000000_0000000_000000110_0000000_010100000_0000000_000000000L);
            put(new P(0b01_11_1000_111111_111111_111111_111111, 0b0000000_000110010_0000000_000000000_0000000_000000010_0000000_011000100L), 0b0000000_000110010_0000000_000000000_0000000_000000010_0000000_011000100L);
            put(new P(0b01_11_0101_111111_111111_111111_110000, 0b0000000_010000010_0000000_000110000_0000000_010010000_0000000_000101000L), 0b0000000_010000010_0000000_000000000_0000000_010010000_0000000_000000000L);
            put(new P(0b10_00_0001_111111_111111_111000_010000, 0b0000000_011010000_0000000_001000000_0000000_001001001_0000000_000100000L), 0b0000000_000000000_0000000_001000000_0000000_001001001_0000000_000000000L);
            put(new P(0b00_01_0001_111111_111111_111111_111111, 0b0000000_000000000_0000000_000111000_0000000_011100000_0000000_100000010L), 0b0000000_000000000_0000000_000111000_0000000_011100000_0000000_100000010L);
            put(new P(0b01_01_0001_111111_111111_111111_100010, 0b0000000_001000101_0000000_101000000_0000000_101000000_0000000_000001000L), 0b0000000_000000000_0000000_101000000_0000000_101000000_0000000_000000000L);
            put(new P(0b11_01_0011_111111_111111_100100_110000, 0b0000000_000000000_0000000_000000000_0000000_101111011_0000000_000100000L), 0b0000000_000000000_0000000_000000000_0000000_101111011_0000000_000100000L);
            put(new P(0b01_11_0101_111111_111111_111111_111111, 0b0000000_000001000_0000000_010100000_0000000_001010011_0000000_000000000L), 0b0000000_000001000_0000000_010100000_0000000_001010011_0000000_000000000L);
            put(new P(0b10_10_0101_111111_111111_111111_111000, 0b0000000_010000000_0000000_000100011_0000000_010000100_0000000_001010000L), 0b0000000_010000000_0000000_000100011_0000000_000000000_0000000_000000000L);
            put(new P(0b01_11_0101_111111_111111_010010_110001, 0b0000000_100000000_0000000_000000000_0000000_100000001_0000000_100100110L), 0b0000000_100000000_0000000_000000000_0000000_100000000_0000000_000000000L);
            put(new P(0b11_11_0101_111111_111111_111111_111111, 0b0000000_110000001_0000000_000000101_0000000_000101001_0000000_000000000L), 0b0000000_110000001_0000000_000000101_0000000_000101001_0000000_000000000L);
            put(new P(0b10_11_1000_111111_111111_111111_100011, 0b0000000_010101000_0000000_000010001_0000000_000100000_0000000_000001000L), 0b0000000_000000000_0000000_000010001_0000000_000000000_0000000_000000000L);
            put(new P(0b00_11_0010_111111_111111_110010_000100, 0b0000000_100010000_0000000_101100000_0000000_000110001_0000000_000000000L), 0b0000000_100010000_0000000_101100000_0000000_000110001_0000000_000000000L);
            put(new P(0b01_11_1000_111111_111111_111111_111111, 0b0000000_000101000_0000000_010001000_0000000_000001010_0000000_010100000L), 0b0000000_000101000_0000000_010001000_0000000_000001010_0000000_010100000L);
            put(new P(0b01_10_0011_111111_111111_111111_100001, 0b0000000_010100000_0000000_100010000_0000000_001100000_0000000_001000100L), 0b0000000_000000000_0000000_100010000_0000000_001100000_0000000_000000000L);
            put(new P(0b01_00_0011_111111_111111_100011_000000, 0b0000000_000100010_0000000_010101001_0000000_001000000_0000000_000001000L), 0b0000000_000000000_0000000_000000000_0000000_001000000_0000000_000001000L);
            put(new P(0b00_01_0001_111111_111111_111111_111111, 0b0000000_100001100_0000000_111000000_0000000_000100000_0000000_000100000L), 0b0000000_100001100_0000000_111000000_0000000_000100000_0000000_000100000L);
            put(new P(0b10_01_1000_111111_111111_111111_010011, 0b0000000_000000000_0000000_100000110_0000000_110000000_0000000_000010010L), 0b0000000_000000000_0000000_100000110_0000000_110000000_0000000_000000000L);
            put(new P(0b01_00_0110_111111_111111_000011_110110, 0b0000000_001000100_0000000_001000010_0000000_000001000_0000000_101000010L), 0b0000000_001000100_0000000_000000000_0000000_000001000_0000000_000000000L);
            put(new P(0b01_00_0001_111111_111111_111111_111111, 0b0000000_100001000_0000000_000001010_0000000_010011000_0000000_000000100L), 0b0000000_100001000_0000000_000001010_0000000_010011000_0000000_000000100L);
            put(new P(0b00_00_0101_111111_111111_111111_010111, 0b0000000_010001000_0000000_000000000_0000000_001100000_0000000_100001101L), 0b0000000_000000000_0000000_000000000_0000000_001100000_0000000_100001101L);
            put(new P(0b01_01_0111_111111_111111_111111_000000, 0b0000000_000010000_0000000_010000000_0000000_101000001_0000000_010000010L), 0b0000000_000000000_0000000_000000000_0000000_101000001_0000000_010000010L);
            put(new P(0b11_10_0111_111111_111111_111111_111111, 0b0000000_000000100_0000000_100010001_0000000_000000010_0000000_010100100L), 0b0000000_000000100_0000000_100010001_0000000_000000010_0000000_010100100L);
            put(new P(0b10_11_0001_111111_111111_111111_110011, 0b0000000_001000100_0000000_000000010_0000000_010010010_0000000_001000000L), 0b0000000_001000100_0000000_000000010_0000000_000000000_0000000_000000000L);
            put(new P(0b00_01_0111_111111_111111_000100_000111, 0b0000000_110011000_0000000_100000000_0000000_110001000_0000000_000000000L), 0b0000000_110011000_0000000_100000000_0000000_110001000_0000000_000000000L);
            put(new P(0b01_01_0011_111111_111111_111111_111111, 0b0000000_001000000_0000000_000000100_0000000_000010010_0000000_011000100L), 0b0000000_001000000_0000000_000000100_0000000_000010010_0000000_011000100L);
            put(new P(0b11_11_0101_111111_111111_111111_110011, 0b0000000_110000000_0000000_001010000_0000000_000010001_0000000_000000000L), 0b0000000_110000000_0000000_000000000_0000000_000000000_0000000_000000000L);
            put(new P(0b01_00_0111_111111_111111_010110_000000, 0b0000000_100010001_0000000_100001100_0000000_000000000_0000000_000010100L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_000010100L);
            put(new P(0b00_11_0000_111111_111111_111111_111111, 0b0000000_100001000_0000000_001000000_0000000_001010000_0000000_100001010L), 0b0000000_100001000_0000000_001000000_0000000_001010000_0000000_100001010L);
            put(new P(0b11_01_0011_111111_111111_111111_010001, 0b0000000_000011000_0000000_001101001_0000000_000000010_0000000_000000001L), 0b0000000_000011000_0000000_000000000_0000000_000000010_0000000_000000000L);
            put(new P(0b01_01_0010_111111_111111_000100_100110, 0b0000000_110001000_0000000_100000000_0000000_100000010_0000000_011000000L), 0b0000000_000000000_0000000_100000000_0000000_100000010_0000000_000000000L);
            put(new P(0b10_10_0100_111111_111111_111111_111111, 0b0000000_010000011_0000000_000001100_0000000_000001000_0000000_000001010L), 0b0000000_010000011_0000000_000001100_0000000_000001000_0000000_000001010L);
            put(new P(0b01_01_1000_111111_111111_111111_110110, 0b0000000_000001100_0000000_000000010_0000000_000010011_0000000_010100000L), 0b0000000_000001100_0000000_000000000_0000000_000010011_0000000_000000000L);
            put(new P(0b00_00_0011_111111_111111_010010_000100, 0b0000000_110110000_0000000_000010000_0000000_000000000_0000000_000100001L), 0b0000000_000000000_0000000_000000000_0000000_000000000_0000000_000100001L);
            put(new P(0b01_00_0010_111111_111111_111111_111111, 0b0000000_100100000_0000000_001011000_0000000_000000100_0000000_000100000L), 0b0000000_100100000_0000000_001011000_0000000_000000100_0000000_000100000L);
            put(new P(0b11_10_0100_111111_111111_111111_000010, 0b0000000_000000010_0000000_000101000_0000000_000000100_0000000_000101101L), 0b0000000_000000010_0000000_000000000_0000000_000000000_0000000_000101101L);
            put(new P(0b11_00_0110_111111_111111_100101_110000, 0b0000000_100000000_0000000_100000001_0000000_000010000_0000000_101010010L), 0b0000000_100000000_0000000_000000000_0000000_000000000_0000000_000000000L);
            put(new P(0b10_10_0101_111111_111111_111111_111111, 0b0000000_000001000_0000000_001001000_0000000_100010001_0000000_010001000L), 0b0000000_000001000_0000000_001001000_0000000_100010001_0000000_010001000L);
            put(new P(0b11_01_0101_111111_111111_111111_010000, 0b0000000_010000000_0000000_000001100_0000000_001100001_0000000_000000100L), 0b0000000_010000000_0000000_000000000_0000000_001100001_0000000_000000000L);
            put(new P(0b01_00_0101_111111_111111_010101_100010, 0b0000000_000000101_0000000_011000010_0000000_110000010_0000000_000000000L), 0b0000000_000000000_0000000_011000010_0000000_000000000_0000000_000000000L);
            put(new P(0b10_11_0011_111111_111111_111111_111111, 0b0000000_001000000_0000000_010000011_0000000_001001000_0000000_100001000L), 0b0000000_001000000_0000000_010000011_0000000_001001000_0000000_100001000L);
            put(new P(0b10_00_0100_111111_111111_111111_100011, 0b0000000_000000100_0000000_101011010_0000000_000000010_0000000_100000000L), 0b0000000_000000000_0000000_101011010_0000000_000000000_0000000_000000000L);
            put(new P(0b01_00_0011_111111_111111_000001_101000, 0b0000000_000000100_0000000_010000100_0000000_001001000_0000000_001000101L), 0b0000000_000000000_0000000_010000100_0000000_001001000_0000000_000000000L);
            put(new P(0b11_11_0011_111111_111111_111111_111111, 0b0000000_000101100_0000000_010000110_0000000_000000100_0000000_000000100L), 0b0000000_000101100_0000000_010000110_0000000_000000100_0000000_000000100L);
            put(new P(0b10_01_0111_111111_111111_111111_000100, 0b0000000_011010000_0000000_000010000_0000000_000000000_0000000_110000100L), 0b0000000_000000000_0000000_000010000_0000000_000000000_0000000_110000100L);
            put(new P(0b01_10_1000_111111_111111_100010_100001, 0b0000000_000100000_0000000_100010001_0000000_100001000_0000000_100000010L), 0b0000000_000000000_0000000_100010001_0000000_100001000_0000000_000000000L);
            put(new P(0b01_01_0010_111111_111111_111111_111111, 0b0000000_000100000_0000000_100010000_0000000_000101000_0000000_001000011L), 0b0000000_000100000_0000000_100010000_0000000_000101000_0000000_001000011L);
            put(new P(0b00_00_0001_111111_111111_111111_010011, 0b0000000_100100000_0000000_001010000_0000000_100000100_0000000_100000001L), 0b0000000_000000000_0000000_000000000_0000000_100000100_0000000_100000001L);
            put(new P(0b01_00_0010_111111_111111_110000_001000, 0b0000000_000110000_0000000_000100000_0000000_100000010_0000000_001100100L), 0b0000000_000000000_0000000_000000000_0000000_100000010_0000000_001100100L);
            put(new P(0b10_00_0111_111111_111111_111111_111111, 0b0000000_000000000_0000000_011000100_0000000_010000001_0000000_011100000L), 0b0000000_000000000_0000000_011000100_0000000_010000001_0000000_011100000L);
            put(new P(0b00_00_0100_111111_111111_111111_100010, 0b0000000_100010010_0000000_000000010_0000000_100000010_0000000_100001000L), 0b0000000_000000000_0000000_000000010_0000000_000000000_0000000_100001000L);
            put(new P(0b11_10_0110_111111_111111_010011_100011, 0b0000000_000001000_0000000_000000010_0000000_011110000_0000000_010010000L), 0b0000000_000001000_0000000_000000010_0000000_000000000_0000000_000000000L);
            put(new P(0b00_01_0110_111111_111111_111111_111111, 0b0000000_000010110_0000000_000001000_0000000_010000100_0000000_000010001L), 0b0000000_000010110_0000000_000001000_0000000_010000100_0000000_000010001L);
            put(new P(0b00_00_0111_111111_111111_111111_010001, 0b0000000_001000010_0000000_000001000_0000000_001010100_0000000_100100000L), 0b0000000_000000000_0000000_000000000_0000000_001010100_0000000_100100000L);
            put(new P(0b00_11_1000_111111_111111_000110_100100, 0b0000000_010000010_0000000_010010010_0000000_000000000_0000000_010010001L), 0b0000000_000000000_0000000_010010010_0000000_000000000_0000000_010000000L);
        }
    };

//    @Test
//    void playableCards() {
//        /*
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i)
//        {
//            try {
//                long pkTrick;
//                long pkHand;
//                do pkTrick = rng.nextInt() & 0b11111111_11111111_11111111_11111111L; while (!PackedTrick.isValid((int)pkTrick) || (i%3 + 1) <= PackedTrick.size((int)pkTrick));
//                do pkHand = rng.nextLong() & PackedCardSet.ALL_CARDS; while (!PackedCardSet.isValid(pkHand) || 8 < PackedCardSet.size(pkHand) || PackedCardSet.isEmpty(pkHand));
//                System.out.println("put(new P(0b" + s(pkTrick, 32) + ", 0b" + s(pkHand, 64) + "L), 0b" + s(PackedTrick.playableCards((int)pkTrick, pkHand), 64) + "L);");
//            }
//            catch (Error e) {
//
//            }
//            assertTrue(true);
//        }
//        /*/
//        for(P p : playableCardsList.keySet()) {
//            long expected = playableCardsList.get(p);
//            long actual = PackedTrick.playableCards(p.pkTrick, p.pkHand);
//            if(expected != actual) {
//                System.out.println("pkTrick:" + PackedTrick.toString(p.pkTrick));
//                System.out.println("pkHand:" + PackedCardSet.toString(p.pkHand));
//                System.out.println();
//                System.out.println("expected:" + PackedCardSet.toString(playableCardsList.get(p)));
//                System.out.println("actual:  " + PackedCardSet.toString(PackedTrick.playableCards(p.pkTrick, p.pkHand)));
//            }
//            assertEquals(expected, actual);
//        }
//        //*/
//    }

//    String s(long l, int m)
//    {
//        String s = Long.toString(l, 2);
//        while (s.length() % m != 0)
//            s = "0" + s;
//        if(m == 64)
//            s = s.substring(0, 7) + "_" + s.substring(7, 16) + "_" +
//                    s.substring(16, 16 + 7) + "_" + s.substring(16 + 7, 16 + 16) + "_" +
//                    s.substring(32, 32 + 7) + "_" + s.substring(32 + 7, 32 + 16) + "_" +
//                    s.substring(48, 48 + 7) + "_" + s.substring(48 + 7, 48 + 16);
//        if(m == 32)
//            s = s.substring(0, 2) + "_" +
//                    s.substring(2, 4) + "_" +
//                    s.substring(4, 8) + "_" +
//                    s.substring(8, 14) + "_" +
//                    s.substring(14, 20) + "_" +
//                    s.substring(20, 26) + "_" +
//                    s.substring(26, 32);
//        return s;
//    }




//    @Test
//    void winningPlayerTest() {
//        int playerId = 0;
//        int counter = 0;
//        ArrayList<Integer> cards = new ArrayList<>();
//        Color trump;
//        int noIdea = 0;
//        boolean winnter = false;
//        boolean is = false;
//        boolean comming = false;
//        int winningCard;
//        int nbCard = -1;
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)
//                    && ((Bits32.extract(iterations, 0, 6) != 0b111111)
//                            || Bits32.extract(iterations, 6, 6) != 0b111111
//                            || Bits32.extract(iterations, 12, 6) != 0b111111
//                            || Bits32.extract(iterations, 18, 6) != 0b111111)) {
//                cards.clear();
//                ;
//                counter = 0;
//                playerId = Bits32.extract(iterations, 28, 2);
//                trump = Card.Color.ALL.get(Bits32.extract(iterations, 30, 2));
//                for (int j = 0; j < 4; ++j) {
//                    if (Bits32.extract(iterations, 6 * j, 6) != 0b111111) {
//                        cards.add(Bits32.extract(iterations, 6 * j, 6));
//                        counter += 1;
//                    }
//                }
//                winningCard = cards.get(0);
//                for (int k = 0; k < counter - 1; ++k) {
//                    if (PackedCard.isBetter(trump, cards.get(k + 1),
//                            winningCard)) {
//                        winningCard = cards.get(k + 1);
//                    }
//                }
//                noIdea = cards.indexOf(winningCard);
//                assertEquals((noIdea + playerId) % 4,
//                        PackedTrick.winningPlayer(iterations).ordinal());
//
//            }
//        }
//    }

//    @Test
//    void playableCardTestUnit2() {
//        int pkTrick1 = PackedTrick.firstEmpty(Card.Color.SPADE,
//                PlayerId.PLAYER_1);
//        long pkHand1 = 0b0000_0000_0010_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000L;
//        int pkTrick2 = Bits32.pack(0b110, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand2 = 0b0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_1010_0000_0000_0010_0000L;
//        int pkTrick3 = Bits32.pack(0b110, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand3 = 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_1010_0000L;
//        int pkTrick4 = Bits32.pack(0b10_0110, 6, 0b0, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand4 = 0b0000_0000_0010_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0010L;
//        int pkTrick5 = Bits32.pack(0b11_0001, 6, 0b1_0011, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b1,
//                2);
//        long pkHand5 = 0b0000_0000_0000_0000_0000_0000_1000_0000_0000_0000_0000_0100_0000_0000_1000_0001L;
//        int pkTrick6 = Bits32.pack(0b1_0101, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand6 = 0b0000_0000_0000_0001_0000_0000_0001_0000_0000_0000_1000_0000_0000_0000_0000_1000L;
//        int pkTrick7 = Bits32.pack(0b11_0011, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b11,
//                2);
//        long pkHand7 = 0b0000_0000_0000_0000_0000_0000_1000_1000_0000_0000_0100_0000_0000_0001_0000_0000L;
//        int pkTrick8 = Bits32.pack(0b1000, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b10,
//                2);
//        long pkHand8 = 0b0000_0000_0000_0000_0000_0000_0010_0010_0000_0000_0100_0000_0000_0000_1000_0000L;
//        int pkTrick9 = Bits32.pack(0b11_0100, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b10,
//                2);
//        long pkHand9 = 0b0000_0000_0000_0010_0000_0000_0000_1000_0000_0000_0001_0001_0000_0000_0000_0000L;
//        int pkTrick10 = Bits32.pack(0b10_0100, 6, 0b0100, 6, PackedCard.INVALID,
//                6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand10 = 0b0000_0000_1000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0000L;
//        int pkTrick11 = Bits32.pack(0b10_0100, 6, 0b0100, 6, PackedCard.INVALID,
//                6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand11 = 0b0000_0000_0000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0100L;
//        System.out.println("{\u266110 \u2663J} expected\n" + PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick1, pkHand1)));
//        System.out.println();
//        System.out.println("{\u2660J \u26617 \u26619 \u266310} expected\n"
//                + PackedCardSet.toString(
//                        PackedTrick.playableCards(pkTrick2, pkHand2)));
//        System.out.println();
//        System.out.println("{\u2660J \u2660K} expected\n" + PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick3, pkHand3)));
//        System.out.println();
//        System.out.println("{\u26607 \u266210} expected\n" + PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick4, pkHand4)));
//        System.out.println();
//        System.out.println(
//                "{\u26606 \u2660K \u2662K} expected\n" + PackedCardSet.toString(
//                        PackedTrick.playableCards(pkTrick5, pkHand5)));
//        System.out.println();
//        System.out.println("{\u26609 \u2661K} expected\n" + PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick6, pkHand6)));
//        System.out.println();
//        System.out.println("{\u2660A \u2661Q \u26629 \u2662K} expected\n"
//                + PackedCardSet.toString(
//                        PackedTrick.playableCards(pkTrick7, pkHand7)));
//        System.out.println();
//        System.out.println(
//                "{\u2660K \u26627 \u2662J} expected\n" + PackedCardSet.toString(
//                        PackedTrick.playableCards(pkTrick8, pkHand8)));
//        System.out.println();
//        System.out.println("{\u26629 \u26637} expected\n" + PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick9, pkHand9)));
//        System.out.println();
//        System.out.println("{\u2662J \u2662K} expected\n" + PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick10, pkHand10)));
//        System.out.println();
//        System.out.println("{\u2662J \u2662K} expected\n" + PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick11, pkHand11)));
//    }

//    @Test
//    void playableCardsWorks() {
//        // deux cartes jouables: valet de pique, as de coeur
//        // en main: 8 de pique, valet de pique, as de coeur, 7 de trèfle
//        // premiere carte jouée: 6 de coeur, as de pique, 6 de trèfle, atout
//        // pique
//        assertEquals(0b100000000_0000000000100000L, PackedTrick.playableCards(
//                0b00_11_0000_111111_110000_001000_010000,
//                0b10_0000000000000000_0000000100000000_0000000000100100L));
//
//        // en main: 9 de pique, valet de pique, 6 de coeur, 7 coeur;
//        // premiere carte jouée: 10 de coeur, atout carreau
//        assertEquals(0b11_0000000000000000L, PackedTrick.playableCards(
//                0b10_11_0000_111111_111111_111111_010100,
//                0b00_0000000000000000_0000000000000011_0000000000101000L));
//
//        // en main: 6 de pique, 7 de pique, 8 de pique, 6 de coeur
//        // premiere carte jouée: 9 de pique, 2nd: 10 de pique, atout pique
//        assertEquals(0b0_0000000000000111L, PackedTrick.playableCards(
//                0b00_11_0000_111111_111111_000100_000011,
//                0b00_0000000000000000_0000000000000001_0000000000000111L));
//
//        // en main: 6 de pique, 7 de pique, 8 de pique, 6 de coeur
//        // premiere carte jouée: 9 de carreau, 2nd: 10 de pique, atout carreau
//        assertEquals(0b1_0000000000000111L, PackedTrick.playableCards(
//                0b10_11_0000_111111_111111_000100_100011,
//                0b00_0000000000000000_0000000000000001_0000000000000111L));
//
//        // en main: 6 de pique, 7 de pique, 8 de pique, as de pique
//        // premiere carte jouée: 9 de carreau, 2nd: 10 de pique, atout pique
//        assertEquals(0b0_0000000100000000L, PackedTrick.playableCards(
//                0b00_11_0000_111111_111111_000100_100011,
//                0b00_0000000000000000_0000000000000000_0000000100000111L));
//
//        // atout pas demandé, mais pas coupé, et peut pas suivre
//        assertEquals(0b1000000000000000000000001000000000000000000100100L,
//                PackedTrick.playableCards(
//                        0b00_11_0000_111111_110001_010000_100000,
//                        0b1000000000000000000000001000000000000000000100100L));
//        assertEquals(0b1000000000000000000100000L,
//                PackedTrick.playableCards(
//                        0b00_10_0000_111111_111111_001000_010000,
//                        0b1000000000000000000000001000000000000000000100100L));
//
//        // que des atouts dans la main, pas atout demandé, mais qqn coupe et pas
//        // moyen de surcouper
//        // en main: 6, 7 et 8 de pique
//        // première carte jouée: 9 de carreau, deuxieme carte jouée: 10 de
//        // pique, atout pique
//        assertEquals(0b0_0000000000000111L, PackedTrick.playableCards(
//                0b00_11_0000_111111_111111_000100_100011,
//                0b00_0000000000000000_0000000000000000_0000000000000111L));
//
//        // quand qqn a couper, tu peux pas sous couper, tu peux pas suivre, et
//        // t'as une carte qui n'est pas d'atout
//        // 6, 7 ,8 de pique et 6 de coeur, atout pique,
//        // premiere carte jouee 9 de carreau, deuxieme carte 10 de pique
//        assertEquals(0b1_0000000000000000L, PackedTrick.playableCards(
//                0b00_11_0000_111111_111111_000100_100011,
//                0b00_0000000000000000_0000000000000001_0000000000000111L));
//
//        // qqn a coupé, peut surcouper, peut pas suivre
//        // 6, 7 ,9 de pique et 6 de coeur, atout pique,
//        // premiere carte jouee 9 de carreau, deuxieme carte 10 de pique
//        assertEquals(0b1_0000000000001000L, PackedTrick.playableCards(
//                0b00_11_0000_111111_111111_000100_100011,
//                0b00_0000000000000000_0000000000000001_0000000000001011L));
//
//        // qqn a coupé, pas surcouper, peut suivre
//        // 6, 7 ,8 de pique et 6 de coeur, atout pique,
//        // premiere carte jouee 9 de coeur, deuxieme carte 10 de pique
//        assertEquals(0b1_0000000000000000L, PackedTrick.playableCards(
//                0b00_11_0000_111111_111111_000100_010011,
//                0b00_0000000000000000_0000000000000001_0000000000000111L));
//
//        // on est le quatrieme joueur, les deux joueurs precedents ont coupe
//        // 6, 7 ,8 de pique et 6 de coeur, atout pique,
//        // premiere carte jouee 9 de coeur, deuxieme carte 10 de pique,
//        // troisième carte bourre
//        assertEquals(0b1_0000000000000000L, PackedTrick.playableCards(
//                0b00_11_0000_111111_000101_000100_010011,
//                0b00_0000000000000000_0000000000000001_0000000000000111L));
//
//        // on est le quatrieme joueur, les deux joueurs precedents ont coupe
//        // 6, 7 ,9 de pique et 6 de coeur, atout pique,
//        // premiere carte jouee 9 de coeur, deuxieme carte 10 de pique,
//        // troisième carte bourre
//        assertEquals(0b1_0000000000000000L, PackedTrick.playableCards(
//                0b00_11_0000_111111_000101_000100_010011,
//                0b00_0000000000000000_0000000000000001_0000000000001011L));
//
//        // qqn a coupé, peut surcouper, peut suivre
//        // 6, 7 ,9 de pique et 6 de coeur, atout pique
//        // premiere carte jouee 9 de coeur, deuxieme carte 10 de pique
//        assertEquals(0b00_0000000000000000_0000000000000001_0000000000001000L,
//                PackedTrick.playableCards(
//                        0b00_11_0000_111111_111111_000100_010011,
//                        0b00_0000000000000000_0000000000000001_0000000000001011L));
//
//        // qqn a coupé, pas d'atout plus haut, soit autre couleur, pas de
//        // couleur de base
//        // 6, 7 ,8 de pique et 6 de coeur, atout pique
//        // premiere carte jouee 9 de carreau, deuxieme carte 10 de pique
//        assertEquals(0b1_0000000000000000L, PackedTrick.playableCards(
//                0b00_11_0000_111111_111111_000100_100011,
//                0b00_0000000000000000_0000000000000001_0000000000000111L));
//
//        // cas normal: atout demandé, tout le monde a atout, atout pique
//        // en main: 6, 7, 8 de pique, 6 de coeur
//        // première carte jouée: 9 de pique, après 10 de pique, après bourre
//        assertEquals(0b0_0000000000000111L, PackedTrick.playableCards(
//                0b00_11_0000_111111_000101_000100_000011,
//                0b00_0000000000000000_0000000000000001_0000000000000111L));
//
//        // personne n'a coupé, on a de l'atout
//        // en main: 6, 7, 8 de pique
//        // première carte jouée: 9 de coeur, après 10 de coeur, après valet de
//        // coeur
//        assertEquals(0b0_0000000000000111L, PackedTrick.playableCards(
//                0b00_11_0000_111111_010101_010100_010011,
//                0b00_0000000000000000_0000000000000000_0000000000000111L));
//
//        // peut couper mais peut pas suivre
//        // en main: 6, 7, 8 de pique, 6 de coeur
//        // première carte jouée: 9 de coeur, après 10 de coeur, après valet de
//        // coeur
//        assertEquals(0b1_0000000000000111L, PackedTrick.playableCards(
//                0b00_11_0000_111111_010101_010100_010011,
//                0b00_0000000000000000_0000000000000001_0000000000000111L));
//
//        // cas de la dernière pli, on peut jouer tout ce qu'on a en main
//        // en main: 6 de pique
//        // première carte jouée: 7 de coeur, atout coeur
//        assertEquals(0b0_0000000000000001L, PackedTrick.playableCards(
//                0b01_11_0000_111111_111111_111111_010001,
//                0b00_0000000000000000_0000000000000000_0000000000000001L));
//
//        // cas de la dernière pli, on peut jouer tout ce qu'on a en main
//        // en main: 6 de pique
//        // première carte jouée: 7 de coeur, deuxieme carte: 8 de carreau atout
//        // coeur
//        assertEquals(0b0_0000000000000001L, PackedTrick.playableCards(
//                0b01_11_0000_111111_111111_100011_010001,
//                0b00_0000000000000000_0000000000000000_0000000000000001L));
//
//        // cas de la dernière pli, on peut jouer tout ce qu'on a en main
//        // en main: 6 de pique
//        // première carte jouée: 7 de coeur, deuxieme carte: 8 de carreau,
//        // troisieme carte: as de trefle, atout coeur
//        assertEquals(0b0_0000000000000001L, PackedTrick.playableCards(
//                0b01_11_0000_111111_111000_100011_010001,
//                0b00_0000000000000000_0000000000000000_0000000000000001L));
//
//        // jamais une main vide, set retourné toujours un subset de la main
//        // donnée
//
//        ArrayList<Integer> cards = new ArrayList<>();
//        for (int i = 0; i < Card.Rank.COUNT; ++i) {
//            for (int j = 0; j < Card.Color.COUNT; ++j) {
//                cards.add(PackedCard.pack(Color.ALL.get(j), Rank.ALL.get(i)));
//            }
//        }
//        for (int i = 1; i < PackedCardSet.subsetOfColor(PackedCardSet.ALL_CARDS,
//                Color.SPADE); ++i) {
//            for (int j = 0; j < cards.size(); ++j) {
//                for (int m = 0; m < cards.size(); ++m) {
//                    assertTrue(PackedTrick.playableCards(
//                            (0b00_11_0000_111111_111111 << 12)
//                                    | (cards.get(j) << 6) | cards.get(m),
//                            ((i << 48) | (i << 32) | (i << 16) | i)) > 0);
//                    assertTrue((PackedTrick.playableCards(
//                            (0b00_11_0000_111111_111111 << 12)
//                                    | (cards.get(j) << 6) | cards.get(m),
//                            ((i << 48) | (i << 32) | (i << 16) | i))
//                            & ((i << 48) | (i << 32) | (i << 16) | i)) > 0);
//                }
//            }
//        }
//    }

//    @Test
//    void playableCardTestUnit() {
//        int pkTrick1 = PackedTrick.firstEmpty(Card.Color.SPADE,
//                PlayerId.PLAYER_1);
//        long pkHand1 = 0b0000_0000_0010_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_0000_0000L;
//        int pkTrick2 = Bits32.pack(0b110, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand2 = 0b0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_1010_0000_0000_0010_0000L;
//        int pkTrick3 = Bits32.pack(0b110, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand3 = 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001_0000_0000_0000_1010_0000L;
//        int pkTrick4 = Bits32.pack(0b10_0110, 6, 0b0, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand4 = 0b0000_0000_0010_0000_0000_0000_0001_0000_0000_0000_0000_0000_0000_0000_0000_0010L;
//        int pkTrick5 = Bits32.pack(0b11_0001, 6, 0b1_0011, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b1,
//                2);
//        long pkHand5 = 0b0000_0000_0000_0000_0000_0000_1000_0000_0000_0000_0000_0100_0000_0000_1000_0001L;
//        int pkTrick6 = Bits32.pack(0b1_0101, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand6 = 0b0000_0000_0000_0001_0000_0000_0001_0000_0000_0000_1000_0000_0000_0000_0000_1000L;
//        int pkTrick7 = Bits32.pack(0b11_0011, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b11,
//                2);
//        long pkHand7 = 0b0000_0000_0000_0000_0000_0000_1000_1000_0000_0000_0100_0000_0000_0001_0000_0000L;
//        int pkTrick8 = Bits32.pack(0b1000, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b10,
//                2);
//        long pkHand8 = 0b0000_0000_0000_0000_0000_0000_0010_0010_0000_0000_0100_0000_0000_0000_1000_0000L;
//        int pkTrick9 = Bits32.pack(0b11_0100, 6, PackedCard.INVALID, 6,
//                PackedCard.INVALID, 6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0b10,
//                2);
//        long pkHand9 = 0b0000_0000_0000_0010_0000_0000_0000_1000_0000_0000_0001_0001_0000_0000_0000_0000L;
//        int pkTrick10 = Bits32.pack(0b10_0100, 6, 0b0100, 6, PackedCard.INVALID,
//                6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand10 = 0b0000_0000_1000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0000L;
//        int pkTrick11 = Bits32.pack(0b10_0100, 6, 0b0100, 6, PackedCard.INVALID,
//                6, PackedCard.INVALID, 6, 0, 4, 0, 2, 0, 2);
//        long pkHand11 = 0b0000_0000_0000_0000_0000_0000_1010_0000_0000_0000_0000_0001_0000_0000_0000_0100L;
//        assertEquals("{\u266110,\u2663J}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick1, pkHand1)));
//        assertEquals("{\u2660J,\u26617,\u26619,\u266310}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick2, pkHand2)));
//        assertEquals("{\u2660J,\u2660K}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick3, pkHand3)));
//        assertEquals("{\u26607,\u266210}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick4, pkHand4)));
//        assertEquals("{\u26606,\u2660K,\u2662K}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick5, pkHand5)));
//        assertEquals("{\u26609,\u2661K}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick6, pkHand6)));
//        assertEquals("{\u2660A,\u2661Q,\u26629,\u2662K}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick7, pkHand7)));
//        assertEquals("{\u2660K,\u26627,\u2662J}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick8, pkHand8)));
//        assertEquals("{\u26629,\u26637}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick9, pkHand9)));
//        assertEquals("{\u2662J,\u2662K}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick10, pkHand10)));
//        assertEquals("{\u2662J,\u2662K}", PackedCardSet
//                .toString(PackedTrick.playableCards(pkTrick11, pkHand11)));
//    }
//
//    @Test
//    void isValidWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            assertEquals(IsValidTest(iterations), PackedTrick.isValid(iterations));
//        }
//    }
//
//    private boolean IsValidTest(int pkTrick) {
//        if ((Bits32.extract(pkTrick, 24, 4) < 9)) {
//            int Card0 = Bits32.extract(pkTrick, 0, 6);
//            int Card1 = Bits32.extract(pkTrick, 6, 6);
//            int Card2 = Bits32.extract(pkTrick, 12, 6);
//            int Card3 = Bits32.extract(pkTrick, 18, 6);
//            if ((PackedCard.isValid(Card0) && PackedCard.isValid(Card1)
//                    && PackedCard.isValid(Card2)
//                    && PackedCard.isValid(Card3))) {
//                return true;
//            }
//            if ((PackedCard.isValid(Card0) && PackedCard.isValid(Card1)
//                    && PackedCard.isValid(Card2) && (Card3 == 0b111111))) {
//                return true;
//            }
//            if ((PackedCard.isValid(Card0) && PackedCard.isValid(Card1)
//                    && (Card2 == 0b111111) && (Card3 == 0b111111))) {
//                return true;
//            }
//            if ((PackedCard.isValid(Card0) && (Card1 == 0b111111)
//                    && (Card2 == 0b111111)) && (Card3 == 0b111111)) {
//                return true;
//            }
//            if ((Card0 == 0b111111) && (Card1 == 0b111111)
//                    && (Card2 == 0b111111) && (Card3 == 0b111111)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Test
//    void firstEmptyWorks() {
//        for (int j = 0; j < 4; ++j) {
//            for (int i = 0; i < 4; ++i) {
//                int trick = PackedTrick.firstEmpty(Color.ALL.get(j),
//                        PlayerId.ALL.get(i));
//                int shouldBe1 = Bits32.extract(trick, 0, 24);
//                int shouldBe0 = Bits32.extract(trick, 24, 4);
//                int shouldBePlayer = Bits32.extract(trick, 28, 2);
//                int shouldBeColor = Bits32.extract(trick, 30, 2);
//                assertTrue((shouldBeColor == (j)));
//                assertTrue((shouldBePlayer == (i)));
//                assertTrue(shouldBe0 == 0);
//                assertTrue(shouldBe1 == 0b111111111111111111111111);
//            }
//        }
//    }
//
//    @Test
//    void nextEmptyWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)
//                    && (Bits32.extract(iterations, 0, 6) != 0b111111)
//                    && (Bits32.extract(iterations, 6, 6) != 0b111111)
//                    && (Bits32.extract(iterations, 12, 6) != 0b111111)
//                    && (Bits32.extract(iterations, 18, 6) != 0b111111)) {
//                if (Bits32.extract(iterations, 24, 4) == 8) {
//                    assertEquals(PackedTrick.INVALID,
//                            PackedTrick.nextEmpty(iterations));
//                } else {
//                    int nextTrick = PackedTrick.nextEmpty(iterations);
//
//                    assertEquals(Bits32.extract(nextTrick, 0, 24),
//                            0b111111111111111111111111);
//                    assertEquals(Bits32.extract(iterations, 30, 2),
//                            Bits32.extract(nextTrick, 30, 2));
//                    assertEquals(PackedTrick.winningPlayer(iterations),
//                            PlayerId.ALL.get(Bits32.extract(nextTrick, 28, 2)));
//                    assertEquals(Bits32.extract(iterations, 24, 4) + 1,
//                            Bits32.extract(nextTrick, 24, 4));
//                }
//            }
//        }
//    }
//
//    @Test
//    void sizeWorks() {
//        int shouldBeThisSize;
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            shouldBeThisSize = 0;
//            if (PackedTrick.isValid(iterations)) {
//                for (int j = 0; j < 4; ++j) {
//                    if (Bits32.extract(iterations, 6 * j, 6) != 0b111111) {
//                        shouldBeThisSize += 1;
//                    }
//                }
//                assertEquals(shouldBeThisSize, PackedTrick.size(iterations));
//            }
//        }
//    }
//
//    @Test
//    void cardWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                for (int j = 0; j < 4; ++j) {
//                    int shouldBeThatCard = Bits32.extract(iterations, 6 * j, 6);
//                    if (PackedCard.isValid(shouldBeThatCard)) {
//                        assertEquals(shouldBeThatCard,
//                                PackedTrick.card(iterations, j));
//
//                    }
//                }
//            }
//        }
//    }
//
//    @Test
//    void withAddedCardWorks() {
//        int k;
//        int index;
//        int ones = 0b111111;
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations) && ((iterations
//                    & 0b111111000000000000000000) == (0b111111 << 18))) {
//                for (int j = 0; j < 64; ++j) {
//                    if (PackedCard.isValid(j)) {
//                        k = 0;
//                        index = 0;
//                        while (k < 4) {
//                            if (Bits32.extract(iterations, k * 6, 6) == 0b111111) {
//                                index = k;
//                                k = 5;
//                            } else {
//                                k++;
//                            }
//                        }
//
//                        assertEquals(
//                                (iterations - (ones << 6 * index)) + (j << 6 * index),
//                                PackedTrick.withAddedCard(iterations, j));
//                    }
//                }
//            }
//        }
//    }
//
//    @Test
//    void isLastWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                if (Bits32.extract(iterations, 24, 4) == 8) {
//                    assertTrue(PackedTrick.isLast(iterations));
//                } else {
//                    assertFalse(PackedTrick.isLast(iterations));
//                }
//            }
//        }
//    }
//
//    @Test
//    void isEmptyWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                if (Bits32.extract(iterations, 0, 24) == 0b111111111111111111111111) {
//                    assertTrue(PackedTrick.isEmpty(iterations));
//                } else {
//                    assertFalse(PackedTrick.isEmpty(iterations));
//                }
//            }
//        }
//    }
//
//    @Test
//    void isFullWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                if ((Bits32.extract(iterations, 0, 4) < 9)
//                        && (Bits32.extract(iterations, 6, 4) < 9)
//                        && (Bits32.extract(iterations, 12, 4) < 9)
//                        && (Bits32.extract(iterations, 18, 4) < 9)) {
//                    assertTrue(PackedTrick.isFull(iterations));
//                }
//
//                else {
//                    assertFalse(PackedTrick.isFull(iterations));
//                }
//            }
//        }
//    }
//
//    @Test
//    void trumpWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                int colorIndex = Bits32.extract(iterations, 30, 2);
//                assertEquals(Color.ALL.get(colorIndex),
//                        PackedTrick.trump(iterations));
//            }
//        }
//    }
//
//    @Test
//    void playerWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                int playerIndex = Bits32.extract(iterations, 28, 2);
//                for (int j = 0; j < 4; ++j) {
//                    assertEquals(PlayerId.ALL.get((playerIndex + j) % 4),
//                            PackedTrick.player(iterations, j));
//
//                }
//            }
//        }
//    }
//
//    @Test
//    void indexWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                int index = Bits32.extract(iterations, 24, 4);
//                assertEquals(index, PackedTrick.index(iterations));
//            }
//        }
//    }
//
//    @Test
//    void baseColorWorks() {
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                int shouldBeThatColor = Bits32.extract(iterations, 4, 2);
//                assertEquals(shouldBeThatColor,
//                        PackedTrick.baseColor(iterations).ordinal());
//            }
//        }
//    }
//
//    @Test
//    void pointsWorks() {
//        int Card0, Card1, Card2, Card3, trump, points0, points1, points2,
//                points3, pointBonus;
//        SplittableRandom rng = newRandom();
//        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
//            int iterations = rng.nextInt();
//            if (PackedTrick.isValid(iterations)) {
//                Card0 = Bits32.extract(iterations, 0, 6);
//                Card1 = Bits32.extract(iterations, 6, 6);
//                Card2 = Bits32.extract(iterations, 12, 6);
//                Card3 = Bits32.extract(iterations, 18, 6);
//                trump = Bits32.extract(iterations, 30, 2);
//                pointBonus = 0;
//                points0 = 0;
//                points1 = 0;
//                points2 = 0;
//                points3 = 0;
//                if (PackedCard.isValid(Card0)) {
//                    points0 = PackedCard.points(Color.ALL.get(trump), Card0);
//                }
//                if (PackedCard.isValid(Card1)) {
//                    points1 = PackedCard.points(Color.ALL.get(trump), Card1);
//                }
//                if (PackedCard.isValid(Card2)) {
//                    points2 = PackedCard.points(Color.ALL.get(trump), Card2);
//                }
//                if (PackedCard.isValid(Card3)) {
//                    points3 = PackedCard.points(Color.ALL.get(trump), Card3);
//                }
//                if (Bits32.extract(iterations, 24, 4) == 8) {
//                    pointBonus += 5;
//                }
//                // les cartes entrée dans points sont toutes valides
//                // si on entre des mauvaises cartes , assertError
//                if (PackedCard.isValid(Card0) && PackedCard.isValid(Card1)
//                        && PackedCard.isValid(Card2)
//                        && PackedCard.isValid(Card3)) {
//                    int sum = points0 + points1 + points2 + points3
//                            + pointBonus;
//                    assertEquals(sum, PackedTrick.points(iterations));
//                }
//            }
//        }
//    }
}
