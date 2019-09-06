package algat.model.lesson.block.interactive;

import algat.App;
import algat.controller.content.lessons.block.interactive.InteractiveWrapperController;
import algat.model.lesson.block.BlockContent;
import algat.model.lesson.block.paragraph.Paragraph;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class InteractiveWrapper implements BlockContent {

    String fxmlPath;
    Paragraph caption;

    public InteractiveWrapper(String fxmlPath, Paragraph caption) {
        this.fxmlPath = fxmlPath;
        this.caption = caption;
    }



    @Override
    public Node assemble() {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/fxml/interactive/InteractiveWrapper.fxml"));
        try {
            Node node = loader.load();
            InteractiveWrapperController controller = loader.getController();
            controller.loadContent(fxmlPath);
            return node;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("It was impossible to load the fxml file at [" + fxmlPath + "]");
        }
    }
}
