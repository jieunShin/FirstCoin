package kookmin.cs.firstcoin.BP_pos;

public class LongDistance {

	private String order_id = "";
	private String user_email = "";
	private String order_time = "";
	private String order_status = "";
	private String price = "";
	private String Content = "";
	private String pickup_time;
	private String name="";
	private String phone="";
	private String total_btc = "";

	// 받아온 데이터 형태 : order_id , 메일, 주문시간, 주문상태 , 총금액 , Content \n
	public LongDistance(String str) {
		String[] str_token = str.split(",");

		order_id = str_token[0];
		user_email = str_token[1];
		order_time = str_token[2];
		order_status = str_token[3];
		price = str_token[4];
		Content = str_token[5];
		pickup_time = str_token[6];
		name = str_token[7];
		phone = str_token[8];
		setTotal_btc(str_token[9]);
	}

	public String getOrderId() {
		return order_id;
	}

	public String getContent() {
		return Content;
	}

	public String getUserEmail() {
		return user_email;
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

	public String getPickUptime() {
		return pickup_time;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setOrderId(String orderid) {
		this.order_id = orderid;
	}

	public void setOrderStatus(String orderstate) {
		this.order_status = orderstate;
	}

	public String getTotal_btc() {
		return total_btc;
	}

	public void setTotal_btc(String total_btc) {
		this.total_btc = total_btc;
	}

}
