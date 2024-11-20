import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
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
    private ComandaController control;

    public Home(){
        try {
            control = new ComandaController();
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

        Button btnAdd = new Button("Abrir Comanda");
        btnAdd.setOnAction( e -> {
         try {
             control.adicionar();
             comandas.refresh();
         } catch (ComandaException erro) {
             alert(AlertType.ERROR, "Erro ao adicionar/abrir comanda");
         }
        });
        inputComanda.add(new Label("Abrir ou Criar Comanda"),0,0);
        inputComanda.add(new Label("Numero de Comanda"), 0, 1);
        inputComanda.add(txtIdComanda, 1, 1);
        inputComanda.add(btnAdd, 0, 2);

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
                                    Comanda c = comandas.getItems().get( getIndex() );
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
        TableColumn<Comanda, Void> col2 = new TableColumn<>("Acoes");
        col2.setCellFactory(callback);  

        comandas.getColumns().addAll(col1, col2);
        comandas.setItems(control.getLista());

        comandas.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            control.entityToBoundary(novo);
        });
    }
    public void gerarBindings() {
        Bindings.bindBidirectional(
            txtIdComanda.textProperty(), control.idProperty(), (StringConverter) new IntegerStringConverter());
    }    
    public void alert(AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText("Aviso");
        alerta.setContentText(texto);
        alerta.showAndWait();
    }
}
