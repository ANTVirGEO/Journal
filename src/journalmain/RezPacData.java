package journalmain;

import javafx.beans.property.SimpleStringProperty;

public class RezPacData {
    private final SimpleStringProperty Pat_ID = new SimpleStringProperty();
    private final SimpleStringProperty EMK = new SimpleStringProperty();
    private final SimpleStringProperty Familiya = new SimpleStringProperty();
    private final SimpleStringProperty Imya = new SimpleStringProperty();
    private final SimpleStringProperty Otchestvo = new SimpleStringProperty();
    private final SimpleStringProperty Pol = new SimpleStringProperty();
    private final SimpleStringProperty DataR = new SimpleStringProperty();
    private final SimpleStringProperty Telefon = new SimpleStringProperty();
    private final SimpleStringProperty Email = new SimpleStringProperty();

    public RezPacData() {
        this("","","","","","","","","");
    }

    public RezPacData(String Pat_ID, String EMK, String Familiya, String Imya, String Otchestvo, String Pol, String DataR, String Telefon, String Email) {
        setPat_ID(Pat_ID);
        setEMK(EMK);
        setFamiliya(Familiya);
        setImya(Imya);
        setOtchestvo(Otchestvo);
        setPol(Pol);
        setDataR(DataR);
        setTelefon(Telefon);
        setEmail(Email);
    }

    public String getPat_ID() {
        return Pat_ID.get();
    }

    public void setPat_ID(String fName) {
        Pat_ID.set(fName);
    }

    public SimpleStringProperty Pat_IDProperty(){
        return Pat_ID;
    }

    public String getEMK() {
        return EMK.get();
    }

    public void setEMK(String fName) {
        EMK.set(fName);
    }

    public SimpleStringProperty EMKProperty(){
        return EMK;
    }

    public String getFamiliya() {
        return Familiya.get();
    }

    public void setFamiliya(String fName) {
        Familiya.set(fName);
    }

    public SimpleStringProperty FamiliyaProperty(){
        return Familiya;
    }

    public String getImya() {
        return Imya.get();
    }

    public void setImya(String fName) {
        Imya.set(fName);
    }

    public SimpleStringProperty ImyaProperty(){
        return Imya;
    }

    public String getOtchestvo() {
        return Otchestvo.get();
    }

    public void setOtchestvo(String fName) {
        Otchestvo.set(fName);
    }

    public SimpleStringProperty OtchestvoProperty(){
        return Otchestvo;
    }

    public String getPol() {
        return Pol.get();
    }

    public void setPol(String fName) {
        Pol.set(fName);
    }

    public SimpleStringProperty PolProperty(){
        return Pol;
    }

    public String getDataR() {
        return DataR.get();
    }

    public void setDataR(String fName) {
        DataR.set(fName);
    }

    public SimpleStringProperty DataRProperty(){
        return DataR;
    }

    public String getTelefon() {
        return Telefon.get();
    }

    public void setTelefon(String fName) {
        Telefon.set(fName);
    }

    public SimpleStringProperty TelefonProperty(){
        return Telefon;
    }

    public String getEmail() {
        return Email.get();
    }

    public void setEmail(String fName) {
        Email.set(fName);
    }

    public SimpleStringProperty EmailProperty(){
        return Email;
    }

}