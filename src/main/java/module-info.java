module com.uce.insight {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires static lombok;

    opens com.uce.insight.ui.login to javafx.fxml;
    opens com.uce.insight.ui.main to javafx.fxml;
    opens com.uce.insight.ui.project to javafx.fxml, javafx.base;
    opens com.uce.insight to javafx.fxml;
    exports com.uce.insight;
    exports com.uce.insight.ui;
    opens com.uce.insight.ui to javafx.fxml;
}