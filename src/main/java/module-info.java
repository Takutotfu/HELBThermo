module com.example.helbthermo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.helbthermo to javafx.fxml;
    exports com.example.helbthermo;
}