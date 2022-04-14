package planning;

public enum HexadecimalDigits {
	d0("0"), 
	d1("1"), 
	d2("2"), 
	d3("3"), 
	d4("4"), 
	d5("5"), 
	d6("6"), 
	d7("7"), 
	d8("8"), 
	d9("9"), 
	dA("A"), 
	dB("B"), 
	dC("C"), 
	dD("D"), 
	dE("E"), 
	dF("F");
	
	private String value;
	private HexadecimalDigits(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
}
