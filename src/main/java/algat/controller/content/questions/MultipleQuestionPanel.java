package algat.controller.content.questions;

import algat.model.lesson.Question;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.Vector;

public class MultipleQuestionPanel extends QuestionPanel {

    public BorderPane mainBorderPane;
    public VBox answersVBox;

    private Vector<CheckBox> answerCheckBoxes;


    @Override
    public void populate(Question question) {

        this.question = question;
        Node questionText = question.getQuestion().assemble();
        questionText.getStyleClass().add("question-text");
        questionText.getStyleClass().add("base-text");
        mainBorderPane.setTop(questionText);

        int totalAnswers = question.getIncorrectAnswers().size()+question.getCorrectAnswers().size();

        indexes = new Vector<>();
        for (int i = 0; i < totalAnswers; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);

        answerCheckBoxes = new Vector<>();
        for (int i = 0; i < totalAnswers; i++) {
            BorderPane answerBorderPane = new BorderPane();

            CheckBox radioButton = new CheckBox();
            answerCheckBoxes.add(radioButton);
            answerBorderPane.setLeft(radioButton);

            Node answer;
            if (indexes.get(i)>=question.getIncorrectAnswers().size()) {
                answer = question.getCorrectAnswers().get(indexes.get(i)-question.getIncorrectAnswers().size()).assemble();
                answerBorderPane.setCenter(answer);
            } else {
                answer = question.getIncorrectAnswers().get(indexes.get(i)).assemble();
                answerBorderPane.setCenter(answer);
            }
            answer.getStyleClass().add("answer-text");
            answer.getStyleClass().add("base-text");

            answersVBox.getChildren().add(answerBorderPane);
        }

    }

    @Override
    protected boolean isButtonSelected(int index) {
        return answerCheckBoxes.get(index).isSelected();
    }
}
