package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.fxmisc.richtext.InlineCssTextArea;

public class Controller {
    /**
     * Input area
     */
    @FXML
    private InlineCssTextArea inputArea;

    /**
     * List area
     */
    @FXML
    private InlineCssTextArea listArea;

    /**
     * Total area
     */
    @FXML
    private InlineCssTextArea totalArea;

    /**
     * Company info area
     */
    @FXML
    private InlineCssTextArea companyInfoArea;

    /**
     * Main window
     */
    @FXML
    private VBox mainWindow;

    /**
     * Boolean to store if quantity button is pressed
     */
    private boolean quantityPressed = false;

    /**
     * Boolean to store if total botton is pressed
     */
    private boolean totalPressed = false;

    /**
     * Boolean to store if No tax button is pressed
     */
    private boolean hasTax = true;

    /**
     * String to store receipt total
     */
    private String receiptTotal;

    /**
     * String to store receipt change
     */
    private String receiptChange;

    /**
     * Load company information from database
     */
    public void loadCompanyInfo(){
        try{
            CompanyInfo companyInfo = DataSource.getInstance().getCompanyInfo();
            companyInfoArea.insertText(companyInfoArea.getCaretPosition(), companyInfo.processCompanyInfo());
        }catch (SQLException e){
            showPopUpWindow("Add Company Information in settings", Alert.AlertType.INFORMATION);
            return;
        }
    }

    /**
     * Called when any number button is clicked
     * @param event button clicked
     */
    @FXML
    public void processNum(ActionEvent event){
        String value = ((Button)event.getSource()).getText();
        inputArea.insertText(inputArea.getCaretPosition(), value);
    }

    /**
     * Called when any back button is clicked
     */
    @FXML
    public void processBack(){
        String inputText = inputArea.getText();
        if(inputText.isEmpty()){
            return;
        }
        inputText = inputText.substring(0,inputText.length() - 1);
        inputArea.replaceText(inputText);
    }

    /**
     * Called when noTax button is clicked
     */
    @FXML
    public void processNoTax(){
        hasTax = false;
    }

    /**
     * Called when department name is clicked
     * @param event button clicked
     */
    @FXML
    public void processItem(ActionEvent event){
        String itemType = ((Button)event.getSource()).getText();
        String input = inputArea.getText();
        input = input.replaceAll("\\sQty","");
        String[] itemInfo = input.split(" ");
        Item item;
        if(quantityPressed){
            int itemQuantity = Integer.parseInt(itemInfo[0]);
            String price = itemInfo[1];
            item = new Item(itemType, price, itemQuantity, hasTax);
            quantityPressed = false;
        }else{
            String price = itemInfo[0];
            item = new Item(itemType,price,1, hasTax);
        }

        CashRegister.getInstance().addToItemList(item);
        listArea.setStyle(listArea.getCurrentParagraph(), "-fx-fill: #0466c8;");
        listArea.insertText(listArea.getCaretPosition(),item.toString());
        listArea.setEditable(false);
        inputArea.clear();
        displaySubtotal();
        hasTax = true;
        totalPressed = false;
    }

    /**
     * Called when Qty button is clicked
     * @param event button clicked
     */
    @FXML
    public void processQuantity(ActionEvent event){
        String inputText = inputArea.getText();
        if(inputText.isEmpty()){
            return;
        }

        String buttonValue = ((Button)event.getSource()).getText();
        inputArea.replaceText(inputArea.getText() + " " + buttonValue + " ");
        quantityPressed = true;
    }

    /**
     * Called when void button is clicked
     */
    @FXML
    public void processVoid(){
        try{
            Item removedItem = CashRegister.getInstance().voidLastItem();
            listArea.setStyle(listArea.getCurrentParagraph(), "-fx-fill: #E63946;");
            listArea.insertText(listArea.getCaretPosition(),  removedItem.getName() + " " + "-" +removedItem.getPriceForEach().multiply(new BigDecimal(removedItem.getQuantity())) + "\n");
            displaySubtotal();
        }catch (Exception e){
            showPopUpWindow("List is empty", Alert.AlertType.INFORMATION);
            return;
        }
    }

    /**
     * Displays subtotal
     */
    private void displaySubtotal(){
        totalArea.replaceText("Subtotal: " + CashRegister.getInstance().getSubTotal() + "\n");
    }

    /**
     * Displays total when total button is clicked
     */
    @FXML
    public void displayTotal(){
        if(!totalPressed){
            totalArea.insertText(totalArea.getCaretPosition(), "Tax: " + CashRegister.getInstance().getTotalTax().toString() + "\n" + "Total: " + CashRegister.getInstance().getTotal().toString());
            receiptTotal = totalArea.getText();
            totalPressed = true;
        }
    }

    /**
     * Called when cash button is clicked
     */
    @FXML
    public void processCash(){
        BigDecimal cashPaid = new BigDecimal(inputArea.getText()).setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal change = CashRegister.getInstance().getChange(cashPaid);
        displayChange(change.toString(), cashPaid);
        receiptChange = totalArea.getText();
        CashRegister.getInstance().generateReceipt(receiptTotal, receiptChange);
        inputArea.clear();
        listArea.clear();
        CashRegister.getInstance().reInitialize();
    }

    /**
     * Displays change
     * @param change    customer's change
     * @param cashPaid  cash amount paid by customer
     */
    private void displayChange(String change, BigDecimal cashPaid){
        totalArea.replaceText("Cash: " + cashPaid + "\n" + "Change: " + change);
    }

    /**
     * Called when Code button is clicked
     */
    @FXML
    public void processCode(){
        if(quantityPressed){
            quantityPressed = false;
            String input = inputArea.getText();
            input = input.replaceAll("\\sQty", "");
            String[] itemInfo =  input.split("\\s");
            int quantity = Integer.parseInt(itemInfo[0]);
            int plu = Integer.parseInt(itemInfo[1]);
            totalPressed = false;
            Runnable task = () -> {
                try{
                    Item item = DataSource.getInstance().getItemUsingPlu(plu, quantity);
                    confirmItem(item);
                }catch (SQLException e){
                    Platform.runLater(
                            () -> showPopUpWindow("Error", Alert.AlertType.ERROR)
                    );
                }
            };
            new Thread(task).start();
        }else{
            int plu = Integer.parseInt(inputArea.getText());
            Runnable task = () -> {
                try{
                    Item item  = DataSource.getInstance().getItemUsingPlu(plu);
                    confirmItem(item);
                }catch (SQLException e){
                    Platform.runLater(
                            () -> showPopUpWindow("Error", Alert.AlertType.ERROR)
                    );
                }
            };
            new Thread(task).start();
        }
    }

    /**
     * Checks if items is exists and adds to item list
     * @param item item to check
     */
    private void confirmItem(Item item){
        if(item != null){
            CashRegister.getInstance().addToItemList(item);
            Platform.runLater(
                    () -> {
                        listArea.setStyle(listArea.getCurrentParagraph(), "-fx-fill: #0466c8;");
                        listArea.insertText(listArea.getCaretPosition(), CashRegister.getInstance().getItemList().getLast().toString());
                        listArea.setEditable(false);
                        inputArea.clear();
                        displaySubtotal();
                    }
            );
        }else{
            Platform.runLater(
                    () -> {
                        showPopUpWindow("Item not found", Alert.AlertType.ERROR);
                        inputArea.clear();
                    }

            );
        }
    }

    /**
     * Called when Add ompany info dialog is clicked
     */
    @FXML
    public void showCompanyInfoDialog(){
        Dialog<ButtonType> dialog = new Dialog();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("Company information");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("companyInfoWindow.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            showPopUpWindow("Error", Alert.AlertType.ERROR);
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result =  dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            CompanyInfoController controller = fxmlLoader.getController();
            Runnable task = () -> {
                try{
                    DataSource.getInstance().insertCompanyInfo(controller.getCompanyName(), controller.getCompanyAddress(), controller.getCompanyPhoneNumber(), controller.getCompanyEmail());
                    Platform.runLater(
                            () -> {
                                companyInfoArea.clear();
                                String companyInfo = controller.processCompanyInfo();
                                companyInfoArea.insertText(companyInfoArea.getCaretPosition(),companyInfo);
                                showPopUpWindow("Item Successfully added", Alert.AlertType.INFORMATION);
                            }
                    );
                }catch(SQLException e){
                    Platform.runLater(
                            () -> showPopUpWindow("Couldn't add company info", Alert.AlertType.ERROR)
                    );
                }
            };
            new Thread(task).start();
        }
    }

    /**
     * Called when Add new item menu is clicked
     */
    @FXML
    public void showAddNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog();
        dialog.setTitle("Add New Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddNewItemWindow.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            showPopUpWindow("Error", Alert.AlertType.ERROR);
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result =  dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            Runnable task = () -> {
                AddNewItemController controller = fxmlLoader.getController();
                try{
                    boolean isAdded = DataSource.getInstance().insertItem(controller.getItemName(), controller.getItemPlu(), controller.getItemPrice(), controller.getHasTax());
                    if(isAdded){
                        Platform.runLater(
                                () -> showPopUpWindow("Item Successfully added", Alert.AlertType.INFORMATION)
                        );
                    }else{
                        Platform.runLater(
                                () -> showPopUpWindow("Item already on file", Alert.AlertType.INFORMATION)
                        );
                    }
                }catch (SQLException e){
                    Platform.runLater(
                            () -> showPopUpWindow("Couldn't add item", Alert.AlertType.ERROR)
                    );
                }
            };
            new Thread(task).start();
        }else{
            System.out.println("Cancel button was pressed");
            return;
        }
    }

    /**
     * Called when Search for item menu is clicked
     */
    @FXML
    public void showSearchItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setTitle("Search Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("SearchItemWindow.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e){
            showPopUpWindow("Error", Alert.AlertType.ERROR);
            return;
        }

        ButtonType addToItemList = new ButtonType("Add to list");

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(addToItemList);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == addToItemList){
            Runnable task = () -> {
                SearchItemController controller = fxmlLoader.getController();
                try{
                    Item item = DataSource.getInstance().getItemUsingName(controller.getItemName(), controller.getQuantity());
                    if(item != null){
                        CashRegister.getInstance().addToItemList(item);
                        Platform.runLater( () -> {
                                    listArea.setStyle(listArea.getCurrentParagraph(), "-fx-fill: #0466c8;");
                                    listArea.insertText(listArea.getCaretPosition(), CashRegister.getInstance().getItemList().getLast().toString());

                                    listArea.setEditable(false);
                                    inputArea.clear();
                                    displaySubtotal();
                                }
                        );
                    }else{
                        Platform.runLater(
                                ()  -> showPopUpWindow("Item not on file", Alert.AlertType.ERROR)
                        );
                    }
                }catch (SQLException e){
                    Platform.runLater(
                            () -> showPopUpWindow("Error", Alert.AlertType.ERROR)
                    );
                }
            };
            new Thread(task).start();
        }
    }

    /**
     * Called when delete item menu is clicked
     */
    @FXML
    public void showDeleteItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete Item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("DeleteItemWindow.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            showPopUpWindow("Error", Alert.AlertType.ERROR);
            return;
        }

        ButtonType deleteItem = new ButtonType("Delete");

        dialog.getDialogPane().getButtonTypes().add(deleteItem);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == deleteItem){
            Runnable task = () -> {
                    DeleteItemController controller = fxmlLoader.getController();
                    try{
                        boolean isDeleted = DataSource.getInstance().deleteItem(controller.getItemName());
                        if(isDeleted){
                            Platform.runLater(
                                    () -> showPopUpWindow("Item deleted", Alert.AlertType.INFORMATION)
                            );
                        }else{
                            Platform.runLater(
                                    ()  -> showPopUpWindow("Item does not exist", Alert.AlertType.INFORMATION)
                            );
                        }
                    }catch (SQLException e){
                        Platform.runLater(
                                () -> showPopUpWindow("Error", Alert.AlertType.ERROR)
                        );
                    }
            };
            new Thread(task).start();
        }else{
            System.out.println("Cancel key was pressed");
            return;
        }
    }

    /**
     *  Called when view all item menu is clicked
     */
    @FXML
    public void showAllItemWindow(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsOnFileWindow.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        }catch (IOException e){
            showPopUpWindow("Error", Alert.AlertType.ERROR);
            e.printStackTrace();
            return;
        }
    }

    /**
     * Shows popup window
     * @param message message to display
     * @param alertType type of alert
     */
    private void showPopUpWindow(String message, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Alert");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
