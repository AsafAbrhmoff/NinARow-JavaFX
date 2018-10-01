package JavaFX;

import Engine.EngineGame;
import Engine.Parser;
import Engine.XMLParser;
import javafx.animation.*;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

import java.awt.*;
import java.io.IOException;
import java.net.URL;


public class MainController extends Application implements GameState, Update, QuitGameMode, Replay {
    private Parser m_Parser = new XMLParser();
    private EngineGame m_Engine = new EngineGame(m_Parser);
    private MainToolBarController m_MainToolbar;
    private BoardUIController m_BoardUI;
    private CurrentTurnDetailsController m_CurrentDetails;
    private HistoryController m_History;
    private PlayerDetailsController m_PlayersDetails;
    private ReplayController m_Replay;
    private QuitGameController m_QuitGame;
    private LoadGameController m_LoadGame;
    private ComputerMoveController m_ComputerMove;
    private Scene m_Scene;
    private StackPane m_MatrixStackPane = new StackPane();
    private BorderPane m_GameForm = new BorderPane();
    private Task<Boolean> m_LoadTask;
    private Task<Void> m_TimeTask;
    private Stage m_LoadGameStage;
    private boolean m_Animation = false;
    private final int k_WidthProgressBarScreen = 543;
    private final int k_WidthMainScreen = 1200;

    @Override
    public void start(Stage primaryStage) {
        final int heightMainScreen = 650;
        Parent mainToolbarComponent = null;
        primaryStage.setResizable(true);
        mainToolbarComponent = initializeMainToolBar();
        m_MainToolbar.setStage(primaryStage);
        m_MainToolbar.addGameStateListener(this);
        m_GameForm.setTop(mainToolbarComponent);
        m_Scene = new Scene(m_GameForm, k_WidthMainScreen, heightMainScreen);
        setSkin("/css/DefaultSkin.css");
        primaryStage.setScene(m_Scene);
        primaryStage.setTitle("N In A Row");
        primaryStage.show();
        primaryStage.setOnCloseRequest(evt -> {
            if(m_TimeTask != null)
                m_TimeTask.cancel();
        } );

    }

    private Parent initializeMainToolBar() {
        Parent mainToolbarComponent = null;
        try {
            FXMLLoader currentLoader;
            currentLoader = new FXMLLoader();
            URL url = getClass().getResource("/FXML/MainToolBar.fxml");
            currentLoader.setLocation(url);
            mainToolbarComponent = currentLoader.load(url.openStream());
            m_MainToolbar = currentLoader.getController();
            m_MainToolbar.setModel(m_Engine);
        } catch (IOException ex) {
        }
        return mainToolbarComponent;
    }

    private Parent initialCurrentTurnDetails() {
        Parent currentPlayerDetailsComponent = null;
        try {
            FXMLLoader currentLoader = new FXMLLoader();
            URL url = getClass().getResource("/FXML/CurrentTurnDetails.fxml");
            currentLoader.setLocation(url);
            currentPlayerDetailsComponent = currentLoader.load(url.openStream());
            m_CurrentDetails = currentLoader.getController();
            m_CurrentDetails.setModel(m_Engine);
        } catch (IOException ex) {
        }
        return currentPlayerDetailsComponent;
    }

    private Parent initialComputerMove() {
        Parent ComputerMoveComponent = null;
        try {
            FXMLLoader currentLoader = new FXMLLoader();
            URL url = getClass().getResource("/FXML/ComputerMove.fxml");
            currentLoader.setLocation(url);
            ComputerMoveComponent = currentLoader.load(url.openStream());
            m_ComputerMove = currentLoader.getController();
        } catch (IOException ex) {
        }
        return ComputerMoveComponent;
    }

    private Parent initialHistoryMove() {
        Parent historyComponent = null;
        try {
            FXMLLoader currentLoader = new FXMLLoader();
            URL url = getClass().getResource("/FXML/HistoryMove.fxml");
            currentLoader.setLocation(url);
            historyComponent = currentLoader.load(url.openStream());
            m_History = currentLoader.getController();
            m_History.setModel(m_Engine);
        } catch (IOException ex) { }
        return historyComponent;
    }

    private Parent initialPlayersDetails() {
        Parent playersDetailsComponent = null;
        try {
            FXMLLoader currentLoader = new FXMLLoader();
            URL url = getClass().getResource("/FXML/PlayersDetails.fxml");
            currentLoader.setLocation(url);
            playersDetailsComponent = currentLoader.load(url.openStream());
            m_PlayersDetails = currentLoader.getController();
            m_PlayersDetails.setModel(m_Engine);
        } catch (IOException ex) {
        }

        return playersDetailsComponent;
    }

    private Parent initialReplay() {
        Parent replayComponent = null;
        try {
            FXMLLoader currentLoader = new FXMLLoader();
            URL url = getClass().getResource("/FXML/Replay.fxml");
            currentLoader.setLocation(url);
            replayComponent = currentLoader.load(url.openStream());
            m_Replay = currentLoader.getController();
            m_Replay.setModel(m_Engine);
            m_Replay.addReplayNotifier(this);
            m_Replay.setRowsAndCols(m_Engine.getRows(),m_Engine.getMaxCol());
            m_Replay.addScreen(m_BoardUI.getBoard(),m_History.getDetails(),m_PlayersDetails.getDetails(),
                    m_CurrentDetails.getCurrentPlayerTotalTurnsPlayed(),m_CurrentDetails.getCurrentPlayerName(),m_CurrentDetails.getCurrentPlayerColorOnBoard(),
                    m_CurrentDetails.getCurrentPlayerID(),m_CurrentDetails.getCurrentPlayerType());
        } catch (IOException ex) {
            System.out.println(ex.getCause());
        }
        return replayComponent;
    }

    private Parent initialQuitGame() {
        Parent QuitGameComponent = null;
        try {
            FXMLLoader currentLoader = new FXMLLoader();
            URL url = getClass().getResource("/FXML/QuitGame.fxml");
            currentLoader.setLocation(url);
            QuitGameComponent = currentLoader.load(url.openStream());
            m_QuitGame = currentLoader.getController();
            m_QuitGame.setModel(m_Engine);
            m_QuitGame.setDisableQuitGameButton(false);
            m_QuitGame.addListener(this);
        } catch (IOException ex) {
        }
        return QuitGameComponent;
    }

    private Parent initialLoadGameComponent() {
        Parent loadGameComponent = null;
        try {
            FXMLLoader currentLoader = new FXMLLoader();
            URL url = getClass().getResource("/FXML/LoadGame.fxml");
            currentLoader.setLocation(url);
            loadGameComponent = currentLoader.load(url.openStream());
            m_LoadGame = currentLoader.getController();
        } catch (IOException ex) {
        }
        return loadGameComponent;
    }

    private void initialBoard() {
        m_BoardUI = new BoardUIController(m_Engine.getRows(), m_Engine.getMaxCol(), m_Engine.getVarient(), m_Engine,m_Animation);
        m_BoardUI.addUpdateListener(this);
        m_BoardUI.addGameStateListener(this);
        m_BoardUI.initialBoard();
    }

    @Override
    public void loadGame(String i_GamePath) {
        final int heightProgressBarScreen = 338;
        Parent loadGameComponent = initialLoadGameComponent();
        m_LoadTask = new LoadGameTask(m_Engine, i_GamePath, loadGameComponent);
        m_LoadGame.initialize(m_LoadTask, () -> m_MainToolbar.displayStartGameButton(), () -> m_LoadGameStage.close());
        m_LoadGameStage = new Stage();
        Scene loadGameContainer = new Scene(loadGameComponent, k_WidthProgressBarScreen, heightProgressBarScreen);
        m_LoadGameStage.setScene(loadGameContainer);
        m_LoadGameStage.initModality(Modality.APPLICATION_MODAL);
        m_LoadGameStage.setTitle("Load Game");
        m_LoadGameStage.show();
        m_LoadGameStage.setResizable(false);
        m_LoadGameStage.setOnCloseRequest(evt -> evt.consume());
        new Thread(m_LoadTask).start();
    }
    @Override
    public void computerMove(Task<Boolean> i_ComputerMoveTask){
        final int heightProgressBarScreen = 175;
        Parent computerMoveComponent = initialComputerMove();
        Stage computerMoveStage = new Stage();
        m_ComputerMove.initialize(i_ComputerMoveTask,() -> computerMoveStage.close());
        Scene ComputerMoveContainer = new Scene(computerMoveComponent, k_WidthProgressBarScreen, heightProgressBarScreen);
        computerMoveStage.setScene(ComputerMoveContainer);
        computerMoveStage.initModality(Modality.APPLICATION_MODAL);
        computerMoveStage.setTitle("Computer Move");
        computerMoveStage.setResizable(false);
        computerMoveStage.show();
        computerMoveStage.setOnCloseRequest(evt -> evt.consume());
        new Thread(i_ComputerMoveTask).start();
    }

    @Override
    public void startGame() {
        final int heightPlayerDetails = 200;
        final int heightGameForm = 800;
        Parent currentPlayerDetailsComponent = null;
        Parent historyComponent = null;
        Parent playersDetailsComponent = null;
        Parent replayComponent = null;
        Parent QuitGameComponent = null;
        currentPlayerDetailsComponent = initialCurrentTurnDetails();
        historyComponent = initialHistoryMove();
        playersDetailsComponent = initialPlayersDetails();
        m_TimeTask = new TimeTask(m_Engine);
        m_CurrentDetails.setTimeBindingTask(m_TimeTask);
        new Thread(m_TimeTask).start();
        initialMatrixComponent();
        initialBoard();
        replayComponent = initialReplay();
        QuitGameComponent = initialQuitGame();
        ScrollPane VBoxScrollPane = new ScrollPane();
        VBox playerDetails = new VBox();
        playerDetails.setPrefHeight(heightPlayerDetails);
        playerDetails.getChildren().addAll(QuitGameComponent, currentPlayerDetailsComponent, replayComponent, historyComponent, playersDetailsComponent);
        VBoxScrollPane.setContent(playerDetails);
        playerDetails.setAlignment(Pos.CENTER_RIGHT);
        m_GameForm.setPrefWidth(k_WidthMainScreen);
        m_GameForm.setPrefHeight(heightGameForm);
        m_GameForm.setRight(VBoxScrollPane);
    }

    @Override
    public void endGame() {
        updateWinnerAnimation(m_Engine.getWinnersTaraget());
        m_QuitGame.setDisableQuitGameButton(true);
        m_MainToolbar.setEndGameMode();
        m_BoardUI.setEndGameMode();
        m_Engine.restartGame();
    }

    @Override
    public void initialMatrixComponent() {
        final int topRightBottomInset = 150;
        final int leftInset = 300;
        ScrollPane boardScrollPane = new ScrollPane();
        m_MatrixStackPane.setPadding(new Insets(topRightBottomInset, topRightBottomInset, topRightBottomInset, leftInset));
        boardScrollPane.setContent(m_MatrixStackPane);
        m_GameForm.setCenter(boardScrollPane);
    }

    @Override
    public void updateStatistics(boolean i_InitialMode) {
        m_CurrentDetails.updateDetails();
        if (i_InitialMode)
            m_PlayersDetails.initialDetails();
        else {
            m_PlayersDetails.updateDetails();
            m_History.updateHistory();
            m_Replay.addScreen(m_BoardUI.getBoard(),m_History.getDetails(),m_PlayersDetails.getDetails(),
                    m_CurrentDetails.getCurrentPlayerTotalTurnsPlayed(),m_CurrentDetails.getCurrentPlayerName(),m_CurrentDetails.getCurrentPlayerColorOnBoard(),
                    m_CurrentDetails.getCurrentPlayerID(),m_CurrentDetails.getCurrentPlayerType());
        }
    }

    @Override
    public void updateBoardInScene(VBox i_BoardRows, Background i_BackgroundImage) {
        final int buttonSize = 40;
        final int buttonSpacing = 1;
        VBox backgroundBoardRows = new VBox(m_Engine.getRows());
        for(int i = 0; i<m_Engine.getRows();++i)
        {
            HBox boardCol = new HBox(m_Engine.getMaxCol());
            boardCol.setSpacing(buttonSpacing);
            for(int j = 0;j<m_Engine.getMaxCol();++j){
                Button backgroundButton = new Button();
                backgroundButton.setPrefHeight(buttonSize);
                backgroundButton.setPrefWidth(buttonSize);
                backgroundButton.setBackground(i_BackgroundImage);
                boardCol.getChildren().add(backgroundButton);
            }
            backgroundBoardRows.setSpacing(buttonSpacing);
            backgroundBoardRows.getChildren().add(boardCol);
        }

        m_MatrixStackPane.getChildren().clear();
        m_MatrixStackPane.getChildren().add(backgroundBoardRows);
        m_MatrixStackPane.getChildren().add(i_BoardRows);
    }

    @Override
    public void quitGame(int i_removePlayerUniqueID, String i_RetiredName) {
        String color = m_PlayersDetails.getColor(i_removePlayerUniqueID);
        m_PlayersDetails.setRetiredPlayer(i_removePlayerUniqueID);
        m_History.updateRetireMove(i_RetiredName);
        m_BoardUI.quitMode(color);
        m_CurrentDetails.updateDetails();
        m_Replay.addScreen(m_BoardUI.getBoard(),m_History.getDetails(),m_PlayersDetails.getDetails(),
                m_CurrentDetails.getCurrentPlayerTotalTurnsPlayed(),m_CurrentDetails.getCurrentPlayerName(),m_CurrentDetails.getCurrentPlayerColorOnBoard(),
                m_CurrentDetails.getCurrentPlayerID(),m_CurrentDetails.getCurrentPlayerType());
        m_BoardUI.checkWinner();
    }

    @Override
    public void updatePopoutAnimation(ComplexButton i_CurrentButton){
        Path path = new Path();
        path.getElements().addAll(new MoveTo(i_CurrentButton.getScaleX() +19.5,i_CurrentButton.getScaleY() - 20), new VLineTo(i_CurrentButton.getScaleY() + 18.5));
        updateAnimation(i_CurrentButton,path);
    }

    private void updateAnimation(ComplexButton i_CurrentButton, Path i_Path) {
        final int twoSeconds = 2000;
        i_Path.setStroke(Color.GRAY);
        i_Path.setStrokeWidth(0);
        m_MatrixStackPane.getChildren().add(i_Path);
        PathTransition pathAnimation = new PathTransition(Duration.millis(twoSeconds), i_Path, i_CurrentButton);
        pathAnimation.setCycleCount(1);
        pathAnimation.play();
    }

    public void updateWinnerAnimation(List<Point> i_CurrentButton) {
        for(Point currentCoordinate: i_CurrentButton){
            ComplexButton currentButton = m_BoardUI.getButton(currentCoordinate.y,currentCoordinate.x);
            FadeTransition fadeAnimation = new FadeTransition(Duration.millis(100));
            fadeAnimation.setFromValue(1);
            fadeAnimation.setToValue(0.1);
            fadeAnimation.setCycleCount(20);
            fadeAnimation.setAutoReverse(true);
            ParallelTransition animationContainer = new ParallelTransition(currentButton,fadeAnimation);
            animationContainer.play();
        }
    }

    @Override
    public void updateInsertAnimation(ComplexButton i_CurrentButton,Point i_CurrentMove) {
        Path path = new Path();
        path.getElements().addAll(new MoveTo(i_CurrentButton.getScaleX() +19.5,-45*i_CurrentMove.y), new VLineTo(i_CurrentButton.getScaleY() + 18.5));
        updateAnimation(i_CurrentButton,path);
    }

    @Override
    public void ReplayGameMode(boolean i_PrevAction, boolean i_NextAction) {
        m_BoardUI.setBoard(m_Replay.getCurrentBoard());
        if(i_PrevAction)
            m_History.removeDetails(m_Replay.getHistoryDetails());
        else if(i_NextAction)
            m_History.addDetails(m_Replay.getHistoryDetails());
        m_CurrentDetails.setDetails(m_Replay.getCurrentPlayerTotalTurnsPlayed(),m_Replay.getCurrentPlayerName(),
                m_Replay.getCurrentPlayerColorOnBoard(),m_Replay.getCurrentPlayerID(),m_Replay.getCurrentPlayerType());
        m_PlayersDetails.setDetails(m_Replay.getPlayersDetails());
    }

    @Override
    public void endReplayGameMode() {
        m_BoardUI.endReplayMode(m_Replay.getCurrentBoard());
        m_History.setDetails(m_Replay.getHistoryDetails());
        m_CurrentDetails.updateDetails();
        m_PlayersDetails.setDetails(m_Replay.getPlayersDetails());
    }

    @Override
    public void setSkin(String i_SkinFileName) {
        if(m_Scene.getStylesheets().size() != 0)
            m_Scene.getStylesheets().remove(m_Scene.getStylesheets().get(0));
        if(i_SkinFileName != null)
            m_Scene.getStylesheets().add(getClass().getResource(i_SkinFileName).toExternalForm());
    }

    @Override
    public void setAnimation(boolean i_Animation) {
        m_Animation = i_Animation;
        m_BoardUI.setAnimation(i_Animation);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
