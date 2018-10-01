package Engine;

public enum GameStateEnum {
    PRE_GAME("PRE_GAME"),
    GAMING("GAMING"),
    END_GAME("END_GAME"),
    EXIT_GAME("EXIT_GAME");

    private String m_Name;

    private GameStateEnum(String i_Name){
        m_Name = i_Name;
    }

    @Override
    public String toString(){
        return m_Name;
    }
}
