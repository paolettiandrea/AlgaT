package algat.model.parser;

import algat.App;
import algat.model.parser.rules.ItalicRule;
import algat.model.parser.rules.ParserRule;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Object meant to parse an AlgaT lesson file, using a simplified markdown syntax in order to assemble a
 */
public class Parser {

    public static final List<ParserRule> PARSER_RULES = new LinkedList<ParserRule>() {{
        add(new ItalicRule());
    }};

    public static void parse(String lessonResourceName, AnchorPane contentAnchorPane) {

        InputStream inStream = App.class.getResourceAsStream(lessonResourceName);           // Assumes that the App class stays at the root of the classpath
        if (inStream != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inStream));

                String line = br.readLine();
                while (line!=null)  {
                    if (line.startsWith("#"))
                    line = br.readLine();
                }


            } catch (IOException e) {
                e.printStackTrace();
                exit(1);
            }

        } else {
            System.out.println("ERROR: it was impossible to find the resource [" + lessonResourceName + "] on lesson parsing construction");
            exit(1);
        }
    }



}
