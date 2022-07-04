module com.example.clientfront {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.clientfront to javafx.fxml;
    exports com.example.clientfront;

}