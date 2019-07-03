package algat.model.parser.rules;

public abstract class ParserRule {

    protected String name;
    protected char startingChar;
    protected char terminationChar;


    public ParserRule(String name, char startingChar, char terminationChar) {
        this.name = name;
        this.startingChar = startingChar;
        this.terminationChar = terminationChar;
    }


    public String getName() {
        return name;
    }

    public char getStartingChar() {
        return startingChar;
    }

    public char getTerminationChar() {
        return terminationChar;
    }

    public boolean isSpecialChar(char c) {
        return (c==terminationChar || c==startingChar);
    }
}
