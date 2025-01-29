package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        switch (type) {
            case ROOK -> marchOrthogonally(validMoves, board, myPosition, 8);
            case BISHOP -> marchDiagonally(validMoves, board, myPosition, 8);
            case QUEEN -> marchAll(validMoves, board, myPosition, 8);
            case KING -> marchAll(validMoves, board, myPosition, 1);
            case KNIGHT -> marchKnightMoves(validMoves, board, myPosition);
            case PAWN -> validMovesPawn(validMoves, board, myPosition);
        }
        return validMoves;
    }

    private void marchAll(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition origin, int limit) {
        marchDiagonally(validMoves, board, origin, limit);
        marchOrthogonally(validMoves, board, origin, limit);
    }

    private void marchOrthogonally(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition origin, int limit) {
        marchInDirection(validMoves, board, origin, +1, 0, limit);
        marchInDirection(validMoves, board, origin, -1, 0, limit);
        marchInDirection(validMoves, board, origin, 0, +1, limit);
        marchInDirection(validMoves, board, origin, 0, -1, limit);
    }

    private void marchDiagonally(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition origin, int limit) {
        marchInDirection(validMoves, board, origin, +1, +1, limit);
        marchInDirection(validMoves, board, origin, -1, +1, limit);
        marchInDirection(validMoves, board, origin, +1, -1, limit);
        marchInDirection(validMoves, board, origin, -1, -1, limit);
    }

    private void marchKnightMoves(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition origin) {
        int[][] validDirections = {
                {1, 2}, {1, -2},
                {2, 1}, {2, -1},
        };
        for (var dir : validDirections) {
            marchInDirection(validMoves, board, origin, dir[0], dir[1], 1);
            marchInDirection(validMoves, board, origin, -dir[0], dir[1], 1);
        }
    }

    private void marchInDirection(Collection<ChessMove> validMoves, ChessBoard board,
                                  ChessPosition origin, int rowDelta, int colDelta, int limit) {
        int row = origin.getRow();
        int col = origin.getColumn();

        row += rowDelta;
        col += colDelta;

        ChessPosition checkPosition;
        ChessPiece occupant;
        ChessMove nextMove;
        int moveCount = 0;
        while (row >= 1 && row <= 8 && col >= 1 && col <= 8 && moveCount < limit) {
            checkPosition = new ChessPosition(row, col);
            occupant = board.getPiece(checkPosition);
            nextMove = new ChessMove(origin, checkPosition);
            if (occupant == null) {
                validMoves.add(nextMove);
            } else if (occupant.getTeamColor() != pieceColor) {
                validMoves.add(nextMove);
                break;
            } else {
                break;
            }
            row += rowDelta;
            col += colDelta;
            moveCount++;
        }
    }

    private void validMovesPawn(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition origin) {
        boolean white = pieceColor == ChessGame.TeamColor.WHITE;
        int homeRow = white ? 2 : 7;
        int forward = white ? 1 : -1;

        int row = origin.getRow();
        int col = origin.getColumn();

        pawnAttemptMove(validMoves, board, origin, row + forward, col - 1, true);
        pawnAttemptMove(validMoves, board, origin, row + forward, col + 1, true);
        var couldAdvance = pawnAttemptMove(validMoves, board, origin, row + forward, col, false);

        if (row == homeRow && couldAdvance) {
            pawnAttemptMove(validMoves, board, origin, row + 2 * forward, col, false);
        }
    }

    private boolean pawnAttemptMove(Collection<ChessMove> validMoves, ChessBoard board, ChessPosition origin,
                                 int endRow, int endCol, boolean attack) {
        if (endCol < 1 || endCol > 8 || endRow < 1 || endRow > 8) {
            return false;
        }

        ChessPosition end = new ChessPosition(endRow, endCol);
        var occupant = board.getPiece(end);
        if (attack && (occupant == null || occupant.getTeamColor() == pieceColor)) {
            return false;
        } else if (!attack && occupant != null) {
            return false;
        }

        if (end.getRow() == 1 || end.getRow() == 8) {
            validMoves.add(new ChessMove(origin, end, PieceType.BISHOP));
            validMoves.add(new ChessMove(origin, end, PieceType.KNIGHT));
            validMoves.add(new ChessMove(origin, end, PieceType.ROOK));
            validMoves.add(new ChessMove(origin, end, PieceType.QUEEN));
            return false;
        } else {
            validMoves.add(new ChessMove(origin, end, null));
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
