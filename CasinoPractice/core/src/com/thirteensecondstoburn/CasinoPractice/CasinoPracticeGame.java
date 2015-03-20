package com.thirteensecondstoburn.CasinoPractice;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.thirteensecondstoburn.CasinoPractice.Screens.BlackJackScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.CaribbeanStudPokerScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.CrazyFourPokerScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.MenuScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.SettingsScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.SplashScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.ThreeCardPokerScreen;

public class CasinoPracticeGame extends Game {
    public static int SCREEN_WIDTH = 1920;
    public static int SCREEN_HEIGHT = 1080;
    private Assets assets;
    private SplashScreen splashScreen;
    private MenuScreen menuScreen;
    private SettingsScreen settingsScreen;
    private ThreeCardPokerScreen threeCardPokerScreen;
    private CrazyFourPokerScreen crazyFourPokerScreen;
    private CaribbeanStudPokerScreen caribbeanStudPokerScreen;
    private BlackJackScreen blackJackScreen;
    private float balance;
    private Preferences saveData;

    public static final boolean ALLOW_HINTS = true;
    private boolean showHints = false;
    private boolean actionHints = false;
    private boolean simpleBlackjackHints = false;
    private float blackjackPenetration = .65f;
    private int blackjackDecks = 8;
    private boolean blackjackHitSoft17 = false;
    private int tableMinimum = 5;

    public SplashScreen getSplashScreen() {
        if(splashScreen == null) splashScreen = new SplashScreen(this);
        return splashScreen;
    }

    public MenuScreen getMenuScreen() {
        if(menuScreen == null) menuScreen = new MenuScreen(this);
        return menuScreen;
    }

    public SettingsScreen getSettingsScreen() {
        if(settingsScreen == null) settingsScreen = new SettingsScreen(this);
        return settingsScreen;
    }

    public ThreeCardPokerScreen getThreeCardPokerScreen() {
        if(threeCardPokerScreen == null) threeCardPokerScreen = new ThreeCardPokerScreen(this);
        return threeCardPokerScreen;
    }

    public CrazyFourPokerScreen getCrazyFourPokerScreen() {
        if(crazyFourPokerScreen == null) crazyFourPokerScreen = new CrazyFourPokerScreen(this);
        return crazyFourPokerScreen;
    }

    public CaribbeanStudPokerScreen getCaribbeanStudPokerScreen() {
        if(caribbeanStudPokerScreen == null) caribbeanStudPokerScreen = new CaribbeanStudPokerScreen(this);
        return caribbeanStudPokerScreen;
    }

    public BlackJackScreen getBlackJackScreen() {
        if(blackJackScreen == null) blackJackScreen = new BlackJackScreen(this);
        return blackJackScreen;
    }

    @Override
	public void create () {
        saveData = Gdx.app.getPreferences("com.thirteensecondstoburn.CasinoPractice.saveData");
        balance = saveData.getFloat("balance", 5000.0f);
        if(balance < 5000)
            balance = 5000;

        showHints = saveData.getBoolean("showHints", false);
        actionHints = saveData.getBoolean("actionHints", false);
        blackjackHitSoft17 = saveData.getBoolean(("hitSoft"), false);
        simpleBlackjackHints = saveData.getBoolean(("simpleBjHints"), false);
        blackjackPenetration = saveData.getFloat(("bjPenetration"), .65f);
        blackjackDecks = saveData.getInteger("bjDecks", 8);
        tableMinimum = saveData.getInteger("tableMinimum", 5);

        setScreen(getSplashScreen());
	}

    public void saveSettings() {
        saveData.putFloat("balance", balance);
        saveData.putBoolean("showHints", showHints);
        saveData.putBoolean("actionHints", actionHints);
        saveData.putBoolean("hitSoft", blackjackHitSoft17);
        saveData.putBoolean("simpleBjHints", isSimpleBlackjackHints());
        saveData.putFloat("bjPenetration", blackjackPenetration);
        saveData.putInteger("bjDecks", blackjackDecks);
        saveData.putInteger("tableMinimum", tableMinimum);
        saveData.flush();
    }

    @Override
    public void dispose() {
        super.dispose();
        if(assets != null) try {assets.dispose();} catch (Exception ex) {}
        if(splashScreen != null) try {splashScreen.dispose();} catch (Exception ex) {}
        if(threeCardPokerScreen != null) try {threeCardPokerScreen.dispose();} catch (Exception ex) {}
        if(crazyFourPokerScreen != null) try {crazyFourPokerScreen.dispose();} catch (Exception ex) {}
        if(caribbeanStudPokerScreen != null) try {caribbeanStudPokerScreen.dispose();} catch (Exception ex) {}
        saveSettings();
    }

    public Assets getAssets() {
        if(assets == null) assets = new Assets();
        return assets;
    }

    public void addToBalance(float amount) {
        balance += amount;
    }

    public void subtractFromBalance(long amount) {
        balance -= amount;
    }

    public float getBalance() {
        return balance;
    }

    public boolean usePreActionHints() {
        return ALLOW_HINTS && actionHints;
    }

    public boolean useHintText() {
        return ALLOW_HINTS && showHints;
    }

    public boolean isShowHints() {
        return this.showHints;
    }

    public void setShowHints(boolean value) {
        this.showHints = value;
    }

    public boolean isActionHints() {
        return actionHints;
    }

    public void setActionHints(boolean value) {
        this.actionHints = value;
    }

    public boolean isSimpleBlackjackHints() {
        return simpleBlackjackHints;
    }

    public void setSimpleBlackjackHints(boolean simpleBlackjackHints) {
        this.simpleBlackjackHints = simpleBlackjackHints;
    }

    public float getBlackjackPenetration() {
        return blackjackPenetration;
    }

    public void setBlackjackPenetration(float blackjackPenetration) {
        this.blackjackPenetration = blackjackPenetration;
    }

    public int getBlackjackDecks() {
        return blackjackDecks;
    }

    public void setBlackjackDecks(int blackjackDecks) {
        this.blackjackDecks = blackjackDecks;
    }

    public boolean getBlackjackHitSoft17() {
        return blackjackHitSoft17;
    }

    public void setBlackjackHitSoft17(boolean hit) {
        this.blackjackHitSoft17 = hit;
    }

    public int getTableMinimum() {
        return tableMinimum;
    }

    public void setTableMinimum(int tableMinimum) {
        this.tableMinimum = tableMinimum;
    }

    public int getTableMaximum() { return tableMinimum * 10; }
}
