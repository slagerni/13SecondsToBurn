package com.thirteensecondstoburn.CasinoPractice;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.thirteensecondstoburn.CasinoPractice.Screens.CrazyFourPokerScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.MenuScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.SplashScreen;
import com.thirteensecondstoburn.CasinoPractice.Screens.ThreeCardPokerScreen;

public class CasinoPracticeGame extends Game {
    public static int SCREEN_WIDTH = 1920;
    public static int SCREEN_HEIGHT = 1080;
    private Assets assets;
    private SplashScreen splashScreen;
    private MenuScreen menuScreen;
    private ThreeCardPokerScreen threeCardPokerScreen;
    private CrazyFourPokerScreen crazyFourPokerScreen;
    private int balance;
    private Preferences saveData;

    public SplashScreen getSplashScreen() {
        if(splashScreen == null) splashScreen = new SplashScreen(this);
        return splashScreen;
    }

    public MenuScreen getMenuScreen() {
        if(menuScreen == null) menuScreen = new MenuScreen(this);
        return menuScreen;
    }

    public ThreeCardPokerScreen getThreeCardPokerScreen() {
        if(threeCardPokerScreen == null) threeCardPokerScreen = new ThreeCardPokerScreen(this);
        return threeCardPokerScreen;
    }

    public CrazyFourPokerScreen getCrazyFourPokerScreen() {
        if(crazyFourPokerScreen == null) crazyFourPokerScreen = new CrazyFourPokerScreen(this);
        return crazyFourPokerScreen;
    }

    @Override
	public void create () {
        saveData = Gdx.app.getPreferences("com.thirteensecondstoburn.CasinoPractice.saveData");
        if(saveData.contains("balance")) {
            balance = saveData.getInteger("balance");
            if(balance < 5000)
                balance = 5000;
        } else {
            balance = 5000;
        }
        setScreen(getSplashScreen());
	}

    @Override
    public void dispose() {
        super.dispose();
        if(assets != null) try {assets.dispose();} catch (Exception ex) {}
        if(splashScreen != null) try {splashScreen.dispose();} catch (Exception ex) {}
        if(threeCardPokerScreen != null) try {threeCardPokerScreen.dispose();} catch (Exception ex) {}
        if(crazyFourPokerScreen != null) try {crazyFourPokerScreen.dispose();} catch (Exception ex) {}
        saveData.putInteger("balance", balance);
        saveData.flush();
    }

    public Assets getAssets() {
        if(assets == null) assets = new Assets();
        return assets;
    }

    public void addToBalance(long amount) {
        balance += amount;
    }

    public void subtractFromBalance(long amount) {
        balance -= amount;
    }

    public long getBalance() {
        return balance;
    }
}
