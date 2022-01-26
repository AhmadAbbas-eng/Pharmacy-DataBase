package Relations;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * CustomerOrderBatch represents the details of each customer's order
 * 
 * @version 26 January 2022
 * @author Aseel Sabri
 *
 */
public class CustomerOrderBatch {
	private static ArrayList<CustomerOrderBatch> data = new ArrayList<CustomerOrderBatch>();
	private static ObservableList<CustomerOrderBatch> dataList;
	private int orderID;
	private int productID;
	private LocalDate productionDate;
	private LocalDate expiryDate;
	private int amount;

	/**
	 * Allocates a {@code CustomerOrderBatch} object and initializes it to represent
	 * the specified parameters.
	 * 
	 * @param orderID        The ID of Customer order.
	 * @param productID      The ID of the product.
	 * @param productionDate The batch production date.
	 * @param expiryDate     The Batch expire date.
	 * @param amount         The amount of the order for this product.
	 */
	public CustomerOrderBatch(int orderID, int productID, LocalDate productionDate, LocalDate expiryDate, int amount) {
		super();
		this.orderID = orderID;
		this.productID = productID;
		this.productionDate = productionDate;
		this.expiryDate = expiryDate;
		this.amount = amount;
	}

	public int getOID() {
		return getOrderID();
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOID(int oID) {
		setOrderID(oID);
	}

	public void setOrderID(int oID) {
		orderID = oID;
	}

	public int getPID() {
		return getProductID();
	}

	public int getProductID() {
		return productID;
	}

	public void setPID(int pID) {
		setProductID(pID);
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

	public static ObservableList<CustomerOrderBatch> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<CustomerOrderBatch> dataList) {
		CustomerOrderBatch.dataList = dataList;
	}

	public static ArrayList<CustomerOrderBatch> getData() {
		return data;
	}

	public static void setData(ArrayList<CustomerOrderBatch> data) {
		CustomerOrderBatch.data = data;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 */
	public static void getCustomerOrderBatchData() throws ClassNotFoundException, SQLException, ParseException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from S_order_batch;", null);

		for (int i = 0; i < table.size(); i++) {

			LocalDate writingDate = LocalDate.parse(table.get(i).get(2));
			LocalDate cashingDate = LocalDate.parse(table.get(i).get(3));
			CustomerOrderBatch temp = new CustomerOrderBatch(Integer.parseInt(table.get(i).get(0)),
					Integer.parseInt(table.get(i).get(1)), writingDate, cashingDate,
					Integer.parseInt(table.get(i).get(4)));

			data.add(temp);
		}

		dataList = FXCollections.observableArrayList(data);
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<CustomerOrderBatch> of data
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 */
	public static ArrayList<CustomerOrderBatch> getSupplierOrderBatchData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<CustomerOrderBatch> tempData = new ArrayList<CustomerOrderBatch>();
		Connection getPhoneConnection = Queries.dataBaseConnection();

		for (int i = 0; i < table.size(); i++) {
			LocalDate writingDate = LocalDate.parse(table.get(i).get(2));
			LocalDate cashingDate = LocalDate.parse(table.get(i).get(3));
			CustomerOrderBatch temp = new CustomerOrderBatch(Integer.parseInt(table.get(i).get(0)),
					Integer.parseInt(table.get(i).get(1)), writingDate, cashingDate,
					Integer.parseInt(table.get(i).get(4)));

			tempData.add(temp);
		}

		getPhoneConnection.close();
		return tempData;
	}

	public static void insertCustomerOrderBatch(String oID, String pID, String productionDate, String expiryDate,
			int amount) {
		try {
			Queries.queryUpdate("Insert into C_order_batch values (? ,?, ?, ?, ?);",
					new ArrayList<>(Arrays.asList(oID, pID, productionDate, expiryDate, amount + "")));

			Queries.queryUpdate(
					"update batch set Batch_Amount=Batch_Amount- ? where Product_ID=? and Batch_Production_Date=? "
							+ " and Batch_Expiry_Date=? ;",
					new ArrayList<>(Arrays.asList(amount + "", pID + "", productionDate, expiryDate)));

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
