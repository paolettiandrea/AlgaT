package algat.model.parser.rules;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;

import java.util.List;

public class ItalicRule implements ParserRule {
    @Override
    public String getStartingString() {
        return "*";
    }

    @Override
    public String getTerminationString() {
        return "*";
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public void applyStyle(Node node) {
        node.setStyle("-fx-font-weight: italic");
    }

    @Override
    public void assembleContent(String string, List<ParserRule> ruleStack, TextFlow lastTextFlow, VBox contentVBox) {

    }
}
