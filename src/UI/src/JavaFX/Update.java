package JavaFX;

import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.awt.*;

public interface Update {
    void updateStatistics(boolean i_InitialMode);
    void updateInsertAnimation(ComplexButton i_CurrentDisc,Point i_CurrentMove);
    void updateBoardInScene(VBox i_BoardRows, Background i_BackgroundImage);
    void updatePopoutAnimation(ComplexButton i_CurrentButton);
}
