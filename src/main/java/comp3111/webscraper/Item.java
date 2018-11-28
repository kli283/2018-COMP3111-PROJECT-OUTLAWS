package comp3111.webscraper;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javafx.scene.control.Hyperlink;

/**
 * Class representing an item that has been scraped from a website. 
 * 
 * @author Kaushal Kalyanasundaram, Kenny Li and Ruben Wijkmark
 */
public class Item {
	private String title ; 
	private double price ;
	private Hyperlink url ;
	private Date itemDate ;
	private String site ; 
	
	/**
	 * Get the title of the item
	 * @return the items title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Set the title of the item
	 * @param title - the title to be set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Get the price of the item
	 * @return - the items price
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Set the price of the item
	 * @param price - the price to be set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	/**
	 * Get the url of the item
	 * 
	 * @return the items url
	 */
	public Hyperlink getUrl() {
		return url;
	}
	
	/**
	 * Sets the Url of the item.
	 * 
	 * @param url - the url to be set.
	 */
	public void setUrl(String url) {
		this.url = new Hyperlink(url);
		
		this.url.setOnAction(e -> {
		    if(Desktop.isDesktopSupported())
		    {
		        try {
		            Desktop.getDesktop().browse(new URI(url));
		        } catch (IOException e1) {
		            e1.printStackTrace();
		        } catch (URISyntaxException e1) {
		            e1.printStackTrace();
		        }
		    }
		});
	}
	
	/**
	 * Get the date of the item.
	 * 
	 * @return the items date.
	 */
	public Date getItemDate() {
		return itemDate;
	}
	
	/**
	 * Sets the date of the item.
	 * 
	 * @param itemDate - the date to be set.
	 */
	public void setItemDate(Date itemDate) {
		this.itemDate = itemDate;
	}
	
	/**
	 * Gets the site of the item
	 * 
	 * @return the items site
	 */
	public String getSite() {
		return site;
	}
	
	/**
	 * Sets the site of the item. 
	 * @param site the site to be set
	 */
	public void setSite(String site) {
		this.site = site;
	}
}
