package algat.controller.content.lessons.block.interactive.custom.greedycoin;

import algat.model.lesson.block.interactive.InteractiveContentController;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.util.List;
import java.util.Vector;

public class CustomGreedyCoin extends InteractiveContentController {

    public BorderPane mainPane;
    public TextField amountTextField;
    public TextField possibleCoinListTextField;
    public Button buildButton;

    private GreedyCoin greedyCore;

    public CustomGreedyCoin() {
        super(Duration.seconds(0.5),0.5,3);
    }

    @FXML
    public void build() {
        if (greedyCore!=null) {
            mainPane.getChildren().remove(greedyCore);
        }

        try {
            int amount = Integer.parseInt(amountTextField.getText());
            List<Integer> posCoins = parsePossibleCoinList(possibleCoinListTextField.getText());

            greedyCore = new GreedyCoin(posCoins, amount);
            mainPane.setCenter(greedyCore);
            wrapperInterface.enableControlPanel();
            reset();
        } catch (NumberFormatException e) {
            System.out.println("I valori inseriti non sono validi. Ammessi solo numeri interi e le monete possibili devono essere separate da virgole");
        }


    }

    private List<Integer> parsePossibleCoinList(String possibleCoinsString) {
        List<Integer> posCoins = new Vector<>();

        for (String s: possibleCoinsString.split(",")) {
            posCoins.add(Integer.parseInt(s.strip()));
        }

        return posCoins;
    }


    @Override
    public Transition step() {
        return greedyCore.step();
    }

    @Override
    public void reset() {
        super.reset();

        if (greedyCore!=null) {
            greedyCore.reset();
        } else {
            wrapperInterface.disableControlPanel();
        }

    }

    @Override
    public boolean isCompletelyEnded() {
        return greedyCore.isDone();
    }

}
