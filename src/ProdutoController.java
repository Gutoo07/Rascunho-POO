import java.util.Optional;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ProdutoController {
    private ObservableList<Produto> lista = FXCollections.observableArrayList();
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private DoubleProperty valor = new SimpleDoubleProperty();

    private ComandaDAO comandaDAO;

    public ProdutoController() throws ComandaException {
        comandaDAO = new ComandaDAOimp();
    }

    public void adicionar() throws ComandaException {
        int produtoId = this.id.get();
        for (Produto p : lista) {
            if (p.getId() == produtoId) {
                Optional<ButtonType> opcao = alert(AlertType.CONFIRMATION,
                    "O ID inserido ja existe no sistema."+
                    "\nDeseja sobrescrever Nome e Valor?").showAndWait();
                if (opcao.isPresent() && opcao.get() == ButtonType.OK) {
                    p.setNome(this.nome.get());
                    p.setValor(this.valor.get());
                    comandaDAO.atualizarProduto(p);
                }
                return;
            }
        }
        Produto p = new Produto(this.id.get());
        p.setNome(this.nome.get());
        p.setValor(this.valor.get());
        lista.add(p);
        comandaDAO.inserirProduto(p);
    }
    public void refresh() throws ComandaException {
        lista.clear();
        lista.addAll(comandaDAO.refreshProdutos());
    }
    public void excluir(Produto p) throws ComandaException {
        lista.remove(p);
        comandaDAO.excluirProduto(p);
    }
    public void entityToBoundary(Produto p) {
        if (p != null) {
            this.id.set(p.getId());
            this.nome.set(p.getNome());
            this.valor.set(p.getValor());
        }
    }
    public void pesquisarProdutoNome() throws ComandaException {
        lista.clear();
        lista.addAll(comandaDAO.pesquisarProdutoNome(this.nome.get()));
    }
    public Alert alert(AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText("Aviso");
        alerta.setContentText(texto);
        return alerta;
    }
    public IntegerProperty idProperty() {
        return this.id;
    }
    public StringProperty nomeProperty() {
        return this.nome;
    }
    public DoubleProperty valorProperty() {
        return this.valor;
    }
    public ObservableList getLista() {
        return this.lista;
    }
}
