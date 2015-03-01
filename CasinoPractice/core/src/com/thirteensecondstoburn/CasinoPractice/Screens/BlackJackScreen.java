package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.BlackjackHand;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStack;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Nick on 2/20/2015.
 */
public class BlackJackScreen extends TableScreen implements ActionCompletedListener {

    public BlackJackScreen(CasinoPracticeGame game) {
        super(game);
    }
    public static final float HAND_X_START = (CasinoPracticeGame.SCREEN_WIDTH - Card.CARD_WIDTH * 3) / 2;

    Deck deck;
    boolean canBet = true;
    Image title;
    int lastBet = ChipStack.TABLE_MIN;

    BlackjackHand dealerHand;

    int cardIndex = 0;
    int currentHandIndex = 0;
    BlackjackHand currentHand;

    Text blackjackPayText;
    Text dealerHandText;

    TableButton dealButton;
    TableButton hitButton;
    TableButton standButton;
    TableButton surrenderButton;
    TableButton doubleButton;
    TableButton clearButton;

    BlackjackHand mainHand;

    List<BlackjackHand> hands;


    @Override
    protected void setup() {
        Texture titleTex = assets.getTexture(Assets.TEX_NAME.BLACKJACK_TITLE);
        title = new Image(titleTex);
        title.setColor(mainColor);
        title.setPosition(315, stage.getHeight() - titleTex.getHeight());


        dealerHandText = new Text(assets, "", 1.5f);
        dealerHandText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 90);

        blackjackPayText = new Text(assets, "BLACKJACK PAYS 3 TO 2 - DEALER HITS ON SOFT 17", 1);
        blackjackPayText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - 40);
        blackjackPayText.setColor(.5f, .5f, .5f, .5f);

        mainHand = new BlackjackHand(game, assets);
        mainHand.addActionListener(this);
        mainHand.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (canBet) {
                    int betAmount = leftSide.getBetAmount();
                    if (mainHand.getBetTotal() + leftSide.getBetAmount() > game.getBalance() - betAmount) {
                        mainHand.setHandText("Insufficient funds to make that bet");
                        dealerHandText.setText("");
                        return;
                    }
                    if (mainHand.getBetTotal() + betAmount > ChipStack.TABLE_MAX) {
                        mainHand.setHandText("Table maximum of " + ChipStack.TABLE_MAX);
                        dealerHandText.setText("");
                        return;
                    }
                    mainHand.setBetTotal(mainHand.getBetTotal() + leftSide.getBetAmount());
                    lastBet = mainHand.getBetTotal();
                    subtractFromBalance(betAmount);
                    calculateWager();
                } else {
                    if (mainHand.canSplit()) {
                        splitHand(mainHand);
                    }
                }
            }
        });

        hands = new ArrayList<BlackjackHand>();
        hands.add(mainHand);
        setHandPositions();

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
        clearButton.setVisible(true);

        hitButton = new TableButton(assets, "Hit", mainColor);
        hitButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        hitButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (hitButton.isInside(x, y)) {
                    hitHand();
                }
            }
        });
        hitButton.setVisible(false);

        standButton = new TableButton(assets, "Stand", mainColor);
        standButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT + 10);
        standButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (standButton.isInside(x, y)) {
                    stand();
                }
            }
        });
        standButton.setVisible(false);

        doubleButton = new TableButton(assets, "Double", mainColor);
        doubleButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT *2 + 20);
        doubleButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (doubleButton.isInside(x, y)) {
                    doubleDown();
                }
            }
        });
        doubleButton.setVisible(false);

        surrenderButton = new TableButton(assets, "Surrender", mainColor);
        surrenderButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, TableButton.BUTTON_HEIGHT *3 + 30);
        surrenderButton.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (surrenderButton.isInside(x, y)) {
                    surrender();
                }
            }
        });
        surrenderButton.setVisible(false);
        surrenderButton.setTextScale(1.5f);

        stage.addActor(title);

        stage.addActor(dealerHandText);
        stage.addActor(blackjackPayText);

        stage.addActor(dealButton);
        stage.addActor(clearButton);
        stage.addActor(hitButton);
        stage.addActor(standButton);
        stage.addActor(doubleButton);
        stage.addActor(surrenderButton);

        stage.addActor(mainHand);
    }

    private void dealHand() {
        if(dealerHand != null) dealerHand.remove();
        mainHand.clear();
        currentHand = mainHand;
        currentHandIndex = 0;
        for(BlackjackHand hand : hands) {
            hand.remove();
        }
        hands.clear();
        hands.add(mainHand);
        stage.addActor(mainHand);
        setHandPositions();

        if(mainHand.getBetTotal() == 0) {
            if(lastBet > game.getBalance()) {
                mainHand.setHandText("You can't repeat your last bet");
                dealerHandText.setText("");
                return;
            }
            mainHand.setBetTotal(lastBet);
            leftSide.setWagerText("" + (lastBet));
            subtractFromBalance(lastBet);
        }

        if(mainHand.getBetTotal() < ChipStack.TABLE_MIN) {
            mainHand.setHandText("Minimum bet is " + ChipStack.TABLE_MIN);
            return;
        }

        Card.Back back;
        back = Card.Back.BACK1;
        deck = new Deck(assets, back);
        deck.shuffle();

        cardIndex = 0;
        dealerHand = new BlackjackHand(game, assets);
        dealerHand.addCard(nextCard(), false);
        dealerHand.addCard(nextCard());
        dealerHand.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 80);
        dealerHand.deal(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        dealerHand.addActionListener(this);
        dealerHand.hideBets();

        mainHand.addCard(nextCard());
        mainHand.addCard(nextCard());
        currentHand = mainHand;
        currentHandIndex = 0;

        stage.addActor(dealerHand);
        canBet = false;
        hideButtons(); // cause we're dealing and we'll turn them on when it's done
        leftSide.setWonText("");
        dealerHandText.setText("");
    }

    private void hideButtons() {
        dealButton.setVisible(false);
        clearButton.setVisible(false);
        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleButton.setVisible(false);
        surrenderButton.setVisible(false);
    }

    private void clearHand() {
        for(BlackjackHand hand : hands) {
            addToBalance(hand.getBetTotal());
            hand.setHandText("");
        }
        lastBet = ChipStack.TABLE_MIN;
        leftSide.setWonText("");
        dealerHand.setVisible(false);
        leftSide.setWagerText("");
        canBet = true;
        toggleButtons(false);
        mainHand.clear();
        currentHand = mainHand;
        currentHandIndex = 0;

        for(BlackjackHand hand : hands) {
            hand.remove();
        }
        hands.clear();
        hands.add(mainHand);
        stage.addActor(mainHand);
        setHandPositions();
    }

    private void toggleButtons(boolean isPlaying) {
        dealButton.setVisible(!isPlaying);
        clearButton.setVisible(!isPlaying);
        hitButton.setVisible(isPlaying);
        standButton.setVisible(isPlaying);
        doubleButton.setVisible(isPlaying);
        surrenderButton.setVisible(isPlaying);
    }

    private void setHandPositions() {
        float totalSpace = stage.getWidth() - leftSide.getWidth();
        int totalHands = hands.size();
        if(totalHands == 1) {
            hands.get(0).setPosition(totalSpace / 2 - 150, 50);
        } else if (totalHands == 2) {
            hands.get(0).setPosition(leftSide.getWidth() + 5, 50);
            hands.get(1).setPosition(totalSpace / 2 + 50, 50);
        } else if (totalHands == 3) {
            hands.get(0).setPosition(leftSide.getWidth() + 5, 10);
            hands.get(1).setPosition(totalSpace / 2 + 50, 10);
            hands.get(2).setPosition(leftSide.getWidth() + 5, 50 + Card.CARD_HEIGHT);
        } else if (totalHands == 4) {
            hands.get(0).setPosition(leftSide.getWidth() + 5, 10);
            hands.get(1).setPosition(totalSpace / 2 + 50, 10);
            hands.get(2).setPosition(leftSide.getWidth() + 5, 50 + Card.CARD_HEIGHT);
            hands.get(3).setPosition(totalSpace / 2 + 50, 50 + Card.CARD_HEIGHT);
        }
    }

    private void stand() {
        BlackjackHand nextHand = getNextHand();
        if(nextHand == null) {
            // all betting is done, let the dealer go
            playDealer();
            return;
        } else {
            // move to the next split hand
            currentHand = nextHand;
            if(currentHand != null) {
                doubleButton.setVisible(true);
                surrenderButton.setVisible(true);
            }
        }
    }

    private void hitHand() {
        doubleButton.setVisible(false); // can only double the first card
        surrenderButton.setVisible(false); // same w/ surrendering
        currentHand.addCard(nextCard());
        int hard = currentHand.getHard();
        int soft = currentHand.getSoft();
        if(soft > 21 ) {
            BlackjackHand nextHand = getNextHand();
            if(nextHand == null) {
                // all betting is done, let the dealer go
                playDealer();
                return;
            } else {
                // move to the next split hand
                currentHand = nextHand;
                if(currentHand != null) {
                    doubleButton.setVisible(true);
                    surrenderButton.setVisible(true);
                }
            }
        }

    }

    private void doubleDown() {
        if(currentHand.getBetTotal() > game.getBalance()) {
            currentHand.setHandText("You don't have enough funds to double down");
            return;
        }
        Card card = nextCard();
        card.setRotation(90);
        currentHand.addCard(card);
        currentHand.setBetTotal(currentHand.getBetTotal() * 2);
        calculateWager();

        // if you double, that's the last you get
        BlackjackHand nextHand = getNextHand();
        if(nextHand == null) {
            // all betting is done, let the dealer go
            playDealer();
            return;
        } else {
            // move to the next split hand
            currentHand = nextHand;
        }
    }

    private void surrender() {
        currentHand.surrender();

        BlackjackHand nextHand = getNextHand();
        if(nextHand == null) {
            // all betting is done, let the dealer go
            playDealer();
            return;
        } else {
            // move to the next split hand
            currentHand = nextHand;
        }
    }

    private void splitHand(BlackjackHand hand) {
        if(hands.size() == 4) {
            hand.setHandText("You can't split more than four times");
            return;
        }

        if(hand.getBetTotal() > game.getBalance()) {
            hand.setHandText("You don't have enough funds to split");
            return;
        }

        Card splitCard = hand.splitHand();
        // first add a new card to the hand just split
        hand.addCard(nextCard());

        // now add a new blackjack hand
        final BlackjackHand newHand = new BlackjackHand(game, assets, true);
        newHand.addCard(splitCard);
        newHand.addCard(nextCard());
        newHand.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(newHand.canSplit()) {
                    splitHand(newHand);
                }
            }
        });
        newHand.setBetTotal(hand.getBetTotal());
        newHand.setColor(Color.DARK_GRAY);
        subtractFromBalance(hand.getBetTotal());

        stage.addActor(newHand);
        hands.add(newHand);

        calculateWager();

        // we have a new hand, move them around
        setHandPositions();
    }

    private BlackjackHand getNextHand() {
        if(currentHandIndex >= hands.size() - 1) {
            for(BlackjackHand hand : hands) {
                hand.setColor(Color.WHITE);
            }
            return null;
        }

        BlackjackHand nextHand = hands.get(++currentHandIndex);
        for(BlackjackHand hand : hands) {
            hand.setColor(Color.DARK_GRAY);
        }
        nextHand.setColor(Color.WHITE);
        return nextHand;
    }

    private Card nextCard() {
        return deck.getCards().get(cardIndex++);
    }

    private void playDealer() {
        hideButtons();
        dealerHand.showCards();
        boolean nonBustHand = false;
        for(BlackjackHand hand : hands) {
            if(hand.bestHandTotal() != -1) nonBustHand = true;
        }
        if((dealerHand.getHard() <= 16 || (dealerHand.getHard() > 21 && dealerHand.getSoft() <= 17)) && !mainHand.isBlackjack() && nonBustHand) {
            hitDealer();
        } else {
            calculateWinners();
        }
    }

    private void hitDealer() {
        stage.addAction(
                sequence(
                        Actions.delay(.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                dealerHand.addCard(nextCard());
                            }
                        }),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                playDealer();
                            }
                        })
                ));
    }

    private void calculateWinners() {
        int total = 0;
        int initialBet = 0;

        if(dealerHand.isBlackjack()) {
            // special case, if the dealer has blackjack the only thing the player could have done is tie
            if(mainHand.isBlackjack()) {
                total = mainHand.getBetTotal();
            } else {
                mainHand.popStack(false);
            }
            initialBet += mainHand.getBetTotal();
        } else {
            for(BlackjackHand hand : hands) {
                initialBet += hand.getBetTotal();
                if (hand == mainHand && hand.isBlackjack()) {
                    // pay out 3-2 for the blackjack
                    total += hand.getBetTotal() + hand.getBetTotal() * 3 / 2;
                    hand.popStack(true);
                } else if(hand.surrendered()) {
                    // give back 1/2 but it's still a loss
                    total += hand.getBetTotal() / 2;
                    hand.popStack(false);
                } else if(hand.bestHandTotal() > dealerHand.bestHandTotal()) {
                    // play won even money.
                    total += hand.getBetTotal() * 2;
                    hand.popStack(true);
                } else if(hand.bestHandTotal() != -1 && hand.bestHandTotal() == dealerHand.bestHandTotal()) {
                    // push
                    total += hand.getBetTotal();
                } else {
                    // hand busted or lost to the dealer
                    hand.popStack(false);
                }
                hand.setBetTotal(0);
            }
        }

        System.out.println("Initial bet: " + initialBet + " Total: " + total);

        addToBalance(total);

        if(total - initialBet > 0)
            leftSide.setWonColor(Color.GREEN);
        else if(total - initialBet < 0)
            leftSide.setWonColor(Color.RED);
        else
            leftSide.setWonColor(Color.WHITE);

        leftSide.setWonText("" + (total - initialBet));

        canBet = true;
        toggleButtons(false);
    }

    private void calculateWager() {
        int wager = 0;
        for(BlackjackHand hand : hands) {
            wager += hand.getBetTotal();
            System.out.println("Wager: " + wager);
        }
        leftSide.setWagerText("" + wager);
    }


    @Override
    public void actionCompleted(Actor caller) {
        if(caller == dealerHand) {
            mainHand.setVisible(true);
            mainHand.deal(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        } else if(caller.getName() == "InnerHand") {
            toggleButtons(true);
            if(dealerHand.isBlackjack() || mainHand.isBlackjack()) {
                playDealer();
                return;
            }
            if(game.usePreBetHints()) {
                // TODO HINTS
                //setPreBetHint();
            }
        }
    }
}
