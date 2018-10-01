package JavaFX;

import Engine.EngineGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReplayController implements Controller{
    private int m_BoardRows;
    private int m_BoardCols;
    private List<ComplexButton[][]> m_Boards = new ArrayList();
    private int m_TurnIndex = -1;
    private List<ListView<String>> m_HistoryDetails = new ArrayList<>();
    private List<List<String>> m_PlayersDetails = new ArrayList();
    private EngineGame m_Engine;
    private List<Replay> m_ReplayListeners = new LinkedList();
    private List<String> m_CurrentPlayerTotalTurnsPlayed = new ArrayList();
    private List<String> m_CurrentPlayerName = new ArrayList();
    private List<String> m_CurrentPlayerType = new ArrayList();
    private List<String> m_CurrentPlayerColorOnBoard = new ArrayList();
    private List<String> m_CurrentPlayerID = new ArrayList();


    @FXML
    private Button PrevButton;

    @FXML
    private Button ReplayButton;

    @FXML
    private Button NextButton;

    @FXML
    void NextReplayAction(ActionEvent event) {
        if(m_TurnIndex < m_Boards.size() - 1){
            m_TurnIndex++;
            for(Replay listener : m_ReplayListeners){
                listener.ReplayGameMode(false,true);
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"You are in the last screen", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void PrevReplayAction(ActionEvent event) {
        if(m_TurnIndex > 0){
            m_TurnIndex--;
            for(Replay listener : m_ReplayListeners){
                listener.ReplayGameMode(true,false);
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"You are in the first screen", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void StartReplayAction(ActionEvent event) {
        PrevButton.setDisable(false);
        NextButton.setDisable(false);
        ReplayButton.setText("Cancel Replay");
        ReplayButton.setOnAction(e -> cancelReplayAction(e));
        for(Replay listener : m_ReplayListeners){
            listener.ReplayGameMode(false,false);
        }
    }

    void cancelReplayAction(ActionEvent event) {
        PrevButton.setDisable(true);
        NextButton.setDisable(true);
        ReplayButton.setText("Start Replay");
        ReplayButton.setOnAction(e -> StartReplayAction(e));

        m_TurnIndex = m_Boards.size() - 1;
        for(Replay listener : m_ReplayListeners){
            listener.endReplayGameMode();
        }
    }

    public void setRowsAndCols(int i_Rows, int i_Cols){
        m_BoardRows = i_Rows;
        m_BoardCols = i_Cols;
    }

    public void addReplayNotifier(Replay i_Listener){
        m_ReplayListeners.add(i_Listener);
    }

    public void removeReplayNotifier(Replay i_Listener){
        m_ReplayListeners.remove(i_Listener);
    }
    
    public void setModel(EngineGame i_Engine) {
        m_Engine = i_Engine;
    }

    public void addScreen(ComplexButton[][] i_CloneBoard, ListView<String> i_CloneHistoryDetails, List<String> i_ClonePlayersDetails,
                          String i_CurrentPlayerTotalTurnsPlayed, String i_CurrentPlayerName, String i_CurrentPlayerColor, String i_CurrentPlayerID,
                          String i_CurrentPlayerType)
    {
        m_TurnIndex++;
        m_Boards.add(i_CloneBoard);
        m_HistoryDetails.add(i_CloneHistoryDetails);
        m_PlayersDetails.add(i_ClonePlayersDetails);
        m_CurrentPlayerTotalTurnsPlayed.add(i_CurrentPlayerTotalTurnsPlayed);
        m_CurrentPlayerID.add(i_CurrentPlayerID);
        m_CurrentPlayerName.add(i_CurrentPlayerName);
        m_CurrentPlayerColorOnBoard.add(i_CurrentPlayerColor);
        m_CurrentPlayerType.add(i_CurrentPlayerType);
    }

    public List<String> getPlayersDetails(){
        return m_PlayersDetails.get(m_TurnIndex);
    }

    public ComplexButton[][] getCurrentBoard(){
        return m_Boards.get(m_TurnIndex);
    }

    public ListView<String> getHistoryDetails(){
        return m_HistoryDetails.get(m_TurnIndex);
    }

    public String getCurrentPlayerTotalTurnsPlayed(){
        return m_CurrentPlayerTotalTurnsPlayed.get(m_TurnIndex);
    }

    public String getCurrentPlayerColorOnBoard(){
        return m_CurrentPlayerColorOnBoard.get(m_TurnIndex);
    }

    public String getCurrentPlayerName(){
        return m_CurrentPlayerName.get(m_TurnIndex);
    }

    public String getCurrentPlayerID(){
        return m_CurrentPlayerID.get(m_TurnIndex);
    }

    public String getCurrentPlayerType(){
        return m_CurrentPlayerType.get(m_TurnIndex);
    }
}
