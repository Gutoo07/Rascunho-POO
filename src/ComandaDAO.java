import java.util.List;

public interface ComandaDAO {
    void inserir(Comanda c) throws ComandaException;
    void excluir(Comanda c) throws ComandaException;
    // void pesquisarPorId(int id) throws ComandaException;
    List<Comanda> refresh() throws ComandaException;
}
