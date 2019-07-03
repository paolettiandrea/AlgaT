package algat.model.parser.rules.style;

public class ItalicRule extends StyleRule {
    public ItalicRule() {
        super("Italic", '*', '*');
    }

    @Override
    public String getStyleString() {
        return "-fx-font-style:italic;";
    }

}
