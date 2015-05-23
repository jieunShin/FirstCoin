package kookmin.cs.firstcoin.BP_order;

public class Transaction {

   private String date = "";
   private String content = "";
   private String price = "";
   private String total_btc = "";
   private String store_name = "";

   Transaction() {

   };

   public String toString() {
      String str = date + "\t" + content + "\t" + price;
      return str;
   }

   public Transaction(String data) {
      String[] data_token = data.split(",");
      String[] date_token = data_token[0].split(" ");
      date = date_token[0] + "\n" + date_token[1];
      price = data_token[1];
      content = data_token[2];
      setTotal_btc(data_token[3]);
      setStore_name(data_token[4]);
   }

   public void addTransaction(String data) {
      String[] data_token = data.split(",");
      String[] date_token = data_token[0].split(" ");
      date = date_token[0] + "\n" + date_token[1];
      price = data_token[1];
      content = data_token[2];
      setTotal_btc(data_token[3]);
      setStore_name(data_token[4]);
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public String getPrice() {
      return price;
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

   public String getStore_name() {
      return store_name;
   }

   public void setStore_name(String store_name) {
      this.store_name = store_name;
   }

}