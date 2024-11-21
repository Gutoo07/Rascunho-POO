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
        comandaDAO = new ComandaDAOimp();
    }

    public void adicionar() throws ComandaException {
        int clienteId = this.id.get();
        for (Cliente c : lista) {
            if ( c.getId() == clienteId) {
                Alert alerta = alert(AlertType.CONFIRMATION,
                "O ID inserido ja existe no sistema.\nDeseja sobrescrever Nome, Telefone e CPF?"); 
                Optional<ButtonType> opcao = alerta.showAndWait();
                if (opcao.isPresent() && opcao.get() == ButtonType.OK) {
                    c.setNome(this.nome.get());
                    c.setTelefone(this.telefone.get());
                    c.setCpf(this.cpf.get());
                    comandaDAO.atualizarCliente(c);
                }     
                return;     
            }         
        }
        Cliente c = new Cliente(clienteId);
        c.setNome(this.nome.get());
        c.setTelefone(this.telefone.get());
        c.setCpf(this.cpf.get());
        lista.add(c);
        comandaDAO.inserirCliente(c);              
    }
    public void refresh() throws ComandaException {
        lista.clear();
        lista.addAll(comandaDAO.refreshClientes());
    }
    public void excluir(Cliente c) throws ComandaException {
        lista.remove(c);
        comandaDAO.excluirCliente(c);
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
