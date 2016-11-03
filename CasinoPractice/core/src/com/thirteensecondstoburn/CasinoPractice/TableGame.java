package com.thirteensecondstoburn.CasinoPractice;

/**
 * Created by Nick on 10/19/2016.
 */
public enum TableGame {
    ThreeCardPoker(1, "Three Card Poker", Assets.TEX_NAME.THREE_CARD_POKER_TITLE),
    Crazy4Poker(2, "Crazy 4 Poker", Assets.TEX_NAME.CRAZY_FOUR_POKER_TITLE),
    CaribbeanStudPoker(3, "Caribbean Stud Poker", Assets.TEX_NAME.CARIBBEAN_STUD_POKER_TITLE),
    Blackjack(4, "Blackjack", Assets.TEX_NAME.BLACKJACK_TITLE),
    Craps(5, "Craps", Assets.TEX_NAME.CRAPS_TITLE),
    Roulette(6, "Roulette", Assets.TEX_NAME.ROULETTE_TITLE),
    LetItRide(7, "Let it Ride", Assets.TEX_NAME.LET_IT_RIDE_TITLE);

    private final int id;
    private final String displayName;
    private final Assets.TEX_NAME titleTexture;

    TableGame(int id, String displayName, Assets.TEX_NAME titleTexture) {
        this.id = id;
        this.displayName = displayName;
        this.titleTexture = titleTexture;
    }

    @Override
    public String toString() { return displayName; }
    public int getId() {return id;}
    public Assets.TEX_NAME getTitleTexture() {return titleTexture;}
}
