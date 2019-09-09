package algat.controller.content.lessons.block.interactive.custom.greedycoin;

import algat.model.lesson.block.interactive.InteractiveContentController;
import javafx.animation.Transition;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.util.Vector;

public class UnoptimalGreedyCoin extends InteractiveContentController {

    public BorderPane mainPane;
    GreedyCoin greedy;

    public UnoptimalGreedyCoin() {
        super(Duration.seconds(0.5),0.5,3);

        Vector<Integer> possibleCoins = new Vector<>();


        possibleCoins.add(10);
        possibleCoins.add(9);
        possibleCoins.add(2);
        possibleCoins.add(1);

        greedy = new GreedyCoin(possibleCoins, 38);

    }

    public void initialize() {
        mainPane.setCenter(greedy);
    }

    @Override
    public Transition step() {
        return greedy.step();
    }

    @Override
    public void reset() {
        super.reset();
        greedy.reset();
    }

    @Override
    public boolean isCompletelyEnded() {
        return greedy.isDone();
    }
}
