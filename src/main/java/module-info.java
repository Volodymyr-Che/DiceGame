module com.example.fingame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fingame to javafx.fxml;
    exports com.example.fingame;
}