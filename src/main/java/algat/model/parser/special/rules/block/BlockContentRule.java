package algat.model.parser.special.rules.block;

import algat.model.lesson.block.BlockContent;
import algat.model.parser.special.rules.SpecialRule;
import algat.model.parser.style.rules.StyleRule;

import java.util.List;

public abstract class BlockContentRule extends SpecialRule {
    public abstract BlockContent assembleBlockContent(String headToBeParsed, String parameter, List<StyleRule> activeStyles);

}
