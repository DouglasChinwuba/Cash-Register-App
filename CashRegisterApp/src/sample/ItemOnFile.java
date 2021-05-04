package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public class ItemOnFile {
    /**
     * SimpleStringProperty name
     */
    private SimpleStringProperty name = new SimpleStringProperty();

    /**
     * SimpleIntegerProperty plu
     */
    private SimpleIntegerProperty plu = new SimpleIntegerProperty();

    /**
     * SimpleObjectProperty price
     */
    private SimpleObjectProperty price = new SimpleObjectProperty();

    /**
     * SimpleBooleanProperty hasTax
     */
    private SimpleBooleanProperty hasTax = new SimpleBooleanProperty();

    /**
     * ItemOnFile Constructor
     * @param name   item name
     * @param plu    item plu
     * @param price  item price
     * @param hasTax tax item
     */
    public ItemOnFile(String name, int plu, BigDecimal price, int hasTax){
        this.name.set(name);
        this.plu.set(plu);
        this.price.set(price);
        this.hasTax.set(hasTax == 1 ? true : false);
    }

    /**
     * Item name setter
     * @param name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Item name getter
     * @return item name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Item plu getter
     * @return item plu
     */
    public int getPlu() {
        return plu.get();
    }

    /**
     * item plu getter
     * @return plu
     */
    public SimpleIntegerProperty pluProperty() {
        return plu;
    }

    /**
     * item plu setter
     * @param plu
     */
    public void setPlu(int plu) {
        this.plu.set(plu);
    }

    /**
     * item price getter
     * @return price
     */
    public Object getPrice() {
        return price.get();
    }

    /**
     * item plu getter
     * @return price
     */
    public SimpleObjectProperty priceProperty() {
        return price;
    }

    /**
     * item price setter
     * @param price
     */
    public void setPrice(Object price) {
        this.price.set(price);
    }

    /**
     * item hasTax getter
     * @return hasTax
     */
    public boolean isHasTax() {
        return hasTax.get();
    }

    /**
     * item hasTax getter
     * @return hasTax
     */
    public SimpleBooleanProperty hasTaxProperty() {
        return hasTax;
    }

    /**
     * item hasTax setter
     * @param hasTax
     */
    public void setHasTax(boolean hasTax) {
        this.hasTax.set(hasTax);
    }

    /**
     * itemOnFile toString
     * @return itemOnFile toString
     */
    @Override
    public String toString() {
        return "ItemOnFile{" +
                "name='" + name + '\'' +
                ", plu=" + plu +
                ", price=" + price +
                ", hasTax=" + hasTax +
                '}';
    }
}
