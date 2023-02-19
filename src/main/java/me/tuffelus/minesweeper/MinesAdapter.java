package me.tuffelus.minesweeper;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MinesAdapter extends MouseAdapter {

    Board board;

    MinesAdapter(Board board){
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent){

        if(mouseEvent.getButton() == MouseEvent.BUTTON1){
            board.leftClick(mouseEvent.getX(), mouseEvent.getY());
        }
        else{
            board.rightClick(mouseEvent.getX(), mouseEvent.getY());
        }
    }
}