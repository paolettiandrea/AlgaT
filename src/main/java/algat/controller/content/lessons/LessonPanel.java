package algat.controller.content.lessons;


import algat.App;
import algat.controller.content.lessons.block.BlockDisplayer;
import algat.model.lesson.Lesson;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Controller for the ContentPanel, responsable for displaying the lessons and content in general
 */
public class LessonPanel {

    /**
     * The VBox that lays out the blocks of a Lesson
     */
    public VBox blocksVBox;

    public void initialize() {
        // No need for now
    }

    /**
     * Populates the blocksVBox with the given lesson
     * @param lesson The lesson that needs to be used to populate the panel
     */
    public void loadLesson(Lesson lesson) {
        try {
            blocksVBox.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/content/lessons/BlockDisplayer.fxml"));
            VBox blockDisplayerVBox = loader.load();
            blockDisplayerVBox.getStyleClass().add("lesson");
            BlockDisplayer blockDisplayerController = loader.getController();
            blockDisplayerController.populate(lesson.getRootBlock());
            blocksVBox.getChildren().add(blockDisplayerVBox);

            blockDisplayerVBox.getStylesheets().add(App.class.getResource("view/css/lesson.css").toExternalForm());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




