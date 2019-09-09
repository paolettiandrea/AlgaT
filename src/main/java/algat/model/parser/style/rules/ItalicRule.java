package algat.model.parser.style.rules;

public class ItalicRule extends StyleRule {
    public ItalicRule() {
        super("Italic", '*', '*');
    }

    @Override
    public String getStyleString() {
        return "-fx-font-style:italic";
    }

}
