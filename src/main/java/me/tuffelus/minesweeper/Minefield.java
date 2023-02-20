package me.tuffelus.minesweeper;

import java.util.Random;
import java.util.function.Consumer;

public class Minefield {

    Difficulty difficulty;

    private static int BOMB_AMOUNT;
    private static int ROWS;
    private static int COLUMNS;
    private static int CELL_AMOUNT;

    private static int[] cells;

    public Minefield(Difficulty difficulty){

        this.difficulty = difficulty;

        determineSize();
        cells = new int[CELL_AMOUNT];

        initializeCells();
        populateRandomBombs();
    }

    public void determineSize(){
        BOMB_AMOUNT = difficulty.getBombsAmount();
        ROWS = difficulty.getRows();
        COLUMNS = difficulty.getColumns();
        CELL_AMOUNT = ROWS * COLUMNS;
    }

    public void initializeCells() {
        for (int i = 0; i < CELL_AMOUNT; i++) {
            cells[i] = TileType.COVER.getIndex();
        }
    }

    public void populateRandomBombs() {

        int count = 0;
        Random random = new Random();
        while (count < BOMB_AMOUNT) {
            int pos = random.nextInt(CELL_AMOUNT - 1);
            if (cells[pos] != TileType.COVERED_BOMB.getIndex()) {

                cells[pos] = TileType.COVERED_BOMB.getIndex();
                manipulateAdjacentTiles(pos, this::incrementIfIsNumberTileAndInBounds);
                count++;
            }
        }
    }

    public void incrementIfIsNumberTileAndInBounds(int cellToChange) {

        if (cellToChange >= 0 && cellToChange < CELL_AMOUNT) {
            if (cells[cellToChange] != TileType.COVERED_BOMB.getIndex()) {
                cells[cellToChange]++;
            }
        }
    }

    public void manipulateAdjacentTiles(int pos, Consumer<Integer> consumer) {

        int currentColumn = pos % COLUMNS;
        for (int row = -1; row <= 1; row++) {
            for (int column = -COLUMNS; column <= COLUMNS; column += COLUMNS) {

                int cellToManipulate = pos + row + column;
                if (currentColumn + row >= 0 && currentColumn + row <= (COLUMNS - 1)) {
                    if (cellToManipulate != pos) {
                        consumer.accept(cellToManipulate);
                    }
                }
            }
        }
    }

    public boolean checkForFlag(int cellToCheck) {
        return cellToCheck < CELL_AMOUNT && cellToCheck >= 0 && cells[cellToCheck] > 19 && cells[cellToCheck] <= 29;
    }

    public int amountFlagsAdjacent(int pos) {

        int count = 0;
        int currentColumn = pos % COLUMNS;
        for (int col = -1; col <= 1; col++) {
            for (int row = -COLUMNS; row <= COLUMNS; row += COLUMNS) {

                int cellToManipulate = pos + col + row;
                if (currentColumn + col >= 0 && currentColumn + col <= (COLUMNS - 1)) {
                    if (cellToManipulate != pos) {
                        if (checkForFlag(cellToManipulate)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public void addToCellValue(int cursorCell, int value){
        cells[cursorCell] += value;
    }

    public int getCellAmount(){
        return CELL_AMOUNT;
    }

    public int getCellAtIndex(int index){
        return cells[index];
    }
}
