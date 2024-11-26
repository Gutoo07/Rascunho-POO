package view;
import dao.ComandaDAO;
import dao.ComandaDAOimp;
import dao.ComandaException;
import control.HomeController;
import  control.ClienteController;
import model.Comanda;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    private Label comandasAbertas;
    private Label totalComandas;

    private HomeController control;
    private ComandaDAO comandaDAO;

    public Home() throws ComandaException {
        try {
            control = new HomeController();
            comandaDAO = new ComandaDAOimp();
            comandasAbertas = new Label("Comandas Abertas: "+control.contarComandasAbertas());
            totalComandas = new Label("Total de Todas as Comandas: "+ control.getTotalComandas());
            atualizarHome();
        } catch (ComandaException e) {
            alert(AlertType.ERROR, "Erro ao inicializar control: ComandaController");
        }
        Header header = new Header();
        VBox layout = new VBox();
        Scene scene = new Scene(layout, Main.W, Main.H);

        /*Gridpane com o TextField (input) de numero de comanda*/
        GridPane inputComanda = new GridPane();
        inputComanda.setHgap(10);
        inputComanda.setVgap(15);
        comandas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /*Botao Criar Comanda e sua funcao*/
        Button btnAdd = new Button("Criar Comanda");
        btnAdd.setOnAction( e -> {
         try {
            ClienteController cliente = new ClienteController();
            cliente.refresh();
            if(!cliente.cpfExist(txtCpfCliente.getText())){
                alert(AlertType.ERROR, "Insira um CPF de Cliente valido.");
            }else{
                control.adicionar();
                atualizarHome();
            }
         } catch (ComandaException erro) {
             alert(AlertType.ERROR, "Erro ao adicionar/abrir comanda");
         }
        });
        /*Adicionando os elementos no GridPane*/
        inputComanda.add(new Label("Gerenciador de Comandas"),0,0);
        inputComanda.add(new Label("Numero de Comanda"), 0, 1);
        inputComanda.add(new Label("CPF do Cliente"), 0, 2);
        inputComanda.add(txtIdComanda, 1, 1);
        inputComanda.add(txtCpfCliente, 1, 2);
        inputComanda.add(btnAdd, 0, 3);
        inputComanda.add(comandasAbertas, 3, 0);
        inputComanda.add(totalComandas, 3, 1);

        /*Borderpane principal contendo o Gridpane e a TableView de comandas*/
        BorderPane paneGeral = new BorderPane();
        paneGeral.setTop(inputComanda);
        paneGeral.setCenter(comandas);

        gerarColunas();
        gerarBindings();

        layout.getChildren().addAll(header, paneGeral);
        Main.mapScene.put("INICIO", scene);
    }
    /*Gera as colunas e as adiciona na tabela de Comandas*/
    public void gerarColunas() {
        TableColumn<Comanda, String> col = new TableColumn<>("Nome");
        // col.prefWidthProperty().bind(comandas.widthProperty().multiply(0.22));
        col.setCellValueFactory(new PropertyValueFactory<Comanda, String>("nome"));
        TableColumn<Comanda, Integer> col1 = new TableColumn<>("Comanda");
        col1.setStyle( "-fx-alignment: CENTER;");
        col1.prefWidthProperty().bind(comandas.widthProperty().multiply(0.05));
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
                                    atualizarHome();
                                    // comandas.refresh();
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
        TableColumn<Comanda, Void> excluirComandaCol = new TableColumn<>("");
        excluirComandaCol.setStyle( "-fx-alignment: CENTER;");
        excluirComandaCol.prefWidthProperty().bind(comandas.widthProperty().multiply(0.05));
        excluirComandaCol.setCellFactory(callback);  
        TableColumn<Comanda, Void> abrirComandaCol = new TableColumn<>("");
        abrirComandaCol.setStyle( "-fx-alignment: CENTER;");
        abrirComandaCol.prefWidthProperty().bind(comandas.widthProperty().multiply(0.05));
        abrirComandaCol.setCellFactory(comandaCol -> new TableCell<Comanda, Void>() {
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
                            e1.printStackTrace();
                        }

                    });
                }
            }
        });
        TableColumn<Comanda, String> colVazia = new TableColumn<>("");
        colVazia.prefWidthProperty().bind(comandas.widthProperty().multiply(0.07));
        colVazia.setStyle( "-fx-alignment: CENTER;");
        colVazia.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(control.getComandaVazia(cellData.getValue().getId()));
            } catch (ComandaException e) {
                e.printStackTrace();
            }
            return null;
        });

        comandas.getColumns().addAll(col1, col, abrirComandaCol, excluirComandaCol, colVazia);
        comandas.setItems(control.getLista());

        /*Funcao que coloca no TextField os dados da comanda que for clicada na tabela*/
        comandas.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            control.entityToBoundary(novo);
        });
    }
    public void gerarBindings() {
        Bindings.bindBidirectional(txtIdComanda.textProperty(), control.idProperty(), (StringConverter) new IntegerStringConverter());
        Bindings.bindBidirectional(txtCpfCliente.textProperty(), control.nomeProperty());       
    }    
    /*Funcao para facilitar o lancamento de pop-ups*/
    public static void alert(AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText("Aviso");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
    /*Atualiza a lista de comandas com uma funcao no sql, atualiza a lista de comandas da tabela e atualiza os dados das labels*/
    public void atualizarHome() throws ComandaException {
        control.refresh();
        comandas.refresh();
        comandasAbertas.setText("Comandas Abertas : " + control.contarComandasAbertas());
        totalComandas.setText("Total de Todas as Comandas: "+ control.getTotalComandas());
    }
}