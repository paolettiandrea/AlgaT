package algat.controller.content.questions;

import algat.model.lesson.Question;
import javafx.scene.layout.AnchorPane;

import java.util.Vector;

public abstract class QuestionPanel {

    protected Vector<Integer> indexes;
    protected Question question;

    public abstract void populate(Question question);

    public AnchorPane mainPane;

    public boolean check() {
        for (int i = 0; i < indexes.size(); i++) {
            if (isButtonSelected(i)) {
                if (indexes.get(i)<question.getIncorrectAnswers().size())  return false;
            } else {
                if (indexes.get(i)>=question.getIncorrectAnswers().size()) return false;
            }
        }
        return true;
    }

    protected abstract boolean isButtonSelected(int index);

}
