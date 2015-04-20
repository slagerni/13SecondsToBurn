package com.thirteensecondstoburn.CasinoPractice;

import com.badlogic.gdx.Gdx;
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
        BLANK_CIRCLE, SETTINGS, CRAPS_TABLE, CRAPS_TITLE, DIEFACE1, DIEFACE2, DIEFACE3, DIEFACE4, DIEFACE5, DIEFACE6, CHIP_ON, CHIP_OFF
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

        texture = new Texture(Gdx.files.internal("Background.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.BACKGROUND, texture);

        texture = new Texture(Gdx.files.internal("GenericButton.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.BUTTON_BLANK, texture);

        texture = new Texture(Gdx.files.internal("GenericButtonDown.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.BUTTON_BLANK_DOWN, texture);

        texture = new Texture(Gdx.files.internal("LeftSide.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.LEFT_SIDE, texture);

        texture = new Texture(Gdx.files.internal("AnteCircle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.ANTE_CIRCLE, texture);

        texture = new Texture(Gdx.files.internal("PairPlusCircle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.PAIRPLUS_CIRCLE, texture);

        texture = new Texture(Gdx.files.internal("PlayCircle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.PLAY_CIRCLE, texture);

        texture = new Texture(Gdx.files.internal("WinPopup.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.WIN_POPUP, texture);

        texture = new Texture(Gdx.files.internal("LosePopup.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.LOSE_POPUP, texture);

        texture = new Texture(Gdx.files.internal("TCPPaytable.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.THREE_CARD_POKER_PAYTABLE, texture);

        texture = new Texture(Gdx.files.internal("TCPTitle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.THREE_CARD_POKER_TITLE, texture);

        texture = new Texture(Gdx.files.internal("QueensUpCircle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.QUEENS_UP_CIRCLE, texture);

        texture = new Texture(Gdx.files.internal("SuperBonusCircle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.SUPER_BONUS_CIRCLE, texture);

        texture = new Texture(Gdx.files.internal("CFPPaytable.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.CRAZY_FOUR_POKER_PAYTABLE, texture);

        texture = new Texture(Gdx.files.internal("CFPTitle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.CRAZY_FOUR_POKER_TITLE, texture);

        texture = new Texture(Gdx.files.internal("MenuButton.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.MENU_BUTTON, texture);

        texture = new Texture(Gdx.files.internal("MenuButtonDown.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.MENU_BUTTON_DOWN, texture);

        texture = new Texture(Gdx.files.internal("SplashTitle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.SPLASH_TITLE, texture);

        texture = new Texture(Gdx.files.internal("13stb.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.BURN_LOGO, texture);

        texture = new Texture(Gdx.files.internal("Black50Alpha.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.BLACK_50_ALPHA, texture);

        texture = new Texture(Gdx.files.internal("cStudTitle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.CARIBBEAN_STUD_POKER_TITLE, texture);

        texture = new Texture(Gdx.files.internal("cStudPaytable.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.CARIBBEAN_STUD_PAYTABLE, texture);

        texture = new Texture(Gdx.files.internal("BlackjackTitle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.BLACKJACK_TITLE, texture);

        texture = new Texture(Gdx.files.internal("BlankCircle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.BLANK_CIRCLE, texture);

        texture = new Texture(Gdx.files.internal("Settings.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.SETTINGS, texture);

        texture = new Texture(Gdx.files.internal("CrapsTable.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.CRAPS_TABLE, texture);

        texture = new Texture(Gdx.files.internal("CrapsTitle.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.CRAPS_TITLE, texture);

        texture = new Texture(Gdx.files.internal("DieFace1.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.DIEFACE1, texture);

        texture = new Texture(Gdx.files.internal("DieFace2.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.DIEFACE2, texture);

        texture = new Texture(Gdx.files.internal("DieFace3.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.DIEFACE3, texture);

        texture = new Texture(Gdx.files.internal("DieFace4.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.DIEFACE4, texture);

        texture = new Texture(Gdx.files.internal("DieFace5.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.DIEFACE5, texture);

        texture = new Texture(Gdx.files.internal("DieFace6.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.DIEFACE6, texture);

        texture = new Texture(Gdx.files.internal("OnChip.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.CHIP_ON, texture);

        texture = new Texture(Gdx.files.internal("OffChip.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        textures.put(TEX_NAME.CHIP_OFF, texture);

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
}
