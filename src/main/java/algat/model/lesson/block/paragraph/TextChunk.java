package algat.model.lesson.block.paragraph;

import javafx.scene.Node;
import javafx.scene.text.Text;

/**
 * A piece of text with uniform styling
 */
public class TextChunk extends ParagraphContent {

    public TextChunk(String textContent, String stylingString) {
        super(textContent, stylingString);
    }

    public String getTextContent() {
        return textContent;
    }

    @Override
    public Node assemble() {
        Text text = new Text(textContent);
        text.setStyle(stylingString);
        return text;
    }
}
