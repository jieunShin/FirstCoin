package kookmin.cs.firstcoin.BP_order;

public class Store {
	private String name;
	private String address;
	private String phoneNumber;
	private String merchantID;

	Store(String data) {
		String[] data_token = data.split(",");
		merchantID = data_token[0];
		name = data_token[1];
		address = data_token[2];
		phoneNumber = data_token[3];
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getMerchantID() {
		return merchantID;
	}

}
