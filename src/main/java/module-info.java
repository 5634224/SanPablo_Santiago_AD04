module com.santiago.sanpablo_santiago_ad04 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;


    opens com.santiago.sanpablo_santiago_ad04 to javafx.fxml;
    opens entity;
    exports com.santiago.sanpablo_santiago_ad04;
    exports entity;
    exports com.santiago.sanpablo_santiago_ad04.controller;
    opens com.santiago.sanpablo_santiago_ad04.controller to javafx.fxml;
}