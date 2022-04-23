package me.tuffelus.minesweeper;

public enum Difficulty {
    EASY(81, 9, 10),
    HARD(256, 16, 40),
    EXTREME(480, 30, 99);

    private final int width;
    private final int height;
    private final int bombsAmount;

    Difficulty(int height, int width, int bombsAmount){
        this.width = width;
        this.height = height;

        if(getTileAmount() > bombsAmount){
            this.bombsAmount = bombsAmount;
        }else {
            this.bombsAmount = getTileAmount() - 1;
        }

    }

    public int getTileAmount() {
        return width * height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getBombsAmount() {
        return bombsAmount;
    }
}
