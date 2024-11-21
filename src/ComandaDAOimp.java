import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

public class ComandaDAOimp implements ComandaDAO {

    String hostName = "localhost"; 
    String dbName = "trabalhobd"; 
    String user = "guto"; //trocar pro seu user do sql
    String senha = "guto"; //trocar pela sua senha do sql

    private Connection con = null;


    public ComandaDAOimp() throws ComandaException { 
        try { 
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            con = DriverManager.getConnection(String.format(
                "jdbc:jtds:sqlserver://%s:57480;databaseName=%s;password=%s;", hostName, dbName, user, senha
            )); //SQLServer
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }

    @Override
    public void inserirComanda(Comanda c) throws ComandaException {
        try {
            String SQL = """
                    INSERT INTO comanda (id) VALUES
                    (?)
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, c.getId());
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override
    public void inserirCliente(Cliente c) throws ComandaException {
        try {
            String SQL = """
                    INSERT INTO cliente VALUES
                    (?, ?, ?, ?)
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, c.getId());
            stm.setString(2, c.getNome());
            stm.setString(3, c.getTelefone());
            stm.setString(4, c.getCpf());
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override
    public void inserirProduto(Produto p) throws ComandaException {
        try {
            String SQL = """
                    INSERT INTO produto VALUES
                    (?, ?, ?)
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, p.getId());
            stm.setString(2, p.getNome());
            stm.setDouble(3, p.getValor());
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }    
    @Override
    public void atualizarCliente(Cliente c) throws ComandaException {
        try {
            String SQL = """
                    UPDATE cliente
                    SET nome = ?,
                        telefone = ?,
                         cpf= ?
                    WHERE id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setString(1, c.getNome());
            stm.setString(2, c.getTelefone());
            stm.setString(3, c.getCpf());
            stm.setInt(4, c.getId());
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }    
    @Override
    public void atualizarProduto(Produto p) throws ComandaException {
        try {
            String SQL = """
                    UPDATE produto
                    SET nome = ?,
                        valor = ?
                    WHERE id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setString(1, p.getNome());
            stm.setDouble(2, p.getValor());
            stm.setInt(3, p.getId());
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }      
    @Override
    public void excluirComanda(Comanda c) throws ComandaException {
        try {
            String SQL = """
                    DELETE FROM comanda
                    WHERE id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, c.getId());
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override
    public void excluirCliente(Cliente c) throws ComandaException {
        try {
            String SQL = """
                    DELETE FROM cliente
                    WHERE id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, c.getId());
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }    
    @Override
    public void excluirProduto(Produto p) throws ComandaException {
        try {
            String SQL = """
                    DELETE FROM produto
                    WHERE id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, p.getId());
            int i = stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }   
    @Override
    public List<Comanda> refreshComandas() throws ComandaException {
        List<Comanda> lista = new ArrayList<>();
        try {
            String SQL = """
                    SELECT *
                    FROM comanda
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                Comanda c = new Comanda(rs.getInt("id"));
                c.setValorTotal(rs.getDouble("valorPago"));
                c.setValorPago(rs.getDouble("valorPago"));
                c.setClienteId(rs.getInt("clienteId"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
        return lista;
    }
    @Override
    public List<Cliente> refreshClientes() throws ComandaException {
        List<Cliente> lista = new ArrayList<>();
        try {
            String SQL = """
                    SELECT * FROM cliente
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                Cliente c = new Cliente(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
        return lista;
    }    
    public List<Produto> refreshProdutos() throws ComandaException {
        List<Produto> lista = new ArrayList<>();
        try {
            String SQL = """
                    SELECT * FROM produto
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                Produto p = new Produto(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setValor(rs.getDouble("valor"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
        return lista;
    }
    @Override
    public List<Cliente> pesquisarClienteNome(String nome) throws ComandaException {
        List<Cliente> lista = new ArrayList<>();
        try {
            String SQL = """
                    SELECT * FROM cliente WHERE nome LIKE ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setString(1, "%" + nome + "%");
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                Cliente c = new Cliente(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
        return lista;
    }
    @Override
    public List<Produto> pesquisarProdutoNome(String nome) throws ComandaException {
        List<Produto> lista = new ArrayList<>();
        try {
            String SQL = """
                    SELECT * FROM produto WHERE NOME LIKE ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setString(1, "%" + nome + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Produto p = new Produto(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setValor(rs.getDouble("valor"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
        return lista;
    }
}
