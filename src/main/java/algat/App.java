package algat;

import algat.controller.Main;
import algat.model.AppContent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {
    /***
     * The primary stage made available by JavaFX on application start
     */
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The method called by JavaFX after the instantiation of this Application
     *
     * @param stage The primary stage made available by JavaFX
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Main.fxml"));
        Parent root = loader.load();

        // Load the content data and populate the hierarchy with it
        AppContent data = new AppContent("content");
        Main mainController = loader.getController();
        mainController.populate(data);


        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("view/styles.css").toExternalForm());

        primaryStage.setTitle("AlgaT");
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
