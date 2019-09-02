package algat.model.parser.style.rules;

public class BoldRule extends StyleRule {

    public BoldRule() {
        super("Bold", '^', '^');
    }

    @Override
    public String getStyleString() {
        return "-fx-font-weight: bold";
    }
}
