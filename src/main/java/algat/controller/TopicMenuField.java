package algat.controller;

import algat.model.Topic;
import algat.model.lesson.Lesson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class TopicMenuField {

    @FXML
    public Label topicLabel;
    @FXML
    public VBox menuFieldVBox;

    public void initialize() {
        // No need for initialization for now
    }

    /**
     * Populates this TopicMenuField with the data of the given Topic
     *
     * @param topic The topic that needs to be represented by this field
     */
    public void populate(Topic topic, ContentPanel contentPanelController) {
        topicLabel.setText(topic.getTopicName());
        for (Lesson lesson : topic.getLessonList()) {

            Label lessonMenuButton = new Label(lesson.getName());
            lessonMenuButton.setOnMouseClicked(event -> {
                contentPanelController.loadLesson(lesson);
            });
            //lessonMenuButton.setPadding(new Insets(0,0,0,30));
            menuFieldVBox.getChildren().add(lessonMenuButton);
        }
    }
}
