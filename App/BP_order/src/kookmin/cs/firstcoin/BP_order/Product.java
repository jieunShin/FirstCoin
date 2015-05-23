package kookmin.cs.firstcoin.BP_order;

public class Product {

	String name = "";
	String price = "";
	String number = "";

	public String getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public String getPrice() {
		return price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Product() {
		name = "";
		price = "";
		number = "";
	}

	public Product(String str) {
		String[] str_token = str.split(",");

		name = str_token[0];
		price = str_token[1];
		number = "0";
	}
}
