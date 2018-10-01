package Engine;

public enum VarientEnum {
    REGULAR("Regular"),
    CIRCULAR("Circular"),
    POPOUT("Popout");

    private String m_Varient;

    private VarientEnum(String i_Varient){
        m_Varient = i_Varient;
    }

    @Override
    public String toString() {
        return m_Varient;
    }
}
