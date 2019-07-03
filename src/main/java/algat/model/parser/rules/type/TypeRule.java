package algat.model.parser.rules.type;

import javafx.scene.Node;

/**
 * A rule that defines the type of object that the text within should be represented through
 */
public abstract class TypeRule {
    public abstract Node build(String contentString, String stylingString);
}
