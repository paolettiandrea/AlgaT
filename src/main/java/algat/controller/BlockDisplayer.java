package algat.controller;

import algat.model.lesson.Block;
import algat.model.lesson.BlockContent;
import algat.model.lesson.Paragraph;
import algat.model.lesson.TextChunk;
import algat.view.AppViewSettings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;

public class BlockDisplayer {

    public VBox mainVBox;
    public Label blockTitleLabel;
    public VBox actualContentVBox;
    public VBox childBlocksVBox;


    public void initialize() {
        blockTitleLabel.setPadding(new Insets(AppViewSettings.contentBlockSpacing,0.0,0.0,0.0));
    }

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
            if (content.getClass()== Paragraph.class) {
                Paragraph paragraphContent = (Paragraph)content;
                TextFlow paragraphTextFlow = new TextFlow();

                for (TextChunk textChunk: paragraphContent.getTextContent()) {
                    paragraphTextFlow.getChildren().add(new Text(textChunk.getTextContent()));

                }
                actualContentVBox.getChildren().add(paragraphTextFlow);
            }
        }
    }

    private void populateChildBlocks(Block block) {
        for (Block childBlock: block.getChildBlocks()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/BlockDisplayer.fxml"));
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
