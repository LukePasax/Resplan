package planning;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestColor {
	
	@Test
	public void testCreationFromRGB() {
		final ColorFactory cf = new ColorFactoryImpl();
		
		try{
			cf.createColorFromRGB(168, 20, 260);
			fail();
		} catch (IllegalColorException e) {
			System.out.println(e);
		}
		
		assertEquals("96DC28", cf.createColorFromRGB(150, 220, 40).getColorString());
		
		assertEquals("010101", cf.createColorFromRGB(1, 1, 1).getColorString());
	}
	
	@Test
	public void testCreationFromHexadecimal() {
		final ColorFactory cf = new ColorFactoryImpl();
		
		try{
			cf.createColorFromHexadecimal("15Za21");
			fail();
		} catch (IllegalColorException e) {
			System.out.println(e);
		}
		
		try{
			cf.createColorFromHexadecimal("15fa215");
			fail();
		} catch (IllegalColorException e) {
			System.out.println(e);
		}
		
		assertEquals("D5F43A", cf.createColorFromHexadecimal("D5f43a").getColorString());
	}
}
