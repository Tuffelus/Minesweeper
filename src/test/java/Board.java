import javax.swing.*;
import java.util.List;

public class Board extends JPanel {
    public Board(List<Tile> tiles, Difficulty difficulty) {
        JFrame jFrame = new JFrame("Minesweeper");
        jFrame.setResizable(true);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(16+16*difficulty.getWidth(), 39+16*difficulty.getHeight());


        for (Tile tile : tiles) {
            jFrame.add(tile);
        }
        jFrame.add(this);
        this.setVisible(false);
        jFrame.setVisible(true);
    }
}
