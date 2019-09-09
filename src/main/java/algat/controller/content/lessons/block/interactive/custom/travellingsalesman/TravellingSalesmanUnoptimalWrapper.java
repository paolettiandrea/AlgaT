package algat.controller.content.lessons.block.interactive.custom.travellingsalesman;

import algat.model.lesson.block.interactive.InteractiveContentController;
import javafx.animation.Transition;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class TravellingSalesmanUnoptimalWrapper extends InteractiveContentController {

    public BorderPane mainBorderPane;
    TravellingSalesman travellingSalesman;

    public TravellingSalesmanUnoptimalWrapper() {
        super(Duration.seconds(1), 0.5, 3);
    }

    public void initialize() {
        travellingSalesman = new TravellingSalesman();
        mainBorderPane.setCenter(travellingSalesman);

        travellingSalesman.addCity(100,80);
        travellingSalesman.addCity(170,40);
        travellingSalesman.addCity(300,20);
        travellingSalesman.addCity(250,170);
        travellingSalesman.addCity(420,200);
        travellingSalesman.addCity(150,250);
        travellingSalesman.addCity(75,179);
    }

    @Override
    public Transition step() {
        return travellingSalesman.step();
    }

    @Override
    public void reset() {
        super.reset();
        travellingSalesman.reset(0);
    }

    @Override
    public boolean isCompletelyEnded() {
        return travellingSalesman.isDone();
    }
}
