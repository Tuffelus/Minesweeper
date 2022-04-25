package me.tuffelus.minesweeper;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Board extends JPanel{
    public Board(List<Tile> tiles) {
        JFrame jFrame = new JFrame("Minesweeper");

        jFrame.setResizable(true);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(160, 183);
        this.setVisible(true);

        for (Tile tile : tiles) {
            jFrame.add(tile);
        }
        jFrame.add(this);
        jFrame.setVisible(true);
    }
}
