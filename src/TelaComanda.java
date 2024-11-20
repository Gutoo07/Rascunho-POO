import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class TelaComanda{
    
    public TelaComanda(){
        Header header = new Header();

        VBox layout = new VBox();
        // Label label = new Label("TELA COMANDA");
        // layout.getChildren().addAll(header,label);
        Scene scene = new Scene(layout, Main.W, Main.H);

        

        layout.getChildren().addAll(header);
        Main.mapScene.put("COMANDA", scene);
    }

}
