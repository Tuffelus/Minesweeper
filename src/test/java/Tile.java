import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Tile extends JButton {
    final int x;
    final int y;
    final int index;
    TileState tileState;
    boolean revealed = false;
    boolean flag = false;



    public Tile(int x, int y, int index, TileState tileState) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.tileState = tileState;
        this.setBounds(x,y,16,16);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getButton() == MouseEvent.BUTTON3){
                    leftClicked();
                }
                if(e.getButton() == MouseEvent.BUTTON1){
                    rightClicked();
                    setImage();
                }
            }
        });

    }


    public void setImage(){
        ImageIcon image;
        if(!revealed){
            image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cellup.png")));
        }else if(flag){
            image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cellflag.png")));
        }
        else{
            switch (tileState){
                case BOMB -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cellmine.png")));
                case EMPTY -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("celldown.png")));
                case ONE -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cell1.png")));
                case TWO -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cell2.png")));
                case THREE -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cell3.png")));
                case FOUR -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cell4.png")));
                case FIVE -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cell5.png")));
                case SIX -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cell6.png")));
                case SEVEN -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cell7.png")));
                case EIGHT -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cell8.png")));
                case BLAST -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("blast.png")));
                default -> image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("cellup.png")));
            }
        }
        this.setIcon(image);
    }

    public void setTileState(TileState tileState) {
        this.tileState = tileState;
        setImage();
    }

    public void rightClicked(){
        revealed = true;
        if(tileState == TileState.BOMB){
            setTileState(TileState.BLAST);
            Game.gameOver();
        }else{
            setImage();
        }
    }
    public void leftClicked(){
        flag = !flag;
        setImage();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getIndex() {
        return index;
    }

    public void setRevealed(){
        revealed = true;
        setImage();
    }

    public TileState getTileState() {
        return tileState;
    }

}
