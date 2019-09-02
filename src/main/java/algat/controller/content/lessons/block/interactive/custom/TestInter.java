package algat.controller.content.lessons.block.interactive.custom;

import algat.model.lesson.block.interactive.InteractiveContentController;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Just a sample interactive animation that organizes rectangles in a row, putting a new one every step.
 */
public class TestInter extends InteractiveContentController {

    private static final int MAX_RECTANGLES = 10;
    private static final double RECTANGLE_SIZE = 20;
    private static final double RECTANGLE_BORDER_WIDTH = 2;

    public HBox rectangleHBox;
    public Text rectangleCountText;

    private int rectangleCount = 0;

    public TestInter() {
        super(Duration.seconds(1), 0.5, 10);
    }

    public void initialize() {
        rectangleHBox.setMaxWidth(MAX_RECTANGLES*(RECTANGLE_SIZE+RECTANGLE_BORDER_WIDTH*2));
        rectangleHBox.setMinHeight(RECTANGLE_SIZE+RECTANGLE_BORDER_WIDTH*2);
        rectangleHBox.setBackground(new Background(new BackgroundFill(Color.gray(0.5), null, null)));
    }



    @Override
    public void reset() {
        super.reset();
        rectangleCountText.setText("0/"+MAX_RECTANGLES);
        rectangleHBox.getChildren().clear();
        rectangleCount = 0;
    }

    @Override
    public Transition step() {
        Rectangle rectangle = new Rectangle(RECTANGLE_SIZE, RECTANGLE_SIZE);
        rectangle.setTranslateY(-40);
        rectangle.setFill(Paint.valueOf("Red"));
        rectangle.setArcWidth(5);
        rectangle.setArcHeight(5);
        rectangle.setStroke(Color.gray(0.2));
        rectangle.setStrokeType(StrokeType.OUTSIDE);
        rectangle.setSmooth(false);
        rectangle.setStrokeWidth(RECTANGLE_BORDER_WIDTH);

        rectangleHBox.getChildren().add(rectangle);

        TranslateTransition enterTransition = new TranslateTransition();
        enterTransition.setNode(rectangle);
        enterTransition.setFromY(-40);
        enterTransition.setToY(0);
        enterTransition.setDuration(Duration.seconds(0.25));
        enterTransition.setInterpolator(Interpolator.EASE_BOTH);

        rectangleCount++;
        rectangleCountText.setText(rectangleCount + "/" + MAX_RECTANGLES);

        if (rectangleCount==10) {
            animationEnded();
        }
        return enterTransition;
    }
}
