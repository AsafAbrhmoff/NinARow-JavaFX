package Engine;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardGame implements Serializable {
    private BoardCell[][] m_Board;
    private int m_Rows;
    private int m_Cols;
    private final BoardCell k_EmptyCell = null;
    private final int k_FirstCol = 0;
    private final int k_AmountOfDirections = 4;
    private List<Point> m_WinnersTarget = new ArrayList<>();
    private List<Point> m_CurrentWinnerTarget = new ArrayList<>();

    public BoardGame(int i_Row,int i_Col){
        m_Rows = i_Row;
        m_Cols = i_Col;
        m_Board = new BoardCell[m_Rows][m_Cols];
        initialBoard();
    }

    public List<Point> getWinnersTarget(){
        return m_WinnersTarget;
    }

    public char getSignOnBoard(int i_Row,int i_Col){
        if(m_Board[i_Row][i_Col] != k_EmptyCell)
            return m_Board[i_Row][i_Col].getSign();
        return SignOnBoardEnum.NOT_FOUND.getSign();
    }

    public String getSignOnBoard(Point i_LastMove){
        char currentChar = m_Board[i_LastMove.y][i_LastMove.x].getSign();
        for(SignOnBoardEnum cur : SignOnBoardEnum.values()){
            if(cur.getSign() == currentChar){
                return cur.name();
            }
        }
        return null;
    }

    public BoardCell[][] getBoardForDisplay() {
        return m_Board;
    }

    public void restartBoard(){
        initialBoard();
    }

    public int getRows() {return  m_Rows;}
    
    public int getCols(){
        return  m_Cols;
    }

    private void initialBoard(){
        for(int i = 0; i <m_Rows; ++i){
            for(int j = 0;j< m_Cols;++j){
                m_Board[i][j] = k_EmptyCell;
            }
        }
    }

    public void removeDiscFromBoard(Point i_LastMove){
        m_Board[i_LastMove.y][i_LastMove.x] = k_EmptyCell;
    }

    public boolean checkOutOfRange(int i_Col){
        return i_Col >= k_FirstCol && i_Col < m_Cols;
    }

    public boolean checkFullColOnBoard(int i_Col){
        return m_Board[0][i_Col] == k_EmptyCell;
    }

    public boolean checkLegalPopoutMode(int i_Row, int i_Col,boolean i_Popout,char i_Sign){
        return (i_Popout && m_Board[i_Row][i_Col] != null && m_Board[i_Row][i_Col].getSign() == i_Sign) || (!i_Popout);
    }

    public void popoutDisc(int i_Row,int i_Col){
        boolean legalNextRow = true;
        do {
            if (i_Row - 1 == -1 || m_Board[i_Row - 1][i_Col] == k_EmptyCell) {
                legalNextRow = false;
                break;
            }
            m_Board[i_Row][i_Col] = m_Board[i_Row - 1][i_Col];
            i_Row--;
        }
        while (legalNextRow);
        m_Board[i_Row][i_Col] = k_EmptyCell;

    }

    public int updateDiscOnBoard(int i_Col, char i_Sign, int i_Turn,boolean i_Popout) {
        boolean legalNextRow = true;
        if (i_Popout) {
            int currentRow = m_Rows -1;
            popoutDisc(currentRow,i_Col);
            return m_Rows -1;
        } else {
            int currentRow = 0;
            do {
                if (currentRow + 1 == m_Rows || m_Board[currentRow + 1][i_Col] != k_EmptyCell) {
                    legalNextRow = false;
                    break;
                }
                currentRow++;
            }
            while (legalNextRow);
            m_Board[currentRow][i_Col] = new BoardCell(i_Turn, i_Sign);
            return currentRow;
        }
    }

    public boolean checkExistingDisc(int i_Rows,int i_Cols){
        return m_Board[i_Rows][i_Cols] != null;
    }

    public int getTurnByDisc(Point i_CurrentCoordinate){
        return m_Board[i_CurrentCoordinate.y][i_CurrentCoordinate.x].getPlayerTurn();
    }

    public boolean checkPopOutDraw(char i_SignOnBoard){
        for (int i = 0; i < m_Cols; ++i) {
            if (m_Board[0][i] == k_EmptyCell || m_Board[m_Rows - 1][i].getSign() == i_SignOnBoard)
                return false;
        }
        return true;
    }

    public boolean checkDraw(VarientEnum i_Varient,char i_SignOnBoard) {

        if (i_Varient.equals(VarientEnum.POPOUT))
            return checkPopOutDraw(i_SignOnBoard);
        else {

            for (int i = 0; i < m_Cols; ++i) {
                if (m_Board[0][i] == k_EmptyCell)
                    return false;
            }
            return true;
        }
    }

    public boolean checkWinGame(Point i_LastMove,int i_Sequence,char i_Sign, VarientEnum i_Varient){
        int upSequence = 0;
        int downSequence = 0;
        int xDirection  = -1;
        int yDirection  = 0;
        for(int i = 0; i < k_AmountOfDirections; ++i){
            m_CurrentWinnerTarget.clear();
            upSequence = checkSequence(i_LastMove,xDirection,yDirection,i_Sequence,i_Sign, i_Varient);
            downSequence = checkSequence(i_LastMove,-xDirection,-1 *yDirection,i_Sequence,i_Sign, i_Varient);
            if(yDirection == 0)
                yDirection--;
            else
                xDirection++;
            if(upSequence + downSequence + 1 >= i_Sequence){
                m_WinnersTarget.add(i_LastMove);
                for(Point currentDiscCoordinate: m_CurrentWinnerTarget)
                    m_WinnersTarget.add(currentDiscCoordinate);
                return true;
            }
        }
        return false;
    }

    private int checkSequence(Point i_lastMove, int xDirection, int yDirection, int i_Sequence, char i_Sign, VarientEnum i_Varient) {
        int counter = 0;
        for (int i = 1; i <= i_Sequence; ++i) {
            if (m_Board[(yDirection * i + i_lastMove.y + m_Rows) % m_Rows][(xDirection * i + i_lastMove.x + m_Cols) % m_Cols] != null) {
                if (checkLimitation(i_lastMove.y + yDirection * i, i_lastMove.x + xDirection * i) == true &&
                        m_Board[(yDirection * i + i_lastMove.y + m_Rows) % m_Rows][(xDirection * i + i_lastMove.x + m_Cols) % m_Cols].getSign() == i_Sign)
                {
                    m_CurrentWinnerTarget.add(new Point((xDirection * i + i_lastMove.x + m_Cols) % m_Cols,(yDirection * i + i_lastMove.y + m_Rows) % m_Rows));
                    counter++;
                }
                else if (checkLimitation(i_lastMove.y + yDirection * i, i_lastMove.x + xDirection * i) == false && i_Varient == VarientEnum.CIRCULAR &&
                        (yDirection == 0 || xDirection == 0) && m_Board[(yDirection * i + i_lastMove.y + m_Rows) % m_Rows][(xDirection * i + i_lastMove.x + m_Cols) % m_Cols].getSign() == i_Sign)
                {
                    m_CurrentWinnerTarget.add(new Point((xDirection * i + i_lastMove.x + m_Cols) % m_Cols,(yDirection * i + i_lastMove.y + m_Rows) % m_Rows));
                    counter++;
                }
                else
                    return counter;
            }
            else
                return counter;
        }
        return counter;
    }

    private boolean checkLimitation(int i_YCoordinate, int i_XCoordinate) {
        if(i_XCoordinate >= 0 && i_XCoordinate < m_Cols && i_YCoordinate >= 0 && i_YCoordinate < m_Rows)
            return true;
        return false;
    }
}
