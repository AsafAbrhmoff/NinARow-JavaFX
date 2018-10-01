package JavaFX;

import Engine.EngineGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MainToolBarController implements Controller{
    private EngineGame m_Engine;
    private Stage m_Stage;
    private List<GameState> m_InitialGameNotifier = new LinkedList<>();

    @FXML
    private Button StartGameButton;

    @FXML
    private Button LoadXMLButton;

    @FXML
    private Button InformationButton;

    @FXML
    private MenuButton Skin;

    @FXML
    private CheckMenuItem FirstSkin;

    @FXML
    private CheckMenuItem SecondSkin;

    @FXML
    private CheckMenuItem ThirdSkin;

    @FXML
    private MenuButton Animation;

    @FXML
    private CheckMenuItem YesAnimation;

    @FXML
    private CheckMenuItem NoAnimation;

    public void setModel(EngineGame i_Engine) {
        m_Engine = i_Engine;
    }

    public void setStage(Stage i_Stage){
        m_Stage = i_Stage;
    }

    public void addGameStateListener(GameState i_Listener){
        if(m_InitialGameNotifier.contains(i_Listener) == false)
            m_InitialGameNotifier.add(i_Listener);
    }

    public void removeGameStateListener(GameState i_Listener){
        if(m_InitialGameNotifier.contains(i_Listener))
            m_InitialGameNotifier.remove(i_Listener);
    }

    @FXML
    void handleCancelAnimation(ActionEvent event) {
        NoAnimation.setSelected(true);
        YesAnimation.setSelected(false);
        for(GameState currentListener: m_InitialGameNotifier)
            currentListener.setAnimation(false);
    }

    @FXML
    void handleClickLoadXMLButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(m_Stage);
        if (selectedFile != null) {
            for (GameState listener : m_InitialGameNotifier)
                listener.loadGame(selectedFile.getAbsoluteFile().toString());
        }
    }

    public void displayStartGameButton(){
        StartGameButton.setDisable(false);
    }

    @FXML
    void handleClickStartGameButton(ActionEvent event) {
        LoadXMLButton.setDisable(true);
        InformationButton.setDisable(false);
        m_Engine.startGame();
        StartGameButton.setText("End Game");
        StartGameButton.setOnAction(e -> handleClickEndGameButton(e));
        for(GameState listener : m_InitialGameNotifier)
            listener.startGame();
    }

    private void handleClickEndGameButton(ActionEvent event){
        JOptionPane.showMessageDialog(null, m_Engine.getPlayerTurnName(m_Engine.getTurn()) + " finished the game!", "End Game", JOptionPane.INFORMATION_MESSAGE);
        for(GameState listener : m_InitialGameNotifier)
            listener.endGame();
    }

    public void setEndGameMode(){
        LoadXMLButton.setDisable(false);
        InformationButton.setDisable(true);
        StartGameButton.setText("Start Game");
        StartGameButton.setOnAction(e -> handleClickStartGameButton(e));
    }

    @FXML
    void handleClickInformationButton(ActionEvent event) {
        switch (m_Engine.getVarient()){
            case REGULAR:{
                JOptionPane.showMessageDialog(null, "You can insert disc by clicking a column in the highest row.", "Regular Game", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            case CIRCULAR:{
                JOptionPane.showMessageDialog(null, "You can insert disc by clicking a column in the highest row. \n You can achieve a win by circular target.", "Circular Game", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            case POPOUT:{
                JOptionPane.showMessageDialog(null, "You can insert disc by clicking a column in the highest row.\n Or You can remove your disc by clicking a column in the lowest row.", "Popout Game", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    private void handleSelectedSkin(CheckMenuItem i_MainSkin, CheckMenuItem i_FirstDisableSkin,
                                    CheckMenuItem i_SecondDisableSkin, String i_PathSkin){
        if(!i_MainSkin.isSelected()) {
            for (GameState currentListener : m_InitialGameNotifier)
                currentListener.setSkin("/css/DefaultSkin.css");
        } else {
            if(i_FirstDisableSkin.isSelected()) {
                i_FirstDisableSkin.setSelected(false);
            } else if(i_SecondDisableSkin.isSelected()) {
                i_SecondDisableSkin.setSelected(false);
            }
            for (GameState currentListener : m_InitialGameNotifier)
                currentListener.setSkin(i_PathSkin);
        }
    }

    @FXML
    void handleSelectedFSecondSkin(ActionEvent event) {
        final String secondSkinPath = "/css/SecondSkin.css";
        handleSelectedSkin(SecondSkin,ThirdSkin,FirstSkin,secondSkinPath);
    }

    @FXML
    void handleSelectedFirstSkin(ActionEvent event) {
        final String firstSkinPath = "/css/FirstSkin.css";
        handleSelectedSkin(FirstSkin,SecondSkin,ThirdSkin,firstSkinPath);
    }

    @FXML
    void handleSelectedThirdSkin(ActionEvent event) {
        String thirdSkinPath = "/css/ThirdSkin.css";
        handleSelectedSkin(ThirdSkin,FirstSkin,SecondSkin,thirdSkinPath);
    }

    @FXML
    void handleSetAnimation(ActionEvent event) {
        NoAnimation.setSelected(false);
        YesAnimation.setSelected(true);
        for(GameState currentListener: m_InitialGameNotifier)
            currentListener.setAnimation(true);
    }
}
