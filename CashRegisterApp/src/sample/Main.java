package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Start method called by javafx thread
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 480));
        primaryStage.show();
        Controller controller = loader.getController();
        controller.loadCompanyInfo();
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Main inti method called by javafx thread
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        super.init();
        DataSource.getInstance().open();
    }

    /**
     * Stop method by javafx thread when application is closed
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        DataSource.getInstance().close();
    }
}
