package JavaFX;

import Engine.EngineGame;
import javafx.application.Platform;
import javafx.concurrent.Task;

import javax.swing.*;
import java.awt.*;

public class ComputerTask extends Task<Boolean> {
    private EngineGame m_Engine;
    private Point m_LastMove = null;
    private Runnable m_ComputerMove;
    public ComputerTask(EngineGame i_Engine){
        m_Engine = i_Engine;
        updateProgress(0,1);
    }

    public void setComputerMove(Runnable i_ComputerMove){
        m_ComputerMove = i_ComputerMove;
    }

    @Override
    protected Boolean call() throws Exception {
        double totalProgress = 0;
        updateMessage("Choosing Disc");
        while(Math.round(totalProgress * 100) != 100){
            totalProgress += 0.05;
            updateProgress(totalProgress,1);
            if(Math.round(totalProgress * 100) == 50) {
                m_LastMove = m_Engine.computerOperation();
                updateMessage("Check Validation");
            }
            else{
                Thread.sleep(100);
                if(Math.round(totalProgress * 100) == 80){
                    updateMessage("Finish Move");
                }
            }
        }
        m_ComputerMove.run();
        return Boolean.TRUE;
    }

    public Point getLastMove(){
        return m_LastMove;
    }
}
