/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dom;

import hr.algebra.model.Player;
import hr.algebra.online.GameBoardNew;
import hr.algebra.online.TileNew;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author dnlbe
 */
public class DOMUtils {

    private static final String FILENAME = "game.xml";

    public static void saveGameBoards(ObservableList<GameBoardNew> gameBoards) {

        try {
            Document document = createDocument("GameBoards");
            gameBoards.forEach(c -> document.getDocumentElement().appendChild(createBoardElement(c, document)));
            saveDocument(document, FILENAME);
        } catch (ParserConfigurationException | TransformerException e) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static Element createBoardElement(GameBoardNew board, Document document) throws DOMException {
        Element element = document.createElement("board");
        element.appendChild(createElement(document, "nextPlayer", board.getActivePlayer().getID()));
        element.appendChild(createElement(document, "activePlayer", board.getActivePlayer().getID()));
        element.appendChild(createTilesElement(board.getTiles(), document));
        element.appendChild(createPlayerElement(board.getPlayer1(), document));
        element.appendChild(createPlayerElement(board.getPlayer2(), document));
        element.appendChild(createElement(document, "count", Integer.toString(board.getCount()) + ""));

        return element;
    }

    private static Element createTilesElement(List<TileNew> tiles, Document document) throws DOMException {
        
        Element element = document.createElement("tiles");
        tiles.forEach(c -> element.appendChild(createTileElement(c, document)));
        return element;
    }
    
    private static Element createTileElement(TileNew tile, Document document) throws DOMException {
        Element element = document.createElement("tile");
        element.setAttributeNode(createAttribute(document, "index", Integer.toString(tile.getIndex())));
        element.appendChild(createElement(document, "value", tile.getValue()));
        if (tile.getIsOpen()) {
            element.appendChild(createElement(document, "isOpen", "true"));
        }
        else{
            element.appendChild(createElement(document, "isOpen", "false"));
        }
        
        
        return element;
    }
    
    private static Element createPlayerElement(Player player, Document document) throws DOMException {
        Element element = document.createElement("player");
        element.setAttributeNode(createAttribute(document, "id", player.getID()));
        element.appendChild(createElement(document, "score", Integer.toString(player.getScore())));
        if (player.getVis()) {
            element.appendChild(createElement(document, "visible", "true"));
        }
        else{
            element.appendChild(createElement(document, "visible", "false"));
        }
        
        
        return element;
    }

    private static Document createDocument(String root) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, root, null);
    }

    private static Attr createAttribute(Document document, String name, String value) {
        Attr attr = document.createAttribute(name);
        attr.setValue(value);
        return attr;
    }

    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        //transformer.transform(new DOMSource(document), new StreamResult(System.out));
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }

    public static ObservableList<GameBoardNew> loadBoards() {
        ObservableList<GameBoardNew> boards = FXCollections.observableArrayList();
        try {
            Document document = createDocument(new File(FILENAME));
            NodeList nodes = document.getElementsByTagName("board");
            for (int i = 0; i < nodes.getLength(); i++) {
                // dangerous class cast exception
                boards.add(processBoardNode((Element) nodes.item(i)));
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, e);
        }
        return boards;
    }

    private static Document createDocument(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }

    private static GameBoardNew processBoardNode(Element element) {
        GameBoardNew board = new GameBoardNew();
        
            Player nextPlayer = new Player();
            Player activePlayer = new Player();
            
            nextPlayer.setID(element.getElementsByTagName("nextPlayer").item(0).getTextContent());
            activePlayer.setID(element.getElementsByTagName("activePlayer").item(0).getTextContent());
            
            board.setNextPlayer(nextPlayer);
            board.setActivePlayer(activePlayer);
            
            board.setTiles(processTilesNode((Element)element.getElementsByTagName("tiles").item(0)));
            Player p1 = processPlayerNode((Element) element.getElementsByTagName("player").item(0));
            Player p2 = processPlayerNode((Element) element.getElementsByTagName("player").item(1));
            board.setCount(Integer.valueOf(element.getElementsByTagName("count").item(0).getTextContent()));
            
            board.loadPlayers(p1, p2);
        return board;
    }

    private static List<TileNew> processTilesNode(Element element) {
        List<TileNew> tiles = FXCollections.observableArrayList();
        for (int i = 0; i < element.getElementsByTagName("tile").getLength(); i++) {
            tiles.add(processTileNode((Element) element.getElementsByTagName("tile").item(i)));
        }
        return tiles;
    }
    
    private static TileNew processTileNode(Element element) {
        TileNew tile = new TileNew(element.getElementsByTagName("value").item(0).getTextContent());
        tile.setIndex(Integer.valueOf(element.getAttribute("index")));
        if (element.getElementsByTagName("isOpen").item(0).getTextContent().equals("false")) {
            tile.setIsOpen(false);
        }
        else{
            tile.setIsOpen(true);
        }
        
        return tile;
    }
    
    private static Player processPlayerNode(Element element) {
        Player player = new Player();
        player.setID(element.getAttribute("id"));
        player.setScore(Integer.valueOf(element.getElementsByTagName("score").item(0).getTextContent()));
        
        if (element.getElementsByTagName("visible").item(0).getTextContent().equals("true")) {
            player.setVis(true);
        }
        else{
            player.setVis(false);
        }
        
        return player;
    }

}
