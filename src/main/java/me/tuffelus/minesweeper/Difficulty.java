package me.tuffelus.minesweeper;

public enum Difficulty {
    EASY(9, 9, 10),
    HARD(16, 16, 40),
    EXTREME(16, 30, 99);

    private final int columns;
    private final int rows;
    private final int bombsAmount;

    Difficulty(int columns, int rows, int bombsAmount) {
        this.columns = columns;
        this.rows = rows;
        this.bombsAmount = bombsAmount;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getBombsAmount() {
        return bombsAmount;
    }
}
