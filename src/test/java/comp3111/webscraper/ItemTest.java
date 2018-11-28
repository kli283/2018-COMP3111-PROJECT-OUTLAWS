package comp3111.webscraper;


import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the item class.
 * 
 * @author Kaushal Kalyanasundaram, Kenny Li and Ruben Wijkmark
 *
 */
public class ItemTest {

	/**
	 * Tests the setTitle method of the Item class.
	 */
	@Test
	public void testSetTitle() {
		Item i = new Item();
		i.setTitle("ABCDE");
		assertEquals(i.getTitle(), "ABCDE");
	}
	
	/**
	 * Tests the setSitle method of the Item class.
	 */
	@Test
	public void testSetSite() {
		Item i = new Item();
		i.setSite("www.example.com");
		assertEquals(i.getSite(), "www.example.com");
	}
	
	
	
}
