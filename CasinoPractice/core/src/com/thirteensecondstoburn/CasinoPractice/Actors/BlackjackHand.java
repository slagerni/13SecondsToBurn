package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 2/20/2015.
 */
public class BlackjackHand extends Group implements ActionCompletedListener {
    CasinoPracticeGame game;
    Assets assets;
    Hand hand;
    Text handText;
    ChipStackGroup betStack;
    List<ActionCompletedListener> listeners = new ArrayList<ActionCompletedListener>();


    private int hard = 0;
    private int soft = 0;

    private boolean isSplit = false;
    private boolean isBlackjack = false;
    private boolean surrender = false;

    public BlackjackHand(final CasinoPracticeGame game, Assets assets) {
        this(game, assets, false);
    }

    public BlackjackHand(final CasinoPracticeGame game, Assets assets, boolean isSplit) {
        this.game = game;
        this.assets = assets;
        this.isSplit = isSplit;

        betStack = new ChipStackGroup(game, assets, Assets.TEX_NAME.BLANK_CIRCLE);

        hand = new Hand(75);
        hand.setPosition(betStack.getWidth() + 10, 0);
        hand.addActionListener(this);
        hand.setName("InnerHand");

        handText = new Text(assets, "", 1f);
        handText.setPosition(betStack.getWidth() + 10, Card.CARD_HEIGHT + 10);

        setSize(betStack.getWidth() + hand.getWidth() + 25, Card.CARD_HEIGHT + handText.getHeight() + 15);

        addActor(betStack);
        addActor(hand);
        addActor(handText);
    }

    public void addCard(Card card) {
        addCard(card, true);
    }
    public void addCard(Card card, boolean faceUp) {
        card.setFaceUp(faceUp);
        hand.addCard(card);
        setHandTotals();
    }

    public void setHandText(String text) {
        handText.setText(text);
    }

    public void setBetTotal(int amount) {
        betStack.setTotal(amount);
    }

    public int getBetTotal() {
        return betStack.getTotal();
    }

    public void surrender() {
        surrender = true;
    }

    public boolean surrendered() {
        return surrender;
    }

    public int getHard() { return hard; }
    public int getSoft() { return soft; }

    private void setHandTotals() {
        if(surrender) {
            setHandText("Surrender");
            return;
        }

        boolean foundAce = false;
        hard = 0;
        soft = 0;

        for(Card c : hand.getCards()) {
            if(!c.isFaceUp()) {
                continue; // don't count face down cards
            }

            Card.FaceValue fv = c.getFaceValue();
            if(!foundAce) {
                if (fv == Card.FaceValue.ACE) {
                    foundAce = true;
                    hard += fv.getFaceValue();
                    soft += 1;
                } else {
                    hard += fv.getFaceValue();
                    soft += fv.getFaceValue();
                }
            } else {
                if (fv == Card.FaceValue.ACE) {
                    hard += 1; // can't do multiple aces as 11
                    soft += 1;
                } else {
                    hard += fv.getFaceValue();
                    soft += fv.getFaceValue();
                }
            }
        }
        // show the running total(s)
        isBlackjack = false;
        // check blackjack even w/ the facedown card
        if(hand.getCards().size() == 2 && (hand.getCards().get(0).getFaceValue().getFaceValue() + hand.getCards().get(1).getFaceValue().getFaceValue() == 21) && !isSplit) {
            setHandText("Blackjack!");
            isBlackjack = true;
        } else if(soft > 21) {
            setHandText("Bust");
        } else if(hard == soft) {
            setHandText("" + hard);
        } else if (hard > 21) {
            setHandText("" + soft);
        } else {
            setHandText("" + hard);
        }
    }

    public void clear() {
        hand.clear();
        betStack.clear();
        handText.setText("");
        hard = 0;
        soft = 0;
        surrender = false;
        isSplit = false;
    }

    public void deal(int x, int y) {
        hand.dealCards(x, y);
    }

    public boolean canSplit() {
        return hand.getCards().size() == 2 && hand.getCards().get(0).getFaceValue().getFaceValue() == hand.getCards().get(1).getFaceValue().getFaceValue();
    }

    public Card splitHand() {
        isSplit = true;
        // can only split if there are 2 cards so remove the second one and return it so it can be moved elsewhere
        if(canSplit())
            return hand.getCards().remove(1);

        return null;
    }

    public void addActionListener(ActionCompletedListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(Actor actor) {
        for(ActionCompletedListener l : listeners) {
            l.actionCompleted(actor);
        }
    }

    public void hideBets() {
        removeActor(betStack);
        hand.setPosition(0,0);
        // if we're hiding the bets, put the text at the bottom and slide the hand up a bit (dealer hand)
        handText.setPosition(0, 0);
        hand.setPosition(0, 30);
    }

    public void showCards() {
        for(Card c : hand.getCards()) {
            c.setFaceUp(true);
        }
        setHandTotals();
    }

    public boolean isBlackjack() {return isBlackjack;}

    public int bestHandTotal() {
        if ((hard > 21 && soft > 21) || surrender) {
            return -1; // BUST
        } else if (hard <= 21 && hard > soft) {
            return hard;
        } else {
            return soft;
        }
    }

    public void popStack(boolean won) {
        betStack.popStack(won);
    }

    public Card getFirstCard() {
        return hand.getCards().get(0);
    }

    @Override
    public void actionCompleted(Actor caller) {
        notifyListeners(caller);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        hand.setColor(color);
    }
}
