package sakkApplication;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EredmenyMegjelenito 
{
    private static final Logger logger = LogManager.getLogger(EredmenyMegjelenito.class);
    
    private ArrayList<JatekosStatisztika> jatekosStatisztikak;
    
    @FXML
    private TableView<JatekosStatisztika> tableView;

    @FXML
    private TableColumn<JatekosStatisztika, String> nev;

    @FXML
    private TableColumn<JatekosStatisztika, String> gyozelmiArany;

    @FXML
    private TableColumn<JatekosStatisztika, Integer> lejatszottPartik;

    @FXML
    private TableColumn<JatekosStatisztika, String> atlagosNyertesLepesSzam;
    
    @FXML
    public void initialize() 
    {
        beolvasEredmenyXML();
        beallitTablazat();
    }
    
    void beolvasEredmenyXML()
    {
        XMLHandler xmlHandler = new XMLHandler("./target/generated-sources/jatekeredmenyek.xml");
        GameStatistics gameStatisticsCalculator = new GameStatistics(xmlHandler.readXmlFile());
        jatekosStatisztikak = gameStatisticsCalculator.getGameStatistics();
    }
    
    void beallitTablazat()
    {
        nev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        gyozelmiArany.setCellValueFactory(new PropertyValueFactory<>("gyozelmiArany"));
        lejatszottPartik.setCellValueFactory(new PropertyValueFactory<>("lejatszottPartik"));
        atlagosNyertesLepesSzam.setCellValueFactory(new PropertyValueFactory<>("atlagosNyertesLepesSzam"));
        
        tableView.setItems(FXCollections.observableArrayList(jatekosStatisztikak));

        logger.info(jatekosStatisztikak);
    }
    
    @FXML
    private void exit(ActionEvent event) throws IOException
    {
        logger.info("Kilépés...");
        Platform.exit();
        System.exit(0);
    }
}
