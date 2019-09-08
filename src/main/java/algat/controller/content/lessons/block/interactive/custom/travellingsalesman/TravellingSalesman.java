package algat.controller.content.lessons.block.interactive.custom.travellingsalesman;

import algat.App;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class TravellingSalesman extends Pane {

    private @FXML Pane mainPane;
    private @FXML Pane temporaryLinkPane;
    private @FXML Pane optimalLinksPane;

    private List<Circle> cities = new Vector<>();
    private Circle pointedCity;
    private int visitedCities = 0;

    // City colors
    private static final Color HIGHLIGHTED_CITY_COLOR = new Color(0.8,0.8,0.2, 1);
    private static final Color VISITED_CITY_COLOR = new Color(0.5,0.2,0.2, 1);

    private static final Duration HIGHTLIGHT_FILL_DURATION = Duration.seconds(0.3);


    // Link colors
    private static final Color DISCARDED_LINK_COLOR = new Color(0.8, 0.8, 0.8, 1);
    private static final Color POINTED_LINK_COLOR = new Color(0.1, 0.1, 0.1, 1);
    private static final Color STILL_TO_CONSIDER_LINK_COLOR = new Color(0.6, 0.6, 0.6, 1);
    private static final Color OPTIMAL_LINK_COLOR = VISITED_CITY_COLOR;

    private static final Duration LINK_FADE_IN_DURATION = Duration.seconds(1);
    private static final Duration LINK_FOCUS_IN_DURATION = Duration.seconds(1);
    private static final Duration LINK_MIN_SWITCH_DURATION = Duration.seconds(1);
    private static final Duration CHECKED_LINK_FADE_OUT_DURATION = Duration.seconds(1);



    public TravellingSalesman() {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/fxml/interactive/custom/travellingsalesman/TravellingSalesman.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void addCity(double xPos, double yPos) {
        Circle newCircle = new Circle(xPos, yPos, 5);
        newCircle.getStyleClass().add("city");
        newCircle.setFill(STILL_TO_CONSIDER_LINK_COLOR);
        mainPane.getChildren().add(newCircle);

        cities.add(newCircle);
    }

    public Transition step() {
        visitedCities++;

        SequentialTransition sequentialTransition = new SequentialTransition();

        FadeTransition temporaryLinksFadeInTransition = new FadeTransition(LINK_FADE_IN_DURATION, temporaryLinkPane);
        temporaryLinksFadeInTransition.setToValue(1);
        sequentialTransition.getChildren().add(temporaryLinksFadeInTransition);

        // Scroll all other cities in order to find the closest one
        double minDistance = Double.MAX_VALUE;
        Line minLine = null;
        Circle optimalCity = null;

        for (Circle otherCity: cities) {
            if (otherCity!=pointedCity) {
                Line newLine = buildLinkLine(pointedCity.getCenterX(), pointedCity.getCenterY(),
                        otherCity.getCenterX(), otherCity.getCenterY());

                temporaryLinkPane.getChildren().add(newLine);

                // If the other city has already been visited discard this link right away
                if (otherCity.getFill().equals(VISITED_CITY_COLOR)) {
                    newLine.setStroke(DISCARDED_LINK_COLOR);

                } else {

                    // This line needs to be considered
                    newLine.setStroke(STILL_TO_CONSIDER_LINK_COLOR);

                    double distance = distance(pointedCity.getCenterX(), pointedCity.getCenterY(),
                            otherCity.getCenterX(), otherCity.getCenterY());

                    StrokeTransition focusInStrokeTransition = new StrokeTransition(LINK_FOCUS_IN_DURATION, newLine);
                    focusInStrokeTransition.setToValue(POINTED_LINK_COLOR);
                    sequentialTransition.getChildren().add(focusInStrokeTransition);

                    PauseTransition afterFocusPauseTransition = new PauseTransition(Duration.seconds(0.5));
                    sequentialTransition.getChildren().add(afterFocusPauseTransition);

                    if (distance >= minDistance) {
                        StrokeTransition consideredUnoptimalTransition = new StrokeTransition(LINK_FOCUS_IN_DURATION, newLine);
                        consideredUnoptimalTransition.setToValue(DISCARDED_LINK_COLOR);
                        sequentialTransition.getChildren().add(consideredUnoptimalTransition);
                    } else {

                        ParallelTransition minChangeTransition = new ParallelTransition();


                        if (minLine!=null) {
                            StrokeTransition oldMinTransition = new StrokeTransition(LINK_MIN_SWITCH_DURATION, minLine);
                            oldMinTransition.setToValue(DISCARDED_LINK_COLOR);
                            minChangeTransition.getChildren().add(oldMinTransition);
                        }

                        StrokeTransition newMinTransition = new StrokeTransition(LINK_MIN_SWITCH_DURATION, newLine);
                        newMinTransition.setToValue(OPTIMAL_LINK_COLOR);
                        minChangeTransition.getChildren().add(newMinTransition);

                        sequentialTransition.getChildren().add(minChangeTransition);

                        minLine = newLine;
                        minDistance = distance;
                        optimalCity = otherCity;
                    }
                }
            }
        }

        // Highlight optimal city found
        ParallelTransition switchHighlightingTransition = new ParallelTransition();


        FillTransition highlightNextCityTransition = new FillTransition(LINK_MIN_SWITCH_DURATION, optimalCity);
        highlightNextCityTransition.setToValue(HIGHLIGHTED_CITY_COLOR);
        switchHighlightingTransition.getChildren().add(highlightNextCityTransition);

        FillTransition deHighlightOldCityTransition = new FillTransition(LINK_MIN_SWITCH_DURATION, pointedCity);
        deHighlightOldCityTransition.setToValue(VISITED_CITY_COLOR);
        switchHighlightingTransition.getChildren().add(deHighlightOldCityTransition);


        sequentialTransition.getChildren().add(switchHighlightingTransition);

        PauseTransition endPause = new PauseTransition(Duration.seconds(0.2));
        Line optimalLine = minLine;
        endPause.setOnFinished(event -> {
            temporaryLinkPane.getChildren().remove(optimalLine);
            optimalLinksPane.getChildren().add(optimalLine);
        });
        sequentialTransition.getChildren().add(endPause);

        FadeTransition fadeOutLinksTransition = new FadeTransition(CHECKED_LINK_FADE_OUT_DURATION, temporaryLinkPane);
        fadeOutLinksTransition.setToValue(0);
        fadeOutLinksTransition.setOnFinished(event -> {
            temporaryLinkPane.getChildren().clear();
        });
        sequentialTransition.getChildren().add(fadeOutLinksTransition);

        Circle city = optimalCity;
        sequentialTransition.setOnFinished(event -> {
            pointedCity = city;
        });

        return sequentialTransition;

    }

    public void reset() {

        // Choose randomly a city
        Random random = new Random();
        pointedCity = cities.get(random.nextInt(cities.size()));
        pointedCity.setFill(HIGHLIGHTED_CITY_COLOR);
        
        temporaryLinkPane.getChildren().clear();
        optimalLinksPane.getChildren().clear();

        for (Circle city :
                cities) {
            city.setFill(STILL_TO_CONSIDER_LINK_COLOR);
        }


    }

    public boolean isDone() {
        return visitedCities>=(cities.size()-1);
    }


    private Line buildLinkLine(double fromX, double fromY, double toX, double toY) {
        Line newLine = new Line(fromX, fromY, toX, toY);
        newLine.setStrokeWidth(2);
        newLine.getStyleClass().add("link-line");
        return newLine;
    }

    private double distance(double fromX, double fromY, double toX, double toY) {
        return Math.sqrt(Math.pow(fromX-toX,2) + Math.pow(fromY - toY,2));
    }
}
