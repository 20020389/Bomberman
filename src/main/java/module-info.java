module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.media;
    requires java.scripting;

    opens com.snow to javafx.fxml;
    exports com.snow;
}
