package game.board;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javax.swing.JOptionPane;

public class Board_Clientside_Cell extends Polygon {

    // цветове за различните играчи:
    public static final Color[] COLOR_PLAYERS = {Color.CRIMSON, Color.DARKORANGE, Color.DARKVIOLET, Color.FORESTGREEN, Color.MAGENTA, Color.MEDIUMBLUE};
    // цвят за дъската:
    public static final Color COLOR_BOARD = Color.LIGHTGOLDENRODYELLOW;
    // цвят на мрежата:
    public static final Color COLOR_GRID = Color.BLACK;

    // дължина на страната на триъгълника от мрежата:
    public static final double SIDE_3 = 50.;
    // дължина на страната на квадрата от мрежата:
    public static final double SIDE_4 = 50.;
    // дължина на страната на шестоъгълника от мрежата:
    public static final double SIDE_6 = 50.;
    // дебелина на линиите от мрежата:
    public static final double GRID_WIDTH = 1;

    // координати на върховете на триъгълника
    public static final double[] COORDS_3
            = {-Board_Clientside_Cell.SIDE_3 * 0.500, Board_Clientside_Cell.SIDE_3 * 0.433,
                Board_Clientside_Cell.SIDE_3 * 0.500, Board_Clientside_Cell.SIDE_3 * 0.433,
                0.000, -Board_Clientside_Cell.SIDE_3 * 0.433};
    // координати на върховете на квадрата
    public static final double[] COORDS_4
            = {-Board_Clientside_Cell.SIDE_4 * 0.500, Board_Clientside_Cell.SIDE_4 * 0.500,
                Board_Clientside_Cell.SIDE_4 * 0.500, Board_Clientside_Cell.SIDE_4 * 0.500,
                Board_Clientside_Cell.SIDE_4 * 0.500, -Board_Clientside_Cell.SIDE_4 * 0.500,
                -Board_Clientside_Cell.SIDE_4 * 0.500, -Board_Clientside_Cell.SIDE_4 * 0.500
            };
    // координати на върховете на шестоъгълника
    public static final double[] COORDS_6
            = {-Board_Clientside_Cell.SIDE_6 * 0.866, Board_Clientside_Cell.SIDE_6 * 0.500,
                0.000, Board_Clientside_Cell.SIDE_6 * 1.000,
                Board_Clientside_Cell.SIDE_6 * 0.866, Board_Clientside_Cell.SIDE_6 * 0.500,
                Board_Clientside_Cell.SIDE_6 * 0.866, -Board_Clientside_Cell.SIDE_6 * 0.500,
                0.000, -Board_Clientside_Cell.SIDE_6 * 1.000,
                -Board_Clientside_Cell.SIDE_6 * 0.866, -Board_Clientside_Cell.SIDE_6 * 0.500
            };

    public final int rowId;
    public final int colId;
    public final Board_Clientside board;
    public Color color;

    public Board_Clientside_Cell(int rowId, int colId, Board_Clientside board) {
        // генерира се полигон със съответната форма и координати на върховете:
        super((board.boardShape == 3) ? (COORDS_3) : ((board.boardShape == 4) ? (COORDS_4) : (COORDS_6)));
        this.setFill(Board_Clientside_Cell.COLOR_BOARD);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(Board_Clientside_Cell.GRID_WIDTH);
        this.setStroke(Board_Clientside_Cell.COLOR_GRID);
        this.rowId = rowId;
        this.colId = colId;
        this.board = board;
        this.color = Board_Clientside_Cell.COLOR_BOARD;
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JOptionPane.showMessageDialog(null, "row: " + rowId + ", col: " + colId);
            }
        });
    }
}
