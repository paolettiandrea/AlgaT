package algat.model;

/**
 * Object wrapping the data defining a Lesson
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
    private String lessonResourceName;

    /**
     * The name of the lesson
     */
    private String name;

    public Lesson(String name, String lessonResourceName) {
        this.name = name;
        this.lessonResourceName = lessonResourceName;
    }

    public String getName() {
        return name;
    }
}
