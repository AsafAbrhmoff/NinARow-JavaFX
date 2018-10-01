package JavaFX;

import javafx.concurrent.Task;

public interface GameState {
    void loadGame(String i_GamePath);
    void startGame();
    void initialMatrixComponent();
    void endGame();
    void setSkin(String i_SkinFileName);
    void setAnimation(boolean i_Animation);
    void computerMove(Task<Boolean> i_ComputerMoveTask);
}
