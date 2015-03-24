package com.thirteensecondstoburn.Sudoku;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Base64Coder;

public class Settings {
	private Preferences prefs;
	
	public Color[] numberHighlightColors = new Color[] { Color.valueOf("8B4513FF"),
			Color.valueOf("800080FF"), Color.valueOf("FF00FFFF"), Color.BLUE, Color.RED, Color.CYAN,
			Color.YELLOW, Color.valueOf("00FF00FF"), Color.valueOf("FF8C00FF") };
	public Color numberForgroundColor = Color.BLACK;
	public Color cellBackgroundColor = Color.WHITE;
	public Color cellOddBackgroundColor = Color.LIGHT_GRAY;
	public Color houseColor = Color.GREEN;
	public Color backgroundColor = Color.valueOf("ADD8E6");
	public Color singlePossibilityColor = Color.valueOf("F0E68CFF");
	public boolean autoRemove = true;
	public boolean highlightPossibilities = true;
	public boolean highlightSinglePossibilities = true;
	public boolean autoColoring = true;
	public String defaultQuickPlay = "Any";

    // stats
    public int gamesPlayed = 0;
    public int beginnerPlayed = 0;
    public int easyPlayed = 0;
    public int mediumPlayed = 0;
    public int hardPlayed = 0;
    public int beginnerSolved = 0;
    public int easySolved = 0;
    public int mediumSolved = 0;
    public int hardSolved = 0;
    public int hintsUsed = 0;
    public int hardestRatingSolved = -1;
	
	public Settings() {
		prefs = Gdx.app.getPreferences("settings");
		defaultQuickPlay = prefs.getString("defaultQuickPlay", "Any");
        gamesPlayed = prefs.getInteger("gamesPlayed", 0);
        beginnerPlayed = prefs.getInteger("beginnerPlayed", 0);
        easyPlayed = prefs.getInteger("easyPlayed", 0);
        mediumPlayed = prefs.getInteger("mediumPlayed", 0);
        hardPlayed = prefs.getInteger("hardPlayed", 0);
        beginnerSolved = prefs.getInteger("beginnerSolved", 0);
        easySolved = prefs.getInteger("easySolved", 0);
        mediumSolved = prefs.getInteger("mediumSolved", 0);
        hardSolved = prefs.getInteger("hardSolved", 0);
        hintsUsed = prefs.getInteger("hintsUsed", 0);
        hardestRatingSolved = prefs.getInteger("hardestRatingSolved", -1);
	}
	
	public void save() {		
		prefs.putString("defaultQuickPlay", defaultQuickPlay);
        prefs.putInteger("gamesPlayed", gamesPlayed);
        prefs.putInteger("beginnerPlayed", beginnerPlayed);
        prefs.putInteger("easyPlayed", easyPlayed);
        prefs.putInteger("mediumPlayed", mediumPlayed);
        prefs.putInteger("hardPlayed", hardPlayed);
        prefs.putInteger("beginnerSolved", beginnerSolved);
        prefs.putInteger("easySolved", easySolved);
        prefs.putInteger("mediumSolved", mediumSolved);
        prefs.putInteger("hardSolved", hardSolved);
        prefs.putInteger("hintsUsed", hintsUsed);
        prefs.putInteger("hardestRatingSolved", hardestRatingSolved);
		prefs.flush();
	}
	
	public void saveData(String data) {
		
		prefs.putString("savedData", Base64Coder.encodeString(data));
		prefs.flush();
	}

	public String getSavedData() {
		return Base64Coder.decodeString(prefs.getString("savedData"));
	}

    public int getRating() {
        return prefs.getInteger("currentRating", 0);
    }

    public void saveRating(int rating) {
        prefs.putInteger("currentRating", rating);
        prefs.flush();
    }

}
