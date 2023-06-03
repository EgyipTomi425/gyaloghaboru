package sakkApplication;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginGuiController 
{
    private static final Logger logger = LogManager.getLogger(LoginGuiController.class);
    
    @FXML
    private TextField vilagosNevTextField;
    
    @FXML
    private TextField sotetNevTextField;
    
    @FXML
    private void initialize() 
    {
        vilagosNevTextField.setText(System.getProperty("user.name"));
        sotetNevTextField.setText(System.getProperty("user.name") + "2");
    }
    
    @FXML
    private void jatekInditasaGombMegnyomva(ActionEvent event) throws IOException 
    {
        logger.info("\nVilágossal fog játszani: {}.\nEllenfele sötéttel: {}",
                vilagosNevTextField.getText(), sotetNevTextField.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
        Parent root = fxmlLoader.load();

        SakkGuiController controller = fxmlLoader.getController();
        // Mivel az fxml-ben megadtam egy controller osztályt, ezért tilos itt kódban beállítani!
        // Mivel egy labelt is beállít, ezért kiemelten fontos, hogy akkor állítsuk be, ha már létezik!
        controller.setVilagosNev(vilagosNevTextField.getText());
        controller.setSotetNev(sotetNevTextField.getText());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @FXML
    private void exit(ActionEvent event) throws IOException
    {
        logger.info("Kilépés...");
        Platform.exit();
        System.exit(0);
    }
}