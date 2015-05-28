package journalmain;

import javafx.beans.property.SimpleStringProperty;

public class RezPriemData {
    private final SimpleStringProperty Patient = new SimpleStringProperty();
    private final SimpleStringProperty MOTCONSU_ID = new SimpleStringProperty();
    private final SimpleStringProperty MODELS_ID = new SimpleStringProperty();
    private final SimpleStringProperty STATUS = new SimpleStringProperty();
    private final SimpleStringProperty DATE = new SimpleStringProperty();
    private final SimpleStringProperty NAME = new SimpleStringProperty();

    public RezPriemData() {
        this("","","","","","");
    }

    public RezPriemData(String Patient, String MOTCONSU_ID, String MODELS_ID, String STATUS, String DATE, String NAME) {
        setPatient(Patient);
        setMOTCONSU_ID(MOTCONSU_ID);
        setMODELS_ID(MODELS_ID);
        setSTATUS(STATUS);
        setDATE(DATE);
        setNAME(NAME);
    }

    public String getPatient() {
        return Patient.get();
    }

    public void setPatient(String fName) {
        Patient.set(fName);
    }

    public SimpleStringProperty PatientProperty(){
        return Patient;
    }

    public String getMOTCONSU_ID() {
        return MOTCONSU_ID.get();
    }

    public void setMOTCONSU_ID(String fName) {
        MOTCONSU_ID.set(fName);
    }

    public SimpleStringProperty MOTCONSU_IDProperty(){
        return MOTCONSU_ID;
    }

    public String getMODELS_ID() {
        return MODELS_ID.get();
    }

    public void setMODELS_ID(String fName) {
        MODELS_ID.set(fName);
    }

    public SimpleStringProperty MODELS_IDProperty(){
        return MODELS_ID;
    }

    public String getSTATUS() {
        return STATUS.get();
    }

    public void setSTATUS(String fName) {
        STATUS.set(fName);
    }

    public SimpleStringProperty STATUSProperty(){
        return STATUS;
    }

    public String getDATE() {
        return DATE.get();
    }

    public void setDATE(String fName) {
        DATE.set(fName);
    }

    public SimpleStringProperty DATEProperty(){
        return DATE;
    }

    public String getNAME() {
        return NAME.get();
    }

    public void setNAME(String fName) {
        NAME.set(fName);
    }

    public SimpleStringProperty NAMEProperty(){
        return NAME;
    }

}