package comp3111.webscraper;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.Date;


/**
 * @author Kaushal Kalyanasundaram, Kenny Li and Ruben Wijkmark
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
    
    @FXML
    private MenuItem lastSearchMenuItem;

    @FXML
    private MenuItem quitMenuItem;
    
    @FXML
    private MenuItem closeMenuItem;

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
    	scraper.scrape(textFieldKeyword.getText());
    	
    	updateTabs(scraper.getSearchRecord());
    	
    	if(!scraper.getSearchRecordCache().isEmpty()) {
    		lastSearchMenuItem.setDisable(false);
    	}
    	
    	refineButton.setDisable(false);
    }
    
    /**
     * Updates the data displayed in the different tabs. 
     * 
     * @param items - the list of items that the data will be based on.
     */
    private void updateTabs(List<Item> items) {
    	
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
    	
    	List<Item> SortedResult = items.stream().sorted(Comparator.comparing(Item::getPrice)).collect(Collectors.toList());
    	// Update the console tab
    	String output = "";
    	for (Item item : SortedResult) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\t" + item.getItemDate() + "\t" + item.getSite() + "\n";
    	}
    	textAreaConsole.setText(output);
    	
    	
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

       	
    	labelCount.setText(String.valueOf(items.size()));
    	
    	//calculates the average price
    	double average_price = a/no_of_nonzero_items;
    	System.out.println("Average Price:");
    	System.out.println(average_price);
    	
    	labelPrice.setText(String.valueOf(average_price));
    	
    	if(items.size()==0) {
    		labelPrice.setText(String.valueOf("-"));
    		labelMin.setText(String.valueOf("-"));
    		labelLatest.setText(String.valueOf("-"));
    	}
    	
    	// Update the Table tab
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
     * Called when the refine button is pressed.  Filters the searched data and keeps the 
     * items with their titles containing the keyword typed in the text area.
     */
    @FXML
    private void actionRefine() {
    	
    	String keyword = textFieldKeyword.getText().toLowerCase();
    	
    	scraper.refineSearchRecord(keyword);
    	
    	updateTabs(scraper.getSearchRecord());
    	refineButton.setDisable(true);
    }
    
    /**
     * Called when the about your team button is pressed. Makes a dialog 
     * displaying information about the team members appear.
     */
    @FXML
    private void actionAbout() {
    	final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Names: Ruben Wijkmark, Kaushal Kalyanasundaram, Kenny Li"));
        dialogVbox.getChildren().add(new Text("ITSC: rwijkmark, kkac, klian"));
        dialogVbox.getChildren().add(new Text("Github: RubenWijkmark, kaushalkalyan, kli283"));
        dialog.setTitle("About");
        dialog.setResizable(false);
        Scene dialogScene = new Scene(dialogVbox, 330, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    /**
     * Called when the last search button is pressed. Reverts the search results
     * to the previous search.
     */
    @FXML
    private void actionLast() {
    	lastSearchMenuItem.setDisable(true);
    	scraper.setSearchRecord(scraper.getSearchRecordCache().stream().collect(Collectors.toList()));
    	updateTabs(scraper.getSearchRecord());
    }
    
    /**
     * Called when the quit button is pressed. Exits the program and 
     * closes all connections.
     */
    @FXML
    private void actionQuit() {
    	scraper.closeWebClient();
    	Platform.exit();
    }
    
    /**
     * Called when the close button is pressed. Clears the current search record and 
     * initialize all tabs to their initial state.
     */
    @FXML
    private void actionClose() {
    	labelCount.setText("<total>");
    	labelLatest.setText("<Latest>");
    	labelMin.setText("<Lowest>");
    	labelPrice.setText("<AvgPrice>");
    	itemTable.getItems().clear();
    	textAreaConsole.clear();
    	scraper.getSearchRecord().clear(); 	
    	scraper.getSearchRecordCache().clear();
    }
    
}

