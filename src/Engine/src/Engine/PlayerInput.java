package Engine;

public class PlayerInput {
    private short m_Id;
    private String m_Name;
    private boolean m_ComputerPlayer;

    public PlayerInput(short i_Id, String i_Name, boolean i_ComputerPlayer){
        m_Id = i_Id;
        m_Name = i_Name;
        m_ComputerPlayer = i_ComputerPlayer;
    }

    public short getId() {
        return m_Id;
    }

    public String getName() {
        return m_Name;
    }

    public boolean getComputerPlayer() {
        return m_ComputerPlayer;
    }
}
