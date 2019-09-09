package algat.controller.content.questions;

import algat.model.lesson.Question;
import javafx.scene.layout.AnchorPane;

import java.util.Vector;

/**
 * Abstract base for different kinds of question panels
 */
public abstract class QuestionPanel {

    protected Vector<Integer> indexes;
    protected Question question;

    public abstract void populate(Question question);

    public AnchorPane mainPane;

    /**
     * Check if the given answer is correct
     * @return true if the answer is correct
     */
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

    /**
     * Query if the button corresponding to the answer of the given index is selected or not
     */
    protected abstract boolean isButtonSelected(int index);

}
