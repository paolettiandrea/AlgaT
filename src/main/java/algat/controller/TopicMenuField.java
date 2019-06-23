package algat.controller;

import algat.model.Lesson;
import algat.model.Topic;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
    public void populate(Topic topic) {
        topicLabel.setText(topic.getTopicName());
        for (Lesson lesson : topic.getLessonList()) {
            Label lessonLabel = new Label(lesson.getName());
            lessonLabel.setPadding(new Insets(0, 0, 0, 20));
            menuFieldVBox.getChildren().add(lessonLabel);
        }
    }
}
