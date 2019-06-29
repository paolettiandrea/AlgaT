package algat.controller;


import algat.model.lesson.Lesson;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ContentPanel {


    public VBox blocksVBox;

    public void initialize() {
    }

    public void loadLesson(Lesson lesson) {
        try {
            blocksVBox.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/BlockDisplayer.fxml"));
            VBox blockDisplayerVBox = loader.load();
            BlockDisplayer blockDisplayerController = loader.getController();
            blockDisplayerController.populate(lesson.getRootBlock());
            blocksVBox.getChildren().add(blockDisplayerVBox);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    }




