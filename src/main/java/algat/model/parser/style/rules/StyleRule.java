package algat.model.parser.style.rules;

/**
 * Object that define a parser rule that associates a couple of characters (starting and termination) with a style to be applied to the text between those characters
 */
public abstract class StyleRule {

    /**
     * A printable name that identifies this style.
     */
    final protected String name;
    /**
     * The starting char for this style.
     */
    final protected char startingChar;
    /**
     * The termination char for this style. (It can be equal to the starting char).
     */
    final protected char terminationChar;


    public StyleRule(String name, char startingChar, char terminationChar) {
        this.name = name;
        this.startingChar = startingChar;
        this.terminationChar = terminationChar;
    }

    /**
     * @return the name of the style in a printable format
     */
    public String getName() {
        return name;
    }

    /**
     * @return the starting character for this style
     */
    public char getStartingChar() {
        return startingChar;
    }

    /**
     * @return the termination character for this style.
     */
    public char getTerminationChar() {
        return terminationChar;
    }

    /**
     * Check if a given char is special (meaning is starting or termination for this style).
     * @param c The char that needs to be checked.
     * @return true if the given char is special for this rule, false otherwise.
     */
    public boolean isSpecialChar(char c) {
        return (c==terminationChar || c==startingChar);
    }

    /**
     * @return The styling string that allow the application of this style to a JavaFX node through setStyle
     */
    public abstract String getStyleString();


}
