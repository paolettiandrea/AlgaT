package algat.model.parser.special;

import algat.model.lesson.block.BlockContent;
import algat.model.lesson.block.paragraph.ParagraphContent;
import algat.model.parser.special.rules.block.BlockContentRule;
import algat.model.parser.special.rules.block.ImageRule;
import algat.model.parser.special.rules.block.InteractiveRule;
import algat.model.parser.special.rules.paragraph.HyperlinkRule;
import algat.model.parser.special.rules.paragraph.ParagraphContentRule;
import algat.model.parser.style.StyleParser;
import algat.model.parser.style.rules.StyleRule;

import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Object meant to be used statically to parse special blocks of text
 */
public class SpecialElementsParser {

    public static final char INLINE_STARTING_CHAR = '{';
    public static final char INLINE_TERMINATION_CHAR = '}';
    public static final char BLOCK_STARTING_CHAR = '<';
    public static final char BLOCK_TERMINATION_CHAR = '>';
    public static final char HEAD_STARTING_CHAR = '(';
    public static final char HEAD_TERMINATION_CHAR = ')';
    public static final char PARAMETER_STARTING_CHAR = '[';
    public static final char PARAMETER_TERMINATION_CHAR = ']';

    public static final List<ParagraphContentRule> POSSIBLE_INLINE_RULES = new LinkedList<>() {{
        add(new HyperlinkRule());
    }};

    public static final List<BlockContentRule> POSSIBLE_STANDALONE_RULES = new LinkedList<>() {{
        add(new ImageRule());
        add(new InteractiveRule());
    }};

    public static BlockContent parseBlockContent(String toBeParsed , List<StyleRule> activeStyles) {
        checkValidity(toBeParsed);
        String id = extractId(toBeParsed);
        for (BlockContentRule blockContentRule: POSSIBLE_STANDALONE_RULES) {
            if (blockContentRule.getUniqueId().equals(id)) {
                return blockContentRule.assembleBlockContent(extractHead(toBeParsed), extractParameter(toBeParsed), activeStyles);
            }
        }
        System.out.println("ERROR: couldn't find a possible block content with the id [" + id + "]");
        return null;
    }

    public static ParagraphContent parseParagraphContent(String toBeParsed, List<StyleRule> activeStyles) {
        checkValidity(toBeParsed);
        String id = extractId(toBeParsed);
        for (ParagraphContentRule inlineRule: POSSIBLE_INLINE_RULES) {
            if (inlineRule.getUniqueId().equals(id)) {
                return inlineRule.assembleParagraphContent(extractHead(toBeParsed), extractParameter(toBeParsed), StyleParser.combineStyleStrings(activeStyles));
            }
        }
        System.out.println("ERROR: couldn't find a possible paragraph content with the id [" + id + "]");
        return null;
    }



    private static void checkValidity (String s) {
        s = s.strip();
        if (s.charAt(0)!=INLINE_STARTING_CHAR && s.charAt(0)!=BLOCK_STARTING_CHAR) {
            System.out.println("Error: [" + s + "] is not a valid special type string");
            exit(1);
        }
        if (s.charAt(s.length()-1)!=INLINE_TERMINATION_CHAR && s.charAt(s.length()-1)!=BLOCK_TERMINATION_CHAR) {
            System.out.println("Error: [" + s + "] is not a valid special type string");
            exit(1);
        }

        // TODO improve validity check (errors for styled heads when the type head is not stylable, readable error messages..)
    }

    private static String extractHead(String s) {
        return s.substring(s.indexOf(HEAD_STARTING_CHAR)+1, s.indexOf(HEAD_TERMINATION_CHAR));
    }

    private static String extractParameter(String s) {
        int termination_index = s.indexOf(PARAMETER_TERMINATION_CHAR);
        if (termination_index==-1) {
            throw new Error("Error during the extraction of the parameter substring from string "+s+" \n" +
                    "Termination char wasn't found");
        }
        return s.substring(s.indexOf(PARAMETER_STARTING_CHAR)+1, s.indexOf(PARAMETER_TERMINATION_CHAR));
    }

    private static String extractId(String s) {
        s = s.strip();
        s = s.substring(1, s.length()-1).strip();

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == HEAD_STARTING_CHAR || s.charAt(i) == PARAMETER_STARTING_CHAR) {
                return buf.toString().strip();
            } else {
                buf.append(s.charAt(i));
            }
        }
        return buf.toString().strip();
    }

}
