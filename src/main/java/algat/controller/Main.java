package algat.controller;

import algat.model.AppContent;
import algat.model.Topic;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Main controller for the application
 */
public class Main {

    /**
     * An FX-injected reference to the VBox meant to contain the navigation menu
     */
    @FXML
    private VBox topicNavigationVBox;


    /**
     * Method called by FXML when the corresponding FXML is loaded
     */
    public void initialize() {

    }

    /**
     * Populate the application with the content
     *
     * @param data The content that the application needs to display
     */
    public void populate(AppContent data) {

        for (Topic topic : data.getTopicList()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/TopicMenuField.fxml"));
                AnchorPane topicMenuField = loader.load();
                TopicMenuField topicMenuFieldController = loader.getController();
                topicMenuFieldController.populate(topic);
                topicNavigationVBox.getChildren().add(topicMenuField);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR:failed to load the TopicMenuField");
            }
        }
    }
}
