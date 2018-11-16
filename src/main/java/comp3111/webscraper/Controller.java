/**
 * 
 */
package comp3111.webscraper;

import java.awt.Desktop;
import java.net.URI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.stream.Collectors;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


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
    
    @FXML
    private Button refineButton;
    
    
    
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

    	
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\t" + item.getItemDate() + "\t" + item.getSite() + "\n";
    	}
    	textAreaConsole.setText(output);
    	
    	updateTabs(result);
    	

    	
    	refineButton.setDisable(false);
    }
    
    
    
    private void updateTabs(List<Item> items) {
    	
    	double a = 0.0;
    	int no_of_nonzero_items = 0;
    	double lowest_price = 0.0;
    	
    	//Calculates total sum of prices and no of items.
    	for (Item item : items) {
    		if (item.getPrice() != 0.0 && item.getPrice() > 0.0) {
    			a = a + item.getPrice();
    			no_of_nonzero_items++;
    		}
    	}
    	
    	//calculates hyperlink smallest non zero price
    	List<Item> SortedResult = items.stream().sorted(Comparator.comparing(Item::getPrice)).collect(Collectors.toList());
    	for (Item item : SortedResult) {
    		if(item.getPrice() > 0.0) {
    			labelMin.setText(String.valueOf(item.getUrl()));
    			lowest_price = item.getPrice();

    			break;
    		} else {
    			labelMin.setText("-");
    		}
    	}
    	labelMin.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent e) {
    	        System.out.println("This link is clicked");
    	    }
    	});
    	//Hyperlink for latest Item
    	for (Item item : items) {
    		labelLatest.setText(String.valueOf(item.getUrl()));
    		break;
    		
    	}
       	labelLatest.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override
    	    public void handle(ActionEvent e) {
    	        System.out.println("This link is clicked");
    	    }
    	});
       	
    	labelCount.setText(String.valueOf(items.size()));

    	//calculates the average price
    	double average_price = a/no_of_nonzero_items;
    	System.out.println("Average Price:");
    	System.out.println(average_price);
    	
    	labelPrice.setText(String.valueOf(average_price));
    	
    	ObservableList<Item> ObservableItems = FXCollections.observableList(items);
    	itemTable.setItems(ObservableItems);
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
     * Called when the refine button is pressed.
     */
    @FXML
    private void actionRefine() {
    	
    	String keyword = textFieldKeyword.getText().toLowerCase();
    	String searchResult = textAreaConsole.getText();
    	String refinedSearchResult = "";
    	String[] itemStrings = searchResult.split("\n");
    	List<Item> result = new Vector<Item>();
    	
    	for(String string : itemStrings) {
    		String[] parts = string.split("\t");
    		String title = parts[0].toLowerCase();
    		if(title.contains(keyword)) {
    			refinedSearchResult = refinedSearchResult + string + "\n";
    			Item item = new Item();
    			item.setTitle(parts[0]);
    			item.setPrice(new Double(parts[1]));
    			item.setUrl(parts[2]);
    			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    			LocalDate dateTime = LocalDate.parse(parts[3], formatter);
    			item.setItemDate(java.sql.Date.valueOf(dateTime));
    			item.setSite(parts[3]);
    			result.add(item);
    		}
    	}
    	textAreaConsole.setText(refinedSearchResult);
    	
    	// Trigger the update process of the tabs
    	updateTabs(result);
    	
    	refineButton.setDisable(true);
    }

    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
}

