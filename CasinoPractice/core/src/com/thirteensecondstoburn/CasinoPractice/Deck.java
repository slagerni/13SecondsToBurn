package com.thirteensecondstoburn.CasinoPractice;

import com.thirteensecondstoburn.CasinoPractice.Actors.Card;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Nick on 1/15/2015.
 */
public class Deck {
    private ArrayList<Card> cards = new ArrayList<Card>();
    private Random rand = new Random();

    // 52 card deck shortcut
    public Deck(Assets assets, Card.Back back) {
        this(assets, back, 1, 0);
    }

    public Deck(Assets assets, Card.Back back, int numJokers) {
        this(assets, back, 1, numJokers);
    }

    public Deck(Assets assets, Card.Back back, int numDecks, int numJokers) {
        for(int i=0; i< numDecks; i++) {
            cards.add(new Card(Card.FaceValue.ACE, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.KING, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.QUEEN, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.JACK, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.TEN, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.NINE, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.EIGHT, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.SEVEN, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.SIX, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.FIVE, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.FOUR, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.THREE, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.TWO, Card.Suit.CLUB, back, false, assets));
            cards.add(new Card(Card.FaceValue.ACE, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.KING, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.QUEEN, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.JACK, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.TEN, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.NINE, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.EIGHT, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.SEVEN, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.SIX, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.FIVE, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.FOUR, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.THREE, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.TWO, Card.Suit.DIAMOND, back, false, assets));
            cards.add(new Card(Card.FaceValue.ACE, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.KING, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.QUEEN, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.JACK, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.TEN, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.NINE, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.EIGHT, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.SEVEN, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.SIX, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.FIVE, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.FOUR, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.THREE, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.TWO, Card.Suit.HEART, back, false, assets));
            cards.add(new Card(Card.FaceValue.ACE, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.KING, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.QUEEN, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.JACK, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.TEN, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.NINE, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.EIGHT, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.SEVEN, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.SIX, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.FIVE, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.FOUR, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.THREE, Card.Suit.SPADE, back, false, assets));
            cards.add(new Card(Card.FaceValue.TWO, Card.Suit.SPADE, back, false, assets));
            if(numJokers > 0) {
                cards.add(new Card(Card.FaceValue.JOKER, Card.Suit.JOKER_BLACK, back, false, assets));
            }
            if(numJokers > 1) {
                cards.add(new Card(Card.FaceValue.JOKER, Card.Suit.JOKER_RED, back, false, assets));
            }

        }
    }

    public void shuffle() {
        ArrayList<Card> shuffled = new ArrayList<Card>(cards.size());
        while(cards.size() > 0) {
            int index = rand.nextInt(cards.size());
            shuffled.add(cards.get(index));
            cards.remove(index);
        }
        cards = shuffled;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
