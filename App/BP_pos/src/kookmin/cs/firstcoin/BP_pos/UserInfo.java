/**
 * @brief 사용자 정보에 대한 Model클래스
 * @details UserInfo는 사용자의 ID, 사용자 상점이름, 상인의 ID number에 대한 데이터를 가집니다.
 */

package kookmin.cs.firstcoin.BP_pos;

public class UserInfo {

	private static String id = "";
	private static String shopName = "";
	private static int merchantId;

	public static String getShopName() {
		return shopName;
	}

	public static void setShopName(String shopName) {
		UserInfo.shopName = shopName;
	}

	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		UserInfo.id = id;
	}

	public static UserInfo getClasss() {
		return UserInfo.getClasss();
	}

	public static String getMerchantId() {
		return Integer.toString(merchantId);
	}

	public static void setMerchantId(String merchantId) {
		UserInfo.merchantId = Integer.parseInt(merchantId);
	}
}