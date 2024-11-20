public class Comanda {
    private int id;
    private double valorTotal = 0.0;
    private double valorPago = 0.0;
    private int clienteId;

    public Comanda(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
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
