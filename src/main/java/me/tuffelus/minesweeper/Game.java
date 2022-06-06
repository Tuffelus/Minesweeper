package me.tuffelus.minesweeper;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private JLabel status;

    public Game(){
        status = new JLabel("");
        add(status, BorderLayout.SOUTH);
        add(new Board(status));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Game minesweeper = new Game();
        minesweeper.setVisible(true);
    }
}