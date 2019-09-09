package algat.model.parser.style;

import algat.model.lesson.block.paragraph.ParagraphContent;
import algat.model.lesson.block.paragraph.TextChunk;
import algat.model.parser.style.rules.BoldRule;
import algat.model.parser.style.rules.ItalicRule;
import algat.model.parser.style.rules.StyleRule;

import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Object meant to parse a styled string of simple text
 */
public class StyleParser {

    /**
     * Builds a StyleParser with no active style rules
     */
    public StyleParser() {
        this.activeRules = new LinkedList<>();
    }

    /**
     * Builds a StyleParser with an existing list of active rules to work with. Note that the parser might modify the given list.
     * This constructor allows the use of multiple StyleParsers in sequence sharing the same rule list
     * (therefore giving the next parser contextual information about the end-state styling of the parser before.
     * @param activeRules A list of active rules that the parser should use as initial state and elaborate (possibly modifying it) during the parsing.
     */
    public StyleParser(List<StyleRule> activeRules) {
        this.activeRules = activeRules;
    }

    public static String combineStyleStrings(List<StyleRule> rules) {
        StringBuilder sb = new StringBuilder();
        for (StyleRule rule: rules ) {
            sb.append(rule.getStyleString()).append("; ");
        }
        return sb.toString();
    }

    /**
     * A set of the specialElements of styles active during the parsing
     */
    List<StyleRule> activeRules;
    /**
     * The builder where the uniform string is buffered until flushed
     */
    StringBuilder chunkBufferBuilder = new StringBuilder();
    /**
     * The list of content that the parser builds during the parsing
     */
    private LinkedList<ParagraphContent> assembledContent = new LinkedList<>();

    public static final List<StyleRule> PARSER_RULES = new LinkedList<>() {{
        add(new ItalicRule());
        add(new BoldRule());
    }};



    public void parseParagraph(String paragraphText) {
        for (int i = 0; i < paragraphText.length(); i++) {
            // For each char in the paragraph string:
            //      - check
            boolean isSpecialChar = false;
            for (StyleRule rule : PARSER_RULES) {
                if (rule.isSpecialChar(paragraphText.charAt(i))) {
                    if (i>0 && paragraphText.charAt(i-1)!='\\') {
                        // Found a non escaped special char
                        processStyleChar(paragraphText.charAt(i), rule);
                        isSpecialChar = true;
                        break;
                    } else {
                        // The special was escaped so remove the escape from the buffer before continuing
                        if (chunkBufferBuilder.length()>0)
                            chunkBufferBuilder.deleteCharAt(chunkBufferBuilder.length()-1);
                    }
                }
            }
            if (!isSpecialChar) {
                chunkBufferBuilder.append(paragraphText.charAt(i));
            }
        }
        flushBuffer();
    }

    /**
     * Check if a rule of the given type is already present in this object's activeRules list
     * @param rule The rule which type needs to be checked
     * @return true if a rule of the given type is already present, false otherwise
     */
    private boolean isStyleAlreadyActive(StyleRule rule) {
        for (StyleRule activeRules: activeRules) {
            if (rule.getClass()==activeRules.getClass()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Allows the retrieval of the assembled content list after parsing
     */
    public LinkedList<ParagraphContent> getAssembledContent() {
        return assembledContent;
    }

    private void addStyle(StyleRule rule) {
        if (!isStyleAlreadyActive(rule))
            activeRules.add(rule);
        else {
            throw new Error("Tried to add a style [" + rule.getName() + "] which was already present.");
        }
    }

    private void removeStyle(StyleRule ruleClass) {
        for (int i = 0; i < activeRules.size(); i++) {
            if (ruleClass.getClass()==activeRules.get(i).getClass()) {
                activeRules.remove(i);
                break;
            }
        }
    }

    private void processStyleChar(char c, StyleRule rule) {
        if (c==rule.getStartingChar()) {
            if (!isStyleAlreadyActive(rule)) {
                // The style wasn't already active, so the buffer is flushed and the style is added to the list
                flushBuffer();
                addStyle(rule);
            } else {
                if (rule.getTerminationChar()==rule.getStartingChar()) {
                    // The rule was already opened, but this StyleRule has the same char for termination,
                    // so it's assumed that the style needs to be removed
                    flushBuffer();
                    removeStyle(rule);
                } else {
                    System.out.println("PARSING ERROR: the starting char of [" + rule.getName() + "] was found, but the style was already opened");
                    exit(1);
                }
            }
        } else if (c == rule.getTerminationChar()) {
            if (isStyleAlreadyActive(rule)) {
                flushBuffer();
                removeStyle(rule);
            } else {
                System.out.println("PARSING ERROR: found a termination char for the style [" + rule.getName() + "], but that style wasn't active");
                exit(1);
            }
        }
    }

    private void flushBuffer() {
        // Build the style string
        if (chunkBufferBuilder.length()>0) {
            ParagraphContent content = new TextChunk(chunkBufferBuilder.toString(), combineStyleStrings(activeRules));
            assembledContent.add(content);
            chunkBufferBuilder.delete(0, chunkBufferBuilder.length());      // Clear the buffer
        }
    }

}