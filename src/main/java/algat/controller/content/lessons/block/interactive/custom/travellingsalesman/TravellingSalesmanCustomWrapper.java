package algat.controller.content.lessons.block.interactive.custom.travellingsalesman;

import algat.model.lesson.block.interactive.InteractiveContentController;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class TravellingSalesmanCustomWrapper extends InteractiveContentController {

    public BorderPane mainBorderPane;
    private TravellingSalesman travellingSalesman;

    public Label topLabel;
    public Button topButton;

    boolean inputCompleted = false;

    public TravellingSalesmanCustomWrapper() {
        super(Duration.seconds(1), 0.5, 3);
    }

    public void initialize() {
        travellingSalesman = new TravellingSalesman();
        mainBorderPane.setCenter(travellingSalesman);




    }

    @Override
    public Transition step() {
        return travellingSalesman.step();
    }

    @Override
    public void reset() {
        if (inputCompleted) {
            super.reset();
            travellingSalesman.reset();
        } else {
            inputMode();
        }
    }

    @Override
    public boolean isCompletelyEnded() {
        return travellingSalesman.isDone();
    }


    private void inputDone() {
        if (travellingSalesman.getCityNumber()>=3) {
            inputCompleted = true;
            simulMode();
            reset();
        } else {
            throw new Error("You need to define at least 3 cities before you can start");
        }
    }

    private void resetToInput() {
        mainBorderPane.getChildren().remove(travellingSalesman);
        travellingSalesman = new TravellingSalesman();
        mainBorderPane.setCenter(travellingSalesman);

        inputMode();
    }




    private void inputMode() {
        travellingSalesman.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                travellingSalesman.addCity(event.getX(), event.getY());
            }
        });

        topButton.setOnAction(event -> inputDone());
        topButton.setText("Inizia");

        topLabel.setText("Inserisci città cliccando, quando sei pronto");

        wrapperInterface.disableControlPanel();


        inputCompleted = false;
    }

    private void simulMode(){
        wrapperInterface.enableControlPanel();
        travellingSalesman.setOnMouseClicked(event -> {});

        topButton.setOnAction(event -> resetToInput());
        topButton.setText("Input");

        topLabel.setText("Quando vuoi puoi tornare all'");
    }
}
