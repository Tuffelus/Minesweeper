package me.tuffelus.minesweeper;

import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame {
    private Difficulty difficulty = Difficulty.HARD;
    private JLabel status;

    public Minesweeper(){
        status = new JLabel("");
        add(status, BorderLayout.SOUTH);
        add(new Board((status), difficulty));
        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args){
        Minesweeper minesweeper = new Minesweeper();
        minesweeper.setVisible(true);
    }
}