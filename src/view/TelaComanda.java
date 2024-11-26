package view;
import model.Pagamento;
import model.Produto;
import model.ProdutoComanda;
import control.ComandaProdutoController;
import control.ProdutoController;
import dao.ComandaDAO;
import dao.ComandaDAOimp;
import dao.ComandaException;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

public class TelaComanda {
    TextField txtNomeCliente = new TextField("");
    TextField txtTelefoneCliente = new TextField("");
    TextField txtPesquisarProduto = new TextField("");
    Label valorTotalLabel;
    TableView<Produto> tabelaComanda = new TableView<>();
    
    ComandaProdutoController produtoController = new ComandaProdutoController();
    ProdutoController produtoControllerDisponiveis = new ProdutoController();
    
    private ComandaDAO comandaDAO;

    public TelaComanda() throws ComandaException {
        comandaDAO = new ComandaDAOimp();
        Header header = new Header();
        VBox vbox = new VBox();
        GridPane grid = new GridPane();
        grid.setHgap(50);
        grid.setVgap(10);
        tabelaComanda.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label nomeLabel = new Label("Nome: " + Main.persistenceCliente.getNome());
        Label telefoneLabel = new Label("Telefone: " + Main.persistenceCliente.getTelefone());
        Label cpfLabel = new Label("CPF: " + Main.persistenceCliente.getCpf());
    
        gerarBindings();

        /*Colunas da tabela de produtos disponiveis para consumo*/
        TableView<Produto> tabelaProdutos = new TableView<>();
        tabelaProdutos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Produto, String> nomeProdutoCol = new TableColumn<>("Nome");
        nomeProdutoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        TableColumn<Produto, Double> valorProdutoCol = new TableColumn<>("Valor Uni.");
        valorProdutoCol.prefWidthProperty().bind(tabelaProdutos.widthProperty().multiply(0.09));
        valorProdutoCol.setStyle( "-fx-alignment: CENTER;");
        valorProdutoCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValor()).asObject());
        TableColumn<Produto, Void> addProdutoCol = new TableColumn<>("+");
        addProdutoCol.prefWidthProperty().bind(tabelaProdutos.widthProperty().multiply(0.06));
        addProdutoCol.setStyle( "-fx-alignment: CENTER;");
        addProdutoCol.setCellFactory(col -> new TableCell<Produto, Void>() {
            private final Button addButton = new Button("+");
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                    addButton.setOnAction(e -> {
                        Produto produto = getTableRow().getItem();
                        if (produto != null) {
                            produtoController.add(Main.persistenceComanda.getId(),produto.getId(), 1);
                            try {
                                atualizarTabelaComanda();
                            } catch (ComandaException e1) {
                                e1.printStackTrace();
                            } 
                        }
                    });
                }
            }
        });

        tabelaProdutos.getColumns().addAll(nomeProdutoCol, valorProdutoCol, addProdutoCol);

        produtoControllerDisponiveis.refresh();
        tabelaProdutos.setItems(produtoControllerDisponiveis.getLista());

        /*Colunas da tabela de produtos consumidos pela comanda respectiva*/
        TableColumn<Produto, String> nomeComandaCol = new TableColumn<>("Nome");
        nomeComandaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        TableColumn<Produto, Integer> qtdComandaCol = new TableColumn<>("Qtd");
        qtdComandaCol.setStyle( "-fx-alignment: CENTER;");
        qtdComandaCol.prefWidthProperty().bind(tabelaProdutos.widthProperty().multiply(0.06));
        qtdComandaCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(produtoController.get(cellData.getValue().getId())).asObject());
        TableColumn<Produto, Double> valorComandaCol = new TableColumn<>("Valor Uni.");
        valorComandaCol.setStyle( "-fx-alignment: CENTER;");
        valorComandaCol.prefWidthProperty().bind(tabelaProdutos.widthProperty().multiply(0.09));
        valorComandaCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValor()).asObject());
        TableColumn<Produto, Double> valorTotalProduto = new TableColumn<>("Valor Total");
        valorTotalProduto.setStyle( "-fx-alignment: CENTER;");
        valorTotalProduto.prefWidthProperty().bind(tabelaProdutos.widthProperty().multiply(0.09));
        valorTotalProduto.setCellValueFactory(cellData -> {
            try {
                return new SimpleDoubleProperty(produtoController.getValorTotalProduto(Main.persistenceComanda.getId(), cellData.getValue().getId())).asObject();
            } catch (ComandaException e) {
                e.printStackTrace();
            }
            return null;
        });
        TableColumn<Produto, Void> lessProdutoCol = new TableColumn<>("");
        lessProdutoCol.setStyle( "-fx-alignment: CENTER;");
        lessProdutoCol.prefWidthProperty().bind(tabelaProdutos.widthProperty().multiply(0.05));
        lessProdutoCol.setCellFactory(col -> new TableCell<Produto, Void>() {
            private final Button addButton = new Button("-");
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                    addButton.setOnAction(e -> {
                        Produto produto = getTableRow().getItem();
                        if (produto != null) {
                            produtoController.less(Main.persistenceComanda.getId(),produto.getId(), 1);
                            try {
                                atualizarTabelaComanda();
                            } catch (ComandaException e1) {
                                e1.printStackTrace();
                            } 
                        }
                    });
                }
            }
        });
        TableColumn<Produto, Void> deleteProdutoCol = new TableColumn<>("");
        deleteProdutoCol.setStyle( "-fx-alignment: CENTER;");
        deleteProdutoCol.prefWidthProperty().bind(tabelaProdutos.widthProperty().multiply(0.05));
        deleteProdutoCol.setCellFactory(col -> new TableCell<Produto, Void>() {
            private final Button addButton = new Button("X");
            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                    addButton.setOnAction(e -> {
                        Produto produto = getTableRow().getItem();
                        if (produto != null) {
                            produtoController.delete(Main.persistenceComanda.getId(),produto.getId());
                            try {
                                atualizarTabelaComanda();
                            } catch (ComandaException e1) {
                                e1.printStackTrace();
                            } 
                        }
                    });
                }
            }
        });

        tabelaComanda.getColumns().addAll(nomeComandaCol,valorComandaCol, qtdComandaCol, valorTotalProduto, lessProdutoCol, deleteProdutoCol);

        /*Funcao de Pagamento, no caso meramente ilustrativa e apenas fecha a comanda em questao*/

        valorTotalLabel = new Label("Valor Total: R$ "+ comandaDAO.getValorTotalComanda(Main.persistenceComanda.getId())); 
        /*Botao de Pagar e sua funcao*/
        Button pagarButton = new Button("Pagar");
        pagarButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aguardando pagamento");
            alert.setHeaderText(null);
            alert.setContentText("Aguardando pagamento...");
            alert.initModality(Modality.WINDOW_MODAL);
            alert.showAndWait();
            try {
                comandaDAO.excluirComanda(Main.persistenceComanda);
                Main.pageSelected = "INICIO";
                Main.updateComponent();
                Scene scene = Main.mapScene.get("INICIO");
                System.out.println(Main.persistenceCliente.getId());
                System.out.println(comandaDAO.getValorTotalComanda(Main.persistenceComanda.getId()));
                Pagamento pagamento = new Pagamento(comandaDAO.getValorTotalComanda(Main.persistenceComanda.getId()) , Main.persistenceCliente.getId());
                System.out.println("PAGAMENTO!");
                comandaDAO.realizarPagamento(pagamento);
                Main.changeTela(scene);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PAGAMENTO CONCLUIDO!");
                alert.setHeaderText(null);
                alert.setContentText("O pagamento da Comanda foi finalizado!");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.showAndWait();
            } catch (ComandaException erro) {
                erro.printStackTrace();
            }
        });
        /*Funcao de pesquisa dinamica, retornando os produtos correspondentes
        ao texto digitado na tabela de produtos*/
        txtPesquisarProduto.setOnKeyTyped(e -> {
            try {
                produtoControllerDisponiveis.pesquisarProdutoNome();
                tabelaProdutos.refresh();
            } catch (ComandaException erro) {
                erro.printStackTrace();
            }
        });

        /*Adicionando os elementos ao gridpane
        com dados do cliente e textfield de pesquisa de produtos*/
        grid.add(header, 0, 0, 1, 1);
        grid.add(nomeLabel, 0, 0);
        grid.add(telefoneLabel, 0, 1);
        grid.add(cpfLabel, 0, 2);
        grid.add(new Label("Adicionar / Pesquisar Produtos"), 0, 3);
        grid.add(txtPesquisarProduto, 0, 4);
        grid.add(new Label("Comanda "+Main.persistenceComanda.getId()), 3, 0);
        grid.add(valorTotalLabel, 3 ,1);
        grid.add(pagarButton, 3, 2);

        /*GridPane principal com as duas tabelas*/
        GridPane grid2 = new GridPane();
        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(40.0);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(60.0);
        grid2.getColumnConstraints().addAll(col0, col1);
        grid2.add(tabelaProdutos, 0, 0);
        grid2.add(tabelaComanda, 1, 0);
        vbox.getChildren().addAll(header, grid, grid2);
        Scene scene = new Scene(vbox, Main.W, Main.H);
        Main.mapScene.put("COMANDA", scene);

        atualizarTabelaComanda();
    }
    /*Atualiza a lista de produtos consumidos da comanda*/
    private void atualizarTabelaComanda() throws ComandaException {
        List<ProdutoComanda> lista = comandaDAO.getProdutoComandaByIdComanda(Main.persistenceComanda.getId());
        tabelaComanda.getItems().clear();
        for(ProdutoComanda produtoComanda : lista){
            produtoController.set(produtoComanda.getIdProduto(), produtoComanda.getQtd());
        }
        valorTotalLabel.setText("Valor Total: R$ "+ comandaDAO.getValorTotalComanda(Main.persistenceComanda.getId()));
        
        produtoController.getMap().forEach((id, qtd) -> {
            Produto produto;
            try {
                produto = comandaDAO.getProdutoById(id);
                if (produto != null) {
                    tabelaComanda.getItems().add(produto);
                }
            } catch (ComandaException e) {
                e.printStackTrace();
            }
        });
    }
    public void gerarBindings() {
        Bindings.bindBidirectional(txtPesquisarProduto.textProperty(), produtoControllerDisponiveis.nomeProperty());
    }
}
