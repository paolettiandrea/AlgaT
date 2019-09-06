module algat {
    requires javafx.controls;
    requires javafx.fxml;

    opens algat to javafx.fxml;
    opens algat.controller to javafx.fxml;
    opens algat.controller.content to javafx.fxml;
    opens algat.controller.content.lessons to javafx.fxml;
    opens algat.controller.content.lessons.block to javafx.fxml;
    opens algat.controller.content.lessons.block.interactive to javafx.fxml;
    opens algat.controller.content.lessons.block.interactive.custom to javafx.fxml;
    opens algat.controller.content.questions to javafx.fxml;
    opens algat.controller.content.lessons.block.interactive.lightindicator to javafx.fxml;

    exports algat;
}
