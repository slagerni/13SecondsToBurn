package com.thirteensecondstoburn.Sudoku;

public class Generate {
	public HumanSolver solver;

	public enum Difficulty {Any, Beginner, Easy, Medium, Hard, Random};
	public enum TechniqueUse {MayHave, MustHave, MayNotHave};

	public Generate() {
	}

	public int getPuzzle(Data data) {
		return getPuzzle(data, Difficulty.Any);
	}

	public int getPuzzle(Data data, Difficulty difficulty) {
		return getPuzzle(data, difficulty, TechniqueUse.MayHave, TechniqueUse.MayHave, TechniqueUse.MayHave, TechniqueUse.MayHave, TechniqueUse.MayHave, TechniqueUse.MayHave, TechniqueUse.MayHave, TechniqueUse.MayHave, TechniqueUse.MayHave, TechniqueUse.MayHave, (difficulty == Difficulty.Random ? TechniqueUse.MayHave : TechniqueUse.MayNotHave));
	}

	public int getPuzzle(Data data, 
			Difficulty difficulty, 
			TechniqueUse useSingleSector,
			TechniqueUse useNakedPair,
			TechniqueUse useHiddenPair,
			TechniqueUse useNakedSubset,
			TechniqueUse useHiddenSubset,
			TechniqueUse useXWing,
			TechniqueUse useSwordfish,
			TechniqueUse useColoring,
			TechniqueUse useMulticoloring,
			TechniqueUse useXYWing,
			TechniqueUse useGuess
		) {
		int rating = 0;
		boolean humanSolved = false;
		Data fill = null;
		SudokuEngine engine = new SudokuEngine();
		String puzzle = "";
		while (!humanSolved) {
			fill = new Data();
			puzzle = engine.generate(0, 99999999).replace("\n", "");
			fill.setFromString(puzzle);
			fill.solution = engine.solve(puzzle);

			solver = new HumanSolver(fill,
				true,
				true,
				true,
				true,
				true,
				true,
				true,
				true,
				true,
				true,
				useGuess != TechniqueUse.MayNotHave
			);
			humanSolved = solver.Solve();
			if(humanSolved) {
				System.out.println("Human rating: " + solver.rating + " (" + solver.mediumCount + "." + solver.hardCount + ")");
				System.out.println("Single Sector: " + solver.singleSectorCandidateCount);
				System.out.println("Naked Pair: " + solver.nakedPairCount);
				System.out.println("Hidden Pair: " + solver.hiddenPairCount);
				System.out.println("Naked Subset: " + solver.nakedSubsetCount);
				System.out.println("Hidden Subset: " + solver.hiddenSubsetCount);
				System.out.println("X Wing: " + solver.xWingCount);
				System.out.println("swordfish: " + solver.swordfishCount);
				System.out.println("coloring: " + solver.coloringCount);
				System.out.println("multicoloring: " + solver.multicoloringCount);
				System.out.println("XY Wing: " + solver.xyWingCount);
				System.out.println("Guesses: " + solver.guessCount);
				System.out.println("Difficulty: " + difficulty);
				switch (difficulty) {
					case Hard:
						humanSolved = solver.rating >= 30;
						break;
					case Medium:
						humanSolved = solver.rating >= 10 && solver.rating < 30;
						break;
					case Easy:
						humanSolved = solver.rating > 0 && solver.rating < 10;
						break;
					case Beginner:
						humanSolved = solver.rating == 0;
						break;
					default:
						humanSolved = true;
						break;
				}
				rating = solver.rating;
			} 
			// ok, check the counts for usability
			if(useSingleSector == TechniqueUse.MustHave && solver.singleSectorCandidateCount == 0) humanSolved = false;
			if(useNakedPair == TechniqueUse.MustHave && solver.nakedPairCount == 0) humanSolved = false;
			if(useHiddenPair == TechniqueUse.MustHave && solver.hiddenPairCount == 0) humanSolved = false;
			if(useNakedSubset == TechniqueUse.MustHave && solver.nakedSubsetCount == 0) humanSolved = false;
			if(useHiddenSubset == TechniqueUse.MustHave && solver.hiddenSubsetCount == 0) humanSolved = false;
			if(useXWing == TechniqueUse.MustHave && solver.xWingCount == 0) humanSolved = false;
			if(useSwordfish == TechniqueUse.MustHave && solver.swordfishCount == 0) humanSolved = false;
			if(useColoring == TechniqueUse.MustHave && solver.coloringCount == 0) humanSolved = false;
			if(useMulticoloring == TechniqueUse.MustHave && solver.multicoloringCount == 0) humanSolved = false;
			if(useXYWing == TechniqueUse.MustHave && solver.xyWingCount == 0) humanSolved = false;
			if(useGuess == TechniqueUse.MustHave && solver.guessCount == 0) humanSolved = false;
			
			if(useSingleSector == TechniqueUse.MayNotHave && solver.singleSectorCandidateCount > 0) humanSolved = false;
			if(useNakedPair == TechniqueUse.MayNotHave && solver.nakedPairCount > 0) humanSolved = false;
			if(useHiddenPair == TechniqueUse.MayNotHave && solver.hiddenPairCount > 0) humanSolved = false;
			if(useNakedSubset == TechniqueUse.MayNotHave && solver.nakedSubsetCount > 0) humanSolved = false;
			if(useHiddenSubset == TechniqueUse.MayNotHave && solver.hiddenSubsetCount > 0) humanSolved = false;
			if(useXWing == TechniqueUse.MayNotHave && solver.xWingCount > 0) humanSolved = false;
			if(useSwordfish == TechniqueUse.MayNotHave && solver.swordfishCount > 0) humanSolved = false;
			if(useColoring == TechniqueUse.MayNotHave && solver.coloringCount > 0) humanSolved = false;
			if(useMulticoloring == TechniqueUse.MayNotHave && solver.multicoloringCount > 0) humanSolved = false;
			if(useGuess == TechniqueUse.MayNotHave && solver.guessCount > 0) humanSolved = false;
		}
		data.setFromString(puzzle);
		data.solution = fill.solution;
		System.out.println("Puzzle:   " + puzzle);
		System.out.println("Solution: " + data.solution);
		return rating;
	}
}

