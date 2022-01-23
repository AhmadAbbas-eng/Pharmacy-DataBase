package Relations;

public class OrderDrug {
	private int OrderId;
	private int BatchNo;
	private String ProductName;
	private int Quantity;
	public OrderDrug(int batchNo, String productName, int cost) {
		super();
		BatchNo = batchNo;
		ProductName = productName;
		this.Quantity = cost;
	}
	public int getOrderId() {
		return OrderId;
	}
	public void setOrderId(int orderId) {
		OrderId = orderId;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public int getBatchNo() {
		return BatchNo;
	}
	public void setBatchNo(int batchNo) {
		BatchNo = batchNo;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public int getCost() {
		return Quantity;
	}
	public void setCost(int cost) {
		this.Quantity = cost;
	}

}
