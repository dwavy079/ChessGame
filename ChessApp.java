/**
 * Legacy entry point kept for compatibility.
 *
 * JavaFX is not configured in this project, so this launcher delegates to the
 * Swing implementation used by the rest of the codebase.
 */
public class ChessApp {
    public static void main(String[] args) {
        ChessGame.main(args);
    }
}
