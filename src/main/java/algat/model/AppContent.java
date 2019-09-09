
package algat.model;


import algat.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * Class that contains all the content of the AlgaT application
 */
public class AppContent {

    /**
     * The name (complete of extension) that is expected for the files defining the order of topics and lessions
     */
    static final String ORDER_FILE_EXPECTED_NAME = "order.algat";
    /**
     * The path (relative to the content folder) to the folder containing the topics
     */
    static final String TOPICS_REL_PATH = "topics";

    /**
     * The name of the content directory as readable for getResource methods
     */
    private String contentDirName;
    /**
     * The list of Topics in the order they need to be displayed in the sidebar
     */
    private List<Topic> topicList = new ArrayList<>();


    /**
     * Constructor that loads the content in a sort of distribution-agnostic way.
     *
     * @param contentPath The path where is expected to be found the content directory in the format readable for getResource methods
     * @throws IOException When the loading of resources failed
     */
    public AppContent(String contentPath) throws IOException {
        this.contentDirName = contentPath;

        InputStream topicOrderInStream = App.class.getResourceAsStream(contentPath + "/" + TOPICS_REL_PATH + "/" + ORDER_FILE_EXPECTED_NAME);           // Assumes that the App class stays at the root of the classpath
        if (topicOrderInStream != null) {
            // Each line of this order file should reference a topic folder with the same name
            // Try to access it and read its order file
            BufferedReader br = new BufferedReader(new InputStreamReader(topicOrderInStream));
            String topicFolderName = br.readLine();
            while (topicFolderName != null) {
                topicList.add(new Topic(topicFolderName, contentPath + "/" + TOPICS_REL_PATH + "/" + topicFolderName));
                topicFolderName = br.readLine();
            }
            br.close();
        } else {
            System.out.println("ERROR: it was impossible to find the resource [" + contentPath + "/" + TOPICS_REL_PATH + "/" + ORDER_FILE_EXPECTED_NAME + "] during data AppContent construction");
            exit(1);
        }


    }


    public List<Topic> getTopicList() {
        return topicList;
    }
}
