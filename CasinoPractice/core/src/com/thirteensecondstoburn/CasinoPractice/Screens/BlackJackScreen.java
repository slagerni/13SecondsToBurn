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
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;
import com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics;

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
    int lastBet =5;

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

    Strategy strategy;
    int postHintTotal;
    int postHintDealer;
    boolean postHintIsSoft;
    BjAction postHintBjAction;

    BlackjackHand mainHand;
    Card.Back back;

    List<BlackjackHand> hands;


    @Override
    protected void setup() {
        lastBet = game.getTableMinimum();

        Texture titleTex = assets.getTexture(Assets.TEX_NAME.BLACKJACK_TITLE);
        title = new Image(titleTex);
        title.setColor(mainColor);
        title.setPosition(315, stage.getHeight() - titleTex.getHeight());

        strategy = new Strategy(game.getBlackjackDecks());

        dealerHandText = new Text(assets, "", 1.5f);
        dealerHandText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 90);

        if(game.getBlackjackHitSoft17()) {
            blackjackPayText = new Text(assets, "BLACKJACK PAYS 3 TO 2 - DEALER HITS ON SOFT 17", 1);
        } else {
            blackjackPayText = new Text(assets, "BLACKJACK PAYS 3 TO 2 - DEALER MUST STAND ON ALL 17’S", 1);
        }
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
                        showHint("Insufficient funds to make that bet");
                        mainHand.setHandText("");
                        dealerHandText.setText("");
                        return;
                    }
                    if (mainHand.getBetTotal() + betAmount >  game.getTableMaximum()) {
                        showHint("Table maximum of " +  game.getTableMaximum());
                        mainHand.setHandText("");
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

        if(mainHand.getBetTotal() < game.getTableMinimum()) {
            showHint("Minimum bet is " + game.getTableMinimum());
            return;
        }

        statistics.increment(CasinoPracticeStatistics.Dealt);

        if(deck == null || cardIndex > deck.getTotalCards() * game.getBlackjackPenetration()) {
            showHint("Cards Shuffled");
            if(back == null || back == Card.Back.BACK2) {
                back = Card.Back.BACK1;
            } else {
                back = Card.Back.BACK2;
            }
            deck = new Deck(assets, back, game.getBlackjackDecks(), 0);
            deck.shuffle();
            cardIndex = 0;
        }

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
        lastBet = game.getTableMinimum();
        leftSide.setWonText("");
        if(dealerHand != null) {
            dealerHand.setVisible(false);
        }
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
        showPostActionHint(BjAction.STAND);

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
            setButtonHints();
        }
    }

    private void hitHand() {
        showPostActionHint(BjAction.HIT);

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
        setButtonHints();

    }

    private void doubleDown() {
        if(currentHand.getBetTotal() > game.getBalance()) {
            currentHand.setHandText("You don't have enough funds to double down");
            return;
        }
        showPostActionHint(BjAction.DOUBLE);

        Card card = nextCard();
        card.setRotation(90);
        currentHand.addCard(card);
        subtractFromBalance(currentHand.getBetTotal());
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
            setButtonHints();
        }
    }

    private void surrender() {
        currentHand.surrender();
        showPostActionHint(BjAction.SURRENDER);

        BlackjackHand nextHand = getNextHand();
        if(nextHand == null) {
            // all betting is done, let the dealer go
            playDealer();
            return;
        } else {
            // move to the next split hand
            currentHand = nextHand;
            setButtonHints();
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

        showPostActionHint(BjAction.SPLIT);

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
                if (newHand.canSplit() && !dealButton.isVisible()) {
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
        setButtonHints();
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
        float total = 0;
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
                    total += hand.getBetTotal() + hand.getBetTotal() * 3 / 2f;
                    hand.popStack(true);
                } else if(hand.surrendered()) {
                    // give back 1/2 but it's still a loss
                    total += hand.getBetTotal() / 2f;
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

        if(total != (int)total) {
            leftSide.setWonText(String.format("%.2f", total - initialBet));
        } else {
            leftSide.setWonText(String.format("%.0f", total - initialBet));
        }

        canBet = true;
        toggleButtons(false);
    }

    private void calculateWager() {
        int wager = 0;
        for(BlackjackHand hand : hands) {
            wager += hand.getBetTotal();
        }
        leftSide.setWagerText("" + wager);
    }

    private void setButtonHints() {
        hitButton.setColor(mainColor);
        standButton.setColor(mainColor);
        doubleButton.setColor(mainColor);
        surrenderButton.setColor(mainColor);
        currentHand.setColor(Color.WHITE);

        // post hints can use some of this goodness
        postHintDealer = dealerHand.getHard();
        postHintIsSoft = false;
        postHintTotal = currentHand.getHard();

        if(currentHand.getHard() != currentHand.getSoft() && currentHand.getHard() <= 21) {
            postHintIsSoft = true;
        } else if(currentHand.getHard() > 21) {
            postHintTotal = currentHand.getSoft();
        }

        postHintBjAction = strategy.getAction(currentHand, dealerHand.getHard(), doubleButton.isVisible(), surrenderButton.isVisible());

        if(!game.usePreActionHints()) return; // only if it's actually turned on
        switch (postHintBjAction) {
            case HIT:
                hitButton.setColor(hintColor);
                break;
            case STAND:
                standButton.setColor(hintColor);
                break;
            case DOUBLE:
                doubleButton.setColor(hintColor);
                break;
            case SURRENDER:
                surrenderButton.setColor(hintColor);
                break;
            case SPLIT:
                currentHand.setColor(hintColor);
                break;
        }
    }

    public void showPostActionHint(BjAction taken) {
        if(game.useHintText() && taken != postHintBjAction) {
            String soft = "";
            if(postHintIsSoft) soft = " soft ";
            switch (postHintBjAction) {
                case HIT:
                    showHint(String.format("Hit%s %d vs dealer %d", soft, postHintTotal, postHintDealer));
                    break;
                case SPLIT:
                    showHint(String.format("Split%s %d vs dealer %d", soft, postHintTotal, postHintDealer));
                    break;
                case DOUBLE:
                    showHint(String.format("Double%s %d vs dealer %d when allowed", soft, postHintTotal, postHintDealer));
                    break;
                case SURRENDER:
                    showHint(String.format("Surrender%s %d vs dealer %d when allowed", soft, postHintTotal, postHintDealer));
                    break;
                case STAND:
                    showHint(String.format("Stand%s %d vs dealer %d", soft, postHintTotal, postHintDealer));
                    break;
            }
        }
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
            setButtonHints();
        }
    }

    public enum BjAction {

        HIT("H"), STAND("S"), SPLIT("P"), DOUBLE("D"),
        SURRENDER ("R"), DOUBLE_HIT("DH"), DOUBLE_STAND("DS"),
        SURRENDER_HIT("RH"), SURRENDER_STAND("RS"), SURRENDER_SPLIT("RP");

        private final String name;
        BjAction(String name) {
            this.name = name;
        }

        @Override
        public String toString() { return name; }
    }

    private class Strategy {

        private BjAction[][] hard = new BjAction[17][10];
        private BjAction[][] soft = new BjAction[9][10];
        private BjAction[][] split = new BjAction[10][10];

        public Strategy(int decks) {
            int dealer = 0;
            int player = 0;

            if(!game.getBlackjackHitSoft17()) {
                //<editor-fold desc="Stand on Soft 17">
                if (decks == 1) {
                    // 5
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 9
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 10
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 11
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer] = BjAction.DOUBLE_HIT;
                    dealer = 0;
                    player++;
                    // 12
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 13
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 17
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 18
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 19
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 13
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 17
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 18
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 19
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 2,2
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 3,3
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 4,4
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 5,5
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6,6
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7,7
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.SURRENDER_STAND;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8,8
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                    dealer = 0;
                    player++;
                    // 9,9
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 10,10
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // A,A
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                } else if (decks > 1 && decks < 4) {
                    // 5
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 9
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 10
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 11
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer] = BjAction.DOUBLE_HIT;
                    dealer = 0;
                    player++;
                    // 12
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 13
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 17
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 18
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 19
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 13
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 17
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 18
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 19
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 2,2
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 3,3
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 4,4
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 5,5
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6,6
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7,7
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8,8
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                    dealer = 0;
                    player++;
                    // 9,9
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 10,10
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // A,A
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                } else if (decks >= 4) {
                    // 5
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 9
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 10
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 11
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 12
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 13
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 17
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 18
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 19
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 13
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 17
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 18
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 19
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 2,2
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 3,3
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 4,4
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 5,5
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6,6
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7,7
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8,8
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                    dealer = 0;
                    player++;
                    // 9,9
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 10,10
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // A,A
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                }
                //</editor-fold>
            } else {
                //<editor-fold desc="Hit Soft 17">
                if(decks == 1) {
                    // 5
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 9
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 10
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 11
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer] = BjAction.DOUBLE_HIT;
                    dealer = 0;
                    player++;
                    // 12
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 13
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 16
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 17
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.SURRENDER_STAND;
                    dealer = 0;
                    player++;
                    // 18
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 19
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 13
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 17
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 18
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 19
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 2,2
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 3,3
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 4,4
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 5,5
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6,6
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7,7
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.SURRENDER_STAND;
                    split[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 8,8
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                    dealer = 0;
                    player++;
                    // 9,9
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 10,10
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // A,A
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                } else if(decks > 1 && decks < 4) {
                    // 5
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 9
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 10
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 11
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer] = BjAction.DOUBLE_HIT;
                    dealer = 0;
                    player++;
                    // 12
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 13
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 16
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 17
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.SURRENDER_STAND;
                    dealer = 0;
                    player++;
                    // 18
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 19
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 13
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 17
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 18
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 19
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 2,2
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 3,3
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 4,4
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 5,5
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6,6
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7,7
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8,8
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SURRENDER_SPLIT;
                    dealer = 0;
                    player++;
                    // 9,9
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 10,10
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // A,A
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                } else if(decks >= 4) {
                    // 5
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 9
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 10
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 11
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer++] = BjAction.DOUBLE_HIT;
                    hard[player][dealer] = BjAction.DOUBLE_HIT;
                    dealer = 0;
                    player++;
                    // 12
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 13
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 16
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer++] = BjAction.SURRENDER_HIT;
                    hard[player][dealer] = BjAction.SURRENDER_HIT;
                    dealer = 0;
                    player++;
                    // 17
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.SURRENDER_STAND;
                    dealer = 0;
                    player++;
                    // 18
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 19
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer++] = BjAction.STAND;
                    hard[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 13
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 14
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 15
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 16
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 17
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.DOUBLE_HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 18
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer++] = BjAction.HIT;
                    soft[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 19
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.DOUBLE_STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 20
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 21
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer++] = BjAction.STAND;
                    soft[player][dealer] = BjAction.STAND;

                    dealer = 0;
                    player = 0;
                    // 2,2
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 3,3
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 4,4
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 5,5
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.DOUBLE_HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 6,6
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 7,7
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer++] = BjAction.HIT;
                    split[player][dealer] = BjAction.HIT;
                    dealer = 0;
                    player++;
                    // 8,8
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SURRENDER_SPLIT;
                    dealer = 0;
                    player++;
                    // 9,9
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // 10,10
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer++] = BjAction.STAND;
                    split[player][dealer] = BjAction.STAND;
                    dealer = 0;
                    player++;
                    // A,A
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer++] = BjAction.SPLIT;
                    split[player][dealer] = BjAction.SPLIT;
                }
                //</editor-fold>
            }

            // spit out the tables to check them
//            System.out.print("Hard ");
//            for (int i = 0; i < hard[0].length; i++) {
//                System.out.print(String.format("%3d", i + 2));
//            }
//            System.out.println();
//            for (int i = 0; i < hard.length; i++) {
//                System.out.print(String.format("%5s", "" + (i + 5)));
//                for (int j = 0; j < hard[i].length; j++) {
//                    System.out.print(String.format("%3s", hard[i][j]));
//                }
//                System.out.println();
//            }
//            System.out.println();
//
//            System.out.print("Soft ");
//            for (int i = 0; i < soft[0].length; i++) {
//                System.out.print(String.format("%3d", i + 2));
//            }
//            System.out.println();
//            for (int i = 0; i < soft.length; i++) {
//                System.out.print(String.format("%5s", "" + (i + 13)));
//                for (int j = 0; j < soft[i].length; j++) {
//                    System.out.print(String.format("%3s", soft[i][j]));
//                }
//                System.out.println();
//            }
//            System.out.println();
//
//            System.out.print("Pair ");
//            for (int i = 0; i < split[0].length; i++) {
//                System.out.print(String.format("%3d", i + 2));
//            }
//            System.out.println();
//            for (int i = 0; i < split.length; i++) {
//                System.out.print(String.format("%5s", "" + (i + 2) + "," + (i + 2)));
//                for (int j = 0; j < split[i].length; j++) {
//                    System.out.print(String.format("%3s", split[i][j]));
//                }
//                System.out.println();
//            }
        }

        public BjAction getAction(BlackjackHand hand, int dealerValue, boolean canDouble, boolean canSurrender) {
            int hardValue = hand.getHard();
            int softValue = hand.getSoft();
            boolean canSplit = hand.canSplit();
            int splitValue = hand.getFirstCard().getFaceValue().getFaceValue();

//            System.out.println(String.format("hard %s, soft %s, dealer %s, split %s, splitValue %s", hardValue, softValue, dealerValue, canSplit, splitValue));

            BjAction result;
            if(game.isSimpleBlackjackHints()) {
                // dumbed down non-table version
                if(canSplit) {
//                    System.out.println("CAN SPLIT");
                    if(splitValue == 8 || splitValue == 11 ||
                            ((splitValue == 2 || splitValue == 3 || splitValue == 6 || splitValue == 7 || splitValue == 9) && dealerValue < 7)) {
//                        System.out.println("SUGGEST SPLIT");
                        return BjAction.SPLIT;
                    }
                }

                if(hardValue == 16 && dealerValue == 10 && canSurrender) {
//                    System.out.println("SUGGEST SURRENDER");
                    return BjAction.SURRENDER;
                }

                if(softValue != hardValue && hardValue <= 21) {
                    // even though it's soft, use the hard value
                    if(hardValue >= 13 && hardValue <= 15){
//                        System.out.println("SUGGEST HIT SOFT 13-15");
                        return BjAction.HIT;
                    } else if(hardValue >= 16 && hardValue <= 18) {
                        if(dealerValue >= 7) {
//                            System.out.println("SUGGEST HIT SOFT 16-18 dealer > 7");
                            return BjAction.HIT;
                        } else {
                            if(canDouble) {
//                                System.out.println("SUGGEST DOUBLE SOFT 16-18");
                                return BjAction.DOUBLE;
                            }
                            if(hardValue == 18 ) {
//                                System.out.println("SUGGEST STAND ON SOFT 18");
                                return BjAction.STAND;
                            }
//                            System.out.println("SUGGEST HIT SOFT 16-18");
                            return BjAction.HIT;
                        }
                    } else if(hardValue >= 19) {
//                        System.out.println("SUGGEST STAND SOFT 19+");
                        return BjAction.STAND;
                    }
                }

                // if we've hit and gone over w/ A=11 use the soft value
                if(hardValue > 21) {
                    hardValue = softValue;
                }

                if(hardValue >=4 && hardValue <= 8) {
//                    System.out.println("SUGGEST 4-8 HIT");
                    return BjAction.HIT;
                }

                if(hardValue == 9) {
                    if(canDouble && dealerValue < 7) {
//                        System.out.println("SUGGEST 9 DOUBLE");
                        return BjAction.DOUBLE;
                    }
//                    System.out.println("SUGGEST 9 HIT");
                    return BjAction.HIT;
                }

                if(hardValue == 10 || hardValue == 11) {
                    if(canDouble && hardValue > dealerValue) {
//                        System.out.println(String.format("SUGGEST 10-11 DOUBLE DEALER: %s", dealerValue));
                        return BjAction.DOUBLE;
                    }
//                    System.out.println(String.format("SUGGEST 10-11 HIT DEALER: %s", dealerValue));
                    return BjAction.HIT;
                }

                if(hardValue >= 12 && hardValue <= 16 && dealerValue >= 7) {
//                    System.out.println(String.format("SUGGEST HIT 12-16 dealer > 7: %s", dealerValue));
                    return BjAction.HIT;
                }

//                System.out.println("SUGGEST STAND (CATCH ALL)");
                return BjAction.STAND;
            } else {
                // basic strategy
                int dealerIndex = dealerValue - 2;
                if (canSplit) {
                    // look in the split table
                    int splitIndex = splitValue - 2;
                    result = split[splitIndex][dealerIndex];
                } else if (hardValue != softValue && hardValue <= 21) {
                    // this is a soft hand
                    int softIndex = hardValue - 13; // we still look at the hard cause we care about the high number
                    result = soft[softIndex][dealerIndex];
                } else {

                    int hardIndex = hardValue - 5;
                    // if we've hit and gone over w/ A=11 use the soft value
                    if(hardValue > 21) {
                        hardIndex = softValue - 5;
                    }
                    result = hard[hardIndex][dealerIndex];
                }

                if (result == BjAction.DOUBLE_HIT) {
                    if (canDouble) {
                        result = BjAction.DOUBLE;
                    } else {
                        result = BjAction.HIT;
                    }
                } else if (result == BjAction.DOUBLE_STAND) {
                    if (canDouble) {
                        result = BjAction.DOUBLE;
                    } else {
                        result = BjAction.STAND;
                    }
                } else if (result == BjAction.SURRENDER_HIT) {
                    if (canSurrender) {
                        result = BjAction.SURRENDER;
                    } else {
                        result = BjAction.HIT;
                    }
                } else if (result == BjAction.SURRENDER_STAND) {
                    if (canSurrender) {
                        result = BjAction.SURRENDER;
                    } else {
                        result = BjAction.STAND;
                    }
                } else if (result == BjAction.SURRENDER_SPLIT) {
                    if (canSurrender) {
                        result = BjAction.SURRENDER;
                    } else {
                        result = BjAction.SPLIT;
                    }
                }
            }
            return result;
        }
    }
}
