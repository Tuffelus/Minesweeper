package me.tuffelus.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.function.Consumer;

public class Board extends JPanel {
    private static final int AMOUNT_IMAGES = 31;
    private static final int IMAGE_PIXELS = 16;

    private static final Difficulty difficulty = Difficulty.HARD;
    private static final int BOMB_AMOUNT = difficulty.getBombsAmount();
    private static final int ROWS = difficulty.getRows();
    private static final int COLUMNS = difficulty.getColumns();

    private static final int FLAG_VALUE = 10;

    private static final Dimension BOARD_SIZE = new Dimension(COLUMNS * IMAGE_PIXELS + 1, ROWS * IMAGE_PIXELS + 1);

    private static boolean inGame;
    private static int minesLeft;
    private static final Image[] img = new Image[AMOUNT_IMAGES];
    private static final int CELL_AMOUNT = ROWS * COLUMNS;
    private static int CellsRevealed;
    private static final int[] cells = new int[CELL_AMOUNT];

    private static JLabel status;

    public Board(JLabel status) {
        Board.status = status;

        setPreferredSize(BOARD_SIZE);

        for (int i = 0; i < AMOUNT_IMAGES; i++) {
            String path = "src/main/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }

        addMouseListener(new MinesAdapter(this));
        newGame();
    }

    private void newGame() {
        inGame = true;

        initializeCells();
        populateRandomBombs();

        status.setText(Integer.toString(minesLeft));
    }

    public void initializeCells() {
        CellsRevealed = 0;
        for (int i = 0; i < CELL_AMOUNT; i++) {
            cells[i] = TileType.COVER.getIndex();
        }
    }

    public void populateRandomBombs() {
        int count = 0;
        Random random = new Random();
        while (count < BOMB_AMOUNT) {
            int pos = random.nextInt(CELL_AMOUNT - 1);
            if (cells[pos] != TileType.COVERED_BOMB.getIndex()) {

                cells[pos] = TileType.COVERED_BOMB.getIndex();
                manipulateAdjacentTiles(pos, this::incrementIfIsNumberTileAndInBounds);

                minesLeft = BOMB_AMOUNT;
                count++;
            }
        }
    }

    public void manipulateAdjacentTiles(int pos, Consumer<Integer> consumer) {
        int currentColumn = pos % COLUMNS;
        for (int col = -1; col <= 1; col++) {
            for (int row = -COLUMNS; row <= COLUMNS; row += COLUMNS) {

                int cellToManipulate = pos + col + row;
                if (currentColumn + col >= 0 && currentColumn + col <= (COLUMNS - 1)) {
                    if (cellToManipulate != pos) {
                        consumer.accept(cellToManipulate);
                    }
                }
            }
        }
    }

    public void incrementIfIsNumberTileAndInBounds(int cellToChange) {
        if (cellToChange >= 0 && cellToChange < CELL_AMOUNT) {
            if (cells[cellToChange] != TileType.COVERED_BOMB.getIndex()) {
                cells[cellToChange]++;
            }
        }
    }

    public void checkIfEmpty(int cellToCheck) {
        if (cellToCheck < CELL_AMOUNT && cellToCheck >= 0 && cells[cellToCheck] > 9) {
            revealCell(cellToCheck);
        }
    }

    public void revealCell(int cellToReveal) {
        if (cells[cellToReveal] >= 10 && cells[cellToReveal] <= 18) {
            cells[cellToReveal] -= TileType.COVER.getIndex();
            CellsRevealed++;
            System.out.println(CellsRevealed);
        } else if (cells[cellToReveal] == 19) {
            status.setText("You Lost! Game Over XD");
            inGame = false;
        }

        if (cells[cellToReveal] == TileType.EMPTY.getIndex()) {
            manipulateAdjacentTiles(cellToReveal, this::checkIfEmpty);
        }
    }

    public boolean checkForFlag(int cellToCheck) {
        return cellToCheck < CELL_AMOUNT && cellToCheck >= 0 && cells[cellToCheck] > 19 && cells[cellToCheck] <= 29;
    }

    public int amountFlagsAdjacent(int pos) {
        int count = 0;
        int currentColumn = pos % COLUMNS;
        for (int col = -1; col <= 1; col++) {
            for (int row = -COLUMNS; row <= COLUMNS; row += COLUMNS) {

                int cellToManipulate = pos + col + row;
                if (currentColumn + col >= 0 && currentColumn + col <= (COLUMNS - 1)) {
                    if (cellToManipulate != pos) {
                        if (checkForFlag(cellToManipulate)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    @Override
    public void paintComponent(Graphics g) {
        if (CellsRevealed + BOMB_AMOUNT == CELL_AMOUNT) {
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

    public int getImageIndexOfCell(int x, int y) {
        int cell = cells[(y * COLUMNS) + x];
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


    public void leftClick(int xOnBoard, int yOnBoard){

        int cursorCell = determineClickedCell(xOnBoard, yOnBoard);

        if(!inGame){
            newGame();
        }
        if(cells[cursorCell] >= TileType.NUMBERS.getFirstIndex() && cells[cursorCell] < TileType.NUMBERS.getLastIndex()){
            if (cells[cursorCell] == amountFlagsAdjacent(cursorCell)){
                manipulateAdjacentTiles(cursorCell, this::checkIfEmpty);
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

        if((cells[cursorCell]) > TileType.FLAG.getFirstIndex()){
            takeFlag(cursorCell);
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
        cells[cursorCell] -= FLAG_VALUE;
        minesLeft++;
        status.setText(Integer.toString(minesLeft));
    }

    public void placeFlag(int cursorCell){
        if(minesLeft > 0 && cells[cursorCell] >= TileType.COVER.getIndex()){
            cells[cursorCell] += FLAG_VALUE;
            minesLeft--;
            status.setText(Integer.toString(minesLeft));
        }
        else if(cells[cursorCell] >= TileType.COVER.getIndex()){
            status.setText("No flags left");
        }
    }
}