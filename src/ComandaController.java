import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

public class ComandaController {
    private ObservableList<Comanda> lista = FXCollections.observableArrayList();
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty cpf = new SimpleStringProperty();
    private ComandaDAO comandaDAO;

    public ComandaController() throws ComandaException {
        comandaDAO = new ComandaDAOimp();
    }

    public void adicionar() throws ComandaException {
        int comandaId = this.id.get();
        String cpf = this.cpf.get();
        Object[] values = comandaDAO.getNomeByCpf(cpf);

        for (Comanda aux : lista) {
            if ( aux.getId() == comandaId ) {
                //abre comanda ativa
                Main.persistenceCliente = comandaDAO.getClienteById(aux.getClienteId());
                Main.persistenceComanda = aux;
                System.out.println(Main.persistenceCliente.getTelefone());
                System.out.println("Comanda ativa");
                Main.pageSelected = "COMANDA";
                try {
                    Main.updateComponent();
                } catch (ComandaException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Scene scene = Main.mapScene.get("COMANDA");
                Main.changeTela(scene);
                return;
            }
        }
        String nome = values[0].toString();
        int id = Integer.parseInt(values[1].toString());
        Comanda c = new Comanda(comandaId, nome, id);
        lista.add(c);
        System.out.println("O CLIENTE ID É: "+c.getClienteId());
        comandaDAO.inserirComanda(c);
    }
    public void excluir(Comanda c) throws ComandaException {
        if (c.getValorPago() >= c.getValorTotal()) {
            lista.remove(c);
            comandaDAO.excluirComanda(c);
        }
    }
    public void refresh() throws ComandaException {
        lista.clear();
        lista.addAll(comandaDAO.refreshComandas());
    }

    public void limpar() {
        this.id.set(0);
    }

    public void entityToBoundary(Comanda c) {
        if (c != null) {
            this.id.set(c.getId());
            Cliente cliente;
            try {
                cliente = comandaDAO.getClienteById(c.getClienteId());
                System.out.println(cliente.getNome());
                this.cpf.set(cliente.getCpf());
            }
             catch (ComandaException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    
    public ObservableList<Comanda> getLista() {
        return this.lista;
    }
    public IntegerProperty idProperty() {
        return this.id;
    }

    public StringProperty nomeProperty() {
        return this.cpf;
    }
}