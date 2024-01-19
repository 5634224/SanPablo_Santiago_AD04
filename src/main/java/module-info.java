module com.santiago.sanpablo_santiago_ad04 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens com.santiago.sanpablo_santiago_ad04 to javafx.fxml;
    exports com.santiago.sanpablo_santiago_ad04;
}