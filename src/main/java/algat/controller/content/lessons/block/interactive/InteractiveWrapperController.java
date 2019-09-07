package algat.controller.content.lessons.block.interactive;

import algat.App;
import algat.controller.content.lessons.block.interactive.lightindicator.LightIndicator;
import algat.model.lesson.block.interactive.InteractiveContentController;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.io.IOException;

// TODO disable step button while still executing step
// TODO disable step button if the animation is terminated

public class InteractiveWrapperController {

    public BorderPane borderPane;
    public Slider speedSlider;
    public Button resetButton;
    public Button stepButton;
    public StackPane autoButtonStackContainer;
    public StackPane customContentContainer;
    public Path controlPanelBackgroundShape;
    private LightIndicator autoIndicator;

    SequentialTransition singleStepTransition;
    ParallelTransition controlPanelColorTransition;
    private boolean autoFlag = false;
    private boolean isPlayingFlag = false;

    private final Color INDICATOR_ON_COLOR = new Color(0.5, 0.9, 0.5, 1);
    private final Color INDICATOR_OFF_COLOR = new Color(0.9, 0.5, 0.5, 1);

    private final Duration CONTROL_PANEL_FILL_CHANGE_DURATION = Duration.seconds(0.3);
    private final Color CONTROL_PANEL_STEPPING_COLOR = new Color(0.7, 0.9, 0.6, 1);
    private final Color CONTROL_PANEL_PAUSE_COLOR = new Color(0.8, 0.8, 0.5, 1);
    private final Color CONTROL_PANEL_INACTIVE_COLOR = new Color(0.7, 0.7, 0.7, 1);





    private InteractiveContentController contentController;

    public void initialize() {
        autoIndicator = new LightIndicator();
        autoIndicator.setLightColorNoAnimation(INDICATOR_OFF_COLOR);
        autoButtonStackContainer.getChildren().add(autoIndicator);
        autoIndicator.setRadius(8);

        Tooltip autoIndicatorTooltip = new Tooltip("Toggle auto stepping");
        Tooltip.install(autoIndicator, autoIndicatorTooltip);

        Tooltip stepButtonTooltip = new Tooltip("Step");
        Tooltip.install(stepButton, stepButtonTooltip);

        Tooltip resetButtonTooltip = new Tooltip("Reset");
        Tooltip.install(resetButton, resetButtonTooltip);

        controlPanelBackgroundShape.setFill(CONTROL_PANEL_INACTIVE_COLOR);
        controlPanelBackgroundShape.setStroke(CONTROL_PANEL_INACTIVE_COLOR);
    }

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

        if (autoFlag) {
            autoIndicator.setLightColor(INDICATOR_ON_COLOR);
        } else {
            autoIndicator.setLightColor(INDICATOR_OFF_COLOR);
        }

        // if not already playing start
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
            customContentContainer.getChildren().add(node);
            StackPane.setAlignment(node, Pos.CENTER);
            contentController.reset();

        } catch (IOException e) {
            e.printStackTrace();
        }

        speedSlider.setSnapToTicks(false);
        speedSlider.setMin(contentController.getMinRate());
        speedSlider.setMax(contentController.getMaxRate());
        speedSlider.setValue(1.0);

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (singleStepTransition!=null)
                singleStepTransition.setRate(newValue.doubleValue());
        });
    }


    private void initiateStep() {
        if (isPlayingFlag) throw new Error("The previous step was already playing");

        changeControlPanelColor(CONTROL_PANEL_STEPPING_COLOR);

        if (!contentController.isCompletelyEnded()) {
            Transition contentStepTransition = contentController.step();
            if (contentStepTransition == null)
                throw new Error("The step transition returned by the content controller was null");

            ParallelTransition stepTransition = new ParallelTransition(contentStepTransition);
            stepTransition.setOnFinished(event -> {
                // If it's not on auto there's no point in breaking, so stop the animation
                changeControlPanelColor(CONTROL_PANEL_PAUSE_COLOR);
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

            singleStepTransition.setRate(speedSlider.getValue());
            singleStepTransition.play();
        }
    }

    private void animationStoppingCallback() {
        isPlayingFlag = false;
        stepButton.setDisable(false);
        changeControlPanelColor(CONTROL_PANEL_INACTIVE_COLOR);

        if (contentController.isCompletelyEnded()) {
            stepButton.setDisable(true);
        }

    }

    private void changeControlPanelColor(Color newColor) {
        if (controlPanelColorTransition != null) {
            controlPanelColorTransition.stop();
        }

        StrokeTransition controlPanelStrokeTransition = new StrokeTransition(CONTROL_PANEL_FILL_CHANGE_DURATION, controlPanelBackgroundShape);
        controlPanelStrokeTransition.setToValue(newColor);

        FillTransition controlPanelFillTransition = new FillTransition(CONTROL_PANEL_FILL_CHANGE_DURATION, controlPanelBackgroundShape);
        controlPanelFillTransition.setToValue(newColor);

        controlPanelColorTransition = new ParallelTransition(controlPanelFillTransition, controlPanelStrokeTransition);
        controlPanelColorTransition.setOnFinished(event -> {
            controlPanelColorTransition = null;
        });

        controlPanelColorTransition.play();
    }
}
