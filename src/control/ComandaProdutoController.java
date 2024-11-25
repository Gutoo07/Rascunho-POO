package control;
import dao.ComandaDAO;
import dao.ComandaDAOimp;
import dao.ComandaException;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/*Controler da tela de Comanda */
public class ComandaProdutoController {
    private static Map<Integer, Integer> map;
    private ComandaDAO comandaDAO;

    public ComandaProdutoController() {
        map = new HashMap<>();
        try {
            comandaDAO = new ComandaDAOimp();
        } catch (ComandaException e) {
            e.printStackTrace();
        }
    }

    //sql
    public void add(int idComanda, int id, int qtd) {
        map.put(id, map.getOrDefault(id, 0) + qtd);
        try {
            comandaDAO.addProdutoComanda(idComanda, id, map.get(id));
        } catch (ComandaException e) {
            e.printStackTrace();
        }
    }

    /* Diminui o numero de qtd Produto*/
    public void less(int idComanda, int id, int qtd) {

        int qtdAtual = map.get(id) - 1;
        if(qtdAtual > 0){
            map.put(id,qtdAtual);
            try {
                
                comandaDAO.addProdutoComanda(idComanda, id, map.get(id));
            } catch (ComandaException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            delete(idComanda, id);
        }
    }

    /* Altera valor da quantidade de produto*/
    public void set( int id, int qtd) {
        map.put(id, qtd);
    }

    /* Deleta o pedido da lista*/
    public void delete(int idComanda, int id) {
        try {
            comandaDAO.removeProdutoComanda(idComanda, id);
            map.remove(id);
        } catch (ComandaException e) {
            e.printStackTrace();
        }
        
    }
    public void pesquisarProdutoNome() throws ComandaException {

    }

    public int get(int id) {
        return map.getOrDefault(id, 0);
    }
    public double getValorTotalProduto(int idComanda, int idProduto) throws ComandaException {
        return comandaDAO.getValorTotalProduto(idComanda, idProduto);
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }
}
