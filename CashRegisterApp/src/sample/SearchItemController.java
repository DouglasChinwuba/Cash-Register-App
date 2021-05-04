package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class SearchItemController {

    /**
     * itemName textField
     */
    @FXML
    private TextField itemName;

    /**
     * item quantity spinner
     */
    @FXML
    private Spinner quantity;

    /**
     * Returns item Name
     * @return  itemName
     */
    public String getItemName(){
        return itemName.getText();
    }

    /**
     * Returns item quantity
     * @return item quantity
     */
    public int getQuantity(){
        return (int)quantity.getValue();
    }
}
