public class ProdutoComanda {
    private int idComanda;
    private int idProduto;
    private int qtd;


    public ProdutoComanda() {
    }

    public ProdutoComanda( int idProduto, int idComanda, int qtd) {
        this.idComanda = idComanda;
        this.idProduto = idProduto;
        this.qtd = qtd;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
}
