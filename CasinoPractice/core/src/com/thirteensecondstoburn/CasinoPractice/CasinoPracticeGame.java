package com.thirteensecondstoburn.CasinoPractice;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IGoogleServices;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IInternalApplicationBilling;
import com.thirteensecondstoburn.CasinoPractice.Screens.BlackJackScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.CaribbeanStudPokerScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.CrapsScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.CrazyFourPokerScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.MenuScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.RouletteScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.SettingsScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.SplashScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.StoreScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.ThreeCardPokerScreen;

import java.time.ZonedDateTime;

public class CasinoPracticeGame extends Game {
    public static IGoogleServices googleServices;
    public static IInternalApplicationBilling billing;
    public static int SCREEN_WIDTH = 1920;
    public static int SCREEN_HEIGHT = 1080;
    private Assets assets;
    private SplashScreen splashScreen;
    private MenuScreen menuScreen;
    private SettingsScreen settingsScreen;
    private StoreScreen storeScreen;
    private ThreeCardPokerScreen threeCardPokerScreen;
    private CrazyFourPokerScreen crazyFourPokerScreen;
    private CaribbeanStudPokerScreen caribbeanStudPokerScreen;
    private BlackJackScreen blackJackScreen;
    private CrapsScreen crapsScreen;
    private RouletteScreen rouletteScreen;
    private double balance;

    private ZonedDateTime lastDailyChips = null;
    private Preferences saveData;

    private static final String SAVE_KEY = Base64Coder.encodeString("encodedSettings");
    public static final boolean ALLOW_HINTS = true;
    private boolean showHints = false;
    private boolean actionHints = false;
    private boolean simpleBlackjackHints = false;
    private float blackjackPenetration = .65f;
    private int blackjackDecks = 8;
    private boolean blackjackHitSoft17 = false;
    private int tableMinimum = 5;

    private boolean isNewInstall = false;

    private String rouletteType = "European";

    public CasinoPracticeGame(IGoogleServices googleServices, IInternalApplicationBilling billing) {
        super();
        CasinoPracticeGame.googleServices = googleServices;
        CasinoPracticeGame.billing = billing;
    }

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

    public StoreScreen getStoreScreen() {
        if(storeScreen == null) storeScreen = new StoreScreen(this);
        return storeScreen;
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

    public CrapsScreen getCrapsScreen() {
        if(crapsScreen == null) crapsScreen = new CrapsScreen(this);
        return crapsScreen;
    }

    public RouletteScreen getRouletteScreen() {
        if(rouletteScreen == null) rouletteScreen = new RouletteScreen(this);
        return rouletteScreen;
    }

    @Override
	public void create () {
        // set up the in app billing
        billing.create();

        saveData = Gdx.app.getPreferences("com.thirteensecondstoburn.CasinoPractice.saveData");
        balance = Double.parseDouble(saveData.getString("balance", "5000.00"));
        if(balance < 5000)
            balance = 5000;

        showHints = saveData.getBoolean("showHints", false);
        actionHints = saveData.getBoolean("actionHints", false);
        blackjackHitSoft17 = saveData.getBoolean(("hitSoft"), false);
        simpleBlackjackHints = saveData.getBoolean(("simpleBjHints"), false);
        blackjackPenetration = saveData.getFloat(("bjPenetration"), .65f);
        blackjackDecks = saveData.getInteger("bjDecks", 8);
        tableMinimum = saveData.getInteger("tableMinimum", 5);
        rouletteType = saveData.getString("rouletteType");
        isNewInstall = saveData.getBoolean("newInstall");

        loadEncodedSettings();

        setScreen(getSplashScreen());
	}

    private void loadEncodedSettings() {
        Json json = new Json();

        String encodedKey = saveData.getString(SAVE_KEY);
        if(encodedKey != null) {
            try {
                EncodedSettings encodedSettings = json.fromJson(EncodedSettings.class, Base64Coder.decodeString(encodedKey));
                balance = encodedSettings.balance;
                lastDailyChips = ZonedDateTime.parse(encodedSettings.lastDailyChips);
            } catch (Exception ex) {
                // corrupt file, assume someone tampered with it
                System.out.println("Error reading JSON settings: " + ex.getMessage());

                balance = 1000;
                lastDailyChips = ZonedDateTime.now();
            }
        } else {
            if(isNewInstall) {
                balance = 4000; // the daily pop will push this up to 5000
            } else {
                balance = 0; // assume someone is messing w/ the config file
            }
            lastDailyChips = ZonedDateTime.now().minusDays(1); // allow the daily pop to happen
        }
    }

    private static class EncodedSettings {
        public double balance;
        public String lastDailyChips;
    }

    public void saveSettings() {
        saveData.putBoolean("showHints", showHints);
        saveData.putBoolean("actionHints", actionHints);
        saveData.putBoolean("hitSoft", blackjackHitSoft17);
        saveData.putBoolean("simpleBjHints", isSimpleBlackjackHints());
        saveData.putFloat("bjPenetration", blackjackPenetration);
        saveData.putInteger("bjDecks", blackjackDecks);
        saveData.putInteger("tableMinimum", tableMinimum);
        saveData.putString("rouletteType", rouletteType);
        saveData.putBoolean("newInstall", false);

        EncodedSettings encodedSettings = new EncodedSettings();
        encodedSettings.balance = balance;
        encodedSettings.lastDailyChips = lastDailyChips.toString();

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        String encodedJson = Base64Coder.encodeString(json.toJson(encodedSettings));
        saveData.putString(SAVE_KEY, encodedJson);

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
        if(blackJackScreen != null) try {blackJackScreen.dispose();} catch (Exception ex) {}
        if(crapsScreen != null) try {crapsScreen.dispose();} catch (Exception ex) {}
        if(rouletteScreen != null) try {rouletteScreen.dispose();} catch (Exception ex) {}
        if(menuScreen != null) try {menuScreen.dispose();} catch (Exception ex) {}
        if(settingsScreen != null) try {settingsScreen.dispose();} catch (Exception ex) {}
        if(storeScreen != null) try {storeScreen.dispose();} catch (Exception ex) {}
        saveSettings();
        billing.dispose();
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

    public double getBalance() {
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

    public int getTableMaximum() { return tableMinimum * 100; }

    public String getRouletteType() {
        return rouletteType;
    }

    public void setRouletteType(String rouletteType) {
        this.rouletteType = rouletteType;
    }

    public ZonedDateTime getLastDailyChips() {
        return lastDailyChips;
    }

    public void setLastDailyChips(ZonedDateTime lastDailyChips) {
        this.lastDailyChips = lastDailyChips;
        saveSettings();
    }

    public boolean isNewInstall() {
        return isNewInstall;
    }

    public void setIsNewInstall(boolean isNewInstall) {
        this.isNewInstall = isNewInstall;
    }
}
