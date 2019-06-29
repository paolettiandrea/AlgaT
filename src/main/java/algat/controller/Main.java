package algat.controller;

import algat.model.AppContent;
import algat.model.Topic;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Main controller for the application
 */
public class Main {

    public static final Duration SIDEBAR_ANIMATION_DURATION = new Duration(400);
    public static final Duration FLIP_BUTTON_MOVE_ANIMATION_DURATION = new Duration(300);

    @FXML
    private ImageView collapseButtonIV;

    @FXML
    private ImageView expandButtonIV;

    /**
     * An FX-injected reference to the VBox meant to contain the navigation menu
     */
    @FXML
    private VBox topicNavigationVBox;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private ContentPanel contentPanelController;




    /**
     * Method called by FXML when the corresponding FXML is loaded
     */
    public void initialize()
    {

    }

    /**
     * Populate the application with the content
     *
     * @param data The content that the application needs to display
     */
    public void populate(AppContent data) {

        for (Topic topic : data.getTopicList()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/algat/view/fxml/TopicMenuField.fxml"));
                AnchorPane topicMenuField = loader.load();
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
        double val = topicNavigationVBox.getPrefWidth()/mainSplitPane.getWidth() + expandButtonIV.getFitWidth()/mainSplitPane.getWidth();

        KeyValue keyValue = new KeyValue(mainSplitPane.getDividers().get(0).positionProperty(), val);
        Timeline dividerSlidingTimeline = new Timeline(new KeyFrame(SIDEBAR_ANIMATION_DURATION, keyValue));

        double buttonDistance = collapseButtonIV.getFitWidth()
                                + BorderPane.getMargin(collapseButtonIV).getRight()
                                + BorderPane.getMargin(expandButtonIV).getLeft() ;



        TranslateTransition ttCollapseButt = new TranslateTransition(FLIP_BUTTON_MOVE_ANIMATION_DURATION,collapseButtonIV);
        ttCollapseButt.setFromX(buttonDistance);
        ttCollapseButt.setToX(0);

        RotateTransition rtCollapseButt = new RotateTransition(SIDEBAR_ANIMATION_DURATION, collapseButtonIV);
        rtCollapseButt.setFromAngle(0);
        rtCollapseButt.setToAngle(-180);

        TranslateTransition ttExpandButt = new TranslateTransition(FLIP_BUTTON_MOVE_ANIMATION_DURATION,expandButtonIV);
        ttExpandButt.setFromX(0);
        ttExpandButt.setToX(-buttonDistance);

        RotateTransition rtExpandButt = new RotateTransition(SIDEBAR_ANIMATION_DURATION, expandButtonIV);
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



        // Save the initial distance of the expand/collapse buttons

        double buttonDistance = collapseButtonIV.getFitWidth()
                + BorderPane.getMargin(collapseButtonIV).getRight()
                + BorderPane.getMargin(expandButtonIV).getLeft() ;

        TranslateTransition ttCollapseButt = new TranslateTransition(SIDEBAR_ANIMATION_DURATION,collapseButtonIV);
        ttCollapseButt.setFromX(0);
        ttCollapseButt.setToX(buttonDistance);

        RotateTransition rtCollapseButt = new RotateTransition(SIDEBAR_ANIMATION_DURATION, collapseButtonIV);
        rtCollapseButt.setFromAngle(60);
        rtCollapseButt.setToAngle(0);

        TranslateTransition ttExpandButt = new TranslateTransition(SIDEBAR_ANIMATION_DURATION,expandButtonIV);
        ttExpandButt.setFromX(-buttonDistance);
        ttExpandButt.setToX(0);

        RotateTransition rtExpandButt = new RotateTransition(SIDEBAR_ANIMATION_DURATION, expandButtonIV);
        rtExpandButt.setFromAngle(60);
        rtExpandButt.setToAngle(0);

        ParallelTransition parallelTransition = new ParallelTransition(ttCollapseButt, ttExpandButt);
        ParallelTransition flipCollapseTransition = new ParallelTransition(rtCollapseButt, rtExpandButt, timeline);
        SequentialTransition sequentialTransition = new SequentialTransition(parallelTransition, flipCollapseTransition);

        sequentialTransition.play();
    }

}
