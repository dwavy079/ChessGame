import java.awt.*;
import javax.swing.*;

public class ChessGame extends JFrame {
    private Board board;
    private JPanel chessBoard;
    private JLabel statusLabel;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean isWhiteTurn = true;
    
    public ChessGame() {
        board = new Board();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Create chess board panel
        chessBoard = new JPanel(new GridLayout(8, 8));
        chessBoard.setPreferredSize(new Dimension(640, 640));
        updateBoard();
        
        // Status label
        statusLabel = new JLabel("White's turn", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(chessBoard, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void updateBoard() {
        chessBoard.removeAll();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(80, 80));
                square.setOpaque(true);
                square.setBorderPainted(true);
                square.setFocusPainted(false);
                square.setContentAreaFilled(true);
                
                // Set background color (checkerboard pattern)
                Color baseColor;
                if ((row + col) % 2 == 0) {
                    baseColor = new Color(240, 217, 181); // Light square
                } else {
                    baseColor = new Color(181, 136, 99); // Dark square
                }
                
                // Highlight selected square
                if (row == selectedRow && col == selectedCol) {
                    square.setBackground(new Color(255, 255, 0)); // Yellow highlight
                } else {
                    square.setBackground(baseColor);
                }
                
                // Add piece icon
                ChessPiece piece = board.getPiece(row, col);
                if (piece != null) {
                    square.setText(piece.getSymbol());
                    // Use a large font size for chess symbols
                    square.setFont(new Font(Font.SERIF, Font.BOLD, 56));
                    // White pieces use dark color, black pieces use darker color
                    square.setForeground(piece.isWhite() ? new Color(30, 30, 30) : new Color(50, 50, 50));
                } else {
                    square.setText("");
                }
                
                final int r = row;
                final int c = col;
                square.addActionListener(e -> handleSquareClick(r, c));
                
                chessBoard.add(square);
            }
        }
        
        chessBoard.revalidate();
        chessBoard.repaint();
    }
    
    private void handleSquareClick(int row, int col) {
        ChessPiece piece = board.getPiece(row, col);
        
        // If a square is already selected
        if (selectedRow != -1 && selectedCol != -1) {
            // If clicking on the same square, deselect
            if (row == selectedRow && col == selectedCol) {
                selectedRow = -1;
                selectedCol = -1;
                updateBoard();
                return;
            }
            
            // Try to move the selected piece
            ChessPiece selectedPiece = board.getPiece(selectedRow, selectedCol);
            if (selectedPiece != null && selectedPiece.isWhite() == isWhiteTurn) {
                if (board.isValidMove(selectedRow, selectedCol, row, col)) {
                    board.makeMove(selectedRow, selectedCol, row, col);
                    selectedRow = -1;
                    selectedCol = -1;
                    isWhiteTurn = !isWhiteTurn;
                    
                    // Check for game over conditions
                    if (board.isCheckmate(!isWhiteTurn)) {
                        statusLabel.setText((isWhiteTurn ? "White" : "Black") + " wins by checkmate!");
                        disableBoard();
                    } else if (board.isStalemate(!isWhiteTurn)) {
                        statusLabel.setText("Stalemate! Game is a draw.");
                        disableBoard();
                    } else if (board.isInCheck(!isWhiteTurn)) {
                        statusLabel.setText((isWhiteTurn ? "Black" : "White") + " is in check! " + 
                                          (isWhiteTurn ? "White" : "Black") + "'s turn");
                    } else {
                        statusLabel.setText((isWhiteTurn ? "White" : "Black") + "'s turn");
                    }
                    
                    updateBoard();
                    return;
                }
            }
        }
        
        // Select a new piece (if it's the correct color)
        if (piece != null && piece.isWhite() == isWhiteTurn) {
            selectedRow = row;
            selectedCol = col;
            updateBoard();
        } else {
            selectedRow = -1;
            selectedCol = -1;
            updateBoard();
        }
    }
    
    private void disableBoard() {
        for (Component comp : chessBoard.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChessGame().setVisible(true);
        });
    }
}
