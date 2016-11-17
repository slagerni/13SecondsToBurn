package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStackGroup;
import com.thirteensecondstoburn.CasinoPractice.Actors.Hand;
import com.thirteensecondstoburn.CasinoPractice.Actors.MultiLineTableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;
import com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nick on 4/27/2016.
 */
public class LetItRideScreen extends TableScreen implements ActionCompletedListener {
    public static final float HAND_X_START = (CasinoPracticeGame.SCREEN_WIDTH - Card.CARD_WIDTH * 3) / 2;
    Deck deck;
    boolean isFirstDeck = true;
    boolean canBet = true;

    ChipStackGroup bet1Stack;
    ChipStackGroup bet2Stack;
    ChipStackGroup bet3Stack;

    TableButton dealButton;
    MultiLineTableButton letItRideButton;
    MultiLineTableButton pullItBackButton;
    TableButton clearButton;

    Hand communityHand;
    Hand playerHand;

    Image paytable;
    Image title;

    Text playerHandText;

    int lastBet = 5;

    String letItRideReason = "";

    public LetItRideScreen(CasinoPracticeGame game) {
        super(game);
    }

    @Override
    public void setup() {
        stage.addAction(Actions.alpha(1));

        lastBet = game.getTableMinimum();

        paytable = new Image(assets.getTexture(Assets.TEX_NAME.LET_IT_RIDE_PAYTABLE));
        paytable.setColor(1, 1, 1, .75f);
        paytable.setPosition(stage.getWidth() - paytable.getWidth() - 10, stage.getHeight() - paytable.getHeight() - 10);

        title = new Image(assets.getTexture(Assets.TEX_NAME.LET_IT_RIDE_TITLE));
        title.setColor(Color.WHITE);
        title.setPosition(315, stage.getHeight() - 270);

        bet1Stack = new ChipStackGroup(game, assets, Assets.TEX_NAME.LET_IT_RIDE_CIRCLE_1);
        bet1Stack.setPosition(HAND_X_START + Card.CARD_WIDTH * 3 - Card.CARD_WIDTH / 2, stage.getHeight()/2 - 75);
        bet1Stack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                addToBet();
            }
        });

        bet2Stack = new ChipStackGroup(game, assets, Assets.TEX_NAME.LET_IT_RIDE_CIRCLE_2);
        bet2Stack.setPosition(HAND_X_START + Card.CARD_WIDTH * 2 - Card.CARD_WIDTH / 2, stage.getHeight()/2 - 75);
        bet2Stack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                addToBet();
            }
        });

        bet3Stack = new ChipStackGroup(game, assets, Assets.TEX_NAME.LET_IT_RIDE_CIRCLE_DOLLAR_SIGN);
        bet3Stack.setPosition(HAND_X_START + Card.CARD_WIDTH / 2, stage.getHeight()/2 - 75);
        bet3Stack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                addToBet();
            }
        });

        dealButton = new TableButton(assets, "Deal", mainColor);
        dealButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        dealButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (dealButton.isInside(x, y)) {
                    dealHand();
                    if(game.usePreActionHints()) {
                        setPreBetHint();
                    }
                }
            }
        });
        dealButton.setVisible(true);

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

        letItRideButton = new MultiLineTableButton(assets, "Let|It|Ride", mainColor);
        letItRideButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        letItRideButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (letItRideButton.isInside(x, y) && !canBet) {
                    if(game.useHintText()) {
                        TableButton correctButton = getCorrectButtonForHint();
                        if(correctButton != letItRideButton) {
                            showHint("Pull it back. Strategy conditions not met.");
                        }
                    }
                    play(false);
                }
            }
        });
        letItRideButton.setVisible(false);

        pullItBackButton = new MultiLineTableButton(assets, "Pull|It|Back", mainColor);
        pullItBackButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT + 10);
        pullItBackButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pullItBackButton.isInside(x, y)) {
                    if(game.useHintText()) {
                        TableButton correctButton = getCorrectButtonForHint();
                        if(correctButton != pullItBackButton) {
                            showHint(letItRideReason);
                        }
                    }
                    play(true);
                }
            }
        });
        pullItBackButton.setVisible(false);

        playerHandText = new Text(assets, "", 1.5f);
        playerHandText.setPosition(HAND_X_START, Card.CARD_HEIGHT + 60);

        stage.addActor(paytable);
        stage.addActor(title);

        stage.addActor(bet1Stack);
        stage.addActor(bet2Stack);
        stage.addActor(bet3Stack);

        stage.addActor(dealButton);
        stage.addActor(clearButton);
        stage.addActor(letItRideButton);
        stage.addActor(pullItBackButton);

        stage.addActor(playerHandText);
    }

    @Override
    public void actionCompleted(Actor caller) {
        if(caller == playerHand) {
            communityHand.setVisible(true);
            communityHand.dealAction(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        } else if(caller == communityHand) {
            toggleButtons(true);
        }
    }

    private void calculateWager() {
        leftSide.setWagerText("" + (bet1Stack.getTotal() + bet2Stack.getTotal() + bet3Stack.getTotal()));
    }

    private void addToBet() {
        int betAmount = leftSide.getBetAmount();
        if (bet1Stack.getTotal() * 3 + betAmount * 3 > game.getBalance() - betAmount * 3) {
            showHint("Insufficient funds to make that bet");
            playerHandText.setText("");
            return;
        }
        if (bet1Stack.getTotal() * 3 + betAmount * 3 > game.getTableMaximum()) {
            showHint("Table maximum of " + game.getTableMaximum());
            playerHandText.setText("");
            return;
        }
        bet1Stack.increaseTotal(betAmount);
        bet2Stack.increaseTotal(betAmount);
        bet3Stack.increaseTotal(betAmount);
        lastBet = bet1Stack.getTotal();
        subtractFromBalance(betAmount * 3);
        calculateWager();

        toggleButtons(false);
    }

    private void hideButtons() {
        dealButton.setVisible(false);
        clearButton.setVisible(false);
        letItRideButton.setVisible(false);
        pullItBackButton.setVisible(false);
    }

    private void toggleButtons(boolean isPlaying) {
        dealButton.setVisible(!isPlaying);
        clearButton.setVisible(!isPlaying);
        letItRideButton.setVisible(isPlaying);
        pullItBackButton.setVisible(isPlaying);
    }

    private void dealHand() {
        if (playerHand != null) playerHand.remove();
        if (communityHand != null) communityHand.remove();
        if (bet1Stack.getTotal() == 0) {
            if (lastBet * 3 > game.getBalance()) {
                playerHandText.setText("You can't repeat your last bet");
                return;
            }
            bet1Stack.setTotal(lastBet);
            bet2Stack.setTotal(lastBet);
            bet3Stack.setTotal(lastBet);
            calculateWager();
            subtractFromBalance(lastBet * 3);
        }

        if (bet1Stack.getTotal() < game.getTableMinimum()) {
            showHint("Minimum bet is " + game.getTableMinimum());
            return;
        }

        statistics.increment(CasinoPracticeStatistics.Dealt);

        Card.Back back;
        if (isFirstDeck) {
            back = Card.Back.BACK1;
        } else {
            back = Card.Back.BACK2;
        }
        isFirstDeck = !isFirstDeck;
        deck = new Deck(assets, back);
        deck.shuffle();

        communityHand = new Hand(deck.getCards().subList(0, 2), Card.CARD_WIDTH);
        communityHand.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 50);
        communityHand.addActionListener(this);
        communityHand.setVisible(false);

        playerHand = new Hand(deck.getCards().subList(2, 5), Card.CARD_WIDTH);
        playerHand.setPosition(HAND_X_START, 50);
        playerHand.setVisible(true);
        playerHand.showHand();
        playerHand.dealAction(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        playerHand.addActionListener(this);

        stage.addActor(communityHand);
        stage.addActor(playerHand);

        canBet = false;
        hideButtons(); // cause we're dealing and we'll turn them on when it's done
        leftSide.setWonText("");
        playerHandText.setText("");
    }

    private void clearHand() {
        addToBalance(bet1Stack.getTotal() + bet2Stack.getTotal() + bet3Stack.getTotal());
        lastBet = game.getTableMinimum();
        bet1Stack.setTotal(0);
        bet2Stack.setTotal(0);
        bet3Stack.setTotal(0);
        leftSide.setWonText("");
        leftSide.setWagerText("");
        playerHandText.setText("");
        canBet = true;
        toggleButtons(false);
        clearButton.setVisible(false);
    }

    private void setPreBetHint() {
        letItRideButton.setColor(mainColor);
        pullItBackButton.setColor(mainColor);

        TableButton button = getCorrectButtonForHint();
        button.setColor(hintColor);
    }

    TableButton getCorrectButtonForHint() {
        int communityCardsVisible = countVisibleCommunityCards();
        Hand checkHand = new Hand(playerHand.getCards(), 0);
        checkHand.sort();
        if(communityCardsVisible == 0) {
            int card0 = checkHand.getCards().get(0).getFaceValue().getStraightValue();
            int card1 = checkHand.getCards().get(1).getFaceValue().getStraightValue();
            int card2 = checkHand.getCards().get(2).getFaceValue().getStraightValue();

            // paying hand?
            if((card0 == card1 && card0 == card2)
                    || (card0 == card1 && card0 >= 10)
                    || (card1 == card2 && card1 >= 10)){
                letItRideReason = "Let it Ride: Paying Hand";
                return letItRideButton;
            }

            // everything else needs to include a potential flush
            if(isFlush(checkHand.getCards())){
                // any 3 to a royal flush
                if(card2 >= 10) {
                    letItRideReason = "Let it Ride: Any 3 to a royal flush";
                    return letItRideButton;
                }

                // any 3 in a row except 2-3-4 and A-2-3
                if(card0 - card2 == 2 && card2 >= 3) {
                    letItRideReason = "Let it Ride: 3 to a straight flush with a low card of a 3";
                    return letItRideButton;
                }

                // any 3 to a straight spread 4 with a 10 or greater
                if(card0 - card2 == 3 && card0 >= 10) {
                    letItRideReason = "Let it Ride: Any 3 cards to a straight flush with a spread of 4 and a 10 or greater";
                    return letItRideButton;
                }

                // any 3 to a straight spread 5 with the at least 2 >= 10
                if(card0 - card2 == 4 && card0 >= 10 && card2 >= 10
                        && card0 != card1 && card1 != card2) {
                    letItRideReason = "Let it Ride: Any 3 cards to a straight flush with a spread of 5 and 2 cards 10 or greater";
                    return letItRideButton;
                }
            }
        } else if (communityCardsVisible == 1) {
            checkHand.getCards().add(new Card(communityHand.getCards().get(1)));
            checkHand.sort();
            int card0 = checkHand.getCards().get(0).getFaceValue().getStraightValue();
            int card1 = checkHand.getCards().get(1).getFaceValue().getStraightValue();
            int card2 = checkHand.getCards().get(2).getFaceValue().getStraightValue();
            int card3 = checkHand.getCards().get(3).getFaceValue().getStraightValue();

            // paying hand
            if((card0 == card1 && card0 >= 10) // pair of 10+
                    || (card1 == card2 && card1 >= 10) // pair of 10+
                    || (card2 == card3 && card2 >= 10) // pair of 10+
                    || (card0 == card1 && card2 == card3) // 2 pair
                    || (card0 == card1 && card0 == card2) // 3 of a kind
                    || (card1 == card2 && card1 == card3)){ // 3 of a kind
                letItRideReason = "Let it Ride: Paying Hand";
                return letItRideButton;
            }

            // is flush
            if(isFlush(checkHand.getCards())) {
                letItRideReason = "Let it Ride: Flush potential";
                return letItRideButton;
            }

            // any 4 to an outside straight
            if(card0 - card3 == 3
                    && card0 != card1 && card1 != card2 && card2 != card3) {
                letItRideReason = "Let it Ride: Outside straight draw";
                return letItRideButton;
            }

            // any 4 to an inside straight where the bottom is 10
            if(card0 - card3 == 4 && card3 >= 10
                    && card0 != card1 && card1 != card2 && card2 != card3) {
                letItRideReason = "Let it Ride: Inside straight draw w/ a 10 or higher as the low card";
                return letItRideButton;
            }
        }

        return pullItBackButton;
    }

    private boolean isFlush(List<Card>cards) {
        Card.Suit suit = cards.get(0).getSuit();
        for(Card c : cards) {
            if(c.getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    private int countVisibleCommunityCards() {
        int cardsVisible = 0;
        for(Card card : communityHand.getCards()) {
            if(card.isFaceUp()) {
                cardsVisible++;
            }
        }
        return cardsVisible;
    }

    private void play(boolean pullItBack) {
        int cardsVisible = countVisibleCommunityCards();

        switch (cardsVisible) {
            case 0:
                if(pullItBack) {
                    addToBalance(bet1Stack.getTotal());
                    bet1Stack.clear();
                    calculateWager();
                    toggleButtons(true);
                }
                communityHand.getCards().get(1).setFaceUp(true);
                if(game.usePreActionHints()) {
                    setPreBetHint();
                }
                break;
            case 1:
                if(pullItBack) {
                    addToBalance(bet2Stack.getTotal());
                    bet2Stack.clear();
                    calculateWager();
                }
                communityHand.getCards().get(0).setFaceUp(true);
                calculateWinner();
                toggleButtons(false);
                break;
            default:
                toggleButtons(true);
                break;
        }
    }

    private void calculateWinner() {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(playerHand.getCards());
        allCards.addAll(communityHand.getCards());
        BestHand fullHand = new BestHand(allCards);

        int mulitplier = 0;
        boolean won = true;

        switch (fullHand.getHandType()) {
            case ROYAL_FLUSH:
                mulitplier = 1000;
                break;
            case STRAIGHT_FLUSH:
                mulitplier = 200;
                break;
            case FOUR_OF_A_KIND:
                mulitplier = 50;
                break;
            case FULL_HOUSE:
                mulitplier = 11;
                break;
            case FLUSH:
                mulitplier = 8;
                break;
            case STRAIGHT:
                mulitplier = 5;
                break;
            case THREE_OF_A_KIND:
                mulitplier = 3;
                break;
            case TWO_PAIR:
                mulitplier = 2;
                break;
            case PAIR:
                mulitplier = 1;
                if(fullHand.getStraightValues().get(0) < 10) {
                    won = false;
                }
                break;
            default:
                won = false;
                break;
        }

        if(won) {
            playerHandText.setText(setHandValueText(fullHand));
        } else {
            playerHandText.setText("No Hand");
        }

        long initialBet = bet1Stack.getTotal() + bet2Stack.getTotal() + bet3Stack.getTotal(); // original bets still out
        long total = 0;
        if(won) {
            total = initialBet + initialBet * mulitplier;

            if(bet1Stack.getTotal() > 0) {
                bet1Stack.popStack(true);
                bet1Stack.clear();
            }
            if(bet2Stack.getTotal() > 0) {
                bet2Stack.popStack(true);
                bet2Stack.clear();
            }
            if(bet3Stack.getTotal() > 0) {
                bet3Stack.popStack(true);
                bet3Stack.clear();
            }
        } else {
            if(bet1Stack.getTotal() > 0) {
                bet1Stack.popStack(false);
                bet1Stack.clear();
            }
            if(bet2Stack.getTotal() > 0) {
                bet2Stack.popStack(false);
                bet2Stack.clear();
            }
            if(bet3Stack.getTotal() > 0) {
                bet3Stack.popStack(false);
                bet3Stack.clear();
            }
        }

        addToBalance(total);
        statistics.increment(CasinoPracticeStatistics.Wagered, initialBet);

        if(total - initialBet > 0) {
            statistics.increment(CasinoPracticeStatistics.TimesWon);
            statistics.increment(CasinoPracticeStatistics.Won, total - initialBet);
            statistics.checkMaximumWon(total - initialBet);
            leftSide.setWonColor(Color.GREEN);
        }
        else if(total - initialBet < 0) {
            statistics.increment(CasinoPracticeStatistics.TimesLost);
            statistics.increment(CasinoPracticeStatistics.Lost, initialBet);
            statistics.checkMaximumLost(total - initialBet);
            leftSide.setWonColor(Color.RED);
        }
        else {
            statistics.increment(CasinoPracticeStatistics.TimesPushed);
            leftSide.setWonColor(Color.WHITE);
        }
        statistics.updateReturnPerHand();

        leftSide.setWonText("" + (total - initialBet));

    }

    private String setHandValueText(BestHand hand) {
        Card.FaceValue firstFaceValue = hand.getFaceValues().get(0);
        switch(hand.getHandType()) {
            case ROYAL_FLUSH:
                return "Royal flush in " + hand.getSortedCards().get(0).getSuit();
            case STRAIGHT_FLUSH:
                return "Straight Flush to the " + firstFaceValue.getSingleText();
            case FOUR_OF_A_KIND:
                return "Four of a kind of " + firstFaceValue.getSingleText();
            case FULL_HOUSE:
                return "Full house " + firstFaceValue.getMultiText() + " over " + hand.getFaceValues().get(1).getMultiText();
            case FLUSH:
                return firstFaceValue.getSingleText() + " high flush";
            case STRAIGHT:
                return "Straight to the " + firstFaceValue.getSingleText();
            case THREE_OF_A_KIND:
                return "Three of a kind of " + firstFaceValue.getMultiText();
            case TWO_PAIR:
                return "Two pair, " + firstFaceValue.getMultiText() + " and " + hand.getFaceValues().get(1).getMultiText();
            case PAIR:
                return "Pair of " + firstFaceValue.getMultiText();
            default:
                return firstFaceValue.getSingleText() + " high";
        }
    }

    enum HandType {HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH};
    class BestHand implements Comparable<BestHand>{
        private HandType handType;
        private ArrayList<Integer> straightValues = new ArrayList<Integer>();
        private ArrayList<Card.FaceValue> faceValues = new ArrayList<Card.FaceValue>();
        private List<Card> sortedCards;

        public BestHand(List<Card> cards) {
            sortedCards = new ArrayList<Card>();
            for(Card c : cards) {
                sortedCards.add(new Card(c));
            }
            Collections.sort(sortedCards);
            calculateHand(sortedCards);
        }

        void calculateHand(List<Card> cards) {
            boolean pair = isPair(cards);
            boolean twoPair = isTwoPair(cards);
            boolean threeOfAKind = isThreeOfAKind(cards);
            boolean straight = isStraight(cards);
            boolean flush = isFlush(cards);
            boolean fullHouse = isFullHouse(cards);
            boolean fourOfAKind = isFourOfAKind(cards);

            if(straight && flush && sortedCards.get(0).getFaceValue().getStraightValue() == 14) {
                handType = HandType.ROYAL_FLUSH;
                faceValues.add(sortedCards.get(0).getFaceValue()); // only the top matters
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(straight && flush) {
                handType = HandType.STRAIGHT_FLUSH;
                // gotta move the ace if it's supposed to be low
                if (sortedCards.get(0).getFaceValue().getStraightValue() == 14 && sortedCards.get(1).getFaceValue().getStraightValue() == 5) {
                    Card move = sortedCards.remove(0);
                    sortedCards.add(move);
                }
                faceValues.add(sortedCards.get(0).getFaceValue()); // only the top matters
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(fourOfAKind) {
                handType = HandType.FOUR_OF_A_KIND;
                // if the 4 low are matching, move the odd card to the end
                if (sortedCards.get(0) != sortedCards.get(1)) {
                    Card move = sortedCards.remove(0);
                    sortedCards.add(move);
                }
                faceValues.add(sortedCards.get(0).getFaceValue()); // only need 1 since no one else can have the same 4
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(fullHouse) {
                handType = HandType.FULL_HOUSE;
                // make sure the 3 are in front
                int val1 = cards.get(1).getFaceValue().getStraightValue();
                int val2 = cards.get(2).getFaceValue().getStraightValue();
                if(val1 != val2) {
                    // pair at the front. shuffle them to the end
                    Card c = sortedCards.remove(0);
                    sortedCards.add(c);
                    c = sortedCards.remove(0);
                    sortedCards.add(c);
                }
                faceValues.add(sortedCards.get(0).getFaceValue());
                faceValues.add(sortedCards.get(2).getFaceValue());
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(2).getFaceValue().getStraightValue());

            } else if(threeOfAKind) {
                handType = HandType.THREE_OF_A_KIND;
                int val0 = sortedCards.get(0).getFaceValue().getStraightValue();
                int val1 = sortedCards.get(1).getFaceValue().getStraightValue();
                int val2 = sortedCards.get(2).getFaceValue().getStraightValue();

                if(val0 != val1 && val1 != val2) {
                    // top 2 need to move to the end
                    Card single = sortedCards.remove(0);
                    sortedCards.add(single);
                    single = sortedCards.remove(0);
                    sortedCards.add(single);
                } else if(val0 != val1 && val1 == val2) {
                    Card single = sortedCards.remove(0);
                    sortedCards.add(3, single);
                }
                // first is the 3 of a kind, others don't matter since no one else can have the same 3
                faceValues.add(sortedCards.get(0).getFaceValue());
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
                if(sortedCards.get(0).getFaceValue().getStraightValue() == 14 && sortedCards.get(1).getFaceValue().getStraightValue() == 5) {
                    Card move = sortedCards.remove(0);
                    sortedCards.add(move);
                }
                faceValues.add(sortedCards.get(0).getFaceValue()); // only the top matters
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
            } else if(twoPair) {
                handType = HandType.TWO_PAIR;
                int val0 = sortedCards.get(0).getFaceValue().getStraightValue();
                int val1 = sortedCards.get(1).getFaceValue().getStraightValue();
                int val2 = sortedCards.get(2).getFaceValue().getStraightValue();
                int val3 = sortedCards.get(3).getFaceValue().getStraightValue();
                if(val0 != val1) {
                    // we now know that the odd card is in the first position. move it to the back
                    Card c = sortedCards.remove(0);
                    sortedCards.add(c);
                } else if(val2 != val3) {
                    // we now know that the odd card is in the middle. move it to the back
                    Card c = sortedCards.remove(2);
                    sortedCards.add(c);
                } // otherwise it's sorted fine

                // sorted, so high pair is first
                faceValues.add(sortedCards.get(0).getFaceValue());
                faceValues.add(sortedCards.get(2).getFaceValue());
                faceValues.add(sortedCards.get(4).getFaceValue());
                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(2).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(4).getFaceValue().getStraightValue());

            } else if(pair) {
                handType = HandType.PAIR;
                int val0 = sortedCards.get(0).getFaceValue().getStraightValue();
                int val1 = sortedCards.get(1).getFaceValue().getStraightValue();
                int val2 = sortedCards.get(2).getFaceValue().getStraightValue();
                int val3 = sortedCards.get(3).getFaceValue().getStraightValue();
                int val4 = sortedCards.get(4).getFaceValue().getStraightValue();
                Card c1 = null, c2 = null;
                // sort the pair to the front
                if(val1 == val2) {
                    c1 = sortedCards.remove(1);
                    c2 = sortedCards.remove(1);
                } else if(val2 == val3) {
                    c1 = sortedCards.remove(2);
                    c2 = sortedCards.remove(2);
                } else if(val3 == val4) {
                    c1 = sortedCards.remove(3);
                    c2 = sortedCards.remove(3);
                }
                if(sortedCards.size() == 3) {
                    sortedCards.add(0, c1);
                    sortedCards.add(0, c2);
                }

                straightValues.add(sortedCards.get(0).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(2).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(3).getFaceValue().getStraightValue());
                straightValues.add(sortedCards.get(4).getFaceValue().getStraightValue());

                faceValues.add(sortedCards.get(0).getFaceValue());
                faceValues.add(sortedCards.get(2).getFaceValue());
                faceValues.add(sortedCards.get(3).getFaceValue());
                faceValues.add(sortedCards.get(4).getFaceValue());
            } else {
                handType = HandType.HIGH_CARD;
                // all the cards matter
                for(Card c : sortedCards) {
                    faceValues.add(c.getFaceValue());
                    straightValues.add(c.getFaceValue().getStraightValue());
                }
            }
        }

        boolean isFourOfAKind(List<Card>cards) {
            int val0 = sortedCards.get(0).getFaceValue().getStraightValue();
            int val1 = sortedCards.get(1).getFaceValue().getStraightValue();
            int val2 = sortedCards.get(2).getFaceValue().getStraightValue();
            int val3 = sortedCards.get(3).getFaceValue().getStraightValue();
            int val4 = sortedCards.get(4).getFaceValue().getStraightValue();
            // sorted, so either the first 4 are the same or the last 4
            if ((val0 == val1 && val0 == val2 && val0 == val3) || (val4 == val1 && val4 == val2 && val4 == val3)) {
                return true;
            }
            return false;
        }

        boolean isFullHouse(List<Card>cards) {
            int val0 = sortedCards.get(0).getFaceValue().getStraightValue();
            int val1 = sortedCards.get(1).getFaceValue().getStraightValue();
            int val2 = sortedCards.get(2).getFaceValue().getStraightValue();
            int val3 = sortedCards.get(3).getFaceValue().getStraightValue();
            int val4 = sortedCards.get(4).getFaceValue().getStraightValue();
            // sorted, so first 3 equal and last 2 or first 2 equal and last 3
            if((val0 == val1 && val0 == val2 && val3 == val4) || (val0 == val1 && val2 == val3 && val2 == val4)){
                return true;
            }
            return false;
        }

        boolean isThreeOfAKind(List<Card>cards) {
            // sorted, so either the first 3, middle 3 or the last 3
            int val0 = sortedCards.get(0).getFaceValue().getStraightValue();
            int val1 = sortedCards.get(1).getFaceValue().getStraightValue();
            int val2 = sortedCards.get(2).getFaceValue().getStraightValue();
            int val3 = sortedCards.get(3).getFaceValue().getStraightValue();
            int val4 = sortedCards.get(4).getFaceValue().getStraightValue();
            if((val0 == val1 && val0 == val2) || (val1 == val2 && val1 == val3) || (val2 == val3 && val2 == val4)) {
                return true;
            }
            return false;
        }

        boolean isStraight(List<Card> cards) {
            String straightString = "14,13,12,11,10,9,8,7,6,5,4,3,2,";
            String aceLowString = "14,5,4,3,2,";
            String cardValues = "";
            for(Card c : sortedCards) {
                cardValues += c.getFaceValue().getStraightValue() + ",";
            }
            return straightString.contains(cardValues) || aceLowString.contains(cardValues);
        }

        boolean isFlush(List<Card> cards) {
            Card.Suit suit = sortedCards.get(0).getSuit();
            for(Card c : sortedCards) {
                if(c.getSuit() != suit) return false;
            }
            return true;
        }

        boolean isTwoPair(List<Card> cards) {
            // sorted, so two sets in a row should match
            int count = 0;
            for(int i=0; i<cards.size() -1; i++) {
                if(sortedCards.get(i).getFaceValue().getStraightValue() == sortedCards.get(i+1).getFaceValue().getStraightValue())
                    count++;
            }

            return count == 2;
        }

        boolean isPair(List<Card> cards) {
            // sorted, so just check cards next to each other
            int val0 = cards.get(0).getFaceValue().getStraightValue();
            int val1 = cards.get(1).getFaceValue().getStraightValue();
            int val2 = cards.get(2).getFaceValue().getStraightValue();
            int val3 = cards.get(3).getFaceValue().getStraightValue();
            int val4 = cards.get(4).getFaceValue().getStraightValue();

            if(val0 == val1 || val1== val2 || val2 == val3 || val3 == val4) {
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
