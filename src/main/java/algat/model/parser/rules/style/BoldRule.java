package algat.model.parser.rules.style;

public class BoldRule extends StyleRule {

    public BoldRule() {
        super("Bold", '^', '^');
    }

    @Override
    public String getStyleString() {
        return "-fx-font-weight: bold";
    }
}
