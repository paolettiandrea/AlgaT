package algat.controller.content.lessons.block.interactive.lightindicator;

import algat.App;
import javafx.animation.FillTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

public class LightIndicator extends AnchorPane {
    public Circle backCircle;
    public Circle frontCircle;

    public final Duration colorChangeDuration = Duration.seconds(0.2);

    private DoubleProperty radiusProp;
    private DoubleProperty strokeWeight;

    private FillTransition lightColorTransition;

    public LightIndicator() {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/fxml/interactive/lightindicator/LightIndicator.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public void initialize() {
        radiusProp = new SimpleDoubleProperty(9);
        radiusProp.addListener((observable, oldValue, newValue) -> {
            updateDimensions(newValue.doubleValue());
        });

        strokeWeight = new SimpleDoubleProperty(3);
        strokeWeight.addListener((observable, oldValue, newValue) -> {
            updateDimensions(radiusProp.get());
        });



        updateDimensions(radiusProp.get());
    }

    public void setLightColorNoAnimation(Color newColor) {
        frontCircle.setFill(newColor);
    }

    public void setLightColor(Color newColor) {

        if (lightColorTransition!=null) {
            lightColorTransition.stop();
        }

        lightColorTransition = new FillTransition(colorChangeDuration, frontCircle);
        lightColorTransition.setToValue(newColor);

        lightColorTransition.setOnFinished(event -> {
            lightColorTransition = null;
        });

        lightColorTransition.play();


    }

    public DoubleProperty radiusProperty() {
        return radiusProp;
    }


    public void setRadius(double newRadius) {
        radiusProp.setValue(newRadius);
    }

    public  void setStrokeColor(Color newColor) {
        backCircle.setFill(newColor);
    }

    private void updateDimensions(double newRadius) {
        if (radiusProp.get() <= strokeWeight.get()) {
            throw new Error("LightIndicator Error: given a radius <= stroke thickness");
        }
        backCircle.setRadius(newRadius);
        frontCircle.setRadius(newRadius-strokeWeight.get());

        InnerShadow innerShadow = (InnerShadow)frontCircle.getEffect();
        innerShadow.setHeight(radiusProp.doubleValue()*1.5);
        innerShadow.setWidth(radiusProp.doubleValue()*1.5);
    }

    public double getStrokeWeight() {
        return strokeWeight.get();
    }

    public DoubleProperty strokeWeightProperty() {
        return strokeWeight;
    }

    public void setStrokeWeight(double strokeWeight) {
        this.strokeWeight.set(strokeWeight);
    }
}
