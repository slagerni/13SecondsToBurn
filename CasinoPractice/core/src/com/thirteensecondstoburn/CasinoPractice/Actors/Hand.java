package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nick on 1/15/2015.
 */
public class Hand extends Group {
    List<Card> cards;
    float offset = 0;
    List<Action> cardActions;
    int dealActionIndex = 0;
    int dealFromX, dealFromY;
    List<ActionCompletedListener> listeners = new ArrayList<ActionCompletedListener>();

    class DealRunnable implements Runnable {
        List<Card> cards;
        public DealRunnable(List<Card>cards) {
            this.cards = new ArrayList<Card>(cards.size());
            for(Card c : cards) this.cards.add(new Card(c));
        }

        @Override
        public void run() {
            dealActionIndex++;
            if(dealActionIndex < cards.size()) {
                dealCard(dealActionIndex);
            } else {
                notifyListeners();
            }
        }
    }

    public Hand(float offset) {
        this.offset = offset;
        this.cards = new ArrayList<Card>();
    }

    public Hand(List<Card> cards, float offset) {
        this.cards = new ArrayList<Card>(cards.size());
        for(Card c : cards) this.cards.add(new Card(c));

        this.offset = offset;

        cardActions = new ArrayList<Action>();
        for(int i=0; i<cards.size(); i++) {
            cardActions.add(sequence(
                    parallel(moveTo(getX() + offset * i, getY(), .3f), rotateTo(720f, .3f)),
                    run(new DealRunnable(cards))));
        }

        positionCards();
    }

    private void positionCards() {
        int start = 0;
        for(Card card : cards) {
            card.setX(start);
            addActor(card);
            start += offset;
        }
    }

    public void setCards(List<Card> newCards) {
        this.cards = new ArrayList<>(newCards.size());
        for(Card c : newCards) this.cards.add(new Card(c));
        positionCards();
    }

    public void sort() {
        Collections.sort(cards);
        positionCards();
    }

    public void sortLowAce() {
        Collections.sort(cards, Card.LowAceComparator);
        positionCards();
    }

    public boolean hasFaceValue(Card.FaceValue value) {
        for(Card c : cards) {
            if(c.getFaceValue() == value) return true;
        }
        return false;
    }

    public void addCardListener(EventListener listener) {
        for(Card card : cards) {
            card.addListener(listener);
        }
    }

    public void showHand() {
        for(Card card : cards) {
            card.setFaceUp(true);
        }
    }

    public void dealAction(int fromX, int fromY) {
        dealFromX = fromX;
        dealFromY = fromY;
        dealActionIndex = 0;
        for(Card card : cards) {
            card.setPosition(dealFromX, dealFromY);
        }
        dealCard(dealActionIndex);
    }

    private void dealCard(int index) {
        Card card = cards.get(index);
        card.addAction(cardActions.get(index));
    }

    public void dealCards(int fromX, int fromY) {
        cardActions = new ArrayList<>();
        for(int i=0; i<cards.size(); i++) {
            cardActions.add(sequence(
                    parallel(moveTo(offset * i, getY(), .3f), rotateTo(720f, .3f)),
                    run(new DealRunnable(cards))));
        }
        dealAction(fromX, fromY);
    }

    public void addCard(Card card) {
        cards.add(card);
        positionCards();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addActionListener(ActionCompletedListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for(ActionCompletedListener l : listeners) {
            l.actionCompleted(this);
        }
    }

    public void clear() {
        for(Card c : cards) {
            removeActor(c);
        }
        cards.clear();
        if(cardActions != null ) {
            cardActions.clear();
        }
    }
}
