public class Knight extends ChessPiece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }
    
    @Override
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, Board board) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        
        // Knight moves in L-shape: 2 squares in one direction, 1 square perpendicular
        if (!((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2))) {
            return false;
        }
        
        // Check if destination is empty or has an enemy piece
        ChessPiece target = board.getPiece(toRow, toCol);
        return target == null || target.isWhite() != isWhite;
    }
    
    @Override
    public String getSymbol() {
        return isWhite ? "♘" : "♞";
    }
}
