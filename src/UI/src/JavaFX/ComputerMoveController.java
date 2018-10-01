package JavaFX;

import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.util.Optional;

public class ComputerMoveController {

    @FXML
    private ProgressBar taskProgressBar;

    @FXML
    private Label progressPercentLabel;

    @FXML
    private Label LoadMessage;

    public void initialize(Task<Boolean> ComputerMoveTask, Runnable onFinished) {
        taskProgressBar.progressProperty().bind(ComputerMoveTask.progressProperty());
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        ComputerMoveTask.progressProperty(),
                                        100)),
                        " %"));
        LoadMessage.textProperty().bind(ComputerMoveTask.messageProperty());

        ComputerMoveTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (ComputerMoveTask.valueProperty().getValue() == true)
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
