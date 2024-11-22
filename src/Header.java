import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


//Vou usar essa classe aq em todas telas pra padronizar o HEADER do sistema
//To vendo como que vou fazer pra fazer algo sobre seleção sla
// Pensei em uma variavel static na main do tipo public static String selectedPage;
// algo do tipo
public class Header extends HBox {
    
    public Header(){
        
        //Isso aqui tem q lembrar que esses parametros vão ser usado no mapScene do main, mas eu vou ver ainda
        
        //Labels
        
        Label label1 = createText("INICIO");
        //Label label2 = createText("COMANDA");
        Label label3 = createText("PRODUTOS");
        Label label4 = createText("CLIENTES");
        //Style
        this.setPrefHeight(60);
        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setMargin(label1,new Insets(0,0,0,25)); // só gambiarra pra margin do primeiro Label 
        //Acho que esse width nem ta mudando nada mas mantem no codigo msm assim
        setStyle("-fx-pref-height: 50px; -fx-pref-width: 200px;"+
        "-fx-background-color: linear-gradient(to top right, #ff7f50, #6a5acd);");


        //Config
        this.getChildren().addAll(label1,label3,label4);

    }

    //Padrozinando o label
    public Label createText(String title){

        //Acho que vou adicionar uma condicional para pageSelected, mas tem que ver se nn vai bugar

        Label label = new Label(title);
        if(!Main.pageSelected.equals(title)){
            
            label.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;");
            label.setOnMouseEntered(e -> label.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;"));
            label.setOnMouseClicked(e -> {
                //QUALQUER QUE PESSOA QUE VER ESSA SEQUENCIA DE CÓDIGOS NÃO ALTERA NADA AQUI POR TODO AMOR Q TEM A SUA VIDA
                //PROBLEMA DE PERFOMANCE?? GAMBIARRA
                Main.pageSelected = title;
                try {
                    Main.updateComponent();
                } catch (ComandaException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Scene scene = Main.mapScene.get(title);
                Main.changeTela(scene);
            });
            label.setOnMouseExited(e -> label.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;"));
        }else{
            label.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-style: hidden hidden solid hidden ; -fx-border-width: 2; -fx-border-color: white;");

        }
        return label;
    }



}
