package algat.model.parser.special.rules.block;

import algat.model.lesson.block.BlockContent;
import algat.model.lesson.block.interactive.InteractiveWrapper;
import algat.model.lesson.block.paragraph.Paragraph;
import algat.model.parser.style.rules.StyleRule;

import java.util.List;

public class InteractiveRule extends BlockContentRule {
    @Override
    public BlockContent assembleBlockContent(String headToBeParsed, String parameter, List<StyleRule> activeStyles) {
        Paragraph captionPar = new Paragraph(headToBeParsed, activeStyles);
        return new InteractiveWrapper(parameter, captionPar);
    }

    @Override
    public String getUniqueId() {
        return "interactive";
    }

    @Override
    public boolean allowsHeadStyling() {
        return true;
    }
}
