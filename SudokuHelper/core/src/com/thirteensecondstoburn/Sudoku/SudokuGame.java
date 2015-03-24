package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Game;

public class SudokuGame extends Game {
    public static boolean IS_FREE_VERSION = true;

	public static Settings settings;
	private SudokuScreen sudokuScreen;
	private SplashScreen splashScreen;
	private MainMenuScreen mainMenuScreen;
	private ManualEntryScreen manualEntryScreen;
    private NagScreen nagScreen;
    private StatisticsScreen statisticsScreen;
	private Assets assets;
	private Data data = new Data();

	public SudokuScreen getSudokuScreen() {
		if(sudokuScreen == null) sudokuScreen = new SudokuScreen(this);
		return sudokuScreen;
	}
	
	public SplashScreen getSplashScreen() {
		if(splashScreen == null) splashScreen = new SplashScreen(this);
		return splashScreen;
	}
	
	public MainMenuScreen getMainMenuScreen() {
		if(mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this);
		return mainMenuScreen;
	}

    public ManualEntryScreen getManualEntryScreen() {
        if(manualEntryScreen == null) manualEntryScreen = new ManualEntryScreen(this);
        return manualEntryScreen;
    }

    public NagScreen getNagScreen(boolean fromHint) {
        if(nagScreen == null) nagScreen = new NagScreen(this);
        nagScreen.setFromHint(fromHint);
        return nagScreen;
    }

    public StatisticsScreen getStatisticsScreen() {
        if(statisticsScreen == null) statisticsScreen = new StatisticsScreen(this);
        return statisticsScreen;
    }

    public Data getData() {
		return data;
	}
	
	
	@Override
	public void create() {		
		settings  = new Settings();
		setScreen(getSplashScreen());
	}

	@Override
	public void dispose() {
        settings.save(); // make sure everything is saved when exiting
		super.dispose();
		if(sudokuScreen != null) sudokuScreen.dispose();
		if(splashScreen != null) splashScreen.dispose();
		if(mainMenuScreen != null) mainMenuScreen.dispose();
		if(manualEntryScreen != null) manualEntryScreen.dispose();
		if(assets != null) assets.dispose();
	}

	@Override
	public void render() {	
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	public Assets getAssets() {
		if(assets == null) assets = new Assets();
		return assets;
	}
}
