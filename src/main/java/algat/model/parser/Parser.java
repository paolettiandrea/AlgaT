package algat.model.parser;

import algat.model.lesson.ParagraphContent;
import algat.model.lesson.TextChunk;
import algat.model.parser.rules.style.BoldRule;
import algat.model.parser.rules.style.ItalicRule;
import algat.model.parser.rules.style.StyleRule;

import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Object meant to parse an AlgaT lesson file, using a simplified markdown syntax in order to assemble a paragraph composed of textual elements.
 */
public class Parser {

    /**
     * A set of the type of styles active during the parsing
     */
    List<StyleRule> activeRules = new LinkedList<>();
    /**
     * The builder where the uniform string is buffered until flushed
     */
    StringBuilder chunkBufferBuilder = new StringBuilder();
    private LinkedList<ParagraphContent> assembledContent = new LinkedList<>();

    public static final List<StyleRule> PARSER_RULES = new LinkedList<StyleRule>() {{
        add(new ItalicRule());
        add(new BoldRule());
    }};

    public void parse(String paragraphText) {
        for (int i = 0; i < paragraphText.length(); i++) {
            boolean isSpecialChar = false;
            for (StyleRule rule : PARSER_RULES) {
                if (rule.isSpecialChar(paragraphText.charAt(i))) {
                    if (i>0 && paragraphText.charAt(i-1)!='\\') {
                        // Found a non escaped special char
                        processStyleChar(paragraphText.charAt(i), rule);
                        isSpecialChar = true;
                        break;
                    }
                }
            }
            if (!isSpecialChar) {
                chunkBufferBuilder.append(paragraphText.charAt(i));
            }
        }
        flushBuffer();
    }

    public boolean isStyleAlreadyActive(StyleRule rule) {
        for (StyleRule activeRules: activeRules) {
            if (rule.getClass()==activeRules.getClass()) {
                return true;
            }
        }
        return false;
    }

    private void addStyle(StyleRule rule) {
        activeRules.add(rule);
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
            StringBuilder styleStringBuilder = new StringBuilder();
            for (StyleRule rule: activeRules) {
                styleStringBuilder.append(rule.getStyleString()).append(" ");
            }

            ParagraphContent content = new TextChunk(chunkBufferBuilder.toString(), styleStringBuilder.toString());
            assembledContent.add(content);
            chunkBufferBuilder.delete(0, chunkBufferBuilder.length());      // Clear the buffer
        }
    }

    public LinkedList<ParagraphContent> getAssembledContent() {
        return assembledContent;
    }
}