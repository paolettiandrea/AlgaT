package algat.model.parser.special.rules.paragraph;

import algat.model.lesson.block.paragraph.Hyperlink;
import algat.model.lesson.block.paragraph.ParagraphContent;


public class HyperlinkRule extends ParagraphContentRule {
    @Override
    public String getUniqueId() {
        return "link";
    }

    @Override
    public boolean allowsHeadStyling() {
        return false;
    }

    @Override
    public ParagraphContent assembleParagraphContent(String headToBeParsed, String parameter, String contextStylingString) {
        Hyperlink hyperlink = new Hyperlink(headToBeParsed, contextStylingString , parameter);
        return hyperlink;
    }
}
