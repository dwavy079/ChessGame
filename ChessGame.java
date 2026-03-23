import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Backward-compatible launcher for the richer Swing UI.
 *
 * The landing page (`index.html`) describes the `ChessFrontend` feature set:
 * two-player/vs-bot mode, hints, move history, captured pieces, and promotion
 * choices. This class now launches that same experience so running ChessGame
 * matches the indexed game behavior.
 */
public class ChessGame extends ChessFrontend {

    public ChessGame() {
        super();
        setTitle("ChessGame");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                     | UnsupportedLookAndFeelException ignored) {
            }
            new ChessGame().setVisible(true);
        });
    }
}

