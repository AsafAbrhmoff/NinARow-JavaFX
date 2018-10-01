package JavaFX;

import Engine.EngineGame;
import Engine.GameStateEnum;
import Engine.PlayerEngine;
import Engine.VarientEnum;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class BoardUIController {
    private ComplexButton[][] m_Board;
    private int m_Rows;
    private int m_Cols;
    private VarientEnum m_GameType;
    private EngineGame m_Engine;
    private Background m_InitialBackground = null;
    private List<GameState> m_GameStateNotifier = new LinkedList<>();
    private List<Update> m_UpdateDetailsNotifier = new LinkedList<>();
    private boolean m_Animation = false;

    public BoardUIController(int i_Rows, int i_Cols, VarientEnum i_GameType,EngineGame i_Engine,
                             boolean i_Animation){
        m_Rows = i_Rows;
        m_Cols = i_Cols;
        m_GameType = i_GameType;
        m_Board = new ComplexButton[m_Rows][m_Cols];
        m_Engine = i_Engine;
        m_Animation = i_Animation;
    }

    public void setAnimation(boolean i_Animation){
        m_Animation = i_Animation;
    }

    public ComplexButton getButton(int i_Row, int i_Col){
        return m_Board[i_Row][i_Col];
    }

    public void endReplayMode(ComplexButton[][] i_Board) {
        setBoard(i_Board);
        if(m_Engine.getStatus() == GameStateEnum.END_GAME)
            preventBoardClicked();
        for (int i = 0; i < m_Cols; ++i) {
            m_Board[0][i].setOnAction(event -> handleInsertDisc(((ComplexButton) event.getSource()).getCol()));
            if (m_GameType == VarientEnum.POPOUT) {
                m_Board[m_Rows - 1][i].setOnAction(event -> handlePopDisc(((ComplexButton) event.getSource()).getCol()));
            }
        }
    }

    public void setBoard(ComplexButton[][] i_Board){
        for (int i = 0; i < m_Rows; ++i) {
            for (int j = 0; j < m_Cols; ++j) {
                m_Board[i][j].setDisable(false);
                m_Board[i][j].setColor(i_Board[i][j].getColor());
                m_Board[i][j].setBackground(i_Board[i][j].getBackground());
                m_Board[i][j].setOnAction(null);
            }
        }
    }

    private ComplexButton[][] cloneBoard() {
        ComplexButton[][] cloneBoard = new ComplexButton[m_Rows][m_Cols];
        for (int i = 0; i < m_Rows; ++i) {
            for (int j = 0; j < m_Cols; ++j) {
                cloneBoard[i][j] = new ComplexButton(m_Board[i][j].getRow(), m_Board[i][j].getCol());
                cloneBoard[i][j].setColor(m_Board[i][j].getColor());
                cloneBoard[i][j].setBackground(m_Board[i][j].getBackground());
            }
        }
        return cloneBoard;
    }

    public ComplexButton[][] getBoard() {
        return cloneBoard();
    }

    public void quitMode(String i_Color) {
        for (int i = m_Rows - 1; i >= 0; --i) {
            for (int j = m_Cols - 1; j >= 0; --j) {
                if (m_Board[i][j].getColor() != null && m_Board[i][j].getColor().equals(i_Color)) {
                    popoutDisc(i, j);
                    ++j;
                }
            }
        }
    }

    public void addUpdateListener(Update i_Listener){
        m_UpdateDetailsNotifier.add(i_Listener);
    }

    public void addGameStateListener(GameState i_Listener){
        m_GameStateNotifier.add(i_Listener);
    }


    public void initialBoard(){
        final int buttonSize = 40;
        final String path = "/resources/background.png";
        Background image = new Background(new BackgroundFill(new ImagePattern(new Image(path)),CornerRadii.EMPTY, Insets.EMPTY));
        VBox boardRows = new VBox(m_Rows);
        for(int i = 0;i<m_Rows;++i){
            HBox boardCols = new HBox(m_Cols);
            boardCols.setSpacing(1);
            for(int j =0; j<m_Cols;++j){
                m_Board[i][j] = new ComplexButton(i,j);
                m_Board[i][j].setPrefWidth(buttonSize);
                m_Board[i][j].setPrefHeight(buttonSize);
                m_Board[i][j].setBackground(image);

                boardCols.getChildren().add(m_Board[i][j]);
                if(i == 0){
                    m_Board[i][j].setOnAction(event -> handleInsertDisc(((ComplexButton)event.getSource()).getCol()));
                    m_Board[i][j].setDisable(false);
                }
                else if(i == m_Rows - 1 && m_GameType == VarientEnum.POPOUT){
                    m_Board[i][j].setOnAction(event -> handlePopDisc(((ComplexButton)event.getSource()).getCol()));
                    m_Board[i][j].setDisable(false);
                }
                else{
                    m_Board[i][j].setOnAction(null);
                    m_Board[i][j].setMouseTransparent(true);
                }
            }
            boardRows.setSpacing(1);
            boardRows.getChildren().add(boardCols);
        }
        for(Update updateDetails: m_UpdateDetailsNotifier) {
            updateDetails.updateBoardInScene(boardRows,image);
            updateDetails.updateStatistics(true);
        }
    }

    public void checkWinner() {
        if (m_Engine.checkFinishedGame()) {
            setEndGameMode();
            if (m_Engine.getWinner().size() == 1) {
                JOptionPane.showMessageDialog(null, m_Engine.getWinner().get(0).getName() + " Won!", "The Winner", JOptionPane.INFORMATION_MESSAGE);
            } else if (m_Engine.getWinner().size() >= 2) {
                String winners = "The winners are: ";
                for (PlayerEngine currentPlayer : m_Engine.getWinner()) {
                    winners += currentPlayer.getName() + ", ";
                }
                winners.substring(0, winners.length() - 2);
                JOptionPane.showMessageDialog(null, winners, "Draw", JOptionPane.INFORMATION_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "The board is full", "Draw", JOptionPane.INFORMATION_MESSAGE);
            for (GameState listener : m_GameStateNotifier)
                    listener.endGame();
        } else {
            if (m_Engine.ComputerTurn(m_Engine.getTurn())) {
                ComputerTask computerTask = new ComputerTask(m_Engine);
                computerTask.setComputerMove(() -> {
                    Platform.runLater(() -> {
                        Point currentMove = computerTask.getLastMove();
                        if (currentMove != null) {
                            if (m_Engine.getPopoutLastMove())
                                continuePopoutDiscMove(currentMove);
                            else
                                playerMove(currentMove);
                        }
                    });
                });
                for (GameState listener : m_GameStateNotifier)
                    listener.computerMove(computerTask);
            }
        }
    }

    public void handleInsertDisc(int i_Col) {
        if (m_Engine.ComputerTurn(m_Engine.getTurn()) == false) {
            if (m_InitialBackground == null)
                m_InitialBackground = m_Board[0][0].getBackground();
            Point currentMove = m_Engine.humanMove(i_Col, false);
            if (currentMove == null) {
                JOptionPane.showMessageDialog(null, "col: " + (i_Col + 1) + " is fulled. please enter another col in range", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                playerMove(currentMove);
            }
        }
    }

    private void playerMove(Point i_CurrentMove){
        String playerSign = m_Engine.getPlayerTurnSign(i_CurrentMove);
        String path = "/resources/"+ ColorOnBoardEnum.valueOf(playerSign).getColor().concat(".png");
        Background image = new Background(new BackgroundFill(new ImagePattern(new Image(path)),CornerRadii.EMPTY, Insets.EMPTY));
        m_Board[i_CurrentMove.y][i_CurrentMove.x].setBackground(image);
        m_Board[i_CurrentMove.y][i_CurrentMove.x].setColor(ColorOnBoardEnum.valueOf(playerSign).getColor());
        m_Engine.finishedTurn(i_CurrentMove);
        for(Update updateDetails: m_UpdateDetailsNotifier) {
            if(m_Animation)
                updateDetails.updateInsertAnimation(m_Board[i_CurrentMove.y][i_CurrentMove.x],i_CurrentMove);
            updateDetails.updateStatistics(false);
        }
        checkWinner();
    }

    public void setImage(Point i_CurrentMove, Background i_Image){
        m_Board[i_CurrentMove.y][i_CurrentMove.x].setBackground(i_Image);
    }

    private void preventBoardClicked() {
        for (int j = 0; j < m_Cols; ++j) {
            m_Board[0][j].setOnAction(null);
            m_Board[m_Rows - 1][j].setOnAction(null);
        }
    }

    public void setEndGameMode(){
        preventBoardClicked();
    }

    public void popoutDisc(int i_Row, int i_Col){
        for (int i = i_Row; i > 0 ; --i) {
            m_Board[i][i_Col].setBackground(m_Board[i - 1][i_Col].getBackground());
            m_Board[i][i_Col].setColor(m_Board[i - 1][i_Col].getColor());
            m_Board[i - 1][i_Col].setBackground(m_InitialBackground);
            m_Board[i - 1][i_Col].setColor(null);
            m_Board[i - 1][i_Col].setVisible(true);
            if (m_Board[i][i_Col].getColor() != null && m_Animation) {
                for (Update listener : m_UpdateDetailsNotifier)
                    listener.updatePopoutAnimation(m_Board[i][i_Col]);
            }
        }
    }

    private void continuePopoutDiscMove(Point i_CurrentMove){
        popoutDisc(m_Rows - 1,i_CurrentMove.x);
        m_Engine.finishedTurn(i_CurrentMove);
        for(Update updateDetails: m_UpdateDetailsNotifier)
            updateDetails.updateStatistics(false);
        checkWinner();

    }

    public void handlePopDisc(int i_Col) {
        if (m_Engine.ComputerTurn(m_Engine.getTurn()) == false) {
            Point currentMove = m_Engine.humanMove(i_Col, true);
            if (currentMove == null)
                JOptionPane.showMessageDialog(null, "Your popout move is not legal", "Error", JOptionPane.ERROR_MESSAGE);
            else {
                continuePopoutDiscMove(currentMove);
            }
        }
    }
}
