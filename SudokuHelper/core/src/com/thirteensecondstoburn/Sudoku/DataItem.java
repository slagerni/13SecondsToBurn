package com.thirteensecondstoburn.Sudoku;

import java.io.Serializable;

import com.thirteensecondstoburn.Sudoku.Data.MarkType;

public class DataItem implements Serializable {
	private static final long serialVersionUID = -1059192234695241171L;
	public int number = 0;
	public boolean[] possible = new boolean[9];
	public MarkType[] mark = new MarkType[9];
	public boolean isStartValue = false;

	public DataItem() {
		for (int i = 0; i < 9; ++i ) {
			possible[i] = true;
			mark[i] = MarkType.NONE;
		}
	}

	public void clearMarks() {
		for (int i = 0; i < 9; ++i ) {
			mark[i] = MarkType.NONE;
		}
	}
	public DataItem(DataItem copy) {
		this.number = copy.number;
		for(int i=0; i<9; ++i) {
			this.possible[i] = copy.possible[i];
			this.mark[i] = copy.mark[i];
		}
		this.isStartValue = copy.isStartValue;
	}
}
