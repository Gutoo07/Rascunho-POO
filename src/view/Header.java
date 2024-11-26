package view;
import dao.ComandaException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Header extends HBox {
    
    public Header(){

        /*Labels*/ 
        Label label1 = createText("INICIO");
        Label label3 = createText("PRODUTOS");
        Label label4 = createText("CLIENTES");
        Label labelBugDesign = new Label("TESTE");
        labelBugDesign.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-style: hidden hidden solid hidden; -fx-border-width: 2; -fx-border-color: white; -fx-opacity: 0;");

        /*Style*/
        this.setPrefHeight(60);
        setSpacing(20);
        setAlignment(Pos.CENTER_LEFT);
        setMargin(label1,new Insets(0,0,0,25));
        setStyle("-fx-pref-height: 50px; -fx-pref-width: 200px;"+
        "-fx-background-color: linear-gradient(to top right, #ff7f50, #6a5acd);");

        this.getChildren().addAll(label1,label3,label4,labelBugDesign);
    }

    /*Padrozinando o label*/
    public Label createText(String title){

        Label label = new Label(title);
        if(!Main.pageSelected.equals(title)){
            label.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;");
            label.setOnMouseEntered(e -> label.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;"));
            label.setOnMouseClicked(e -> {
                Main.pageSelected = title;
                try {
                    Main.updateComponent();
                } catch (ComandaException erro) {
                    erro.printStackTrace();
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
