package me.tuffelus.minesweeper;

public class Main {
    public static void main(String[] args) {
        Game Minesweeper = new Game(Difficulty.EASY);
        Game.start();
    }
}
