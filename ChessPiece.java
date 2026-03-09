public abstract class ChessPiece {
    protected boolean isWhite;

    public ChessPiece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board);
    public abstract String getSymbol();

    protected boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        int rowStep = Integer.compare(toRow, fromRow);
        int colStep = Integer.compare(toCol, fromCol);

        int currentRow = fromRow + rowStep;
        int currentCol = fromCol + colStep;

        while (currentRow != toRow || currentCol != toCol) {
            if (board.getPiece(currentRow, currentCol) != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return true;
    }
}

