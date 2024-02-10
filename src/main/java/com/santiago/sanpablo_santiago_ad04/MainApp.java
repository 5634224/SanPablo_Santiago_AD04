package com.santiago.sanpablo_santiago_ad04;

import com.santiago.sanpablo_santiago_ad04.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        // Cargador del FXML de la escena
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("mainApp.fxml"));
//
//        // Carga la escena a partir del FXML, y la configura
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//
//        // Carga el controlador de la escena
//        MainController controller = fxmlLoader.getController();
//
//        // Pasa el stage y las propiedades a la escena (esto es por si en el futuro se necesitase cambiar de escena)
//        Escenario escenario = new Escenario(stage);
//        controller.setStage(escenario);
//
//        // Fija el tamaño minimo de la ventana
//        stage.setMinWidth(980);
//        stage.setMinHeight(600);
//
//        // Pone la escena dentro del escenario y la muestra
//        stage.setScene(scene);
//        stage.show();

        Escenario escenario = new Escenario(stage);
        Scene scene = new Escenario.Builder()
                .escenario(escenario)
                .mainClass(MainApp.class)
                .URLResource("mainApp.fxml")
                .title("San Pablo Santiago AD04")
                .width(980)
                .height(600)
                .minWidth(980)
                .minHeight(600)
                .maximized(false)
                .fullScreen(false)
                .showScene(true)
                .build();
    }

    public static void main(String[] args) {
        launch();
    }
}