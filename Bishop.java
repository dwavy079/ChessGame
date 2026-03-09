public class Bishop extends ChessPiece {
    public Bishop(boolean isWhite) {
        super(isWhite);
    }
    
    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        // Bishop moves diagonally
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        
        if (rowDiff != colDiff) {
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
        return isWhite ? "♗" : "♝";
    }
}
