package comp3111.webscraper;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.Vector;
import java.util.stream.Collectors;


/**
 * WebScraper provide code that scrape web content. After it is constructed, you can call the method scrape with a keyword, 
 * the client will go to the default url and parse the page by looking at the HTML DOM. It also stores the current scraped data and the 
 * results from the previous scrape.
 */
public class WebScraper {

	private static final String DEFAULT_URL = "https://newyork.craigslist.org/";
	private static final String SECOND_URL = "https://www.preloved.co.uk/";
	private WebClient client;
	int no_of_items = 0;
	int page_no = 0;
    private List<Item> searchRecordCache;
    private List<Item> searchRecord;

	/**
	 * Default Constructor 
	 */
	public WebScraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
    	searchRecordCache = new Vector<Item>();
    	searchRecord = new Vector<Item>();
	}

	/**
	 * Scrapes web content from craigslist. Updates the stored search record cache with 
	 * the previous search results and updates the stored search record with the new search results.
	 * 
	 * @param keyword - the keyword you want to search
	 * @return A list of Item that has found. A zero size list is return if nothing is found. Null if any exception (e.g. no connectivity)
	 */
	public List<Item> scrape(String keyword) {
		Vector<Item> result = new Vector<Item>();
		try {
			
			String searchUrl2 = SECOND_URL + "search?keyword=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page2 = client.getPage(searchUrl2);
			List<?> moreItems = (List<?>) page2.getByXPath("//li[@class='search-result']");
		
			for(int i = 0; i < 50000; i++) {
				
				//The general URL for each page for a given search result
				String searchUrl = DEFAULT_URL + "search/sss?s=" + no_of_items + "&query=" + URLEncoder.encode(keyword, "UTF-8") + "&sort=rel";
				HtmlPage page = client.getPage(searchUrl);
				List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");
				page_no++;
				System.out.println("CRAIGSLIST SEARCHED");
				
				
				// If we are at the end aka last page then we break and found the last page
				if(items.size() == 0) {

					System.out.println("END of SEARCH");
					break;
				}
				System.out.println("Page No: ");
				System.out.println(page_no);
				System.out.println("\n");
	
				for (int j = 0; j < items.size(); j++) {
					
					HtmlElement htmlItem = (HtmlElement) items.get(j);
					HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='result-info']/a"));
					HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']"));
	
					HtmlElement itemDate = ((HtmlElement) htmlItem.getFirstByXPath(".//time[@class='result-date']"));
					SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					Date date = parseFormat.parse(itemDate.getAttribute("datetime"));
//					System.out.println("ITEM DATE: " + date);
					// It is possible that an item doesn't have any price, we set the price to 0.0
					// in this case
					String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
	
					Item item = new Item();
					item.setTitle(itemAnchor.asText());
					item.setUrl(itemAnchor.getHrefAttribute());

					item.setItemDate(date);
					item.setPrice(new Double(itemPrice.replace("$", "").replace(",", "")));
					item.setSite("Craigslist");
					
					
					
					result.add(item);
					
					
				}
				// Each page has 120 items by default so we move 120 for each addition page
				no_of_items = no_of_items + 120;

			}
			for (int i = 0; i < moreItems.size(); i++) {
				HtmlElement htmlItem = (HtmlElement) moreItems.get(i);
//				System.out.println("HTML ITEM: " + htmlItem);
//				HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//h2[@class='search-result__content']/a"));
				HtmlAnchor itemAnchor =  ((HtmlAnchor) htmlItem.getFirstByXPath(".//a"));

//				System.out.println("ANCHOR: " + itemAnchor);
				HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@itemprop='price']"));
				

				// It is possible that an item doesn't have any price, we set the price to 0.0
				// in this case
				String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
//				Hyperlink urlHyper = new Hyperlink(DEFAULT_URL + itemAnchor.getHrefAttribute());
//				System.out.println("PRICE: " + itemPrice);
				
//				System.out.println("TITLE: " + itemAnchor.asText());
//				System.out.println("URL: " + itemAnchor.getHrefAttribute());
				
				Item item = new Item();
				item.setTitle(itemAnchor.asText());
				item.setUrl(itemAnchor.getHrefAttribute());

				item.setPrice(new Double(itemPrice.replace("£", "").replace(",", "").replace("$", "")));
				item.setSite("Preloved");
				result.add(item);
			}
			client.close();
			// Sorting the list of results
			//List<Item> SortedResult = result.stream().sorted(Comparator.comparing(Item::getPrice)).collect(Collectors.toList());
			no_of_items = 0;
			page_no = 0;
			
			// Update the stored search record cache
			searchRecordCache.clear();
	    	searchRecordCache = searchRecord.stream().collect(Collectors.toList());
	    	
	    	// Update the stored search record
			searchRecord = result;
			
			return result;
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * Filters the stored search record.
	 * 
	 * @param keyword - The keyword that the search record will be filtered by.
	 */
	public void refineSearchRecord(String keyword){
		
    	List<Item> refinedSearchRecord = new Vector<Item>();
    	
    	for(Item item : searchRecord) {
    		if(item.getTitle().toLowerCase().contains(keyword)) {
    			refinedSearchRecord.add(item);
    		}
    	}
    	
    	searchRecord = refinedSearchRecord.stream().collect(Collectors.toList());
	}
	
	/**
	 * Sets the stored search record.
	 *  
	 * @param searchRecord - the search record to be set.
	 */
	public void setSearchRecord(List<Item> searchRecord) {
		this.searchRecord = searchRecord;
	}
	
	/**
	 * Sets the stored search record cache.
	 * 
	 * @param searchRecordCache - The search record to be set
	 */
	public void setSearchRecordCache(List<Item> searchRecordCache) {
		this.searchRecordCache = searchRecordCache;
	}
	
	/**
	 * Gets the stored search record.
	 * 
	 * @return The stored search record
	 */
	public List<Item> getSearchRecord() {
		return searchRecord;
	}
	
	/**
	 * Gets the stored search record cache (the previous search results)
	 * 
	 * @return - The stored search record cache
	 */
	public List<Item> getSearchRecordCache() {
		return searchRecordCache;
	}

}