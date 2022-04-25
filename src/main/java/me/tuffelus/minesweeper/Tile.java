package me.tuffelus.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Tile extends JButton {
    final int x;
    final int y;
    TileState tileState;
    boolean revealed = false;

    public Tile(int x, int y, TileState tileState) {
        this.x = x;
        this.y = y;
        this.tileState = tileState;
        this.setBounds(x,y,16,16);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);

        this.addActionListener(e -> {
            clicked();
            System.out.println("Clicked at " + this.x + " " + this.y);
        });


        setImage();
    }


    public void setImage(){
        ImageIcon image;
        if(!revealed){
            image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cellup.png")));
        }
        else{
            switch (tileState){
                case BOMB -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cellmine.png")));
                case EMPTY -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("celldown.png")));
                default -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cellup.png")));
            }
        }

        this.setIcon(image);
    }

    public void setTileState(TileState tileState) {
        this.tileState = tileState;
        setImage();
    }

    public void clicked(){
        revealed = !revealed;
        setImage();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileState getTileState() {
        return tileState;
    }

}
