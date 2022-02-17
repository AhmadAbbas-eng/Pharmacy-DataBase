package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.ObservableList;

/**
 * SupplierOrderBatch represents the details of each supplier's order
 * 
 * @version 26 January 2022
 * @author Ahmad Abbas
 * @author Loor Sawalhi
 */
public class SupplierOrderBatch {
	private static ArrayList<SupplierOrderBatch> data = new ArrayList<SupplierOrderBatch>();
	private static ObservableList<SupplierOrderBatch> dataList;
	private int orderID;
	private int productID;
	private LocalDate productionDate;
	private LocalDate expiryDate;
	private int amount;

	/**
	 * Allocates a {@code SupplierOrderBatch} object and initializes it to represent
	 * the specified parameters.
	 * 
	 * @param orderID        The order ID.
	 * @param productID      The product ID.
	 * @param productionDate The batch production Date.
	 * @param expiryDate     The batch expire date.
	 * @param amount         The amount of the product on the order.
	 */
	public SupplierOrderBatch(int orderID, int productID, LocalDate productionDate, LocalDate expiryDate, int amount) {
		super();
		this.orderID = orderID;
		this.productID = productID;
		this.productionDate = productionDate;
		this.expiryDate = expiryDate;
		this.amount = amount;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int oID) {
		orderID = oID;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int pID) {
		productID = pID;
	}

	public LocalDate getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(LocalDate productionDate) {
		this.productionDate = productionDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public static ObservableList<SupplierOrderBatch> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<SupplierOrderBatch> dataList) {
		SupplierOrderBatch.dataList = dataList;
	}

	public static ArrayList<SupplierOrderBatch> getData() {
		return data;
	}

	public static void setData(ArrayList<SupplierOrderBatch> data) {
		SupplierOrderBatch.data = data;
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<SupplierOrderBatch> of data
	 */
	public static ArrayList<SupplierOrderBatch> getSupplierOrderBatchData(ArrayList<ArrayList<String>> table){
		ArrayList<SupplierOrderBatch> tempData = new ArrayList<SupplierOrderBatch>();
		for (int i = 0; i < table.size(); i++) {
			LocalDate writingDate = LocalDate.parse(table.get(i).get(2));
			LocalDate cashingDate = LocalDate.parse(table.get(i).get(3));
			SupplierOrderBatch temp = new SupplierOrderBatch(Integer.parseInt(table.get(i).get(0)),
					Integer.parseInt(table.get(i).get(1)), writingDate, cashingDate,
					Integer.parseInt(table.get(i).get(4)));

			tempData.add(temp);
		}
			return tempData;	
	}
	
	public static void insertSupplierOrderBatch(String oID, String pID, LocalDate productionDate, LocalDate expiryDate,
			int amount) {
		Queries.queryUpdate("Insert into S_order_batch values (?, ?, ?, ?, ?);", new ArrayList<>(
				Arrays.asList(oID, pID, productionDate.toString(), expiryDate.toString(), amount + "")));
	}
}
