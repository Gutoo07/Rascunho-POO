import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;
    public static final int H = 500;
    public static final int W = 900;
    public static final String PersistenceDataPage = null;
    public static Map<String,Scene> mapScene = new HashMap(); 
    public static String pageSelected = "INICIO";
    public static Cliente persistenceCliente = new Cliente(-1);
    public static Comanda persistenceComanda = new Comanda(-1);
    
    //map das telas scenes
    //tem que ter um em cada tela, e as tela vai ser divida por aqui java
    //mapScene.put("tela01", tela01);
    //mapScene.put("tela02", tela02);
    //Parent é o Tipo generico dos layouts, só anotando caso precisar criar uma função futura 
    //import javafx.scene.Parent;

    public void start(Stage primaryStage) {
        stage = primaryStage;

        stage.setTitle("Sistema Comanda | Bar do Bolivia");
        changeTela(mapScene.get("INICIO"));
    }

    public void alert(){
        System.out.println("Testando");
    }

    public static void changeTela(Scene scene){
        stage.setScene(scene);
        stage.setWidth(stage.getWidth());
        stage.setHeight(stage.getHeight());
        stage.show();
    }

    //Deus me perdoe por essa gambiarra horrivel
    public static void updateComponent() throws ComandaException{
        System.out.println("UPDATE");
        mapScene.clear();
        new Home();
        new TelaComanda();
        new TelaCliente();
        new TelaProduto();
    }



    public static void main(String[] args) throws ComandaException {
        updateComponent();
        launch(args);
    }
}
