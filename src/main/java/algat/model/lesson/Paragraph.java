package algat.model.lesson;

import java.util.LinkedList;

/**
 * A paragraph, composed of textual elements
 */
public class Paragraph implements BlockContent {

    LinkedList<TextChunk> textContent = new LinkedList<>();

    public Paragraph(String toBeParsed) {
        textContent.add(new TextChunk(toBeParsed));
    }

    public LinkedList<TextChunk> getTextContent() {
        return textContent;
    }
}
