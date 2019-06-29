package algat.model.lesson;

import algat.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.lang.System.exit;

/**
 * Object wrapping the data defining a Lesson
 * It's meant to contain mainly text (divided in blocks and paragraphs) and custom anchor panes
 */
public class Lesson {

    /**
     * The expected extension for the files that contain data representing a Lesson
     */
    public static final String EXPECTED_FILE_EXTENSION = ".txt";

    /**
     * The string identifying the Lesson resource file in the notation ".../.../file.extension"
     * where the path is relative to the classPath root
     */
    public final String lessonResourceName;

    /**
     * The name of the lesson
     */
    private String name;

    public Block getRootBlock() {
        return rootBlock;
    }

    Block rootBlock;

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


    public String getName() {
        return name;
    }


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

                int utior = 0;
            } catch (IOException e) {
                e.printStackTrace();
                exit(1);
            }

        } else {
            System.out.println("ERROR: it was impossible to find the resource [" + lessonResourceName + "] on lesson parsing construction");
            exit(1);
        }
    }
}


