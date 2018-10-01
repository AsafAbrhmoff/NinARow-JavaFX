package JavaFX;

import Engine.EngineGame;
import Engine.GameStateEnum;
import javafx.concurrent.Task;

public class TimeTask extends Task<Void> {
    private int m_Seconds;
    private int m_Minutes;
    private EngineGame m_Engine;
    private final int k_SecondsInMinutes = 60;

    public TimeTask(EngineGame i_Engine){
        m_Engine = i_Engine;
    }

    @Override
    protected Void call() throws Exception {
        while(m_Engine.getStatus() != GameStateEnum.END_GAME){
            getElapsedTime();
            Thread.sleep(1000);
        }
        return null;
    }

    private void getElapsedTime() {
        long seconds = m_Engine.getTimeInSeconds();
        updateMessage(String.format(" %02d:%02d", (seconds / k_SecondsInMinutes), (seconds % k_SecondsInMinutes)));
    }
}
