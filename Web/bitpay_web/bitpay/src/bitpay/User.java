package bitpay;

public class User {

	private static String id = "";
	private static String passwd = "";
	private static int merchant_id = -1;
	
	public static void setId(String id) {
		User.id = id;
	}
	
	public static void setPassword(String passwd) {
		User.passwd = passwd;
	}
	
	public static void setMerchantId(int merchant_id) {
		User.merchant_id = merchant_id;
	}
	
	public static String getId() {
		return id;
	}
	
	public static String getPassword() {
		return passwd;
	}
	
	public static int getMerchantId() {
		return merchant_id;
	}
}


