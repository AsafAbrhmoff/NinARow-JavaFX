package Engine;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public interface CommandsInterface {
    void startGame();

    void loadGame(String i_FileName) throws JAXBException;

    List<DataHistoryDisc> getHistory();

    void undo();

    void restartGame();

    void saveGame(ObjectOutputStream i_DataOut) throws IOException;
}
