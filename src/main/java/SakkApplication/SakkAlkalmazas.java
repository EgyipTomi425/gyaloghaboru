package sakkApplication;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SakkAlkalmazas extends Application 
{
    @Override
    public void start(Stage allapot) throws IOException 
    {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        allapot.setTitle("Menyhárt Tamás: Gyalogháború");
        Scene scene = new Scene(root);
        allapot.setScene(scene);
        allapot.setResizable(false);
        allapot.show();
    }
}
