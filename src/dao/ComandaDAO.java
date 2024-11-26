package dao;
import model.*;

import java.util.List;

public interface ComandaDAO {
    /* Operacoes - Cliente */
    List<Cliente> pesquisarClienteNome(String nome) throws ComandaException;
    void inserirCliente(Cliente c) throws ComandaException;
    Object[] getNomeByCpf(String cpf) throws ComandaException;
    void atualizarCliente(Cliente c) throws ComandaException;
    String getNomeById(int id) throws ComandaException;
    Cliente getClienteById(int id) throws ComandaException;
    void excluirCliente(Cliente c) throws ComandaException;
    boolean clienteInativo(int clienteId) throws ComandaException;
    List<Cliente> refreshClientes() throws ComandaException;


    /* Operacoes - Comanda */
    void excluirComanda(Comanda c) throws ComandaException;
    void inserirComanda(Comanda c) throws ComandaException;
    List<Comanda> refreshComandas() throws ComandaException;
    int contarComandasAbertas() throws ComandaException;


    /* Operacoes - Produto */
    void inserirProduto(Produto p) throws ComandaException;
    void atualizarProduto(Produto p) throws ComandaException;
    void excluirProduto(Produto p) throws ComandaException;
    List<Produto> refreshProdutos() throws ComandaException;
    List<Produto> pesquisarProdutoNome(String nome) throws ComandaException;
    Produto getProdutoById(int id) throws ComandaException;


    /* Operacoes - Produto_Comanda */
    List<ProdutoComanda> getProdutoComandaByIdComanda(int idComanda) throws ComandaException;
    void removeProdutoComanda(int idComanda, int idProduto) throws ComandaException;
    void addProdutoComanda(int idComanda, int idProduto, int qtd) throws ComandaException;
    double getValorTotalComanda(int idComanda) throws ComandaException;
    double getValorTotalProduto(int idComanda, int idProduto) throws ComandaException;
    double getTotalComandas() throws ComandaException;
    boolean getComandaVazia(int idComanda) throws ComandaException;
    boolean produtoNaoUsado(int idProduto) throws ComandaException;

    //////////// Operacoes - Pagamento ////////////
    void realizarPagamento(Pagamento pagamento) throws ComandaException;
}
