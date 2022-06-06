import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    static Difficulty difficulty = Difficulty.EASY;
    static List<Tile> tiles = new ArrayList<>();

    public Game(Difficulty difficulty){
        Game.difficulty = difficulty;
    }

    public static void start(){
        System.out.println(difficulty);

        fillListWithEmptyTiles(tiles);
        randomiseBombs(difficulty.getBombsAmount());
        calcNumbers();

        Board board = new Board(tiles, difficulty);
    }

    public static void fillListWithEmptyTiles(List<Tile> tiles){
        int i = 0;
        for(int y = 0; y < difficulty.getHeight(); y++){
            for(int x = 0; x < difficulty.getWidth(); x++){
                tiles.add(new Tile(x*16,y*16,i++, TileState.EMPTY));
            }
        }
    }

    public static int randomiseBombs(int remaining){
        if(remaining <= 0){
            return -1;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(tiles.size());

        if(tiles.get(randomNumber).getTileState() != TileState.BOMB){
            tiles.get(randomNumber).setTileState(TileState.BOMB);
            return randomiseBombs(--remaining);
        }
        return randomiseBombs(remaining);
    }

    public static void calcNumbers(){
        for (Tile tile : tiles) {
            if(tile.tileState != TileState.BOMB) {
                switch (bombsAdjacent(tile)) {
                    case 1 -> tile.setTileState(TileState.ONE);
                    case 2 -> tile.setTileState(TileState.TWO);
                    case 3 -> tile.setTileState(TileState.THREE);
                    case 4 -> tile.setTileState(TileState.FOUR);
                    case 5 -> tile.setTileState(TileState.FIVE);
                    case 6 -> tile.setTileState(TileState.SIX);
                    case 7 -> tile.setTileState(TileState.SEVEN);
                    case 8 -> tile.setTileState(TileState.EIGHT);
                    default -> tile.setTileState(TileState.EMPTY);
                }
            }
        }
    }
    public static int bombsAdjacent(Tile tile){
        int amountBombs = 0;

        boolean blockedUp = tile.getY()==0;
        boolean blockedLeft = tile.getX()==0;
        boolean blockedRight = tile.getX()/16 ==difficulty.getWidth()-1;
        boolean blockedBottom = tile.getY()/16 ==difficulty.getHeight()-1;

        if (!blockedUp && !blockedLeft) {
            if (tiles.get(tile.getIndex() - difficulty.getWidth() - 1).getTileState() == TileState.BOMB) {
                amountBombs++;
            }
        }
        if (!blockedUp && !blockedRight) {
            if (tiles.get(tile.getIndex() - difficulty.getWidth() + 1).getTileState() == TileState.BOMB) {
                amountBombs++;
            }
        }
        if (!blockedUp) {
            if (tiles.get(tile.getIndex() - difficulty.getWidth()).getTileState() == TileState.BOMB) {
                amountBombs++;
            }
        }
        if (!blockedLeft) {
            if (tiles.get(tile.getIndex() - 1).getTileState() == TileState.BOMB) {
                amountBombs++;
            }
        }
        if (!blockedRight) {
            if (tiles.get(tile.getIndex() + 1).getTileState() == TileState.BOMB) {
                amountBombs++;
            }
        }
        if (!blockedBottom && !blockedLeft) {
            if (tiles.get(tile.getIndex() + difficulty.getWidth() - 1).getTileState() == TileState.BOMB) {
                amountBombs++;
            }
        }
        if (!blockedBottom) {
            if (tiles.get(tile.getIndex() + difficulty.getWidth()).getTileState() == TileState.BOMB) {
                amountBombs++;
            }
        }
        if (!blockedBottom && !blockedRight) {
            if (tiles.get(tile.getIndex() + difficulty.getWidth() + 1).getTileState() == TileState.BOMB) {
                amountBombs++;
            }
        }
        return amountBombs;
    }

    public static void gameOver(){
        for(Tile tile : tiles){
            tile.setRevealed();
        }
    }
}