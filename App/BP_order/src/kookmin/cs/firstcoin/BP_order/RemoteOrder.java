package kookmin.cs.firstcoin.BP_order;

public class RemoteOrder {

	private String numAccept = "1"; // ������ȣ
	private String name = "";
	private String price = "";
	private String number = "";

	private String user_email = "";
	private String order_time = "";
	private String order_status = "";
	private String Content = "";

	// �޾ƿ� ������ ���� : ����,�ֹ��ð�,order_status,�ѱݾ�,�޴�\n
	public RemoteOrder(String str) {
		String[] str_token = str.split(",");

		user_email = str_token[0];
		order_time = str_token[1];
		order_status = str_token[2];
		price = str_token[3];
		Content = str_token[4];

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

	public void setNumAccept(String NumAccept) {
		this.numAccept = NumAccept;
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


}
