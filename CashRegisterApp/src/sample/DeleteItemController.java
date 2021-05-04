package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DeleteItemController {
    /**
     * itemName field
     */
    @FXML
    private TextField itemName;

    /**
     * Returns item name
     * @return item name
     */
    public String getItemName(){
        return itemName.getText();
    }
}
