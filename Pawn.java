public class Pawn extends ChessPiece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }
    
    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;
        
        // Move forward one square
        if (toCol == fromCol && toRow == fromRow + direction) {
            return board.getPiece(toRow, toCol) == null;
        }
        
        // Move forward two squares from starting position
        if (toCol == fromCol && fromRow == startRow && toRow == fromRow + 2 * direction) {
            return board.getPiece(toRow, toCol) == null && 
                   board.getPiece(fromRow + direction, fromCol) == null;
        }
        
        // Capture diagonally
        if (Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction) {
            ChessPiece target = board.getPiece(toRow, toCol);
            return target != null && target.isWhite() != isWhite;
        }
        
        return false;
    }
    
    @Override
    public String getSymbol() {
        return isWhite ? "♙" : "♟";
    }
}
