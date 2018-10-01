package Engine;

public enum PlayerTypeEnum {
    HUMAN("Human",1),
    COMPUTER("Computer",2);

    private String m_TypeName;
    private int m_MenuChoice;

    private PlayerTypeEnum(String i_TypeName, int i_MenuChoice)
    {
        m_TypeName = i_TypeName;
        m_MenuChoice = i_MenuChoice;
    }

    public int getMenuChoice(){
        return  m_MenuChoice;
    }

    @Override
    public String toString() {
        return m_TypeName;
    }
}
