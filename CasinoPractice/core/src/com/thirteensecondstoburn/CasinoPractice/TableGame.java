package com.thirteensecondstoburn.CasinoPractice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Nick on 10/19/2016.
 */
public class TableGame {
    public final static TableGame Miscellaneous = new TableGame (0, "Miscellaneous Stats", null);
    public final static TableGame ThreeCardPoker = new TableGame(1, "Three Card Poker", Assets.TEX_NAME.THREE_CARD_POKER_TITLE);
    public final static TableGame Crazy4Poker = new TableGame(2, "Crazy 4 Poker", Assets.TEX_NAME.CRAZY_FOUR_POKER_TITLE);
    public final static TableGame CaribbeanStudPoker = new TableGame(3, "Caribbean Stud Poker", Assets.TEX_NAME.CARIBBEAN_STUD_POKER_TITLE);
    public final static TableGame Blackjack = new TableGame(4, "Blackjack", Assets.TEX_NAME.BLACKJACK_TITLE);
    public final static TableGame Craps = new TableGame(5, "Craps", Assets.TEX_NAME.CRAPS_TITLE);
    public final static TableGame Roulette = new TableGame(6, "Roulette", Assets.TEX_NAME.ROULETTE_TITLE);
    public final static TableGame LetItRide = new TableGame(7, "Let it Ride", Assets.TEX_NAME.LET_IT_RIDE_TITLE);

    private final int id;
    private final String displayName;
    private final Assets.TEX_NAME titleTexture;

    @JsonCreator
    public TableGame(@JsonProperty("id") int id, @JsonProperty("displayName") String displayName, @JsonProperty("titleTexture") Assets.TEX_NAME titleTexture) {
        this.id = id;
        this.displayName = displayName;
        this.titleTexture = titleTexture;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return displayName;
        }
    }

    public static TableGame parse(String toParse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(toParse, TableGame.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getId() {return id;}
    public Assets.TEX_NAME getTitleTexture() {return titleTexture;}
    public String getDisplayName() {return displayName;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!TableGame.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final TableGame other = (TableGame) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * id;
    }
}
