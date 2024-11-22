import java.util.HashMap;
import java.util.Map;

public class ComandaProdutoController {
    private static Map<Integer, Integer> map;
    private ComandaDAO comandaDAO;

    public ComandaProdutoController() {
        map = new HashMap<>();
        try {
            comandaDAO = new ComandaDAOimp();
        } catch (ComandaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //sql
    public void add(int idComanda, int id, int qtd) {
        map.put(id, map.getOrDefault(id, 0) + qtd);
        try {
            comandaDAO.addProdutoComanda(idComanda, id, map.get(id));
        } catch (ComandaException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void set( int id, int qtd) {
        map.put(id, qtd);
    }

    public void delete(int idComanda, int id) {
        map.remove(id);
    }

    public int get(int id) {
        return map.getOrDefault(id, 0);
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }
}
