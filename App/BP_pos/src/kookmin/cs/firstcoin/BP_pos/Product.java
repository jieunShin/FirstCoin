/**
 * @brief ��ǰ ������ ��
 * @details name, price, number ������ String�� index �Ѱ��� int��
 */


package kookmin.cs.firstcoin.BP_pos;

public class Product {

	private String name="";
	private String price="";
	private String number = "";
	private int index = 0;
	
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
	public int getIndex(){
		return index;
	}
	public void setIndex(int index){
		this.index = index;
	}
	
	//��ϰ� �߰��� �κ�
	public Product() {
		name = "";
		price = "";
		number = "";
		index = 0;
	}
	
	public Product(String str) {
		String[] str_token = str.split(",");
		
		name = str_token[0];
		price = str_token[1];
		number = "0";		
	}
}
