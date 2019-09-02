package algat.model.lesson;

import algat.model.lesson.block.paragraph.Paragraph;

import java.util.LinkedList;

public class Question {

    public static final String CORRECT_ANSWER_STRING = "[o]";
    public static final String WRONG_ANSWER_STRING = "[x]";     // It needs to have the same lenght of the CORRECT_ANSWER_STRING

    private Paragraph question;
    private LinkedList<Paragraph> correctAnswers = new LinkedList<>();
    private LinkedList<Paragraph> incorrectAnswers = new LinkedList<>();

    public Question(String toBeParsed) {
        toBeParsed = toBeParsed.trim();


        int firstAnswerOccurrence = Math.min(
                toBeParsed.indexOf(CORRECT_ANSWER_STRING), toBeParsed.indexOf(WRONG_ANSWER_STRING));

        if (firstAnswerOccurrence != -1) {
            question = new Paragraph(toBeParsed.substring(0, firstAnswerOccurrence));
            String rawAnswers = toBeParsed.substring(firstAnswerOccurrence);

            // Strip and parse the first answer until empty
            while (!rawAnswers.isBlank()) {
                rawAnswers = rawAnswers.strip();

                int lastHeadOccurrence = Math.max(
                        rawAnswers.lastIndexOf(CORRECT_ANSWER_STRING), rawAnswers.lastIndexOf(WRONG_ANSWER_STRING));

                String lastHead = rawAnswers.substring(lastHeadOccurrence, lastHeadOccurrence+CORRECT_ANSWER_STRING.length());
                String lastAnswerHeadless = rawAnswers.substring(lastHeadOccurrence+CORRECT_ANSWER_STRING.length());
                Paragraph lastAnswer = new Paragraph(lastAnswerHeadless);
                rawAnswers = rawAnswers.substring(0,lastHeadOccurrence);

                if (lastHead.equals(CORRECT_ANSWER_STRING)) {
                    correctAnswers.add(lastAnswer);
                } else if (lastHead.equals(WRONG_ANSWER_STRING)) {
                    incorrectAnswers.add(lastAnswer);
                } else {
                    throw  new Error("QUESTION PARSING ERROR: unrecognised rawAnswers head: [" + lastHead + "]");
                }
            }
        } else {
            throw new Error("QUESTION PARSING ERROR: the raw answer text seem to lack at least one correct and one incorrect answer");
        }
    }


    public Paragraph getQuestion() {
        return question;
    }

    public LinkedList<Paragraph> getCorrectAnswers() {
        return correctAnswers;
    }

    public LinkedList<Paragraph> getIncorrectAnswers() {
        return incorrectAnswers;
    }
}
