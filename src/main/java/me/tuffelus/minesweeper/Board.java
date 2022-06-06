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
    private static final int BOMB_AMOUNT = 40;
    private static final int ROWS = 16;
    private static final int COLUMNS = 16;

    //cell types
    private static final int COVER = 10;
    private static final int BOMB = 9;
    private static final int COVERED_BOMB = COVER + BOMB;
    private static final int FLAG = 10;
    private static final int EMPTY_CELL = 0;
    private static final int COVERED_EMPTY = COVER + EMPTY_CELL;


    private static final Dimension BOARD_SIZE = new Dimension(COLUMNS*IMAGE_SIZE+1, ROWS*IMAGE_SIZE+1);


    private static boolean inGame;
    private static int minesLeft;
    private static final Image[] img = new Image[AMOUNT_IMAGES];
    private static final int CELL_AMOUNT = ROWS * COLUMNS;
    private static int[] cells = new int[CELL_AMOUNT];

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

                if(currentColumn > 0 ){
                    cellToChange = pos - 1 - COLUMNS;
                    if(cellToChange >= 0){
                        if (cells[cellToChange] != COVERED_BOMB){
                            cells[cellToChange]++;
                        }
                    }

                    cellToChange = pos - 1;
                    if(cellToChange >= 0){
                        if (cells[cellToChange] != COVERED_BOMB){
                            cells[cellToChange]++;
                        }
                    }

                    cellToChange = pos - 1 + COLUMNS;
                    if(cellToChange < CELL_AMOUNT){
                        if (cells[cellToChange] != COVERED_BOMB){
                            cells[cellToChange]++;
                        }
                    }
                }

                cellToChange = pos - COLUMNS;
                if(cellToChange >= 0){
                    if(cells[cellToChange] != COVERED_BOMB){
                        cells[cellToChange]++;
                    }
                }

                cellToChange = pos + COLUMNS;
                if(cellToChange < CELL_AMOUNT){
                    if(cells[cellToChange] != COVERED_BOMB){
                        cells[cellToChange]++;
                    }
                }

                if(currentColumn < (COLUMNS - 1)){
                    cellToChange = pos + 1 - COLUMNS;
                    if(cellToChange >= 0){
                        if(cells[cellToChange] != COVERED_BOMB){
                            cells[cellToChange]++;
                        }
                    }

                    cellToChange = pos +1;
                    if(cellToChange < CELL_AMOUNT){
                        if (cells[cellToChange] != COVERED_BOMB){
                            cells[cellToChange]++;
                        }
                    }

                    cellToChange = pos + 1 + COLUMNS;
                    if(cellToChange < CELL_AMOUNT){
                        if (cells[cellToChange] != COVERED_BOMB){
                            cells[cellToChange]++;
                        }
                    }
                }
                minesLeft = BOMB_AMOUNT;
                status.setText(Integer.toString(minesLeft));
                count++;
            }
        }
    }

    public void revealAdjacent(int pos){
        int currentColumn = pos % COLUMNS;
        int cellToReveal;

        if(currentColumn > 0 ){
            cellToReveal = pos - 1 - COLUMNS;
            if(cellToReveal >= 0 && cells[cellToReveal] > 9){
                cells[cellToReveal] -= COVER;
                if(cells[cellToReveal] == EMPTY_CELL){
                    revealAdjacent(cellToReveal);
                }
            }

            cellToReveal = pos - 1;
            if(cellToReveal >= 0 && cells[cellToReveal] > 9){
                cells[cellToReveal] -= COVER;
                if(cells[cellToReveal] == EMPTY_CELL){
                    revealAdjacent(cellToReveal);
                }
            }

            cellToReveal = pos - 1 + COLUMNS;
            if(cellToReveal < CELL_AMOUNT && cells[cellToReveal] > 9){
                cells[cellToReveal] -= COVER;
                if(cells[cellToReveal] == EMPTY_CELL){
                    revealAdjacent(cellToReveal);
                }
            }
        }

        cellToReveal = pos - COLUMNS;
        if(cellToReveal >= 0 && cells[cellToReveal] > 9){
            cells[cellToReveal] -= COVER;
            if(cells[cellToReveal] == EMPTY_CELL){
                revealAdjacent(cellToReveal);
            }
        }

        cellToReveal = pos + COLUMNS;
        if(cellToReveal < CELL_AMOUNT && cells[cellToReveal] > 9){
            cells[cellToReveal] -= COVER;
            if(cells[cellToReveal] == EMPTY_CELL){
                revealAdjacent(cellToReveal);
            }
        }

        if(currentColumn < (COLUMNS - 1)){
            cellToReveal = pos + 1 - COLUMNS;
            if(cellToReveal >= 0 && cells[cellToReveal] > 9){
                cells[cellToReveal] -= COVER;
                if(cells[cellToReveal] == EMPTY_CELL){
                    revealAdjacent(cellToReveal);
                }

            }

            cellToReveal = pos +1;
            if(cellToReveal < CELL_AMOUNT && cells[cellToReveal] > 9){
                cells[cellToReveal] -= COVER;
                if(cells[cellToReveal] == EMPTY_CELL){
                    revealAdjacent(cellToReveal);
                }
            }

            cellToReveal = pos + 1 + COLUMNS;
            if(cellToReveal < CELL_AMOUNT && cells[cellToReveal] > 9){
                cells[cellToReveal] -= COVER;
                if(cells[cellToReveal] == EMPTY_CELL){
                    revealAdjacent(cellToReveal);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g){
        for (int y = 0; y < ROWS; y++){
            for(int x = 0; x < COLUMNS; x++){

                int cell = cells[(y*COLUMNS) + x];
                if(!inGame && (cell == COVERED_BOMB)){
                    cell = BOMB;
                }
                if(!inGame && (cell > 19 && cell < 29)){
                    cell = 12;
                }
                if(cell >= 10 && cell < 20){
                    cell = 10;
                }
                if(cell >= 20 && cell < 30){
                    cell = 11;
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
                    status.setText("You Lost! Game Over :(");
                }
                else if (cells[cursorCell] == COVERED_EMPTY){
                    cells[cursorCell] -= COVER;
                    revealAdjacent(cursorCell);
                }
                else if (cells[cursorCell] > 9 && cells[cursorCell] < 20){
                    cells[cursorCell] -= COVER;
                }
            }
            else{
                if((cells[cursorCell]) > 19){
                    cells[cursorCell] -= FLAG;
                    minesLeft++;
                    status.setText(Integer.toString(minesLeft));
                }
                else{
                    if(minesLeft > 0){
                        cells[cursorCell] += FLAG;
                        minesLeft--;
                        status.setText(Integer.toString(minesLeft));
                    }
                    else{
                        status.setText("No flags left");
                    }
                }
            }
            repaint();
        }
    }
}