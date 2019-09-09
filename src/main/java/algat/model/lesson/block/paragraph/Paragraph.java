package algat.model.lesson.block.paragraph;

import algat.model.lesson.block.BlockContent;
import algat.model.parser.special.SpecialElementsParser;
import algat.model.parser.style.StyleParser;
import algat.model.parser.style.rules.StyleRule;
import javafx.scene.Node;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;
import java.util.List;

/**
 * A paragraph, composed of a list of ParagraphContent.
 */
public class Paragraph implements BlockContent {

    /**
     * The list of content contained in this paragraph in the order it should be displayed in.
     */
    private LinkedList<ParagraphContent> content = new LinkedList<>();

    public Paragraph (String toBeParsed) {
        this(toBeParsed, new LinkedList<>());
    }

    public Paragraph(String toBeParsed, List<StyleRule> activeStyles) {
        this(toBeParsed, activeStyles, true);
    }

    /**
     * Builds a paragraph populating it with the information parsed from the given string.
     * @param toBeParsed The unparsed string acting as a blueprint for this paragraph.
     */
    public Paragraph(String toBeParsed, List<StyleRule> activeStyles, boolean allowSpecialElements) {

        while (toBeParsed.length()>0) {
            int firstSpecialStartIndex = toBeParsed.indexOf(SpecialElementsParser.INLINE_STARTING_CHAR);
            if (firstSpecialStartIndex==-1 || !allowSpecialElements) {
                // No more inline special content left
                parseStyledText(toBeParsed, activeStyles);
                break;
            } else {
                // There still is inline special content, process the first one and go on
                int firstSpecialTerminalIndex = toBeParsed.indexOf(SpecialElementsParser.INLINE_TERMINATION_CHAR);
                if (firstSpecialTerminalIndex != -1) {
                    //if (firstSpecialStartIndex > 0 && toBeParsed.charAt(firstSpecialStartIndex-1) != '\\')
                    String textBeforeSpecial = toBeParsed.substring(0,firstSpecialStartIndex);
                    parseStyledText(textBeforeSpecial, activeStyles);

                    String toParse = toBeParsed.substring(firstSpecialStartIndex,firstSpecialTerminalIndex+1);
                    content.add(SpecialElementsParser.parseParagraphContent(toParse, activeStyles));

                    toBeParsed = toBeParsed.substring(firstSpecialTerminalIndex+1);
                } else {
                    // TODO: error
                    System.out.println("Error: no termination char");
                }
            }
        }
    }

    /**
     * If the given string is not just white chars parses it and adds the assembled TextChunks to the content list.
     * @param s The string that needs to be parsed
     * @param activeStyles The styles that were active at the end of the previous section
     */
    private  void parseStyledText(String s, List<StyleRule> activeStyles) {
        if (s.strip().length()>0) {
            StyleParser styleParser = new StyleParser(activeStyles);
            styleParser.parseParagraph(s);
            content.addAll(styleParser.getAssembledContent());
        }
    }

    /**
     * @return The list of ParagraphContent contained in this paragraph in the order it should be displayed in.
     */
    public LinkedList<ParagraphContent> getContentList() {
        return content;
    }

    @Override
    public Node assemble() {
        TextFlow tf = new TextFlow();
        for (ParagraphContent cont: content ) {
            tf.getChildren().add(cont.assemble());
        }
        tf.getStyleClass().add("paragraph");
        return tf;
    }

}
