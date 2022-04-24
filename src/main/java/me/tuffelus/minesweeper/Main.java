package me.tuffelus.minesweeper;

public class Main {
    public static void main(String[] args) {
        Game minesweeper = new Game(Difficulty.EASY);
        minesweeper.start();
    }
}
