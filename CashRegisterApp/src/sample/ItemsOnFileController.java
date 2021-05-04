package sample;


import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class ItemsOnFileController {
    /**
     * Controller instance
     */
    private static Controller controller = new Controller();

    /**
     * All items tableView
     */
    @FXML
    private TableView<ItemOnFile> itemTable;

    /**
     * Initialize view all items window
     */
    public void initialize(){
        refresh();
        Task<ObservableList<ItemOnFile>> task = new Task() {
            @Override
            public ObservableList<ItemOnFile> call() {
                return DataSource.getInstance().getItemsOnFile();
            }
        };
        itemTable.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }

    /**
     * Displays new item dialog
     */
    @FXML
    public void showAddNewItemDialog(){
        controller.showAddNewItemDialog();
    }

    /**
     * Displays delete item dialog
     */
    @FXML
    public void showDeleteWindow(){
        controller.showDeleteItemDialog();
    }

    /**
     * Refreshes window
     */
    @FXML
    public void refresh(){
        DataSource.getInstance().getItemsOnFile().clear();
        DataSource.getInstance().populateList();
    }

}
