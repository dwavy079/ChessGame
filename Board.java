
public class Board {
    private ChessPiece[][] squares;
    
    public Board() {
        squares = new ChessPiece[8][8];
        initializeBoard();
    }
    
    private void initializeBoard() {
        // Initialize empty board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = null;
            }
        }
        
        // Place black pieces
        squares[0][0] = new Rook(false);
        squares[0][1] = new Knight(false);
        squares[0][2] = new Bishop(false);
        squares[0][3] = new Queen(false);
        squares[0][4] = new King(false);
        squares[0][5] = new Bishop(false);
        squares[0][6] = new Knight(false);
        squares[0][7] = new Rook(false);
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Pawn(false);
        }
        
        // Place white pieces
        squares[7][0] = new Rook(true);
        squares[7][1] = new Knight(true);
        squares[7][2] = new Bishop(true);
        squares[7][3] = new Queen(true);
        squares[7][4] = new King(true);
        squares[7][5] = new Bishop(true);
        squares[7][6] = new Knight(true);
        squares[7][7] = new Rook(true);
        for (int i = 0; i < 8; i++) {
            squares[6][i] = new Pawn(true);
        }
    }
    
    public ChessPiece getPiece(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return null;
        }
        return squares[row][col];
    }
    
    public void setPiece(int row, int col, ChessPiece piece) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            squares[row][col] = piece;
        }
    }
    
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        ChessPiece piece = getPiece(fromRow, fromCol);
        if (piece == null) {
            return false;
        }
        
        // Check if destination has a piece of the same color
        ChessPiece targetPiece = getPiece(toRow, toCol);
        if (targetPiece != null && targetPiece.isWhite() == piece.isWhite()) {
            return false;
        }
        
        // Check if the move is valid for this piece type
        if (!piece.isValidMove(fromRow, fromCol, toRow, toCol, this)) {
            return false;
        }
        
        // Check if the move would put own king in check
        Board testBoard = copy();
        testBoard.makeMove(fromRow, fromCol, toRow, toCol);
        if (testBoard.isInCheck(piece.isWhite())) {
            return false;
        }
        
        return true;
    }
    
    public ChessPiece makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        ChessPiece piece = getPiece(fromRow, fromCol);
        ChessPiece captured = getPiece(toRow, toCol);
        setPiece(toRow, toCol, piece);
        setPiece(fromRow, fromCol, null);
        
        // Handle pawn promotion (simplified - always promotes to queen)
        if (piece instanceof Pawn) {
            if ((piece.isWhite() && toRow == 0) || (!piece.isWhite() && toRow == 7)) {
                setPiece(toRow, toCol, new Queen(piece.isWhite()));
            }
        }
        
        return captured;
    }
    
    public boolean isInCheck(boolean isWhite) {
        // Find the king
        int kingRow = -1, kingCol = -1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = getPiece(i, j);
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
        
        if (kingRow == -1) return false;
        
        // Check if any opponent piece can attack the king
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = getPiece(i, j);
                if (piece != null && piece.isWhite() != isWhite) {
                    if (piece.isValidMove(i, j, kingRow, kingCol, this)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean isCheckmate(boolean isWhite) {
        if (!isInCheck(isWhite)) {
            return false;
        }
        
        // Try all possible moves for all pieces of this color
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = getPiece(i, j);
                if (piece != null && piece.isWhite() == isWhite) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (isValidMove(i, j, toRow, toCol)) {
                                return false; // Found a legal move
                            }
                        }
                    }
                }
            }
        }
        
        return true; // No legal moves found
    }
    
    public boolean isStalemate(boolean isWhite) {
        if (isInCheck(isWhite)) {
            return false;
        }
        
        // Check if there are any legal moves
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = getPiece(i, j);
                if (piece != null && piece.isWhite() == isWhite) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (isValidMove(i, j, toRow, toCol)) {
                                return false; // Found a legal move
                            }
                        }
                    }
                }
            }
        }
        
        return true; // No legal moves and not in check
    }
    
    public Board copy() {
        Board copy = new Board();
        // Clear the initialized board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy.squares[i][j] = null;
            }
        }
        // Copy pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy.squares[i][j] = this.squares[i][j];
            }
        }
        return copy;
    }
}
