package algat.model.parser.rules.type;

import javafx.scene.Node;
import javafx.scene.text.Text;

public class SimpleText extends TypeRule {

    @Override
    public Node build(String contentString, String stylingString) {
        Text text = new Text(contentString);
        text.setStyle(stylingString);
        return text;
    }
}
