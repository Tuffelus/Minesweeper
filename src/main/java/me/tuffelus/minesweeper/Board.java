package me.tuffelus.minesweeper;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private static final int AMOUNT_IMAGES = 13;
    private static final int IMAGE_PIXELS = 16;

    private final Difficulty difficulty;
    private static int COLUMNS;
    private static int ROWS;
    private static int BOMB_AMOUNT;

    private static final int FLAG_VALUE = 10;

    private static Dimension BOARD_SIZE;

    private static boolean inGame;
    private static int minesLeft;
    private static final Image[] img = new Image[AMOUNT_IMAGES];
    private static int CellsRevealed;

    Minefield minefield;

    private static JLabel status;

    public Board(JLabel status, Difficulty difficulty) {

        this.difficulty = difficulty;
        getInfoFromDifficulty();

        Board.status = status;

        setPreferredSize(BOARD_SIZE);

        for (int i = 0; i < AMOUNT_IMAGES; i++) {
            String path = "src/main/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter(this));
        newGame();
    }

    public void getInfoFromDifficulty(){
        COLUMNS = difficulty.getColumns();
        ROWS = difficulty.getRows();
        BOMB_AMOUNT = difficulty.getBombsAmount();
        BOARD_SIZE = new Dimension(COLUMNS * IMAGE_PIXELS + 1, ROWS * IMAGE_PIXELS + 1);
    }

    private void newGame() {
        inGame = true;

        minefield = new Minefield(difficulty);
        CellsRevealed = 0;
        minesLeft = BOMB_AMOUNT;

        status.setText(Integer.toString(minesLeft));
    }

    public int getImageIndexOfCell(int x, int y) {

        int cell = minefield.getCellAtIndex((y * COLUMNS) + x);
        if (!inGame && (cell == TileType.COVERED_BOMB.getIndex())) {
            cell = TileType.BOMB.getImageIndex();
        }
        if (cell >= TileType.COVER.getFirstIndex() && cell <= TileType.COVER.getLastIndex()) {
            cell = TileType.COVER.getImageIndex();
        }
        if (inGame && cell >= TileType.FLAG.getFirstIndex() && cell <= TileType.FLAG.getLastIndex()) {
            cell = TileType.FLAG.getImageIndex();
        }
        if (!inGame && (cell >= TileType.FALSE_FLAG.getFirstIndex() && cell <= TileType.FALSE_FLAG.getLastIndex())) {
            cell = TileType.FALSE_FLAG.getImageIndex();
        }
        return cell;
    }

    public void revealCell(int cellToReveal) {

        int cell = minefield.getCellAtIndex(cellToReveal);

        if (cell >= TileType.COVERED_EMPTY.getFirstIndex() && cell <= TileType.COVERED_NUMBER.getLastIndex()) {
            minefield.addToCellValue(cellToReveal, -TileType.COVER.getIndex());
            CellsRevealed++;
            System.out.println(CellsRevealed);
        } else if (cell == TileType.COVERED_BOMB.getIndex()) {
            status.setText("You Lost! Game Over XD");
            inGame = false;
        }
        cell = minefield.getCellAtIndex(cellToReveal);

        if (cell == TileType.EMPTY.getIndex()) {
            minefield.manipulateAdjacentTiles(cellToReveal, this::checkIfEmpty);
        }
    }

    public void checkIfEmpty(int cellToCheck) {

        if (cellToCheck < minefield.getCellAmount() && cellToCheck >= 0 && minefield.getCellAtIndex(cellToCheck) > 9) {
            revealCell(cellToCheck);
        }
    }

    public void leftClick(int xOnBoard, int yOnBoard){

        int cursorCell = determineClickedCell(xOnBoard, yOnBoard);
        int cell = minefield.getCellAtIndex(cursorCell);

        if(!inGame){
            newGame();
        }
        if(cell >= TileType.NUMBERS.getFirstIndex() && cell < TileType.NUMBERS.getLastIndex()){
            if (cell == minefield.amountFlagsAdjacent(cursorCell)){
                minefield.manipulateAdjacentTiles(cursorCell, this::checkIfEmpty);
                status.setText(Integer.toString(minesLeft));
            }
        }
        revealCell(cursorCell);
        repaint();
    }

    public void rightClick(int xOnBoard, int yOnBoard){

        if(!inGame){
            newGame();
            repaint();
            return;
        }

        int cursorCell = determineClickedCell(xOnBoard, yOnBoard);
        int cell = minefield.getCellAtIndex(cursorCell);

        if(cell > TileType.FLAG.getFirstIndex()){
            takeFlag(cursorCell);
            status.setText(Integer.toString(minesLeft));
        }
        else{
            placeFlag(cursorCell);
        }
        repaint();
    }

    public int determineClickedCell(int xOnBoard, int yOnBoard){

        int cursorColumn = xOnBoard/IMAGE_PIXELS;
        int cursorRow = yOnBoard/IMAGE_PIXELS;

        return ROWS * cursorRow + cursorColumn;
    }

    public void takeFlag(int cursorCell){
        minefield.addToCellValue(cursorCell, -FLAG_VALUE);
        minesLeft++;
        status.setText(Integer.toString(minesLeft));
    }

    public void placeFlag(int cursorCell){
        int cell = minefield.getCellAtIndex(cursorCell);
        if(minesLeft > 0 && cell >= TileType.COVER.getIndex()){
            minefield.addToCellValue(cursorCell, FLAG_VALUE );
            minesLeft--;
            status.setText(Integer.toString(minesLeft));
        }
        else if(cell >= TileType.COVER.getIndex()){
            status.setText("No flags left");
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        if (CellsRevealed + BOMB_AMOUNT == minefield.getCellAmount()) {
            status.setText("You won! :D");
            inGame = false;
        }

        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Image image = img[getImageIndexOfCell(x, y)];
                g.drawImage(image, x * IMAGE_PIXELS, y * IMAGE_PIXELS, this);
            }
        }
    }
}