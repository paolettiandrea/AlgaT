package algat.model.lesson;

import algat.model.parser.Parser;

import java.util.LinkedList;

/**
 * A paragraph, composed of textual elements
 */
public class Paragraph implements BlockContent {

    private LinkedList<ParagraphContent> textContent;

    public Paragraph(String toBeParsed) {
        Parser parser = new Parser();
        parser.parse(toBeParsed);
        textContent = parser.getAssembledContent();
    }

    public LinkedList<ParagraphContent> getContentList() {
        return textContent;
    }
}
