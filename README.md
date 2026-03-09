# ChessGame

A desktop chess game written in Java with a modern Swing UI.

The project contains:
- A simple chess **engine** (`Board`, `ChessPiece` and the piece subclasses)
- A feature-rich **frontend** (`ChessFrontend`) with:
  - Play vs **bot** (black) or **two-player** mode
  - **Move highlighting** for the selected piece
  - **Captured pieces** display for each side
  - **Move history** panel
  - **Pawn promotion choices** (Queen, Rook, Bishop, Knight)
  - A **hint** button that suggests a random legal move

---

## How to run

### Requirements

- Java 17+ (any recent JDK should work)

### Compile

From the `chessgameclean` directory:

```bash
javac *.java
```

This compiles all classes, including the engine and the Swing frontends.

### Run the main UI (recommended)

```bash
java ChessFrontend
```

This starts the full-featured Swing UI:
- Default mode is **Vs Bot (Black)** – you play White, a simple random-move bot plays Black.
- Use the mode dropdown in the top bar to switch to **Two Player** if you want human vs human.

You can also run the simpler window:

```bash
java ChessGame
```

---

## Controls and gameplay

- **Select a piece**: Click one of your pieces.
- **Highlighted moves**: All legal destination squares are highlighted:
  - Green for normal moves
  - Light orange for capture squares
- **Make a move**: Click a highlighted destination square.
- **Deselect**: Click the selected square again.
- **Hint**: Click the *Hint* button to automatically select a random reasonable move for the side to move and briefly highlight its destination.
- **New Game**: Resets the board, move list, and captures.

### Pawn promotion

When your pawn reaches the last rank:

- `ChessFrontend` shows a dialog to choose:
  - **Queen**
  - **Rook**
  - **Bishop**
  - **Knight**
- The pawn is replaced by your chosen piece on that square.
- The bot always promotes to a **Queen** automatically.

---

## Code structure

- `Board.java`  
  Holds the 8×8 array of `ChessPiece`, sets up the starting position, validates moves, and checks for:
  - Legal moves per piece
  - **Check**, **checkmate**, and **stalemate**

- `ChessPiece.java`  
  Abstract base class for all pieces. Common utilities:
  - `isWhite()` – side to move
  - `isPathClear(...)` – used by Rook/Bishop/Queen for sliding movement

- Piece implementations  
  - `Pawn.java`
  - `Rook.java`
  - `Knight.java`
  - `Bishop.java`
  - `Queen.java`
  - `King.java`

- `ChessFrontend.java`  
  Main Swing application with:
  - 8×8 board of buttons
  - Side panel showing **captured pieces** and **move history**
  - Top bar with title, **turn indicator**, mode selector, *Hint*, and *New Game*
  - Bottom bar with status/help messages
  - Simple **random-move bot** for Black in Vs Bot mode

- `ChessGame.java`  
  A smaller, older Swing UI that also lets you play chess, kept as an alternative front end.

---

## Website / GitHub Pages

This repository includes a static `index.html` that you can host with GitHub Pages to showcase the project.  
See [index.html](./index.html) for the simple project website layout and customize as you like.

