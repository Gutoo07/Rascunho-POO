import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ComandaController {
    private ObservableList<Comanda> lista = FXCollections.observableArrayList();
    private IntegerProperty id = new SimpleIntegerProperty();

    private ComandaDAO comandaDAO;

    public ComandaController() throws ComandaException {
        comandaDAO = new ComandaDAOimp();
    }

    public void adicionar() throws ComandaException {
        int comandaId = this.id.get();
        for (Comanda aux : lista) {
            if ( aux.getId() == comandaId ) {
                //abre comanda ativa
                System.out.println("Comanda ativa");
                return;
            }
        }
        Comanda c = new Comanda(comandaId);
        lista.add(c);
        comandaDAO.inserir(c);
    }
    public void excluir(Comanda c) throws ComandaException {
        if (c.getValorPago() >= c.getValorTotal()) {
            lista.remove(c);
            comandaDAO.excluir(c);
        }
    }
    public void refresh() throws ComandaException {
        lista.clear();
        lista.addAll(comandaDAO.refresh());
    }

    public void limpar() {
        this.id.set(0);
    }

    public void entityToBoundary(Comanda c) {
        if (c != null) {
            this.id.set(c.getId());
        }
    }
    
    public ObservableList<Comanda> getLista() {
        return this.lista;
    }
    public IntegerProperty idProperty() {
        return this.id;
    }
}