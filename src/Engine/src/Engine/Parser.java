package Engine;

import javax.xml.bind.JAXBException;
import java.io.Serializable;

public interface Parser extends Serializable {
    DetailsInput purse(String i_SourceName, int i_MaxRows,int i_MinRows ,int i_MaxCols, int i_MinCols) throws JAXBException;
}
