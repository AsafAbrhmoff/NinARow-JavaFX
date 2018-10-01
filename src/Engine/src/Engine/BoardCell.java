package Engine;

public class BoardCell {
    private char m_Sign;
    private int m_PlayerTurn;

    public BoardCell(int i_PlayerTurn, char i_Sign){
        m_PlayerTurn = i_PlayerTurn;
        m_Sign = i_Sign;
    }

    public char getSign() {
        return m_Sign;
    }

    public int getPlayerTurn() {
        return m_PlayerTurn;
    }
}
