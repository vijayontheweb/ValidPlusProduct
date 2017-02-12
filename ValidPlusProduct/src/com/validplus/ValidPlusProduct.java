package com.validplus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author vshanmughada
 *
 *         The purpose of this program is to find 2 pluses that can be drawn on
 *         good cells of the grid, and print maximum the product of their areas
 *         as an integer
 *
 */
public class ValidPlusProduct {

	static final int MIN_NO_OF_ROWS = 2;
	static final int MAX_NO_OF_ROWS = 15;
	static final int MIN_NO_OF_COLUMNS = 2;
	static final int MAX_NO_OF_COLUMNS = 15;

	/**
	 * Holds all the cells in a grid
	 */
	private Cell[][] cellArray;
	/**
	 * Holds size of rows and columns in the grid
	 */
	private int rowSize, colSize;

	/**
	 * Holds reference to the top 2 non-overlapping cells with valid plus and
	 * maximum area
	 */
	private Cell firstMaxCell, secondMaxCell;

	protected static ValidPlusProduct getInstance(){
		return new ValidPlusProduct();
	}
	
	
	/**
	 * The program expect inputs in which the first line contains two
	 * space-separated integers, for rowSize and colSize. The next subsequent
	 * lines contains characters, where each character is either G (good) or B
	 * (bad).
	 * 
	 * Logic is as follows:
	 * 
	 * Step 1 : Store the cells in a 2 dimensional array Step 2 : Iterate over
	 * the grid and identify the first cell with valid plus and maximum area
	 * Step 3 : Mask the first cell cluster to avoid overlap in subsequent
	 * iteration Step 4 : Iterate over the grid again and identify the second
	 * cell with valid plus and maximum area Step 5 : Get the product of their
	 * areas
	 * 
	 * @param args
	 */
	public int getProduct(String filePath) {
		int product = 0;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
			String gridSize = br.readLine();
			String rowSizeStr = gridSize.split(" ")[0];
			String columnSizeStr = gridSize.split(" ")[1];
			rowSize = Integer.parseInt(rowSizeStr);
			colSize = Integer.parseInt(columnSizeStr);

			if (rowSize < MIN_NO_OF_ROWS || rowSize > MAX_NO_OF_ROWS) {
				throw new RowSizeOutOfBoundsException(rowSizeStr);
			}
			if (colSize < MIN_NO_OF_COLUMNS || colSize > MAX_NO_OF_COLUMNS) {
				throw new ColSizeOutOfBoundsException(columnSizeStr);
			}

			cellArray = new Cell[rowSize][colSize];

			for (int row = 0; row < rowSize; row++) {
				String rowStr = br.readLine();
				String[] matrix = rowStr.split("");
				for (int col = 0; col < colSize; col++) {
					cellArray[row][col] = new Cell(row, col, matrix[col]);
				}
			}
			System.out.println("Input Cell Grid with Values");
			System.out.println("***************************");
			printCell(); // Statement not required. illustration purpose only
			System.out.println("Cell Grid with Areas");
			System.out.println("********************");
			Cell firstMaxCell = getCellWithMaxArea();
			System.out.println("Cell Grid with values after Masking firstMaxCell valid plus");
			System.out.println("***********************************************************");
			maskAsReserved(firstMaxCell);
			printCell(); // Statement not required. illustration purpose only			
			System.out.println("Revised Cell Grid with Areas(Post Masking)");
			System.out.println("******************************************");
			Cell secondMaxCell = getCellWithMaxArea();			
			System.out.println("Cell Grid with values after Masking secondMaxCell valid plus");
			System.out.println("************************************************************");
			maskAsReserved(secondMaxCell); // Statement not required.
											// illustration
											// purpose only
			printCell(); // Statement not required. illustration purpose only			
			System.out.println("\nfirstMaxCell->" + firstMaxCell);
			System.out.println("secondMaxCell->" + secondMaxCell);
			product = firstMaxCell.getArea() * secondMaxCell.getArea();
			System.out.println("Product Value = " + product);			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return product;
	}

	private void printCell() {
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				System.out.print(cellArray[row][col].getValue());
			}
			System.out.println();
		}
	}

	/**
	 * Iterates over cell Array, and for every cell, computes the cell area
	 * based on valid plus criteria. During the course of iteration, it
	 * ultimately picks the one valid cell with maximum area
	 * 
	 * @return
	 */
	private Cell getCellWithMaxArea() {
		Cell cellWithMaxArea = null;
		int maxArea = 0;
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				int currentCellArea = getCellArea(cellArray[row][col]);
				System.out.print(currentCellArea);
				if (currentCellArea > maxArea) {
					maxArea = currentCellArea;
					cellWithMaxArea = cellArray[row][col];
				}
			}
			System.out.println();
		}
		cellWithMaxArea.setArea(maxArea);
		return cellWithMaxArea;

	}

	/**
	 * Once a first cell is identified, that meets the valid plus criteria with
	 * maximum area, that specific cell area is masked to bypass subsequent
	 * processing while identifying the second largest valid plus cell with
	 * maximum area. This provision was made so that the two valid pluses does
	 * not overlap. This is achieved by marking its value with '+'i.e. Anything
	 * other than 'G'. Also a 'hopCount' (derived from maxArea) signifies the
	 * number of jumps required from the middle cell to any edge cell of the
	 * valid plus
	 * 
	 * @param cell
	 */
	private void maskAsReserved(Cell cell) {
		cell.setValue("+");
		int maxArea = cell.getArea();
		int hopCount = (maxArea - 1) / 4;
		for (int hopIndex = 1; hopIndex <= hopCount; hopIndex++) {
			(cellArray[cell.getRowNumber()][cell.getColNumber() + hopIndex]).setValue("+");
			(cellArray[cell.getRowNumber()][cell.getColNumber() - hopIndex]).setValue("+");
			(cellArray[cell.getRowNumber() + hopIndex][cell.getColNumber()]).setValue("+");
			(cellArray[cell.getRowNumber() - hopIndex][cell.getColNumber()]).setValue("+");
		}
	}

	/**
	 * A cell's area is derived based on the count of the given cell with its
	 * adjoining neighbors that meets the valid plus criteria
	 * 
	 * @param cell
	 * @return
	 */
	private int getCellArea(Cell cell) {
		int length = 0;
		if ("G".equals(cell.getValue())) {
			length++;
			while (hasValidPlus(cell, length + 2)) {
				length = length + 2;
			}
			return (length * 2) - 1;
		}
		return 0;
	}

	/**
	 * A valid plus is defined as the crossing of two segments (horizontal and
	 * vertical) of equal lengths and containing Good cells i.e."G". These
	 * lengths must be odd, and the middle cell of its horizontal segment must
	 * cross the middle cell of its vertical segment. A 'hopCount' (derived from
	 * length) signifies the number of jumps required from the middle cell to
	 * any edge cell of the valid plus
	 * 
	 * @param cell
	 * @param length
	 * @return
	 */
	private boolean hasValidPlus(Cell cell, int length) {
		int hopCount = (length - 1) / 2;
		return (hasValidRight(cell, hopCount) && hasValidLeft(cell, hopCount) && hasValidTop(cell, hopCount)
				&& hasValidBottom(cell, hopCount));
	}

	private boolean hasValidRight(Cell cell, int hopCount) {
		if ((cell.getColNumber() + hopCount) < colSize
				&& "G".equals((cellArray[cell.getRowNumber()][cell.getColNumber() + hopCount]).getValue())) {
			return true;
		}
		return false;
	}

	private boolean hasValidLeft(Cell cell, int hopCount) {
		if ((cell.getColNumber() - hopCount) >= 0
				&& "G".equals((cellArray[cell.getRowNumber()][cell.getColNumber() - hopCount]).getValue())) {
			return true;
		}
		return false;
	}

	private boolean hasValidTop(Cell cell, int hopCount) {
		if ((cell.getRowNumber() - hopCount) >= 0
				&& "G".equals((cellArray[cell.getRowNumber() - hopCount][cell.getColNumber()]).getValue())) {
			return true;
		}
		return false;
	}

	private boolean hasValidBottom(Cell cell, int hopCount) {
		if ((cell.getRowNumber() + hopCount) < rowSize
				&& "G".equals((cellArray[cell.getRowNumber() + hopCount][cell.getColNumber()]).getValue())) {
			return true;
		}
		return false;
	}

}

/**
 * A Cell is defined by its value and the position it occupies in the 2
 * dimensional array.
 */
class Cell {

	int rowNumber;
	int colNumber;
	String value;
	int area;

	Cell(int rowNumber, int colNumber, String value) {
		this.rowNumber = rowNumber;
		this.colNumber = colNumber;
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * Overrides toString method to display the coordinates and cell area
	 * delimited with a hash
	 */
	public String toString() {
		return this.rowNumber + "#" + this.colNumber + "#" + this.area;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public int getColNumber() {
		return colNumber;
	}

	public String getValue() {
		return value;
	}

	public int getArea() {
		return area;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setArea(int area) {
		this.area = area;
	}
}

class RowSizeOutOfBoundsException extends RuntimeException {
	public RowSizeOutOfBoundsException(String message) {
		super("The given RowSize=" + message + " does not fall between the range " + ValidPlusProduct.MIN_NO_OF_ROWS
				+ " and " + ValidPlusProduct.MAX_NO_OF_ROWS);
	}
}

class ColSizeOutOfBoundsException extends RuntimeException {
	public ColSizeOutOfBoundsException(String message) {
		super("The given ColumnSize=" + message + " does not fall between the range "
				+ ValidPlusProduct.MIN_NO_OF_COLUMNS + " and " + ValidPlusProduct.MAX_NO_OF_COLUMNS);
	}
}
