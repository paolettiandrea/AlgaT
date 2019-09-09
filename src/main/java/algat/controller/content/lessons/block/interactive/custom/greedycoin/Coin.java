package algat.controller.content.lessons.block.interactive.custom.greedycoin;

import algat.App;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.io.IOException;

public class Coin extends StackPane {

    public static final double COIN_STROKE_WIDTH = 6;
    public static final double COIN_HEIGHT = 30;


    @FXML Rectangle rectangle;
    @FXML Text text;

    double actualStrokeWidth;
    double width;

    private ObjectProperty<Paint> overallColorProperty = new SimpleObjectProperty<>();

    public Coin(double width, int val) {

        this.width = width;

        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/fxml/interactive/custom/greedycoin/Coin.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        text.setText("" + val);

        if (text.getBoundsInLocal().getWidth()>width) {
            text.setText("");
        }

        if (width<COIN_STROKE_WIDTH) {
            actualStrokeWidth = 0;
            rectangle.setStrokeLineCap(StrokeLineCap.BUTT);
        } else actualStrokeWidth = COIN_STROKE_WIDTH;


    }

    public void initialize(){
        overallColorProperty.addListener((observable, oldValue, newValue) -> {
            rectangle.setStroke(newValue);
            rectangle.setFill(newValue);
        });

        rectangle.setWidth(width - actualStrokeWidth);
        rectangle.setHeight(COIN_HEIGHT);
        rectangle.setStrokeType(StrokeType.CENTERED);
        rectangle.setStrokeWidth(actualStrokeWidth);
        rectangle.setStrokeLineJoin(StrokeLineJoin.ROUND);
        rectangle.setStrokeLineCap(StrokeLineCap.ROUND);
    }

    public ObjectProperty<Paint> getOverallColorProperty() {
        return overallColorProperty;
    }

    public void setOverallColor(Color newColor) {
        overallColorProperty.setValue(newColor);
    }


}
