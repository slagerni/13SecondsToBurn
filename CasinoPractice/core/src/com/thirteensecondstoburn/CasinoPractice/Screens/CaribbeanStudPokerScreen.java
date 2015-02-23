package com.thirteensecondstoburn.CasinoPractice.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.ActionCompletedListener;
import com.thirteensecondstoburn.CasinoPractice.Actors.Card;
import com.thirteensecondstoburn.CasinoPractice.Actors.ChipStack;
import com.thirteensecondstoburn.CasinoPractice.Actors.TableButton;
import com.thirteensecondstoburn.CasinoPractice.Actors.Text;
import com.thirteensecondstoburn.CasinoPractice.Actors.WinLosePopup;
import com.thirteensecondstoburn.CasinoPractice.Assets;
import com.thirteensecondstoburn.CasinoPractice.CasinoPracticeGame;
import com.thirteensecondstoburn.CasinoPractice.Deck;
import com.thirteensecondstoburn.CasinoPractice.Actors.Hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Nick on 2/18/2015.
 */
public class CaribbeanStudPokerScreen extends TableScreen implements ActionCompletedListener {
    public static final float HAND_X_START = (CasinoPracticeGame.SCREEN_WIDTH - Card.CARD_WIDTH * 3) / 2;

    Deck deck;

    boolean isFirstDeck = true;
    boolean canBet = true;

    ChipStack anteStack;
    ChipStack playStack;
    Image anteCircle;
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

    WinLosePopup antePopup;
    WinLosePopup playPopup;

    int lastAnteBet = ChipStack.TABLE_MIN;
    BestHand bestHandPlayer = null;

    Card visibleCard;

    public CaribbeanStudPokerScreen(CasinoPracticeGame game) {
        super(game);
    }

    @Override
    public void setup() {
        Texture paytableTex = assets.getTexture(Assets.TEX_NAME.CARIBBEAN_STUD_PAYTABLE);
        paytable = new Image(paytableTex);
        paytable.setScale(.75f, .75f);
        paytable.setColor(1, 1, 1, .75f);
        paytable.setPosition(stage.getWidth() - 350 * paytable.getScaleX(), stage.getHeight() - paytableTex.getHeight() * paytable.getScaleY());

        title = new Image(assets.getTexture(Assets.TEX_NAME.CARIBBEAN_STUD_POKER_TITLE));
        title.setColor(mainColor);
        title.setPosition(315, stage.getHeight() - 250);


        anteCircle = new Image(assets.getTexture(Assets.TEX_NAME.ANTE_CIRCLE));
        anteCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + anteCircle.getWidth()) / 2, stage.getHeight()/2);
        playCircle = new Image(assets.getTexture(Assets.TEX_NAME.PLAY_CIRCLE));
        playCircle.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + playCircle.getWidth()) / 2, anteCircle.getY() - 200);

        anteStack = new ChipStack(assets, 0);
        anteStack.setPosition(HAND_X_START - (HAND_X_START - leftSide.getWidth() + anteCircle.getWidth()) / 2, stage.getHeight()/2 + 10);
        anteStack.addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (anteStack.isInside(x, y) && canBet) {
                    clearButton.setVisible(true);
                    int betAmount = leftSide.getBetAmount();
                    if(anteStack.getTotal() + leftSide.getBetAmount() * 2 > game.getBalance() - betAmount) {
                        playerHandText.setText("Insufficient funds to make that bet");
                        dealerHandText.setText("");
                        return;
                    }
                    if(anteStack.getTotal() + betAmount > ChipStack.TABLE_MAX) {
                        playerHandText.setText("Table maximum of " + ChipStack.TABLE_MAX);
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

        playButton = new TableButton(assets, "Bet 1x", mainColor);
        playButton.setPosition(stage.getWidth() - TableButton.BUTTON_WIDTH, 0);
        playButton.setName("1xButton");
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
        foldButton.setName("foldButton");
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

        qualifyText = new Text(assets, "DEALER QUALIFIES WITH ACE KING OR BETTER", 1);
        qualifyText.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - 40);
        qualifyText.setColor(.5f, .5f, .5f, .5f);

        antePopup = new WinLosePopup(assets);
        playPopup = new WinLosePopup(assets);

        stage.addActor(paytable);
        stage.addActor(title);

        stage.addActor(anteCircle);
        stage.addActor(playCircle);
        stage.addActor(anteStack);
        stage.addActor(playStack);

        stage.addActor(dealButton);
        stage.addActor(clearButton);
        stage.addActor(playButton);
        stage.addActor(foldButton);

        stage.addActor(dealerHandText);
        stage.addActor(playerHandText);
        stage.addActor(qualifyText);

        stage.addActor(antePopup);
        stage.addActor(playPopup);
    }

    private void dealHand() {
        if(playerHand != null) playerHand.remove();
        if(dealerHand != null) dealerHand.remove();
        playStack.setTotal(0);
        if(anteStack.getTotal() == 0) {
            if(lastAnteBet * 3 > game.getBalance()) {
                playerHandText.setText("You can't repeat your last bet");
                dealerHandText.setText("");
                return;
            }
            anteStack.setTotal(lastAnteBet);
            leftSide.setWagerText("" + (lastAnteBet));
            subtractFromBalance(lastAnteBet);
        }

        Card.Back back;
        if(isFirstDeck) { back = Card.Back.BACK1;} else {back = Card.Back.BACK2;}
        isFirstDeck = !isFirstDeck;
        deck = new Deck(assets, back);
        deck.shuffle();

        dealerHand = new Hand(deck.getCards().subList(0, 5), 125);
        dealerHand.getCards().get(4).setFaceUp(true); // one card shows from the dealer
        // TEST
//        ArrayList<Card> dealerTestCards = new ArrayList<Card>();
//        dealerTestCards.add(new Card(Card.FaceValue.KING, Card.Suit.SPADE, Card.Back.BACK1, false, assets));
//        dealerTestCards.add(new Card(Card.FaceValue.FIVE, Card.Suit.DIAMOND, Card.Back.BACK1, false, assets));
//        dealerTestCards.add(new Card(Card.FaceValue.FIVE, Card.Suit.CLUB, Card.Back.BACK1, false, assets));
//        dealerTestCards.add(new Card(Card.FaceValue.FIVE, Card.Suit.HEART, Card.Back.BACK1, false, assets));
//        dealerTestCards.add(new Card(Card.FaceValue.ACE, Card.Suit.HEART, Card.Back.BACK1, true, assets));
//        dealerHand = new Hand(dealerTestCards, 125);

        visibleCard = dealerHand.getCards().get(4);
        dealerHand.setPosition(HAND_X_START, CasinoPracticeGame.SCREEN_HEIGHT - Card.CARD_HEIGHT - 50);
        dealerHand.dealAction(CasinoPracticeGame.SCREEN_WIDTH, CasinoPracticeGame.SCREEN_HEIGHT);
        dealerHand.addActionListener(this);

        // TEST
//        ArrayList<Card> playerTestCards = new ArrayList<Card>();
//        playerTestCards.add(new Card(Card.FaceValue.EIGHT, Card.Suit.HEART, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.THREE, Card.Suit.SPADE, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.THREE, Card.Suit.CLUB, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.EIGHT, Card.Suit.CLUB, Card.Back.BACK1, true, assets));
//        playerTestCards.add(new Card(Card.FaceValue.THREE, Card.Suit.HEART, Card.Back.BACK1, true, assets));
//        bestHandPlayer = new BestHand(playerTestCards);

        bestHandPlayer = new BestHand(deck.getCards().subList(5, 10));
        playerHand = new Hand(bestHandPlayer.sortedCards, 125);
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
        playStack.setTotal(anteStack.getTotal() * 2);
        subtractFromBalance(anteStack.getTotal() * 2);
        calculateWager();
        showDealerHand();
        calculateWinner(false);
        canBet = true;
        toggleButtons(false);
        if(game.useHintText()) {
            TableButton correctButton = getCorrectButtonForHint();
            if(correctButton != buttonPressed) {
                showHint(getHintText(correctButton));
            }
        }
    }

    private void foldHand() {
        calculateWinner(true);
        canBet = true;
        toggleButtons(false);
        showDealerHand();
        anteStack.clearTotal();
        if(game.useHintText()) {
            TableButton correctButton = getCorrectButtonForHint();
            if(correctButton != foldButton) {
                showHint(getHintText(correctButton));
            }
        }
    }

    private void clearHand() {
        addToBalance(anteStack.getTotal());
        lastAnteBet = ChipStack.TABLE_MIN;
        anteStack.setTotal(0);
        playStack.setTotal(0);
        leftSide.setWonText("");
        dealerHandText.setText("");
        playerHandText.setText("");
        leftSide.setWagerText("");
        canBet = true;
        toggleButtons(false);
    }

    private void calculateWager() {
        leftSide.setWagerText("" + (anteStack.getTotal() + playStack.getTotal()));
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
            if(game.usePreBetHints()) {
                setPreBetHint();
            }
        }
    }

    enum HandType {HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH};
    private void calculateWinner(boolean playerFolded) {
        BestHand bestHandDealer = new BestHand(dealerHand.getCards());
        dealerHand.setCards(bestHandDealer.sortedCards);

        // ok, now that we know what they both have, who won and how much?
        int playerWon = bestHandPlayer.getHandType().ordinal() - bestHandDealer.getHandType().ordinal();
        if(playerFolded) playerWon = -1;

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

        // dealer must have A,K,x,x,x or better to qualify
        boolean dealerQualifies = bestHandDealer.getHandType().ordinal() > 0 || (bestHandDealer.getStraightValues().get(0) == 14 && bestHandDealer.getStraightValues().get(1) == 13);
        int total;
        if(playerWon > 0 || (!dealerQualifies && !playerFolded)) {
            // the player really won
            if(dealerQualifies) {
                //If the player has the higher poker hand then the Ante pays even money and the Play goes to the paytable.
                total = anteStack.getTotal() * 2 + playStack.getTotal();
                switch (bestHandPlayer.getHandType()) {
                    case ROYAL_FLUSH:
                        total += playStack.getTotal() * 100;
                        break;
                    case STRAIGHT_FLUSH:
                        total += playStack.getTotal() * 50;
                        break;
                    case FOUR_OF_A_KIND:
                        total += playStack.getTotal() * 20;
                        break;
                    case FULL_HOUSE:
                        total += playStack.getTotal() * 7;
                        break;
                    case FLUSH:
                        total += playStack.getTotal() * 5;
                        break;
                    case STRAIGHT:
                        total += playStack.getTotal() * 4;
                        break;
                    case THREE_OF_A_KIND:
                        total += playStack.getTotal() * 3;
                        break;
                    case TWO_PAIR:
                        total += playStack.getTotal() * 2;
                        break;
                    default:
                        total += playStack.getTotal();
                        break;
                }
                antePopup.pop(true, anteCircle.getX() + 37, anteCircle.getY() + 38);
                playPopup.pop(true, playCircle.getX() + 37, playCircle.getY() + 38);
            } else {
                // If the dealer does not qualify then the Ante bet will pay even money and the play will push.
                total = anteStack.getTotal() * 2 + playStack.getTotal();
                antePopup.pop(true, playCircle.getX() + 37, playCircle.getY() + 38);
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

        addToBalance(total);

        int initialBet = anteStack.getTotal() + playStack.getTotal();

        if(total - initialBet > 0)
            leftSide.setWonColor(Color.GREEN);
        else if(total - initialBet < 0)
            leftSide.setWonColor(Color.RED);
        else
            leftSide.setWonColor(Color.WHITE);

        leftSide.setWonText("" + (total - initialBet));

        setHandText(bestHandPlayer, bestHandDealer, dealerQualifies, playerFolded);

        anteStack.clearTotal();
        playStack.clearTotal();
    }

    private void setHandText(BestHand playerHand, BestHand dealerHand, boolean dealerQualified, boolean playerFolded) {
        StringBuilder pb = new StringBuilder();
        StringBuilder db = new StringBuilder();

        if(playerFolded) {
            pb.append("Folded: ");
        } else {
            if(!dealerQualified) {
                db.append("Dealer doesn't qualify: ");
            }
        }

        pb.append(setHandValueText(playerHand));
        db.append(setHandValueText(dealerHand));

        dealerHandText.setText(db.toString());
        playerHandText.setText(pb.toString());
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

    private void setPreBetHint() {
        foldButton.setColor(mainColor);
        playButton.setColor(mainColor);

        TableButton button = getCorrectButtonForHint();
        button.setColor(hintColor);
    }

    private TableButton getCorrectButtonForHint() {
        if(bestHandPlayer.getHandType().ordinal() > HandType.HIGH_CARD.ordinal()) return playButton; // pair or better? play on
        if(bestHandPlayer.getStraightValues().get(0) != 14 || bestHandPlayer.getStraightValues().get(1) != 13) return foldButton; // less than the dealer's qualifying hand? bail.

        int vcValue = visibleCard.getFaceValue().getStraightValue();
        // Raise if the dealer's card is a 2 through queen and matches one of yours.
        if(vcValue < 13 && bestHandPlayer.getStraightValues().contains(vcValue)) return playButton;

        // Raise if the dealer's card is an ace or king and you have a queen or jack in your hand.
        if(vcValue >= 13 && (bestHandPlayer.getStraightValues().contains(12) || bestHandPlayer.getStraightValues().contains(11))) return playButton;

        // Raise if the dealer's rank does not match any of yours and you have a queen in your hand and the dealer's card is less than your fourth highest card.
        if(!bestHandPlayer.getStraightValues().contains(vcValue) && bestHandPlayer.getStraightValues().get(2) == 12 && bestHandPlayer.getStraightValues().get(3) > vcValue) return playButton;

        // everything's covered. fold it.
        return foldButton;
    }

    private String getHintText(TableButton correctButton) {
        int vcValue = visibleCard.getFaceValue().getStraightValue();
        if(correctButton == playButton) {
            // this means the player folded when they shouldn't have
            if(bestHandPlayer.getHandType().ordinal() > HandType.HIGH_CARD.ordinal())
                return "Always bet with a Pair or higher";

            // Raise if the dealer's card is a 2 through queen and matches one of yours.
            if(vcValue < 13 && bestHandPlayer.getStraightValues().contains(vcValue))
                return "Raise if the dealer's card is a 2 through queen and matches one of yours";

            // Raise if the dealer's card is an ace or king and you have a queen or jack in your hand.
            if(vcValue >= 13 && (bestHandPlayer.getStraightValues().contains(12) || bestHandPlayer.getStraightValues().contains(11)))
                return "Raise if the dealer's card is an ace or king and you have a queen or jack in your hand";

            // Raise if the dealer's rank does not match any of yours and you have a queen in your hand and the dealer's card is less than your fourth highest card.
            if(!bestHandPlayer.getStraightValues().contains(vcValue) && bestHandPlayer.getStraightValues().get(2) == 12 && bestHandPlayer.getStraightValues().get(3) > vcValue)
                return "Raise if the dealer's rank does not match any of yours and you have a queen in your hand and the dealer's card is less than your fourth highest card";
        } else {
            // this means the player played when they should have folded
            System.out.println("card1: " + (bestHandPlayer.getStraightValues().get(0) != 14) + " card2: " + (bestHandPlayer.getStraightValues().get(1) != 13));
            if(bestHandPlayer.getStraightValues().get(0) != 14 || bestHandPlayer.getStraightValues().get(1) != 13)
                return "Always fold with worse than Ace, King";

            return "Raise only if: the dealer's rank does not match any of yours and you have a queen in your hand and the dealer's card is less than your fourth highest card"
                + " OR the dealer's card is an ace or king and you have a queen or jack in your hand"
                + " OR the dealer's card is a 2 through queen and matches one of yours";
        }

        System.out.println("THIS SHOULDN'T HAPPEN");
        return "This shouldn't happen"; // this shouldn't happen;
    }

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
