package comp3111.webscraper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Vector;

/**
 * Test class for the item class.
 * 
 * @author Ruben Wijkmark
 *
 */
public class WebScraperTest {
	
	/**
	 * Testing to see if the refine logic works for a keyword that matches an item.
	 */
	@Test
	public void testRefineMatchingKeyword() {
		WebScraper scraper = new WebScraper();
		List<Item> searchRecord = new Vector<Item>();
		Item item1 = new Item();
		item1.setTitle("test");
		Item item2 = new Item();
		item2.setTitle("testing");
		searchRecord.add(item1);
		searchRecord.add(item2);
		scraper.setSearchRecord(searchRecord);
		scraper.refineSearchRecord("testing");
		List<Item> compareList = new Vector<Item>();
		compareList.add(item2);
		assertEquals(scraper.getSearchRecord(), compareList);
	}
	
	/**
	 * Testing to see if the refine logic works when the keyword matches no items in the list.
	 */
	@Test
	public void testRefineNoMatchingKeyword() {
		WebScraper scraper = new WebScraper();
		List<Item> searchRecord = new Vector<Item>();
		Item item1 = new Item();
		item1.setTitle("test");
		Item item2 = new Item();
		item2.setTitle("testing");
		searchRecord.add(item1);
		searchRecord.add(item2);
		scraper.setSearchRecord(searchRecord);
		scraper.refineSearchRecord("no match");
		List<Item> compareList = new Vector<Item>();
		assertEquals(scraper.getSearchRecord(), compareList);
	}
	
	/**
	 * Testing to see if the refine logic is not case sensitive for the keyword.
	 */
	@Test
	public void testRefineNoCaseSensitivity() {
		
		WebScraper scraper = new WebScraper();
		List<Item> searchRecord = new Vector<Item>();
		List<Item> compareList = new Vector<Item>();
		
		Item item1 = new Item();
		item1.setTitle("Hong Kong");
		Item item2 = new Item();
		item2.setTitle("testing");
		
		searchRecord.add(item1);
		searchRecord.add(item2);
		compareList.add(item2);
		
		scraper.setSearchRecord(searchRecord);
		scraper.refineSearchRecord("TESTING");

		assertEquals(scraper.getSearchRecord(), compareList);
	}
}
