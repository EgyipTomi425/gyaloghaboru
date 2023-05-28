package SakkApplication;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import modell.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.*;

public class SakkGuiController 
{
    private static final Logger logger = LogManager.getLogger(SakkGuiController.class);
    
    @FXML
    private GridPane tabla;
    
    private SakktablaModell modell = new SakktablaModell();
    private SakkLepesValaszto lepesValaszto = new SakkLepesValaszto(modell);
    
    @FXML
    private void initialize() 
    {
        for (var i = 0; i < tabla.getRowCount(); i++) 
        {
            for (var j = 0; j < tabla.getColumnCount(); j++) 
            {
                StackPane mezo = createMezo(i, j);
                tabla.add(tabla, j, i);
            }
        }
        lepesValaszto.lepesFazisProperty().addListener(this::MutassKivalasztasFazisValtozas);
    }

    private StackPane createMezo(int i, int j) 
    {
        StackPane mezo = new StackPane();
        mezo.getStyleClass().add("mezo");
        Circle kor = new Circle(50);
        kor.fillProperty().bind(createMezoBinding(modell.getMezoProperty(i, j)));
        mezo.getChildren().add(kor);
        mezo.setOnMouseClicked(this::handleMouseClick);
        return mezo;
    }

    private void handleMouseClick(MouseEvent event) 
    {
        var mezo = (StackPane) event.getSource();
        var sor = GridPane.getRowIndex(mezo);
        var oszlop = GridPane.getColumnIndex(mezo);
        logger.info("Kattintás az alábbi mezőre: ({},{})", sor, oszlop);
        lepesValaszto.kivalaszt(new Pozicio(sor, oszlop));
        if (lepesValaszto.isReadyToMove()) 
        {
            lepesValaszto.lepj();
        }
    }
    
    private ObjectBinding<Paint> createMezoBinding(ReadOnlyObjectProperty<MezoBirtokos> mezoProperty) 
    {
        return new ObjectBinding<Paint>() 
        {
            {
                super.bind(mezoProperty);
            }
            @Override
            protected Paint computeValue() 
            {
                return switch (mezoProperty.get()) 
                {
                    case URES -> Color.TRANSPARENT;
                    case VILAGOS -> Color.ORANGE;
                    case SOTET -> Color.BLACK;
                };
            }
        };
    }
    
    private void MutassKivalasztasFazisValtozas
        (ObservableValue<? extends LepesFazis> ertek, LepesFazis kezdoFazis, LepesFazis ujFazis) 
    {
        switch (ujFazis) 
        {
            case FIGURA_VALASZTAS -> {}
            case MEZO_VALASZTAS -> mutatValasztas(lepesValaszto.getFrom());
            case LEPESRE_KESZ -> elrejtValasztas(lepesValaszto.getFrom());
        }
    }

    private void mutatValasztas(Pozicio pozicio) 
    {
        var mezo = getMezo(pozicio);
        mezo.getStyleClass().add("selected");
    }

    private void hideSelection(Pozicio pozicio) 
    {
        var mezo = getMezo(pozicio);
        mezo.getStyleClass().remove("selected");
    }

    private StackPane getMezo(Pozicio pozicio) 
    {
        for (var child : tabla.getChildren()) 
        {
            if (GridPane.getRowIndex(child) == pozicio.sor() 
                    && GridPane.getColumnIndex(child) == pozicio.oszlop()) 
            {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
    }
}