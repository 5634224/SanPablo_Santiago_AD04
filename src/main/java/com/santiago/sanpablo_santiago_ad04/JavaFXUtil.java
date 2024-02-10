/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.santiago.sanpablo_santiago_ad04;

import java.util.Optional;
import java.util.function.UnaryOperator;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

/**
 * Clase que modela algunos metodos utiles a la hora de desarrollar aplicaciones JavaFX
 * @author Santiago Francisco San Pablo Raposo.
 * @version old 21.05.2022
 * @version new 29.11.2023
 */
public abstract class JavaFXUtil {

    /**
     * Metodo que sirve pare restringir los TextFields a numeros.
     * @param textField que se quiere restringir
     */
    public static void integerTextField(TextField textField) {
        //Gracias a: https://javiergarciaescobedo.es/programacion-en-java/96-javafx/458-textfield-de-javafx-limitado-para-numeros-enteros
        UnaryOperator<Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            //System.out.println("newText " + newText);
            if (newText.matches("-?([0-9]*)?")) {
                //System.out.println("cumple regla");
                return change;
            }
            //System.out.println("No cumple regla");
            return null;
        };
        textField.setTextFormatter(
                new TextFormatter<Integer>(
                        new IntegerStringConverter(), 0, integerFilter));
    }

    /**
     * Metodo que sirve para emitir una ventana de alerta al usuario
     * @param tipoAlerta Elige el tipo de alerta.
     * @param tituloAlerta Elige un titulo para la alerta.
     * @param cabeceraAlerta Elige una cabecera para la alerta.
     * @param contenidoAlerta Elige una descripcion para la alerta.
     * @param tipoBotones Elige los botones que tendrá la ventana de alerta (OK, Yes, No...)
     * @return El botón de la alerta que haya pulsado el usuario
     */
    public static Optional<ButtonType> alerta(Alert.AlertType tipoAlerta, String tituloAlerta, String cabeceraAlerta, String contenidoAlerta, ButtonType... tipoBotones) {
        Alert a = new Alert(tipoAlerta, contenidoAlerta, tipoBotones);
        a.setTitle(tituloAlerta);
        a.setHeaderText(cabeceraAlerta);
        return a.showAndWait();
    }

    /**
     * Método que sirve para emitir una ventana de input al usuario
     */
    public static Optional<String> input(String titulo, String cabecera, String contenido) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(cabecera);
        dialog.setContentText(contenido);

        return dialog.showAndWait();
    }

    /**
     * Metodo que sirve para emitir una ventana de alerta al usuario
     * @param tipoAlerta Elige el tipo de alerta.
     * @param tituloAlerta Elige un titulo para la alerta.
     * @param contenidoAlerta Elige una descripcion para la alerta.
     * @param tipoBotones Elige los botones que tendrá la ventana de alerta (OK, Yes, No...)
     * @return
     */
    public static Optional<ButtonType> alerta(Alert.AlertType tipoAlerta, String tituloAlerta, String contenidoAlerta, ButtonType... tipoBotones) {
        return alerta(tipoAlerta, tituloAlerta, null, contenidoAlerta, tipoBotones);
    }

    /**
     * Método que sirve para configurar un TableView con una serie de filas y columnas
     * @param tableView Especifica el TableView que se quiere configurar
     * @param numColumns Especifica el número de columnas que se quieren crear
     * @param numRows Especifica el número de filas que se quieren crear
     */
    public static void setupTableView(TableView<String[]> tableView, int numColumns, int numRows) {
        // Crear columnas
        for (int i = 0; i < numColumns; i++) {
            TableColumn<String[], String> column = new TableColumn<>(generateColumnName(i));
            final int columnIndex = i;

            // Establecer ancho predeterminado (100 px)
            column.setPrefWidth(100);

            // Habilitar la edición de celdas
            column.setEditable(true);

            // Definir la celda de la columna
            column.setCellValueFactory(cellData -> {
                String[] row = cellData.getValue();
                if (row != null && row.length > columnIndex) {
                    return new SimpleStringProperty(row[columnIndex]);
                } else {
                    return new SimpleStringProperty("");
                }
            });

            // Hace las celdas editables haciendo que al hacer doble clic sobre ellas se abra un TextField
            column.setCellFactory(TextFieldTableCell.forTableColumn());

            tableView.getColumns().add(column);
        }

        // Agregar filas
        for (int i = 1; i <= numRows; i++) {
            String[] rowData = new String[numColumns];
            for (int j = 0; j < numColumns; j++) {
//                rowData[j] = "Fila " + i + ", Columna " + getColumnName(j);
                rowData[j] = "";
            }
            tableView.getItems().add(rowData);
        }
    }

    /**
     * Método que sirve para generar el nombre de una columna a partir de su índice
     * @param columnIndex Especifica el índice de la columna
     * @return El nombre de la columna generado.
     */
    private static String generateColumnName(int columnIndex) {
        StringBuilder columnName = new StringBuilder();

        while (columnIndex >= 0) {
            columnName.insert(0, (char) ('A' + columnIndex % 26));
            columnIndex = columnIndex / 26 - 1;
        }

        return columnName.toString();
    }
}
