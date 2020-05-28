package hellofx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataModel {
    StringProperty first = new SimpleStringProperty();
    //getter
    public String getFirst() {return first.get();}
    //setter
    public void setFirst(String first) {this.first.set(first);}
    //new "property" accessor
    public StringProperty firstProperty() {return first;}
}
