package journalmain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CalcServData {
    private final SimpleStringProperty Code = new SimpleStringProperty();
    private final SimpleStringProperty Name = new SimpleStringProperty();
    private final SimpleIntegerProperty Price = new SimpleIntegerProperty();
    private final SimpleStringProperty PriceType = new SimpleStringProperty();
    private final SimpleStringProperty PriceDate = new SimpleStringProperty();

    public CalcServData() {
        this("", "", 0, "", "");
    }

    public CalcServData(String Code, String Name, Integer Price, String PriceType, String PriceDate) {
        setCode(Code);
        setName(Name);
        setPrice(Price);
        setPriceType(PriceType);
        setPriceDate(PriceDate);
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

    public SimpleIntegerProperty UserPriceProperty(){
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
}