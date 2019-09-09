package algat.model.lesson.block;

import algat.model.lesson.block.paragraph.Paragraph;
import algat.model.parser.special.SpecialElementsParser;
import algat.model.parser.style.rules.StyleRule;
import algat.view.AppViewSettings;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * A part of a lesson defined by a header.
 * Is composed of an ordered list of content (which can be Paragraphs or interactive custom javafx panels)
 */
public class Block {

    /**
     * The character used to define headers in lesson files
     * @implNote It's typed as String because it's handier to manipulate on the fly, but it should absolutely be only one character
     */
    static final String HEADER_CHAR = "#";

    private int level;
    private String title;
    private LinkedList<BlockContent> actualContent = new LinkedList<>();
    private LinkedList<Block> childBlocks = new LinkedList<>();


    public Block(String toBeParsed, int level) {

        this.level = level;

        int targetLevel = level+1;
        while (targetLevel <= AppViewSettings.getMaxHeaderLevel()) {
            int lastOccurrence = lastOccurrenceOfHeader(toBeParsed, targetLevel);
            while (lastOccurrence!=-1) {
                childBlocks.addFirst(new  Block(toBeParsed.substring(lastOccurrence), targetLevel));
                toBeParsed = toBeParsed.substring(0,lastOccurrence);
                lastOccurrence = lastOccurrenceOfHeader(toBeParsed, targetLevel);
            }
            targetLevel++;
        }

        // At this point only the actual content (nested blocks excluded) of this block should be left

        Scanner scanner = new Scanner(toBeParsed.strip());
        if (level>0) {
            title = scanner.nextLine();
            if (title.startsWith(HEADER_CHAR.repeat(level))) {
                title = title.substring(level).strip();
            } else {
                System.out.println("ERROR: a block has received a string to be parsed which first line doesn't correspond to the expected level header");
                exit(1);
            }
        } else {
            title = "rootBlock";
        }

        String paragraphBuffer = "";
        List<StyleRule> activeStyles = new LinkedList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().strip();
            if (line.equals("")) {
                if (paragraphBuffer.length()>0) {
                    flush(paragraphBuffer, activeStyles);
                    paragraphBuffer = "";
                }
            } else {
                paragraphBuffer = paragraphBuffer.concat(line + " ");
            }
        }
        if (paragraphBuffer.length()>0) {
            flush(paragraphBuffer, activeStyles);
        }

        scanner.close();
    }

    private void flush(String buffer, List<StyleRule> activeStyles) {
        buffer = buffer.strip();
        if (buffer.charAt(0) == SpecialElementsParser.BLOCK_STARTING_CHAR) {
            if (buffer.charAt(buffer.length()-1) == SpecialElementsParser.BLOCK_TERMINATION_CHAR) {
                actualContent.add(SpecialElementsParser.parseBlockContent(buffer, activeStyles));
            } else {
                // TODO: error, the received string seem to be an opened special specialElements, but couldn't find termination char
            }
        } else {
            actualContent.add(new Paragraph(buffer, activeStyles));
        }
    }

    private int lastOccurrenceOfHeader(String s, int level) {
        // FIXME right now the escaped # is ignored, but is also printed in the block content, needs slight refactoring
        int foundIndex;
        do {
            foundIndex = s.lastIndexOf(HEADER_CHAR.repeat(level));
            // If the header symbols are escaped don't consider this one
            if (foundIndex>0) {
                if (s.charAt(foundIndex-1)=='\\') {
                    s = s.substring(0, foundIndex-1);
                    foundIndex = -2;
                } else if (s.charAt(foundIndex-1)== '#') {
                    while (s.charAt(foundIndex)=='#') {
                        foundIndex--;
                    }
                    s = s.substring(0, foundIndex);
                    foundIndex = -2;
                }
            }
        } while (foundIndex==-2);
        return foundIndex;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }

    public LinkedList<BlockContent> getActualContent() {
        return actualContent;
    }

    public LinkedList<Block> getChildBlocks() {
        return childBlocks;
    }
}
