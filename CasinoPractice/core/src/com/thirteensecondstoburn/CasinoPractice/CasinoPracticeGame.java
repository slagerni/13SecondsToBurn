package com.thirteensecondstoburn.CasinoPractice;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IGoogleServices;
import com.thirteensecondstoburn.CasinoPractice.GooglePlay.IInternalApplicationBilling;
import com.thirteensecondstoburn.CasinoPractice.Screens.BlackJackScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.CaribbeanStudPokerScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.CrapsScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.CrazyFourPokerScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.LetItRideScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.MenuScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.RouletteScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.SettingsScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.SplashScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.StatisticsScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.StoreScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.ThreeCardPokerScreen;
import com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics;
import com.thirteensecondstoburn.CasinoPractice.Statistics.StatisticSerializationModule;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    private LetItRideScreen letItRideScreen;
    private StatisticsScreen statisticsScreen;
    private double balance;
    private double sessionBalance;
    private static TableGame currentGame;

    private Calendar lastDailyChips = null;
    private Preferences saveData;

    private com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics statistics;

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
        currentGame = TableGame.Miscellaneous;
        return splashScreen;
    }

    public MenuScreen getMenuScreen() {
        if(menuScreen == null) menuScreen = new MenuScreen(this);
        currentGame = TableGame.Miscellaneous;
        return menuScreen;
    }

    public SettingsScreen getSettingsScreen() {
        if(settingsScreen == null) settingsScreen = new SettingsScreen(this);
        currentGame = TableGame.Miscellaneous;
        return settingsScreen;
    }

    public StoreScreen getStoreScreen() {
        if(storeScreen == null) storeScreen = new StoreScreen(this);
        currentGame = TableGame.Miscellaneous;
        return storeScreen;
    }

    public ThreeCardPokerScreen getThreeCardPokerScreen() {
        if(threeCardPokerScreen == null) threeCardPokerScreen = new ThreeCardPokerScreen(this);
        currentGame = TableGame.ThreeCardPoker;
        return threeCardPokerScreen;
    }

    public CrazyFourPokerScreen getCrazyFourPokerScreen() {
        if(crazyFourPokerScreen == null) crazyFourPokerScreen = new CrazyFourPokerScreen(this);
        currentGame = TableGame.Crazy4Poker;
        return crazyFourPokerScreen;
    }

    public CaribbeanStudPokerScreen getCaribbeanStudPokerScreen() {
        if(caribbeanStudPokerScreen == null) caribbeanStudPokerScreen = new CaribbeanStudPokerScreen(this);
        currentGame = TableGame.CaribbeanStudPoker;
        return caribbeanStudPokerScreen;
    }

    public BlackJackScreen getBlackJackScreen() {
        if(blackJackScreen == null) blackJackScreen = new BlackJackScreen(this);
        currentGame = TableGame.Blackjack;
        return blackJackScreen;
    }

    public CrapsScreen getCrapsScreen() {
        if(crapsScreen == null) crapsScreen = new CrapsScreen(this);
        currentGame = TableGame.Craps;
        return crapsScreen;
    }

    public RouletteScreen getRouletteScreen() {
        if(rouletteScreen == null) rouletteScreen = new RouletteScreen(this);
        currentGame = TableGame.Roulette;
        return rouletteScreen;
    }

    public LetItRideScreen getLetItRideScreen() {
        if(letItRideScreen == null) letItRideScreen = new LetItRideScreen(this);
        currentGame = TableGame.LetItRide;
        return letItRideScreen;
    }

    public StatisticsScreen getStatisticsScreen() {
        if(statisticsScreen == null) statisticsScreen = new StatisticsScreen(this);
        currentGame = TableGame.Miscellaneous;
        return statisticsScreen;
    }

    @Override
	public void create () {
        // set up the in app billing
        billing.create();

        Gdx.input.setCatchBackKey(true); // handle the back key

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

        String statsString = saveData.getString("statistics");
        if(statsString == null || "null".equals(statsString) || "".equals(statsString)) {
            statistics = new com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics();
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new StatisticSerializationModule());
            try {
                statistics = mapper.readValue(statsString, CasinoPracticeStatistics.class);
            } catch (IOException e) {
                statistics = new com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics();
                e.printStackTrace();
            }
        }

        setScreen(getSplashScreen());
	}

    private void loadEncodedSettings() {
        Json json = new Json();

        String encodedKey = saveData.getString(SAVE_KEY);
        if(encodedKey != null) {
            try {
                EncodedSettings encodedSettings = json.fromJson(EncodedSettings.class, Base64Coder.decodeString(encodedKey));
                balance = encodedSettings.balance;
                lastDailyChips = GregorianCalendar.getInstance();
                lastDailyChips.setTimeInMillis(encodedSettings.lastDailyChips);
            } catch (Exception ex) {
                // corrupt file, assume someone tampered with it
                System.out.println("Error reading JSON settings: " + ex.getMessage());

                balance = 1000;
                lastDailyChips = GregorianCalendar.getInstance();
            }
        } else {
            if(isNewInstall) {
                balance = 4000; // the daily pop will push this up to 5000
            } else {
                balance = 0; // assume someone is messing w/ the config file
            }
            lastDailyChips.add(Calendar.DAY_OF_MONTH, -1); // allow the daily pop to happen
        }
    }

    private static class EncodedSettings {
        public double balance;
        public long lastDailyChips;
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
        encodedSettings.lastDailyChips = lastDailyChips.getTimeInMillis();

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        String encodedJson = Base64Coder.encodeString(json.toJson(encodedSettings));
        saveData.putString(SAVE_KEY, encodedJson);

        ObjectMapper mapper = new ObjectMapper();
        String statString = null;
        try {
            statString = mapper.writeValueAsString(statistics);
            saveData.putString("statistics", statString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

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
        if(letItRideScreen != null) try {letItRideScreen.dispose();} catch (Exception ex) {}
        if(menuScreen != null) try {menuScreen.dispose();} catch (Exception ex) {}
        if(settingsScreen != null) try {settingsScreen.dispose();} catch (Exception ex) {}
        if(storeScreen != null) try {storeScreen.dispose();} catch (Exception ex) {}
        if(statisticsScreen != null) try {statisticsScreen.dispose();} catch (Exception ex) {}
        saveSettings();
        billing.dispose();
    }

    public void resetSessionBalance() {
        sessionBalance = 0;
    }

    public Assets getAssets() {
        if(assets == null) assets = new Assets();
        return assets;
    }

    public void addToBalance(float amount) {
        balance += amount;
        sessionBalance += amount;
        statistics.increment(CasinoPracticeStatistics.Balance, amount);
    }

    public void subtractFromBalance(long amount) {
        addToBalance(-amount);
    }

    public double getBalance() {
        return balance;
    }

    public double getSessionBalance() { return sessionBalance; }

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

    public Calendar getLastDailyChips() {
        return lastDailyChips;
    }

    public void setLastDailyChips(Calendar lastDailyChips) {
        this.lastDailyChips = lastDailyChips;
        saveSettings();
    }

    public boolean isNewInstall() {
        return isNewInstall;
    }

    public void setIsNewInstall(boolean isNewInstall) {
        this.isNewInstall = isNewInstall;
    }

    public com.thirteensecondstoburn.CasinoPractice.Statistics.CasinoPracticeStatistics getStatistics() {
        if(statistics == null) {
            statistics = new CasinoPracticeStatistics();
        }
        return statistics;
    }

    public static TableGame getCurrentGame() {return currentGame;}

    public InputProcessor getBackButtonProcessor(final Stage stage) {
        return getBackButtonProcessor(stage, false);
    }

    public InputProcessor getBackButtonProcessor(final Stage stage, final boolean isMenuScreen) {

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) ) {
                    if(isMenuScreen) {
                        Gdx.app.exit();
                    } else {
                        stage.addAction(Actions.sequence(Actions.fadeOut(0.5f), Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                MenuScreen screen = getMenuScreen();
                                if (screen.getStage() != null) {
                                    screen.getStage().addAction(Actions.fadeIn(0.5f));
                                }
                                stage.clear();
                                setScreen(screen);
                            }
                        })));
                    }
                }
                return false;
            }
        };

        return backProcessor;
    }
}
