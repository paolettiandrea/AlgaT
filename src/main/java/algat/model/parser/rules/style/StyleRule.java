package algat.model.parser.rules.style;

import algat.model.parser.rules.ParserRule;

/**
 * Object that define a parser rule that associates a couple of characters (starting and termination) with a style to be applied to the text between those characters
 */
public abstract class StyleRule extends ParserRule {

    public StyleRule(String name, char startingChar, char terminationChar) {
        super(name, startingChar, terminationChar);
    }


    public abstract String getStyleString();
}
