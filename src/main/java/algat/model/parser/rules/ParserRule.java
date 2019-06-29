package algat.model.parser.rules;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.util.List;

/**
 * Object that define a parser rule
 */
public interface ParserRule {
    String getStartingString();
    String getTerminationString();
    boolean isTerminal();

    void applyStyle(Node node);
    void assembleContent(String string, List<ParserRule> ruleStack, TextFlow lastTextFlow, VBox contentVBox);
}
