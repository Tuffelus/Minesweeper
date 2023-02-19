package me.tuffelus.minesweeper;

public enum TileType {
    EMPTY(0,0,0),
    NUMBERS(1,8,-1),
    BOMB(9,9),
    COVERED_EMPTY(10,10),
    COVERED_NUMBER(11,18,10),
    COVERED_BOMB(19,10),
    COVER(10,20,10),
    FLAG(20, 29,11),
    FALSE_FLAG(20,28,12);


    private final int firstIndex;
    private final int lastIndex;
    private final int imageIndex;

    TileType(int firstIndex, int lastIndex, int imageIndex){
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
        this.imageIndex = imageIndex;
    }
    TileType(int index, int ImageIndex){
        this(index, index, ImageIndex);
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public int getIndex(){
        return firstIndex;
    }

    public int getImageIndex() {
        return imageIndex;
    }
}
