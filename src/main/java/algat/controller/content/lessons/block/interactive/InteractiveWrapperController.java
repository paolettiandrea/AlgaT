package algat.controller.content.lessons.block.interactive;

import algat.App;
import algat.model.lesson.block.interactive.InteractiveContentController;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

// TODO disable step button while still executing step
// TODO disable step button if the animation is terminated

public class InteractiveWrapperController {

    public BorderPane borderPane;
    public Slider speedSlider;
    public Button resetButton;
    public Button stepButton;
    public Button autoButton;

    SequentialTransition singleStepTransition;
    private boolean autoFlag = false;
    private boolean isPlayingFlag = false;


    private InteractiveContentController contentController;


    @FXML
    public void step() {
        System.out.println("step");
        initiateStep();

    }

    @FXML
    public void reset() {
        System.out.println("reset");

        if (isPlayingFlag || autoFlag) {
            singleStepTransition.stop();
            animationStoppingCallback();
        }

        if (contentController.isCompletelyEnded()) {
            stepButton.setDisable(false);
        }

        contentController.reset();

        if (autoFlag) initiateStep();


    }

    @FXML
    public void auto()
    {
        autoFlag = !autoFlag;
        if (!isPlayingFlag && autoFlag) {
            initiateStep();
        }
    }


    /**
     * Loads a fxml containing some custom content for this stepper wrapper
     * The fxml is expected to have a controller inheriting from a base stepper class
     * @param fxmlPath The path to the fxml
     */
    public void loadContent(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxmlPath));
        try {
            Node node = loader.load();
            contentController = loader.getController();
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            borderPane.setCenter(node);

            contentController.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initiateStep() {
        if (isPlayingFlag) throw new Error("The previous step was already playing");

        if (!contentController.isCompletelyEnded()) {
            Transition contentStepTransition = contentController.step();
            if (contentStepTransition == null)
                throw new Error("The step transition returned by the content controller was null");

            ParallelTransition stepTransition = new ParallelTransition(contentStepTransition);
            stepTransition.setOnFinished(event -> {
                // If it's not on auto there's no point in breaking, so stop the animation
                if (!autoFlag) {
                    singleStepTransition.stop();
                    animationStoppingCallback();
                }
            });

            PauseTransition breakTransition = new PauseTransition(contentController.getBreakDuration());

            singleStepTransition = new SequentialTransition(stepTransition, breakTransition);
            singleStepTransition.setOnFinished(event -> {
                if (autoFlag && !contentController.isCompletelyEnded()) {
                    isPlayingFlag = false;
                    initiateStep();
                } else {
                    // No autoFlag and the animation is at the end
                    animationStoppingCallback();
                }
            });

            stepButton.setDisable(true);

            singleStepTransition.setRate(getSpeedSliderMapped(contentController.getMinRate(), contentController.getMaxRate()));
            singleStepTransition.play();
        }
    }

    private void animationStoppingCallback() {
        isPlayingFlag = false;
        stepButton.setDisable(false);

        if (contentController.isCompletelyEnded()) {
            stepButton.setDisable(true);

        }

}

    private double getSpeedSliderMapped(double min, double max) {
        if (max > min) {
            return min + speedSlider.getValue()/100*(max-min);
        } else {
            throw new Error ("Min:[" + min + "] is greater than max:[" + max + "]");
        }
    }
}
