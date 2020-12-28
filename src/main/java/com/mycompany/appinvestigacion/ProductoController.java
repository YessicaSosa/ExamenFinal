/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appinvestigacion;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author yessica
 */
public class ProductoController implements Initializable {

    private static final Logger LOG = Logger.getLogger(MarcaController.class.getName());
    //Declaración de la clase en donde se encuentra la conexión a la base de datos
    @FXML
    private ComboBox<String> CombIva;
    @FXML
    private ComboBox<Marca> CombMarca;

    private ConexionBD conex;
    @FXML

    private TableView<Producto> tblDatos;
    @FXML
    private TableColumn<Producto, Integer> colCodigo;
    @FXML
    private TableColumn<Producto, String> colDescripcion;
    @FXML
    private TableColumn<Producto, Integer> colIVA;
    @FXML
    private TableColumn<Producto, Integer> colPrecio;
    @FXML
    private TableColumn<Producto, Integer> colMarca;
    @FXML
    private TableColumn<Producto, Integer> colStock;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtPrecio;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField txtStock;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        this.colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        this.colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        this.colIVA.setCellValueFactory(new PropertyValueFactory<>("iva"));
        this.colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

        //Establecer la forma en el que el combo va a mostrar los ítems en el menú
        this.CombMarca.setCellFactory((ListView<Marca> l) -> {

            return new ListCell<Marca>() {
                @Override
                protected void updateItem(Marca m, boolean empty) {
                    if (!empty) {
                        this.setText(" (" + m.getCodigo() + ") " + m.getDescripcion());
                    } else {
                        this.setText("");
                    }
                    super.updateItem(m, empty);
                }
            };
        });

        //Establecer la forma en que el combo va a mostrar la marca seleccioinada
        this.CombMarca.setButtonCell(new ListCell<Marca>() {
            @Override
            protected void updateItem(Marca m, boolean empty) {
                if (!empty) {
                    this.setText("(" + m.getCodigo() + ") " + m.getDescripcion());
                } else {
                    this.setText("");
                }
                super.updateItem(m, empty);
            }
        }
        );

        //Cargar los posibles valores en el combo de IVA
        this.CombIva.getItems().add("10%");
        this.CombIva.getItems().add("5%");
        this.CombIva.getItems().add("Excento");
        this.CombIva.getSelectionModel().selectFirst();

        //Se crea la conexion a la base de datos con la clase creada para el efecto
        this.conex = new ConexionBD();
        //Invocamos al metodo que trae los registros de la tabla marca para cargar en el combo
        this.cargarMarcas();
        this.cargarDatos();

        this.tblDatos.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Producto> obs, Producto valorAnterior, Producto valorNuevo) -> {
            if (valorNuevo != null) {
                this.txtCodigo.setText(valorNuevo.getCodigo().toString());
                this.txtDescripcion.setText(valorNuevo.getDescripcion());
                this.txtPrecio.setText(valorNuevo.getPrecio().toString());
                this.txtStock.setText(valorNuevo.getStock().toString());
            }
        });
    }

    //Metodo que consulta a la base de datos y carga las marcas en el combo
    private void cargarMarcas() {
        try {
            String sql = "SELECT * FROM marca";
            Statement stm = this.conex.getConexion().createStatement();
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                this.CombMarca.getItems().add(new Marca(resultado.getInt("idmarca"), resultado.getString("descripcion")));
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al cargar Marcas", ex);
        }
    }

    private void cargarDatos() {
        this.tblDatos.getItems().clear();
        try {
            String sql = "SELECT * FROM producto";
            Statement stm = conex.getConexion().prepareStatement(sql);
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                Integer codigo = resultado.getInt("idproducto");
                String descripcion = resultado.getString("descripcion");
                Integer stock = resultado.getInt("cantidad");
                Integer iva = resultado.getInt("iva");
                Integer precio = resultado.getInt("precio");
                String marca = resultado.getString("idmarca");
                Producto p = new Producto(codigo, descripcion, iva,stock, precio, marca);
                this.tblDatos.getItems().add(p);
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar datos de la BD", ex);
        }
    }

    @FXML
    private void onRegistrar(ActionEvent event) {
        String codigo = this.txtCodigo.getText();
        String descripcion = this.txtDescripcion.getText();
        String stock = this.txtStock.getText();
        String iva = this.CombIva.getSelectionModel().getSelectedItem();
        Integer iv;
        switch (iva) {
            case "10%":
                iv = 10;
            case "5%":
                iv = 5;
                break;
            default:
                iv = 0;
                break;
        }
        String precio = this.txtPrecio.getText();
        Integer marca = this.CombMarca.getSelectionModel().getSelectedItem().getCodigo();

        try {
            String Sql = "INSERT INTO producto(idproducto, descripcion, cantidad, precio, iva, idmarca) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement stm = conex.getConexion().prepareStatement(Sql);
            Integer idprod = Integer.parseInt(codigo);
            stm.setInt(1, idprod);
            stm.setString(2, descripcion);
            stm.setString(3, stock);
            stm.setString(4, precio);
            stm.setInt(5, iv);
            stm.setInt(6, marca);
            stm.execute();
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Exito");
            al.setHeaderText("Producto guardada correctamente");
            al.showAndWait();
            this.txtDescripcion.clear();
            this.txtCodigo.clear();
            this.cargarMarcas();
            this.cargarDatos();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
        }
    }

    @FXML
    private void onEditar(ActionEvent event) {
        String codigo = this.txtCodigo.getText();
        String descripcion = this.txtDescripcion.getText();
        String stock = this.txtStock.getText();
        String iva = this.CombIva.getSelectionModel().getSelectedItem();
        Integer iv;
        switch (iva) {
            case "10%":
                iv = 10;
            case "5%":
                iv = 5;
                break;
            default:
                iv = 0;
                break;
        }
        String precio = this.txtPrecio.getText();
        Integer marca = this.CombMarca.getSelectionModel().getSelectedItem().getCodigo();
        String sql = "UPDATE producto SET descripcion=?, cantidad=?, iva=?, precio=?, idmarca=?  WHERE idproducto =?";

        try {

            PreparedStatement stm = conex.getConexion().prepareStatement(sql);
            
            
            stm.setString(1, descripcion);
            stm.setString(2, stock);
            stm.setInt(3, iv);
            stm.setString(4, precio);
            stm.setInt(5, marca);
            stm.setInt(6, Integer.parseInt(codigo));
            stm.execute();
            Alert al = new Alert(Alert.AlertType.INFORMATION);
            al.setTitle("Exito");
            al.setHeaderText("Producto guardada correctamente");
            al.showAndWait();
            this.txtDescripcion.clear();
            this.txtCodigo.clear();
           
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
            Alert al = new Alert(AlertType.INFORMATION);
            al.setTitle("Error de conexion");
            al.setHeaderText("No se puede editar registro en la base de datos");
            al.showAndWait();
        }
        this.cargarMarcas();
        this.cargarDatos();
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        String strCodigo = this.txtCodigo.getText();
        String strDescripcion = this.txtDescripcion.getText();
        if(strCodigo.isEmpty()){
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error al eliminar");
            a.setHeaderText("Ingrese un codigo");
            a.show();
        } else {
            Alert alConfirm = new Alert(AlertType.CONFIRMATION);
            alConfirm.setTitle("Confirmar");
            alConfirm.setHeaderText("Desea eliminar la marca?");
            alConfirm.setContentText(strCodigo + "-" + strDescripcion);
            Optional<ButtonType> accion =  alConfirm.showAndWait();
            
            if (accion.get().equals(ButtonType.OK)) {
              try {
                String sql = "DELETE FROM producto WHERE idproducto = ?";
                PreparedStatement stm = conex.getConexion().prepareStatement(sql);
                Integer cod=Integer.parseInt(strCodigo);
                stm.setInt(1,cod);
                stm.execute();
                int cantidad = stm.getUpdateCount();
                if(cantidad == 0){
                    Alert a=new Alert(AlertType.ERROR);
                    a.setTitle("Error al eliminar");
                    a.setHeaderText("No existe la producto con codigo"+strCodigo);
                    a.show();
                }else{
                   Alert a=new Alert(AlertType.ERROR);
                    a.setTitle("Eliminado");
                    a.setHeaderText("Producto eliminada correctamente.");
                    a.show(); 
                    this.txtCodigo.clear();
                    this.txtDescripcion.clear();
                    this.cargarDatos();
                }
            }catch (SQLException ex){
                LOG.log(Level.SEVERE, "Error al eliminar", ex);
            }  
            }
            
        }
    }
    
}
