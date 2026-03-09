import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class ChessGame extends JFrame {
    private Board board;
    private JPanel chessBoard;
    private JLabel statusLabel;
    private JLabel capturedWhiteLabel;
    private JLabel capturedBlackLabel;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean isWhiteTurn = true;
    private boolean gameOver = false;

    private final boolean[][] highlightedMoves = new boolean[8][8];
    private final List<ChessPiece> capturedByWhite = new ArrayList<>();
    private final List<ChessPiece> capturedByBlack = new ArrayList<>();
    private final Random random = new Random();

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

        // Status and captured pieces panel
        statusLabel = new JLabel("White's turn", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        capturedWhiteLabel = new JLabel("White captures: ");
        capturedBlackLabel = new JLabel("Black captures: ");

        JPanel capturedPanel = new JPanel(new GridLayout(1, 2));
        capturedPanel.add(capturedWhiteLabel);
        capturedPanel.add(capturedBlackLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.NORTH);
        bottomPanel.add(capturedPanel, BorderLayout.SOUTH);

        add(chessBoard, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

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

                // Highlight selected square or possible moves
                if (row == selectedRow && col == selectedCol) {
                    square.setBackground(new Color(255, 255, 0)); // Yellow for selected
                } else if (highlightedMoves[row][col]) {
                    square.setBackground(new Color(144, 238, 144)); // Light green for legal moves
                } else {
                    square.setBackground(baseColor);
                }

                // Add piece icon
                ChessPiece piece = board.getPiece(row, col);
                if (piece != null) {
                    square.setText(piece.getSymbol());
                    square.setFont(new Font(Font.SERIF, Font.BOLD, 56));
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
        if (gameOver || !isWhiteTurn) {
            return; // only allow user to move white, bot is black
        }

        ChessPiece piece = board.getPiece(row, col);

        // If a square is already selected
        if (selectedRow != -1 && selectedCol != -1) {
            // If clicking on the same square, deselect
            if (row == selectedRow && col == selectedCol) {
                selectedRow = -1;
                selectedCol = -1;
                clearHighlights();
                updateBoard();
                return;
            }

            // Try to move the selected piece
            ChessPiece selectedPiece = board.getPiece(selectedRow, selectedCol);
            if (selectedPiece != null && selectedPiece.isWhite() == isWhiteTurn) {
                if (board.isValidMove(selectedRow, selectedCol, row, col)) {
                    boolean movingWhite = isWhiteTurn;
                    ChessPiece captured = board.makeMove(selectedRow, selectedCol, row, col);
                    if (captured != null) {
                        if (movingWhite) {
                            capturedByWhite.add(captured);
                        } else {
                            capturedByBlack.add(captured);
                        }
                        updateCapturedLabels();
                    }

                    selectedRow = -1;
                    selectedCol = -1;
                    clearHighlights();
                    isWhiteTurn = !isWhiteTurn;

                    // Check for game over conditions after player's move
                    if (board.isCheckmate(!isWhiteTurn)) {
                        statusLabel.setText((isWhiteTurn ? "White" : "Black") + " wins by checkmate!");
                        gameOver = true;
                        disableBoard();
                    } else if (board.isStalemate(!isWhiteTurn)) {
                        statusLabel.setText("Stalemate! Game is a draw.");
                        gameOver = true;
                        disableBoard();
                    } else if (board.isInCheck(!isWhiteTurn)) {
                        statusLabel.setText((isWhiteTurn ? "Black" : "White") + " is in check! " +
                                (isWhiteTurn ? "White" : "Black") + "'s turn");
                    } else {
                        statusLabel.setText((isWhiteTurn ? "White" : "Black") + "'s turn");
                    }

                    updateBoard();

                    // Bot (black) moves if game not over
                    if (!gameOver && !isWhiteTurn) {
                        makeBotMove();
                    }
                    return;
                }
            }
        }

        // Select a new piece (if it's the correct color)
        if (piece != null && piece.isWhite() == isWhiteTurn) {
            selectedRow = row;
            selectedCol = col;
            highlightMovesForSelected();
            updateBoard();
        } else {
            selectedRow = -1;
            selectedCol = -1;
            clearHighlights();
            updateBoard();
        }
    }

    private void makeBotMove() {
        if (gameOver) return;

        List<int[]> legalMoves = new ArrayList<>();

        // Collect all legal moves for black (the bot)
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                ChessPiece piece = board.getPiece(fromRow, fromCol);
                if (piece != null && !piece.isWhite()) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (board.isValidMove(fromRow, fromCol, toRow, toCol)) {
                                legalMoves.add(new int[]{fromRow, fromCol, toRow, toCol});
                            }
                        }
                    }
                }
            }
        }

        if (legalMoves.isEmpty()) {
            // No legal moves: either checkmate or stalemate should already be detected,
            // but update status defensively.
            if (board.isCheckmate(false)) {
                statusLabel.setText("White wins by checkmate!");
            } else if (board.isStalemate(false)) {
                statusLabel.setText("Stalemate! Game is a draw.");
            }
            gameOver = true;
            disableBoard();
            return;
        }

        int[] move = legalMoves.get(random.nextInt(legalMoves.size()));
        int fromRow = move[0];
        int fromCol = move[1];
        int toRow = move[2];
        int toCol = move[3];

        ChessPiece captured = board.makeMove(fromRow, fromCol, toRow, toCol);
        if (captured != null) {
            capturedByBlack.add(captured);
            updateCapturedLabels();
        }

        selectedRow = -1;
        selectedCol = -1;
        clearHighlights();
        isWhiteTurn = true;

        // Check for game over conditions after bot's move
        if (board.isCheckmate(isWhiteTurn)) {
            statusLabel.setText("Black wins by checkmate!");
            gameOver = true;
            disableBoard();
        } else if (board.isStalemate(isWhiteTurn)) {
            statusLabel.setText("Stalemate! Game is a draw.");
            gameOver = true;
            disableBoard();
        } else if (board.isInCheck(isWhiteTurn)) {
            statusLabel.setText("White is in check! White's turn");
        } else {
            statusLabel.setText("White's turn");
        }

        updateBoard();
    }

    private void clearHighlights() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                highlightedMoves[r][c] = false;
            }
        }
    }

    private void highlightMovesForSelected() {
        clearHighlights();
        if (selectedRow < 0 || selectedCol < 0) return;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board.isValidMove(selectedRow, selectedCol, r, c)) {
                    highlightedMoves[r][c] = true;
                }
            }
        }
    }

    private void updateCapturedLabels() {
        StringBuilder whiteText = new StringBuilder("White captures: ");
        for (ChessPiece p : capturedByWhite) {
            whiteText.append(p.getSymbol()).append(" ");
        }

        StringBuilder blackText = new StringBuilder("Black captures: ");
        for (ChessPiece p : capturedByBlack) {
            blackText.append(p.getSymbol()).append(" ");
        }

        capturedWhiteLabel.setText(whiteText.toString());
        capturedBlackLabel.setText(blackText.toString());
    }

    private void disableBoard() {
        for (Component comp : chessBoard.getComponents()) {
            if (comp instanceof JButton) {
                comp.setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGame().setVisible(true));
    }
}

