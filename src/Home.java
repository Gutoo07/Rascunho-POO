import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Home{
    
    public Home(){
        Header header = new Header();

        VBox layout = new VBox();
        Label label = new Label("TELA INICIO");
        layout.getChildren().addAll(header,label);
        Scene scene = new Scene(layout, Main.W, Main.H);
        Main.mapScene.put("INICIO", scene);
    }

}
