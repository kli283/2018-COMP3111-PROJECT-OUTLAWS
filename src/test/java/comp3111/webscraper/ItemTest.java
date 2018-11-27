package comp3111.webscraper;


import org.junit.Test;
import static org.junit.Assert.*;


public class ItemTest {

	@Test
	public void testSetTitle() {
		Item i = new Item();
		i.setTitle("ABCDE");
		assertEquals(i.getTitle(), "ABCDE");
	}
	
	@Test
	public void testSetSite() {
		Item i = new Item();
		i.setSite("www.example.com");
		assertEquals(i.getSite(), "www.example.com");
	}
	
	
	
}
