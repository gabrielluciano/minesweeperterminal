package br.com.cod3r.cm.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.cod3r.cm.exception.ExplosionException;

class CellTest {
	
	private Cell cell;

	@BeforeEach
	void beforeEach() {
		cell = new Cell(3, 3);
	}
	
	@Test
	void testNeighborWithDistance1OnLeft() {
		Cell neighbor = new Cell(3, 2);
		boolean result = cell.addNeighbor(neighbor);
		
		assertTrue(result);
	}
	
	@Test
	void testNeighborWithDistance1OnRight() {
		Cell neighbor = new Cell(3, 4);
		boolean result = cell.addNeighbor(neighbor);
		
		assertTrue(result);
	}
	
	@Test
	void testNeighborWithDistance1OnTop() {
		Cell neighbor = new Cell(2, 3);
		boolean result = cell.addNeighbor(neighbor);
		
		assertTrue(result);
	}
	
	@Test
	void testNeighborWithDistance1Below() {
		Cell neighbor = new Cell(4, 3);
		boolean result = cell.addNeighbor(neighbor);
		
		assertTrue(result);
	}
	
	@Test
	void testNeighborWithDistance2() {
		Cell neighbor = new Cell(2, 2);
		boolean result = cell.addNeighbor(neighbor);
		
		assertTrue(result);
	}
	
	@Test
	void testNotNeighborWithDistance2() {
		Cell neighbor = new Cell(3, 5);
		boolean result = cell.addNeighbor(neighbor);
		
		assertFalse(result);
	}
	
	@Test
	void testNotNeighborInDiagonal() {
		Cell neighbor = new Cell(1, 1);
		boolean result = cell.addNeighbor(neighbor);
		
		assertFalse(result);
	}
	
	@Test
	void testFlagAttributeDefaultValue() {
		assertFalse(cell.isFlagged());
	}
	
	@Test
	void testToggleFlag() {
		cell.toggleFlag();
		assertTrue(cell.isFlagged());
	}
	
	@Test
	void testToggleFlagCalledTwoTimes() {
		cell.toggleFlag();
		cell.toggleFlag();
		assertFalse(cell.isFlagged());
	}

	@Test
	void testOpenCellNotMinedAndNotFlagged() {
		assertTrue(cell.open());
	}
	
	@Test
	void testOpenCellNotMinedAndFlagged() {
		cell.toggleFlag();
		assertFalse(cell.open());		
	}
	
	@Test
	void testOpenCellMinedAndFlagged() {
		cell.toggleFlag();
		cell.undermine();
		assertFalse(cell.open());		
	}
	
	@Test
	void testOpenCellMinedAndNotFlagged() {
		cell.undermine();
		assertThrows(ExplosionException.class, () -> {
			cell.open();
		});
	}
	
	@Test
	void testOpenCellWithNeighbor1() {
		Cell cell_11 = new Cell(1, 1);
		Cell cell_22 = new Cell(2, 2);
		
		cell_22.addNeighbor(cell_11);
		cell.addNeighbor(cell_22);
		cell.open();
		
		assertTrue(cell.isOpen() && cell_22.isOpen() && cell_11.isOpen());	
	}
	
	@Test
	void testOpenCellWithNeighbors2() {
		Cell cell_11 = new Cell(1, 1);
		Cell cell_12 = new Cell(1, 2);		
		cell_12.undermine();
		
		Cell cell_22 = new Cell(2, 2);
		cell_22.addNeighbor(cell_11);
		cell_22.addNeighbor(cell_12);
		
		cell.addNeighbor(cell_22);
		cell.open();
		
		assertTrue(cell.isOpen() && cell_22.isOpen() && cell_11.isClosed());	
	}
	
	@Test
	void testGetNumberOfMinesInNeighborhoodWithNoMines() {
		Cell cell_32 = new Cell(3, 2);			
		Cell cell_22 = new Cell(2, 2);

		cell.addNeighbor(cell_22);
		cell.addNeighbor(cell_32);

		assertEquals(0, cell.getNumberOfMinesInNeighborhood());
	}
	
	@Test
	void testGetNumberOfMinesInNeighborhoodWith1Mine() {
		Cell cell_32 = new Cell(3, 2);			
		Cell cell_22 = new Cell(2, 2);
		
		cell_22.undermine();
		
		cell.addNeighbor(cell_22);
		cell.addNeighbor(cell_32);
		
		assertEquals(1, cell.getNumberOfMinesInNeighborhood());
	}
	
	@Test
	void testGetNumberOfMinesInNeighborhoodWith2Mines() {
		Cell cell_32 = new Cell(3, 2);			
		Cell cell_22 = new Cell(2, 2);
		
		cell_22.undermine();
		cell_32.undermine();
		
		cell.addNeighbor(cell_22);
		cell.addNeighbor(cell_32);
		
		assertEquals(2, cell.getNumberOfMinesInNeighborhood());
	}
	
	@Test
	void testGoalAchievedWithDefaultState() {
		cell.open();
		assertTrue(cell.goalAchieved());
	}
	
	@Test
	void testGoalAchievedFlagged() {
		cell.toggleFlag();
		cell.open();
		assertFalse(cell.goalAchieved());
	}
	
	@Test
	void testGoalAchievedWithMineAndFlagged() {
		cell.undermine();
		cell.toggleFlag();
		assertTrue(cell.goalAchieved());
	}
	
	@Test
	void testToString1() {
		assertEquals("?", cell.toString());
		cell.toggleFlag();
		assertEquals("X", cell.toString());
		cell.toggleFlag();
		cell.undermine();
		try {
			cell.open();
		} catch (ExplosionException e) { }
		assertEquals("*", cell.toString());
	}
	
	@Test
	void testToString2() {
		Cell cell_32 = new Cell(3, 2);			
		Cell cell_22 = new Cell(2, 2);
		
		cell_22.undermine();
		cell_32.undermine();
		
		cell.addNeighbor(cell_22);
		cell.addNeighbor(cell_32);
		
		cell.open();
		assertEquals("2", cell.toString());
	}
	
	@Test
	void testRestart() {
		cell.undermine();
		cell.toggleFlag();
		cell.restart();
		
		assertFalse(cell.isFlagged());
	}
}
