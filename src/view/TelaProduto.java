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
import javafx.util.StringConverter;
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

        //Gridpane com TextField de ID, Nome e Valor de Produto
        GridPane inputProduto = new GridPane();
        inputProduto.setHgap(10);
        inputProduto.setVgap(10);
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

        //Borderpane Principal
        BorderPane paneGeral = new BorderPane();
        paneGeral.setTop(inputProduto);
        paneGeral.setCenter(produtos);

        gerarColunas();
        gerarBindings();

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
    public void alert(AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText("Aviso");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
    public void gerarColunas() {
        TableColumn<Produto, Integer> col1 = new TableColumn<>("ID");
        col1.setCellValueFactory(new PropertyValueFactory<Produto, Integer>("id"));
        TableColumn<Produto, String> col2 = new TableColumn<>("Nome");
        col2.setCellValueFactory(new PropertyValueFactory<Produto, String>("nome"));
        TableColumn<Produto, Double> col3 = new TableColumn<>("Valor");
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
                                    System.out.println("botao excluir ativado");
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
         col4.setCellFactory(callback);
         
         produtos.getColumns().addAll(col1, col2, col3, col4);
         produtos.setItems(control.getLista());

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
