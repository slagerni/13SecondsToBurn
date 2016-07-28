package com.thirteensecondstoburn.CasinoPractice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

/**
 * Created by Nick on 1/14/2015.
 */
public class Assets {
    public enum TEX_NAME {
        BACKGROUND, FONT, BUTTON_BLANK, BUTTON_BLANK_DOWN, LEFT_SIDE, ANTE_CIRCLE, PAIRPLUS_CIRCLE, PLAY_CIRCLE, WIN_POPUP, LOSE_POPUP,
        THREE_CARD_POKER_PAYTABLE, THREE_CARD_POKER_TITLE, QUEENS_UP_CIRCLE, SUPER_BONUS_CIRCLE, CRAZY_FOUR_POKER_PAYTABLE, CRAZY_FOUR_POKER_TITLE,
        MENU_BUTTON, MENU_BUTTON_DOWN, SPLASH_TITLE, BURN_LOGO, BLACK_50_ALPHA, CARIBBEAN_STUD_POKER_TITLE, CARIBBEAN_STUD_PAYTABLE, BLACKJACK_TITLE,
        BLANK_CIRCLE, SETTINGS, CRAPS_TABLE, CRAPS_TITLE, DIEFACE1, DIEFACE2, DIEFACE3, DIEFACE4, DIEFACE5, DIEFACE6, CHIP_ON, CHIP_OFF, DICE_FRAMES,
        THREEX_POPUP, FOURX_POPUP, FIVEX_POPUP, SIXX_POPUP, ROULETTE_TABLE_AMERICAN, ROULETTE_TABLE_EUROPEAN, ROULETTE_WHEEL_AMERICAN,
        ROULETTE_WHEEL_EUROPEAN, ROULETTE_WHEEL_BALL, ROULETTE_TITLE, ROULETTE_NUMBER_BOARD, STORE_BUTTON,
        BUY_5000_CHIPS, BUY_25000_CHIPS, BUY_100000_CHIPS, BUY_500000_CHIPS, BUY_5000000_CHIPS, HELP_ICON, LET_IT_RIDE_TITLE,
        LET_IT_RIDE_CIRCLE_1, LET_IT_RIDE_CIRCLE_2, LET_IT_RIDE_CIRCLE_DOLLAR_SIGN, LET_IT_RIDE_PAYTABLE
    }
    TextureAtlas cardAtlas;
    TextureAtlas chipAtlas;
    private HashMap<TEX_NAME, Texture> textures = new HashMap<TEX_NAME, Texture>();
    private BitmapFont font;
    public static class DistanceFieldShader extends ShaderProgram {
        public DistanceFieldShader() {
            super(Gdx.files.internal("shaders/distancefield.vert"),
                    Gdx.files.internal("shaders/distancefield.frag"));
            if (!isCompiled()) {
                throw new RuntimeException("Shader compilation failed:\n"+ getLog());
            }
        }

        /**
         * @param smoothing
         *            a value between 0 and 1
         */
        public void setSmoothing(float smoothing) {
            float delta = 0.5f * MathUtils.clamp(smoothing, 0, 1);
            setUniformf("u_lower", 0.5f - delta);
            setUniformf("u_upper", 0.5f + delta);
        }
    }
    private DistanceFieldShader distanceFieldShader;
    Skin uiSkin;


    Assets() {
        cardAtlas = new TextureAtlas(Gdx.files.internal("CardDeck.pack"));
        chipAtlas = new TextureAtlas(Gdx.files.internal("Chips.pack"));

        Texture texture = new Texture(Gdx.files.internal("ArialDistance.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.FONT, texture);

        font = new BitmapFont(Gdx.files.internal("ArialDistance.fnt"), new TextureRegion(textures.get(TEX_NAME.FONT)), false);
        ShaderProgram fontShader = new ShaderProgram(Gdx.files.internal("shaders/distancefield.vert"), Gdx.files.internal("shaders/distancefield.frag"));
        if (!fontShader.isCompiled()) {
            Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
        }

        distanceFieldShader = new DistanceFieldShader();

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        addLinearTexture("Background.png", TEX_NAME.BACKGROUND);
        addLinearTexture("GenericButton.png", TEX_NAME.BUTTON_BLANK);
        addLinearTexture("GenericButtonDown.png", TEX_NAME.BUTTON_BLANK_DOWN);
        addLinearTexture("LeftSide.png", TEX_NAME.LEFT_SIDE);
        addLinearTexture("AnteCircle.png", TEX_NAME.ANTE_CIRCLE);
        addLinearTexture("PairPlusCircle.png", TEX_NAME.PAIRPLUS_CIRCLE);
        addLinearTexture("PlayCircle.png", TEX_NAME.PLAY_CIRCLE);
        addLinearTexture("WinPopup.png", TEX_NAME.WIN_POPUP);
        addLinearTexture("LosePopup.png", TEX_NAME.LOSE_POPUP);
        addLinearTexture("TCPPaytable.png", TEX_NAME.THREE_CARD_POKER_PAYTABLE);
        addLinearTexture("TCPTitle.png", TEX_NAME.THREE_CARD_POKER_TITLE);
        addLinearTexture("QueensUpCircle.png", TEX_NAME.QUEENS_UP_CIRCLE);
        addLinearTexture("SuperBonusCircle.png", TEX_NAME.SUPER_BONUS_CIRCLE);
        addLinearTexture("CFPPaytable.png", TEX_NAME.CRAZY_FOUR_POKER_PAYTABLE);
        addLinearTexture("CFPTitle.png", TEX_NAME.CRAZY_FOUR_POKER_TITLE);
        addLinearTexture("MenuButton.png", TEX_NAME.MENU_BUTTON);
        addLinearTexture("MenuButtonDown.png", TEX_NAME.MENU_BUTTON_DOWN);
        addLinearTexture("SplashTitle.png", TEX_NAME.SPLASH_TITLE);
        addLinearTexture("13stb.png", TEX_NAME.BURN_LOGO);
        addLinearTexture("Black50Alpha.png", TEX_NAME.BLACK_50_ALPHA);
        addLinearTexture("cStudTitle.png", TEX_NAME.CARIBBEAN_STUD_POKER_TITLE);
        addLinearTexture("cStudPaytable.png", TEX_NAME.CARIBBEAN_STUD_PAYTABLE);
        addLinearTexture("BlackjackTitle.png", TEX_NAME.BLACKJACK_TITLE);
        addLinearTexture("BlankCircle.png", TEX_NAME.BLANK_CIRCLE);
        addLinearTexture("Settings.png", TEX_NAME.SETTINGS);
        addLinearTexture("CrapsTable.png", TEX_NAME.CRAPS_TABLE);
        addLinearTexture("CrapsTitle.png", TEX_NAME.CRAPS_TITLE);
        addLinearTexture("DieFace1.png", TEX_NAME.DIEFACE1);
        addLinearTexture("DieFace2.png", TEX_NAME.DIEFACE2);
        addLinearTexture("DieFace3.png", TEX_NAME.DIEFACE3);
        addLinearTexture("DieFace4.png", TEX_NAME.DIEFACE4);
        addLinearTexture("DieFace5.png", TEX_NAME.DIEFACE5);
        addLinearTexture("DieFace6.png", TEX_NAME.DIEFACE6);
        addLinearTexture("OnChip.png", TEX_NAME.CHIP_ON);
        addLinearTexture("OffChip.png", TEX_NAME.CHIP_OFF);
        addLinearTexture("DiceFrames.png", TEX_NAME.DICE_FRAMES);
        addLinearTexture("3xPopup.png", TEX_NAME.THREEX_POPUP);
        addLinearTexture("4xPopup.png", TEX_NAME.FOURX_POPUP);
        addLinearTexture("5xPopup.png", TEX_NAME.FIVEX_POPUP);
        addLinearTexture("6xPopup.png", TEX_NAME.SIXX_POPUP);
        addLinearTexture("RouletteTableAmerican.png", TEX_NAME.ROULETTE_TABLE_AMERICAN);
        addLinearTexture("RouletteTableEuropean.png", TEX_NAME.ROULETTE_TABLE_EUROPEAN);
        addLinearTexture("RouletteWheelEuropean.png", TEX_NAME.ROULETTE_WHEEL_EUROPEAN);
        addLinearTexture("RouletteWheelAmerican.png", TEX_NAME.ROULETTE_WHEEL_AMERICAN);
        addLinearTexture("RouletteWheelBall.png", TEX_NAME.ROULETTE_WHEEL_BALL);
        addLinearTexture("RouletteTitle.png", TEX_NAME.ROULETTE_TITLE);
        addLinearTexture("RouletteNumberBoard.png", TEX_NAME.ROULETTE_NUMBER_BOARD);
        addLinearTexture("StoreButton.png", TEX_NAME.STORE_BUTTON);
        addLinearTexture("Buy5000Chips.png", TEX_NAME.BUY_5000_CHIPS);
        addLinearTexture("Buy25000Chips.png", TEX_NAME.BUY_25000_CHIPS);
        addLinearTexture("Buy100000Chips.png", TEX_NAME.BUY_100000_CHIPS);
        addLinearTexture("Buy500000Chips.png", TEX_NAME.BUY_500000_CHIPS);
        addLinearTexture("Buy5000000Chips.png", TEX_NAME.BUY_5000000_CHIPS);
        addLinearTexture("HelpIcon.png", TEX_NAME.HELP_ICON);
        addLinearTexture("LetItRideTitle.png", TEX_NAME.LET_IT_RIDE_TITLE);
        addLinearTexture("LetItRideCircle1.png", TEX_NAME.LET_IT_RIDE_CIRCLE_1);
        addLinearTexture("LetItRideCircle2.png", TEX_NAME.LET_IT_RIDE_CIRCLE_2);
        addLinearTexture("LetItRideCircleDollarSign.png", TEX_NAME.LET_IT_RIDE_CIRCLE_DOLLAR_SIGN);
        addLinearTexture("LetItRidePaytable.png", TEX_NAME.LET_IT_RIDE_PAYTABLE);
    }
    
    private void addLinearTexture(String fileName, TEX_NAME texName) {
        Texture texture = new Texture(Gdx.files.internal(fileName));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(texName, texture);
    }

    public void dispose() {
        cardAtlas.dispose();
    }

    public TextureRegion getCardTexture(String name) {
        return cardAtlas.findRegion(name);
    }
    public TextureRegion getChipTexture(String amount) { return chipAtlas.findRegion(amount);}

    public BitmapFont getFont() {
        return font;
    }
    public DistanceFieldShader getDistanceFieldShader() {
        return distanceFieldShader;
    }

    public Texture getTexture(TEX_NAME name) {
        return textures.get(name);
    }

    public Skin getSkin() { return uiSkin; }

    private Color backgroundColor = new Color().valueOf("0C530FFF");
    public Color getBackgroundColor(){ return backgroundColor; }

}
