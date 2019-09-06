package algat.controller;

import algat.controller.content.ContentPanel;
import algat.model.AppContent;
import algat.model.Topic;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Main controller for the application
 */
public class Main {

    public static final Duration SIDEBAR_ANIMATION_DURATION = new Duration(300);
    public static final Duration FLIP_BUTTON_MOVE_ANIMATION_DURATION = new Duration(300);


    public Polygon expandButtonShape;
    public Polygon collapseButtonShape;
    public Rectangle fullscreenButton;

    /**
     * An FX-injected reference to the VBox meant to contain the navigation menu
     */
    @FXML
    private VBox topicNavigationVBox;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private ContentPanel contentPanelController;

    private Stage primaryStage;


    /**
     * Method called by FXML when the corresponding FXML is loaded
     */
    public void initialize() {
        mainSplitPane.setDividerPositions(0);

        // Set polgon button points
        makeEquilateral(expandButtonShape, 3, 8);
        makeEquilateral(collapseButtonShape, 3, 8);

        collapseButtonShape.setTranslateX(collapseButtonShape.getBoundsInParent().getWidth()*2);

        Tooltip collapseTooltip = new Tooltip("Collapse menu");
        Tooltip.install(collapseButtonShape, collapseTooltip);

        Tooltip expandTooltip = new Tooltip("Expand menu");
        Tooltip.install(expandButtonShape, expandTooltip);

        Tooltip fullscreenTooltip = new Tooltip("Toogle fullscreen");
        Tooltip.install(fullscreenButton, fullscreenTooltip);

    }

    /**
     * Populate the application with the content
     *
     * @param data The content that the application needs to display
     */
    public void populate(AppContent data, Stage primaryStage) {

        this.primaryStage = primaryStage;

        for (Topic topic : data.getTopicList()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/TopicMenuField.fxml"));
                VBox topicMenuField = loader.load();
                TopicMenuField topicMenuFieldController = loader.getController();
                topicMenuFieldController.populate(topic, contentPanelController);
                topicNavigationVBox.getChildren().add(topicMenuField);


            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR:failed to load the TopicMenuField");
            }
        }
    }

    @FXML
    public void expandSidebar() {
        double val = topicNavigationVBox.getWidth()/mainSplitPane.getWidth();

        KeyValue keyValue = new KeyValue(mainSplitPane.getDividers().get(0).positionProperty(), val);
        Timeline dividerSlidingTimeline = new Timeline(new KeyFrame(SIDEBAR_ANIMATION_DURATION, keyValue));

        double buttonDistance =  expandButtonShape.getBoundsInLocal().getWidth()
                + BorderPane.getMargin(expandButtonShape).getLeft()
                + BorderPane.getMargin(collapseButtonShape).getRight();

        TranslateTransition ttCollapseButt = new TranslateTransition(FLIP_BUTTON_MOVE_ANIMATION_DURATION,collapseButtonShape);
        ttCollapseButt.setFromX(buttonDistance);
        ttCollapseButt.setToX(0);

        RotateTransition rtCollapseButt = new RotateTransition(SIDEBAR_ANIMATION_DURATION, collapseButtonShape);
        rtCollapseButt.setFromAngle(0);
        rtCollapseButt.setToAngle(-180);

        TranslateTransition ttExpandButt = new TranslateTransition(FLIP_BUTTON_MOVE_ANIMATION_DURATION,expandButtonShape);
        ttExpandButt.setFromX(0);
        ttExpandButt.setToX(-buttonDistance);

        RotateTransition rtExpandButt = new RotateTransition(SIDEBAR_ANIMATION_DURATION, expandButtonShape);
        rtExpandButt.setFromAngle(0);
        rtExpandButt.setToAngle(-180);

        ParallelTransition flipExpandTransition = new ParallelTransition(rtCollapseButt, rtExpandButt,dividerSlidingTimeline);
        flipExpandTransition.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition buttonMoveTransition = new ParallelTransition(ttCollapseButt, ttExpandButt);
        buttonMoveTransition.setInterpolator(Interpolator.EASE_IN);

        SequentialTransition sequentialTransition = new SequentialTransition(flipExpandTransition, buttonMoveTransition);

        sequentialTransition.play();

    }

    @FXML
    public void collapseSidebar() {
        KeyValue keyValue = new KeyValue(mainSplitPane.getDividers().get(0).positionProperty(), 0);
        Timeline timeline = new Timeline(new KeyFrame(SIDEBAR_ANIMATION_DURATION, keyValue));

        double buttonDistance =  expandButtonShape.getBoundsInLocal().getWidth()
                + BorderPane.getMargin(expandButtonShape).getLeft()
                + BorderPane.getMargin(collapseButtonShape).getRight();


        TranslateTransition ttCollapseButt = new TranslateTransition(SIDEBAR_ANIMATION_DURATION,collapseButtonShape);
        ttCollapseButt.setFromX(0);
        ttCollapseButt.setToX(buttonDistance);

        RotateTransition rtCollapseButt = new RotateTransition(SIDEBAR_ANIMATION_DURATION, collapseButtonShape);
        rtCollapseButt.setFromAngle(120);
        rtCollapseButt.setToAngle(0);

        TranslateTransition ttExpandButt = new TranslateTransition(SIDEBAR_ANIMATION_DURATION,expandButtonShape);
        ttExpandButt.setFromX(-buttonDistance);
        ttExpandButt.setToX(0);

        RotateTransition rtExpandButt = new RotateTransition(SIDEBAR_ANIMATION_DURATION, expandButtonShape);
        rtExpandButt.setFromAngle(120);
        rtExpandButt.setToAngle(0);

        ParallelTransition parallelTransition = new ParallelTransition(ttCollapseButt, ttExpandButt);
        ParallelTransition flipCollapseTransition = new ParallelTransition(rtCollapseButt, rtExpandButt, timeline);
        SequentialTransition sequentialTransition = new SequentialTransition(parallelTransition, flipCollapseTransition);

        sequentialTransition.play();
    }

    @FXML
    public void toggleFullscreen() {
        if (primaryStage.isFullScreen()) {
            primaryStage.setFullScreen(false);
        } else {
            primaryStage.setFullScreen(true);
        }
    }


    private void makeEquilateral(Polygon poly, int sideNumber, double radius) {
        poly.getPoints().clear();
        double angleStep = Math.PI*2/sideNumber;
        for (int i = 0; i < sideNumber; i++) {
            poly.getPoints().add(Math.cos(angleStep*i)*radius);
            poly.getPoints().add(Math.sin(angleStep*i)*radius);
        }
    }
}
