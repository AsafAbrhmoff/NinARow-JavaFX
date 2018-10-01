package JavaFX;

import Engine.EngineGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class QuitGameController implements Controller{
    private EngineGame m_Engine;
    @FXML
    private Button QuitGame;
    private List<QuitGameMode> m_Quit = new LinkedList<>();

    @FXML
    void handleQuitGameButtonClicked(ActionEvent event) {
        JOptionPane.showMessageDialog(null, m_Engine.getPlayerTurnName(m_Engine.getTurn()) + " Quit!", "Quit Game", JOptionPane.INFORMATION_MESSAGE);
        int removePlayerUniqueID = m_Engine.getUniqueID();
        String retiredName = m_Engine.quitGame();
        for(QuitGameMode listener: m_Quit)
            listener.quitGame(removePlayerUniqueID,retiredName);
    }

    public void setDisableQuitGameButton(boolean i_Disable){
        QuitGame.setDisable(i_Disable);
    }

    public void addListener(QuitGameMode i_Listener){
        m_Quit.add(i_Listener);
    }

    @Override
    public void setModel(EngineGame i_Engine) {
        m_Engine = i_Engine;
    }
}