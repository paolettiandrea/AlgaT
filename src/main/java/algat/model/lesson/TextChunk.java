package algat.model.lesson;

/**
 * A piece of text with uniform styling
 */
public class TextChunk {
    public TextChunk(String toBeParsed) {
        textContent = toBeParsed;
    }

    /**
     * The string contained in this chunk
     */
    String textContent;

    /**
     * The string defining the styling of this chunk in fxml syntax
     */
    String stylingString;

    public String getTextContent() {
        return textContent;
    }
}
