package me.tuffelus.minesweeper;

import javax.swing.*;

public class Tile extends JLabel {
    final int x;
    final int y;
    TileState tileState;
    final static String IMAGE_SOURCE_FOLDER = "D:\\!Dateien\\Programieren\\Java\\Minesweeper\\src\\main\\resources\\";

    public Tile(int x, int y, TileState tileState) {
        this.x = x;
        this.y = y;
        this.tileState = tileState;
        setImage();
    }

    public void setTileState(TileState tileState) {
        this.tileState = tileState;
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

    public void setImage(){
        ImageIcon image;
        switch (tileState){
            case BOMB -> image = new ImageIcon(IMAGE_SOURCE_FOLDER + "cellmine.png");
            case EMPTY -> image = new ImageIcon(IMAGE_SOURCE_FOLDER + "celldown.png");
            default -> image = new ImageIcon(IMAGE_SOURCE_FOLDER + "cellup.png");
        }
        this.setIcon(image);
    }


}
