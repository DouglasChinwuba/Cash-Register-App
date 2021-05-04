package sample;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class AddNewItemController {
    /**
     * itemName field
     */
    @FXML
    private TextField itemName;

    /**
     * itemPlu field
     */
    @FXML
    private TextField itemPlu;

    /**
     * itemPrice field
     */
    @FXML
    private TextField itemPrice;

    /**
     * Yes radioButton
     */
    @FXML
    private RadioButton yes;

    /**
     * item name getter
     * @return item name
     */
    public String getItemName(){
        return itemName.getText();
    }

    /**
     * Item plu getter
     * @return item plu
     */
    public int getItemPlu(){
        return Integer.parseInt(itemPlu.getText());
    }

    /**
     * Item price getter
     * @return item price
     */
    public BigDecimal getItemPrice() {
        return new BigDecimal(itemPrice.getText()).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Item hasTax getter
     * @return 1 if item has tax
     */
    public int getHasTax() {
        return yes.isSelected() ? 1 : 0;
    }
}
