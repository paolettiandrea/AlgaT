package algat.view;

/**
 * Class acting (maybe momentarily) as a bundle of graphical and structural settings for the app
 */
public class AppViewSettings {

    /**
     * Array of the wanted sizes for headers while rendering lessons. At index 0 the size for lvl 1, index 1 for lvl 2 and so forth.
     * @implNote This array also defines implicitly the maximum header level allowed with it's length.
     */
    public static final int[] headerSizes = new int[] {27, 24, 21, 18, 16, 14};
    /**
     * The spacing between every block in the content pane
     */
    public static final int contentBlockSpacing = 10;
    /**
     * The indentation of the lesson buttons in the side menu
     */
    public static final int menuLessonIndentation = 10;
    /**
     * The spacing between every paragraph
     */
    public static final int contentParagraphSpacing = 5;

    //public static final int[] headerTopSpacings = new int[] {5, 3, 21, 18};


    /**
     * Gets the max header level allowed for lesson files
     * @return The level of the maximum header level
     */
    public static int getMaxHeaderLevel() { return headerSizes.length; }

    /**
     * Return the wanted font size for a given header level
     * @param level The (valid) header level
     * @return The font size for the given header level
     */
    public static int getHeaderFontSize(int level) {
        if (level>0 && level<=headerSizes.length)
            return headerSizes[level-1];
        else {
            System.out.println("Error: requested a fontsize for a header level out of bounds: [" + level + "]");
            return -1;
        }
    }
}
