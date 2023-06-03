package sakkApplication;
import util.EnumImageStorage;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import modell.*;
import static util.SakkLepesValaszto.LepesFazis;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.*;

public class SakkGuiController
{
    private static final Logger logger = LogManager.getLogger(SakkGuiController.class);

    @FXML
    private GridPane tabla;

    private SakktablaModell modell; // Lusta inicializálás hatékonyabbnak bizonyult a mohóval ellentétben
    private SakkLepesValaszto lepesValaszto; // Lusta inicializálás hatékonyabbnak bizonyult a mohóval ellentétben
    
    private ImageStorage<MezoBirtokos> imageStorage = new EnumImageStorage<>(MezoBirtokos.class);
    
    private String vilagosNev = "";
    private String sotetNev = "";
    
    @FXML
    private Label jatekosNevekLabel;
    
    @FXML
    private Label winnerLabel;
    
    @FXML
    private Button newGameButton;

    public void setVilagosNev(String s)
    {
        vilagosNev = s;
        if(jatekosNevekLabel != null) setJatekosNevekLabel(); // Bessenczy-féle "misztikus..." hibák elkerülésére
        logger.info("Világos játékos neve: {}", vilagosNev);
    }
    
    public void setSotetNev(String s)
    {
        sotetNev = s;
        if(jatekosNevekLabel != null) setJatekosNevekLabel(); // Bessenczy-féle "misztikus..." hibák elkerülésére
        logger.info("Sötét játékos neve: {}", sotetNev);
    }
    
    private void setModell()
    {
        modell = new SakktablaModell();
        lepesValaszto = new SakkLepesValaszto(modell);
    }
    
    public void setJatekosNevekLabel()
    {
        jatekosNevekLabel.setText(vilagosNev + " vs " + sotetNev );
    }
    
    @FXML
    private void initialize() 
    {
        setModell();
        setJatekosNevekLabel();
        for (var i = 0; i < tabla.getRowCount(); i++) 
        {
            for (var j = 0; j < tabla.getColumnCount(); j++) 
            {
                StackPane mezo = createMezo(i, j);
                tabla.add(mezo, j, i);
            }
        }
        lepesValaszto.lepesFazisProperty().addListener(this::MutatKivalasztasFazisValtozas);
        setMezoClasses();
    }
    
    private StackPane createMezo(int i, int j) 
    {
        StackPane mezo = new StackPane();
        mezo.getStyleClass().add("mezo");
        var imageView = new ImageView();
        imageView.setFitWidth(55);
        imageView.setFitHeight(75);
        imageView.imageProperty().bind(
                new ObjectBinding<Image>() {
                    {
                        super.bind(modell.MezoProperty(i, j));
                    }
                    @Override
                    protected Image computeValue() {
                        return imageStorage.get(modell.MezoProperty(i, j).get());
                    }
                }
        );
        mezo.getChildren().add(imageView);
        mezo.setOnMouseClicked(this::handleMouseClick);
        mezo.setMinSize(80, 80);
        mezo.setMaxSize(80, 80);
        return mezo;
    }
    
    @FXML
    private void handleMouseClick(MouseEvent event) 
    {
        var mezo = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(mezo);
        var col = GridPane.getColumnIndex(mezo);
        logger.info("({},{}) mezőre kattintás történt.", row, col);
        lepesValaszto.kivalaszt(new Pozicio(row, col));
        if (lepesValaszto.isReadyToMove()) 
        {
            lepesValaszto.lepj();
            if (modell.isGameOver())
            {
                logger.info("{} nyerte meg a partit!", modell.getWinner().name());
                winnerLabel.setText(modell.getWinner().name() + " nyerte meg a partit!");
            }
        }
    }
    
    @FXML
    private void newGameButton(ActionEvent event) throws IOException
    {
        winnerLabel.setText("Játék folyamatban..."); // Ez nem megfigyelés alapú
        if(modell.isGameOver()) kiirEredmenyXML();
        modell.reset();
        elrejtMindenValasztas();
    }
    
    @FXML
    private void eredmenytablakButton(ActionEvent event) throws IOException 
    {
        logger.info("Átlépés az eredménytáblákhoz...");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/eredmenytabla.fxml"));
        Parent root = fxmlLoader.load();
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

    private void MutatKivalasztasFazisValtozas(ObservableValue<? extends LepesFazis> ertek, LepesFazis regiFazis, LepesFazis ujFazis)
    {
        switch (ujFazis) 
        {
            case FIGURA_VALASZTAS -> elrejtMindenValasztas();
            case MEZO_VALASZTAS -> mutatValasztas(lepesValaszto.getFrom());
            case LEPESRE_KESZ -> elrejtValasztas(lepesValaszto.getFrom());
        }
    }
    
    private void mutatValasztas(Pozicio pozicio)
    {
        var mezo = getMezo(pozicio);
        mezo.getStyleClass().add("kivalasztva");
    }

    private void elrejtValasztas(Pozicio pozicio) 
    {
        var mezo = getMezo(pozicio);
        mezo.getStyleClass().remove("kivalasztva");
    }
    
    private void elrejtMindenValasztas()
    {
        for (int i = 0; i < modell.TABLA_MERET; i++)
        {
            for (int j = 0; j < modell.TABLA_MERET; j++)
            {
                var mezo = getMezo(new Pozicio(i,j));
                mezo.getStyleClass().remove("kivalasztva");
            }
        }
    }
    
    private void setMezoClasses()
    {
        for (int i = 0; i < modell.TABLA_MERET; i++)
        {
            for (int j = 0; j < modell.TABLA_MERET; j++)
            {
                var mezo = getMezo(new Pozicio(i,j));
                if (i % 2 == 0 && j % 2 == 0) mezo.getStyleClass().add("vilagoshatter");
                else if (i % 2 == 1 && j % 2 == 1) mezo.getStyleClass().add("vilagoshatter");
                else mezo.getStyleClass().add("sotethatter");
            }
        }
    }

    private StackPane getMezo(Pozicio pozicio) 
    {
        for (var child : tabla.getChildren()) 
        {
            if (GridPane.getRowIndex(child) == pozicio.sor() && GridPane.getColumnIndex(child) == pozicio.oszlop())
            {
                return (StackPane) child;
            }
        }
        logger.error("Az adott pozícióban lévő mező nem létezik!");
        throw new AssertionError();
    }
    
    void kiirEredmenyXML()
    {
        XMLHandler xmlHandler = new XMLHandler("./target/generated-sources/jatekeredmenyek.xml");
        xmlHandler.addGameResult(vilagosNev, sotetNev,
                modell.getMegtettLepesekSzama(), modell.getWinner().name());
    }
}