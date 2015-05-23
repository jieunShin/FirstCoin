package kookmin.cs.firstcoin.BP_order;

public class StoreInfo {
	int merchant_id;
	String name = "";
	String category = "";
	String address = "";
	String phone = "";
	double x;
	double y;

	public StoreInfo() {
		merchant_id = 0;
		name = "";
		category = "";
		address = "";
		phone = "";
		x = 0.;
		y = 0.;
	}

	public StoreInfo(int id, String name, String category, String address, String phone, double x, double y) {
		merchant_id = id;
		this.name = name;
		this.category = category;
		this.address = address;
		this.phone = phone;
		this.x = x;
		this.y = y;
	}

	public StoreInfo(String str) {
		String[] str_token = str.split(",");
		this.category = str_token[0];
		merchant_id = Integer.parseInt(str_token[1]);
		this.name = str_token[2];
		this.address = str_token[3];
		this.phone = str_token[4];
		this.x = Double.parseDouble(str_token[5]);
		this.y = Double.parseDouble(str_token[6]);
	}

	public int getId() {
		return merchant_id;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setId(int id) {
		merchant_id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
}
