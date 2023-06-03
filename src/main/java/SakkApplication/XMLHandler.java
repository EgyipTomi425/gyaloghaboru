package sakkApplication;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XML fájlok írásához és feldolgozásához használt segédosztály,
 * Linuxon volt tesztelve, de elvileg platformfüggetlen.
 */
public class XMLHandler 
{
    private static final Logger logger = LogManager.getLogger(XMLHandler.class);
    private Path xmlFilePath;

    /**
     * A fájl elérési útját adja meg a modellnek.
     * @param xmlFilePath egy elérési út /valami/ezaz%valami formátumban (lehet relatív is)
     */
    public XMLHandler(String xmlFilePath) 
    {
        this.xmlFilePath = Paths.get(xmlFilePath);
    }

    /**
     * Eltárolja az XML fájlban a játék eredményeit.
     * @param vilagos világos játékos neve
     * @param sotet sötét játékos neve
     * @param lepesek megtett lépések száma
     * @param gyoztes győztes színe (nagybetűs enum.name()-é konvertálása)
     */
    public void addGameResult(String vilagos, String sotet, int lepesek, String gyoztes) 
    {
        try 
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc;

            File file = xmlFilePath.toFile();
            if (file.exists()) 
            {
                doc = docBuilder.parse(file);
            } else 
            {
                doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("jatekok");
                doc.appendChild(rootElement);
            }

            Element jatekElement = doc.createElement("jatek");

            Element vilagosElement = doc.createElement("vilagos");
            vilagosElement.appendChild(doc.createTextNode(vilagos));
            jatekElement.appendChild(vilagosElement);

            Element sotetElement = doc.createElement("sotet");
            sotetElement.appendChild(doc.createTextNode(sotet));
            jatekElement.appendChild(sotetElement);

            Element lepesekElement = doc.createElement("lepesek");
            lepesekElement.appendChild(doc.createTextNode(String.valueOf(lepesek)));
            jatekElement.appendChild(lepesekElement);

            Element gyoztesElement = doc.createElement("gyoztes");
            gyoztesElement.appendChild(doc.createTextNode(gyoztes));
            jatekElement.appendChild(gyoztesElement);

            Node root = doc.getFirstChild();
            root.appendChild(jatekElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            logger.info("Az adatok sikeresen hozzá lettek fűzve az XML fájlhoz.");
        } catch (Exception e) 
        {
            logger.error("Hiba az XML fájlba való íráskor.");
            e.printStackTrace();
        }
    }
    
    /**
     * Beolvassa az adatokat egy ArrayListbe, és visszaadja.
     * @return a beolvasott adatok ArrayListben reprezentálva.
     */
    public ArrayList<GameResult> readXmlFile() 
    {
        ArrayList<GameResult> gameResults = new ArrayList<>();

        try 
        {
            File file = xmlFilePath.toFile();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("jatek");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String vilagos = element.getElementsByTagName("vilagos").item(0).getTextContent();
                    String sotet = element.getElementsByTagName("sotet").item(0).getTextContent();
                    int lepesek = Integer.parseInt(element.getElementsByTagName("lepesek").item(0).getTextContent());
                    String gyoztes = element.getElementsByTagName("gyoztes").item(0).getTextContent();

                    GameResult gameResult = new GameResult(vilagos, sotet, lepesek, gyoztes);
                    gameResults.add(gameResult);
                    
                    logger.info("Az adatok sikeresen beolvasva az XML fájlból.");
                }
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) 
        {
            logger.error("Hiba az XML fájlban való olvasáskor.");
            e.printStackTrace();
        }

        return gameResults;
    }
}
