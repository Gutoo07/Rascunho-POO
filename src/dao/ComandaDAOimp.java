package dao;
import model.*;

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
                "jdbc:jtds:sqlserver://%s:57480;databaseName=%s;user=%s;password=%s;", hostName, dbName, user, senha
            )); //SQLServer
        } catch (ClassNotFoundException | SQLException e) { 
            e.printStackTrace();
            throw new ComandaException(e);
            //System.out.println("Erro conexão com Banco de Dados!");
        }
    }

    @Override
    public void inserirComanda(Comanda c) throws ComandaException {
        try {
            String SQL = """
                    INSERT INTO comanda (id,clienteId) VALUES
                    (?,?)
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, c.getId());
            stm.setInt(2,c.getClienteId());
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

    public Object[] getNomeByCpf(String cpf) throws ComandaException{
        try {
            String SQL = 
                """
                SELECT nome, id
                FROM cliente
                WHERE cpf = ?;
                """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setString(1, cpf);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String id = rs.getString("id");
                Object[] values = new Object[2];
                values[0] = nome;
                values[1] = id;
                return values;
            } else {
                System.out.println("Nenhum nome encontrado para o CPF " + cpf);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }

    public String getNomeById(int id) throws ComandaException{
        try {
            String SQL = 
                """
                SELECT nome
                FROM cliente
                WHERE id = ?;
                """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                return nome;
            } else {
                System.out.println("Nenhum nome encontrado para o ID " + id);
            }
            return null;
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
                    DELETE FROM comanda_produto
                    WHERE comandaId = ?
                    DELETE FROM comanda
                    WHERE id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, c.getId());
            stm.setInt(2, c.getId());
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
                int clientId = rs.getInt("clienteId");
                String nome = getNomeById(clientId);
                // System.out.println("O NOME É: " + nome);
                Comanda c = new Comanda(rs.getInt("id"),nome,clientId);
                // c.setValorTotal(rs.getDouble("valorPago"));
                c.setValorPago(rs.getDouble("valorPago"));
                c.setClienteId(clientId);
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

    @Override
    public Cliente getClienteById(int id) throws ComandaException {
        try {
            String SQL = """
                    SELECT * FROM cliente WHERE id = ?
                    """;
            System.out.println("SELECT * FROM cliente WHERE id = " + id);
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                Cliente c = new Cliente(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setTelefone(rs.getString("telefone"));
                c.setCpf(rs.getString("cpf"));
                return c;
            }
            return null;       
        } catch (SQLException e) {
            System.out.println("Erro sql");
            //e.printStackTrace();
            throw new ComandaException(e);
        }
        
    }

    /*IF EXISTS (SELECT 1 FROM comanda_produto WHERE produtoId = 1 AND comandaId = 15)
BEGIN
    UPDATE comanda_produto
    SET qtd = 0
    WHERE produtoId = 1 AND comandaId = 15;
END
ELSE
BEGIN
    INSERT INTO comanda_produto (produtoId, comandaId, qtd)
    VALUES (1, 15, 1);
END */

    @Override
    public void addProdutoComanda(int idComanda, int idProduto, int qtd) throws ComandaException {
        try {
            String SQL = "IF EXISTS (SELECT 1 FROM comanda_produto WHERE produtoId = ? AND comandaId = ?)\n"
            + "BEGIN\n"
            + "    UPDATE comanda_produto\n"
            + "    SET qtd = ?\n"
            + "    WHERE produtoId = ? AND comandaId = ?;\n"
            + "END\n"
            + "ELSE\n"
            + "BEGIN\n"
            + "    INSERT INTO comanda_produto (produtoId, comandaId, qtd)\n"
            + "    VALUES (?, ?, ?);\n"
            + "END";
            System.out.println("QTD: "+qtd);
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, idProduto);
            stm.setInt(2, idComanda);
            stm.setInt(3, qtd);
            stm.setInt(4, idProduto);
            stm.setInt(5, idComanda);
            stm.setInt(6, idProduto);
            stm.setInt(7, idComanda);
            stm.setInt(8, qtd);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }

    @Override
    public Produto getProdutoById(int id) throws ComandaException {
        try {
            String SQL = 
                """
                SELECT *
                FROM produto
                WHERE id = ?;
                """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, id);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Produto produto = new Produto(id);
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getDouble("valor"));
                return produto;
            } else {
                System.out.println("Nenhum nome encontrado para o ID " + id);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }

    @Override
    public List<ProdutoComanda> getProdutoComandaByIdComanda(int idComanda) throws ComandaException {
        try {
            String SQL = 
                """
                SELECT *
                FROM comanda_produto cop
                INNER JOIN comanda co
                ON cop.comandaId = co.id
                WHERE co.id = ?
                """;

            List<ProdutoComanda> lista = new ArrayList<>();
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, idComanda);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ProdutoComanda produtoComanda = 
                new ProdutoComanda(
                rs.getInt("produtoId"),
                rs.getInt("comandaId"),
                rs.getInt("qtd"));
                lista.add(produtoComanda);
            }

            return lista;
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override 
    public double getValorTotalComanda(int idComanda) throws ComandaException {
        try {
            double valorTotalComanda = 0.0;
            String SQL = """
                    SELECT co.id as comanda, SUM(cop.qtd * p.valor) AS valor_total
                    FROM comanda co
                    INNER JOIN comanda_produto cop
                    ON cop.comandaId = co.id
                    INNER JOIN produto p
                    ON cop.produtoId = p.id
                    WHERE co.id = ?
                    GROUP BY co.id
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, idComanda);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                valorTotalComanda = rs.getDouble("valor_total");
            }
            return valorTotalComanda;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override
    public double getValorTotalProduto(int idComanda, int idProduto) throws ComandaException {
        try {
            Double valorTotalProduto = 0.0;
            String SQL = """
                SELECT SUM(cop.qtd * p.valor) AS valor_total_produto
                FROM comanda co
                INNER JOIN comanda_produto cop
                ON co.id = cop.comandaId
                INNER join produto p
                ON cop.produtoId = p.id
                WHERE co.id = ? AND p.id = ?
                GROUP BY p.id, co.id
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, idComanda);
            stm.setInt(2, idProduto);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                valorTotalProduto = rs.getDouble("valor_total_produto");
            }
            return valorTotalProduto;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override
    public int contarComandasAbertas() throws ComandaException {
        try {
            int comandasAbertas = 0;
            String SQL = """
                    SELECT COUNT(co.id) as comandas_abertas
                    FROM comanda co
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                comandasAbertas = rs.getInt("comandas_abertas");
            }
            return comandasAbertas;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override
    public void removeProdutoComanda(int idComanda, int idProduto) throws ComandaException {
        try {
            String SQL = 
                """  
                DELETE FROM comanda_produto
                WHERE comandaId = ? and produtoId = ?
                """;

            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, idComanda);
            stm.setInt(2, idProduto);

            int rs = stm.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override 
    public double getTotalComandas() throws ComandaException {
        try {
            double totalComandas = 0.0;
            String SQL = """
                SELECT SUM(cop.qtd * p.valor) AS total_comandas
                FROM comanda co
                INNER JOIN comanda_produto cop
                ON cop.comandaId = co.id
                INNER JOIN produto p
                ON cop.produtoId = p.id
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                totalComandas = rs.getDouble("total_comandas");
            }
            return totalComandas;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override
    public boolean getComandaVazia(int idComanda) throws ComandaException {
        try {
            String SQL = """
                SELECT co.id
                FROM comanda co
                LEFT OUTER JOIN comanda_produto cop
                ON co.id = cop.comandaId
                WHERE cop.comandaId IS NULL AND co.id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, idComanda);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
    @Override 
    public boolean produtoNaoUsado(int idProduto) throws ComandaException {
        try {
            String SQL = """
                SELECT p.id
                FROM produto p
                LEFT OUTER JOIN comanda_produto cop
                ON p.id = cop.produtoId
                WHERE cop.produtoId IS NULL AND p.id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, idProduto);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    } 
    @Override
    public boolean clienteInativo(int clienteId) throws ComandaException {
        try {
            String SQL = """
                SELECT c.id
                FROM cliente c
                LEFT OUTER JOIN comanda co
                ON c.id = co.clienteId
                WHERE co.clienteId IS NULL AND c.id = ?
                    """;
            PreparedStatement stm = con.prepareStatement(SQL);
            stm.setInt(1, clienteId);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComandaException(e);
        }
    }
}
