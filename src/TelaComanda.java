import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TelaComanda {
    TextField txtNomeCliente = new TextField("");
    TextField txtTelefoneCliente = new TextField("");
    ComandaProdutoController produtoController = new ComandaProdutoController();
    TableView<Produto> tabelaComanda = new TableView<>();
    ProdutoController produtoControllerDisponiveis = new ProdutoController();
    private ComandaDAO comandaDAO;

    

    public TelaComanda() throws ComandaException {
        Header header = new Header();
        comandaDAO = new ComandaDAOimp();
        VBox vbox = new VBox();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label nomeLabel = new Label("Nome: " + Main.persistenceCliente.getNome());
        Label telefoneLabel = new Label("Telefone: " + Main.persistenceCliente.getTelefone());
        Label cpfLabel = new Label("CPF: " + Main.persistenceCliente.getCpf());

        //Produtos
        TableView<Produto> tabelaProdutos = new TableView<>();
        TableColumn<Produto, String> nomeProdutoCol = new TableColumn<>("Nome");
        nomeProdutoCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));

        TableColumn<Produto, Double> valorProdutoCol = new TableColumn<>("Valor");
        valorProdutoCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValor()).asObject());

        TableColumn<Produto, Void> addProdutoCol = new TableColumn<>("Adicionar");
        addProdutoCol.setCellFactory(col -> new TableCell<Produto, Void>() {
            private final Button addButton = new Button("Adicionar");

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
                            System.out.println("Produto selecionado: " + produto.getNome());
                            produtoController.add(Main.persistenceComanda.getId(),produto.getId(), 1);
                            try {
                                atualizarTabelaComanda();
                            } catch (ComandaException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            } 
                        }
                    });
                }
            }
        });

        tabelaProdutos.getColumns().addAll(nomeProdutoCol, valorProdutoCol, addProdutoCol);

        produtoControllerDisponiveis.refresh();
        tabelaProdutos.getItems().addAll(produtoControllerDisponiveis.getLista());

        //Produtos da Comanda
        TableColumn<Produto, String> nomeComandaCol = new TableColumn<>("Nome");
        nomeComandaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));

        TableColumn<Produto, Integer> qtdComandaCol = new TableColumn<>("Quantidade");
        qtdComandaCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(produtoController.get(cellData.getValue().getId())).asObject());

        TableColumn<Produto, Double> valorComandaCol = new TableColumn<>("Valor");
        valorComandaCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getValor()).asObject());

        tabelaComanda.getColumns().addAll(nomeComandaCol,valorComandaCol,qtdComandaCol);

        //config
        grid.add(header, 0, 0, 1, 1);
        grid.add(nomeLabel, 0, 0);
        grid.add(telefoneLabel, 0, 1);
        grid.add(cpfLabel, 0, 2);
        grid.add(new Label("Produtos Disponíveis"), 0, 3);
        grid.add(tabelaProdutos, 0, 4, 3, 1);
        grid.add(new Label("Comanda"), 5, 3);
        grid.add(tabelaComanda, 5, 4, 3, 1);

        vbox.getChildren().addAll(header, grid);
        Scene scene = new Scene(vbox, Main.W, Main.H);
        Main.mapScene.put("COMANDA", scene);

        atualizarTabelaComanda();
    }




    private void atualizarTabelaComanda() throws ComandaException {
        List<ProdutoComanda> lista = comandaDAO.getProdutoComandaByIdComanda(Main.persistenceComanda.getId());
        System.out.println("Lista: " + lista.size());
        tabelaComanda.getItems().clear();
        for(ProdutoComanda produto : lista){
            produtoController.set(produto.getIdProduto(), produto.getQtd());
        }
        produtoController.getMap().forEach((id, qtd) -> {
            Produto produto;
            try {
                produto = comandaDAO.getProdutoById(id);
                if (produto != null) {
                    tabelaComanda.getItems().add(produto);
                }
            } catch (ComandaException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
 
        });
    }
}
