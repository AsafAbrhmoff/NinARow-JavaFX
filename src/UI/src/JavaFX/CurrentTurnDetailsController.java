package JavaFX;

import Engine.EngineGame;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class CurrentTurnDetailsController implements Controller{
    private EngineGame m_Engine;
    private SimpleIntegerProperty m_CurrentPlayerTotalTurnsPlayed = new SimpleIntegerProperty(0);
    private SimpleStringProperty m_CurrentPlayerName = new  SimpleStringProperty("");
    private SimpleStringProperty m_CurrentPlayerColorOnBoard = new SimpleStringProperty("");
    private SimpleIntegerProperty m_CurrentPlayerID = new SimpleIntegerProperty(0);
    private SimpleStringProperty  m_TimeElapsed = new SimpleStringProperty("");
    private SimpleStringProperty m_GameMode = new SimpleStringProperty("");
    private SimpleIntegerProperty m_Target = new SimpleIntegerProperty();
    private SimpleStringProperty m_Type = new SimpleStringProperty("");
    private SimpleStringProperty m_NextPlayer = new SimpleStringProperty("");

    @FXML
    private AnchorPane CurrentDetails;

    @FXML
    private Label CurrentTurn;

    @FXML
    private Label CurrentPlayerTotalTurnsPlayed;

    @FXML
    private Label CurrentPlayerName;

    @FXML
    private Label CurrentPlayerColorOnBoard;

    @FXML
    private Label CurrentPlayerID;

    @FXML
    private Label TimeElapsed;

    @FXML
    private Label GameMode;

    @FXML
    private Label Target;

    @FXML
    private Label Type;

    @FXML
    private Label NextPlayer;

    public void setModel(EngineGame i_Engine) {
        m_Engine = i_Engine;
    }

    @FXML
    private void initialize() {
        CurrentPlayerTotalTurnsPlayed.textProperty().bind(Bindings.format("%,d", m_CurrentPlayerTotalTurnsPlayed));
        CurrentPlayerName.textProperty().bind(m_CurrentPlayerName);
        CurrentPlayerColorOnBoard.textProperty().bind(m_CurrentPlayerColorOnBoard);
        CurrentPlayerID.textProperty().bind(Bindings.format("%,d", m_CurrentPlayerID));
        GameMode.textProperty().bind(m_GameMode);
        Target.textProperty().bind(Bindings.format("%,d", m_Target));
        Type.textProperty().bind(m_Type);
        NextPlayer.textProperty().bind(m_NextPlayer);
    }

    public void setTimeBindingTask(Task<Void> TimeTask){
        TimeElapsed.textProperty().bind(TimeTask.messageProperty());
    }


    public void updateDetails()
    {
        m_CurrentPlayerTotalTurnsPlayed.set(m_Engine.getPlayerTurnPlayed(m_Engine.getTurn()));
        m_CurrentPlayerName.set(m_Engine.getPlayerTurnName(m_Engine.getTurn()));
        m_CurrentPlayerColorOnBoard.set(ColorOnBoardEnum.valueOf(m_Engine.getPlayerTypeName(m_Engine.getTurn())).getColor());
        m_CurrentPlayerID.set((m_Engine.getPlayerTurnId(m_Engine.getTurn())));
        m_GameMode.set(m_Engine.getVarient().name());
        m_Target.set(m_Engine.getSequence());
        m_Type.set(m_Engine.ComputerTurn(m_Engine.getTurn()) == true ? "Computer" : "Human Player");
        m_NextPlayer.set(m_Engine.getPlayerTurnName((m_Engine.getTurn() + 1) % m_Engine.getAmountOfPlayers()));
    }

    public void setDetails(String i_CurrentPlayerTotalTurnsPlayed, String i_CurrentPlayerName, String i_CurrentPlayerColorOnBoard,
                           String i_CurrentPlayerID, String i_CurrentPlayerType){
         m_CurrentPlayerTotalTurnsPlayed.set(Integer.parseInt(i_CurrentPlayerTotalTurnsPlayed));
         m_CurrentPlayerID.set(Integer.parseInt(i_CurrentPlayerID));
         m_CurrentPlayerColorOnBoard.set(i_CurrentPlayerColorOnBoard);
         m_CurrentPlayerName.set(i_CurrentPlayerName);
         m_Type.set(i_CurrentPlayerType);
    }

    public String getCurrentPlayerTotalTurnsPlayed(){ return CurrentPlayerTotalTurnsPlayed.getText(); }
    public String getCurrentPlayerName(){
        return CurrentPlayerName.getText();
    }
    public String getCurrentPlayerColorOnBoard(){
        return CurrentPlayerColorOnBoard.getText();
    }
    public String getCurrentPlayerID(){
        return CurrentPlayerID.getText();
    }
    public String getCurrentPlayerType(){
        return Type.getText();
    }


}
