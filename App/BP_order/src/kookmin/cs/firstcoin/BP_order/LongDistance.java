package kookmin.cs.firstcoin.BP_order;

public class LongDistance {

	// order_id, merchant_id, store_name, order_time, order_status, total,
	// content
	private String order_id = "";
	private String merchant_id = "";
	private String store_name = "";
	private String order_time = "";
	private String order_status = "";
	private String price = "";
	private String content = "";
	private String total_btc = "";

	// 받아온 데이터 형태 :order_id,merchant_id,store_name, 주문시간,order_status,총금액,메뉴\n
	public LongDistance(String str) {
		String[] str_token = str.split(",");

		order_id = str_token[0];
		merchant_id = str_token[1];
		store_name = str_token[2];
		order_time = str_token[3];
		order_status = str_token[4];
		price = str_token[5];
		content = str_token[6];
		setTotal_btc(str_token[7]);
	}

	public String getOrderId() {
		return order_id;
	}

	public String getMerchantId() {
		return merchant_id;
	}

	public String getStoreName() {
		return store_name;
	}

	public String getOrderTime() {
		return order_time;
	}

	public String getOrderStatus() {
		return order_status;
	}

	public String getPrice() {
		return price;
	}

	public String getContent() {
		return content;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTotal_btc() {
		return total_btc;
	}

	public void setTotal_btc(String total_btc) {
		this.total_btc = total_btc;
	}

}
