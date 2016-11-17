package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStackGroup;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;
import com.thirteensecondstoburn.CasinoPractice.Actors.Hand;
import com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nick on 1/14/2015.
 */
public class ThreeCardPokerScreen extends TableScreen implements ActionCompletedListener {
    public static final float HAND_X_START = (CasinoPracticeGame.SCREEN_WIDTH - Card.CARD_WIDTH * 3) / 2;
    Deck deck;

    boolean isFirstDeck = true;
    boolean canBet = true;

    ChipStackGroup anteStack;
    ChipStackGroup pairPlusStack;
    ChipStackGroup playStack;

    TableButton dealButton;
    TableButton playButton;
    TableButton foldButton;
    TableButton clearButton;
    
    Hand dealerHand;
    Hand playerHand;
    
    Image paytable;
    Image title;

    Text dealerHandText;
    Text playerHandText;
    Text qualifyText;

    int lastAnteBet = 5;
    int lastPairPlusBet = 0;
    BestHand bestHandPlayer = null;

    public ThreeCardPokerScreen(CasinoPracticeGame game) {
        super(game);
    }

    @Override
    public void setup() {
        Gdx.input.setInputProcessor(stage);
        stage.addAction(Actions.alpha(1));

        lastAnteBet = game.getTableMinimum();

        paytable = new Image(assets.getTexture(Assets.TEX_NAME.THREE_CARD_POKER_PAYTABLE));
        paytable.setScale(.75f, .75f);
        paytable.setColor(1, 1, 1, .75f);
        paytable.setPosition(stage.getWidth() - 500 * paytable.getScaleX(), stage.getHeight() - 500 * paytable.getScaleY());

        title = new Image(assets.getTexture(Assets.TEX_NAME.THREE_CARD_POKER_TITLE));
        title.setColor(mainColor);
        title.setPosition(315, stage.getHeight() - 270);

        pairPlusStack = new ChipStackGroup(game, assets, Assets.TEX_NAME.PAIRPLUS_CIRCLE);
        pairPlusStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + pairPlusStack.getWidth()) / 2, stage.getHeight()/2 + 10);
        pairPlusStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pairPlusStack.hit(x, y, true) != null && canBet) {
                    clearButton.setVisible(true);
                    int betAmount = leftSide.getBetAmount();
                    if(anteStack.getTotal() + betAmount > game.getBalance()) {
                        showHint("Insufficient funds to make that bet");
                        playerHandText.setText("");
                        dealerHandText.setText("");
                        return;
                    }
                    if(pairPlusStack.getTotal() + betAmount > game.getTableMaximum()) {
                        showHint("Table maximum of " + game.getTableMaximum());
                        playerHandText.setText("");
                        dealerHandText.setText("");
                        return;
                    }
                    pairPlusStack.increaseTotal(betAmount);
                    lastPairPlusBet = pairPlusStack.getTotal();
                    subtractFromBalance(betAmount);
                    calculateWager();
                    dealButton.setVisible(true);
                    showHint("Pairplus bet is a suckers bet!");
                }
            }
        });

        anteStack = new ChipStackGroup(game, assets, Assets.TEX_NAME.ANTE_CIRCLE);
        anteStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + anteStack.getWidth()) / 2, pairPlusStack.getY() - 200);
        anteStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (anteStack.hit(x, y, true) != null && canBet) {
                    clearButton.setVisible(true);
                    int betAmount = leftSide.getBetAmount();
                    if(anteStack.getTotal() + leftSide.getBetAmount() > game.getBalance() - betAmount) {
                        showHint("Insufficient funds to make that bet");
                        playerHandText.setText("");
                        dealerHandText.setText("");
                        return;
                    }
                    if(anteStack.getTotal() + betAmount > game.getTableMaximum()) {
                        showHint("Table maximum of " + game.getTableMaximum());
                        playerHandText.setText("");
                        dealerHandText.setText("");
                        return;
                    }
                    anteStack.increaseTotal(betAmount);
                    lastAnteBet = anteStack.getTotal();
                    subtractFromBalance(betAmount);
                    calculateWager();
                    dealButton.setVisible(true);
                }
            }
        });

        playStack = new ChipStackGroup(game, assets, Assets.TEX_NAME.PLAY_CIRCLE);
        playStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + playStack.getWidth()) / 2, anteStack.getY() - 200);
        playStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (playStack.hit(x, y, true) != null && !canBet) {
                    playHand(playButton);
                }
            }
        });

        dealButton = new TableButton(assets, "Deal", mainColor);
        dealButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        dealButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dealButton.isInside(x, y)) {
                    dealHand();
                }
            }
        });
        dealButton.setVisible(true);

        playButton = new TableButton(assets, "Play", mainColor);
        playButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        playButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (playButton.isInside(x, y) && !canBet) {
                    playHand(playButton);
                    playButton.setVisible(false);
                }
            }
        });
        playButton.setVisible(false);

        clearButton = new TableButton(assets, "Clear", mainColor);
        clearButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT + 10);
        clearButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (clearButton.isInside(x, y)) {
                    clearHand();
                }
            }
        });
        clearButton.setVisible(false);

        foldButton = new TableButton(assets, "Fold", mainColor);
        foldButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT + 10);
        foldButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (foldButton.isInside(x, y)) {
                    foldHand();
                }
            }
        });
        foldButton.setVisible(false);

        dealerHandText = new Text(assets, "", 1.5f);
        dealerHandText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 90);

        playerHandText = new Text(assets, "", 1.5f);
        playerHandText.setPosition(HAND_X_START, Card.CARD_HEIGHT + 60);

        qualifyText = new Text(assets, "DEALER QUALIFIES WITH QUEEN HIGH", 1);
        qualifyText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - 40);
        qualifyText.setColor(.5f, .5f, .5f, .5f);

        stage.addActor(paytable);
        stage.addActor(title);

        stage.addActor(anteStack);
        stage.addActor(pairPlusStack);
        stage.addActor(playStack);

        stage.addActor(dealButton);
        stage.addActor(clearButton);
        stage.addActor(playButton);
        stage.addActor(foldButton);

        stage.addActor(dealerHandText);
        stage.addActor(playerHandText);
        stage.addActor(qualifyText);
    }

    private void dealHand() {
        if (playerHand != null) playerHand.remove();
        if (dealerHand != null) dealerHand.remove();
        playStack.setTotal(0);
        if (anteStack.getTotal() == 0) {
            if (lastAnteBet * 2 + lastPairPlusBet > game.getBalance()) {
                playerHandText.setText("You can't repeat your last bet");
                dealerHandText.setText("");
                return;
            }
            anteStack.setTotal(lastAnteBet);
            pairPlusStack.setTotal(lastPairPlusBet);
            leftSide.setWagerText("" + (lastAnteBet + lastPairPlusBet));
            subtractFromBalance(lastAnteBet + lastPairPlusBet);
        }

        if (anteStack.getTotal() < game.getTableMinimum()) {
            showHint("Minimum bet is " + game.getTableMinimum());
            dealerHandText.setText("");
            return;
        }

        statistics.Increment(CasinoPracticeStatistics.Dealt);

        Card.Back back;
        if (isFirstDeck) {
            back = Card.Back.BACK1;
        } else {
            back = Card.Back.BACK2;
        }
        isFirstDeck = !isFirstDeck;
        deck = new Deck(assets, back);
        deck.shuffle();

        dealerHand = new Hand(deck.getCards().subList(0, 3), Card.CARD_WIDTH);
        dealerHand.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 50);
        dealerHand.dealAction(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        dealerHand.addActionListener(this);


        // TEST
//        ArrayList<Card> playerTestCards = new ArrayList<Card>();
//        playerTestCards.add(new Card(Card.FaceValue.TWO, Card.Suit.HEART, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.THREE, Card.Suit.SPADE, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.ACE, Card.Suit.DIAMOND, Card.Back.BACK1, true, assets));
//        bestHandPlayer = new BestHand(playerTestCards);
        bestHandPlayer = new BestHand(deck.getCards().subList(3, 6));
        playerHand = new Hand(bestHandPlayer.sortedCards, Card.CARD_WIDTH);
        playerHand.setPosition(HAND_X_START, 50);
        playerHand.setVisible(false);
        playerHand.showHand();
        playerHand.addActionListener(this);

        stage.addActor(dealerHand);
        stage.addActor(playerHand);

        canBet = false;
        hideButtons(); // cause we're dealing and we'll turn them on when it's done
        leftSide.setWonText("");
        dealerHandText.setText("");
        playerHandText.setText("");
    }
    
    private void playHand(TableButton buttonPressed) {
        playStack.setTotal(anteStack.getTotal());
        subtractFromBalance(anteStack.getTotal());
        calculateWager();
        showDealerHand();
        calculateWinner(false);
        canBet = true;
        toggleButtons(false);
        if(game.useHintText()) {
            TableButton correctButton = getCorrectButtonForHint();
            if(correctButton != buttonPressed) {
                System.out.println("Correct: " + correctButton + " Pressed: " + buttonPressed);
                if(correctButton == playButton) {
                    showHint("Always bet with Q, 6, 4 or better");
                } else {
                    showHint("Always fold with worse than Q, 6, 4");
                }
            }
        }

    }

    private void foldHand() {
        calculateWinner(true);
        canBet = true;
        toggleButtons(false);
        showDealerHand();
        anteStack.clear();
        pairPlusStack.clear();
        if(game.useHintText()) {
            TableButton correctButton = getCorrectButtonForHint();
            if(correctButton != foldButton) {
                showHint("Always bet with Q, 6, 4 or better");
            }
        }

    }

    private void clearHand() {
        addToBalance(anteStack.getTotal() + pairPlusStack.getTotal());
        lastAnteBet = game.getTableMinimum();
        lastPairPlusBet = 0;
        anteStack.setTotal(0);
        pairPlusStack.setTotal(0);
        playStack.setTotal(0);
        leftSide.setWonText("");
        leftSide.setWagerText("");
        dealerHandText.setText("");
        playerHandText.setText("");
        canBet = true;
        toggleButtons(false);
    }

    private void calculateWager() {
        leftSide.setWagerText("" + (anteStack.getTotal() + pairPlusStack.getTotal() + playStack.getTotal()));
    }

    private void toggleButtons(boolean isPlaying) {
        dealButton.setVisible(!isPlaying);
        clearButton.setVisible(!isPlaying);
        playButton.setVisible(isPlaying);
        foldButton.setVisible(isPlaying);
    }

    private void hideButtons() {
        dealButton.setVisible(false);
        clearButton.setVisible(false);
        playButton.setVisible(false);
        foldButton.setVisible(false);
    }

    private void showDealerHand() {
        dealerHand.showHand();
    }

    @Override
    public void actionCompleted(Actor caller) {
        if(caller == dealerHand) {
            playerHand.setVisible(true);
            playerHand.dealAction(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        } else if(caller == playerHand) {
            toggleButtons(true);
            if(game.usePreActionHints()) {
                setPreBetHint();
            }
        }
    }

    private void setPreBetHint() {
        foldButton.setColor(mainColor);
        playButton.setColor(mainColor);

        TableButton button = getCorrectButtonForHint();
        button.setColor(hintColor);
    }

    private TableButton getCorrectButtonForHint() {
        if(bestHandPlayer.getHandType().ordinal() > HandType.HIGH_CARD.ordinal()) return playButton;

        // better than Q,6,4?
        ArrayList<Integer> compVals = new ArrayList<Integer>();
        compVals.add(12);
        compVals.add(6);
        compVals.add(4);
        ArrayList<Integer> vals = bestHandPlayer.getStraightValues();
        for(int i = 0; i<vals.size(); i++) {
            if (vals.get(i) > compVals.get(i)) {
                return playButton;
            } else if (vals.get(i) < compVals.get(i)) {
                return foldButton;
            }
        }
        // everything's equal to the min. bet it.
        return playButton;
    }


    private void calculateWinner(boolean playerFolded) {
        BestHand bestHandDealer = new BestHand(dealerHand.getCards());
        dealerHand.setCards(bestHandDealer.sortedCards);

        // ok, now that we know what they both have, who won and how much?
        int playerWon = bestHandPlayer.getHandType().ordinal() - bestHandDealer.getHandType().ordinal();
        if(playerFolded) {
            playerWon = -1;
        }

        if(playerWon == 0) {
            // run through the top values
            for(int i=0; i<bestHandPlayer.getStraightValues().size(); i++) {
                if(bestHandPlayer.getStraightValues().get(i) > bestHandDealer.getStraightValues().get(i)) {
                    playerWon = 1;
                    break;
                } else if(bestHandPlayer.getStraightValues().get(i) < bestHandDealer.getStraightValues().get(i)) {
                    playerWon = -1;
                    break;
                }
            }
        }

        boolean dealerQualifies = bestHandDealer.getHandType().ordinal() > 0 || bestHandDealer.getStraightValues().get(0) >= 12;

        int total = 0;
        if(playerWon > 0 || (!dealerQualifies && !playerFolded)) {
            // the player really won
            if(dealerQualifies) {
                //If the player has the higher poker hand then the Ante and Play will both pay even money.
                total = anteStack.getTotal() * 2 + playStack.getTotal() * 2;
                anteStack.popStack(true);
                playStack.popStack(true);
            } else {
                // If the dealer does not qualify then the player will win even money on the Ante bet and the Play bet will push.
                total = anteStack.getTotal() * 2 + playStack.getTotal();
                anteStack.popStack(true);
            }
        } else if(playerWon < 0) {
            // the player lost
            total = 0; // we've already pulled this from the balance. no need to re-deduct
            anteStack.popStack(false);
            if(playStack.getTotal() > 0) {
                playStack.popStack(false);
            }
        } else {
            // tie.
            total = anteStack.getTotal() + playStack.getTotal();
        }

        // Ante Bonus
        if(bestHandPlayer.getHandType().ordinal() >= HandType.STRAIGHT.ordinal()) {
            switch(bestHandPlayer.getHandType()) {
                case STRAIGHT_FLUSH:
                    total += anteStack.getTotal() * 5;
                    break;
                case THREE_OF_A_KIND:
                    total += anteStack.getTotal() * 4;
                    break;
                default:
                    total += anteStack.getTotal();
            }
        }

        // Pair Plus
        if(!playerFolded && pairPlusStack.getTotal() > 0 && bestHandPlayer.getHandType().ordinal() >= HandType.PAIR.ordinal()) {
            total += pairPlusStack.getTotal(); // return the initial bet
            pairPlusStack.popStack(true);
            switch(bestHandPlayer.getHandType()) {
                case STRAIGHT_FLUSH:
                    total += pairPlusStack.getTotal() * 40;
                    break;
                case THREE_OF_A_KIND:
                    total += pairPlusStack.getTotal() * 30;
                    break;
                case STRAIGHT:
                    total += pairPlusStack.getTotal() * 6;
                    break;
                case FLUSH:
                    total += pairPlusStack.getTotal() * 3;
                    break;
                default:
                    total += pairPlusStack.getTotal();
                    break;
            }
        } else if(pairPlusStack.getTotal() > 0) {
            pairPlusStack.popStack(false);
        }
        addToBalance(total);

        int initialBet = anteStack.getTotal() + playStack.getTotal() + pairPlusStack.getTotal();
        statistics.Increment(CasinoPracticeStatistics.Wagered, initialBet);

        if(total - initialBet > 0) {
            statistics.Increment(CasinoPracticeStatistics.TimesWon);
            statistics.Increment(CasinoPracticeStatistics.Won, total - initialBet);
            leftSide.setWonColor(Color.GREEN);
        }
        else if(total - initialBet < 0) {
            statistics.Increment(CasinoPracticeStatistics.TimesLost);
            statistics.Increment(CasinoPracticeStatistics.Lost, initialBet);
            leftSide.setWonColor(Color.RED);
        }
        else {
            statistics.Increment(CasinoPracticeStatistics.TimesPushed);
            leftSide.setWonColor(Color.WHITE);
        }

        leftSide.setWonText("" + (total - initialBet));

        setHandText(bestHandDealer.getHandType(), bestHandDealer.getFaceValues().get(0), bestHandPlayer.getHandType(), bestHandPlayer.getFaceValues().get(0), dealerQualifies, playerFolded);

        anteStack.clear();
        pairPlusStack.clear();
        playStack.clear();
    }

    private void setHandText(HandType dealerType, Card.FaceValue dealerHigh, HandType playerType, Card.FaceValue playerHigh, boolean dealerQualified, boolean playerFolded) {
        StringBuilder pb = new StringBuilder();
        StringBuilder db = new StringBuilder();

        if(playerFolded) {
            pb.append("Folded: ");
        } else {
            if(!dealerQualified) {
                db.append("Dealer doesn't qualify: ");
            }
        }

        pb.append(setHandValueText(playerType, playerHigh));
        db.append(setHandValueText(dealerType, dealerHigh));

        dealerHandText.setText(db.toString());
        playerHandText.setText(pb.toString());
    }

    private String setHandValueText(HandType type, Card.FaceValue value) {
        switch(type) {
            case STRAIGHT_FLUSH:
                return "Straight Flush to the " + value.getSingleText();
            case THREE_OF_A_KIND:
                return "Three of a kind of " + value.getMultiText();
            case STRAIGHT:
                return "Straight to the " + value.getSingleText();
            case FLUSH:
                return value.getSingleText() + " high flush";
            case PAIR:
                return "Pair of " + value.getMultiText();
            default:
                return value.getSingleText() + " high";
        }
    }

    enum HandType {HIGH_CARD, PAIR, FLUSH, STRAIGHT, THREE_OF_A_KIND, STRAIGHT_FLUSH};
    class BestHand implements Comparable<BestHand>{
        private HandType handType;
        private ArrayList<Integer> straightValues = new ArrayList<Integer>();
        private ArrayList<Card.FaceValue> faceValues = new ArrayList<Card.FaceValue>();
        private List<Card> sortedCards;

        public BestHand(List<Card> threeCards) {
            sortedCards = new ArrayList<Card>();
            for(Card c : threeCards) {
                sortedCards.add(new Card(c));
            }
            Collections.sort(sortedCards);
            calculateHand(sortedCards);
        }

        void calculateHand(List<Card> cards) {
            boolean straight = isStraight(cards);
            boolean flush = isFlush(cards);
            boolean threeOfAKind = isThreeOfAKind(cards);
            boolean pair = isPair(cards);

            if(straight && flush) {
                handType = HandType.STRAIGHT_FLUSH;
                // gotta move the ace if it's supposed to be low
                if(sortedCards.get(0).getFaceValue().getStraightValue() == 14 && sortedCards.get(1).getFaceValue().getStraightValue() == 3) {
                    Card move = sortedCards.remove(0);
                    sortedCards.add(move);
                }
                faceValues.add(sortedCards.get(0).getFaceValue()); // only the top matters
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(threeOfAKind) {
                handType = HandType.THREE_OF_A_KIND;
                faceValues.add(sortedCards.get(0).getFaceValue()); // only the top matters
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(flush) {
                handType = HandType.FLUSH;
                // all the cards matter
                for(Card c : sortedCards) {
                    faceValues.add(c.getFaceValue());
                    straightValues.add(c.getFaceValue().getStraightValue());
                }
            } else if(straight) {
                handType = HandType.STRAIGHT;
                // gotta move the ace if it's supposed to be low
                if(sortedCards.get(0).getFaceValue().getStraightValue() == 14 && sortedCards.get(1).getFaceValue().getStraightValue() == 3) {
                    Card move = sortedCards.remove(0);
                    sortedCards.add(move);
                }
                faceValues.add(sortedCards.get(0).getFaceValue()); // only the top matters
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(pair) {
                handType = HandType.PAIR;
                // sort the pair to the front
                if(sortedCards.get(1).compareTo(sortedCards.get(2)) == 0) {
                    Card c = sortedCards.remove(0);
                    sortedCards.add(c);
                }
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(2).getFaceValue().getStraightValue());
                faceValues.add(sortedCards.get(0).getFaceValue());
                faceValues.add(sortedCards.get(2).getFaceValue());
            } else {
                handType = HandType.HIGH_CARD;
                // all the cards matter
                for(Card c : sortedCards) {
                    faceValues.add(c.getFaceValue());
                    straightValues.add(c.getFaceValue().getStraightValue());
                }
            }
        }

        boolean isThreeOfAKind(List<Card>cards) {
            int value = cards.get(0).getFaceValue().getStraightValue();
            if(cards.get(0).getFaceValue().getStraightValue() == cards.get(1).getFaceValue().getStraightValue()
                    && cards.get(0).getFaceValue().getStraightValue() == cards.get(2).getFaceValue().getStraightValue()) {
                return true;
            }
            return false;
        }

        boolean isStraight(List<Card> cards) {
            String straightString = "14,13,12,11,10,9,8,7,6,5,4,3,2,";
            String aceLowString = "14,3,2,";
            String cardValues = "";
            for(Card c : cards) {
                cardValues += c.getFaceValue().getStraightValue() + ",";
            }
            return straightString.contains(cardValues) || aceLowString.contains(cardValues);
        }

        boolean isFlush(List<Card> cards) {
            Card.Suit suit = cards.get(0).getSuit();
            for(Card c : cards) {
                if(c.getSuit() != suit) return false;
            }
            return true;
        }

        boolean isPair(List<Card> cards) {
            // sorted, so just check cards next to each other
            if(cards.get(0).getFaceValue().getStraightValue() == cards.get(1).getFaceValue().getStraightValue()
                    || cards.get(1).getFaceValue().getStraightValue() == cards.get(2).getFaceValue().getStraightValue()
                    ) {
                return true;
            }
            return false;
        }

        @Override
        public int compareTo(BestHand other) {
            int returnValue = 0;
            if(handType.ordinal() != other.handType.ordinal()) {
                returnValue =  handType.ordinal() - other.handType.ordinal();
            }

            // if they're the same type, just go to the straight value
            for(int i = 0; i<straightValues.size(); i++) {
                if(returnValue == 0) {
                    returnValue = straightValues.get(i) - other.straightValues.get(i);
                }
            }

            return returnValue;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for(Card c : sortedCards) {
                sb.append(c.getFaceValue().getStraightValue() + ", ");
            }
            sb.append(handType);
            return sb.toString();
        }

        public HandType getHandType() {
            return handType;
        }

        public ArrayList<Integer> getStraightValues() {
            return straightValues;
        }

        public ArrayList<Card.FaceValue> getFaceValues() {
            return faceValues;
        }

        public List<Card> getSortedCards() {
            return sortedCards;
        }
    }

}
