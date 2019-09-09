package algat.model.parser.special.rules.paragraph;

import algat.model.lesson.block.paragraph.ParagraphContent;
import algat.model.parser.special.rules.SpecialRule;

public abstract class ParagraphContentRule extends SpecialRule {
    public abstract ParagraphContent assembleParagraphContent(String headToBeParsed, String parameter, String contextStylingString);
}
