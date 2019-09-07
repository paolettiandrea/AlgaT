package algat.controller.content.lessons.block.interactive.custom.greedycoin;

import algat.App;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class GreedyCoin extends AnchorPane {

    @FXML private AnchorPane mainPane;
    @FXML private HBox coinHBox;
    @FXML private Rectangle amountContainerRectangle;
    @FXML private AnchorPane coinContainerAnchorPane;

    List<Integer> possibleCoins;
    double amount;
    double horizontalScale;

    List<Coin> possibleCoinsList = new Vector<>();
    int lastPossibleCoinConsideredIndex;
    double amountRemained;
    double amountLastOccupiedXPos;

    private static final double COIN_SPACING = 5;
    private static final double AMOUNT_CONTAINER_STROKE_WIDTH = 6;
    private static final Duration COIN_SLIDE_DURATION = Duration.seconds(1);
    private static final Duration COIN_FADE_IN_DURATION = Duration.seconds(0.2);
    private static final Duration POSSIBLE_COINS_FADE_DURATION = Duration.seconds(0.4);
    private static final Duration INACTIVATION_COLOR_CHANGE_DURATION = Duration.seconds(0.2);
    private static final double UNFOCUSED_POSSIBLE_COINS_OPACITY = 0.1;
    private static final Color ALREADY_CHECKED_POSSIBLE_COIN_COLOR = new Color(0.7,0.7,0.7,1);


    private static final double COIN_COLOR_ALTERNATION_FACTOR = 0.08;
    private static final Color[] possibleColors = {
            Color.web("rgb(26, 188, 156)"),
            Color.web("rgb(46, 204, 113)"),
            Color.web("rgb(52, 152, 219)"),
            Color.web("rgb(155, 89, 182)"),
            Color.web("rgb(241, 196, 15)"),
            Color.web("rgb(231, 76, 60)")
    };

    public GreedyCoin(List<Integer> possibleCoins, double amount) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/fxml/interactive/custom/greedycoin/GreedyCoin.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.possibleCoins = possibleCoins;
        this.amount = amount;
    }

    public void initialize() {
        coinHBox.setSpacing(COIN_SPACING);

        amountContainerRectangle.setHeight(Coin.COIN_HEIGHT +AMOUNT_CONTAINER_STROKE_WIDTH);
        amountContainerRectangle.setStrokeWidth(AMOUNT_CONTAINER_STROKE_WIDTH);
        amountContainerRectangle.setStrokeType(StrokeType.CENTERED);

        AnchorPane.setLeftAnchor(coinContainerAnchorPane, 0.0);
        AnchorPane.setTopAnchor(coinContainerAnchorPane, AMOUNT_CONTAINER_STROKE_WIDTH);
    }


    void reset() {
        lastPossibleCoinConsideredIndex = -1;
        amountRemained = amount;
        amountLastOccupiedXPos = AMOUNT_CONTAINER_STROKE_WIDTH;

        coinHBox.getChildren().clear();
        coinContainerAnchorPane.getChildren().clear();
        possibleCoinsList.clear();

        coinHBox.setTranslateX(0);

        coinContainerAnchorPane.getChildren().clear();

        double possibleCoinAvailableWidth = mainPane.getPrefWidth() - COIN_SPACING*(possibleCoins.size()-1) - Coin.COIN_STROKE_WIDTH*(possibleCoins.size()-1);
        double amountAvailableWidth = mainPane.getPrefWidth();

        double totPossibleVal = 0;
        for (Integer coinVal: possibleCoins) {
            totPossibleVal += coinVal;
        }

        double possibleCoinScale = totPossibleVal / possibleCoinAvailableWidth;
        double amountScale = amount / amountAvailableWidth;
        horizontalScale = Math.max(possibleCoinScale, amountScale);

        amountContainerRectangle.setWidth(amount/horizontalScale + AMOUNT_CONTAINER_STROKE_WIDTH);

        // Populate possible coins
        for (int i = 0; i < possibleCoins.size(); i++) {
            Coin newPossibleCoin = new Coin(possibleCoins.get(i)/horizontalScale, possibleCoins.get(i));
            coinHBox.getChildren().add(newPossibleCoin);
            possibleCoinsList.add(newPossibleCoin);
            newPossibleCoin.setOverallColor(possibleColors[i%possibleColors.length]);
        }
    }


    Transition step() {
        int targetIndex = ++lastPossibleCoinConsideredIndex;

        SequentialTransition sequentialTransition = new SequentialTransition();


        // Initial transition
        ParallelTransition everyOtherPossibleCoinFadeTransition = new ParallelTransition();
        for (int i = 0; i < possibleCoins.size(); i++) {
            FadeTransition fadeTrans = new FadeTransition(POSSIBLE_COINS_FADE_DURATION, possibleCoinsList.get(i));;
            fadeTrans.setToValue(UNFOCUSED_POSSIBLE_COINS_OPACITY);
            if (i!=targetIndex) everyOtherPossibleCoinFadeTransition.getChildren().add(fadeTrans);
        }
        sequentialTransition.getChildren().add(everyOtherPossibleCoinFadeTransition);

        int loopNumber = (int)(amountRemained/possibleCoins.get(targetIndex));
        for (int i = 0; i <= loopNumber; i++) {
            int thisCoinAmount = possibleCoins.get(targetIndex);

            double consideredCoinMinX = possibleCoinsList.get(targetIndex).getBoundsInParent().getMinX();
            double xTrans = amountLastOccupiedXPos - consideredCoinMinX;
            double localLastXPos = amountLastOccupiedXPos;
            TranslateTransition slidingTransition = new TranslateTransition(COIN_SLIDE_DURATION, coinHBox);
            slidingTransition.setToX(xTrans);
            slidingTransition.setInterpolator(Interpolator.EASE_BOTH);

            // Add a new coin in the contaier at the end of the sliding animation (only if it's not the last slide)
            if (i<loopNumber) {
                // Alternate between slightly darker and slightly brighter colors for the coins of the same size
                Color targetColor;
                if (i%2==0) targetColor = possibleColors[targetIndex].interpolate(Color.WHITE, COIN_COLOR_ALTERNATION_FACTOR);
                else targetColor = possibleColors[targetIndex].interpolate(Color.BLACK, COIN_COLOR_ALTERNATION_FACTOR);

                slidingTransition.setOnFinished(event -> {
                    Coin newCoin = new Coin(thisCoinAmount/horizontalScale, thisCoinAmount);
                    newCoin.setOpacity(0);
                    newCoin.setOverallColor(targetColor);
                    coinContainerAnchorPane.getChildren().add(newCoin);
                    AnchorPane.setLeftAnchor(newCoin, localLastXPos);
                    AnchorPane.setTopAnchor(newCoin, 0.0);

                    FadeTransition fadeInTransition = new FadeTransition(COIN_FADE_IN_DURATION, newCoin);
                    fadeInTransition.setInterpolator(Interpolator.EASE_IN);
                    fadeInTransition.setFromValue(0);
                    fadeInTransition.setToValue(1);
                    fadeInTransition.play();
                });
                amountRemained  = amountRemained - thisCoinAmount;
                amountLastOccupiedXPos += thisCoinAmount/horizontalScale;
            }

            sequentialTransition.getChildren().add(slidingTransition);
            sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(0.3)));
        }

        // Final transition
        Timeline inactivationColorChange = new Timeline();
        inactivationColorChange.getKeyFrames().add(new KeyFrame(INACTIVATION_COLOR_CHANGE_DURATION,
                new KeyValue(possibleCoinsList.get(targetIndex).getOverallColorProperty(), ALREADY_CHECKED_POSSIBLE_COIN_COLOR)));
        sequentialTransition.getChildren().add(inactivationColorChange);

        ParallelTransition unfadeUnfocusedTransition = new ParallelTransition();

        TranslateTransition returnPossibleCoinToZeroTransition = new TranslateTransition(COIN_SLIDE_DURATION, coinHBox);
        returnPossibleCoinToZeroTransition.setToX(0);
        unfadeUnfocusedTransition .getChildren().add(returnPossibleCoinToZeroTransition);

        for (int i = 0; i < possibleCoins.size(); i++) {
            FadeTransition fadeTrans = new FadeTransition(POSSIBLE_COINS_FADE_DURATION, possibleCoinsList.get(i));;
            fadeTrans.setToValue(1);
            if (i!=targetIndex) unfadeUnfocusedTransition.getChildren().add(fadeTrans);
        }

        sequentialTransition.getChildren().add(unfadeUnfocusedTransition);

        return sequentialTransition;
    }

    boolean isDone() {
        return lastPossibleCoinConsideredIndex >= (possibleCoins.size()-1);
    }




}
