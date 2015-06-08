package journalmain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CalcServAdvData {
    private final SimpleStringProperty Code = new SimpleStringProperty();
    private final SimpleStringProperty Name = new SimpleStringProperty();
    private final SimpleIntegerProperty Price = new SimpleIntegerProperty();
    private final SimpleStringProperty PriceType = new SimpleStringProperty();
    private final SimpleStringProperty PriceDate = new SimpleStringProperty();
    private final SimpleIntegerProperty Count = new SimpleIntegerProperty();
    private final SimpleIntegerProperty Summa = new SimpleIntegerProperty();

    public CalcServAdvData() {
        this("", "", 0, "", "", 0, 0);
    }

    public CalcServAdvData(String Code, String Name, Integer Price, String PriceType, String PriceDate, Integer Count, Integer Summa) {
        setCode(Code);
        setName(Name);
        setPrice(Price);
        setPriceType(PriceType);
        setPriceDate(PriceDate);
        setCount(Count);
        setSumma(Summa);
    }

    public String getCode() {
        return Code.get();
    }

    public void setCode(String fName) {
        Code.set(fName);
    }

    public SimpleStringProperty CodeProperty(){
        return Code;
    }

    public String getName() {
        return Name.get();
    }

    public void setName(String fName) {
        Name.set(fName);
    }

    public SimpleStringProperty NameProperty(){
        return Name;
    }

    public Integer getPrice() {
        return Price.get();
    }

    public void setPrice(Integer fName) {
        Price.set(fName);
    }

    public SimpleIntegerProperty PriceProperty(){
        return Price;
    }

    public String getPriceType() {
        return PriceType.get();
    }

    public void setPriceType(String fName) {
        PriceType.set(fName);
    }

    public SimpleStringProperty PriceTypeProperty(){
        return PriceType;
    }

    public String getPriceDate() {
        return PriceDate.get();
    }

    public void setPriceDate(String fName) {
        PriceDate.set(fName);
    }

    public SimpleStringProperty PriceDateProperty(){
        return PriceDate;
    }

    public Integer getCount() {
        return Count.get();
    }

    public void setCount(Integer fName) {
        Count.set(fName);
    }

    public SimpleIntegerProperty CountProperty(){
        return Count;
    }

    public Integer getSumma() {
        return Summa.get();
    }

    public void setSumma(Integer fName) {
        Summa.set(fName);
    }

    public SimpleIntegerProperty SummaProperty(){
        return Summa;
    }

}