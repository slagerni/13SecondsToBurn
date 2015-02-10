package com.thirteensecondstoburn.Sudoku;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class HumanSolver {
	public static int[][] houses = new int[][] {
			{ 0, 1, 2, 9, 10, 11, 18, 19, 20 },
			{ 3, 4, 5, 12, 13, 14, 21, 22, 23 },
			{ 6, 7, 8, 15, 16, 17, 24, 25, 26 },
			{ 27, 28, 29, 36, 37, 38, 45, 46, 47 },
			{ 30, 31, 32, 39, 40, 41, 48, 49, 50 },
			{ 33, 34, 35, 42, 43, 44, 51, 52, 53 },
			{ 54, 55, 56, 63, 64, 65, 72, 73, 74 },
			{ 57, 58, 59, 66, 67, 68, 75, 76, 77 },
			{ 60, 61, 62, 69, 70, 71, 78, 79, 80 } };
	public static int[][] rows = new int[][] { { 0, 1, 2, 3, 4, 5, 6, 7, 8 },
			{ 9, 10, 11, 12, 13, 14, 15, 16, 17 },
			{ 18, 19, 20, 21, 22, 23, 24, 25, 26 },
			{ 27, 28, 29, 30, 31, 32, 33, 34, 35 },
			{ 36, 37, 38, 39, 40, 41, 42, 43, 44 },
			{ 45, 46, 47, 48, 49, 50, 51, 52, 53 },
			{ 54, 55, 56, 57, 58, 59, 60, 61, 62 },
			{ 63, 64, 65, 66, 67, 68, 69, 70, 71 },
			{ 72, 73, 74, 75, 76, 77, 78, 79, 80 } };
	public static int[][] columns = new int[][] {
			{ 0, 9, 18, 27, 36, 45, 54, 63, 72 },
			{ 1, 10, 19, 28, 37, 46, 55, 64, 73 },
			{ 2, 11, 20, 29, 38, 47, 56, 65, 74 },
			{ 3, 12, 21, 30, 39, 48, 57, 66, 75 },
			{ 4, 13, 22, 31, 40, 49, 58, 67, 76 },
			{ 5, 14, 23, 32, 41, 50, 59, 68, 77 },
			{ 6, 15, 24, 33, 42, 51, 60, 69, 78 },
			{ 7, 16, 25, 34, 43, 52, 61, 70, 79 },
			{ 8, 17, 26, 35, 44, 53, 62, 71, 80 } };
	private Data data;

	private static ArrayList<int[]> subset2 = new ArrayList<int[]>();
	private static ArrayList<int[]> subset3 = new ArrayList<int[]>();
	private static ArrayList<int[]> subset4 = new ArrayList<int[]>();

	public int singleNakedCandidateCount = 0;
	public int SingleCandidateCount = 0;
	public int singleSectorCandidateCount = 0;
	public int nakedPairCount = 0;
	public int hiddenPairCount = 0;
	public int nakedSubsetCount = 0;
	public int hiddenSubsetCount = 0;
	public int xWingCount = 0;
	public int swordfishCount = 0;
	public int coloringCount = 0;
	public int multicoloringCount = 0;
	public int xyWingCount = 0;
	public int guessCount = 0;
	public int easyCount = 0;
	public int mediumCount = 0;
	public int hardCount = 0;
	public int rating = 0;

	boolean useSingleSector = true;
	boolean useNakedPair = true;
	boolean useHiddenPair = true;
	boolean useNakedSubset = true;
	boolean useHiddenSubset = true;
	boolean useXWing = true;
	boolean useSwordfish = true;
	boolean useColoring = true;
	boolean useMulticoloring = true;
	boolean useXYWing = true;
	boolean useGuess = true;

	public HumanSolver(Data data) {
		this.data = data;
		if (subset2.size() == 0) {
			fillSubsetPossibilities();
		}
	}

	public HumanSolver(Data data, boolean useSingleSector,
			boolean useNakedPair, boolean useHiddenPair,
			boolean useNakedSubset, boolean useHiddenSubset, boolean useXWing,
			boolean useSwordfish, boolean useColoring,
			boolean useMulticoloring, boolean useXYWing, boolean useGuess) {
		this.data = data;
		if (subset2.size() == 0) {
			fillSubsetPossibilities();
		}
		this.useSingleSector = useSingleSector;
		this.useNakedPair = useNakedPair;
		this.useHiddenPair = useHiddenPair;
		this.useNakedSubset = useNakedSubset;
		this.useHiddenSubset = useHiddenSubset;
		this.useXWing = useXWing;
		this.useSwordfish = useSwordfish;
		this.useColoring = useColoring;
		this.useMulticoloring = useMulticoloring;
		this.useXYWing = useXYWing;
		this.useGuess = useGuess;
	}

	public boolean Solve() {
		rating = 0;
		while (!"I got nothing".equals(NextMove(true)))
			;
		for (int i = 0; i < 81; ++i) {
			if (data.getItems()[i].number == 0) {
				return false;
			}
		}
		return true;
	}

	// techniques found at
	// http://www.simes.clara.co.uk/programs/sudokutechniques.htm
	public String NextMove(boolean solveIt) {
		data.clearMarks();
		String message = null;

		message = singleNakedCandidate(solveIt);
		if (message != null) {
			message = "Single Naked Candidate";
			singleNakedCandidateCount++;
			easyCount++;
		}

		if (message == null) {
			message = SingleCandidate(solveIt);
			if (message != null) {
				message = "Single Candidate";
				SingleCandidateCount++;
				easyCount++;
			}
		}

		if (message == null && useSingleSector) {
			message = singleSectorCandidate(solveIt);
			if (message != null) {
				// message = "Single Sector Candidate: " + message;
				message = "Single Sector Candidate";
				singleSectorCandidateCount++;
				mediumCount++;
				// rating += 1;
			}
		}

		if (message == null && useXWing) {
			message = xWing(solveIt);
			if (message != null) {
				message = "X-Wing";
				xWingCount++;
				mediumCount++;
				// rating += 3;
			}
		}

		if (message == null && useNakedPair) {
			message = nakedPair(solveIt);
			if (message != null) {
				// message = "Naked Pair: " + message;
				message = "Naked Pair";
				nakedPairCount++;
				mediumCount++;
				// rating += 3;
			}
		}

		if (message == null && useColoring) {
			message = coloring(solveIt, false);
			if (message != null) {
				message = "Coloring";
				coloringCount++;
				hardCount++;
				// rating += 10;
			}
		}

		if (message == null && useSwordfish) {
			message = swordfish(solveIt);
			if (message != null) {
				message = "Swordfish";
				swordfishCount++;
				hardCount++;
				// rating += 15;
			}
		}

		if (message == null && useHiddenPair) {
			message = hiddenPair(solveIt);
			if (message != null) {
				// message = "Hidden Pair: " + message;
				message = "Hidden Pair";
				hiddenPairCount++;
				hardCount++;
				// rating += 20;
			}
		}

		if (message == null && useNakedSubset) {
			message = nakedSubset(solveIt);
			if (message != null) {
				// message = "Naked Subset " + message;
				message = "Naked Subset";
				nakedSubsetCount++;
				hardCount++;
				// rating += 20;
			}
		}

		if (message == null && useHiddenSubset) {
			message = hiddenSubset(solveIt);
			if (message != null) {
				// message = "Hidden Subset: " + message;
				message = "Hidden Subset";
				hiddenSubsetCount++;
				hardCount++;
				// rating += 30;
			}
		}

		if (message == null && useMulticoloring) {
			message = coloring(solveIt, true); // multicoloring
			if (message != null) {
				message = "Multicoloring";
				multicoloringCount++;
				hardCount++;
				// rating += 30;
			}
		}

		if (message == null && useXYWing) {
			message = xyWing(solveIt);
			if (message != null) {
				message = "XY-Wing";
				xyWingCount++;
				hardCount++;
				// rating += 50;
			}
		}

		if (message == null && useGuess) {
			message = guess(solveIt);
			if (message != null) {
				message = "Guess";
				guessCount++;
				hardCount++;
				rating += 500;
			}
		}

		if (message == null) {
			message = "I got nothing";
		}
		return message;
	}

	// naked single candidates have all the other possibilities removed already
	// so there is no need to check houses, rows or columns
	private String singleNakedCandidate(boolean solveIt) {
		// loop through all the indexex
		for (int i = 0; i < 81; i++) {
			// if this cell still has possibilities, check if it's the only
			// value
			if (data.getItems()[i].number == 0) {
				boolean[] possible = data.getItems()[i].possible;
				int count = 0;
				int single = -1;

				// loop through the possibilities
				for (int j = 0; j < 9; ++j) {
					if (possible[j]) {
						count++;
						// save it incase it's a single candidate
						single = j;
					}
				}
				// if it is a single, set the number and return a message
				if (count == 1) {
					// data.setNumber(i, single + 1);
					if (solveIt) {
						data.setNumber(i, single + 1, true);
					} else {
						data.mark(i, single, Data.MarkType.SELECT);
					}

					// Debug.WriteLine("Naked candidate " + (single + 1)+
					// " found for index " + i);
					return "Naked candidate " + (single + 1)
							+ " found for index " + i;
				}
			}
		}
		return null;
	}

	// single candidates are hiding w/ other possibilities in their cell.
	// However, they are unique to either their house, row or column.
	private String SingleCandidate(boolean solveIt) {
		for (int hou = 0; hou < 9; ++hou) { // check each house
			for (int num = 0; num < 9; ++num) {// check for each number
				int count = 0;
				int foundAt = 0;
				for (int i = 0; i < 9; ++i) { // loop through all the indexes
					if (data.getItems()[houses[hou][i]].number == 0
							&& data.getItems()[houses[hou][i]].possible[num]) {
						count++;
						foundAt = i;
					}
				}
				if (count == 1) {
					if (solveIt) {
						data.setNumber(houses[hou][foundAt], num + 1, true);
					} else {
						data.mark(houses[hou][foundAt], num,
								Data.MarkType.SELECT);
					}
					// Debug.WriteLine("Single candidate " + (num + 1)+
					// " found for index " + houses[hou, foundAt]);
					return "Single candidate " + (num + 1)
							+ " found for index " + houses[hou][foundAt];
				}
			}
		}

		for (int row = 0; row < 9; ++row) { // check each row
			for (int num = 0; num < 9; ++num) {// check for each number
				int count = 0;
				int foundAt = 0;
				for (int i = 0; i < 9; ++i) { // loop through all the indexes
					if (data.getItems()[rows[row][i]].number == 0
							&& data.getItems()[rows[row][i]].possible[num]) {
						count++;
						foundAt = i;
					}
				}
				if (count == 1) {
					if (solveIt) {
						data.setNumber(rows[row][foundAt], num + 1, true);
					} else {
						data.mark(rows[row][foundAt], num, Data.MarkType.SELECT);
					}
					// Debug.WriteLine("Single candidate " + (num + 1)+
					// " found for index " + rows[row, foundAt]);
					return "Single candidate " + (num + 1)
							+ " found for index " + rows[row][foundAt];
				}
			}
		}

		for (int col = 0; col < 9; ++col) { // check each column
			for (int num = 0; num < 9; ++num) {// check for each number
				int count = 0;
				int foundAt = 0;
				for (int i = 0; i < 9; ++i) { // loop through all the indexes
					if (data.getItems()[columns[col][i]].number == 0
							&& data.getItems()[columns[col][i]].possible[num]) {
						count++;
						foundAt = i;
					}
				}
				if (count == 1) {
					if (solveIt) {
						data.setNumber(columns[col][foundAt], num + 1, true);
					} else {
						data.mark(columns[col][foundAt], num,
								Data.MarkType.SELECT);
					}
					// Debug.WriteLine("Single candidate " + (num + 1)+
					// " found for index " + columns[col, foundAt]);
					return "Single candidate " + (num + 1)
							+ " found for index " + columns[col][foundAt];
				}
			}
		}
		return null;
	}

	// A row or column in which the candidate cells all lie within a single
	// house
	// that contains other candidate calls, whereupon the strategy will
	// eliminate
	// the other candidate cells in the house.
	// Or a house in which the candidate cells all lie within a single row or
	// column that contains other candidate cells, whereupon the strategy will
	// eliminate the other candidate cells in the row or column.
	// 94.8.26.......4.5..3......8....17......4.....78....4..2..1......18.2...6...6.92..
	private String singleSectorCandidate(boolean solveIt) {
		// for each number
		for (int num = 0; num < 9; ++num) {
			// ok, lets start by looping through each row and see if all of a
			// number are in the same house
			for (int row = 0; row < 9; ++row) {
				int house = -1;
				// loop through each cell in the row
				for (int cell = 0; cell < 9; ++cell) {
					if (data.getItems()[rows[row][cell]].number == 0
							&& data.getItems()[rows[row][cell]].possible[num]) {
						if (house == -1) {
							house = cell / 3;
						} else {
							if (house != cell / 3) {
								// multiple houses have this number
								house = -2;
								break;
							}
						}
					}
				}
				if (house > -1) {
					// ok, this has potential. Check house for removals
					String removed = null;
					// find the real house
					house += (row / 3) * 3;
					for (int i = 0; i < 9; ++i) {
						// don't try to remove out of the same row
						if (i / 3 != row % 3) {
							if (data.getItems()[houses[house][i]].number == 0
									&& data.getItems()[houses[house][i]].possible[num]) {
								rating += 1;
								// Debug.WriteLine("Rating singleSectorCandidate: "
								// + rating);
								removed += "removed " + (num + 1)
										+ " from index " + houses[house][i]
										+ " ";
								if (solveIt) {
									data.togglePossible(houses[house][i], num);
								} else {
									data.mark(houses[house][i], num,
											Data.MarkType.REMOVE);
								}
							}
						}
					}
					if (removed != null) {
						// Debug.WriteLine("Single Sector Candidate " +
						// removed);
						return removed;
						// return (num + 1) + " in row " + (row + 1) +
						// " is a candidate?";
					}
				}
			}

			// next we loop through each column and see if all of a number are
			// in the same house
			for (int col = 0; col < 9; ++col) {
				int house = -1;
				// loop through each cell in the row
				for (int cell = 0; cell < 9; ++cell) {
					if (data.getItems()[columns[col][cell]].number == 0
							&& data.getItems()[columns[col][cell]].possible[num]) {
						if (house == -1) {
							house = cell / 3;
						} else {
							if (house != cell / 3) {
								// multiple houses have this number
								house = -2;
								break;
							}
						}
					}
				}
				if (house > -1) {
					// ok, this has potential. Check house for removals
					String removed = null;
					// find the real house
					house = (col / 3) + house * 3;
					for (int i = 0; i < 9; ++i) {
						// don't try to remove out of the same row
						if (i % 3 != col % 3) {
							if (data.getItems()[houses[house][i]].number == 0
									&& data.getItems()[houses[house][i]].possible[num]) {
								removed += "column " + (col + 1) + " house "
										+ (house + 1) + " ";
								removed += "removed " + (num + 1)
										+ " from index " + houses[house][i]
										+ " ";
								if (solveIt) {
									data.togglePossible(houses[house][i], num);
								} else {
									data.mark(houses[house][i], num,
											Data.MarkType.REMOVE);
								}
							}
						}
					}
					if (removed != null) {
						// Debug.WriteLine("Single Sector Candidate " +
						// removed);
						return removed;
						// return (num + 1) + " in row " + (row + 1) +
						// " is a candidate?";
					}
				}
			}

			// time to check to see if all of a number lie in the same row in a
			// house
			// if so, we can remove the number from the rest of the row
			for (int house = 0; house < 9; ++house) {
				int row = -1;
				// loop through each cell in the house
				for (int cell = 0; cell < 9; ++cell) {
					if (data.getItems()[houses[house][cell]].number == 0
							&& data.getItems()[houses[house][cell]].possible[num]) {
						if (row == -1) {
							row = cell / 3;
						} else {
							if (row != cell / 3) {
								// multiple rows have this number
								row = -2;
								break;
							}
						}
					}
				}
				if (row > -1) {
					// ok, this has potential. Check house for removals
					String removed = null;
					// find the real house
					row += house / 3 * 3;
					for (int i = 0; i < 9; ++i) {
						// don't try to remove out of the same row
						if (i / 3 != house % 3) {
							if (data.getItems()[rows[row][i]].number == 0
									&& data.getItems()[rows[row][i]].possible[num]) {
								removed += "house " + (house + 1) + " row "
										+ (row + 1) + " ";
								removed += "removed " + (num + 1)
										+ " from index " + rows[row][i] + " ";
								if (solveIt) {
									data.togglePossible(rows[row][i], num);
								} else {
									data.mark(rows[row][i], num,
											Data.MarkType.REMOVE);
								}
							}
						}
					}
					if (removed != null) {
						// Debug.WriteLine("Single Sector Candidate " +
						// removed);
						return removed;
					}
				}
			}

			// last, check to see if all of a number lie in the same column in a
			// house
			// if so, we can remove the number from the rest of the column
			for (int house = 0; house < 9; ++house) {
				int col = -1;
				// loop through each cell in the house
				for (int cell = 0; cell < 9; ++cell) {
					if (data.getItems()[houses[house][cell]].number == 0
							&& data.getItems()[houses[house][cell]].possible[num]) {
						if (col == -1) {
							col = cell % 3;
						} else {
							if (col != cell % 3) {
								// multiple rows have this number
								col = -2;
								break;
							}
						}
					}
				}
				if (col > -1) {
					// ok, this has potential. Check column for removals
					String removed = null;
					// find the real house
					col += house % 3 * 3;
					for (int i = 0; i < 9; ++i) {
						// don't try to remove out of the same house
						if (i / 3 != house / 3) {
							if (data.getItems()[columns[col][i]].number == 0
									&& data.getItems()[columns[col][i]].possible[num]) {
								removed += "house " + (house + 1) + " col "
										+ (col + 1) + " ";
								removed += "removed " + (num + 1)
										+ " from index " + columns[col][i]
										+ " ";
								if (solveIt) {
									data.togglePossible(columns[col][i], num);
								} else {
									data.mark(columns[col][i], num,
											Data.MarkType.REMOVE);
								}
							}
						}
					}
					if (removed != null) {
						// Debug.WriteLine("Single Sector Candidate " +
						// removed);
						return removed;
					}
				}
			}
		}
		return null;
	}

	// fill in the sets of all possibilities to look for when dealing w/ subset
	// sets
	private void fillSubsetPossibilities() {
		// set up for 2
		for (int i = 0; i < 8; ++i) {
			for (int j = 1; j < 9; ++j) {
				if (i < j) {
					subset2.add(new int[] { i, j });
				}
			}
		}

		// set up for 3
		for (int i = 0; i < 7; ++i) {
			for (int j = 1; j < 8; ++j) {
				for (int k = 2; k < 9; ++k) {
					if (i < j && j < k) {
						subset3.add(new int[] { i, j, k });
					}
				}
			}
		}

		// set up for 4
		for (int i = 0; i < 6; ++i) {
			for (int j = 1; j < 7; ++j) {
				for (int k = 2; k < 8; ++k) {
					for (int l = 3; l < 9; ++l) {
						if (i < j && j < k && k < l) {
							subset4.add(new int[] { i, j, k, l });
						}
					}
				}
			}
		}
	}

	// Naked Pair
	// .......5.6.....7.47...2...387....6....34........289..1.9.....8..2.3.1......9....5
	private String nakedPair(boolean solveIt) {
		String message = "";

		for (int i = 0; i < subset2.size(); ++i) {
			int[] poss = (int[]) subset2.get(i);
			message = checkNakedPair(rows, poss, solveIt);
			if (message.length() == 0) {
				message = checkNakedPair(columns, poss, solveIt);
			}
			if (message.length() == 0) {
				message = checkNakedPair(houses, poss, solveIt);
			}
			if (message.length() > 0)
				return message;
		}
		return null;
	}

	// Hidden Pair
	// 67..8......2...5....3.....9..97.26.5...439..8........2..6...93.5..........1.47...
	private String hiddenPair(boolean solveIt) {
		String message = "";

		for (int i = 0; i < subset2.size(); ++i) {
			int[] poss = (int[]) subset2.get(i);
			message = checkHiddenPair(rows, poss, solveIt);
			if (message.length() == 0) {
				message = checkHiddenPair(columns, poss, solveIt);
			}
			if (message.length() == 0) {
				message = checkHiddenPair(houses, poss, solveIt);
			}
			if (message.length() > 0)
				return message;
		}
		return null;
	}

	// Naked Subsets strategy looks for a sector (i.e. a row, column or house)
	// where
	// some subset of values (of size greater than two) is constrained to a
	// set of cells of the same size, where there aren't any other other
	// possibilities
	// in those cells. These entries can be removed from all other cells then
	// for the sector
	private String nakedSubset(boolean solveIt) {
		String message = "";

		for (int i = 0; i < subset3.size(); ++i) {
			int[] poss = (int[]) subset3.get(i);
			message = CheckNakedSubset3(rows, poss, solveIt);
			if (message.length() == 0) {
				message = CheckNakedSubset3(columns, poss, solveIt);
			}
			if (message.length() == 0) {
				message = CheckNakedSubset3(houses, poss, solveIt);
			}
			if (message.length() > 0)
				return message;
		}

		for (int i = 0; i < subset4.size(); ++i) {
			int[] poss = (int[]) subset4.get(i);
			message = CheckNakedSubset4(rows, poss, solveIt);
			if (message.length() == 0) {
				message = CheckNakedSubset4(columns, poss, solveIt);
			}
			if (message.length() == 0) {
				message = CheckNakedSubset4(houses, poss, solveIt);
			}
			if (message.length() > 0)
				return message;
		}

		return null;
	}

	// Hidden Subsets strategy looks for a sector (i.e. a row, column or house)
	// where
	// some subset of values (of size greater than two) is constrained to a
	// set of cells of the same size, whereupon it will eliminate any other
	// candidates
	// from these cells.
	private String hiddenSubset(boolean solveIt) {
		String message = "";

		for (int i = 0; i < subset3.size(); ++i) {
			int[] poss = (int[]) subset3.get(i);
			message = checkHiddenSubset3(rows, poss, solveIt);
			if (message.length() == 0) {
				message = checkHiddenSubset3(columns, poss, solveIt);
			}
			if (message.length() == 0) {
				message = checkHiddenSubset3(houses, poss, solveIt);
			}
			if (message.length() > 0)
				return message;
		}

		for (int i = 0; i < subset4.size(); ++i) {
			int[] poss = (int[]) subset4.get(i);
			message = checkHiddenSubset4(rows, poss, solveIt);
			if (message.length() == 0) {
				message = checkHiddenSubset4(columns, poss, solveIt);
			}
			if (message.length() == 0) {
				message = checkHiddenSubset4(houses, poss, solveIt);
			}
			if (message.length() > 0)
				return message;
		}

		return null;
	}

	// given the possibility set, check if there is a subset subset and can
	// remove
	// some possibilities in that set for pairs
	private String checkHiddenPair(int[][] subSet, int[] poss, boolean solveIt) {
		String message = "";
		ArrayList<Integer> foundAt = new ArrayList<Integer>();
		// subSet first
		for (int pos = 0; pos < 9; ++pos) {
			int allCount = 0;
			foundAt.clear();
			boolean found1 = false;
			boolean found2 = false;
			for (int cell = 0; cell < 9; ++cell) {
				if (data.getItems()[subSet[pos][cell]].number == 0) {
					// if we find one, up the count
					if (data.getItems()[subSet[pos][cell]].possible[poss[0]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[1]]) {
						if (data.getItems()[subSet[pos][cell]].possible[poss[0]])
							found1 = true;
						if (data.getItems()[subSet[pos][cell]].possible[poss[1]])
							found2 = true;
						allCount++;
						foundAt.add(subSet[pos][cell]);
					}
				}
			}
			// if this really is a subset subsubSet, can we remove anything?
			if (allCount == 2 && found1 && found2) {

				// ok, only these cells have this subSets values. Anything else
				// to remove?
				int found = foundAt.get(0);
				int bonusRating = 0;
				for (int i = 0; i < 9; ++i) {
					if (i != poss[0] && i != poss[1]
							&& data.getItems()[found].possible[i]) {
						bonusRating += 3;
						if (solveIt) {
							data.togglePossible(found, i);
						} else {
							data.mark(found, i, Data.MarkType.REMOVE);
						}
						// Debug.WriteLine("Hidden Pair (" + (poss[0] + 1) + ","
						// + (poss[1] + 1) + ") at [" + foundAt[0] + ", " +
						// foundAt[1] + "]. Removed " + (i + 1));
						message += "Removed " + (i + 1) + " from " + found
								+ " ";
					}
				}
				found = (int) foundAt.get(1);
				for (int i = 0; i < 9; ++i) {
					if (i != poss[0] && i != poss[1]
							&& data.getItems()[found].possible[i]) {
						bonusRating += 3;
						if (solveIt) {
							data.togglePossible(found, i);
						} else {
							data.mark(found, i, Data.MarkType.REMOVE);
						}
						// Debug.WriteLine("Hidden Pair (" + (poss[0] + 1) + ","
						// + (poss[1] + 1) + ") at [" + foundAt[0] + ", " +
						// foundAt[1] + "]. Removed " + (i + 1));
						message += "Removed " + (i + 1) + " from " + found
								+ " ";
					}
				}
				if (message.length() > 0) {
					rating += (data.unsolvedCount() / 6 + bonusRating);
					// Debug.WriteLine("Rating checkHiddenPair: " + rating);
					return message;
				}
			}

		}

		return message;
	}

	// given the possibility set, check if there is a naked subset and can
	// remove
	// some possibilities in that set for pairs
	private String checkNakedPair(int[][] subSet, int[] poss, boolean solveIt) {
		String message = "";
		ArrayList<Integer> foundAt = new ArrayList<Integer>();
		// subSet first
		for (int pos = 0; pos < 9; ++pos) {
			int onlyCount = 0;
			foundAt.clear();
			for (int cell = 0; cell < 9; ++cell) {
				if (data.getItems()[subSet[pos][cell]].number == 0) {
					if (data.getItems()[subSet[pos][cell]].possible[poss[0]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[1]]) {
						boolean possible = true;
						for (int i = 0; i < 9; ++i) {
							if (data.getItems()[subSet[pos][cell]].possible[i]
									&& !(poss[0] == i || poss[1] == i)) {
								possible = false;
							}
						}
						if (possible) {
							onlyCount++;
							foundAt.add(subSet[pos][cell]);
						}
					}
				}
				// abort when possible
				if (onlyCount > 2) {
					break;
				}
			}

			if (onlyCount == 2) {
				for (int cell = 0; cell < 9; ++cell) {
					// if this isn't where we found the naked set, try to remove
					// the possibilities
					if (data.getItems()[subSet[pos][cell]].number == 0
							&& subSet[pos][cell] != foundAt.get(0)
							&& subSet[pos][cell] != foundAt.get(1)) {
						if (data.getItems()[subSet[pos][cell]].possible[poss[0]]) {
							// Debug.WriteLine("Naked Pair (" + (poss[0] + 1) +
							// "," + (poss[1] + 1) + ") at [" + foundAt[0] +
							// ", " + foundAt[1] + "]. Removed " + poss[0]+
							// " from " + subSet[pos, cell]);
							if (solveIt) {
								data.togglePossible(subSet[pos][cell], poss[0]);
							} else {
								data.mark(subSet[pos][cell], poss[0],
										Data.MarkType.REMOVE);
							}
							message += "Removed " + (poss[0] + 1) + " from "
									+ subSet[pos][cell] + " ";
						}
						if (data.getItems()[subSet[pos][cell]].possible[poss[1]]) {
							// Debug.WriteLine("Naked Pair (" + (poss[0] + 1) +
							// "," + (poss[1] + 1) + ") at [" + foundAt[0] +
							// ", " + foundAt[1] + "]. Removed " + poss[1]+
							// " from " + subSet[pos, cell]);
							if (solveIt) {
								data.togglePossible(subSet[pos][cell], poss[1]);
							} else {
								data.mark(subSet[pos][cell], poss[1],
										Data.MarkType.REMOVE);
							}
							message += "Removed " + (poss[1] + 1) + " from "
									+ subSet[pos][cell] + " ";
						}
					}
				}
				if (message.length() > 0) {
					// add a little for the distance between (yes I know it's
					// kinda messed up for houses)
					rating += data.unsolvedCount() / 15
							+ Math.abs(foundAt.get(0) - foundAt.get(1));
					// Debug.WriteLine("Rating checkNakedPair: " + rating);
					return message;
				}
			}
		}

		return message;
	}

	// given the possibility set, check if there is a subset subset and can
	// remove
	// some possibilities in that set for pairs
	private String checkHiddenSubset3(int[][] subSet, int[] poss,
			boolean solveIt) {
		String message = "";
		ArrayList<Integer> foundAt = new ArrayList<Integer>();
		// subSet first
		for (int pos = 0; pos < 9; ++pos) {
			int allCount = 0;
			foundAt.clear();
			boolean found1 = false;
			boolean found2 = false;
			boolean found3 = false;
			for (int cell = 0; cell < 9; ++cell) {
				if (data.getItems()[subSet[pos][cell]].number == 0) {
					// if we find one, up the count
					if (data.getItems()[subSet[pos][cell]].possible[poss[0]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[1]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[2]]) {
						if (data.getItems()[subSet[pos][cell]].possible[poss[0]])
							found1 = true;
						if (data.getItems()[subSet[pos][cell]].possible[poss[1]])
							found2 = true;
						if (data.getItems()[subSet[pos][cell]].possible[poss[2]])
							found3 = true;
						allCount++;
						foundAt.add(subSet[pos][cell]);
					}
				}
			}
			// if this really is a subset subsubSet, can we remove anything?
			if (allCount == 3 && found1 && found2 && found3) {
				int bonusRating = 0;
				// ok, only these cells have this subSets values. Anything else
				// to remove?
				for (int at = 0; at < 3; ++at) {
					int found = foundAt.get(at);
					for (int i = 0; i < 9; ++i) {
						if (i != poss[0] && i != poss[1] && i != poss[2]
								&& data.getItems()[found].possible[i]) {
							bonusRating += 5;
							if (solveIt) {
								data.togglePossible(found, i);
							} else {
								data.mark(found, i, Data.MarkType.REMOVE);
							}
							// Debug.WriteLine("Hidden Subset 3 (" + (poss[0] +
							// 1) + "," + (poss[1] + 1) + "," + (poss[2] + 1) +
							// ") at [" + foundAt[0] + ", " + foundAt[1] + ", "
							// + foundAt[2] + "]. Removed " + (i + 1));
							message += "Removed " + (i + 1) + " from " + found
									+ " ";
						}
					}
				}
				if (message.length() > 0) {
					rating += (data.unsolvedCount() / 4 + bonusRating);
					// Debug.WriteLine("Rating checkHiddenSubset3: " + rating);
					return message;
				}
			}

		}

		return message;
	}

	// given the possibility set, check if there is a naked subset and can
	// remove
	// some possibilities in that set for pairs
	private String CheckNakedSubset3(int[][] subSet, int[] poss, boolean solveIt) {
		String message = "";
		ArrayList<Integer> foundAt = new ArrayList<Integer>();
		// subSet first
		for (int pos = 0; pos < 9; ++pos) {
			int onlyCount = 0;
			foundAt.clear();
			for (int cell = 0; cell < 9; ++cell) {
				if (data.getItems()[subSet[pos][cell]].number == 0) {
					if (data.getItems()[subSet[pos][cell]].possible[poss[0]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[1]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[2]]) {
						boolean possible = true;
						for (int i = 0; i < 9; ++i) {
							if (data.getItems()[subSet[pos][cell]].possible[i]
									&& !(poss[0] == i || poss[1] == i || poss[2] == i)) {
								possible = false;
							}
						}
						if (possible) {
							onlyCount++;
							foundAt.add(subSet[pos][cell]);
						}
					}
				}
				// abort when possible
				if (onlyCount > 3) {
					break;
				}
			}

			if (onlyCount == 3) {
				int bonusRating = 0;
				for (int cell = 0; cell < 9; ++cell) {
					// if this isn't where we found the naked set, try to remove
					// the possibilities
					if (data.getItems()[subSet[pos][cell]].number == 0
							&& subSet[pos][cell] != foundAt.get(0)
							&& subSet[pos][cell] != foundAt.get(1)
							&& subSet[pos][cell] != foundAt.get(2)) {
						for (int at = 0; at < 3; ++at) {
							if (data.getItems()[subSet[pos][cell]].possible[poss[at]]) {
								bonusRating += 4;
								// Debug.WriteLine("Naked Subset 3 (" + (poss[0]
								// + 1) + "," + (poss[1] + 1) + "," + (poss[2] +
								// 1) + ") at [" + foundAt[0] + ", " +
								// foundAt[1] + ", " + foundAt[2] +
								// "]. Removed " + poss[at]+ " from " +
								// subSet[pos, cell]);
								if (solveIt) {
									data.togglePossible(subSet[pos][cell],
											poss[at]);
								} else {
									data.mark(subSet[pos][cell], poss[at],
											Data.MarkType.REMOVE);
								}
								message += "Removed " + (poss[0] + 1)
										+ " from " + subSet[pos][cell] + " ";
							}
						}
					}
				}
				if (message.length() > 0) {
					rating += (data.unsolvedCount() / 5 + bonusRating);
					// Debug.WriteLine("CheckNakedSubset3: " + rating);
					return message;
				}
			}
		}

		return message;
	}

	// given the possibility set, check if there is a subset subset and can
	// remove
	// some possibilities in that set for pairs
	private String checkHiddenSubset4(int[][] subSet, int[] poss,
			boolean solveIt) {
		String message = "";
		ArrayList<Integer> foundAt = new ArrayList<Integer>();
		// subSet first
		for (int pos = 0; pos < 9; ++pos) {
			int allCount = 0;
			foundAt.clear();
			boolean found1 = false;
			boolean found2 = false;
			boolean found3 = false;
			boolean found4 = false;
			for (int cell = 0; cell < 9; ++cell) {
				if (data.getItems()[subSet[pos][cell]].number == 0) {
					// if we find one, up the count
					if (data.getItems()[subSet[pos][cell]].possible[poss[0]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[1]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[2]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[3]]) {
						if (data.getItems()[subSet[pos][cell]].possible[poss[0]])
							found1 = true;
						if (data.getItems()[subSet[pos][cell]].possible[poss[1]])
							found2 = true;
						if (data.getItems()[subSet[pos][cell]].possible[poss[2]])
							found3 = true;
						if (data.getItems()[subSet[pos][cell]].possible[poss[3]])
							found4 = true;
						allCount++;
						foundAt.add(subSet[pos][cell]);
					}
				}
			}
			// if this really is a subset subsubSet, can we remove anything?
			if (allCount == 4 && found1 && found2 && found3 && found4) {
				int bonusRating = 0;
				// ok, only these cells have this subSets values. Anything else
				// to remove?
				for (int at = 0; at < 4; ++at) {
					int found = foundAt.get(at);
					for (int i = 0; i < 9; ++i) {
						if (i != poss[0] && i != poss[1] && i != poss[2]
								&& i != poss[3]
								&& data.getItems()[found].possible[i]) {
							bonusRating += 5;
							if (solveIt) {
								data.togglePossible(found, i);
							} else {
								data.mark(found, i, Data.MarkType.REMOVE);
							}
							// Debug.WriteLine("Hidden Subset 4 (" + (poss[0] +
							// 1) + "," + (poss[1] + 1) + "," + (poss[2] + 1) +
							// "," + (poss[3] + 1) + ") at [" + foundAt[0] +
							// ", " + foundAt[1] + ", " + foundAt[2] + ", " +
							// foundAt[3] + "]. Removed " + (i + 1));
							message += "Removed " + (i + 1) + " from " + found
									+ " ";
						}
					}
				}
				if (message.length() > 0) {
					rating += (data.unsolvedCount() / 2 + bonusRating);
					// Debug.WriteLine("checkHiddenSubset4: " + rating);
					return message;
				}
			}

		}

		return message;
	}

	// given the possibility set, check if there is a naked subset and can
	// remove
	// some possibilities in that set for pairs
	private String CheckNakedSubset4(int[][] subSet, int[] poss, boolean solveIt) {
		String message = "";
		ArrayList<Integer> foundAt = new ArrayList<Integer>();
		// subSet first
		for (int pos = 0; pos < 9; ++pos) {
			int onlyCount = 0;
			foundAt.clear();
			for (int cell = 0; cell < 9; ++cell) {
				if (data.getItems()[subSet[pos][cell]].number == 0) {
					if (data.getItems()[subSet[pos][cell]].possible[poss[0]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[1]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[2]]
							|| data.getItems()[subSet[pos][cell]].possible[poss[3]]) {
						boolean possible = true;
						for (int i = 0; i < 9; ++i) {
							if (data.getItems()[subSet[pos][cell]].possible[i]
									&& !(poss[0] == i || poss[1] == i
											|| poss[2] == i || poss[3] == i)) {
								possible = false;
							}
						}
						if (possible) {
							onlyCount++;
							foundAt.add(subSet[pos][cell]);
						}
					}
				}
				// abort when possible
				if (onlyCount > 4) {
					break;
				}
			}

			if (onlyCount == 4) {
				int bonusRating = 0;
				for (int cell = 0; cell < 9; ++cell) {
					// if this isn't where we found the naked set, try to remove
					// the possibilities
					if (data.getItems()[subSet[pos][cell]].number == 0
							&& subSet[pos][cell] != (int) foundAt.get(0)
							&& subSet[pos][cell] != (int) foundAt.get(1)
							&& subSet[pos][cell] != (int) foundAt.get(2)
							&& subSet[pos][cell] != (int) foundAt.get(3)) {
						for (int at = 0; at < 4; ++at) {
							if (data.getItems()[subSet[pos][cell]].possible[poss[at]]) {
								bonusRating += 4;
								// Debug.WriteLine("Naked Subset 4 (" + (poss[0]
								// + 1) + "," + (poss[1] + 1) + "," + (poss[2] +
								// 1) + "," + (poss[3] + 1) + ") at [" +
								// foundAt[0] + ", " + foundAt[1] + ", " +
								// foundAt[2] + ", " + foundAt[3] +
								// "]. Removed " + poss[at]+ " from " +
								// subSet[pos, cell]);
								if (solveIt) {
									data.togglePossible(subSet[pos][cell],
											poss[at]);
								} else {
									data.mark(subSet[pos][cell], poss[at],
											Data.MarkType.REMOVE);
								}
								message += "Removed " + (poss[0] + 1)
										+ " from " + subSet[pos][cell] + " ";
							}
						}
					}
				}
				if (message.length() > 0) {
					rating += (data.unsolvedCount() / 4 + bonusRating);
					// Debug.WriteLine("CheckNakedSubset4: " + rating);
					return message;
				}
			}
		}

		return message;
	}

	// X-Wing pattern. Check for two rows (or columns) that have exactly two
	// placements in
	// the exact same columns (or rows). Remove all other candidates from these
	// two columns (or rows).
	// 8....32....6.....9.2.9....1..36.91...5...86.......4.73.68...5..5...4.....9......6
	private String xWing(boolean solveIt) {
		ArrayList<Integer> foundAt = new ArrayList<Integer>();

		// loop through each number
		for (int num = 0; num < 9; ++num) {
			// lets check rows first (since we need 2 rows checking the last one
			// is pointless)
			for (int row = 0; row < 8; ++row) {
				// save where we find possibilites
				foundAt.clear();
				for (int cell = 0; cell < 9; ++cell) {
					if (data.getItems()[rows[row][cell]].number == 0
							&& data.getItems()[rows[row][cell]].possible[num]) {
						foundAt.add(cell);
					}
				}
				// ok, this row only has 2 possibilites, is there another one w/
				// the same spots?
				if (foundAt.size() == 2) {
					for (int otherRow = row + 1; otherRow < 9; ++otherRow) {
						int count = 0;
						// check the remaining rows for another one w/ only 2
						for (int cell = 0; cell < 9; ++cell) {
							if (data.getItems()[rows[otherRow][cell]].number == 0
									&& data.getItems()[rows[otherRow][cell]].possible[num]) {
								count++;
							}
						}
						// are they at the same cells? (should form a box)
						if (count == 2
								&& data.getItems()[rows[otherRow][foundAt
										.get(0)]].number == 0
								&& data.getItems()[rows[otherRow][foundAt
										.get(1)]].number == 0
								&& data.getItems()[rows[otherRow][foundAt
										.get(0)]].possible[num]
								&& data.getItems()[rows[otherRow][foundAt
										.get(1)]].possible[num]) {
							String message = "";

							int possibilityCount = data.possibilityCount(num);
							// we've got a match, loop through the rows to check
							// for removals
							for (int rowRemoval = 0; rowRemoval < 9; ++rowRemoval) {
								if (rowRemoval != row && rowRemoval != otherRow) {
									if (data.getItems()[rows[rowRemoval][foundAt
											.get(0)]].number == 0
											&& data.getItems()[rows[rowRemoval][foundAt
													.get(0)]].possible[num]) {
										message += "removing "
												+ (num + 1)
												+ " from index "
												+ rows[rowRemoval][foundAt
														.get(0)] + " ";
										if (solveIt) {
											data.togglePossible(
													rows[rowRemoval][foundAt
															.get(0)], num);
										} else {
											data.mark(rows[rowRemoval][foundAt
													.get(0)], num,
													Data.MarkType.REMOVE);
										}
									}
									if (data.getItems()[rows[rowRemoval][foundAt
											.get(1)]].number == 0
											&& data.getItems()[rows[rowRemoval][foundAt
													.get(1)]].possible[num]) {
										message += "removing "
												+ (num + 1)
												+ " from index "
												+ rows[rowRemoval][foundAt
														.get(0)] + " ";
										if (solveIt) {
											data.togglePossible(
													rows[rowRemoval][foundAt
															.get(1)], num);
										} else {
											data.mark(rows[rowRemoval][foundAt
													.get(1)], num,
													Data.MarkType.REMOVE);
										}
									}
								}
							}
							if (message.length() > 0) {
								// the more possibilities there are, the harder
								// they are to see
								rating += (possibilityCount / 3) + 1;
								// Debug.WriteLine("CheckNakedSubset4: " +
								// rating);

								// Debug.WriteLine("X-Wing Row: " + message);
								return "X-Wing Row: " + message;
							}
						}
					}
				}
			} // end row loop

			// lets check columns now (since we need 2 columns checking the last
			// one is pointless)
			for (int column = 0; column < 8; ++column) {
				// save where we find possibilites
				foundAt.clear();
				for (int cell = 0; cell < 9; ++cell) {
					if (data.getItems()[columns[column][cell]].number == 0
							&& data.getItems()[columns[column][cell]].possible[num]) {
						foundAt.add(cell);
					}
				}
				// ok, this column only has 2 possibilites, is there another one
				// w/ the same spots?
				if (foundAt.size() == 2) {
					for (int otherColumn = column + 1; otherColumn < 9; ++otherColumn) {
						int count = 0;
						// check the remaining columns for another one w/ only 2
						for (int cell = 0; cell < 9; ++cell) {
							if (data.getItems()[columns[otherColumn][cell]].number == 0
									&& data.getItems()[columns[otherColumn][cell]].possible[num]) {
								count++;
							}
						}
						// are they at the same cells? (should form a box)
						if (count == 2
								&& data.getItems()[columns[otherColumn][foundAt
										.get(0)]].number == 0
								&& data.getItems()[columns[otherColumn][foundAt
										.get(1)]].number == 0
								&& data.getItems()[columns[otherColumn][foundAt
										.get(0)]].possible[num]
								&& data.getItems()[columns[otherColumn][foundAt
										.get(1)]].possible[num]) {
							String message = "";
							int possibilityCount = data.possibilityCount(num);
							// we've got a match, loop through the columns to
							// check for removals
							for (int columnRemoval = 0; columnRemoval < 9; ++columnRemoval) {
								if (columnRemoval != column
										&& columnRemoval != otherColumn) {
									if (data.getItems()[columns[columnRemoval][foundAt
											.get(0)]].number == 0
											&& data.getItems()[columns[columnRemoval][foundAt
													.get(0)]].possible[num]) {
										message += "removing "
												+ (num + 1)
												+ " from index "
												+ columns[columnRemoval][foundAt
														.get(0)] + " ";
										if (solveIt) {
											data.togglePossible(
													columns[columnRemoval][foundAt
															.get(0)], num);
										} else {
											data.mark(
													columns[columnRemoval][foundAt
															.get(0)], num,
													Data.MarkType.REMOVE);
										}
									}
									if (data.getItems()[columns[columnRemoval][foundAt
											.get(1)]].number == 0
											&& data.getItems()[columns[columnRemoval][foundAt
													.get(1)]].possible[num]) {
										message += "removing "
												+ (num + 1)
												+ " from index "
												+ columns[columnRemoval][foundAt
														.get(1)] + " ";
										if (solveIt) {
											data.togglePossible(
													columns[columnRemoval][foundAt
															.get(1)], num);
										} else {
											data.mark(
													columns[columnRemoval][foundAt
															.get(1)], num,
													Data.MarkType.REMOVE);
										}
									}
								}
							}
							if (message.length() > 0) {
								// the more possibilities there are, the harder
								// they are to see
								rating += (possibilityCount / 3) + 1;
								// Debug.WriteLine("xWing: " + rating);

								// Debug.WriteLine("X-Wing Column: " + message);
								return "X-Wing Column: " + message;
							}
						}
					}
				}
			} // end column loop

		} // end num loop

		return null;
	}

	// Swordfish pattern. Check for three rows (or columns) that have two or
	// three placements in
	// the exact same columns (or rows). Remove all other candidates from these
	// three columns (or rows).
	// ...47.6....4...3.592........31.........936.........28........164.8...9....7.52...
	private String swordfish(boolean solveIt) {
		ArrayList<Integer> foundAt = new ArrayList<Integer>();
		LinkedHashMap<Integer, ArrayList<Integer>> sector = new LinkedHashMap<Integer, ArrayList<Integer>>();
		String message = "";

		// loop through each number
		for (int num = 0; num < 9; ++num) {
			// make sure we don't have old number info
			sector.clear();
			// lets check rows first
			for (int row = 0; row < 9; ++row) {
				// save where we find possibilites
				foundAt.clear();
				for (int cell = 0; cell < 9; ++cell) {
					if (data.getItems()[rows[row][cell]].number == 0
							&& data.getItems()[rows[row][cell]].possible[num]) {
						foundAt.add(cell);
					}
				}
				// ok, this row only has 2 or 3 possibilites, save it?
				if (foundAt.size() == 2 || foundAt.size() == 3) {
					sector.put(row, new ArrayList<Integer>(foundAt));
				}
			} // end row loop

			// if we've got at least 3 rows, check them for possible removals
			if (sector.keySet().size() >= 3) {
				Integer[] secKeys = new Integer[sector.keySet().size()];
				secKeys = sector.keySet().toArray(secKeys);
				for (int i = 0; i < sector.keySet().size() - 2; ++i) { // we
																		// need
																		// at
																		// least
																		// 2
																		// more
																		// rows..
					int sector1 = secKeys[i];
					ArrayList<Integer> positions1 = new ArrayList<Integer>();
					for (int pos : sector.get(sector1)) {
						positions1.add(pos);
					}
					for (int j = 1; j < sector.keySet().size() - 1; ++j) { // we
																			// need
																			// at
																			// least
																			// 1
																			// more
																			// row..
						if (i != j) {
							int sector2 = secKeys[j];
							ArrayList<Integer> positions2 = new ArrayList<Integer>(
									positions1);
							for (int pos : sector.get(sector2)) {
								if (!positions2.contains(pos)) {
									positions2.add(pos);
								}
							}
							if (positions2.size() <= 3) {
								for (int k = 2; k < sector.keySet().size(); ++k) {
									if (i != k && j != k) {
										int sector3 = secKeys[k];
										ArrayList<Integer> positions3 = new ArrayList<Integer>(
												positions2);
										for (int pos : sector.get(sector3)) {
											if (!positions3.contains(pos)) {
												positions3.add(pos);
											}
										}
										int possibilityCount = data
												.possibilityCount(num);
										// see if we have 3 distinct positions
										if (positions3.size() == 3) {
											// see if anything can be removed
											for (int sec = 0; sec < 9; ++sec) {
												if (sec != sector1
														&& sec != sector2
														&& sec != sector3) {
													for (int pos : positions3) {
														if (data.getItems()[rows[sec][pos]].number == 0
																&& data.getItems()[rows[sec][pos]].possible[num]) {
															message += "removing "
																	+ (num + 1)
																	+ " from index "
																	+ rows[sec][pos]
																	+ " ";
															if (solveIt) {
																data.togglePossible(
																		rows[sec][pos],
																		num);
															} else {
																data.mark(
																		rows[sec][pos],
																		num,
																		Data.MarkType.REMOVE);
															}
														}
													} // pos loop
												}
											} // sec loop
										}
										// something was removed, return the
										// results
										if (message.length() > 0) {
											rating += possibilityCount;
											// Debug.WriteLine("Swordfish: " +
											// rating);
											return "Swordfish: " + message;
										}
									}
								} // k loop
							}
						}
					} // j loop
				} // i loop
			} // sector loop

			// clear it out before we do the columns
			sector.clear();
			// column time
			for (int col = 0; col < 9; ++col) {
				// save where we find possibilites
				foundAt.clear();
				for (int cell = 0; cell < 9; ++cell) {
					if (data.getItems()[columns[col][cell]].number == 0
							&& data.getItems()[columns[col][cell]].possible[num]) {
						foundAt.add(cell);
					}
				}
				// ok, this col only has 2 or 3 possibilites, save it?
				if (foundAt.size() == 2 || foundAt.size() == 3) {
					sector.put(col, new ArrayList<Integer>(foundAt));
				}
			} // end col loop

			// if we've got at least 3 cols, check them for possible removals
			if (sector.keySet().size() >= 3) {
				Integer[] secKeys = new Integer[sector.keySet().size()];
				secKeys = sector.keySet().toArray(secKeys);
				for (int i = 0; i < sector.keySet().size() - 2; ++i) { // we
																		// need
																		// at
																		// least
																		// 2
																		// more
																		// cols..
					int sector1 = secKeys[i];
					ArrayList<Integer> positions1 = new ArrayList<Integer>();
					for (int pos : sector.get(sector1)) {
						positions1.add(pos);
					}
					for (int j = 1; j < sector.keySet().size() - 1; ++j) { // we
																			// need
																			// at
																			// least
																			// 1
																			// more
																			// col..
						if (i != j) {
							int sector2 = secKeys[j];
							ArrayList<Integer> positions2 = new ArrayList<Integer>(
									positions1);
							for (int pos : sector.get(sector2)) {
								if (!positions2.contains(pos)) {
									positions2.add(pos);
								}
							}
							if (positions2.size() <= 3) {
								for (int k = 2; k < sector.keySet().size(); ++k) {
									if (i != k && j != k) {
										int sector3 = secKeys[k];
										ArrayList<Integer> positions3 = new ArrayList<Integer>(
												positions2);
										for (int pos : sector.get(sector3)) {
											if (!positions3.contains(pos)) {
												positions3.add(pos);
											}
										}
										// see if we have 3 distinct positions
										if (positions3.size() == 3) {
											// see if anything can be removed
											for (int sec = 0; sec < 9; ++sec) {
												if (sec != sector1
														&& sec != sector2
														&& sec != sector3) {
													for (int pos : positions3) {
														if (data.getItems()[columns[sec][pos]].number == 0
																&& data.getItems()[columns[sec][pos]].possible[num]) {
															message += "removing "
																	+ (num + 1)
																	+ " from index "
																	+ columns[sec][pos]
																	+ " ";
															if (solveIt) {
																data.togglePossible(
																		columns[sec][pos],
																		num);
															} else {
																data.mark(
																		columns[sec][pos],
																		num,
																		Data.MarkType.REMOVE);
															}
														}
													} // pos loop
												}
											} // sec loop
										}
										// something was removed, return the
										// results
										if (message.length() > 0) {
											return "Swordfish: " + message;
										}
									}
								} // k loop
							}
						}
					} // j loop
				} // i loop
			} // sector loop (columns)

		} // end num loop

		return null;
	}

	// Coloring pattern. There are two different removals possible with coloring
	// 1) if one have of a conjugate pair is found more then once in any sector,
	// the other half MUST be true and all instances can be set in that chain
	// 2) if a sector has both the true and false condition in it, all other
	// possibilities
	// with the same number can be eliminated from that sector
	// Multicoloring will attempt to join chains together based on weak pairs
	// ..963..........8..45.....1........73...825...19........3.....96..7..........142..
	// .3......6..9...8.4..2...7..85...1..9...36.5...1..49......13..4.....2......46....7
	// multicoloring ->
	// .8...3.9.......2.....182.....32..7...98..7..6.6.........13...7........5..7.5..681
	private String coloring(boolean solveIt, boolean multicoloring) {
		String message = "";
		int bonusRating = 0;
		// check for each number
		for (int num = 0; num < 9; ++num) {
			// fill in the coloring chains for this number
			data.setColors(num, multicoloring);
			int[] colors = data.getColors();

			// Check #1
			for (int i = 0; i < 81; ++i) {
				for (int j = 0; j < 81; ++j) {
					// look for two indicies with the same color value
					if (i != j
							&& colors[i] != 0
							&& colors[j] != 0
							&& colors[i] == colors[j]
							&& (Data.getRow(i) == Data.getRow(j)
									|| Data.getColumn(i) == Data.getColumn(j) || Data
									.getHouse(i) == Data.getHouse(j))) {
						// if we find one, then all of the opposite # are true
						for (int found = 0; found < 81; ++found) {
							if (colors[found] == -colors[i]) {
								bonusRating += 1;
								message += "candidate " + (num + 1)
										+ " found at index " + found + " ";
								if (solveIt) {
									data.setNumber(found, num + 1, true);
								} else {
									data.mark(found, num, Data.MarkType.SELECT);
								}
							}
						}
					}
				} // i
			} // j

			// Check #2
			// loop through all the color chain
			for (int poss = 0; poss < 81; ++poss) {
				if (data.getItems()[poss].number == 0
						&& data.getItems()[poss].possible[num]) {
					int row = Data.getRow(poss);
					int col = Data.getColumn(poss);
					int house = Data.getHouse(poss);

					for (int color = 1; color < data.getMaxColors(); ++color) {
						// don't look at anything that helped make this chain
						if (color != colors[poss] && -color != colors[poss]) {
							boolean hasTrue = false;
							boolean hasFalse = false;
							for (int i = 0; i < 81; ++i) {
								if (colors[i] == color
										&& i != poss
										&& (row == Data.getRow(i)
												|| col == Data.getColumn(i) || house == Data
												.getHouse(i))) {
									hasTrue = true;
								}
								if (colors[i] == -color
										&& i != poss
										&& (row == Data.getRow(i)
												|| col == Data.getColumn(i) || house == Data
												.getHouse(i))) {
									hasFalse = true;
								}
							} // i
							if (hasTrue && hasFalse) {
								bonusRating += 1;
								message += "removing " + (num + 1)
										+ " from index " + poss + " ";
								if (solveIt) {
									data.togglePossible(poss, num);
								} else {
									data.mark(poss, num, Data.MarkType.REMOVE);
								}
							}
						}
					} // color
				}
			} // poss loop
			if (message.length() > 0) {
				if (multicoloring) {
					rating += 20 + bonusRating;
					// Debug.WriteLine("Multicoloring: " + rating);
					return "Multicoloring: " + message;
				} else {
					rating += 5 + bonusRating;
					//Debug.WriteLine("Coloring: " + rating);
					return "Coloring: " + message;
				}
			}
		} // num loop

		return null;
	}

	// XY-Wing
	// .9.............678....63.5....3.......8.5.2.1..529.3....9..5...8...34...3.2..8..4
	private String xyWing(boolean solveIt) {
		String message = "";
		// loop through all of the cells looking for a pair
		for (int index = 0; index < 81; ++index) {
			if (data.getItems()[index].number == 0) {
				if (data.numberOfPossibilities(index) == 2) {
					Pair originalPair = new Pair(index, data);
					// ok, at this point we have our two possibilities. Get a
					// set of pairs
					// that share at least one possibility, and at least one
					// row/column/house
					ArrayList<Pair> pairs = new ArrayList<Pair>();
					for (int i = 0; i < 9; ++i) {
						if (data.numberOfPossibilities(houses[originalPair.house][i]) == 2) {
							Pair pair = new Pair(houses[originalPair.house][i],
									data);
							if (pair.onePossibilityInCommon(originalPair)) {
								pairs.add(new Pair(pair));
							}
						}
						if (data.numberOfPossibilities(rows[originalPair.row][i]) == 2) {
							Pair pair = new Pair(rows[originalPair.row][i],
									data);
							if (pair.onePossibilityInCommon(originalPair)) {
								pairs.add(new Pair(pair));
							}
						}
						if (data.numberOfPossibilities(columns[originalPair.column][i]) == 2) {
							Pair pair = new Pair(
									columns[originalPair.column][i], data);
							if (pair.onePossibilityInCommon(originalPair)) {
								pairs.add(new Pair(pair));
							}
						}
					} // i
						// ok, now that we have a list of pairs that has at 1
						// possibility in common
					// search for a pair that matches with the first original
					// pair possibilty
					for (int i = 0; i < pairs.size(); ++i) {
						Pair first = pairs.get(i);
						if (first.inCommon == originalPair.possibility1) {
							// we found one w/ the same first possibility search
							// for another one
							// with the same second possibility AND the same not
							// in common values
							for (int j = 0; j < pairs.size(); ++j) {
								Pair second = pairs.get(j);
								if (second.inCommon == originalPair.possibility2
										&& first.notInCommon == second.notInCommon) {
									// we've got a match. get the intersection
									Integer[] intersect = first
											.intersection(second);
									for (int k = 0; k < intersect.length; ++k) {
										// check for removals
										if (data.getItems()[intersect[k]].number == 0
												&& data.getItems()[intersect[k]].possible[first.notInCommon]) {
											message += "removing "
													+ (first.notInCommon + 1)
													+ " from index "
													+ intersect[k] + " ";
											if (solveIt) {
												data.togglePossible(
														intersect[k],
														first.notInCommon);
											} else {
												data.mark(intersect[k],
														first.notInCommon,
														Data.MarkType.REMOVE);
											}
										}
									}
									if (message.length() > 0) {
										int r = 0;
										r += Math.abs(first.row
												- originalPair.row);
										r += Math.abs(first.column
												- originalPair.column);
										r += Math.abs(second.row
												- originalPair.row);
										r += Math.abs(second.column
												- originalPair.column);
										rating += (r * 5);
										// Debug.WriteLine("xyWing: " + rating);
										return "XY Wing: " + message;
									}
								}
							} // j
						}
					} // i
				}
			}
		}
		return null;
	}

	// Flat out cheat and guess correctly
	private String guess(boolean solveIt) {
		if (data.solution == "" || data.solution == null) {
			String str = "";
			for (int i = 0; i < 81; ++i) {
				if (data.getItems()[i].number > 0) {
					str += data.getItems()[i].number;
				} else {
					str += ".";
				}
			}
			SudokuEngine engine = new SudokuEngine();
			try {
				data.solution = engine.solve(str);
			} catch (Exception ex) { 
				return null;
			}
		}

		if (data.solution.startsWith("0 solutions")) {
			return null;
		}

		// look slightly better and guess on something that only has 2
		// possibilites first
		for (int i = 0; i < 81; ++i) {
			if (data.numberOfPossibilities(i) == 2) {
				int num = Integer.parseInt("" + data.solution.toCharArray()[i]);
				if (data.getItems()[i].possible[num - 1]) {
					if (solveIt) {
						data.setNumber(i, num);
					} else {
						data.mark(i, num - 1, Data.MarkType.SELECT);
					}
					return "Guessing " + num;
				}
			}
		}

		// ok, just guess the first unfilled square we have that has the
		// possibility
		for (int i = 0; i < 81; ++i) {
			int num = Integer.parseInt("" + data.solution.toCharArray()[i]);
			if (data.getItems()[i].possible[num - 1]) {
				if (solveIt) {
					data.setNumber(i, num);
				} else {
					data.mark(i, num - 1, Data.MarkType.SELECT);
				}
				return "Guessing " + num;
			}
		}
		return null;
	}
}

class Pair {
	public int possibility1;
	public int possibility2;
	public int index;
	public int row, column, house;
	public int inCommon = -1, notInCommon = -1;

	public Pair(Pair copy) {
		possibility1 = copy.possibility1;
		possibility2 = copy.possibility2;
		index = copy.index;
		row = copy.row;
		column = copy.column;
		house = copy.house;
		inCommon = copy.inCommon;
		notInCommon = copy.notInCommon;
	}

	public Pair(int index, Data data) {
		for (int i = 0; i < 8; ++i) {
			if (data.getItems()[index].possible[i]) {
				possibility1 = i;
				break;
			}
		} // i
		for (int i = 8; i > 0; --i) {
			if (data.getItems()[index].possible[i]) {
				possibility2 = i;
				break;
			}
		} // i

		row = Data.getRow(index);
		column = Data.getColumn(index);
		house = Data.getHouse(index);
		this.index = index;
	}

	public boolean onePossibilityInCommon(Pair comp) {
		if ((possibility1 == comp.possibility1
				|| possibility2 == comp.possibility1
				|| possibility1 == comp.possibility2 || possibility2 == comp.possibility2)
				&& !((possibility1 == comp.possibility1 && possibility2 == comp.possibility2) || (possibility1 == comp.possibility2 && possibility2 == comp.possibility1))) {
			if (possibility1 == comp.possibility1
					|| possibility1 == comp.possibility2) {
				inCommon = possibility1;
				notInCommon = possibility2;
			} else {
				inCommon = possibility2;
				notInCommon = possibility1;
			}

			return true;
		}
		return false;
	}

	public Integer[] intersection(Pair comp) {
		int[] houseIndicies = new int[81];
		int[] rowIndicies = new int[81];
		int[] columnIndicies = new int[81];
		int[] compHouseIndicies = new int[81];
		int[] compRowIndicies = new int[81];
		int[] compColumnIndicies = new int[81];
		for (int index = 0; index < 9; ++index) {
			houseIndicies[HumanSolver.houses[house][index]]++;
			if (house == Data.getHouse(HumanSolver.rows[comp.row][index]))
				houseIndicies[HumanSolver.rows[comp.row][index]]++;
			if (house == Data.getHouse(HumanSolver.columns[comp.column][index]))
				houseIndicies[HumanSolver.columns[comp.column][index]]++;

			rowIndicies[HumanSolver.rows[row][index]]++;
			if (row == Data.getRow(HumanSolver.houses[comp.house][index]))
				rowIndicies[HumanSolver.houses[comp.house][index]]++;
			if (row == Data.getRow(HumanSolver.columns[comp.column][index]))
				rowIndicies[HumanSolver.columns[comp.column][index]]++;

			columnIndicies[HumanSolver.columns[column][index]]++;
			if (column == Data.getColumn(HumanSolver.rows[comp.row][index]))
				columnIndicies[HumanSolver.rows[comp.row][index]]++;
			if (column == Data.getColumn(HumanSolver.houses[comp.house][index]))
				columnIndicies[HumanSolver.houses[comp.house][index]]++;

			compHouseIndicies[HumanSolver.houses[comp.house][index]]++;
			if (comp.house == Data.getHouse(HumanSolver.rows[row][index]))
				compHouseIndicies[HumanSolver.rows[row][index]]++;
			if (comp.house == Data.getHouse(HumanSolver.columns[column][index]))
				compHouseIndicies[HumanSolver.columns[column][index]]++;

			compRowIndicies[HumanSolver.rows[comp.row][index]]++;
			if (comp.row == Data.getRow(HumanSolver.houses[house][index]))
				compRowIndicies[HumanSolver.houses[house][index]]++;
			if (comp.row == Data.getRow(HumanSolver.columns[column][index]))
				compRowIndicies[HumanSolver.columns[column][index]]++;

			compColumnIndicies[HumanSolver.columns[comp.column][index]]++;
			if (comp.column == Data.getColumn(HumanSolver.rows[row][index]))
				compColumnIndicies[HumanSolver.rows[row][index]]++;
			if (comp.column == Data.getColumn(HumanSolver.houses[house][index]))
				compColumnIndicies[HumanSolver.houses[house][index]]++;
		}

		ArrayList<Integer> intersection = new ArrayList<Integer>();
		for (int i = 0; i < 81; ++i) {
			if ((houseIndicies[i] > 1 || rowIndicies[i] > 1
					|| columnIndicies[i] > 1 || compHouseIndicies[i] > 1
					|| compRowIndicies[i] > 1 || compColumnIndicies[i] > 1)
					&& i != index && i != comp.index) {
				intersection.add(i);
			}
		}

		return intersection.toArray(new Integer[intersection.size()]);
	}
}
