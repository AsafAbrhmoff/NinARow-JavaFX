package JavaFX;

import Engine.DataHistoryDisc;
import Engine.EngineGame;
import Engine.SignOnBoardEnum;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HistoryController implements Controller {
    private EngineGame m_Engine;
    @FXML
    private ListView<String> HistoryList;
    private int m_Counter = 1;
    private final String k_CloseCircleBracket = ")";

    @Override
    public void setModel(EngineGame i_Engine) {
        m_Engine = i_Engine;
    }

    public void updateHistory(){
        String lastMoveMessage;
        DataHistoryDisc lastMove = m_Engine.getLastMove();
        SignOnBoardEnum currentSign = purseSign(lastMove.getSign());
        lastMoveMessage = m_Counter++ + k_CloseCircleBracket + " " + lastMove.getName() + ", Color: " + ColorOnBoardEnum.valueOf((currentSign.name())).getColor() +
                ", " + (lastMove.getPopout() ? "Popout: " : "Insert: ") +"(" +(lastMove.getLastMoveCoordinate().y + 1) + "," + (lastMove.getLastMoveCoordinate().x + 1) + k_CloseCircleBracket;
        HistoryList.getItems().add(lastMoveMessage);
    }

    public void updateRetireMove(String i_RetiredName){
        String lastMoveMessage;
        lastMoveMessage = m_Counter++ + k_CloseCircleBracket + " " + i_RetiredName + ", Retired!" ;
        HistoryList.getItems().add(lastMoveMessage);
    }

    public SignOnBoardEnum purseSign(char i_Sign){
        for(SignOnBoardEnum currentSign : SignOnBoardEnum.values()){
            if(currentSign.getSign() == i_Sign)
                return currentSign;
        }
        return null;
    }

    public void setDetails(ListView<String> i_Details){
        HistoryList.getItems().clear();
        for(int i = 0;i<i_Details.getItems().size();++i)
            HistoryList.getItems().add(i_Details.getItems().get(i));
    }

    public void addDetails(ListView<String> i_Details){
        HistoryList.getItems().add(i_Details.getItems().get(i_Details.getItems().size() - 1));
    }

    public void removeDetails(ListView<String> i_Details){
        HistoryList.getItems().remove(HistoryList.getItems().get(HistoryList.getItems().size() - 1));
    }

    public ListView<String> getDetails(){
        ListView<String> cloneDetails = new ListView<>();
        for(int i =0; i< HistoryList.getItems().size();++i)
            cloneDetails.getItems().add(HistoryList.getItems().get(i));
        return cloneDetails;
    }
}
