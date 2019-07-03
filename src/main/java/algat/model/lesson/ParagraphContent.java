package algat.model.lesson;

import javafx.scene.Node;

/**
 * An object that models a piece of content meant to be shown in a paragraph.
 */
public abstract class  ParagraphContent {
    public ParagraphContent(String textContent, String stylingString) {
        this.stylingString = stylingString;
        this.textContent = textContent;
    }

    /**
     * The string defining the styling of the text shown by this ParagraphContent object.
     * It could be for example the head of an Hyperlink or simply the text content of a TextChunk
     */
    String stylingString;
    /**
     * The text meant to be shown in the paragraph.
     * For simple TextChunk that's basically it, for Hyperlinks it contains the link head.
     */
    String textContent;

    public abstract Node assembleNode();
}
