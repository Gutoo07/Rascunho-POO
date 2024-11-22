import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

public class Home{
    private TableView<Comanda> comandas = new TableView<>();
    private TextField txtIdComanda = new TextField();
    private TextField txtCpfCliente = new TextField();
    private ComandaController control;
    private ComandaDAO comandaDAO;

    public Home(){
        try {
            control = new ComandaController();
            comandaDAO = new ComandaDAOimp();
            control.refresh();
        } catch (ComandaException e) {
            alert(AlertType.ERROR, "Erro ao inicializar control: ComandaController");
        }

        Header header = new Header();
        VBox layout = new VBox();
        
        // Label label = new Label("TELA INICIO"); tirei fora
        // layout.getChildren().addAll(header,label); movido pra baixo apenas
        Scene scene = new Scene(layout, Main.W, Main.H);

        //Gridpane com o TextField (input) de numero de comanda
        GridPane inputComanda = new GridPane();
        inputComanda.setHgap(10);
        inputComanda.setVgap(15);

        Button btnAdd = new Button("Criar Comanda");
        btnAdd.setOnAction( e -> {
         try {
            ClienteController cliente = new ClienteController();
            cliente.refresh();
            if(!cliente.cpfExist(txtCpfCliente.getText())){
                alert(AlertType.ERROR, "Cliente não existe!");
            }else{
                control.adicionar();
                comandas.refresh();
            }
         } catch (ComandaException erro) {
             alert(AlertType.ERROR, "Erro ao adicionar/abrir comanda");
         }
        });

        inputComanda.add(new Label("Gerenciador de Comandas"),0,0);
        inputComanda.add(new Label("Numero de Comanda"), 0, 1);
        inputComanda.add(new Label("CPF do Cliente"), 0, 2);
        inputComanda.add(txtIdComanda, 1, 1);
        inputComanda.add(txtCpfCliente, 1, 2);
        inputComanda.add(btnAdd, 0, 3);

        //Borderpane principal contendo o Gridpane e a TableView de comandas
        BorderPane paneGeral = new BorderPane();
        paneGeral.setTop(inputComanda);
        paneGeral.setCenter(comandas);

        gerarColunas();
        gerarBindings();

        layout.getChildren().addAll(header, paneGeral);
        Main.mapScene.put("INICIO", scene);
    }
    public void gerarColunas() {

        TableColumn<Comanda, String> col = new TableColumn<>("Nome");
        col.setCellValueFactory(new PropertyValueFactory<Comanda, String>("nome"));

        TableColumn<Comanda, Integer> col1 = new TableColumn<>("Comanda");
        col1.setCellValueFactory(new PropertyValueFactory<Comanda, Integer>("id"));

        Callback<TableColumn<Comanda, Void>, TableCell<Comanda, Void>> callback = 
            new Callback<>() {
                @Override
                public TableCell<Comanda, Void> call(TableColumn<Comanda, Void> param){
                    TableCell<Comanda, Void> tc = new TableCell<>() {
                        final Button btnExcluir = new Button("Excluir");
                        {
                            btnExcluir.setOnAction( e -> {
                                try {
                                    Comanda c = comandas.getItems().get(getIndex());
                                    control.excluir(c);
                                    comandas.refresh();
                                } catch (ComandaException err) {
                                    alert(AlertType.ERROR, "Erro ao Excluir");
                                }
                            });
                        }
                        public void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(btnExcluir);
                            }
                        }
                    };
                    return tc;
                }
        };
        TableColumn<Comanda, Void> col2 = new TableColumn<>("");
        col2.setCellFactory(callback);  

        TableColumn<Comanda, Void> abriComandaCol = new TableColumn<>("");
        abriComandaCol.setCellFactory(comandaCol -> new TableCell<Comanda, Void>() {
            private final Button addButton = new Button("Abrir");

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                    addButton.setOnAction(e -> {
                        try {
                            Comanda c = getTableRow().getItem();
                            Main.persistenceCliente = comandaDAO.getClienteById(c.getClienteId());
                            Main.persistenceComanda = c;
                            Main.pageSelected = "COMANDA";
                            Main.updateComponent();
                            Scene scene = Main.mapScene.get("COMANDA");
                            Main.changeTela(scene);
                        } catch (ComandaException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                    });
                }
            }
        });

        comandas.getColumns().addAll(col,col1,abriComandaCol, col2);
        comandas.setItems(control.getLista());

        comandas.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            control.entityToBoundary(novo);
        });
    }
    public void gerarBindings() {
        Bindings.bindBidirectional(txtIdComanda.textProperty(), control.idProperty(), (StringConverter) new IntegerStringConverter());
        Bindings.bindBidirectional(txtCpfCliente.textProperty(), control.nomeProperty());       
    }    
    public static void alert(AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText("Aviso");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}