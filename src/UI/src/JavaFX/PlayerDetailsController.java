package JavaFX;

import Engine.EngineGame;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class PlayerDetailsController implements Controller {
    private EngineGame m_Engine;
    private final int k_MinHeightComponent = 105;
    private final int k_MaxPlayers = 6;

    @FXML
    private VBox PlayerDetails;

    @FXML
    private AnchorPane MessagesContainer;

    @FXML
    private ScrollPane ScrollPaneDetails;

    public void setModel(EngineGame i_Engine) {
        m_Engine = i_Engine;
    }

    public void initialDetails() {
        int amountOfPlayers = m_Engine.getAmountOfPlayers();
        if (amountOfPlayers != k_MaxPlayers) {
            PlayerDetails.getChildren().remove(amountOfPlayers - 1, 5);
            MessagesContainer.setPrefHeight(amountOfPlayers * k_MinHeightComponent);
        }
        ScrollPaneDetails.setFitToWidth(true);
        updateDetails();
    }

    public void updateDetails() {
        int turn = 0;
        for (int i = 0; i < PlayerDetails.getChildren().size(); i++) {
            VBox currentPlayer = (VBox) PlayerDetails.getChildren().get(i);
            if (((Label) ((HBox) (currentPlayer.getChildren().get(5))).getChildren().get(1)).getText().equals("Playing")) {
                ((Label) ((HBox) (currentPlayer.getChildren().get(0))).getChildren().get(1)).setText(Integer.toString(m_Engine.getPlayerTurnPlayed(turn)));
                ((Label) ((HBox) (currentPlayer.getChildren().get(1))).getChildren().get(1)).setText(m_Engine.getPlayerTurnName(turn));
                ((Label) ((HBox) (currentPlayer.getChildren().get(2))).getChildren().get(1)).setText(Short.toString(m_Engine.getPlayerTurnId(turn)));
                ((Label) ((HBox) (currentPlayer.getChildren().get(3))).getChildren().get(1)).setText(ColorOnBoardEnum.valueOf((m_Engine.getPlayerTypeName(turn))).getColor());
                ((Label) ((HBox) (currentPlayer.getChildren().get(4))).getChildren().get(1)).setText(m_Engine.ComputerTurn(turn) == true ? "Computer" : "Human Player");
                turn++;
            }
        }
    }

    public void setRetiredPlayer(int i_UniqueID) {
        VBox retiredPlayer = (VBox) PlayerDetails.getChildren().get(i_UniqueID);
        ((Label) ((HBox) (retiredPlayer.getChildren().get(5))).getChildren().get(1)).setText("Retired");
    }

    public String getColor(int i_UniqueID) {
        VBox retiredPlayer = (VBox) PlayerDetails.getChildren().get(i_UniqueID);
        return ((Label) ((HBox) (retiredPlayer.getChildren().get(3))).getChildren().get(1)).getText();
    }

    public void setDetails(List<String> i_CloneDetails) {
        for (int i = 0; i < PlayerDetails.getChildren().size(); i++) {
            VBox currentPlayer = (VBox) PlayerDetails.getChildren().get(i);
            ((Label) ((HBox) (currentPlayer.getChildren().get(0))).getChildren().get(1)).setText(i_CloneDetails.get(i*2));
            ((Label) ((HBox) (currentPlayer.getChildren().get(5))).getChildren().get(1)).setText(i_CloneDetails.get(i*2 + 1));
        }
    }


    public List<String> getDetails(){
        List<String> cloneDetails = new ArrayList<>();
        for(int i=0;i<PlayerDetails.getChildren().size();i++){
            VBox currentPlayer = (VBox) PlayerDetails.getChildren().get(i);
            cloneDetails.add(((Label)(((HBox) (currentPlayer.getChildren().get(0))).getChildren().get(1))).getText());
            cloneDetails.add(((Label)(((HBox)(currentPlayer.getChildren().get(5))).getChildren().get(1))).getText());
        }
        return cloneDetails;
    }
}
