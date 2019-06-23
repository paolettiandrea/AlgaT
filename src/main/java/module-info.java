module algat {
    requires javafx.controls;
    requires javafx.fxml;

    opens algat to javafx.fxml;
    opens algat.controller to javafx.fxml;

    exports algat;
}
