package view;
import model.Cliente;
import model.Comanda;
import dao.ComandaException;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;
    public static final int H = 500;
    public static final int W = 600;
    public static final String PersistenceDataPage = null;
    public static Map<String,Scene> mapScene = new HashMap(); 
    public static String pageSelected = "INICIO";
    public static Cliente persistenceCliente = new Cliente(-1);
    public static Comanda persistenceComanda = new Comanda(-1);
    
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setHeight(700);
        stage.setWidth(800);
        stage.setTitle("Sistema Comanda | Bar do Bolivia");
        changeTela(mapScene.get("INICIO"));
    }
    public static void changeTela(Scene scene){
        stage.setScene(scene);
        stage.setWidth(stage.getWidth());
        stage.setHeight(stage.getHeight());
        stage.show();
    }
    public static void updateComponent() throws ComandaException {
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
