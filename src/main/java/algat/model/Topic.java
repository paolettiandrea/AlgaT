package algat.model;

import algat.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * A subject containing a few Lessons pertaining that subject
 */
public class Topic {

    /**
     * The name of the topic
     */
    private String topicName;
    /**
     * The string identifying the Topic directory in the notation ".../.../topicDir"
     * where the path is relative to the classPath root
     */
    private String topicDirName;
    /**
     * The list of lessons pertaining this topic, in the order they need to be displayed in the sidebar
     */
    private List<Lesson> lessonList = new ArrayList<>();

    /**
     * Constructor that loads the topic data from a given folder in a sort of distribution-agnostic way.
     *
     * @param topicName         The name of the topic
     * @param topicResourceName The path tho the topic directory in the notation ".../.../topicDir"
     */
    public Topic(String topicName, String topicResourceName) throws IOException {
        this.topicName = topicName;
        this.topicDirName = topicResourceName;

        // For every topic in the order file create a new topic
        InputStream inStream = App.class.getResourceAsStream(topicResourceName + "/" + AppContent.ORDER_FILE_EXPECTED_NAME);           // Assumes that the App class stays at the root of the classpath
        if (inStream != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
            String lessonName = br.readLine();
            while (lessonName != null) {
                System.out.println(lessonName);
                lessonList.add(new Lesson(lessonName, topicResourceName + "/" + lessonName + Lesson.EXPECTED_FILE_EXTENSION));
                lessonName = br.readLine();
            }
            br.close();
        } else {
            System.out.println("ERROR: it was impossible to find the resource [" + topicResourceName + "] during Topic construction");
            exit(1);
        }
    }

    public String getTopicName() {
        return topicName;
    }

    public List<Lesson> getLessonList() {
        return lessonList;
    }
}
