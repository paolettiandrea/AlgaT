package algat.controller;

import algat.controller.content.ContentPanel;
import algat.controller.content.lessons.LessonPanel;
import algat.controller.content.questions.QuizPanel;
import algat.model.Topic;
import algat.model.lesson.Lesson;
import algat.view.AppViewSettings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Controller for a field that displays buttons for a topic it's lessons (and possibly also blocks of a lesson?)
 */
public class TopicMenuField {

    public Label topicLabel;
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
            Label lessonMenuButton = new Label(lesson.getName());
            lessonMenuButton.getStyleClass().add("lesson-menu-label");
            lessonMenuButton.setPadding(new Insets(0, 15, 0, AppViewSettings.menuLessonIndentation));
            lessonMenuButton.setOnMouseClicked(event -> {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/content/lessons/LessonPanel.fxml"));
                    AnchorPane newLessonPane = loader.load();
                    LessonPanel newLessonController = loader.getController();

                    newLessonController.loadLesson(lesson);
                    contentPanelController.changeContent(newLessonPane);

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("ERROR:failed to load the LessonPanel");
                }

            });

            BorderPane lessonBorderPane = new BorderPane();

            lessonBorderPane.setLeft(lessonMenuButton);

            if (!lesson.getQuestions().isEmpty()) {
                Label quizMenuButton = new Label("Q");
                quizMenuButton.getStyleClass().add("lesson-menu-label");
                lessonBorderPane.setRight(quizMenuButton);

                quizMenuButton.setOnMouseClicked(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/content/questions/QuizPanel.fxml"));
                        AnchorPane quizPane = loader.load();
                        QuizPanel quizController = loader.getController();
                        quizController.populate(lesson.getQuestions());

                        contentPanelController.changeContent(quizPane);

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("ERROR:failed to load the TopicMenuField");
                    }
                });
            }

            topicMenuField.getChildren().add(lessonBorderPane);
        }
    }

}
