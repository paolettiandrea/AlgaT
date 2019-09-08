module algat {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens algat to javafx.fxml;
    opens algat.controller to javafx.fxml;
    opens algat.controller.content to javafx.fxml;
    opens algat.controller.content.questions to javafx.fxml;
    opens algat.controller.content.lessons to javafx.fxml;
    opens algat.controller.content.lessons.block to javafx.fxml;
    opens algat.controller.content.lessons.block.interactive to javafx.fxml;
    opens algat.controller.content.lessons.block.interactive.custom to javafx.fxml;
    opens algat.controller.content.lessons.block.interactive.lightindicator to javafx.fxml;
    opens algat.controller.content.lessons.block.interactive.custom.greedycoin to javafx.fxml;
    opens algat.controller.content.lessons.block.interactive.custom.travellingsalesman to javafx.fxml;

    exports algat;
}
