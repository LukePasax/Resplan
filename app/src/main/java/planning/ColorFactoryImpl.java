package planning;

public class ColorFactoryImpl implements ColorFactory {

	@Override
	public Color createColorFromRGB(int cRed, int cGreen, int cBlue) {
		
		if(cRed < 0 || cRed > 255 || cGreen < 0 || cGreen > 255 || cBlue < 0 || cBlue > 255) {
			throw new IllegalColorException("Wrong parameter");
		}
		
		return new Color() {
			
			private String red;
			private String green;
			private String blue;
			
			@Override
			public String getColorString() {
				
				this.red = Integer.toHexString(cRed).toUpperCase();
				this.green = Integer.toHexString(cGreen).toUpperCase();
				this.blue = Integer.toHexString(cBlue).toUpperCase();
				if(red.length() == 1) {
					red = "0" + red;
				}
				if(green.length() == 1) {
					green = "0" + green;
				}
				if(blue.length() == 1) {
					blue = "0" + blue;
				}
					
				return red + green + blue;
			}
		};
	}

	@Override
	public Color createColorFromHexadecimal(String hexaColor) {
		
		final String color = hexaColor.toUpperCase();
		boolean check = false;
		
		if(color.length() != 6) {
			throw new IllegalColorException("Wrong length");								
		}
		if(color.length() == 6) {
			for(int i = 1; i <= 6; i++) {
				for(HexadecimalDigits d : HexadecimalDigits.values()) {
					if(color.substring(i-1, i).equals(d.toString())) {
						check = true;
					}
				}
				if(!check) {
					throw new IllegalColorException("Wrong parameter");								
				}
				check = false;
			}
		}
		
		return new Color() {
			
			@Override
			public String getColorString() {
				return color;
			}
		};
	}

}
