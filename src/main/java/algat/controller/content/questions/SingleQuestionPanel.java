package algat.controller.content.questions;

import algat.model.lesson.Question;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.Vector;

public class SingleQuestionPanel extends QuestionPanel {

    public BorderPane mainBorderPane;
    public VBox answersVBox;


    private Vector<RadioButton> answerButtons;


    @Override
    public void populate(Question question) {
        this.question = question;
        mainBorderPane.setTop(question.getQuestion().assemble());

        int totalAnswers = question.getIncorrectAnswers().size()+1;

        indexes = new Vector<>();
        for (int i = 0; i < totalAnswers; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);

        ToggleGroup toggleGroup = new ToggleGroup();
        answerButtons = new Vector<>();
        for (int i = 0; i < totalAnswers; i++) {
            BorderPane answerBorderPane = new BorderPane();

            RadioButton radioButton = new RadioButton();
            radioButton.setToggleGroup(toggleGroup);
            answerButtons.add(radioButton);
            answerBorderPane.setLeft(radioButton);

            if (indexes.get(i)==totalAnswers-1) {
                answerBorderPane.setCenter(question.getCorrectAnswers().getFirst().assemble());
            } else {
                answerBorderPane.setCenter(question.getIncorrectAnswers().get(indexes.get(i)).assemble());
            }

            answersVBox.getChildren().add(answerBorderPane);
        }
    }



    @Override
    protected boolean isButtonSelected(int index) {
        return answerButtons.get(index).isSelected();
    }
}
