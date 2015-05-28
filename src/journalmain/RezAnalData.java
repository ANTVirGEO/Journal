package journalmain;

import javafx.beans.property.SimpleStringProperty;

public class RezAnalData {
    private final SimpleStringProperty RVAL = new SimpleStringProperty();
    private final SimpleStringProperty LABEL = new SimpleStringProperty();
    private final SimpleStringProperty ANALYSIS_LABEL = new SimpleStringProperty();

    public RezAnalData() {
        this("","","");
    }

    public RezAnalData(String RVAL, String LABEL, String ANALYSIS_LABEL) {
        setRVAL(RVAL);
        setLABEL(LABEL);
        setANALYSIS_LABEL(ANALYSIS_LABEL);
    }

    public String getRVAL() {
        return RVAL.get();
    }

    public void setRVAL(String fName) {
        RVAL.set(fName);
    }

    public SimpleStringProperty Pat_IDProperty(){
        return RVAL;
    }

    public String getLABEL() {
        return LABEL.get();
    }

    public void setLABEL(String fName) {
        LABEL.set(fName);
    }

    public SimpleStringProperty LABELProperty(){
        return LABEL;
    }

    public String getANALYSIS_LABEL() {
        return ANALYSIS_LABEL.get();
    }

    public void setANALYSIS_LABEL(String fName) {
        ANALYSIS_LABEL.set(fName);
    }

    public SimpleStringProperty ANALYSIS_LABELProperty(){
        return ANALYSIS_LABEL;
    }

}