package algat.model.lesson.block;

import algat.App;
import algat.model.lesson.block.paragraph.Paragraph;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class Image implements BlockContent {

    static final String styleClass = "image-displayer";

    String imagePath;
    Paragraph caption;


    public Image(String imagePath, Paragraph caption) {
        this.imagePath = imagePath;
        this.caption = caption;
    }

    @Override
    public Node assemble() {

        // FIXME image not centered
        // TODO make a custom javafx object (image + caption) which should also help with the centering problem

        InputStream is =  App.class.getResourceAsStream(imagePath);
        if (is == null) {
            throw new Error("Trying to load an image at path [" + imagePath +" returned a null stream");
        }

        ImageView view = new ImageView(new javafx.scene.image.Image(is));
        view.setPreserveRatio(true);

        BorderPane bp = new BorderPane();
        bp.setCenter(view);

        ScrollPane scrollPane = new ScrollPane(bp);

        VBox vbox = new VBox();

        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(caption.assemble());
        vbox.getStyleClass().add(styleClass);

        return vbox;
    }
}

