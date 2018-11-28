/**
 * 
 */
package comp3111.webscraper;

import java.util.List;
import java.util.Vector;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;


/**
 * The entry point of the entire program. 
 * 
 * @author kevinw
 */
public class WebScraperApplication extends Application {

    private static final String UI_FILE = "/ui.fxml";  //It is very important that you put the file under folder src/main/resources/
	
	/** 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 * 
	 * This function will be called by the framework shortly after the program started. You are not required to touch any part of this.
	 */
	@Override
	public void start(Stage stage) throws Exception {
    	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(UI_FILE));
		VBox root = (VBox) loader.load();
		Scene scene =  new Scene(root);
   		stage.setScene(scene);
   		stage.setTitle("WebScraper");
   		stage.show();
   		
	}

	/**
	 * Entry point of the program. No argument should be supplied
	 * @param args - not used.
	 */
	public static void main(String args[]) {
		Application.launch(args);
	}


}
