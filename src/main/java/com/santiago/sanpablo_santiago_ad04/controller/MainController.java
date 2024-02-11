package com.santiago.sanpablo_santiago_ad04.controller;

import com.santiago.sanpablo_santiago_ad04.Escenario;
import com.santiago.sanpablo_santiago_ad04.IController;
import com.santiago.sanpablo_santiago_ad04.JavaFXUtil;
import entity.EquipoacbEntity;
import entity.JugadoracbEntity;
import jakarta.persistence.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;

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
    private boolean cambioEquipo; // Flag para detectar si se ha cambiado de equipo
    private EquipoacbEntity anteriorEquipo; // Equipo anterior al cambio

    @FXML
    private TextField txtJugadores_pos;
    @FXML
    private Button btnModificarEquipo;
    @FXML
    private Button btnEliminarEquipo;
    @FXML
    private TableView tbvEquipos;
    @FXML
    private AnchorPane pnEquipos_BotonesAccion;
    @FXML
    private Button btnEquipos_Aceptar;
    @FXML
    private Button btnEquipos_Cancelar;
    @FXML
    private Button btnJugadores_Aceptar;
    @FXML
    private Button btnModificarJugador;
    @FXML
    private ComboBox comboBoxJugadores_Equipo;
    @FXML
    private Button btnBuscarJugadores;
    @FXML
    private TextField txtJugadores_id;
    @FXML
    private Button btnCrearJugador;
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
    private AnchorPane pnJugadores_BotonesAccion;
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
    @FXML
    private Label lblEstadoAccionActualEquipos;
    @FXML
    private Label lblEstadoAccionActualJugadores;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Inicializa el EntityManager
            emf = Persistence.createEntityManagerFactory("default");
            em = emf.createEntityManager();

            // Inicializa los modos de operación
            modoOperacionEquipos = ModoOperacion.NINGUNO;
            lblEstadoAccionActualEquipos.setText(modoOperacionEquipos.getDescripcion());
            modoOperacionJugadores = ModoOperacion.NINGUNO;
            lblEstadoAccionActualJugadores.setText(modoOperacionJugadores.getDescripcion());

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
            colJugadorEquipo.setCellFactory(column -> {
                return new TableCell<JugadoracbEntity, EquipoacbEntity>() {
                    @Override
                    protected void updateItem(EquipoacbEntity item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getNombreE() + " (" + item.getIdEquipo() + ")");
                        }
                    }
                };
            });

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
                equipoActual = newSelection;
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
                anteriorEquipo = newSelection.getEquipoacbByIdEquipo();
                jugadorActual = newSelection;
                txtJugadores_id.setText(String.valueOf(newSelection.getIdJugador()));
                txtJugadores_nombreJugador.setText(newSelection.getNombreJ());
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
    }

    private void clearCamposEquipos() {
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
    }

    private void clearCamposJugadores() {
        txtJugadores_id.clear();
        txtJugadores_nombreJugador.clear();
        txtJugadores_pos.clear();
        comboBoxJugadores_Equipo.getSelectionModel().clearSelection();
    }

    private List<EquipoacbEntity> getAllEquipos() {
        try {
            // Actualiza el modo de operación a consultar
            modoOperacionEquipos = ModoOperacion.CONSULTAR;
            lblEstadoAccionActualEquipos.setText(modoOperacionEquipos.getDescripcion());

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
            // Actualiza el modo de operación a consultar
            modoOperacionJugadores = ModoOperacion.CONSULTAR;
            lblEstadoAccionActualJugadores.setText(modoOperacionJugadores.getDescripcion());

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
            Integer lastId = query.getSingleResult();

            // Commit transacción
            em.getTransaction().commit();

            // Devuelve el último id
            return lastId != null ? lastId : 0;
        } catch (Exception e) {
//            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al obtener el último id de equipo", e.getMessage());
            System.err.println(e.getMessage());
            return -1;
        }
    }

    private int getLastIdJugador() {
        try {
            // Inicia la transacción
            em.getTransaction().begin();

            // Crea un TypedQuery
            TypedQuery<Integer> query = em.createNamedQuery("JugadoracbEntity.lastId", Integer.class);

            // Ejecuta la consulta y obtiene el último id
            int lastId = query.getSingleResult();

            // Commit transacción
            em.getTransaction().commit();

            // Devuelve el último id
            return lastId != 0 ? lastId : 0;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al obtener el último id de jugador", e.getMessage());
            return -1;
        }
    }

    private boolean canDeleteEquipo(EquipoacbEntity equipo) {
        try {
            // Inicia la transacción
            em.getTransaction().begin();

            // Crea un TypedQuery
            TypedQuery<Long> query = em.createNamedQuery("EquipoacbEntity.canDelete", Long.class);
            query.setParameter("equipo", equipo);

            // Ejecuta la consulta y obtiene el número de jugadores asociados al equipo
            long count = query.getSingleResult();

            // Commit transacción
            em.getTransaction().commit();

            // Devuelve true si no hay jugadores asociados al equipo, false en caso contrario
            return count == 0;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al comprobar si se puede eliminar el equipo", e.getMessage());
            return false;
        }
    }

    private boolean commitTransaction() {
        try {
            // Commit transacción
            if (em.getTransaction().isActive())
                em.getTransaction().commit();

            // Actualiza la lista de equipos
            equipos.setAll(getAllEquipos());

            // Actualiza la lista de jugadores
            jugadores.setAll(getAllJugadores());

            return true;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al confirmar la transacción", e.getMessage());
            return false;
        }
    }

    private boolean rollbackTransaction() {
        try {
            // Rollback transacción
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();

            // Actualiza la lista de equipos
            equipos.setAll(getAllEquipos());

            // Actualiza la lista de jugadores
            jugadores.setAll(getAllJugadores());

            return true;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al deshacer la transacción", e.getMessage());
            return false;
        }
    }

    @FXML
    public void btnEquiposAceptar_onAction(ActionEvent actionEvent) {
        try {
            // Obtiene el equipo seleccionado
//            EquipoacbEntity equipo = (EquipoacbEntity) tbvEquipos.getSelectionModel().getSelectedItem();
            EquipoacbEntity equipo = equipoActual;

            // Si el equipo es nulo, no hace nada
            if (equipo == null) {
                JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Equipo no seleccionado", "Seleccione un equipo");
                return;
            }

            // Asigna los datos del equipo
            equipo.setIdEquipo(Integer.parseInt(txtEquipos_id.getText()));
            equipo.setNombreE(txtEquipos_nombreEquipo.getText());

            // Si el modo de operación es crear, inserta el equipo en la base de datos
            if (modoOperacionEquipos == ModoOperacion.CREAR) {
                em.getTransaction().begin();
                em.persist(equipo);
            }

            // Si el modo de operación es modificar, actualiza el equipo en la base de datos
            if (modoOperacionEquipos == ModoOperacion.MODIFICAR) {
                em.getTransaction().begin();
                em.merge(equipo);
            }

            // Si el modo de operación es eliminar, elimina el equipo de la base de datos
            if (modoOperacionEquipos == ModoOperacion.ELIMINAR) {
                if (!canDeleteEquipo(equipo)) {
                    JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "No se puede eliminar el equipo", "El equipo tiene jugadores asociados. Por favor, borra primero los jugadores asociados al equipo.");
                    return;
                }
                em.getTransaction().begin();
                em.remove(equipo);
//                clearCamposEquipos();
            }

            // Commit transacción
            if (!commitTransaction()) return;

            // Selecciona el equipo en la tabla
            if (modoOperacionEquipos != ModoOperacion.ELIMINAR) {
                tbvEquipos.getSelectionModel().select(equipo);
                // Muestra el equipo en la tabla
                tbvEquipos.scrollTo(equipo);
            }

            // Habilita los botones de acción
            btnBuscarEquipos.setDisable(false);

            // Cambia el texto del botón de aceptar
            btnEquipos_Aceptar.setText("Aceptar");

            // Deshabilita los campos de edición
            manageCamposEquipos(false);
            clearCamposEquipos();

            // Equipo actual = null
            equipoActual = null;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al confirmar la transacción", e.getMessage());
            System.err.println(e.getMessage());
            em.getTransaction().rollback();
        }
    }

    @FXML
    public void btnEquiposCancelar_onAction(ActionEvent actionEvent) {
        try {
            // Deshabilita los campos de edición
            manageCamposEquipos(false);
            if (modoOperacionEquipos == ModoOperacion.CREAR) clearCamposEquipos();

            // Rollback transacción
            if (!rollbackTransaction()) return;

            // Equipo actual = null
            equipoActual = null;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al deshacer la transacción", e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void btnJugadoresAceptar_onAction(ActionEvent actionEvent) {
        try {
            // Obtiene el jugador seleccionado
//            JugadoracbEntity jugador = (JugadoracbEntity) tbvJugadores.getSelectionModel().getSelectedItem();
            JugadoracbEntity jugador = jugadorActual;

            // Si el jugador es nulo, no hace nada
            if (jugador == null) {
                JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Jugador no seleccionado", "Seleccione un jugador");
                return;
            }

            // Asigna los datos del jugador
            jugador.setIdJugador(Integer.parseInt(txtJugadores_id.getText()));
            jugador.setNombreJ(txtJugadores_nombreJugador.getText());
            jugador.setPos(txtJugadores_pos.getText());
            jugador.setEquipoacbByIdEquipo((EquipoacbEntity) comboBoxJugadores_Equipo.getSelectionModel().getSelectedItem());

            // Obtiene el modo de operación
            ModoOperacion modoOperacionJugadores = this.modoOperacionJugadores;

            // Si el modo de operación es crear, inserta el jugador en la base de datos
            if (modoOperacionJugadores == ModoOperacion.CREAR) {
                em.getTransaction().begin();
                em.persist(jugador);
            }

            // Si el modo de operación es modificar, actualiza el jugador en la base de datos
            if (modoOperacionJugadores == ModoOperacion.MODIFICAR) {
                em.getTransaction().begin();
                em.merge(jugador);
            }

            // Si el modo de operación es eliminar, elimina el jugador de la base de datos
            if (modoOperacionJugadores == ModoOperacion.ELIMINAR) {
                em.getTransaction().begin();
                em.remove(jugador);
//                clearCamposJugadores();
            }

            // Commit transacción
            if (!commitTransaction()) return;

            // Selecciona el jugador en la tabla
            if (modoOperacionJugadores != ModoOperacion.ELIMINAR) {
                tbvJugadores.getSelectionModel().select(jugador);
                // Muestra el jugador en la tabla
                tbvJugadores.scrollTo(jugador);
            }

            // Habilita los botones de acción
            btnBuscarJugadores.setDisable(false);

            // Cambia el texto del botón de aceptar
            btnJugadores_Aceptar.setText("Aceptar");

            // Deshabilita los campos de edición
            manageCamposJugadores(false);
            clearCamposEquipos();

            // Jugador actual = null
            jugadorActual = null;

            // Si ha cambiado de equipo, emite un mensaje avisándole al usuario
            if (modoOperacionJugadores == ModoOperacion.MODIFICAR && cambioEquipo) {
                JavaFXUtil.alerta(Alert.AlertType.INFORMATION, "Información", "Cambio de equipo", "El jugador " + jugador.getNombreJ() + " ha sido transferido del equipo " + anteriorEquipo.getNombreE() + " (" + anteriorEquipo.getIdEquipo() + ") al equipo " + jugador.getEquipoacbByIdEquipo().getNombreE() + " (" + jugador.getEquipoacbByIdEquipo().getIdEquipo() + ")");
                cambioEquipo = false;
                anteriorEquipo = null;
            }
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al confirmar la transacción", e.getMessage());
            System.err.println(e.getMessage());
            em.getTransaction().rollback();
        }
    }

    @FXML
    public void btnJugadoresCancelar_onAction(ActionEvent actionEvent) {
        try {
            // Deshabilita los campos de edición
            manageCamposJugadores(false);
            if (modoOperacionJugadores == ModoOperacion.CREAR) clearCamposJugadores();

            // Rollback transacción
            if (!rollbackTransaction()) return;

            // Jugador actual = null
            jugadorActual = null;
        } catch (Exception e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "Error al deshacer la transacción", e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void btnEquiposBuscar_onAction(ActionEvent actionEvent) {
        try {
            // Si el id del equipo es nulo, no hace nada
            if (txtEquipos_id.getText().isEmpty()) {
                txtEquipos_nombreEquipo.clear();
                return;
            }

            // Obtiene el id del equipo
            int id = Integer.parseInt(txtEquipos_id.getText());

            // Inicia la transacción
            em.getTransaction().begin();

            // Busca el equipo por id
            EquipoacbEntity equipo = em.find(EquipoacbEntity.class, id);

            // Commit transacción
            em.getTransaction().commit();

            // Si el equipo es nulo, vacía los campos y no hace nada
            if (equipo == null) {
                txtEquipos_nombreEquipo.clear();
                return;
            }

            // Selecciona el equipo en la tabla
            tbvEquipos.getSelectionModel().clearSelection();
            tbvEquipos.getSelectionModel().select(equipo); // Selecciona el equipo en la tabla y lo guarda en equipoActual

            // Muestra el equipo en la tabla
            tbvEquipos.scrollTo(equipo);

            // Carga el equipo en los campos de edición
            //        txtEquipos_id.setText(String.valueOf(equipo.getIdEquipo()));
            //        txtEquipos_nombreEquipo.setText(equipo.getNombreE());
        } catch (NumberFormatException e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "ID incorrecto", "El id del equipo debe ser un número entero");
        }
    }

    @FXML
    public void txtEquiposId_onAction(ActionEvent actionEvent) {
        btnEquiposBuscar_onAction(actionEvent);
    }

    @FXML
    public void btnCrearEquipo_onAction(ActionEvent actionEvent) {
        manageCamposEquipos(true); // Habilita los campos de edición
        clearCamposEquipos();
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
        lblEstadoAccionActualEquipos.setText(modoOperacionEquipos.getDescripcion());
    }

    @FXML
    public void btnModificarEquipo_onAction(ActionEvent actionEvent) {
        manageCamposEquipos(true); // Habilita los campos de edición
        btnBuscarEquipos.setDisable(false);
        btnEquipos_Aceptar.setText("Modificar");

        // Cambia el modo de operación a modificar
        modoOperacionEquipos = ModoOperacion.MODIFICAR;
        lblEstadoAccionActualEquipos.setText(modoOperacionEquipos.getDescripcion());
    }

    @FXML
    public void btnEliminarEquipo_onAction(ActionEvent actionEvent) {
        //manageCamposEquipos(true); // Habilita los campos de edición
        pnEquipos_BotonesAccion.setVisible(true);
        btnBuscarEquipos.setDisable(false);
        btnEquipos_Aceptar.setText("Eliminar");

        // Cambia el modo de operación a eliminar
        modoOperacionEquipos = ModoOperacion.ELIMINAR;
        lblEstadoAccionActualEquipos.setText(modoOperacionEquipos.getDescripcion());
    }

    @FXML
    public void btnBuscarJugadores_onAction(ActionEvent actionEvent) {
        try {
            // Si el id del equipo es nulo, no hace nada
            if (txtJugadores_id.getText().isEmpty()) {
                txtJugadores_nombreJugador.clear();
                txtJugadores_pos.clear();
                comboBoxJugadores_Equipo.getSelectionModel().clearSelection();
                return;
            }

            // Obtiene el id del jugador
            int id = Integer.parseInt(txtJugadores_id.getText());

            // Inicia la transacción
            em.getTransaction().begin();

            // Busca el jugador por id
            JugadoracbEntity jugador = em.find(JugadoracbEntity.class, id);

            // Commit transacción
            em.getTransaction().commit();

            // Si el jugador es nulo, no hace nada
            if (jugador == null) {
                txtJugadores_nombreJugador.clear();
                txtJugadores_pos.clear();
                comboBoxJugadores_Equipo.getSelectionModel().clearSelection();
                return;
            }

            // Selecciona el jugador en la tabla
            tbvJugadores.getSelectionModel().clearSelection();
            tbvJugadores.getSelectionModel().select(jugador); // Selecciona el jugador en la tabla y lo guarda en jugadorActual

            // Muestra el jugador en la tabla
            tbvJugadores.scrollTo(jugador);

            // Carga el jugador en los campos de edición
//            txtJugadores_id.setText(String.valueOf(jugador.getIdJugador()));
//            txtJugadores_nombreJugador.setText(String.valueOf(jugador.getNombreJ()));
//            txtJugadores_pos.setText(jugador.getPos());
//            comboBoxJugadores_Equipo.getSelectionModel().select(jugador.getEquipoacbByIdEquipo());
        } catch (NumberFormatException e) {
            JavaFXUtil.alerta(Alert.AlertType.ERROR, "Error", "ID incorrecto", "El id del jugador debe ser un número entero");
        }
    }

    @FXML
    public void txtJugadoresId_onAction(ActionEvent actionEvent) {
        btnBuscarJugadores_onAction(actionEvent);
    }

    @FXML
    public void btnCrearJugador_onAction(ActionEvent actionEvent) {
        manageCamposJugadores(true);
        clearCamposJugadores();
        btnBuscarJugadores.setDisable(true);
        btnJugadores_Aceptar.setText("Crear");

        // Crea un nuevo jugador
        jugadorActual = new JugadoracbEntity();

        // Asigna el nuevo jugador a la lista de jugadores
        jugadores.add(jugadorActual);

        // Asigna el nuevo jugador a la tabla de jugadores
        tbvJugadores.getSelectionModel().select(jugadorActual);

        // Muestra el nuevo jugador en la tabla de jugadores
        tbvJugadores.scrollTo(jugadorActual);

        // Cambia el modo de operación a crear
        modoOperacionJugadores = ModoOperacion.CREAR;
        lblEstadoAccionActualJugadores.setText(modoOperacionJugadores.getDescripcion());
    }

    @FXML
    public void btnModificarJugador_onAction(ActionEvent actionEvent) {
        manageCamposJugadores(true);
        btnBuscarJugadores.setDisable(false);
        btnJugadores_Aceptar.setText("Modificar");
        cambioEquipo = false;

        // Cambia el modo de operación a modificar
        modoOperacionJugadores = ModoOperacion.MODIFICAR;
        lblEstadoAccionActualJugadores.setText(modoOperacionJugadores.getDescripcion());
    }

    @FXML
    public void comboBoxJugadoresEquipo_onAction(ActionEvent actionEvent) {
        cambioEquipo = true;
    }

    @FXML
    public void btnEliminarJugador_onAction(ActionEvent actionEvent) {
//        manageCamposJugadores(true);
        pnJugadores_BotonesAccion.setVisible(true);
        btnBuscarJugadores.setDisable(false);
        btnJugadores_Aceptar.setText("Eliminar");

        // Cambia el modo de operación a eliminar
        modoOperacionJugadores = ModoOperacion.ELIMINAR;
        lblEstadoAccionActualJugadores.setText(modoOperacionJugadores.getDescripcion());
    }
}