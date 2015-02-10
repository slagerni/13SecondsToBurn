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
	
	public Settings() {
		prefs = Gdx.app.getPreferences("settings");
		defaultQuickPlay = prefs.getString("defaultQuickPlay", "Any");
	}
	
	public void save() {		
		prefs.putString("defaultQuickPlay", defaultQuickPlay);
		prefs.flush();
	}
	
	public void saveData(String data) {
		
		prefs.putString("savedData", Base64Coder.encodeString(data));
		prefs.flush();
	}
	
	public String getSavedData() {
		return Base64Coder.decodeString(prefs.getString("savedData"));
	}
}
