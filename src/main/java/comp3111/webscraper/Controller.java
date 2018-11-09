/**
 * 
 */
package comp3111.webscraper;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.Date;


/**
 * 
 * @author kevinw
 *
 *
 * Controller class that manage GUI interaction. Please see document about JavaFX for details.
 * 
 */
public class Controller {

    @FXML 
    private Label labelCount; 

    @FXML 
    private Label labelPrice; 

    @FXML 
    private Hyperlink labelMin; 

    @FXML 
    private Hyperlink labelLatest; 

    @FXML
    private TextField textFieldKeyword;
    
    @FXML
    private TextArea textAreaConsole;
    
    private WebScraper scraper;
    
    @FXML 
    private TableView<Item> itemTable;
    
    @FXML 
    private TableColumn<Item, String> itemTitle;
    
    @FXML 
    private TableColumn<Item, Double> itemPrice;
    
    @FXML 
    private TableColumn<Item, String> itemUrl;

    @FXML 
    private TableColumn<Item, Date> itemPosted;
    
//    private ObservableList items;

    
    
    /**
     * Default controller
     */
    public Controller() {
    	scraper = new WebScraper();
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {
    	itemTitle.setCellValueFactory(new PropertyValueFactory<Item, String>("title"));
    	itemPrice.setCellValueFactory(new PropertyValueFactory<Item, Double>("price"));
    	itemUrl.setCellValueFactory(new PropertyValueFactory<Item, String>("url"));
//    	itemPosted.setCellValueFactory(new PropertyValueFactory("title"));
    }
    
    /**
     * Called when the search button is pressed.
     */
    @FXML
    private void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    	}
    	textAreaConsole.setText(output);
    	ObservableList<Item> items = FXCollections.observableList(result);
    	itemTable.setItems(items);

//    	labelCount.setText("Hi");
    	
    }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
}

