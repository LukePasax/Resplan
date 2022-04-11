package planning;

public class ColorFactoryImpl implements ColorFactory {

	@Override
	public Color createColorFromRGB(int cRed, int cGreen, int cBlue) throws IllegalColorException {
		return new Color() {
			
			private String red;
			private String green;
			private String blue;
			
			private boolean verify() {
				if(cRed < 0 || cRed > 255 || cGreen < 0 || cGreen > 255 || cBlue < 0 || cBlue > 255) {
					return false;
				}
				this.red = Integer.toHexString(cRed);
				this.green = Integer.toHexString(cGreen);
				this.blue = Integer.toHexString(cBlue);
				return true;
			}
			
			@Override
			public String getColorString() {
				if(verify()) {
					red = red.toUpperCase();
					green = green.toUpperCase();
					blue = blue.toUpperCase();
					if(red.length() == 1) {
						red = "0" + red;
					}
					if(green.length() == 1) {
						red = "0" + green;
					}
					if(blue.length() == 1) {
						red = "0" + blue;
					}
					
					return red + green + blue;
				}
				
				throw new IllegalColorException("Wrong parameter");
				
			}
		};
	}

	@Override
	public Color createColorFromHexadecimal(String hexaColor) {
		return new Color() {
			
			private final String color = hexaColor.toUpperCase();
			private boolean check = false;
			
			@Override
			public String getColorString() {
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
				return color;
			}
		};
	}

}
