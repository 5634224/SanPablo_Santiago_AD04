package com.santiago.sanpablo_santiago_ad04;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

/**
 * Clase que implementa funcionalidad adicional respecto a los escenarios.
 * Entre otras funcionalidades, permite volver a la escena anterior.
 *
 * @author Santiago Francisco San Pablo Raposo
 * @version 28.11.2023
 */
public class Escenario {

    /*===================== CAMPOS =====================*/
    /**
     * Variable estática que contiene el escenario de forma pública para el controlador de cualquier escena (ya que están en el mismo paquete)
     */
    private Stage escenario;

    /**
     * Pila de escenas para poder volver a la escena anterior
     */
    private Stack<Scene> escenas = new Stack<>();

    /*==================== MÉTODOS ====================*/

    /**
     * Constructor de la clase
     *
     * @param escenario Escenario que se quiere establecer como escenario actual
     */
    public Escenario(Stage escenario) {
        this.escenario = escenario;
    }

    /**
     * Método que establece la escena actual
     */
    protected void setEscena(Scene escena) {
        this.escenario.setScene(escena);
        this.escenas.push(escena);
    }

    /**
     * Método que obtiene la escena actual
     */
    public Scene getEscena() {
        return this.escenario.getScene();
    }

    /**
     * Método que permite volver a la escena anterior
     */
    protected void getSceneBack() {
        if (!this.escenas.empty()) {
            this.escenas.pop();
            this.escenario.setScene(escenas.peek());
        }
    }

    public static class Builder {
        private Escenario escenario;
        private Class mainClass;
        private String URLResource;
        private boolean showScene;
        private int width;
        private int minWidth;
        private int height;
        private int minHeight;
        private String title;
        private String icon;
        private boolean resizable;
        private boolean maximized;
        private boolean fullScreen;
        private boolean alwaysOnTop;
//        private boolean fullScreenExitHint;
//        private boolean fullScreenExitKeyCombination;
//        private boolean fullScreenExitHintVisible;
//        private boolean fullScreenExitKeyCombinationVisible;
//        private boolean fullScreenExitHintVisibleSet;
//        private boolean fullScreenExitKeyCombinationVisibleSet;
//        private boolean fullScreenExitHintSet;
//        private boolean fullScreenExitKeyCombinationSet;

        public Builder() {
            this.escenario = null;
            this.mainClass = null;
            this.URLResource = null;
            this.showScene = false;
            this.width = 0;
            this.minWidth = 0;
            this.height = 0;
            this.minHeight = 0;
            this.title = "";
            this.icon = "";
            this.resizable = true;
            this.maximized = false;
            this.fullScreen = false;
            this.alwaysOnTop = false;
        }

        public Builder escenario(Escenario escenario) {
            this.escenario = escenario;
            return this;
        }
        public Builder mainClass(Class mainClass) {
            this.mainClass = mainClass;
            return this;
        }

        public Builder URLResource(String URLResource) {
            this.URLResource = URLResource;
            return this;
        }

        public Builder showScene(boolean showScene) {
            this.showScene = showScene;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder minWidth(int minWidth) {
            this.minWidth = minWidth;
            return this;
        }

        public Builder minHeight(int minHeight) {
            this.minHeight = minHeight;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public Builder resizable(boolean resizable) {
            this.resizable = resizable;
            return this;
        }

        public Builder maximized(boolean maximized) {
            this.maximized = maximized;
            return this;
        }

        public Builder fullScreen(boolean fullScreen) {
            this.fullScreen = fullScreen;
            return this;
        }

        public Builder alwaysOnTop(boolean alwaysOnTop) {
            this.alwaysOnTop = alwaysOnTop;
            return this;
        }

        public Scene build() {
            // Cargador del FXML de la escena
            FXMLLoader fxmlLoader = null;
            if (mainClass != null && URLResource != null) {
                fxmlLoader = new FXMLLoader(mainClass.getResource(URLResource));
            }

            // Carga la escena a partir del FXML, y la configura
            Scene scene = null;
            if (fxmlLoader != null) {
                try {
                    if (width == 0 || height == 0) {
                        scene = new Scene(fxmlLoader.load());
                    } else {
                        scene = new Scene(fxmlLoader.load(), width, height);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (width == 0 || height == 0) {
                    scene = escenario.escenario.getScene();
                } else {
                    scene = escenario.escenario.getScene();
                    scene.getWindow().setWidth(width);
                    scene.getWindow().setHeight(height);
                }
            }

            // Carga el controlador de la escena
            if (fxmlLoader != null) {
                IController controller = fxmlLoader.getController();
                if (controller != null) {
                    // Pasa el stage y las propiedades a la escena (esto es por si en el futuro se necesitase cambiar de escena)
                    controller.setStage(escenario);
                }
            }

            // Fija el tamaño minimo de la ventana
            escenario.escenario.setMinWidth(minWidth);
            escenario.escenario.setMinHeight(minHeight);

            escenario.escenario.setTitle(title);
            escenario.escenario.setResizable(resizable);
            escenario.escenario.setMaximized(maximized);
            escenario.escenario.setFullScreen(fullScreen);
            escenario.escenario.setAlwaysOnTop(alwaysOnTop);

            // Establece el icono de la ventana
            if (icon != null && !icon.isEmpty()) {
                if (mainClass == null) {
                    mainClass = getClass();
                }
                escenario.escenario.getIcons().add(new Image(mainClass.getResource(icon).toString()));
            }

            // Pone la escena dentro del escenario y la muestra
            if (showScene) {
                escenario.setEscena(scene);
                escenario.escenario.show();
            }

            // Devuelve la escena
            return scene;
        }
    }
}
