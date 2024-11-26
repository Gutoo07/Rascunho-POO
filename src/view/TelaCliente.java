package view;
import model.Cliente;
import control.ClienteController;
import dao.ComandaException;

import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

public class TelaCliente {
    private TextField txtIdCliente = new TextField("");
    private TextField txtNomeCliente = new TextField("");
    private TextField txtTelefoneCliente = new TextField("");
    private TextField txtCpfCliente = new TextField("");

    private TableView<Cliente> clientes = new TableView<>();

    ClienteController control;
    
    public TelaCliente(){
        try {
            control = new ClienteController();
            control.refresh();
        } catch (ComandaException e) {
            alert(AlertType.ERROR, "Erro ao inicializar control: ComandaController");
        }
        Header header = new Header();
        VBox layout = new VBox();
        Scene scene = new Scene(layout, Main.W, Main.H);

        /* Gridpane com os textfields e labels*/
        GridPane inputCliente = new GridPane();
        GridPane botoes = new GridPane();
        botoes.setHgap(0);
        inputCliente.setHgap(10);
        inputCliente.setVgap(10);
        ColumnConstraints col0 = new ColumnConstraints();
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col0.setPercentWidth(1.0);
        col1.setPercentWidth(10.0);
        col2.setPercentWidth(50.0);
        inputCliente.getColumnConstraints().addAll(col0, col1, col2);
        clientes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /*Botao de Adicionar Cliente e sua funcao*/
        Button btnAdd = new Button("Adicionar");
        btnAdd.setOnAction( e -> {
            if (this.txtIdCliente.getText().equals("")
                ||this.txtNomeCliente.getText().equals("")
                ||this.txtTelefoneCliente.getText().equals("") ) {
                    alert(AlertType.ERROR,
                    "Um ou mais campos obrigatorios estao em branco.");
                } else {
                    try {
                        control.adicionar();
                        clientes.refresh();
                    } catch (ComandaException erro) {
                        alert(AlertType.ERROR, "Erro ao adicionar Cliente");
                    }
                }
        });
        Button btnPesquisar = new Button("Pesquisar Nome");
        btnPesquisar.setOnAction(e -> {
            try {
                control.pesquisarClienteNome();
                clientes.refresh();
            } catch (ComandaException erro) {
                alert(AlertType.ERROR, "Erro ao Pesquisar Nome de Cliente");
            }
        });
        /*Botao que limpa os campos da tela*/
        Button btnLimpar = new Button("Resetar");
        btnLimpar.setOnAction(e -> {
            try {
                txtIdCliente.setText("");
                txtNomeCliente.setText("");
                txtTelefoneCliente.setText("");
                txtCpfCliente.setText("");
                control.refresh();
            } catch (ComandaException erro) {
                alert(AlertType.ERROR, "Erro ao resetar tela Cliente");
            }
        });
        /*Adicionando os elementos ao gridpane*/
        inputCliente.add(new Label("ID"), 1, 1);
        inputCliente.add(txtIdCliente, 2, 1);
        inputCliente.add(new Label("Nome"), 1, 2);
        inputCliente.add(txtNomeCliente, 2, 2);
        inputCliente.add(new Label("Telefone"), 1, 3);
        inputCliente.add(txtTelefoneCliente, 2, 3);
        inputCliente.add(new Label("CPF"), 1, 4);
        inputCliente.add(txtCpfCliente, 2, 4);
        inputCliente.add(btnAdd, 3, 1);
        inputCliente.add(btnPesquisar, 3, 2);
        inputCliente.add(btnLimpar, 3, 3);

        /*BorderPane principal com o gridpane e a tabela de clientes*/
        BorderPane paneGeral = new BorderPane();
        paneGeral.setTop(inputCliente);
        paneGeral.setCenter(clientes);

        gerarColunas();
        gerarBindings();        

        layout.getChildren().addAll(header, paneGeral);
        Main.mapScene.put("CLIENTES", scene);
    }
    /*Funcao para facilitar o lancamento de pop-ups*/
    public void alert(AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText("Aviso");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
    /*Gera as colunas e as adiciona na tabela de clientes*/
    public void gerarColunas() {
        TableColumn<Cliente, Integer> col1 = new TableColumn<>("ID");
        col1.prefWidthProperty().bind(clientes.widthProperty().multiply(0.05));
        col1.setStyle( "-fx-alignment: CENTER;");
        col1.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("id"));
        TableColumn<Cliente, String> col2 = new TableColumn<>("Nome");
        col2.prefWidthProperty().bind(clientes.widthProperty().multiply(0.22));
        col2.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nome"));
        TableColumn<Cliente, String> col3 = new TableColumn<>("Telefone");
        col3.prefWidthProperty().bind(clientes.widthProperty().multiply(0.08));
        col3.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefone"));
        TableColumn<Cliente, String> col4 = new TableColumn<>("CPF");
        col4.prefWidthProperty().bind(clientes.widthProperty().multiply(0.08));
        col4.setCellValueFactory(new PropertyValueFactory<Cliente, String>("cpf"));

        Callback<TableColumn<Cliente, Void>, TableCell<Cliente, Void>> callback = 
            new Callback<>() {
                @Override
                public TableCell<Cliente, Void> call(TableColumn<Cliente, Void> param) {
                    TableCell<Cliente, Void> tc = new TableCell<>() {
                        final Button btnExcluir = new Button("Excluir");
                        {
                            btnExcluir.setOnAction( e -> {
                                try {
                                    Cliente c = clientes.getItems().get( getIndex() );
                                    control.excluir(c);
                                    clientes.refresh();
                                } catch (ComandaException erro) {
                                    alert(AlertType.ERROR, "Erro ao Excluir Cliente");
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
        TableColumn<Cliente, Void> col5 = new TableColumn<>("Acoes");
        col5.prefWidthProperty().bind(clientes.widthProperty().multiply(0.05));
        col5.setStyle( "-fx-alignment: CENTER;");
        col5.setCellFactory(callback);  

        clientes.getColumns().addAll(col1, col2, col3, col4, col5);
        clientes.setItems(control.getLista());

        /*Funcao que coloca nos textfields os dados do cliente que for clicado na tabela*/
        clientes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            control.entityToBoundary(novo);
        });        
    }
    public void gerarBindings() {
        Bindings.bindBidirectional(
            txtIdCliente.textProperty(), control.idProperty(), (javafx.util.StringConverter) new IntegerStringConverter());
        Bindings.bindBidirectional(txtNomeCliente.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(txtTelefoneCliente.textProperty(), control.telefoneProperty());
        Bindings.bindBidirectional(txtCpfCliente.textProperty(), control.cpfProperty());
    }
}
