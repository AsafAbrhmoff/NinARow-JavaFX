package Engine;

import java.io.Serializable;

public class PlayerEngine implements Serializable {
    private char m_SignOnBoard;
    private int m_TurnPlayed = 0;
    private short m_Id;
    private int m_UniqueID;
    private String m_Name;
    private boolean m_Computer;

    PlayerEngine(String i_Name,short i_Id ,boolean i_IsComputer ,char i_Sign,int i_UniqueID){
        m_SignOnBoard = i_Sign;
        m_Computer = i_IsComputer;
        m_Name = i_Name;
        m_Id = i_Id;
        m_UniqueID = i_UniqueID;
    }

    public int getUniqueID(){
        return m_UniqueID;
    }

    public boolean isComputer() { return m_Computer;}

    public String getPlayerTypeName(){
        for(SignOnBoardEnum currentPlayer: SignOnBoardEnum.values()){
            if(currentPlayer.getSign() == m_SignOnBoard)
                return currentPlayer.name();
        }
        return null;
    }

    public char getSignOnBoard() {
        return m_SignOnBoard;
    }

    public short getId() {return  m_Id;}

    public String getName() {
        return m_Name;
    }

    public int getTurnPlayed() {
        return m_TurnPlayed;
    }

    public void increaseTurnPlayed() {
        this.m_TurnPlayed++;
    }

    public void decreaseTurnsPlayed() { this.m_TurnPlayed--;}

    public void restartDetails(){
        m_TurnPlayed = 0;
    }
}
