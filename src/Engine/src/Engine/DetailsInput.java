package Engine;

import java.util.List;

public class DetailsInput {
    private int m_Sequence;
    private int m_Rows;
    private int m_Cols;
    private VarientEnum m_Variant;
    private List<PlayerInput> m_PlayersInput;

    public List<PlayerInput> getPlayersInput() {
        return m_PlayersInput;
    }

    public void setPlayersInput(List<PlayerInput> m_PlayersInput) {
        this.m_PlayersInput = m_PlayersInput;
    }

    public int getSequence() {
        return m_Sequence;
    }

    public void setSequence(int m_Sequence) {
        this.m_Sequence = m_Sequence;
    }

    public int getRows() {
        return m_Rows;
    }

    public void setRows(int m_Rows) {
        this.m_Rows = m_Rows;
    }

    public int getCols() {
        return m_Cols;
    }

    public void setCols(int m_Cols) {
        this.m_Cols = m_Cols;
    }

    public VarientEnum getVariant() {
        return m_Variant;
    }

    public void setVariant(VarientEnum m_Variant) {
        this.m_Variant = m_Variant;
    }
}
