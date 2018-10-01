package Engine;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.bind.JAXBException;

public class EngineGame implements CommandsInterface, Serializable {
    private final int k_MaxOfRows = 50;
    private final int k_MaxOfCols = 30;
    private final int k_MinOfRows = 5;
    private final int k_MinOfCols = 6;
    private BoardGame m_Board;
    private List<PlayerEngine> m_RegisterPlayers;
    private List<DataHistoryDisc> m_HistoryMoves = new ArrayList<>();;
    private int m_Sequence;
    private int m_Turn = 0;
    private boolean m_Draw = false;
    private boolean m_PopoutMove = false;
    private List<PlayerEngine> m_Winner = new ArrayList<>();
    private GameStateEnum m_Status = GameStateEnum.PRE_GAME;
    private boolean m_GameLoader = false;
    private boolean m_RestartGame = false;
    private DetailsInput m_GameDetails;
    private final int k_AmountOfPlayers = 2;
    private TimeWatch m_StartTime;
    private VarientEnum m_Varient;
    private Parser m_DataParser;

    public EngineGame(Parser i_Parser){
        m_DataParser = i_Parser;
    }

    public int getSequence() {
        return m_Sequence;
    }

    public boolean getPopoutLastMove(){
        return m_PopoutMove;
    }

    public VarientEnum getVarient() {
        return m_Varient;
    }

    public DataHistoryDisc getLastMove(){
        return m_HistoryMoves.get(m_HistoryMoves.size() -1);
    }

    public GameStateEnum getStatus() {
        return m_Status;
    }

    public boolean getGameLoader() {
        return m_GameLoader;
    }

    public long getTimeInSeconds() {
        return m_StartTime.timeInSeconds();
    }

    public String getPlayerTypeName(int i_PlayerIndex){
        return m_RegisterPlayers.get(i_PlayerIndex).getPlayerTypeName();
    }

    public List<Point> getWinnersTaraget(){
        return m_Board.getWinnersTarget();
    }

    public int getAmountOfPlayers(){
        return m_RegisterPlayers.size();
    }

    public short getPlayerTurnId(int i_PlayerIndex) {return m_RegisterPlayers.get(i_PlayerIndex).getId();}

    public int getPlayerTurnPlayed(int i_PlayerIndex){
        return m_RegisterPlayers.get(i_PlayerIndex).getTurnPlayed();
    }

    public String getPlayerTurnName(int i_PlayerIndex){
        return m_RegisterPlayers.get(i_PlayerIndex).getName();
    }

    public boolean getDrawMode() {
        return m_Draw;
    }

    public List<PlayerEngine> getWinner() {
        return m_Winner;
    }

    private void initialGame(DetailsInput i_GameDetails){
        m_Board = new BoardGame(i_GameDetails.getRows(),i_GameDetails.getCols());
        m_RegisterPlayers = new ArrayList<>(k_AmountOfPlayers);
        m_Sequence = i_GameDetails.getSequence();
        m_Varient = i_GameDetails.getVariant();
        List<PlayerInput> gamePlayers = i_GameDetails.getPlayersInput();
        for(int i = 0;i<gamePlayers.size();++i){
            PlayerInput currentPlayer = gamePlayers.get(i);
            String playerNameInSignEnum = "PLAYER" + (i+1);
            m_RegisterPlayers.add(new PlayerEngine(currentPlayer.getName(),currentPlayer.getId(),currentPlayer.getComputerPlayer(),SignOnBoardEnum.valueOf(playerNameInSignEnum).getSign(),i));
        }
    }

    @Override
    public void loadGame(String i_FileName) throws JAXBException {
        m_GameDetails = m_DataParser.purse(i_FileName,k_MaxOfRows,k_MinOfRows,k_MaxOfCols,k_MinOfCols);
        initialGame(m_GameDetails);
    }

    private void addDisc(Point i_LastMove,boolean i_Popout){
        m_HistoryMoves.add(new DataHistoryDisc(m_RegisterPlayers.get(m_Turn).getName(),i_LastMove,m_RegisterPlayers.get(m_Turn).getSignOnBoard(),i_Popout));
    }

    private void removeLastDisc(){
        m_HistoryMoves.remove(m_HistoryMoves.size() - 1);
    }

    public int getTurn() {
        return m_Turn;
    }

    public BoardCell[][] getBoardForDisplay(){return m_Board.getBoardForDisplay();}

    public int getRows(){
        return m_Board.getRows();
    }

    public int getMaxCol(){
        return m_Board.getCols();
    }

    public void initialState(){
        m_GameLoader = true;
        m_RestartGame = false;
    }

    public boolean ComputerTurn(int i_Turn){
        return m_RegisterPlayers.get(i_Turn).isComputer();
    }

    @Override
    public void startGame() {
        m_Status = GameStateEnum.GAMING;
        m_StartTime = new TimeWatch();
    }

    @Override
    public List<DataHistoryDisc> getHistory() {
        return m_HistoryMoves;
    }

    @Override
    public void undo() {
        if(m_HistoryMoves.size() != 0) {
            m_Board.removeDiscFromBoard(m_HistoryMoves.get(m_HistoryMoves.size() - 1).getLastMoveCoordinate());
            removeLastDisc();
            m_Turn = (m_Turn - 1 + m_RegisterPlayers.size())%(m_RegisterPlayers.size());
            m_RegisterPlayers.get(m_Turn).decreaseTurnsPlayed();
        }
        else{
            throw new IllegalStateException("You are in the entry point");
        }
    }

    public char getPlayerTurnSign(){
        return m_RegisterPlayers.get(m_Turn).getSignOnBoard();
    }

    public int getUniqueID(){
        return m_RegisterPlayers.get(m_Turn).getUniqueID();
    }
    
    public String quitGame(){
        String retiredName = m_RegisterPlayers.get(m_Turn).getName();
        for(int i = m_Board.getRows() - 1; i>= 0;--i) {
            for (int j = m_Board.getCols() - 1; j >= 0; --j) {
                if (m_Board.checkLegalPopoutMode(i, j, true, m_RegisterPlayers.get(m_Turn).getSignOnBoard())) {
                    m_Board.popoutDisc(i, j);
                    ++j;
                }
            }
        }
        m_RegisterPlayers.remove(m_Turn);
        if(m_Turn == m_RegisterPlayers.size())
            m_Turn = 0;
        if(m_RegisterPlayers.size() == 1)
            m_Winner.add(m_RegisterPlayers.get(0));
        else {
            for (int i = m_Board.getRows() - 1; i >= 0; --i) {
                for (int j = m_Board.getCols() - 1; j >= 0; --j) {
                    if (m_Board.checkExistingDisc(i, j)) {
                        Point currentDiscCoordinate = new Point(j, i);
                        if (m_Board.checkWinGame(currentDiscCoordinate, m_Sequence, m_Board.getSignOnBoard(i, j), m_Varient)) {
                            setWinners(i, j);
                        }
                    }
                }
            }
        }
        return retiredName;
    }

    private void setWinners(int i_Row,int i_Col) {
        for (int k = 0; k < m_RegisterPlayers.size(); ++k) {
            if (m_RegisterPlayers.get(k).getSignOnBoard() == m_Board.getSignOnBoard(i_Row, i_Col) && m_Winner.contains(m_RegisterPlayers.get(k)) == false)
                m_Winner.add(m_RegisterPlayers.get(k));
        }
    }

    public boolean checkLegalComputerPopoutMove(int i_Col){
        return m_Turn == m_Board.getTurnByDisc(new Point(i_Col,m_Board.getRows() - 1));
    }

    public Point humanMove(int i_Col, boolean i_Popout) throws IllegalArgumentException{
        if(m_Board.checkOutOfRange(i_Col)){
            if((i_Popout || m_Board.checkFullColOnBoard(i_Col)) && m_Board.checkLegalPopoutMode(m_Board.getRows() - 1,i_Col,i_Popout,m_RegisterPlayers.get(m_Turn).getSignOnBoard())){
                Point currentCoordinateMove = updateDiscInPlayerAndBoard(i_Col,i_Popout);
                return currentCoordinateMove;
            }
            else{
                return null;
            }
        }
        else{
            throw new IllegalArgumentException();
        }
    }

    public Point computerOperation(){
        Point currentMoveCoordinate = null;
        int computerMoveType;

        do{
            Random rand = new Random();
            if(m_Varient == VarientEnum.POPOUT) {
                computerMoveType = rand.nextInt(2);
                if (computerMoveType == 1) {
                    currentMoveCoordinate = computerPopoutMove();
                    if (currentMoveCoordinate != null)
                        m_PopoutMove = true;
                } else {
                    currentMoveCoordinate = regularMove();
                    if(currentMoveCoordinate != null)
                        m_PopoutMove = false;
                }
            }
            else{
                currentMoveCoordinate = regularMove();
                if(currentMoveCoordinate != null)
                    m_PopoutMove = false;
            }
        }
        while(currentMoveCoordinate == null);

        return currentMoveCoordinate;
    }

    private Point computerPopoutMove(){
        Point currentMoveCoordinate = null;
        for(int i = 0;i<m_Board.getCols();++i){
            if(m_Board.getSignOnBoard(m_Board.getRows() - 1,i) ==(m_RegisterPlayers.get(m_Turn).getSignOnBoard())){
                currentMoveCoordinate = updateDiscInPlayerAndBoard(i,true);
                break;
            }
        }
        return currentMoveCoordinate;
    }

    private Point regularMove(){
        Random rand = new Random();
        Point currentMoveCoordinate = null;
        int currentCol = rand.nextInt(m_Board.getCols());
        if(m_Board.checkFullColOnBoard(currentCol)){
            currentMoveCoordinate = updateDiscInPlayerAndBoard(currentCol,false);
        }
        return currentMoveCoordinate;
    }

    public String getPlayerTurnSign(Point i_LastMove){
        return m_Board.getSignOnBoard(i_LastMove);
    }


    public void finishedTurn(Point i_LastMove){
        checkWinGame(i_LastMove);
        m_Turn = (m_Turn + 1)% m_RegisterPlayers.size();
        if(m_Winner.size() == 0)
            checkDraw();
    }

    private void checkDraw()
    {
        m_Draw = m_Board.checkDraw(m_Varient,m_RegisterPlayers.get(m_Turn).getSignOnBoard());
    }

    private void checkWinGame(Point i_LastMove){
        if(m_HistoryMoves.get(m_HistoryMoves.size() - 1).getPopout()){
            for(int i = m_Board.getRows() - 1;i > 0 && m_Board.checkExistingDisc(i,i_LastMove.x) == true; --i){
                Point currentDiscCoordinate = new Point(i_LastMove.x,i);
                PlayerEngine currentPlayer = m_RegisterPlayers.get(m_Board.getTurnByDisc(currentDiscCoordinate));
                if(m_Board.checkWinGame(currentDiscCoordinate,m_Sequence,currentPlayer.getSignOnBoard(),m_Varient)){
                    m_Winner.add(currentPlayer);
                }
            }
        }
        else if(m_Board.checkWinGame(i_LastMove,m_Sequence,m_RegisterPlayers.get(m_Turn).getSignOnBoard(),m_Varient)){
            m_Winner.add(m_RegisterPlayers.get(m_Turn));
        }
    }

    private Point updateDiscInPlayerAndBoard(int i_CurrentCol,boolean i_Popout) {
        Point currentMoveCoordinate;
        int row = m_Board.updateDiscOnBoard(i_CurrentCol,m_RegisterPlayers.get(m_Turn).getSignOnBoard(),m_Turn,i_Popout);
        currentMoveCoordinate = new Point(i_CurrentCol,row);
        addDisc(currentMoveCoordinate,i_Popout);
        m_RegisterPlayers.get(m_Turn).increaseTurnPlayed();
        return currentMoveCoordinate;
    }

    public boolean checkFinishedGame(){
        if(m_Winner.size() >= 1 || m_Draw == true){
            m_Status = GameStateEnum.END_GAME;
            return true;
        }
        return false;
    }

    public void setStatus(GameStateEnum i_Status){
        m_Status = i_Status;
    }

    @Override
    public void restartGame(){
        m_RestartGame = true;
        initialGame(m_GameDetails);
        m_HistoryMoves.clear();
        m_Turn = 0;
        m_Draw = false;
        m_Winner = new ArrayList<>();
    }

    @Override
    public void saveGame(ObjectOutputStream i_DataOut) throws IOException {
        i_DataOut.writeObject(this);
    }
}
