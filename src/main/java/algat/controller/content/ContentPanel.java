package algat.controller.content;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;


/**
 * Controller for the panel responsible for managing and displaying the content of the app (Lessons and Quiz)
 */
public class ContentPanel {

    public AnchorPane contentPane;

    public void initialize() {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/content/welcome/WelcomePanel.fxml"));
            AnchorPane welcomePane = loader.load();
            addContentWithAnchor(welcomePane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR:failed to load the WelcomePanel");
        }
    }

    public void changeContent(Node newContent) {
        Node oldContent = contentPane.getChildren().get(0);

        addContentWithAnchor(newContent, 0.0);

        Duration translationDuration = Duration.seconds(0.5);

        TranslateTransition oldContentTranslation = new TranslateTransition(translationDuration, oldContent);
        oldContentTranslation.setFromX(0);
        oldContentTranslation.setToX(oldContent.getBoundsInParent().getWidth());

        TranslateTransition newContentTranslation = new TranslateTransition(translationDuration, newContent);
        newContentTranslation.setFromX(-oldContent.getBoundsInParent().getWidth()*1.5);
        newContentTranslation.setToX(0);

        FadeTransition oldContentFadingTransition = new FadeTransition(translationDuration.multiply(0.7), oldContent);
        oldContentFadingTransition.setFromValue(1.0);
        oldContentFadingTransition.setToValue(0.0);

        FadeTransition newContentFadingTransition = new FadeTransition(translationDuration, newContent);
        newContentFadingTransition.setFromValue(0.0);
        newContentFadingTransition.setToValue(1.0);

        ParallelTransition lateralTransition = new ParallelTransition(oldContentTranslation, newContentTranslation, oldContentFadingTransition, newContentFadingTransition);
        lateralTransition.setOnFinished(actionEvent -> {
            contentPane.getChildren().remove(oldContent);
        });

        lateralTransition.play();
    }

    private void addContentWithAnchor(Node newContent, double anchorVal) {
        contentPane.getChildren().add(newContent);
        AnchorPane.setBottomAnchor(newContent, anchorVal);
        AnchorPane.setLeftAnchor(newContent, anchorVal);
        AnchorPane.setRightAnchor(newContent, anchorVal);
        AnchorPane.setTopAnchor(newContent, anchorVal);
    }
}
