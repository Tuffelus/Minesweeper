package me.tuffelus.minesweeper;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Board extends JFrame{
    public Board(List<Tile> tiles) throws IOException {

        this.setName("Minesweeper");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 450);
        for (Tile tile : tiles) {
            this.add(tile);
        }
        this.setVisible(true);
    }
}
