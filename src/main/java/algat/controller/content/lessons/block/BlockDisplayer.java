package algat.controller.content.lessons.block;

import algat.model.lesson.block.Block;
import algat.model.lesson.block.BlockContent;
import algat.view.AppViewSettings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class BlockDisplayer {

    public VBox mainVBox;
    public Label blockTitleLabel;
    public VBox actualContentVBox;
    public VBox childBlocksVBox;


    public void initialize() {
        blockTitleLabel.setPadding(new Insets(AppViewSettings.contentBlockSpacing,0.0,0.0,0.0));
    }

    /**
     * Populates the displayer with the given block
     */
    public void populate (Block block) {
        clear();
        if (block.getLevel()==0) mainVBox.getChildren().remove(blockTitleLabel);
        else {
            blockTitleLabel.setText(block.getTitle());
            blockTitleLabel.setStyle("-fx-font-size: " + AppViewSettings.getHeaderFontSize(block.getLevel()));
        }
        populateActualContent(block);
        populateChildBlocks(block);
    }

    public void clear () {
        blockTitleLabel.setText("Title Label");
        actualContentVBox.getChildren().clear();
        childBlocksVBox.getChildren().clear();
    }

    private void populateActualContent(Block block) {
        for (BlockContent content: block.getActualContent()) {
            actualContentVBox.getChildren().add(content.assemble());
        }
    }

    private void populateChildBlocks(Block block) {
        for (Block childBlock: block.getChildBlocks()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/content/lessons/BlockDisplayer.fxml"));
                VBox blockDisplayer = loader.load();
                childBlocksVBox.getChildren().add(blockDisplayer);
                BlockDisplayer childController = loader.getController();
                childController.populate(childBlock);
            } catch (IOException e) {
                System.out.println("Error: there was a problem loading the BlockDisplayer fxml while populating child blocks");
                e.printStackTrace();
            }
        }
    }
}
