package br.com.cod3r.cm.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import br.com.cod3r.cm.exception.ExitException;
import br.com.cod3r.cm.exception.ExplosionException;
import br.com.cod3r.cm.model.Board;

public class BoardConsole {

	private Board board;
	private Scanner sc = new Scanner(System.in);
	
	public BoardConsole(Board board) {
		this.board = board;
		
		executeGame();
	}

	private void executeGame() {
		try {
			boolean continueGame = true;
			
			while(continueGame) {
				gameLoop();
				
				System.out.println("Continue? (S/n)");
				String answer = sc.nextLine();
				
				if("n".equalsIgnoreCase(answer)) {
					continueGame = false;
					System.out.println("Goodbye");
				} else {
					board.restart();
				}
			}
		} catch (ExitException e) {
			System.out.println("Goodbye");
		} finally {
			sc.close();
		}
	}

	private void gameLoop() {
		try {
			
			while(!board.goalAchieved()) {
				System.out.println(board);
				
				String typed = captureTypedValue("Enter (x, y): ");
				
				Iterator<Integer> xy = Arrays.stream(typed.split(","))
					.map(s -> Integer.parseInt(s.trim()))
					.iterator();
				
				typed = captureTypedValue("1 - Open | 2 - Toggle flag: ");
				
				if ("1".equals(typed)) {
					board.openCell(xy.next(), xy.next());
				} else if ("2".equals(typed)) {
					board.toggleCellFlag(xy.next(), xy.next());
				}
			}
			
			System.out.println(board);
			System.out.println("You win!");
		} catch (ExplosionException e) {
			System.out.println(board);
			System.out.println("You lose!");
		}
	}
	
	private String captureTypedValue(String text) {
		System.out.print(text);
		String typed = sc.nextLine();
		System.out.println();
		
		if ("exit".equalsIgnoreCase(typed)) {
			throw new ExitException();
		}
		
		return typed;
	}
}
