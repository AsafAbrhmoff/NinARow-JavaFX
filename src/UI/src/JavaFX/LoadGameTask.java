package JavaFX;

import Engine.EngineGame;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;

import javax.swing.*;
import javax.xml.bind.JAXBException;

public class LoadGameTask extends Task<Boolean>  {
    private EngineGame m_Engine;
    private String m_GamePath;
    private Parent m_LoadGameComponent;

    public LoadGameTask(EngineGame i_Engine, String i_Path, Parent i_LoadGameComponent){
        m_Engine = i_Engine;
        m_GamePath = i_Path;
        m_LoadGameComponent = i_LoadGameComponent;
    }

    @Override
    protected Boolean call() throws Exception {
        double totalProgress = 0;
        updateProgress(totalProgress,1);
        updateMessage("Loading Details");
        while(Math.round(totalProgress * 100) != 100){
            totalProgress += 0.01;
            updateProgress(totalProgress,1);
            if(Math.round(totalProgress * 100) == 50) {
                try{
                    m_Engine.loadGame(m_GamePath);
                }
                catch (Exception ex){
                    Platform.runLater(() -> {
                        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                    break;
                }
                updateMessage("Load Game");
            }
            else{
                Thread.sleep(100);
                if(Math.round(totalProgress * 100) == 80){
                    updateMessage("Initial Game");
                }
            }
        }
        if(Math.round(totalProgress * 100) == 100){
            updateMessage("Loaded Successfully!");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
