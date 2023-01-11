package br.com.cod3r.cm;

import br.com.cod3r.cm.model.Board;
import br.com.cod3r.cm.view.BoardConsole;

public class App {

	public static void main(String[] args) {
		
		Board board = new Board(6, 6, 1);	
		new BoardConsole(board);			
	}
}
