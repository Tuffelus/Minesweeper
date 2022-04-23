package me.tuffelus.minesweeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {


    static Difficulty difficulty;

    public Game(Difficulty difficulty){
        Game.difficulty = difficulty;
    }

    public static void start(){
        List<Tile> tiles = new ArrayList<>();
        fillListWithEmptyTiles(tiles);
        randomiseBombs(tiles, difficulty.getBombsAmount());
        try{
            Board board = new Board(tiles);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void fillListWithEmptyTiles(List<Tile> tiles){
        for(int y = 0; y < difficulty.getHeight(); y++){
            for(int x = 0; x < difficulty.getWidth(); x++){
                tiles.add(new Tile(x,y, TileState.EMPTY));
            }
        }
    }

    public static int randomiseBombs(List<Tile> tiles, int remaining){
        if(remaining <= 0){
            return -1;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(tiles.size()+1);

        if(tiles.get(randomNumber).getTileState() != TileState.BOMB){
            tiles.get(randomNumber).setTileState(TileState.BOMB);
            return randomiseBombs(tiles, --remaining);
        }
        return randomiseBombs(tiles, remaining);
    }



}
