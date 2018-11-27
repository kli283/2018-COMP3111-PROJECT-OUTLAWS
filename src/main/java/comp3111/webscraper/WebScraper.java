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
 * WebScraper provide a sample code that scrape web content. After it is constructed, you can call the method scrape with a keyword, 
 * the client will go to the default url and parse the page by looking at the HTML DOM.  
 * <br/>
 * In this particular sample code, it access to craigslist.org. You can directly search on an entry by typing the URL
 * <br/>
 * https://newyork.craigslist.org/search/sss?sort=rel&amp;query=KEYWORD
 *  <br/>
 * where KEYWORD is the keyword you want to search.
 * <br/>
 * Assume you are working on Chrome, paste the url into your browser and press F12 to load the source code of the HTML. You might be freak
 * out if you have never seen a HTML source code before. Keep calm and move on. Press Ctrl-Shift-C (or CMD-Shift-C if you got a mac) and move your
 * mouse cursor around, different part of the HTML code and the corresponding the HTML objects will be highlighted. Explore your HTML page from
 * body &rarr; section class="page-container" &rarr; form id="searchform" &rarr; div class="content" &rarr; ul class="rows" &rarr; any one of the multiple 
 * li class="result-row" &rarr; p class="result-info". You might see something like this:
 * <br/>
 * <pre>
 * {@code
 *    <p class="result-info">
 *        <span class="icon icon-star" role="button" title="save this post in your favorites list">
 *           <span class="screen-reader-text">favorite this post</span>
 *       </span>
 *       <time class="result-date" datetime="2018-06-21 01:58" title="Thu 21 Jun 01:58:44 AM">Jun 21</time>
 *       <a href="https://newyork.craigslist.org/que/clt/d/green-star-polyp-gsp-on-rock/6596253604.html" data-id="6596253604" class="result-title hdrlnk">Green Star Polyp GSP on a rock frag</a>
 *       <span class="result-meta">
 *               <span class="result-price">$15</span>
 *               <span class="result-tags">
 *                   pic
 *                   <span class="maptag" data-pid="6596253604">map</span>
 *               </span>
 *               <span class="banish icon icon-trash" role="button">
 *                   <span class="screen-reader-text">hide this posting</span>
 *               </span>
 *           <span class="unbanish icon icon-trash red" role="button" aria-hidden="true"></span>
 *           <a href="#" class="restore-link">
 *               <span class="restore-narrow-text">restore</span>
 *               <span class="restore-wide-text">restore this posting</span>
 *           </a>
 *       </span>
 *   </p>
 *}
 *</pre>
 * <br/>
 * The code 
 * <pre>
 * {@code
 * List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");
 * }
 * </pre>
 * extracts all result-row and stores the corresponding HTML elements to a list called items. Later in the loop it extracts the anchor tag 
 * &lsaquo; a &rsaquo; to retrieve the display text (by .asText()) and the link (by .getHrefAttribute()). It also extracts  
 * 
 *
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