package JavaFX;


import javafx.scene.control.Button;

public class ComplexButton extends Button{
    private int m_Col;
    private int m_Row;
    private String m_Color =null;

    public ComplexButton(int i_Row, int i_Col){
        m_Row = i_Row;
        m_Col = i_Col;
    }

    public String getColor(){
        return m_Color;
    }

    public void setColor(String i_Color){
        m_Color = i_Color;
    }

    public int getCol(){
        return m_Col;
    }

    public int getRow(){
        return m_Row;
    }

}
