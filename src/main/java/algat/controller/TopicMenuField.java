package algat.controller;

import algat.model.Topic;
import algat.model.lesson.Lesson;
import algat.view.AppViewSettings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class TopicMenuField {

    @FXML
    public Label topicLabel;
    @FXML
    public VBox topicMenuField;

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

            // TODO Make the lessonMenuButton its own fx entity (controller+fxml)
            Label lessonMenuButton = new Label(lesson.getName());
            lessonMenuButton.setPadding(new Insets(0, 0, 0,AppViewSettings.menuLessonIndentation));
            lessonMenuButton.setOnMouseClicked(event -> {
                contentPanelController.loadLesson(lesson);
            });
            //lessonMenuButton.setPadding(new Insets(0,0,0,30));
            topicMenuField.getChildren().add(lessonMenuButton);
        }
    }

}
