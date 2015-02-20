package com.thirteensecondstoburn.CasinoPractice.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.thirteensecondstoburn.CasinoPractice.Assets;

import java.util.Comparator;

import javax.xml.soap.Text;

/**
 * Created by Nick on 1/14/2015.
 */
public class Card extends Actor implements Comparable<Card>{
    public static float CARD_WIDTH = 226;
    public static float CARD_HEIGHT = 314;

    public Card(FaceValue faceValue, Suit suit, Back back, boolean faceUp, Assets assets) {
        setFaceValue(faceValue);
        setSuit(suit);
        setBack(back);
        setFaceUp(faceUp);
        this.assets = assets;
        this.setWidth(226);
        this.setHeight(314);
        setBounds(0, 0, getWidth(), getHeight());
        setTouchable(Touchable.enabled);
    }

    public Card(Card copy) {
        faceValue = copy.faceValue;
        suit = copy.suit;
        back = copy.back;
        faceUp = copy.faceUp;
        assets = copy.assets;
        setWidth(copy.getWidth());
        setHeight((copy.getHeight()));
        setBounds(copy.getX(), copy.getY(), copy.getWidth(), copy.getHeight());
        setTouchable(copy.getTouchable());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(faceUp) {
            Image card = new Image(assets.getCardTexture(getImageKey()));
            card.setOrigin(card.getWidth() / 2, card.getHeight() / 2);// origin sets the location to rotate around
            card.setPosition(this.getX(), this.getY());
            card.setRotation(this.getRotation());
            card.setScaleX(this.getScaleX());
            card.setScaleY(this.getScaleY());
            card.draw(batch, parentAlpha);
        }
        else {
            Image card = new Image(assets.getCardTexture(getBack().getStrValue()));
            card.setPosition(this.getX(), this.getY());
            card.setOrigin(card.getWidth() / 2, card.getHeight() / 2); // origin sets the location to rotate around
            card.setRotation(this.getRotation());
            card.draw(batch, parentAlpha);
        }
    }

    public String getImageKey() {
        return getFaceValue().getStrValue() + getSuit().getStrValue();
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public FaceValue getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(FaceValue faceValue) {
        this.faceValue = faceValue;
    }

    public Back getBack() {
        return back;
    }

    public void setBack(Back back) {
        this.back = back;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    @Override
    public int compareTo(Card compareCard) {
        return compareCard.faceValue.straightValue - faceValue.straightValue;
    }

    @Override
    public String toString() {
        return faceValue.getStrValue() + suit.getStrValue();
    }
    // stupid aces being either high or low..
    public static Comparator<Card> LowAceComparator = new Comparator<Card>() {
        @Override
        public int compare(Card card1, Card card2) {
            int card1Value = card1.faceValue.straightValue;
            int card2Value = card2.faceValue.straightValue;

            if(card1.faceValue ==  FaceValue.ACE) card1Value = 1;
            if(card2.faceValue ==  FaceValue.ACE) card2Value = 1;

            return card2Value - card1Value;
        }
    };

    public enum Suit {
        CLUB ("C", "Clubs"), DIAMOND("D", "Diamonds"), HEART("H", "Hearts"), SPADE("S", "Spades"), JOKER_BLACK("Black", "Joker"), JOKER_RED("Red", "Joker");

        private final String strValue;
        private final String name;
        Suit(String strValue, String name) {
            this.strValue = strValue;
            this.name = name;
        }

        public String getStrValue() {return strValue;}
        @Override
        public String toString() { return name; }
    }

    public enum FaceValue {
        NULL("", "NULL", "NULL", -1, -1),
        TWO("2", "2", "2s", 2, 2),
        THREE("3", "3", "3s", 3, 3),
        FOUR("4", "4", "4s", 4, 4),
        FIVE("5", "5", "5s", 5, 5),
        SIX("6", "6", "6s", 6, 6),
        SEVEN("7", "7", "7s", 7, 7),
        EIGHT("8", "8", "8s", 8, 8),
        NINE("9", "9", "9s", 9, 9),
        TEN("10", "10", "10s", 10, 10),
        JACK("J", "Jack", "Jacks", 11, 10),
        QUEEN("Q", "Queen", "Queens", 12, 10),
        KING("K", "King", "Kings", 13, 10),
        ACE("A", "Ace", "Aces", 14, 11),
        JOKER("Joker", "Joker", "Jokers", 100, 100);

        private final String strValue;
        private final String singleText;
        private final String multiText;
        private final int straightValue;
        private final int faceValue;

        FaceValue(String strValue, String singleText, String multiText, int straitValue, int faceValue) {
            this.strValue = strValue;
            this.singleText = singleText;
            this.multiText = multiText;
            this.straightValue = straitValue;
            this.faceValue = faceValue;
        }

        public String getStrValue() {return strValue;}
        public int getStraightValue() {return straightValue;}
        public String getSingleText() {return singleText;}
        public String getMultiText() {return multiText;}
        public int getFaceValue() {return faceValue;}
    }

    public enum Back {
        BACK1 ("Back1"), BACK2("Back2");

        private final String strValue;

        Back(String strValue) {
            this.strValue = strValue;
        }

        String getStrValue() { return strValue;}
    }

    private Suit suit = Suit.JOKER_BLACK;
    private FaceValue faceValue = FaceValue.JOKER;
    private Back back = Back.BACK1;
    private boolean faceUp = false;
    private Assets assets;
}
