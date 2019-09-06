package algat;

import algat.controller.Main;
import algat.model.AppContent;
import javafx.application.Application;
import javafx.application.HostServices;
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

    private static HostServices hostServices;


    public static HostServices getServices() {
        return hostServices;
    }

    /**
     * Main method used to explicitly launch the application
     * @param args
     */
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/fxml/Main.fxml"));
        Parent root = loader.load();

        // Load the content data and populate the hierarchy with it
        AppContent data = new AppContent("content");
        Main mainController = loader.getController();
        mainController.populate(data, primaryStage);


        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("view/css/main.css").toExternalForm());

        primaryStage.setTitle("AlgaT");
        primaryStage.setScene(scene);
        primaryStage.show();

        // FIXME this is a temporary and pretty bad solution that will most defenetely need refactoring
        // the hostservices should only be needed by the hyperlinks in order to use the showDocument method,
        // if so just pass the reference down the application
        if (hostServices==null) {
            hostServices = getHostServices();
        } else {
            throw new Error("The App class was run twice, this error originated from a piece of code that needs refactoring");
        }

    }
}
