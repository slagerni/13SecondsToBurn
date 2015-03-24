package com.thirteensecondstoburn.Sudoku;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import java.util.Vector;

public class Data {
	public static enum MarkType {NONE, SELECT, REMOVE};

	private DataItem[] items = new DataItem[81];
	private Stack<DataItem[]> history = new Stack<DataItem[]>();
	private Stack<DataItem[]> redoList = new Stack<DataItem[]>();
	private int selectedIndex = -1;
	private int selectedPossibility = -1;
	private boolean[] highlightPossibility = new boolean[9];
	private int[] colors = new int[81];
	int maxColor = 1;
	private boolean suspendEvents = false;
	public String solution;
	private int zoomedTo = -1;
	private boolean selectedPossibilitiesOnly = false;
	private String statusText = "";
    private int rating = 0;
	
	ArrayList<DataListener> listeners = new ArrayList<DataListener>();

	int[][] houses = new int[][] {{0 ,1 ,2 ,9 ,10,11,18,19,20}, {3, 4, 5, 12,13,14,21,22,23}, {6, 7, 8, 15,16,17,24,25,26}, { 
								  27,28,29,36,37,38,45,46,47}, {30,31,32,39,40,41,48,49,50}, {33,34,35,42,43,44,51,52,53}, { 
								  54,55,56,63,64,65,72,73,74}, {57,58,59,66,67,68,75,76,77}, {60,61,62,69,70,71,78,79,80}};
	int[][] rows = new int[][] {{0,1,2,3,4,5,6,7,8},{9,10,11,12,13,14,15,16,17},{18,19,20,21,22,23,24,25,26}, {
								 27,28,29,30,31,32,33,34,35},{36,37,38,39,40,41,42,43,44},{45,46,47,48,49,50,51,52,53}, {
								 54,55,56,57,58,59,60,61,62},{63,64,65,66,67,68,69,70,71},{72,73,74,75,76,77,78,79,80}};		
	int[][] columns = new int[][] {{0,9,18,27,36,45,54,63,72},{1,10,19,28,37,46,55,64,73},{2,11,20,29,38,47,56,65,74}, {
								   3,12,21,30,39,48,57,66,75},{4,13,22,31,40,49,58,67,76},{5,14,23,32,41,50,59,68,77}, {
                                   6,15,24,33,42,51,60,69,78},{7,16,25,34,43,52,61,70,79},{8,17,26,35,44,53,62,71,80}};


	public DataItem[] getItems() {
		return items;
	}

	public int getSelectedIndex() { 
		return selectedIndex; 
	}
	public void setSelectedIndex(int value) {
		selectedIndex = value;
		OnChanged();		
	}

	public int getSelectedPossibility() {
		return selectedPossibility; 
	}
	public void setSelectedPossibility(int value) {
			selectedPossibility = value;
			OnChanged();
	}

	public int getMaxColors() {
		return maxColor - 1;
	}

	public Data() {
		for(int i=0; i<81; ++i) {
			items[i] = new DataItem();
		}
	}

	public Data(Data d) {
		for(int i=0; i<81; ++i) {
			items[i] = new DataItem(d.items[i]);
		}
		this.solution = d.solution;
	}

	// An event that clients can use to be notified whenever the
	// elements of the list change.
	public void addListener(DataListener toAdd) {
		listeners.add(toAdd);
	}

	// Invoke the Changed event; called whenever list changes
	protected void OnChanged() {
		if (listeners.size() > 0 && !suspendEvents) {
			for(DataListener d : listeners) {
				d.OnChange();
			}
		}
	}

	public String getString() {
		String str = "";
		for(int i=0; i<81; ++i) {
			if(items[i].number > 0) {
				str += items[i].number;
			} else {
				str += ".";
			}
		}
		return str;
	}

	public int getNumber(int index) {
		return items[index].number;
	}

	public static int getRow(int index) {
		return index/9;
	}

	public static int getColumn(int index) {
		return index%9;
	}

	public static int getHouse(int index) {
		return (3*((index/9)/3) + (index%9)/3);
	}

	public void setNumber(int index, int num) {
		// @TODO change this back to using a setting
		//setNumber(index, num, Sudoku.settings.AutoRemove);
		setNumber(index, num, true);
	}

	public void setNumber(int index, int num, boolean solveIt) {
		historySave();
		items[index].number = num;

		if(num > 0 && solveIt) {
			// remove possibilities as appropriate
			int row = index/9;
			int col = index%9;
			int blockRow = row/3;
			int blockColumn = col/3;

			for(int i=0; i<9; ++i) {
				items[row*9 + i].possible[num - 1] = false;
				items[col + i*9].possible[num - 1] = false;
				items[(((blockRow*3 + i/3) * 9) + (blockColumn * 3 + i%3))].possible[num - 1] = false;
			}
		}
		OnChanged();
	}

	public void toggleHighlight(int num) {
		highlightPossibility[num] = ! highlightPossibility[num];
		OnChanged();
	}

	public boolean isHighlight(int num) {
		return highlightPossibility[num];
	}

	public void togglePossible(int index, int num) {
		try {
			historySave();
			items[index].possible[num] = !items[index].possible[num];
			OnChanged();
		} catch (Exception ex) {
			//Console.WriteLine(ex.Message);
		}
	}

	public boolean[] getPossible(int index) {
		return items[index].possible;
	}

	public int[] getColors() {
		return colors;
	}

	public int numberOfPossibilities(int index) {
		int count = 0;
		if(items[index].number > 0) return 0;

		for(int i=0; i<9; ++i) {
			if(items[index].possible[i]) count++;
		}
		return count;
	}
	
	public int getOnlyPossibility(int index) {
		int poss = -1;
		if(numberOfPossibilities(index) > 1 ) return poss;
		
		for(int i=0; i<9; ++i) {
			if(items[index].possible[i]) return i;
		}
		return poss;
	}

	public int possibilityCount(int possibility) {
		int count = 0;
		for(int i=0; i<81; ++i) {
			if(items[i].possible[possibility]) count++;
		}
		return count;
	}

	public int unsolvedCount() {
		int count = 0;
		for(int i=0; i<81; ++i) {
			if(items[i].number == 0) {
				count++;
			}
		}
		return count;
	}

	public MarkType getMarked(int index, int num) {
		return items[index].mark[num];
	}

	public void mark(int index, int num, MarkType type) {
		items[index].mark[num] = type;
		OnChanged();
	}

	public void clearMarks() {
		for(int i=0; i<81; ++i) {
			items[i].clearMarks();
		}
		OnChanged();
	}

	public boolean isStartValue(int index) {
		return items[index].isStartValue;
	}

	public void setStartValue(int index, boolean val) {
		items[index].isStartValue = val;
		OnChanged();
	}

	public void historySave() {
		redoList.clear();
		DataItem[] copy = new DataItem[81];
		for(int i=0; i<81; ++i) {
			copy[i] = new DataItem(items[i]);
		}
		history.push(copy);
	}

	public void undo() {
		if(history.size() > 0) {
			DataItem[] old = (DataItem[])history.pop();
			DataItem[] copy = new DataItem[81];
			for(int i=0; i<81; ++i) {
				copy[i] = new DataItem(items[i]);
				items[i] = new DataItem(old[i]);
			}
			redoList.push(copy);
			OnChanged();
		}
	}

	public void redo() {
		if(redoList.size() > 0) {
			DataItem[] copy = new DataItem[81];
			for(int i=0; i<81; ++i) {
				copy[i] = new DataItem(items[i]);
			}
			history.push(copy);

			DataItem[] redoItem = (DataItem[])redoList.pop();
			items = redoItem;
			for(int i=0; i<81; ++i) {
				items[i] = new DataItem(redoItem[i]);
			}
			OnChanged();
		}
	}

	public void startOver() {
		for(int i=0; i<81; ++i) {
			if(!items[i].isStartValue) {
				items[i].number = 0;
				for(int j=0; j<9; j++) {
					items[i].possible[j] = true;
				}
			}
		}

		for(int i=0; i<81; ++i) {
			if(items[i].isStartValue) {
				setNumber(i, items[i].number);
			}
		}
		history.clear();
		redoList.clear();
		historySave();
		OnChanged();
	}

	public int setFromString(String str) {
		suspendEvents = true;
		clear();
		for(int i=0; i<str.length(); ++i) {
			char[] aStr = str.toCharArray();
			if (aStr[i] >= '0' && aStr[i] <= '9') {
				setNumber(i, Integer.parseInt("" + aStr[i]));
				items[i].isStartValue = true;
			}
		}
		int rating = 0;
		try {
			solution = getSolution(str);
			HumanSolver solver = new HumanSolver(new Data(this));
			if(solver.Solve()) {
				rating = solver.rating;
			} else {
				rating = -1;
			}
		} catch (Exception ex) {
			rating = -1;
		}
		history.clear();
		redoList.clear();
		historySave();
		suspendEvents = false;
		// run the update for all cells at once
		OnChanged();
		return rating;
	}

	public void clear() {
		for(int i=0; i<81; ++i) {
			items[i] = new DataItem();
		}
		history.clear();
		redoList.clear();
		OnChanged();
	}

	// used for coloring and displaying coloring
	public void setColors(int num, boolean multicoloring) {
		// clear it out
		colors = new int[81];
		maxColor = 1;
		// loop through all the possibilities for the number
		for(int i=0; i<81; ++i) {
			if(items[i].number <= 0 && items[i].possible[num] && colors[i] == 0) {
				checkForColoring(i, num, maxColor, true);
			}
		}

		if(multicoloring) {
			joinColorStrings();
		}
//		Console.WriteLine("Coloring for " + num + ": ");
//		for(int i=0; i<81; ++i) {
//			Console.Write(colors[i]);
//		}
//		Console.WriteLine();
	}

	// recursive function to find and fill in a coloring chain
	private void checkForColoring(int index, int num, int color, boolean isFirst) {
		int row = getRow(index);
		int col = getColumn(index);
		int house = getHouse(index);

		ArrayList<Integer> conjugates = new ArrayList<Integer>();

		// see if this possibility has a conjugate in it's row
		int foundAt = -1;
		for(int cell = 0; cell<9; ++cell) {
			if(cell != col
				&& items[rows[row][cell]].number <= 0 
				&& items[rows[row][cell]].possible[num]
				) {
				if(foundAt == -1) {
					// this is the first possibility that could be a conjugate
					foundAt = rows[row][cell];
				} else {
					// we've got multiples, just bail
					foundAt = -1;
					break;
				}
			}
		}
		if(foundAt != -1 && colors[foundAt] == 0) {
			conjugates.add(foundAt);
		}

		// see if this possibility has a conjugate in it's column
		foundAt = -1;
		for(int cell = 0; cell<9; ++cell) {
			if(cell != row
				&& items[columns[col][cell]].number <= 0 
				&& items[columns[col][cell]].possible[num]
				) {
				if(foundAt == -1) {
					// this is the first possibility that could be a conjugate
					foundAt = columns[col][cell];
				} else {
					// we've got multiples, just bail
					foundAt = -1;
					break;
				}
			}
		}
		if(foundAt != -1 && colors[foundAt] == 0) {
			conjugates.add(foundAt);
		}

		// see if this possibility has a conjugate in it's house
		foundAt = -1;
		for(int cell = 0; cell<9; ++cell) {
			if(index != houses[house][cell]
				&& items[houses[house][cell]].number <= 0 
				&& items[houses[house][cell]].possible[num]
				) {
				if(foundAt == -1) {
					// this is the first possibility that could be a conjugate
					foundAt = houses[house][cell];
				} else {
					// we've got multiples, just bail
					foundAt = -1;
					break;
				}
			}
		}
		if(foundAt != -1 && colors[foundAt] == 0) {
			conjugates.add(foundAt);
		}

		// do we need to color this index?
		if(!isFirst || conjugates.size() > 0) {
			colors[index] = color;
		}

		// ok, recurse down any conjugates we found?
		if(conjugates.size() > 0) {
			if(isFirst) { maxColor++; }
			for(int i=0; i<conjugates.size(); ++i) {
				checkForColoring(conjugates.get(i), num, -color, false);
			}
		}
	}
	
	private boolean joinColorStrings() {
		// check for each number
		for(int num = 1; num < maxColor - 1; ++num) {
			for(int look = 2; look < maxColor; ++ look) {
				if(look == num) continue;
				boolean hasTrueFalse = false;
				boolean hasFalseTrue = false;
				boolean hasTrueTrue = false;
				boolean hasFalseFalse = false;

				for(int i=0; i<81; ++i) {
					if(colors[i] == num || colors[i] == -num) {
						for(int j=0; j<81; ++j) {
							if(colors[j] == look || colors[j] == -look) {
								// ok, at this point we have a number in the primary String and a number
								// in the secondary String. Lets see if they share a sector
								if(getRow(i) == getRow(j) 
									|| getColumn(i) == getColumn(j)
									|| getHouse(i) == getHouse(j) ) {
									// something matches, which case is this?
									if(colors[i]>0 && colors[j]<0) hasTrueFalse = true;
									if(colors[i]<0 && colors[j]>0) hasFalseTrue = true;
									if(colors[i]>0 && colors[j]>0) hasTrueTrue = true;
									if(colors[i]<0 && colors[j]<0) hasFalseFalse = true;
								}
							}
						} // j
					}
				} // i
				// ok, now that we've compared all possibilities, are any of them weakly joined?
				if((hasTrueFalse && hasFalseTrue) || (hasTrueTrue && hasFalseFalse)) {
					// ok, if it was true/true and false/false we need to flip the look String
					if(hasTrueTrue) {
						for(int i=0; i<81; ++i) {
							if(colors[i] == look || colors[i] == -look) {
								colors[i] = -colors[i];
							}
						}
					}
					// ding ding ding. we have a winner. the Strings can be combined.
					for(int i=0; i<81; ++i) {
						if(colors[i] == look) colors[i] = num;
						if(colors[i] == -look) colors[i] = -num;
						// ok, move any Strings above look's level down one level
						if(colors[i] > look) colors[i]--;
						if(colors[i] < -look) colors[i]++;
					}
					maxColor--;

					return true;
				}
			} // look
		} // num
		return false;
	}

	private String getSolution(String str) {
		SudokuEngine engine = new SudokuEngine();
		return engine.solve(str);
	}
	
	public int getZoom() {
		return zoomedTo;
	}
	
	public void setZoom(int index) {
		zoomedTo = index;
	}
	
	public void resetZoom() {
		zoomedTo = -1;
	}
	
	public boolean isSelectedPossibilitiesOnly() {
		return selectedPossibilitiesOnly;		
	}
	
	public void setSelectedPossibilitiesOnly(boolean value) {
		selectedPossibilitiesOnly = value;
	}
	
	public String getStatusText() {
		return statusText;
	}
	
	public void setStatusText(String value) {
		statusText = value;
	}
	
	public void save() {
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(items);
			oos.flush();
			String toSave = new String(bos.toByteArray(),"ISO-8859-1");
			bos.close();
			oos.close();

			SudokuGame.settings.saveData(toSave);
            SudokuGame.settings.saveRating(rating);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public boolean load() {
		try {
			String dataString = SudokuGame.settings.getSavedData();
			if(dataString == null || dataString.length() == 0) return false;

			ByteArrayInputStream bis = new ByteArrayInputStream(dataString.getBytes("ISO-8859-1"));
			ObjectInputStream ois = new ObjectInputStream(bis);
			items = (DataItem[])ois.readObject();
			bis.close();
			ois.close();

			history.clear();
			redoList.clear();

            rating = SudokuGame.settings.getRating();

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

    public int getRating() {return rating;}
    public void setRating(int value ) {rating = value;}
}
