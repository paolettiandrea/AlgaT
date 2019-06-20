module algat {
    requires javafx.controls;
    requires javafx.fxml;

    opens algat to javafx.fxml;
    exports algat;
}
