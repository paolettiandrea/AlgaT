package algat.model.lesson;

import algat.view.AppViewSettings;

import java.util.LinkedList;
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
        while (targetLevel < AppViewSettings.getMaxHeaderLevel()) {
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
                System.out.println("ERROR: a block has received a string to be parsed which first line doesn't corrispond to the expected level header");
                exit(1);
            }
        } else {
            title = "rootBlock";
        }

        String paragraphBuffer = "";
        while (scanner.hasNextLine()) {
            boolean flushBuffer = false;
            String line = scanner.nextLine().strip();
            if (line.equals("")) {
                if (paragraphBuffer.length()>0) flushBuffer = true;
            } else {
                paragraphBuffer = paragraphBuffer.concat(line + " ");
            }

            // If necessary flush the paragraphBuffer
            if (flushBuffer) {
                actualContent.add(new Paragraph(paragraphBuffer.stripTrailing()));
                paragraphBuffer="";
            }
        }

        if (paragraphBuffer.length()>0) {
            actualContent.add(new Paragraph(paragraphBuffer));
        }



        scanner.close();
    }

    private int lastOccurrenceOfHeader(String s, int level) {

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
