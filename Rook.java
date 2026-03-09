public class Rook extends ChessPiece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        // Rook moves horizontally or vertically
        if (fromRow != toRow && fromCol != toCol) {
            return false;
        }

        // Check if path is clear
        if (!isPathClear(fromRow, fromCol, toRow, toCol, board)) {
            return false;
        }

        // Check if destination is empty or has an enemy piece
        ChessPiece target = board.getPiece(toRow, toCol);
        return target == null || target.isWhite() != isWhite;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "♖" : "♜";
    }
}

