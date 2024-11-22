public class Comanda {
    private int id;
    private String nome;
    private double valorTotal = 0.0;
    private double valorPago = 0.0;
    private int clienteId;


    public Comanda(int id) {
        this.id = id;
    }

    public Comanda(int id,String nome, int clientId) {
        this.id = id;
        this.nome = nome;
        this.clienteId = clientId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNome(){
        return this.nome;
    }
    public int getId() {
        return this.id;
    }
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    public double getValorTotal() {
        return this.valorTotal;
    }
    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }
    public double getValorPago() {
        return this.valorPago;
    }
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    public int getClienteId() {
        return this.clienteId;
    }
}
