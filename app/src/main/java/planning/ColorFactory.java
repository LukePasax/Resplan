package planning;

/**
 *This interface contains some methods to create a Color in different ways
 */
public interface ColorFactory {
	Color createColorFromRGB(final int red, final int green, final int blue);
	
	Color createColorFromHexadecimal(final String hexaColor);
}
