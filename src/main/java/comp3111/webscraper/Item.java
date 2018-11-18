package comp3111.webscraper;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javafx.scene.control.Hyperlink;

public class Item {
	private String title ; 
	private double price ;
	private Hyperlink url ;
	private Date itemDate ;
	private String site ; 
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Hyperlink getUrl() {
		return url;
	}
	
	
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
	public Date getItemDate() {
		return itemDate;
	}
	public void setItemDate(Date itemDate) {
		this.itemDate = itemDate;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	

}
