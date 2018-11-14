/**
 * 
 */
package comp3111.webscraper;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private TableColumn<Item, Hyperlink> itemUrl;

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
    	itemUrl.setCellValueFactory(new PropertyValueFactory<Item, Hyperlink>("url"));
    	itemPosted.setCellValueFactory(new PropertyValueFactory<Item, Date>("itemDate"));
    }
    
    /**
     * Called when the search button is pressed.
     */
    @FXML
    private void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	double a = 0.0;
    	int no_of_nonzero_items = 0;
    	
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\t" + item.getItemDate() + "\t" + item.getSite() + "\n";
    	}
    	//Calculates total sum of prices and no of items.
    	for (Item item : result) {
    		if (item.getPrice() != 0.0 && item.getPrice() > 0.0) {
    			a = a + item.getPrice();
    			no_of_nonzero_items++;
    		}
    	}
    	textAreaConsole.setText(output);
    	
    	labelCount.setText(String.valueOf(result.size()));

    	//calculates the average price
    	double average_price = a/no_of_nonzero_items;
    	System.out.println("Average Price:");
    	System.out.println(average_price);
    	
    	labelPrice.setText(String.valueOf(average_price));
    	ObservableList<Item> items = FXCollections.observableList(result);
    	itemTable.setItems(items);
    	

    }
    
//    /**
//     * Changing to Hyperlink
//     */
//    private void addLink(final String url) {
//        final Hyperlink link = new Hyperlink(url);
//        link.setOnAction(new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent t) {
//                getHostServices().showDocument(link.getText());
//                //openBrowser(link.getText());
//            }
//
//        });
//        itemTable.getItems().addAll(link);
//    }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
}

