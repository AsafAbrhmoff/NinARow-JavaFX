package Engine;

public enum SignOnBoardEnum {
    PLAYER1('X'),
    PLAYER2('O'),
    PLAYER3('K'),
    PLAYER4('Y'),
    PLAYER5('B'),
    PLAYER6('W'),
    NOT_FOUND('~');

    private char m_SignOnBoard;

    SignOnBoardEnum(char i_Sign) {
        m_SignOnBoard = i_Sign;
    }


    public char getSign() {
        return m_SignOnBoard;
    }
}
