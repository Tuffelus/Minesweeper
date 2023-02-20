package me.tuffelus.minesweeper;

public enum Difficulty {
    EASY(9, 9, 10, "Easy"),
    HARD(16, 16, 40, "Hard"),
    EXTREME(16, 30, 99, "Extreme");

    private final int columns;
    private final int rows;
    private final int bombsAmount;
    private final String difficulty;

    Difficulty(int columns, int rows, int bombsAmount, String difficulty) {
        this.columns = columns;
        this.rows = rows;
        this.bombsAmount = bombsAmount;
        this.difficulty = difficulty;
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

    public String getDifficultyName(){
        return difficulty;
    }
}
