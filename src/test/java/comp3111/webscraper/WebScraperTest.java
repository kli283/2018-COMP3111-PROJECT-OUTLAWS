package comp3111.webscraper;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Vector;

public class WebScraperTest {
	
	
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
	
	/*
	@Test
	public void testRefineNoCaseSensitivity() {
		WebScraper scraper = new WebScraper();
		List<Item> searchRecord = new Vector<Item>();
		Item item1 = new Item();
		item1.setTitle("test");
		Item item2 = new Item();
		item2.setTitle("testing");
		searchRecord.add(item1);
		searchRecord.add(item2);
		scraper.setSearchRecord(searchRecord);
		scraper.refineSearchRecord("TESTING");
		List<Item> compareList = new Vector<Item>();
		compareList.add(item2);
		assertEquals(scraper.getSearchRecord(), compareList);
	}
	*/
	

}
