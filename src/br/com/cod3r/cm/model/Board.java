package br.com.cod3r.cm.model;

import java.util.ArrayList;
import java.util.List;

import br.com.cod3r.cm.exception.ExplosionException;

public class Board {

	private int lines;
	private int columns;
	private int mines;
	
	private final List<Cell> cells = new ArrayList<>();

	public Board(int lines, int columns, int mines) {
		this.lines = lines;
		this.columns = columns;
		this.mines = mines;
		
		generateCells();
		associateNeighbors();
		raffleMines();
	}
	
	public void openCell(int line, int column) { 
		try {
			cells.parallelStream()
				.filter(c -> c.getLine() == line && c.getColumn() == column)
				.findFirst()
				.ifPresent(c -> c.open());
		} catch (ExplosionException e) {
			cells.forEach(c -> c.setOpen(true));
			throw e;
		}
	}
	
	public void toggleCellFlag(int line, int column) { 
		cells.parallelStream()
		.filter(c -> c.getLine() == line && c.getColumn() == column)
		.findFirst()
		.ifPresent(c -> c.toggleFlag());
	}

	private void generateCells() {
		for (int l = 0; l < lines; l++) {
			for (int c = 0; c < columns; c++) {
				cells.add(new Cell(l, c));
			}
		}
	}
	
	private void associateNeighbors() {
		for (Cell c1 : cells) {
			for (Cell c2 : cells) {
				c1.addNeighbor(c2);
			}
		}
	}
	
	private void raffleMines() {
		int minesLefting = mines;
		
		while(minesLefting > 0) {
			int randomCellIndex = (int) (Math.random() * cells.size());
			Cell drawnCell = cells.get(randomCellIndex);
			minesLefting = undermineCell(drawnCell, minesLefting);
		}
	}
	
	private int undermineCell(Cell cell, int minesLefting) {
		if (!cell.hasMine() && minesLefting > 0) {
			cell.undermine();
			minesLefting--;
		}
		return minesLefting;
	}
	
	public boolean goalAchieved() {
		return cells.stream().allMatch(c -> c.goalAchieved());
	}
	
	public void restart() {
		cells.forEach(c -> c.restart());
		raffleMines();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("  ");
		for (int c = 0; c < columns; c++) {
			sb.append("  ");
			sb.append(c);
		}
		sb.append("\n\n");
		
		for (int l = 0; l < lines; l++) {
			sb.append(l);
			sb.append("  ");
			for (int c = 0; c < columns; c++) {
				sb.append(" ");
				sb.append(cells.get(c + l * columns));
				sb.append(" ");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
