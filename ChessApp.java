import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
public class ChessApp extends Application {

    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private Tile[][] tiles = new Tile[BOARD_SIZE][BOARD_SIZE];

    private Piece selectedPiece = null;
    private Tile selectedTile = null;
    private boolean whiteToMove = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean light = (row + col) % 2 == 0;
                Tile tile = new Tile(row, col, light);
                tiles[row][col] = tile;
                root.add(tile, col, row);
            }
        }

        setupInitialPosition();

        Scene scene = new Scene(root, BOARD_SIZE * TILE_SIZE, BOARD_SIZE * TILE_SIZE);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ChessApp - JavaFX");
        primaryStage.show();
    }

    enum PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }

    class Piece {
        PieceType type;
        boolean white;

        Piece(PieceType type, boolean white) {
            this.type = type;
            this.white = white;
        }

        String symbol() {
            switch (type) {
                case KING:   return white ? "♔" : "♚";
                case QUEEN:  return white ? "♕" : "♛";
                case ROOK:   return white ? "♖" : "♜";
                case BISHOP: return white ? "♗" : "♝";
                case KNIGHT: return white ? "♘" : "♞";
                case PAWN:   return white ? "♙" : "♟";
            }
            return "?";
        }
    }

    class Tile extends StackPane {
        int row, col;
        Rectangle rect;
        Label label;
        Piece piece;

        Tile(int row, int col, boolean light) {
            this.row = row;
            this.col = col;

            rect = new Rectangle(TILE_SIZE, TILE_SIZE);
            rect.setFill(light ? Color.BEIGE : Color.SADDLEBROWN);

            label = new Label("");
            label.setFont(Font.font(40));
            setAlignment(Pos.CENTER);

            getChildren().addAll(rect, label);

            setOnMouseClicked(e -> handleClick(this));
        }

        void setPiece(Piece p) {
            this.piece = p;
            if (p == null) {
                label.setText("");
            } else {
                label.setText(p.symbol());
                label.setTextFill(p.white ? Color.WHITE : Color.BLACK);
            }
        }

        void highlight(boolean on) {
            if (on) {
                rect.setStroke(Color.YELLOW);
                rect.setStrokeWidth(3);
            } else {
                rect.setStroke(null);
                rect.setStrokeWidth(0);
            }
        }

        boolean isOccupied() {
            return piece != null;
        }
    }

    private void setupInitialPosition() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                tiles[r][c].setPiece(null);
            }
        }

        for (int c = 0; c < 8; c++) {
            tiles[6][c].setPiece(new Piece(PieceType.PAWN, true));
            tiles[1][c].setPiece(new Piece(PieceType.PAWN, false));
        }

        tiles[7][0].setPiece(new Piece(PieceType.ROOK, true));
        tiles[7][1].setPiece(new Piece(PieceType.KNIGHT, true));
        tiles[7][2].setPiece(new Piece(PieceType.BISHOP, true));
        tiles[7][3].setPiece(new Piece(PieceType.QUEEN, true));
        tiles[7][4].setPiece(new Piece(PieceType.KING, true));
        tiles[7][5].setPiece(new Piece(PieceType.BISHOP, true));
        tiles[7][6].setPiece(new Piece(PieceType.KNIGHT, true));
        tiles[7][7].setPiece(new Piece(PieceType.ROOK, true));

        tiles[0][0].setPiece(new Piece(PieceType.ROOK, false));
        tiles[0][1].setPiece(new Piece(PieceType.KNIGHT, false));
        tiles[0][2].setPiece(new Piece(PieceType.BISHOP, false));
        tiles[0][3].setPiece(new Piece(PieceType.QUEEN, false));
        tiles[0][4].setPiece(new Piece(PieceType.KING, false));
        tiles[0][5].setPiece(new Piece(PieceType.BISHOP, false));
        tiles[0][6].setPiece(new Piece(PieceType.KNIGHT, false));
        tiles[0][7].setPiece(new Piece(PieceType.ROOK, false));
    }

    private void handleClick(Tile tile) {
        if (selectedPiece == null) {
            if (tile.piece != null && tile.piece.white == whiteToMove) {
                selectedPiece = tile.piece;
                selectedTile = tile;
                tile.highlight(true);
            }
            return;
        }

        if (tile == selectedTile) {
            selectedTile.highlight(false);
            selectedTile = null;
            selectedPiece = null;
            return;
        }

        if (isLegalMove(selectedTile, tile)) {
            tile.setPiece(selectedPiece);
            selectedTile.setPiece(null);
            whiteToMove = !whiteToMove;
        }

        selectedTile.highlight(false);
        selectedTile = null;
        selectedPiece = null;
    }

    private boolean isLegalMove(Tile from, Tile to) {
        Piece p = from.piece;

        if (to.piece != null && to.piece.white == p.white) return false;

        int dr = to.row - from.row;
        int dc = to.col - from.col;

        switch (p.type) {
            case PAWN:
                return canMovePawn(p, from.row, from.col, to.row, to.col);
            case ROOK:
                if (dr != 0 && dc != 0) return false;
                return pathClear(from.row, from.col, to.row, to.col);
            case BISHOP:
                if (Math.abs(dr) != Math.abs(dc)) return false;
                return pathClear(from.row, from.col, to.row, to.col);
            case QUEEN:
                if (dr == 0 || dc == 0 || Math.abs(dr) == Math.abs(dc)) {
                    return pathClear(from.row, from.col, to.row, to.col);
                }
                return false;
            case KNIGHT:
                return (Math.abs(dr) == 2 && Math.abs(dc) == 1)
                        || (Math.abs(dr) == 1 && Math.abs(dc) == 2);
            case KING:
                return Math.max(Math.abs(dr), Math.abs(dc)) == 1;
        }
        return false;
    }

    private boolean canMovePawn(Piece p, int fromRow, int fromCol, int toRow, int toCol) {
        int dr = toRow - fromRow;
        int dc = toCol - fromCol;

        int direction = p.white ? -1 : 1;
        int startRow = p.white ? 6 : 1;

        Tile dest = tiles[toRow][toCol];

        if (dc == 0) {
            if (dr == direction && !dest.isOccupied()) return true;

            if (fromRow == startRow && dr == 2 * direction) {
                int midRow = fromRow + direction;
                if (!tiles[midRow][fromCol].isOccupied() && !dest.isOccupied()) {
                    return true;
                }
            }
            return false;
        }

        if (Math.abs(dc) == 1 && dr == direction) {
            return dest.isOccupied() && dest.piece.white != p.white;
        }

        return false;
    }

    private boolean pathClear(int fromRow, int fromCol, int toRow, int toCol) {
        int dr = Integer.compare(toRow, fromRow);
        int dc = Integer.compare(toCol, fromCol);

        int r = fromRow + dr;
        int c = fromCol + dc;

        while (r != toRow || c != toCol) {
            if (tiles[r][c].isOccupied()) return false;
            r += dr;
            c += dc;
        }
        return true;
    }
}
