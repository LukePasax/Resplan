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
	}
}
