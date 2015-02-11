package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStack;
import com.thirteensecondstoburn.CasinoPractice.Actors.LeftSide;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Actors.WinLosePopup;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;
import com.thirteensecondstoburn.CasinoPractice.Hand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 1/14/2015.
 */
public class ThreeCardPokerScreen extends TableScreen implements ActionCompletedListener {
    public static final float HAND_X_START = (CasinoPracticeGame.SCREEN_WIDTH - Card.CARD_WIDTH * 3) / 2;
    Deck deck;

    boolean isFirstDeck = true;
    boolean canBet = true;

    ChipStack anteStack;
    ChipStack pairPlusStack;
    ChipStack playStack;
    Image anteCircle;
    Image pairPlusCircle;
    Image playCircle;

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

    WinLosePopup pairPlusPopup;
    WinLosePopup antePopup;
    WinLosePopup playPopup;

    int lastAnteBet = 5;
    int lastPairPlusBet = 0;

    public ThreeCardPokerScreen(CasinoPracticeGame game) {
        super(game);
    }

    @Override
    public void setup() {
        Gdx.input.setInputProcessor(stage);
        stage.addAction(Actions.alpha(1));

        Texture back = assets.getTexture(Assets.TEX_NAME.BACKGROUND);
        back.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        background = new Sprite(back, (int)stage.getWidth(), (int)stage.getHeight());
        background.setSize(stage.getWidth(), stage.getHeight());
        background.setColor(backgroundColor);

        paytable = new Image(assets.getTexture(Assets.TEX_NAME.THREE_CARD_POKER_PAYTABLE));
        paytable.setScale(.75f, .75f);
        paytable.setColor(1, 1, 1, .75f);
        paytable.setPosition(stage.getWidth() - 500 * paytable.getScaleX(), stage.getHeight() - 500 * paytable.getScaleY());

        title = new Image(assets.getTexture(Assets.TEX_NAME.THREE_CARD_POKER_TITLE));
        title.setColor(mainColor);
//        title.setPosition(270, stage.getHeight() - title.getImageHeight() - 10);
        title.setPosition(315, stage.getHeight() - 270);


        pairPlusCircle = new Image(assets.getTexture(Assets.TEX_NAME.PAIRPLUS_CIRCLE));
        pairPlusCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + pairPlusCircle.getWidth()) / 2, stage.getHeight()/2);
        anteCircle = new Image(assets.getTexture(Assets.TEX_NAME.ANTE_CIRCLE));
        anteCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + anteCircle.getWidth()) / 2, pairPlusCircle.getY() - 200);
        playCircle = new Image(assets.getTexture(Assets.TEX_NAME.PLAY_CIRCLE));
        playCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + playCircle.getWidth()) / 2, anteCircle.getY() - 200);

        pairPlusStack = new ChipStack(assets, 0);
        pairPlusStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + pairPlusCircle.getWidth()) / 2, stage.getHeight()/2 + 10);
        pairPlusStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pairPlusStack.isInside(x, y) && canBet) {
                    clearButton.setVisible(true);
                    int betAmount = leftSide.getBetAmount();
                    if(anteStack.getTotal() + betAmount > game.getBalance()) {
                        playerHandText.setText("Insufficient funds to make that bet");
                        dealerHandText.setText("");
                        return;
                    }
                    if(pairPlusStack.getTotal() + betAmount > leftSide.TABLE_MAX) {
                        playerHandText.setText("Table maximum of " + leftSide.TABLE_MAX);
                        dealerHandText.setText("");
                        return;
                    }
                    pairPlusStack.increaseTotal(betAmount);
                    lastPairPlusBet = pairPlusStack.getTotal();
                    subtractFromBalance(betAmount);
                    calculateWager();
                    dealButton.setVisible(true);
                }
            }
        });

        anteStack = new ChipStack(assets, 0);
        anteStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + anteCircle.getWidth()) / 2, pairPlusStack.getY() - 200);
        anteStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (anteStack.isInside(x, y) && canBet) {
                    clearButton.setVisible(true);
                    int betAmount = leftSide.getBetAmount();
                    if(anteStack.getTotal() + leftSide.getBetAmount() > game.getBalance() - betAmount) {
                        playerHandText.setText("Insufficient funds to make that bet");
                        dealerHandText.setText("");
                        return;
                    }
                    if(anteStack.getTotal() + betAmount > leftSide.TABLE_MAX) {
                        playerHandText.setText("Table maximum of " + leftSide.TABLE_MAX);
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

        playStack = new ChipStack(assets, 0);
        playStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + playCircle.getWidth()) / 2, anteStack.getY() - 200);
        playStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (playStack.isInside(x, y) && !canBet) {
                    playHand();
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
                    playHand();
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
        dealerHandText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 50 - 10);

        playerHandText = new Text(assets, "", 1.5f);
        playerHandText.setPosition(HAND_X_START, Card.CARD_HEIGHT + 50 + 60);

        qualifyText = new Text(assets, "DEALER QUALIFIES WITH QUEEN HIGH", 1);
        qualifyText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - 10);
        qualifyText.setColor(.5f, .5f, .5f, .5f);

        pairPlusPopup = new WinLosePopup(assets);
        antePopup = new WinLosePopup(assets);
        playPopup = new WinLosePopup(assets);

        stage.addActor(paytable);
        stage.addActor(title);

        stage.addActor(anteCircle);
        stage.addActor(pairPlusCircle);
        stage.addActor(playCircle);
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

        stage.addActor(pairPlusPopup);
        stage.addActor(antePopup);
        stage.addActor(playPopup);
    }

     private void dealHand() {
        if(playerHand != null) playerHand.remove();
        if(dealerHand != null) dealerHand.remove();
        playStack.setTotal(0);
        if(anteStack.getTotal() == 0) {
            if(lastAnteBet * 2 + lastPairPlusBet > game.getBalance()) {
                playerHandText.setText("You can't repeat your last bet");
                dealerHandText.setText("");
                return;
            }
            anteStack.setTotal(lastAnteBet);
            pairPlusStack.setTotal(lastPairPlusBet);
            leftSide.setWagerText("" + (lastAnteBet + lastPairPlusBet));
            subtractFromBalance(lastAnteBet + lastPairPlusBet);
        }

        Card.Back back;
        if(isFirstDeck) { back = Card.Back.BACK1;} else {back = Card.Back.BACK2;}
        isFirstDeck = !isFirstDeck;
        deck = new Deck(assets, back);
        deck.shuffle();

        dealerHand = new Hand(deck.getCards().subList(0, 3), Card.CARD_WIDTH);
        dealerHand.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 50);
        if(dealerHand.hasFaceValue(Card.FaceValue.TWO ) && dealerHand.hasFaceValue(Card.FaceValue.THREE )) {
            dealerHand.sortLowAce();
        } else {
            dealerHand.sort();
        }
        dealerHand.dealAction(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        dealerHand.addActionListener(this);

        playerHand = new Hand(deck.getCards().subList(3, 6), Card.CARD_WIDTH);
        playerHand.setPosition(HAND_X_START, 50);
        playerHand.setVisible(false);
        if(playerHand.hasFaceValue(Card.FaceValue.TWO ) && playerHand.hasFaceValue(Card.FaceValue.THREE )) {
            playerHand.sortLowAce();
        } else {
            playerHand.sort();
        }
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
    
    private void playHand() {
        playStack.setTotal(anteStack.getTotal());
        subtractFromBalance(anteStack.getTotal());
        calculateWager();
        showDealerHand();
        calculateWinner(false);
        canBet = true;
        toggleButtons(false);
    }

    private void addToBalance(int amount) {
        game.addToBalance(amount);
        leftSide.setBalanceText("" + game.getBalance());
    }

    private void subtractFromBalance(int amount) {
        addToBalance(-amount);
    }

    private void foldHand() {
        calculateWinner(true);
        canBet = true;
        toggleButtons(false);
        showDealerHand();
        anteStack.clearTotal();
        pairPlusStack.clearTotal();
    }

    private void clearHand() {
        addToBalance(anteStack.getTotal() + pairPlusStack.getTotal());
        lastAnteBet = 5;
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
        }
    }

    enum HandType {HIGH_CARD, PAIR, FLUSH, STRAIGHT, THREE_OF_A_KIND, STRAIGHT_FLUSH};
    private void calculateWinner(boolean playerFolded) {
        int dealerHighValue = 1;
        int playerHighValue = 1;
        Card.FaceValue dealerHighFaceValue;
        Card.FaceValue playerHighFaceValue;

        HandType dealerHandType, playerHandType;

        // check for straight
        List<Card> dealerCards = dealerHand.getCards();
        ArrayList<Object> dealerHandResult = getHandType(dealerCards);
        dealerHandType = (HandType)dealerHandResult.get(0);
        dealerHighValue = (Integer)dealerHandResult.get(1);
        dealerHighFaceValue = (Card.FaceValue)dealerHandResult.get(2);
        boolean dealerQualifies = false;
        if(dealerHandType.ordinal() > HandType.HIGH_CARD.ordinal() || dealerCards.get(0).getFaceValue().ordinal() >= Card.FaceValue.QUEEN.ordinal()) {
            dealerQualifies = true;
        }

        List<Card> playerCards = playerHand.getCards();
        ArrayList<Object> playerHandResult = getHandType(playerCards);
        playerHandType = (HandType)playerHandResult.get(0);
        playerHighValue = (Integer)playerHandResult.get(1);
        playerHighFaceValue = (Card.FaceValue)playerHandResult.get(2);

        // ok, now that we know what they both have, who won and how much?
        int playerWon = playerHandType.ordinal() - dealerHandType.ordinal();
        if(playerFolded) playerWon = -1;

        if(playerWon == 0) {
            switch (playerHandType) {
                case STRAIGHT_FLUSH:
                case THREE_OF_A_KIND:
                case STRAIGHT:
                    // we know all of these are the same and can be determined by the top card
                    playerWon = playerHighValue - dealerHighValue;
                    break;
                case FLUSH:
                case HIGH_CARD:
                    // since the hands are sorted, just go down the list together comparing until something is different
                    int temp = playerCards.get(0).getFaceValue().getStraightValue() - dealerCards.get(0).getFaceValue().getStraightValue();
                    if(temp != 0) { playerWon = temp; break;}
                    temp = playerCards.get(1).getFaceValue().getStraightValue() - dealerCards.get(1).getFaceValue().getStraightValue();
                    if(temp != 0) { playerWon = temp; break;}
                    temp = playerCards.get(2).getFaceValue().getStraightValue() - dealerCards.get(2).getFaceValue().getStraightValue();
                    playerWon = temp; // at this point it's 3rd card or a tie
                    break;
                case PAIR:
                    playerWon = playerHighValue - dealerHighValue; // higher pair wins first
                    if(playerWon == 0) { // in the unlikely case they both have the same pair..
                        int playerOther = -1, dealerOther = -1;
                        for (Card c : playerCards) {
                            if (c.getFaceValue().getStraightValue() != playerHighValue) playerOther = c.getFaceValue().getStraightValue();
                        }
                        for (Card c : dealerCards) {
                            if (c.getFaceValue().getStraightValue() != playerHighValue) dealerOther = c.getFaceValue().getStraightValue();
                        }
                        playerWon = playerOther - dealerOther;
                    }
                    break;
            }
        }

        int total = 0;
        if(playerWon > 0 || (!dealerQualifies && !playerFolded)) {
            // the player really won
            if(dealerQualifies) {
                //If the player has the higher poker hand then the Ante and Play will both pay even money.
                total = anteStack.getTotal() * 2 + playStack.getTotal() * 2;
                antePopup.pop(true, anteCircle.getX() + 37, anteCircle.getY() + 38);
                playPopup.pop(true, playCircle.getX() + 37, playCircle.getY() + 38);
            } else {
                // If the dealer does not qualify then the player will win even money on the Ante bet and the Play bet will push.
                total = anteStack.getTotal() * 2 + playStack.getTotal();
                antePopup.pop(true, anteCircle.getX() + 37, anteCircle.getY() + 38);
            }
        } else if(playerWon < 0) {
            // the player lost
            total = 0; // we've already pulled this from the balance. no need to re-deduct
            antePopup.pop(false, anteCircle.getX() + 37, anteCircle.getY() + 38);
            if(playStack.getTotal() > 0) {
                playPopup.pop(false, playCircle.getX() + 37, playCircle.getY() + 38);
            }
        } else {
            // tie.
            total = anteStack.getTotal() + playStack.getTotal();
        }

        // Ante Bonus
        if(playerHandType.ordinal() >= HandType.STRAIGHT.ordinal()) {
            switch(playerHandType) {
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
        if(!playerFolded && pairPlusStack.getTotal() > 0 && playerHandType.ordinal() >= HandType.PAIR.ordinal()) {
            total += pairPlusStack.getTotal(); // return the initial bet
            pairPlusPopup.pop(true, pairPlusCircle.getX() + 37, pairPlusCircle.getY() + 38);
            switch(playerHandType) {
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
            pairPlusPopup.pop(false, pairPlusCircle.getX() + 37, pairPlusCircle.getY() + 38);
        }
        addToBalance(total);

        int initialBet = anteStack.getTotal() + playStack.getTotal() + pairPlusStack.getTotal();

        if(total - initialBet > 0)
            leftSide.setWonColor(Color.GREEN);
        else if(total - initialBet < 0)
            leftSide.setWonColor(Color.RED);
        else
            leftSide.setWonColor(Color.WHITE);

        leftSide.setWonText("" + (total - initialBet));

        setHandText(dealerHandType, dealerHighFaceValue, playerHandType, playerHighFaceValue, dealerQualifies, playerFolded);

        anteStack.clearTotal();
        pairPlusStack.clearTotal();
        playStack.clearTotal();
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

    public ArrayList<Object> getHandType(List<Card> cards) {
        int highValue = cards.get(0).getFaceValue().getStraightValue(); // we've already sorted
        Card.FaceValue highFaceValue = cards.get(0).getFaceValue();
        boolean straight = isStraight(cards);
        boolean flush = isFlush(cards);
        boolean threeOfAKind = isThreeOfAKind(cards);
        Card.FaceValue pair = Card.FaceValue.NULL;
        if(!threeOfAKind) pair = checkPair(cards);
        if(pair.getStraightValue() > 0) {
            highValue = pair.getStraightValue();
            highFaceValue = pair;
        }

        ArrayList<Object> results = new ArrayList<Object>();
        if(straight && flush) {
            results.add(HandType.STRAIGHT_FLUSH);
        } else if(threeOfAKind) {
            results.add(HandType.THREE_OF_A_KIND);
        } else if(straight) {
            results.add(HandType.STRAIGHT);
        } else if(flush) {
            results.add(HandType.FLUSH);
        } else if(pair.getStraightValue() > 0) {
            results.add(HandType.PAIR);
        } else {
            results.add(HandType.HIGH_CARD);
        }

        results.add(highValue);
        results.add(highFaceValue);
        return results;
    }

    public boolean isThreeOfAKind(List<Card>cards) {
        int value = cards.get(0).getFaceValue().getStraightValue();
        for(Card c : cards) {
            if(c.getFaceValue().getStraightValue() != value) return false;
        }
        return true;
    }

    public boolean isStraight(List<Card> cards) {
        String straightString = "14,13,12,11,10,9,8,7,6,5,4,3,2,14,";
        String cardValues = "";
        for(Card c : cards) {
            cardValues += c.getFaceValue().getStraightValue() + ",";
        }
        return straightString.contains(cardValues);
    }

    public boolean isFlush(List<Card> cards) {
        Card.Suit suit = cards.get(0).getSuit();
        for(Card c : cards) {
            if(c.getSuit() != suit) return false;
        }
        return true;
    }

    public Card.FaceValue checkPair(List<Card> cards) {
        // first two
        if(cards.get(0).getFaceValue().getStraightValue() == cards.get(1).getFaceValue().getStraightValue()) return cards.get(0).getFaceValue();
        // first and last
        if(cards.get(0).getFaceValue().getStraightValue() == cards.get(2).getFaceValue().getStraightValue()) return cards.get(0).getFaceValue();
        // last two
        if(cards.get(1).getFaceValue().getStraightValue() == cards.get(2).getFaceValue().getStraightValue()) return cards.get(1).getFaceValue();
        return Card.FaceValue.NULL;
    }
}
