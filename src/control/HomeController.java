package control;
import view.Home;
import dao.ComandaDAO;
import dao.ComandaDAOimp;
import dao.ComandaException;
import model.Comanda;
import model.Cliente;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class HomeController {
    private ObservableList<Comanda> lista = FXCollections.observableArrayList();
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty cpf = new SimpleStringProperty();
    private ComandaDAO comandaDAO;

    public HomeController() throws ComandaException {
        comandaDAO = new ComandaDAOimp();
    }

    public void adicionar() throws ComandaException {
        int comandaId = this.id.get();
        String cpf = this.cpf.get();
        Object[] values = comandaDAO.getNomeByCpf(cpf);

        for (Comanda aux : lista) {
            if ( aux.getId() == comandaId ) {
                //comanda ativa
                System.out.println("Comanda ativa");
                Home.alert(AlertType.ERROR, "Uma comanda com esse ID já existe!");
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
        if (c.getValorPago() >= comandaDAO.getValorTotalComanda(c.getId())) {
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
    public int contarComandasAbertas() throws ComandaException {
        return comandaDAO.contarComandasAbertas();
    }
    public double getTotalComandas() throws ComandaException {
        return comandaDAO.getTotalComandas();
    }
    public String getComandaVazia(int idComanda) throws ComandaException {
        if (comandaDAO.getComandaVazia(idComanda) == true) {
            return "Comanda Vazia";
        }
        return "";
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