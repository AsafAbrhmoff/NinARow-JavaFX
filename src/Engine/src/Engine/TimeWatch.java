package Engine;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class TimeWatch implements Serializable {
    private long m_Starts;

    public TimeWatch() {
        start();
    }

    private void start() {
        m_Starts = System.currentTimeMillis();
    }

    public long timeInSeconds() {
        long ends = System.currentTimeMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ends - m_Starts);
        return seconds;
    }
}
