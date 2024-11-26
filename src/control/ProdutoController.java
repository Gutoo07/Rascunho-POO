package control;
import dao.ComandaDAO;
import dao.ComandaDAOimp;
import dao.ComandaException;
import model.Produto;

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
        id.set(0);
        nome.set("");
        valor.set(0.0);
        comandaDAO = new ComandaDAOimp();
    }
    /*Funcao de adicionar, que confere no BD se o ID a ser adicionado ja existe ou nao.
    Oferece a opcao de sobrescrever ou nao os dados.
    Nao permite a sobrescrita dos dados se o Produto em questao estiver sendo usado em alguma comanda.*/
    public void adicionar() throws ComandaException {
        int produtoId = this.id.get();
        Produto p = new Produto(produtoId);
        p.setNome(this.nome.get());
        p.setValor(this.valor.get());

        if (comandaDAO.getProdutoById(produtoId) != null) {
            Optional<ButtonType> opcao = alert(AlertType.CONFIRMATION,
                "O ID inserido ja existe no sistema."+
                "\nDeseja sobrescrever Nome e Valor?").showAndWait();
            if (opcao.isPresent() && opcao.get() == ButtonType.OK) {
                if (comandaDAO.produtoNaoUsado(produtoId)) {
                    comandaDAO.atualizarProduto(p);
                    refresh();
                } else {
                    alert(AlertType.INFORMATION, "Impossivel alterar: este produto esta sendo usado em alguma comanda.").show();
                }
            }
        } else {
            comandaDAO.inserirProduto(p);
            refresh();
        }
    }
    /*Atualiza a lista de Produtos com a funcao do BD*/
    public void refresh() throws ComandaException {
        lista.clear();
        lista.addAll(comandaDAO.refreshProdutos());
    }
    /*Excluir produto, mas nao permite a exclusao se estiver sendo usado em alguma comanda.*/
    public void excluir(Produto p) throws ComandaException {
        if (comandaDAO.produtoNaoUsado(p.getId())) {
            comandaDAO.excluirProduto(p);
            refresh();
        } else {
            alert(AlertType.INFORMATION, "Este produto esta sendo usado por alguma comanda.").show();
        }
    }
    /*Coloca os dados do produto correspondente na tela*/
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
