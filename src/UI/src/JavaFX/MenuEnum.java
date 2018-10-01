package JavaFX;

import Engine.GameStateEnum;

public enum MenuEnum {
    SAVE_GAME("Save Game",1,null),
    LOAD_SAVED_GAME("Load Saved Game",2,null),
    LOAD_GAME("Load Game",3,GameStateEnum.PRE_GAME),
    START_GAME("Start Game",4,GameStateEnum.PRE_GAME),
    PLAYER_MOVE("Insert Disc",3,GameStateEnum.GAMING),
    RESTART_GAME("Restart Game",3,GameStateEnum.END_GAME),
    GET_HISTORY("Get History",4,GameStateEnum.GAMING),
    UNDO("Undo",5,GameStateEnum.GAMING),
    DETAILS("Get Game Details",6,GameStateEnum.GAMING);


    GameStateEnum m_DisplayMode;
    private String m_Name;
    private int m_NumberInMenu;

    private MenuEnum(String i_Name, int i_NumberInMenu ,GameStateEnum i_DisplayMode){
        m_Name = i_Name;
        m_NumberInMenu = i_NumberInMenu;
        m_DisplayMode = i_DisplayMode;
    }

    public int GetNumberInMenu() {
        return m_NumberInMenu;
    }

    public String GetName() {
        return m_Name;
    }

    public GameStateEnum GetDisplayMode() {
        return m_DisplayMode;
    }
}
