package algat.model.lesson.block.paragraph;

import algat.App;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;

public class Hyperlink extends ParagraphContent {
    String link;

    public Hyperlink(String textContent, String stylingString, String link) {
        super(textContent, stylingString);

        link = link.strip();
        if (!link.startsWith("https://") && !link.startsWith("http://")) {
            link = "http://".concat(link);
        }
        this.link = link;

    }

    @Override
    public Node assemble() {
        Text linkText = new Text(textContent);
        linkText.setStyle(stylingString);
        linkText.getStyleClass().add("hyperlink");

        Tooltip tooltip = new Tooltip(link);
        Tooltip.install(linkText, tooltip);

        linkText.setOnMouseClicked(event -> App.getServices().showDocument(link));

        return linkText;

    }
}
