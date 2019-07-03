package algat.model.lesson;

import javafx.scene.Node;

public class Hyperlink extends ParagraphContent {
    String link;

    public Hyperlink(String textContent, String stylingString, String link) {
        super(textContent, stylingString);
        this.link = link;
    }

    @Override
    public Node assembleNode() {
        javafx.scene.control.Hyperlink link = new javafx.scene.control.Hyperlink(textContent);
        link.setStyle(stylingString);

        return link;

    }
}
