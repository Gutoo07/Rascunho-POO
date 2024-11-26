package view;
import model.Produto;
import control.ProdutoController;
import dao.ComandaException;

import javafx.beans.binding.Bindings;
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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class TelaProduto {
    private TextField txtIdProduto = new TextField(" ");
    private TextField txtNomeProduto = new TextField(" ");
    private TextField txtValorProduto = new TextField(" ");

    private TableView<Produto> produtos = new TableView<>();

    ProdutoController control;

    public TelaProduto() {
        try {
            control = new ProdutoController();
            control.refresh();
        } catch (ComandaException e) {
            alert(AlertType.ERROR, "Erro ao inicializar control: ComandaController");
        }
        Header header = new Header();
        VBox layout = new VBox();
        Scene scene = new Scene(layout, Main.W, Main.H);

        /* Gridpane com TextField de ID, Nome e Valor de Produto*/
        GridPane inputProduto = new GridPane();
        inputProduto.setHgap(10);
        inputProduto.setVgap(10);
        produtos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /* Botao de Adicionar Produto e sua funcao*/
        Button btnAdd = new Button("Adicionar");
        btnAdd.setOnAction(e -> {
            if (this.txtIdProduto.getText().equals("")
                || this.txtNomeProduto.getText().equals("")
                || this.txtValorProduto.getText().equals("")) {
                    alert(AlertType.ERROR,
                    "Um ou mais campos obrigatorios estao em branco.");                    
                } else {
                    try {
                        control.adicionar();
                        produtos.refresh();
                    } catch (ComandaException erro) {
                        alert(AlertType.ERROR, "Erro ao adicionar Produto");
                    }
                }
        });
        /*Botao para limpar os campos de input */
        Button btnLimpar = new Button("Limpar Campos");
        btnLimpar.setOnAction(e -> {
            try {
                txtIdProduto.setText("");
                txtNomeProduto.setText("");
                txtValorProduto.setText("");
                control.refresh();
            } catch (ComandaException erro) {
                alert(AlertType.ERROR, "Erro ao resetar tela Cliente");
            }
        });
        inputProduto.add(new Label("ID"), 1, 1);
        inputProduto.add(txtIdProduto, 2, 1);
        inputProduto.add(new Label("Nome"), 1, 2);
        inputProduto.add(txtNomeProduto, 2, 2);
        inputProduto.add(new Label("Valor"), 1, 3);
        inputProduto.add(txtValorProduto, 2, 3);
        inputProduto.add(btnAdd, 1, 4);
        inputProduto.add(btnLimpar, 2, 4);

        /*Borderpane Principal com o GridPane e a tabela de produtos */
        BorderPane paneGeral = new BorderPane();
        paneGeral.setTop(inputProduto);
        paneGeral.setCenter(produtos);

        gerarColunas();
        gerarBindings();

        /*Funcao de pesquisa dinamica, retornando os produtos correspondentes ao texto digitado no campo Nome e exibindo na tabela */
        txtNomeProduto.setOnKeyTyped( e -> {
            try {
                control.pesquisarProdutoNome();
                produtos.refresh();
            } catch (ComandaException erro) {
                erro.printStackTrace();
            }
        });

        layout.getChildren().addAll(header, paneGeral);
        Main.mapScene.put("PRODUTOS", scene);
    }
    /*Mesma funcao para facilitar o lancamento de pop-ups*/
    public void alert(AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText("Aviso");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
    /*Gera as colunas e as adiciona na tabela de produtos*/
    public void gerarColunas() {
        TableColumn<Produto, Integer> col1 = new TableColumn<>("ID");
        col1.prefWidthProperty().bind(produtos.widthProperty().multiply(0.05));
        col1.setStyle( "-fx-alignment: CENTER;");
        col1.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("id"));
        TableColumn<Produto, String> col2 = new TableColumn<>("Nome");
        col2.prefWidthProperty().bind(produtos.widthProperty().multiply(0.22));
        col2.setCellValueFactory(new PropertyValueFactory<Produto, String>("nome"));
        TableColumn<Produto, Double> col3 = new TableColumn<>("Valor");
        col3.prefWidthProperty().bind(produtos.widthProperty().multiply(0.05));
        col3.setStyle( "-fx-alignment: CENTER;");
        col3.setCellValueFactory(new PropertyValueFactory<Produto, Double>("valor"));

        Callback<TableColumn<Produto, Void>, TableCell<Produto, Void>> callback =
            new Callback<>() {
                @Override
                public TableCell<Produto, Void> call(TableColumn<Produto, Void> param) {
                    TableCell<Produto, Void> tc = new TableCell<>() {
                        final Button btnExcluir = new Button("Excluir");
                        {
                            btnExcluir.setOnAction( e -> {
                                try {
                                    Produto p = produtos.getItems().get( getIndex() );
                                    control.excluir(p);
                                    produtos.refresh();
                                } catch (ComandaException erro) {
                                    alert(AlertType.ERROR, "Erro ao excluir Produto");
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
         TableColumn<Produto, Void> col4 = new TableColumn<>("Acoes");
         col4.prefWidthProperty().bind(produtos.widthProperty().multiply(0.05));
         col4.setStyle( "-fx-alignment: CENTER;");
         col4.setCellFactory(callback);
         
         produtos.getColumns().addAll(col1, col2, col3, col4);
         produtos.setItems(control.getLista());
        /*Funcao que coloca nos textfields os dados do produto que for clicado na tabela*/
         produtos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            control.entityToBoundary(novo);
         });
    }
    public void gerarBindings() {
        Bindings.bindBidirectional(
            txtIdProduto.textProperty(), control.idProperty(), (javafx.util.StringConverter) new IntegerStringConverter());
        Bindings.bindBidirectional(txtNomeProduto.textProperty(), control.nomeProperty());
        Bindings.bindBidirectional(
            txtValorProduto.textProperty(), control.valorProperty(), (javafx.util.StringConverter) new DoubleStringConverter());
    }
}
