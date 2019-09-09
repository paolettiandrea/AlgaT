package algat.model.lesson;

import algat.App;
import algat.model.lesson.block.Block;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Object wrapping the data defining a Lesson
 * It's meant to contain mainly text (divided in blocks and paragraphs) and custom anchor panes
 */
public class Lesson {

    /**
     * The expected extension for the files that contain data representing a Lesson
     */
    public static final String EXPECTED_FILE_EXTENSION = ".lesson";
    public static final String EXPECTED_QUESTIONS_EXTENSION = ".quiz";

    /**
     * The string identifying the Lesson resource file in the notation ".../.../file.extension"
     * where the path is relative to the classPath root
     */
    public final String lessonResourceName;
    /**
     * The name of the lesson
     */
    private String name;
    /**
     * The block at the root of the block hierarchy, always having level=0.
     */
    Block rootBlock;

    LinkedList<Question> questions = new LinkedList<>();


    public Lesson(String name, String lessonResourceName) {
        this.name = name;
        this.lessonResourceName = lessonResourceName;
        if (lessonResourceName.endsWith(EXPECTED_FILE_EXTENSION)) {
            parseLessonFile();
        } else {
            System.out.println("ERROR: the lession resource file [" + lessonResourceName + "] has a different extension than expected");
            exit(1);
        }
    }


    public Block getRootBlock() {
        return rootBlock;
    }

    /**
     * Parses the file given at construction and populate this instance with the parsed data
     */
    private void parseLessonFile()
    {
        InputStream inStream = App.class.getResourceAsStream(lessonResourceName);           // Assumes that the App class stays at the root of the classpath
        if (inStream != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inStream));

                String line = br.readLine();
                String fileContent = "";
                while (line != null) {
                    fileContent += line.concat("\n");
                    line = br.readLine();
                }

                rootBlock = new Block(fileContent,0);
            } catch (IOException e) {
                e.printStackTrace();
                exit(1);
            }

            // Questions
            inStream = App.class.getResourceAsStream(lessonResourceName.replaceAll(EXPECTED_FILE_EXTENSION,EXPECTED_QUESTIONS_EXTENSION));
            if (inStream != null) {
                String rawQuestionFileText;
                try (Scanner scanner = new Scanner(inStream, "UTF-8")) {
                    scanner.useDelimiter("\\A");
                    rawQuestionFileText  = scanner.hasNext() ? scanner.next() : "";
                }
                String[] rawQuestionArray = rawQuestionFileText.split("---");
                for (String rawQuestion : rawQuestionArray) {
                    int i = 0;
                    questions.add(new Question(rawQuestion));
                }
            } // else there are no questions for this lesson

        } else {
            System.out.println("ERROR: it was impossible to find the resource [" + lessonResourceName + "] on lesson parsing construction");
            exit(1);
        }
    }


    /**
     * Gets the name of this lession.
     * @return The name of this lesson.
     */
    public String getName() {
        return name;
    }

    public LinkedList<Question> getQuestions() {
        return questions;
    }


}


