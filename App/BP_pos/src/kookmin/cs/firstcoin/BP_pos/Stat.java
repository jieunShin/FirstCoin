/**
 * @brief 통계 데이터 모델
 * @details 달과 매출액을 저장
 */

package kookmin.cs.firstcoin.BP_pos;


public class Stat {

	private String month;
	private String total;
	private String total_btc;

	Stat() {

	};

	public String toString() {
		String str = month + "\t" + total + "\t";
		return str;
	}

	public Stat(String data) {
		String[] data_token = data.split(",");
		month = data_token[0];
		total = data_token[1];
		total_btc = data_token[2];
	}

	public void addstatmonth(String data) {
		String[] data_token = data.split(",");
		month = data_token[0];
		total = data_token[1];
		total_btc = data_token[2];
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotal_btc() {
		return total_btc;
	}

	public void setTotal_btc(String total_btc) {
		this.total_btc = total_btc;
	}
}
