import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Swing "frontend" for the existing chess engine (Board + ChessPiece + subclasses).
 * - Click a piece to select
 * - Click a highlighted square to move
 * - Optional mode: play vs a simple random-move bot (Black)
 */
public class ChessFrontend extends JFrame {
    private enum Mode { TWO_PLAYER, VS_BOT }

    private Board board;
    private Mode mode = Mode.VS_BOT;
    private boolean whiteToMove = true;
    private boolean gameOver = false;

    private final JButton[][] squares = new JButton[8][8];
    private final boolean[][] legalMoves = new boolean[8][8];
    private int selectedRow = -1;
    private int selectedCol = -1;

    private final List<ChessPiece> capturedByWhite = new ArrayList<>();
    private final List<ChessPiece> capturedByBlack = new ArrayList<>();
    private final List<String> moveHistory = new ArrayList<>();
    private final Random random = new Random();

    private JLabel turnLabel;
    private JLabel statusLabel;
    private JLabel capturedWhiteLabel;
    private JLabel capturedBlackLabel;
    private JTextArea movesArea;
    private JComboBox<String> modeSelect;

    public ChessFrontend() {
        this.board = new Board();
        buildUi();
        refreshAll();
    }

    private void buildUi() {
        setTitle("Chess - Frontend (Java Swing)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(12, 12, 12, 12));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JComponent buildTopBar() {
        JPanel top = new JPanel(new BorderLayout(12, 12));
        top.setBorder(new EmptyBorder(6, 6, 6, 6));
        top.setBackground(new Color(44, 62, 80));

        JLabel title = new JLabel("♔ Chess");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        top.add(title, BorderLayout.WEST);

        JPanel middle = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        middle.setOpaque(false);
        turnLabel = new JLabel("White to move");
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        middle.add(turnLabel);
        top.add(middle, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        actions.setOpaque(false);

        modeSelect = new JComboBox<>(new String[]{"Vs Bot (Black)", "Two Player"});
        modeSelect.setSelectedIndex(0);
        modeSelect.addActionListener(e -> {
            mode = modeSelect.getSelectedIndex() == 0 ? Mode.VS_BOT : Mode.TWO_PLAYER;
            resetGame();
        });
        actions.add(modeSelect);

        JButton hint = styledButton("Hint");
        hint.addActionListener(e -> showHint());
        actions.add(hint);

        JButton newGame = styledButton("New Game");
        newGame.addActionListener(e -> resetGame());
        actions.add(newGame);

        top.add(actions, BorderLayout.EAST);
        return top;
    }

    private JComponent buildCenter() {
        JPanel center = new JPanel(new BorderLayout(12, 12));

        // Board
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80), 5));
        boardPanel.setPreferredSize(new Dimension(640, 640));

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton sq = new JButton();
                sq.setPreferredSize(new Dimension(80, 80));
                sq.setFocusPainted(false);
                sq.setOpaque(true);
                sq.setBorderPainted(false);
                sq.setFont(new Font(Font.SERIF, Font.BOLD, 52));
                final int r = row;
                final int c = col;
                sq.addActionListener(e -> onSquareClicked(r, c));
                squares[row][col] = sq;
                boardPanel.add(sq);
            }
        }
        center.add(boardPanel, BorderLayout.CENTER);

        // Side panel
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(300, 640));

        JPanel captured = new JPanel();
        captured.setLayout(new BoxLayout(captured, BoxLayout.Y_AXIS));
        captured.setBorder(BorderFactory.createTitledBorder("Captured"));
        capturedWhiteLabel = new JLabel("White: (none)");
        capturedBlackLabel = new JLabel("Black: (none)");
        captured.add(capturedWhiteLabel);
        captured.add(Box.createVerticalStrut(6));
        captured.add(capturedBlackLabel);

        JPanel history = new JPanel(new BorderLayout());
        history.setBorder(BorderFactory.createTitledBorder("Moves"));
        movesArea = new JTextArea(18, 24);
        movesArea.setEditable(false);
        movesArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(movesArea);
        history.add(scroll, BorderLayout.CENTER);

        side.add(captured);
        side.add(Box.createVerticalStrut(10));
        side.add(history);

        center.add(side, BorderLayout.EAST);
        return center;
    }

    private JComponent buildBottomBar() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(new EmptyBorder(6, 6, 6, 6));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(new Color(211, 47, 47));
        bottom.add(statusLabel, BorderLayout.CENTER);

        JLabel help = new JLabel("Tip: click a piece, then a highlighted square");
        help.setForeground(new Color(90, 90, 90));
        bottom.add(help, BorderLayout.EAST);

        return bottom;
    }

    private JButton styledButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(new Color(52, 152, 219));
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return b;
    }

    private void onSquareClicked(int row, int col) {
        if (gameOver) return;
        if (mode == Mode.VS_BOT && !whiteToMove) return; // bot controls black

        ChessPiece clicked = board.getPiece(row, col);
        boolean clickedIsCurrentSide =
                clicked != null && clicked.isWhite() == whiteToMove;

        if (selectedRow == -1) {
            if (clickedIsCurrentSide) {
                selectSquare(row, col);
            }
            return;
        }

        // Deselect
        if (row == selectedRow && col == selectedCol) {
            clearSelection();
            refreshBoard();
            return;
        }

        // Change selection to another own piece
        if (clickedIsCurrentSide) {
            selectSquare(row, col);
            refreshBoard();
            return;
        }

        // Attempt move
        if (legalMoves[row][col] && board.isValidMove(selectedRow, selectedCol, row, col)) {
            doMove(selectedRow, selectedCol, row, col, true);
            clearSelection();
            refreshAll();

            if (!gameOver && mode == Mode.VS_BOT && !whiteToMove) {
                // Let UI repaint before bot moves
                new javax.swing.Timer(250, e -> {
                    ((javax.swing.Timer) e.getSource()).stop();
                    doBotMove();
                }).start();
            }
        } else {
            clearSelection();
            refreshBoard();
        }
    }

    private void selectSquare(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        computeLegalMovesForSelected();
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        clearLegalMoves();
    }

    private void clearLegalMoves() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                legalMoves[r][c] = false;
            }
        }
    }

    private void computeLegalMovesForSelected() {
        clearLegalMoves();
        if (selectedRow < 0) return;
        ChessPiece piece = board.getPiece(selectedRow, selectedCol);
        if (piece == null || piece.isWhite() != whiteToMove) return;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board.isValidMove(selectedRow, selectedCol, r, c)) {
                    legalMoves[r][c] = true;
                }
            }
        }
    }

    private void doMove(int fromRow, int fromCol, int toRow, int toCol, boolean isHumanMove) {
        ChessPiece moving = board.getPiece(fromRow, fromCol);
        ChessPiece captured = board.makeMove(fromRow, fromCol, toRow, toCol);

        if (captured != null) {
            if (moving != null && moving.isWhite()) capturedByWhite.add(captured);
            else capturedByBlack.add(captured);
        }

        // Handle pawn promotion choice (human moves only; bot auto-queens)
        boolean wasPromotion = moving instanceof Pawn &&
                ((moving.isWhite() && toRow == 0) || (!moving.isWhite() && toRow == 7));
        if (wasPromotion && isHumanMove) {
            String[] options = {"Queen", "Rook", "Bishop", "Knight"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Choose a piece for pawn promotion:",
                    "Pawn Promotion",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice < 0) {
                choice = 0; // default to Queen if closed
            }
            ChessPiece promoted;
            switch (choice) {
                case 1:
                    promoted = new Rook(moving.isWhite());
                    break;
                case 2:
                    promoted = new Bishop(moving.isWhite());
                    break;
                case 3:
                    promoted = new Knight(moving.isWhite());
                    break;
                case 0:
                default:
                    promoted = new Queen(moving.isWhite());
                    break;
            }
            board.setPiece(toRow, toCol, promoted);
        }

        moveHistory.add(formatMove(moving, fromRow, fromCol, toRow, toCol, captured));
        whiteToMove = !whiteToMove;

        // Game end checks apply to the SIDE TO MOVE
        if (board.isCheckmate(whiteToMove)) {
            gameOver = true;
            statusLabel.setText((whiteToMove ? "Black" : "White") + " wins by checkmate!");
        } else if (board.isStalemate(whiteToMove)) {
            gameOver = true;
            statusLabel.setText("Stalemate! Draw.");
        } else if (board.isInCheck(whiteToMove)) {
            statusLabel.setText((whiteToMove ? "White" : "Black") + " is in check!");
        } else {
            statusLabel.setText(" ");
        }
    }

    private String formatMove(ChessPiece piece, int fr, int fc, int tr, int tc, ChessPiece captured) {
        String files = "abcdefgh";
        String from = "" + files.charAt(fc) + (8 - fr);
        String to = "" + files.charAt(tc) + (8 - tr);
        String capture = captured != null ? "x" : "-";
        String p = piece == null ? "?" : pieceLetter(piece);
        int moveNo = (moveHistory.size() + 1);
        String side = (piece != null && piece.isWhite()) ? "W" : "B";
        return moveNo + ". " + side + ":" + p + from + capture + to;
    }

    private String pieceLetter(ChessPiece piece) {
        if (piece instanceof King) return "K";
        if (piece instanceof Queen) return "Q";
        if (piece instanceof Rook) return "R";
        if (piece instanceof Bishop) return "B";
        if (piece instanceof Knight) return "N";
        if (piece instanceof Pawn) return "P";
        return "?";
    }

    private void doBotMove() {
        if (gameOver || mode != Mode.VS_BOT || whiteToMove) return;

        List<int[]> moves = new ArrayList<>();
        for (int fr = 0; fr < 8; fr++) {
            for (int fc = 0; fc < 8; fc++) {
                ChessPiece p = board.getPiece(fr, fc);
                if (p != null && !p.isWhite()) {
                    for (int tr = 0; tr < 8; tr++) {
                        for (int tc = 0; tc < 8; tc++) {
                            if (board.isValidMove(fr, fc, tr, tc)) {
                                moves.add(new int[]{fr, fc, tr, tc});
                            }
                        }
                    }
                }
            }
        }

        if (moves.isEmpty()) {
            // Defensive: checkmate/stalemate should already be detected
            if (board.isCheckmate(false)) statusLabel.setText("White wins by checkmate!");
            else statusLabel.setText("Game over.");
            gameOver = true;
            refreshAll();
            return;
        }

        int[] mv = moves.get(random.nextInt(moves.size()));
        doMove(mv[0], mv[1], mv[2], mv[3], false);
        refreshAll();
    }

    private void showHint() {
        if (gameOver) return;
        if (mode == Mode.VS_BOT && !whiteToMove) return;

        List<int[]> all = new ArrayList<>();
        for (int fr = 0; fr < 8; fr++) {
            for (int fc = 0; fc < 8; fc++) {
                ChessPiece p = board.getPiece(fr, fc);
                if (p != null && p.isWhite() == whiteToMove) {
                    for (int tr = 0; tr < 8; tr++) {
                        for (int tc = 0; tc < 8; tc++) {
                            if (board.isValidMove(fr, fc, tr, tc)) {
                                all.add(new int[]{fr, fc, tr, tc});
                            }
                        }
                    }
                }
            }
        }

        if (all.isEmpty()) {
            statusLabel.setText("No legal moves.");
            return;
        }

        int[] hint = all.get(random.nextInt(all.size()));
        selectSquare(hint[0], hint[1]);
        refreshBoard();

        // Flash destination
        JButton dest = squares[hint[2]][hint[3]];
        Color original = dest.getBackground();
        dest.setBackground(Color.YELLOW);
        new javax.swing.Timer(450, e -> {
            ((javax.swing.Timer) e.getSource()).stop();
            refreshBoard();
            dest.setBackground(original);
            refreshBoard();
        }).start();

        statusLabel.setText("Hint: selected a piece for you");
    }

    private void resetGame() {
        this.board = new Board();
        this.whiteToMove = true;
        this.gameOver = false;
        this.capturedByWhite.clear();
        this.capturedByBlack.clear();
        this.moveHistory.clear();
        clearSelection();
        statusLabel.setText(" ");
        refreshAll();
    }

    private void refreshAll() {
        refreshTurnLabel();
        refreshCaptured();
        refreshMoves();
        refreshBoard();
    }

    private void refreshTurnLabel() {
        turnLabel.setText((whiteToMove ? "White" : "Black") + " to move" +
                (mode == Mode.VS_BOT ? " (vs bot)" : ""));
    }

    private void refreshCaptured() {
        capturedWhiteLabel.setText("White: " + piecesString(capturedByWhite));
        capturedBlackLabel.setText("Black: " + piecesString(capturedByBlack));
    }

    private String piecesString(List<ChessPiece> pieces) {
        if (pieces.isEmpty()) return "(none)";
        StringBuilder sb = new StringBuilder();
        for (ChessPiece p : pieces) sb.append(p.getSymbol()).append(" ");
        return sb.toString().trim();
    }

    private void refreshMoves() {
        StringBuilder sb = new StringBuilder();
        for (String m : moveHistory) sb.append(m).append("\n");
        movesArea.setText(sb.toString());
        movesArea.setCaretPosition(movesArea.getDocument().getLength());
    }

    private void refreshBoard() {
        // Locate kings for check highlight
        int[] whiteKing = findKing(true);
        int[] blackKing = findKing(false);
        boolean whiteInCheck = board.isInCheck(true);
        boolean blackInCheck = board.isInCheck(false);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton sq = squares[row][col];
                ChessPiece p = board.getPiece(row, col);

                boolean isLight = (row + col) % 2 == 0;
                Color base = isLight ? new Color(240, 217, 181) : new Color(181, 136, 99);
                Color bg = base;

                // Check highlight
                if (whiteInCheck && whiteKing != null && row == whiteKing[0] && col == whiteKing[1]) {
                    bg = new Color(255, 107, 107);
                }
                if (blackInCheck && blackKing != null && row == blackKing[0] && col == blackKing[1]) {
                    bg = new Color(255, 107, 107);
                }

                // Selection highlight
                if (row == selectedRow && col == selectedCol) {
                    bg = new Color(127, 200, 255);
                } else if (legalMoves[row][col]) {
                    // Capture squares slightly different
                    bg = (p != null) ? new Color(255, 224, 178) : new Color(144, 238, 144);
                }

                sq.setBackground(bg);
                sq.setText(p == null ? "" : p.getSymbol());
                sq.setForeground((p != null && p.isWhite()) ? new Color(30, 30, 30) : new Color(60, 60, 60));
            }
        }
    }

    private int[] findKing(boolean isWhite) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                ChessPiece p = board.getPiece(r, c);
                if (p instanceof King && p.isWhite() == isWhite) return new int[]{r, c};
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new ChessFrontend().setVisible(true);
        });
    }
}

