import java.util.List;

public interface ComandaDAO {
    void inserirComanda(Comanda c) throws ComandaException;
    void excluirComanda(Comanda c) throws ComandaException;
    List<Comanda> refreshComandas() throws ComandaException;
    void inserirCliente(Cliente c) throws ComandaException;
    void atualizarCliente(Cliente c) throws ComandaException;
    Object[] getNomeByCpf(String cpf) throws ComandaException;
    String getNomeById(int id) throws ComandaException;
    Cliente getClienteById(int id) throws ComandaException;
    void excluirCliente(Cliente c) throws ComandaException;
    List<Cliente> refreshClientes() throws ComandaException;
    List<Cliente> pesquisarClienteNome(String nome) throws ComandaException;
    void inserirProduto(Produto p) throws ComandaException;
    void atualizarProduto(Produto p) throws ComandaException;
    void excluirProduto(Produto p) throws ComandaException;
    List<Produto> refreshProdutos() throws ComandaException;
    List<Produto> pesquisarProdutoNome(String nome) throws ComandaException;
}
