package JavaFX;

import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public enum ColorOnBoardEnum {
    PLAYER1("YELLOW"),
    PLAYER2("BLUE"),
    PLAYER3("GREEN"),
    PLAYER4("SILVER"),
    PLAYER5("RED"),
    PLAYER6("PINK");


    private String m_DiscColor;

    private ColorOnBoardEnum(String i_ColorName) {
        m_DiscColor = i_ColorName;
    }

        public String getColor() {
        return m_DiscColor;
    }
}