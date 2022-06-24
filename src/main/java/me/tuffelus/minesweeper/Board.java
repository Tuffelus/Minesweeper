package me.tuffelus.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Board extends JPanel {
    //image config
    private static final int AMOUNT_IMAGES = 31;
    private static final int IMAGE_SIZE = 16;    //in pixels

    //difficulty config
    private static Difficulty difficulty = Difficulty.HARD;
    private static final int BOMB_AMOUNT = difficulty.getBombsAmount();
    private static final int ROWS = difficulty.getRows();
    private static final int COLUMNS = difficulty.getColumns();

    //cell types
    private static final int COVER = 10;
    private static final int BOMB = 9;
    private static final int COVERED_BOMB = COVER + BOMB;
    private static final int FLAG_VALUE = 10;
    private static final int FLAG_IMAGE = 11;
    private static final int FALSE_FLAG = 12;
    private static final int EMPTY_CELL = 0;
    private static final int COVERED_EMPTY = COVER + EMPTY_CELL;
    /*
    Lookup table:
        Empty:              0
        Numbers:            1-8
        Bomb:               9
        Covered Empty:      10
        Covered Number:     11-18
        Covered Bomb:       19
        Flag:               20-29
    */


    private static final Dimension BOARD_SIZE = new Dimension(COLUMNS*IMAGE_SIZE+1, ROWS*IMAGE_SIZE+1);

    private static boolean inGame;
    private static int minesLeft;
    private static final Image[] img = new Image[AMOUNT_IMAGES];
    private static final int CELL_AMOUNT = ROWS * COLUMNS;
    private static int CellsFlipped = 0;
    private static final int[] cells = new int[CELL_AMOUNT];

    private static JLabel status;

    public Board (JLabel status){
        Board.status = status;

        setPreferredSize(BOARD_SIZE);

        for (int i = 0;i < AMOUNT_IMAGES; i++){
            String path = "src/main/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter());
        newGame();
    }

    private void newGame(){
        inGame = true;

        CellsFlipped = 0;

        for(int i = 0; i < CELL_AMOUNT; i++){
            cells[i] = COVER;
        }

        Random random = new Random();
        int cellToChange;
        int count = 0;
        while (count < BOMB_AMOUNT){
            int pos = random.nextInt(CELL_AMOUNT-1);
            if(cells[pos] != COVERED_BOMB){
                cells[pos] = COVERED_BOMB;

                int currentColumn = pos % COLUMNS;

                if(currentColumn > 0 ){                     //Only checks to the left if there are cells
                    cellToChange = pos - 1 - COLUMNS;
                    checkCellForBombToZero(cellToChange);

                    cellToChange = pos - 1;
                    checkCellForBombToZero(cellToChange);

                    cellToChange = pos - 1 + COLUMNS;
                    checkCellForBombToTop(cellToChange);
                }
                                                            //left and right
                cellToChange = pos - COLUMNS;
                checkCellForBombToZero(cellToChange);

                cellToChange = pos + COLUMNS;
                checkCellForBombToTop(cellToChange);

                if(currentColumn < (COLUMNS - 1)){          //only checks to the right if there are cells
                    cellToChange = pos + 1 - COLUMNS;
                    checkCellForBombToZero(cellToChange);

                    cellToChange = pos +1;
                    checkCellForBombToTop(cellToChange);

                    cellToChange = pos + 1 + COLUMNS;
                    checkCellForBombToTop(cellToChange);
                }
                minesLeft = BOMB_AMOUNT;
                status.setText(Integer.toString(minesLeft));
                count++;
            }
        }
    }

    public void checkCellForBombToZero(int cellToChange){   //checks if the cell has a negative Index
        if(cellToChange >= 0){
            checkCellForBomb(cellToChange);
        }
    }
    public void checkCellForBombToTop(int cellToChange){    //checks if the cell has a higher than possible Index
        if(cellToChange < CELL_AMOUNT){
            checkCellForBomb(cellToChange);
        }
    }
    public void checkCellForBomb(int cellToChange){
        if(cells[cellToChange] != COVERED_BOMB){
            cells[cellToChange]++;
        }
    }

    public void revealAdjacent(int pos){
        int currentColumn = pos % COLUMNS;
        int cellToReveal;

        if(currentColumn > 0 ){
            cellToReveal = pos - 1 - COLUMNS;
            checkIfEmptyLeft(cellToReveal);

            cellToReveal = pos - 1;
            checkIfEmptyLeft(cellToReveal);

            cellToReveal = pos - 1 + COLUMNS;
            checkIfEmptyRight(cellToReveal);
        }

        cellToReveal = pos - COLUMNS;
        checkIfEmptyLeft(cellToReveal);

        cellToReveal = pos + COLUMNS;
        if(cellToReveal < CELL_AMOUNT && cells[cellToReveal] > 9){
            revealCell(cellToReveal);
        }

        if(currentColumn < (COLUMNS - 1)){
            cellToReveal = pos + 1 - COLUMNS;
            checkIfEmptyLeft(cellToReveal);

            cellToReveal = pos +1;
            checkIfEmptyRight(cellToReveal);

            cellToReveal = pos + 1 + COLUMNS;
            checkIfEmptyRight(cellToReveal);
        }
        status.setText(Integer.toString(minesLeft));
    }

    public void checkIfEmptyLeft(int cellToCheck){
        if(cellToCheck >= 0 && cells[cellToCheck] > 9){
            revealCell(cellToCheck);
        }
    }
    public void checkIfEmptyRight(int cellToCheck){
        if(cellToCheck < CELL_AMOUNT && cells[cellToCheck] > 9){
            revealCell(cellToCheck);
        }
    }

    public void revealCell(int cellToReveal){
        if(cells[cellToReveal] >= 10 && cells[cellToReveal] <= 18){
            cells[cellToReveal] -= COVER;
            CellsFlipped++;
            System.out.println(CellsFlipped);
        }else if(cells[cellToReveal] == 19){
            inGame = false;
            status.setText("You Lost! Game Over XD");
        }

        if(cells[cellToReveal] == EMPTY_CELL){
            revealAdjacent(cellToReveal);
        }
    }

    public int checkIfFlagLeft(int cellToCheck){
        if(cellToCheck >= 0 && cells[cellToCheck] > 19  && cells[cellToCheck] <= 29){ //Range of Flag Index
            return 1;
        }
        return 0;
    }
    public int checkIfFlagRight(int cellToCheck){
        if(cellToCheck < CELL_AMOUNT && cells[cellToCheck] > 19  && cells[cellToCheck] <= 29){ //Range of Flag Index
            return 1;
        }
        return 0;
    }

    public int amountFlagsAdjacent(int pos){
        int count = 0;
        int currentColumn = pos % COLUMNS;
        int cellToCheck;

        if(currentColumn > 0 ){
            cellToCheck = pos - 1 - COLUMNS;
            count += checkIfFlagLeft(cellToCheck);

            cellToCheck = pos - 1;
            count += checkIfFlagLeft(cellToCheck);

            cellToCheck = pos - 1 + COLUMNS;
            count += checkIfFlagRight(cellToCheck);
        }

        cellToCheck = pos - COLUMNS;
        count += checkIfFlagLeft(cellToCheck);

        cellToCheck = pos + COLUMNS;
        count += checkIfFlagRight(cellToCheck);

        if(currentColumn < (COLUMNS - 1)){
            cellToCheck = pos + 1 - COLUMNS;
            count += checkIfFlagLeft(cellToCheck);

            cellToCheck = pos +1;
            count += checkIfFlagRight(cellToCheck);

            cellToCheck = pos + 1 + COLUMNS;
            count += checkIfFlagRight(cellToCheck);
        }

        return count;
    }

    @Override
    public void paintComponent(Graphics g){
        if(CellsFlipped + BOMB_AMOUNT == CELL_AMOUNT){
            status.setText("You won! :D");
            inGame = false;
        }
        for (int y = 0; y < ROWS; y++){
            for(int x = 0; x < COLUMNS; x++){

                int cell = cells[(y*COLUMNS) + x];
                if(!inGame && (cell == COVERED_BOMB)){
                    cell = BOMB;
                }
                if(cell >= 10 && cell < 20){                //Range of uncovered cells
                    cell = COVER;
                }
                if(inGame && cell >= 20 && cell < 30){      //Range of flag Index with Bombs
                    cell = FLAG_IMAGE;
                }
                if(!inGame && (cell >= 20 && cell <= 28)){  //Range of Flag Index with NO Bombs
                    cell = FALSE_FLAG;
                }
                g.drawImage(img[cell], x * IMAGE_SIZE, y * IMAGE_SIZE, this);
            }
        }
    }

    private class MinesAdapter extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e){
            if(!inGame){
                newGame();
                repaint();
                return;
            }

            int x = e.getX();
            int y = e.getY();

            int cursorColumn = x/IMAGE_SIZE;
            int cursorRow = y/IMAGE_SIZE;

            int cursorCell = ROWS * cursorRow + cursorColumn;

            if(e.getButton() == MouseEvent.BUTTON1){
                if(cells[cursorCell] == COVERED_BOMB){
                    inGame = false;
                    status.setText("You Lost! Game Over XD");
                }
                else if (cells[cursorCell] == COVERED_EMPTY){
                    cells[cursorCell] -= COVER;
                    CellsFlipped++;
                    System.out.println(CellsFlipped);
                    revealAdjacent(cursorCell);
                }
                else if (cells[cursorCell] > 9 && cells[cursorCell] < 20) { //Range of covers
                    cells[cursorCell] -= COVER;
                    CellsFlipped++;
                    System.out.println(CellsFlipped);
                }
                else if (cells[cursorCell] > 0 && cells[cursorCell] < 9){ //Range of uncovered numbers
                    if (cells[cursorCell] == amountFlagsAdjacent(cursorCell)){
                        revealAdjacent(cursorCell);
                    }
                }
            }
            else{
                if((cells[cursorCell]) > 19){               //is already a flag
                    cells[cursorCell] -= FLAG_VALUE;
                    minesLeft++;
                    status.setText(Integer.toString(minesLeft));
                }
                else{
                    if(minesLeft > 0 && cells[cursorCell] >= COVER){
                        cells[cursorCell] += FLAG_VALUE;
                        minesLeft--;
                        status.setText(Integer.toString(minesLeft));
                    }
                    else if(cells[cursorCell] >= COVER){
                        status.setText("No flags left");
                    }
                }
            }
            repaint();
        }
    }
}