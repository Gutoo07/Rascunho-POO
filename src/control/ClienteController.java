package control;
import model.Cliente;
import dao.ComandaDAO;
import dao.ComandaException;
import dao.ComandaDAOimp;

import java.util.Optional;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ClienteController {
    private ObservableList<Cliente> lista = FXCollections.observableArrayList();
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nome = new SimpleStringProperty();
    private StringProperty telefone = new SimpleStringProperty();
    private StringProperty cpf = new SimpleStringProperty();

    private ComandaDAO comandaDAO;

    public ClienteController() throws ComandaException {
        id.set(0);
        nome.set("");
        telefone.set("");
        cpf.set("");
        comandaDAO = new ComandaDAOimp();
    }
    /*Adiciona cliente, e confere se o ID a ser inserido ja existe no BD
    Oferece a opcao de sobrescrever os dados
    Permite a sobrescrita dos dados mesmo se o Cliente possuir Comanda(s) abertas*/
    public void adicionar() throws ComandaException {
        int clienteId = this.id.get();
        Cliente c = new Cliente(clienteId);
        c.setNome(this.nome.get());
        c.setTelefone(this.telefone.get());
        c.setCpf(this.cpf.get());
        if (comandaDAO.getClienteById(clienteId) != null) {
            Optional<ButtonType> opcao = alert(AlertType.CONFIRMATION,
                "O ID inserido ja existe no sistema."+
                "\nDeseja sobrescrever Nome, Telefone e CPF?").showAndWait();
            if (opcao.isPresent() && opcao.get() == ButtonType.OK) {
                comandaDAO.atualizarCliente(c);
            }
        } else {
            comandaDAO.inserirCliente(c);    
        }
        refresh();
    }
    /*Usa o BD para atualizar a lista de Clientes*/
    public void refresh() throws ComandaException {
        lista.clear();
        lista.addAll(comandaDAO.refreshClientes());
    }
    public boolean cpfExist(String value){
        if(value == null){
            return false;
        }
        value = value.replaceAll(" ", "");
        for (Cliente c : lista) {
            if(c.getCpf().replaceAll(" ", "").equals(value)){
                return true;
            }
        }
        return false;
    }
    /*Exclui Cliente, mas nao permite a exclusao se ele possuir Comanda(s) aberta*/
    public void excluir(Cliente c) throws ComandaException {
        if (comandaDAO.clienteInativo(c.getId())) {
            comandaDAO.excluirCliente(c);
            refresh();
        } else {
            alert(AlertType.INFORMATION, "Este cliente possui alguma comanda aberta.").show();
        }
    }
    public void entityToBoundary(Cliente c) {
        if (c != null) {
            this.id.set(c.getId());
            this.nome.set(c.getNome());
            this.telefone.set(c.getTelefone());
            this.cpf.set(c.getCpf());
        }
    }
    public Alert alert(AlertType tipo, String texto) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText("Aviso");
        alerta.setContentText(texto);
        return alerta;
    }    
    public void pesquisarClienteNome() throws ComandaException {
        lista.clear();
        lista.addAll(comandaDAO.pesquisarClienteNome(this.nome.get()));
    }
    public IntegerProperty idProperty() {
        return this.id;
    }
    public StringProperty nomeProperty() {
        return this.nome;
    }
    public StringProperty telefoneProperty() {
        return this.telefone;
    }
    public StringProperty cpfProperty() {
        return this.cpf;
    }
    public ObservableList getLista() {
        return this.lista;
    }    
}
