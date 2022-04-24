package me.tuffelus.minesweeper;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Board extends JPanel{
    public Board(List<Tile> tiles) {
        JFrame jFrame = new JFrame("Minesweeper");

        jFrame.setResizable(false);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(160, 183);

        for (Tile tile : tiles) {
            this.add(tile);
        }
        jFrame.add(this);
        jFrame.setVisible(true);
    }
}
