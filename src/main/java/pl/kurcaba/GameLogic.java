package pl.kurcaba;

import javax.swing.text.Position;

/**
 * This class check whether move was compatible with game logic
 */
public class GameLogic implements BoardEvents {

    private final int ROUND_NEEDED_TO_PUT_ALL_PIECES = 11;
    private int gameStage = 1;
    private int moveCounter = 0;
    private boolean playerHaveToHit = false;


    /**
     *
     * @param pieceOnSquare piece which was on clicked square
     * @param squarePosition clicked field position
     *
     *  this function check what to do, when player clicked field
     */
    @Override
    public void squareClicked(Piece pieceOnSquare,PositionOnBoard squarePosition) {

        if (moveCounter > ROUND_NEEDED_TO_PUT_ALL_PIECES) gameStage = 2;

        boolean fieldIsEmpty = pieceOnSquare == null;

        if (fieldIsEmpty)
        {
            boolean pieceHaveToBeInCenter = moveCounter <2;

            if(pieceHaveToBeInCenter) {
                
                boolean fieldIsInCenter = (squarePosition.coordX == 2 || squarePosition.coordX == 3) && (squarePosition.coordY == 2 || squarePosition.coordY == 3);
                if (fieldIsInCenter) {
                    if (gameStage == 1 && !playerHaveToHit) GameWindow.createPlayersPiece(squarePosition);
                    moveCounter += 1;

                }
            }

            else
            {
                if (gameStage == 1 && !playerHaveToHit) GameWindow.createPlayersPiece(squarePosition);
                moveCounter += 1;
            }
        }
        if (pieceOnSquare != null && pieceOnSquare.getPieceType() == PieceType.BLACK)
        {
            if(playerHaveToHit){
                GameWindow.deletePiece(squarePosition);
                playerHaveToHit = false;
            }
        }

    }

    /**
     *
     * @return true - if move was compatible with logic, false if wasn't
     */
    @Override
    public boolean pieceCouldBeMoved() {

        if(gameStage == 1) return false;
        else
        {
            if(!playerHaveToHit) return true;
            else return false;
        }

    }

    /**
     *
     * @param piecesOnBoard is table with information about pieces on game piecesOnBoard
     * @return return true or false, if player have to delete enemy piece, or false if he doesn't have to.
     */
    private boolean playerHaveToHit(PieceType[][] piecesOnBoard)
    {
        for(int i = 0;i<GameWindow.BOARD_HEIGHT;i++)
        {
            for(int j = 0;j<GameWindow.BOARD_WIDTH;j++)
            {
                boolean isBoardCorner = ((i==0 && (j==0 || j==GameWindow.BOARD_HEIGHT -1)) ||
                        (i== GameWindow.BOARD_WIDTH -1) && (j==0 || j==GameWindow.BOARD_HEIGHT -1 ));
                if(isBoardCorner) continue;

                boolean HaveThreeHorizontalNeighbour = (i != 0 && i != GameWindow.BOARD_HEIGHT -1);
                boolean HaveThreeVerticalNeighbour = (j != 0 && j != GameWindow.BOARD_HEIGHT -1);

                if(HaveThreeHorizontalNeighbour)
                {
                    boolean playerHaveToHit = checkSquareHorizontal(piecesOnBoard,new PositionOnBoard(i,j));
                    if(playerHaveToHit) return true;
                }
                if(HaveThreeVerticalNeighbour)
                {
                    boolean playerHaveToHit = checkSquareVertical(piecesOnBoard,new PositionOnBoard(i,j));
                    if(playerHaveToHit) return true;
                }

            }
        }
        return true;
    }

    /**
     * @param piecesOnBoard is table with information about pieces on game piecesOnBoard
     * @param piecePosition position of the piece which is check.
     * @return true if piece have two horizontal neighbours
     */
    private boolean checkSquareHorizontal(PieceType[][] piecesOnBoard, PositionOnBoard piecePosition)
    {
        boolean isLeftPieceWhite = piecesOnBoard[piecePosition.coordX-1][piecePosition.coordY] == PieceType.WHITE;
        if(!isLeftPieceWhite) return false;

        boolean isRightPieceWhite = piecesOnBoard[piecePosition.coordX+1][piecePosition.coordY] == PieceType.WHITE;
        if(!isRightPieceWhite) return false;

        return true;
    }

    /**
     * @param piecesOnBoard is table with information about pieces on game piecesOnBoard
     * @param piecePosition position of the piece which is check.
     * @return true if piece have two vertical neighbours
     */
    private boolean checkSquareVertical(PieceType[][] piecesOnBoard, PositionOnBoard piecePosition)
    {
        boolean isUpPieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY-1] == PieceType.WHITE;
        if(!isUpPieceWhite) return false;

        boolean isDownPieceWhite = piecesOnBoard[piecePosition.coordX][piecePosition.coordY+1] == PieceType.WHITE;
        if(!isDownPieceWhite) return false;
        return true;

    }
}