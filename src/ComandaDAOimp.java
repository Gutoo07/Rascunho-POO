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
    public void inserir(Comanda c) throws ComandaException {
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
    public void excluir(Comanda c) throws ComandaException {
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
    // @Override
    // public void pesquisarPorId(int id) {
    //     List<Comanda> lista = new ArrayList<>();
    //     try {
    //         String SQL = """
    //                 SELECT *
    //                 FROM comanda c
    //                 INNER JOIN 
                    
    //                 """;
    //     }
    // }
    @Override
    public List<Comanda> refresh() throws ComandaException {
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
}
