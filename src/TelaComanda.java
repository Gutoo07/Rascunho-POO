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

public class TelaComanda{
    TextField txtNomeCliente = new TextField("");
    TextField txtTelefoneCliente = new TextField("");
    
    public TelaComanda() throws ComandaException{
        Header header = new Header();

        
        // Label label = new Label("TELA COMANDA");
        // layout.getChildren().addAll(header,label);
        

        VBox vbox = new VBox();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);


        Label nomeLabel = new Label("Nome: "+ Main.persistenceCliente.getNome());
        Label telefoneLabel = new Label("Telefone: "+ Main.persistenceCliente.getTelefone());
        Label cpfLabel = new Label("CPF: "+ Main.persistenceCliente.getCpf());

        //Tabela Produtos
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
                    addButton.setOnAction(e ->  {
                        Produto produto = getTableRow().getItem();
                        if (produto != null) {
                            System.out.println("Produto selecionado: " + produto.getNome());
                        }
                });
                }
            }
        });

        tabelaProdutos.getColumns().addAll(nomeProdutoCol, valorProdutoCol,addProdutoCol);

        ProdutoController produtoController;
      
        produtoController = new ProdutoController();
        produtoController.refresh();
        tabelaProdutos.getItems().addAll(produtoController.getLista());

        
        //Header
        grid.add(header,0,0,1,1);
        grid.add(nomeLabel, 0, 0);
        grid.add(telefoneLabel, 0, 1);
        grid.add(cpfLabel, 0, 2);
        grid.add(new Label("Produtos Disponiveis"),0,3);
        grid.add(tabelaProdutos, 0, 4, 3, 1);
        grid.add(new Label("Comanda"),5,3);
        //grid.add(tabelaProdutos2, 3, 4, 6, 1);
        vbox.getChildren().addAll(header,grid);
        Scene scene = new Scene(vbox, Main.W, Main.H);
        Main.mapScene.put("COMANDA", scene);
    }

}
