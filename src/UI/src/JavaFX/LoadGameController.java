package JavaFX;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.util.Optional;

public class LoadGameController {

    @FXML
    private ProgressBar taskProgressBar;

    @FXML
    private Label progressPercentLabel;

    @FXML
    private Label LoadMessage;

    @FXML
    private Button FinishedLoadGame;

    private Runnable m_CloseForm;

    @FXML
    void handleOKClickButton(ActionEvent event) {
        m_CloseForm.run();
    }

    public void initialize(Task<Boolean> LoadTask, Runnable onFinished, Runnable CloseForm) {
        m_CloseForm = CloseForm;
        taskProgressBar.progressProperty().bind(LoadTask.progressProperty());
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        LoadTask.progressProperty(),
                                        100)),
                        " %"));
        LoadMessage.textProperty().bind(LoadTask.messageProperty());

        LoadTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.FinishedLoadGame.setVisible(true);
            if(LoadTask.valueProperty().getValue() == true)
                onTaskFinished(Optional.ofNullable(onFinished));
        });
    }

    private void onTaskFinished(Optional<Runnable> onFinished) {
        this.LoadMessage.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinished.ifPresent(Runnable::run);
    }

}
