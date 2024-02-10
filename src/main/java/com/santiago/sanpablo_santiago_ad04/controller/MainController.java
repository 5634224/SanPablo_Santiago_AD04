package com.santiago.sanpablo_santiago_ad04.controller;

import com.santiago.sanpablo_santiago_ad04.Escenario;
import com.santiago.sanpablo_santiago_ad04.IController;
import com.santiago.sanpablo_santiago_ad04.JavaFXUtil;
import entity.EquipoacbEntity;
import entity.JugadoracbEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable, IController {
    private Escenario stage;
    private EntityManagerFactory emf;
    private EntityManager em;
    private ObservableList<EquipoacbEntity> equipos;
    private EquipoacbEntity equipoActual;
    private ObservableList<JugadoracbEntity> jugadores;
    private JugadoracbEntity jugadorActual;
    private ModoOperacion modoOperacionEquipos;
    private ModoOperacion modoOperacionJugadores;

    @FXML
    private TextField txtJugadores_pos;
    @FXML
    private Button btnModificarEquipo;
    @FXML
    private Button btnEliminarEquipo;
    @FXML
    private TableView tbvEquipos;
    @FXML
    private Button btnJugadores_Aceptar;
    @FXML
    private Button btnModificarJugador;
    @FXML
    private ComboBox comboBoxJugadores_Equipo;
    @FXML
    private Button btnBuscarJugadores;
    @FXML
    private Pane pnEquipos_BotonesAccion;
    @FXML
    private TextField txtJugadores_id;
    @FXML
    private Button btnEquipos_Cancelar;
    @FXML
    private Button btnCrearJugador;
    @FXML
    private Button btnEquipos_Aceptar;
    @FXML
    private Pane pnJugadores_BotonesAccion;
    @FXML
    private Button btnEliminarJugador;
    @FXML
    private Button btnBuscarEquipos;
    @FXML
    private TextField txtEquipos_id;
    @FXML
    private Button btnCrearEquipo;
    @FXML
    private TextField txtJugadores_nombreJugador;
    @FXML
    private TextField txtEquipos_nombreEquipo;
    @FXML
    private TableView tbvJugadores;
    @FXML
    private Button btnJugadores_Cancelar;
    @FXML
    private TableColumn colJugadorEquipo;
    @FXML
    private TableColumn colJugadorNombre;
    @FXML
    private TableColumn colEquiposId;
    @FXML
    private TableColumn colJugadorPos;
    @FXML
    private TableColumn colEquiposNombre;
    @FXML
    private TableColumn colJugadorId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Inicializa el EntityManager
            emf = Persistence.createEntityManagerFactory("default");
            em = emf.createEntityManager();

            // Inicializa los modos de operación
            modoOperacionEquipos = ModoOperacion.NINGUNO;
            modoOperacionJugadores = ModoOperacion.NINGUNO;

            // Inicializa las observableList
            equipos = FXCollections.observableArrayList();
            jugadores = FXCollections.observableArrayList();

            // Asigna las observableList a los componentes de la vista
            tbvEquipos.setItems(equipos);
            tbvJugadores.setItems(jugadores);
            comboBoxJugadores_Equipo.setItems(equipos);

            // Configura el combobox
            comboBoxJugadores_Equipo.setCellFactory(param -> new ListCell<EquipoacbEntity>() {
                @Override
                protected void updateItem(EquipoacbEntity item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else if (item.getNombreE() == null) {
                        setText(String.valueOf(item.getIdEquipo()));
                    } else {
                        setText(item.getNombreE() + " (" + item.getIdEquipo() + ")");
                    }
                }
            });
            comboBoxJugadores_Equipo.setButtonCell(new ListCell<EquipoacbEntity>() {
                @Override
                protected void updateItem(EquipoacbEntity item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                    } else if (item.getNombreE() == null) {
                        setText(String.valueOf(item.getIdEquipo()));
                    } else {
                        setText(item.getNombreE() + " (" + item.getIdEquipo() + ")");
                    }
                }
            });

            // Configura las columnas de las tablas
            colEquiposId.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
            colEquiposNombre.setCellValueFactory(new PropertyValueFactory<>("nombreE"));

            colJugadorId.setCellValueFactory(new PropertyValueFactory<>("idJugador"));
            colJugadorNombre.setCellValueFactory(new PropertyValueFactory<>("nombreJ"));
            colJugadorPos.setCellValueFactory(new PropertyValueFactory<>("pos"));
            colJugadorEquipo.setCellValueFactory(new PropertyValueFactory<>("equipoacbByIdEquipo"));

            // Configuracion de los TableView
            tbvEquipos.getSelectionModel().selectedItemProperty().addListener(this::handleEquiposSelectionChanged);
            tbvJugadores.getSelectionModel().selectedItemProperty().addListener(this::handleJugadoresSelectionChanged);

            // Llena las listas de equipos y jugadores
            equipos.setAll(getAllEquipos());
            jugadores.setAll(getAllJugadores());

            // Deshabilita los campos de edición
            manageCamposEquipos(false);
            manageCamposJugadores(false);
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al inicializar la aplicación", e.getMessage());
        }
    }

    public void setStage(Escenario stage) {
        this.stage = stage;
    }

    private void handleEquiposSelectionChanged(ObservableValue observableValue, Object o, Object o1) {
        if (modoOperacionEquipos == ModoOperacion.CREAR) return;
        try {
            EquipoacbEntity newSelection = (EquipoacbEntity) o1;
            if (newSelection != null) {
                txtEquipos_id.setText(String.valueOf(newSelection.getIdEquipo()));
                txtEquipos_nombreEquipo.setText(newSelection.getNombreE());
            }
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al seleccionar un equipo", e.getMessage());
        }
    }

    private void handleJugadoresSelectionChanged(ObservableValue observableValue, Object o, Object o1) {
        if (modoOperacionJugadores == ModoOperacion.CREAR) return;
        try {
            JugadoracbEntity newSelection = (JugadoracbEntity) o1;
            if (newSelection != null) {
                txtJugadores_id.setText(String.valueOf(newSelection.getIdJugador()));
                txtJugadores_nombreJugador.setText(String.valueOf(newSelection.getNombreJ()));
                txtJugadores_pos.setText(newSelection.getPos());
                comboBoxJugadores_Equipo.getSelectionModel().select(newSelection.getEquipoacbByIdEquipo());
            }
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al seleccionar un jugador", e.getMessage());
        }
    }

    private void manageCamposEquipos(boolean habilitar) {
//        txtEquipos_id.setDisable(!habilitar);
        btnBuscarEquipos.setDisable(!habilitar ? false : btnBuscarEquipos.isDisable());
        txtEquipos_nombreEquipo.setEditable(habilitar);
        pnEquipos_BotonesAccion.setVisible(habilitar);

        // Limpia los campos
        txtEquipos_id.clear();
        txtEquipos_nombreEquipo.clear();
    }

    private void manageCamposJugadores(boolean habilitar) {
//        txtJugadores_id.setDisable(!habilitar);
        btnBuscarJugadores.setDisable(!habilitar ? false : btnBuscarJugadores.isDisable());
        txtJugadores_nombreJugador.setEditable(habilitar);
        txtJugadores_pos.setEditable(habilitar);
        comboBoxJugadores_Equipo.setDisable(!habilitar);
        pnJugadores_BotonesAccion.setVisible(habilitar);

        // Limpia los campos
        txtJugadores_id.clear();
        txtJugadores_nombreJugador.clear();
    }

    private List<EquipoacbEntity> getAllEquipos() {
        try {
            // Inicia la transacción
            em.getTransaction().begin();

            // Crea un TypedQuery
            //        TypedQuery<EquipoacbEntity> query = em.createQuery(
//            "SELECT e FROM EquipoacbEntity e", EquipoacbEntity.class);
            TypedQuery<EquipoacbEntity> query = em.createNamedQuery("EquipoacbEntity.findAll", EquipoacbEntity.class);

            // Ejecuta la consulta y obtiene la lista de equipos
            List<EquipoacbEntity> equipos = query.getResultList();

            // Commit transacción
            em.getTransaction().commit();

            // Devuelve la lista de equipos
            return equipos;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al obtener los equipos", e.getMessage());
            return null;
        }
    }

    private List<JugadoracbEntity> getAllJugadores() {
        try {
            // Inicia la transacción
            em.getTransaction().begin();

            // Crea un TypedQuery
            TypedQuery<JugadoracbEntity> query = em.createNamedQuery("JugadoracbEntity.findAll", JugadoracbEntity.class);

            // Ejecuta la consulta y obtiene la lista de jugadores
            List<JugadoracbEntity> jugadores = query.getResultList();

            // Commit transacción
            em.getTransaction().commit();

            // Devuelve la lista de jugadores
            return jugadores;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al obtener los jugadores", e.getMessage());
            return null;
        }
    }

    private int getLastIdEquipo() {
        try {
            // Inicia la transacción
            em.getTransaction().begin();

            // Crea un TypedQuery
            TypedQuery<Integer> query = em.createNamedQuery("EquipoacbEntity.lastId", Integer.class);

            // Ejecuta la consulta y obtiene el último id
            int lastId = query.getSingleResult();

            // Commit transacción
            em.getTransaction().commit();

            // Devuelve el último id
            return lastId;
        } catch (Exception e) {
//            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al obtener el último id de equipo", e.getMessage());
            System.err.println(e.getMessage());
            return -1;
        }
    }

    private void commitTransaction() {
        try {
            // Commit transacción
            if (em.getTransaction().isActive())
                em.getTransaction().commit();

            // Actualiza la lista de equipos
            equipos.setAll(getAllEquipos());

            // Actualiza la lista de jugadores
            jugadores.setAll(getAllJugadores());
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al confirmar la transacción", e.getMessage());
        }
    }

    private void rollbackTransaction() {
        try {
            // Rollback transacción
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();

            // Actualiza la lista de equipos
            equipos.setAll(getAllEquipos());

            // Actualiza la lista de jugadores
            jugadores.setAll(getAllJugadores());
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al deshacer la transacción", e.getMessage());
        }
    }

    @FXML
    public void btnEquiposAceptar_onAction(ActionEvent actionEvent) {
        try {
            // Obtiene el equipo seleccionado
//            EquipoacbEntity equipo = (EquipoacbEntity) tbvEquipos.getSelectionModel().getSelectedItem();
            EquipoacbEntity equipo = equipoActual;

            // Si el equipo es nulo, no hace nada
            if (equipo == null) return;

            // Asigna los datos del equipo
            equipo.setIdEquipo(Integer.parseInt(txtEquipos_id.getText()));
            equipo.setNombreE(txtEquipos_nombreEquipo.getText());

            // Si el modo de operación es crear, inserta el equipo en la base de datos
            if (modoOperacionEquipos == ModoOperacion.CREAR) {
                em.getTransaction().begin();
                em.persist(equipo);
//                em.getTransaction().commit();
            }

            // Si el modo de operación es modificar, actualiza el equipo en la base de datos
            if (modoOperacionEquipos == ModoOperacion.MODIFICAR) {
                em.getTransaction().begin();
                em.merge(equipo);
//                em.getTransaction().commit();
            }

            // Si el modo de operación es eliminar, elimina el equipo de la base de datos
            if (modoOperacionEquipos == ModoOperacion.ELIMINAR) {
                em.getTransaction().begin();
                em.remove(equipo);
//                em.getTransaction().commit();
            }

            // Commit transacción
            commitTransaction();

            // Selecciona el equipo en la tabla
            tbvEquipos.getSelectionModel().select(equipo);

            // Muestra el equipo en la tabla
            tbvEquipos.scrollTo(equipo);

            // Cambia el modo de operación a ninguno
            modoOperacionEquipos = ModoOperacion.NINGUNO;

            // Habilita los botones de acción
            btnBuscarEquipos.setDisable(false);

            // Cambia el texto del botón de aceptar
            btnEquipos_Aceptar.setText("Aceptar");

            // Deshabilita los campos de edición
            manageCamposEquipos(false);
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al confirmar la transacción", e.getMessage());
            System.err.println(e.getMessage());
            em.getTransaction().rollback();
        }
    }

    @FXML
    public void btnEquiposCancelar_onAction(ActionEvent actionEvent) {
        rollbackTransaction();
        manageCamposEquipos(false);
        modoOperacionEquipos = ModoOperacion.NINGUNO;
    }

    @FXML
    public void btnJugadoresAceptar_onAction(ActionEvent actionEvent) {
        commitTransaction();
        manageCamposJugadores(false);
        modoOperacionEquipos = ModoOperacion.NINGUNO;
    }

    @FXML
    public void btnJugadoresCancelar_onAction(ActionEvent actionEvent) {
        rollbackTransaction();
        manageCamposJugadores(false);
        modoOperacionEquipos = ModoOperacion.NINGUNO;
    }

    @FXML
    public void btnEquiposBuscar_onAction(ActionEvent actionEvent) {
        // Obtiene el id del equipo
        int id = Integer.parseInt(txtEquipos_id.getText());

        // Inicia la transacción
        em.getTransaction().begin();

        // Busca el equipo por id
        EquipoacbEntity equipo = em.find(EquipoacbEntity.class, id);

        // Commit transacción
        em.getTransaction().commit();

        // Si el equipo es nulo, no hace nada
        if (equipo == null) return;

        // Selecciona el equipo en la tabla
        tbvEquipos.getSelectionModel().select(equipo);

        // Muestra el equipo en la tabla
        tbvEquipos.scrollTo(equipo);

        // Carga el equipo en los campos de edición
//        txtEquipos_id.setText(String.valueOf(equipo.getIdEquipo()));
//        txtEquipos_nombreEquipo.setText(equipo.getNombreE());
    }

    @FXML
    public void txtEquiposId_onAction(ActionEvent actionEvent) {
        btnEquiposBuscar_onAction(actionEvent);
    }

    @FXML
    public void btnCrearEquipo_onAction(ActionEvent actionEvent) {
        manageCamposEquipos(true);
        btnBuscarEquipos.setDisable(true);
        btnEquipos_Aceptar.setText("Crear");

        // Crea un nuevo equipo
        equipoActual = new EquipoacbEntity();
        equipoActual.setIdEquipo(getLastIdEquipo() + 1);

        // Asigna el nuevo equipo a la lista de equipos
        equipos.add(equipoActual);

        // Asigna el nuevo equipo a la tabla de equipos
        tbvEquipos.getSelectionModel().select(equipoActual);

        // Muestra el nuevo equipo en la tabla de equipos
        tbvEquipos.scrollTo(equipoActual);

        // Cambia el modo de operación a crear
        modoOperacionEquipos = ModoOperacion.CREAR;
    }

    @FXML
    public void btnModificarEquipo_onAction(ActionEvent actionEvent) {

    }

    @FXML
    public void btnEliminarEquipo_onAction(ActionEvent actionEvent) {
        // Cambia el modo de operación a eliminar
        modoOperacionEquipos = ModoOperacion.ELIMINAR;

        // Deshabilita los botones de acción
        btnBuscarEquipos.setDisable(true);

        // Cambia el texto del botón de aceptar
        btnEquipos_Aceptar.setText("Eliminar");

        // Habilita los campos de edición
        manageCamposEquipos(true);

        // Cambia el modo de operación a eliminar
        modoOperacionEquipos = ModoOperacion.ELIMINAR;
    }

    @FXML
    public void btnBuscarJugadores_onAction(ActionEvent actionEvent) {
        try {
            // Obtiene el id del jugador
            int id = Integer.parseInt(txtJugadores_id.getText());

            // Inicia la transacción
            em.getTransaction().begin();

            // Busca el jugador por id
            JugadoracbEntity jugador = em.find(JugadoracbEntity.class, id);

            // Commit transacción
            em.getTransaction().commit();

            // Si el jugador es nulo, no hace nada
            if (jugador == null) return;

            // Selecciona el jugador en la tabla
            tbvJugadores.getSelectionModel().select(jugador);

            // Muestra el jugador en la tabla
            tbvJugadores.scrollTo(jugador);

            // Carga el jugador en los campos de edición
//            txtJugadores_id.setText(String.valueOf(jugador.getIdJugador()));
//            txtJugadores_nombreJugador.setText(String.valueOf(jugador.getNombreJ()));
//            txtJugadores_pos.setText(jugador.getPos());
//            comboBoxJugadores_Equipo.getSelectionModel().select(jugador.getEquipoacbByIdEquipo());
        } catch (NumberFormatException e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "ID incorrecto", "El id del jugador debe ser un número entero");
            return;
        }
    }

    @FXML
    public void txtJugadoresId_onAction(ActionEvent actionEvent) {
        btnBuscarJugadores_onAction(actionEvent);
    }

    @FXML
    public void btnCrearJugador_onAction(ActionEvent actionEvent) {
        manageCamposJugadores(true);
        btnBuscarJugadores.setDisable(true);
        btnJugadores_Aceptar.setText("Crear");
    }

    @FXML
    public void btnModificarJugador_onAction(ActionEvent actionEvent) {

    }

    @FXML
    public void btnEliminarJugador_onAction(ActionEvent actionEvent) {

    }
}