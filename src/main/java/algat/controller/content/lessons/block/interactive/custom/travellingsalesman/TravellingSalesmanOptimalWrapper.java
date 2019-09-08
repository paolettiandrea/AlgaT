package algat.controller.content.lessons.block.interactive.custom.travellingsalesman;

import algat.model.lesson.block.interactive.InteractiveContentController;
import javafx.animation.Transition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class TravellingSalesmanOptimalWrapper extends InteractiveContentController {

    public AnchorPane mainAnchorPane;
    TravellingSalesman travellingSalesman;

    public TravellingSalesmanOptimalWrapper() {
        super(Duration.seconds(0.5), 0.5, 10);
    }

    public void initialize() {
        travellingSalesman = new TravellingSalesman();
        mainAnchorPane.getChildren().add(travellingSalesman);
        AnchorPane.setLeftAnchor(travellingSalesman,0.0);
        AnchorPane.setRightAnchor(travellingSalesman,0.0);
        AnchorPane.setTopAnchor(travellingSalesman,0.0);
        AnchorPane.setBottomAnchor(travellingSalesman,0.0);

        travellingSalesman.addCity(10,10);
        travellingSalesman.addCity(300,40);
        travellingSalesman.addCity(250,20);
        travellingSalesman.addCity(15,300);
        travellingSalesman.addCity(150,100);
        travellingSalesman.addCity(100,10);
        travellingSalesman.addCity(100,100);
        travellingSalesman.addCity(150,10);
        travellingSalesman.addCity(270,30);
        travellingSalesman.addCity(270,70);
        travellingSalesman.addCity(150,360);
        travellingSalesman.addCity(160,160);
        travellingSalesman.addCity(120,100);
        travellingSalesman.addCity(109,100);
    }

    @Override
    public Transition step() {
        return travellingSalesman.step();
    }

    @Override
    public void reset() {
        super.reset();
        travellingSalesman.reset();
    }

    @Override
    public boolean isCompletelyEnded() {
        return travellingSalesman.isDone();
    }
}
