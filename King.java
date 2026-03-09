public class King extends ChessPiece {
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        // King moves one square in any direction
        if (rowDiff > 1 || colDiff > 1) {
            return false;
        }

        // Check if destination is empty or has an enemy piece
        ChessPiece target = board.getPiece(toRow, toCol);
        return target == null || target.isWhite() != isWhite;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "♔" : "♚";
    }
}

