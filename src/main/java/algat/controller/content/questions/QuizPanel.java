package algat.controller.content.questions;

import algat.App;
import algat.model.lesson.Question;
import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class QuizPanel {

    public Label counterLabel;
    public Button submitButton;
    public AnchorPane questionContainerPane;
    public AnchorPane mainPane;

    private List<Question> questions;
    private int displayedQuestionIndex;
    private QuestionPanel displayedQuestionController;


    private final Color counterLabelBaseColor = new Color(0.1,0.1, 0.1, 1);

    private final Color correctAnswerLabelColor = new Color(0.2,0.55, 0.2, 1);
    private final Duration correctAnswerLabelAnimDuration = Duration.seconds(0.12);

    private final Color incorrectAnswerLabelColor = new Color(0.8,0.2, 0.2, 1);
    private final Duration incorrectAnswerLabelColorDuration = Duration.seconds(0.2);
    private final Duration incorrectAnswerLabelWiggleDuration = Duration.seconds(0.10);
    private final double incorrectLabelWiggleSize = 10;

    public void initialize () {
        submitButton.setOnAction(event -> submit());
        counterLabel.setTextFill(counterLabelBaseColor);

        mainPane.getStylesheets().add(App.class.getResource("view/css/quiz.css").toExternalForm());

        questionContainerPane.widthProperty().addListener( (observable, oldVal, newVal) -> {
            questionContainerPane.setClip(new Rectangle(questionContainerPane.getWidth(),questionContainerPane.getHeight()));
        });
        questionContainerPane.heightProperty().addListener( (observable, oldVal, newVal) -> {
            questionContainerPane.setClip(new Rectangle(questionContainerPane.getWidth(),questionContainerPane.getHeight()));
        });
    }

    public void populate(List<Question> questions) {
        this.questions = questions;

        displayedQuestionController = assembleQuestionPane(questions.get(0));
        displayedQuestionIndex = 0;
        questionContainerPane.getChildren().add(displayedQuestionController.mainPane);
        AnchorPane.setTopAnchor(displayedQuestionController.mainPane, 0.0);
        AnchorPane.setLeftAnchor(displayedQuestionController.mainPane, 0.0);
        AnchorPane.setRightAnchor(displayedQuestionController.mainPane, 0.0);
        AnchorPane.setBottomAnchor(displayedQuestionController.mainPane, 0.0);

        setCounter(1);
    }



    private QuestionPanel assembleQuestionPane(Question question) {
        String resPath;

        if (question.getCorrectAnswers().size()>1) {
            resPath = "/algat/view/fxml/content/questions/MultipleQuestionPanel.fxml";
        } else {
            resPath = "/algat/view/fxml/content/questions/SingleQuestionPanel.fxml";
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resPath));
            loader.load();
            QuestionPanel questionPanelController = loader.getController();
            questionPanelController.populate(question);

            return questionPanelController;

        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("ERROR:failed to load the question panel at [" + resPath + "]");
        }
    }

    private void submit() {
        submitButton.setDisable(true);
        boolean answerWasCorrect = displayedQuestionController.check();

        if (answerWasCorrect) {
            displayedQuestionIndex++;

            // Correct answer animation
            Timeline counterLabelTimeline = new Timeline();
            counterLabelTimeline.getKeyFrames().add(new KeyFrame(correctAnswerLabelAnimDuration, new KeyValue(counterLabel.textFillProperty(), correctAnswerLabelColor, Interpolator.EASE_IN)));
            counterLabelTimeline.getKeyFrames().add(new KeyFrame(correctAnswerLabelAnimDuration, new KeyValue(counterLabel.scaleXProperty(), 1.10, Interpolator.EASE_IN)));
            counterLabelTimeline.getKeyFrames().add(new KeyFrame(correctAnswerLabelAnimDuration.multiply(4), new KeyValue(counterLabel.textFillProperty(), counterLabelBaseColor, Interpolator.EASE_BOTH)));
            counterLabelTimeline.getKeyFrames().add(new KeyFrame(correctAnswerLabelAnimDuration.multiply(2), new KeyValue(counterLabel.scaleXProperty(), 1, Interpolator.EASE_BOTH)));


            if (displayedQuestionIndex<questions.size()) {

                setCounter(displayedQuestionIndex+1);

                counterLabelTimeline.setOnFinished(event -> {
                    switchDisplayedQuestion(questions.get(displayedQuestionIndex));
                });
            } else {
                // Completed last answer
                counterLabelTimeline.setOnFinished(event -> {
                    // TODO switch to completed quiz panel
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/content/questions/CompletedQuizPanel.fxml"));
                        AnchorPane completedQuizPane = loader.load();

                        mainPane.getChildren().clear();
                        mainPane.getChildren().add(completedQuizPane);
                        AnchorPane.setRightAnchor(completedQuizPane, 0.0);
                        AnchorPane.setLeftAnchor(completedQuizPane, 0.0);
                        AnchorPane.setBottomAnchor(completedQuizPane, 0.0);
                        AnchorPane.setTopAnchor(completedQuizPane, 0.0);

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("ERROR:failed to load the CompletedQuizPanel");
                    }
                });
            }



            counterLabelTimeline.play();

        } else {
            // Incorrect answer animation
            Timeline counterLabelTimeline = new Timeline();
            counterLabelTimeline.getKeyFrames().add(new KeyFrame(incorrectAnswerLabelColorDuration, new KeyValue(counterLabel.textFillProperty(), incorrectAnswerLabelColor, Interpolator.EASE_IN)));
            counterLabelTimeline.getKeyFrames().add(new KeyFrame(incorrectAnswerLabelColorDuration.multiply(4), new KeyValue(counterLabel.textFillProperty(), counterLabelBaseColor, Interpolator.EASE_BOTH)));

            int wigglesNumber = 3;
            for (int i = 0; i < wigglesNumber; i++) {
                double wiggleXTranslation = incorrectLabelWiggleSize;
                if (i%2==0) wiggleXTranslation = -wiggleXTranslation;
                counterLabelTimeline.getKeyFrames().add(new KeyFrame(incorrectAnswerLabelWiggleDuration.multiply(i+1),
                        new KeyValue(counterLabel.translateXProperty(), wiggleXTranslation, Interpolator.EASE_BOTH)));
            }
            counterLabelTimeline.getKeyFrames().add(new KeyFrame(incorrectAnswerLabelWiggleDuration.multiply(wigglesNumber+1),
                    new KeyValue(counterLabel.translateXProperty(), 0, Interpolator.EASE_BOTH)));

            counterLabelTimeline.setOnFinished(event -> {
                switchDisplayedQuestion(questions.get(displayedQuestionIndex));
            });

            counterLabelTimeline.play();
        }

    }

    private void switchDisplayedQuestion(Question newQuestion) {
         submitButton.setDisable(true);

        QuestionPanel newQuestionController = assembleQuestionPane(newQuestion);
        questionContainerPane.getChildren().add(newQuestionController.mainPane);
        AnchorPane.setTopAnchor(newQuestionController.mainPane, 0.0);
        AnchorPane.setRightAnchor(newQuestionController.mainPane, 0.0);
        AnchorPane.setLeftAnchor(newQuestionController.mainPane, 0.0);
        AnchorPane.setBottomAnchor(newQuestionController.mainPane, 0.0);

        Duration translationDuration = Duration.seconds(0.5);

        TranslateTransition oldTranslate = new TranslateTransition(translationDuration, displayedQuestionController.mainPane);
        oldTranslate.setFromY(0);
        oldTranslate.setToY(-questionContainerPane.getHeight());

        FadeTransition oldFadeOut = new FadeTransition(translationDuration, displayedQuestionController.mainPane);
        oldFadeOut.setFromValue(1);
        oldFadeOut.setToValue(0);

        TranslateTransition newTranslate = new TranslateTransition(translationDuration, newQuestionController.mainPane);
        newTranslate.setFromY(questionContainerPane.getHeight());
        newTranslate.setToY(0);

        ParallelTransition parallelTranslate = new ParallelTransition(oldTranslate, oldFadeOut, newTranslate);
        parallelTranslate.setInterpolator(Interpolator.EASE_BOTH);
        parallelTranslate.setOnFinished(event -> {
            questionContainerPane.getChildren().remove(0);
            displayedQuestionController = newQuestionController;
            submitButton.setDisable(false);
        });

        parallelTranslate.play();
    }

    private void setCounter(int num) {
        counterLabel.setText(num + "/" + questions.size());
    }
}
