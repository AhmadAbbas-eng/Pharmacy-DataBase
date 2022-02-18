package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * SupplierOrder class represents the relation between the suppliers' and their
 * orders
 * 
 * @version 26 January 2022
 * @author Ahmad Abbas
 * @author Loor Sawalhi
 *
 */
public class SupplierOrder {

	private static ArrayList<SupplierOrder> data = new ArrayList<SupplierOrder>();
	private static ObservableList<SupplierOrder> dataList;
	private int ID;
	private LocalDate dateOfOrder;
	private double cost;
	private double discount;
	private LocalDate dueDateOfPayment;
	private int supplierID;
	private int managerID;
	private int recievedBy;
	private LocalDate recDate;

	/**
	 * Allocates a {@code SupplierOrder} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param iD               The id of the order.
	 * @param dateOfOrder      The date of the order.
	 * @param cost             The full cost of the order.
	 * @param discount         The discount of the order.
	 * @param dueDateOfPayment The date of paying the dues.
	 * @param supplierID       The supplier ID.
	 * @param managerID        The manager ID who asked for the order.
	 * @param recievedBy       The employee relieved the order.
	 * @param recDate          The date of receive.
	 */
	public SupplierOrder(int iD, LocalDate dateOfOrder, double cost, double discount, LocalDate dueDateOfPayment,
			int supplierID, int managerID, int recievedBy, LocalDate recDate) {
		super();
		this.ID = iD;
		this.dateOfOrder = dateOfOrder;
		this.cost = cost;
		this.discount = discount;
		this.dueDateOfPayment = dueDateOfPayment;
		this.supplierID = supplierID;
		this.managerID = managerID;
		this.recievedBy = recievedBy;
		this.recDate = recDate;
	}

	public int getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(int supplierID) {
		this.supplierID = supplierID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public LocalDate getDateOfOrder() {
		return dateOfOrder;
	}

	public void setDateOfOrder(LocalDate dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public LocalDate getDueDateOfPayment() {
		return dueDateOfPayment;
	}

	public void setDueDateOfPayment(LocalDate dueDateOfPayment) {
		this.dueDateOfPayment = dueDateOfPayment;
	}

	public int getSipplierID() {
		return supplierID;
	}

	public void setSipplierID(int supplierID) {
		this.supplierID = supplierID;
	}

	public int getManagerID() {
		return managerID;
	}

	public void setManagerID(int managerID) {
		this.managerID = managerID;
	}

	public int getRecievedBy() {
		return recievedBy;
	}

	public void setRecievedBy(int recievedBy) {
		this.recievedBy = recievedBy;
	}

	public LocalDate getRecDate() {
		return recDate;
	}

	public void setRecDate(LocalDate recDate) {
		this.recDate = recDate;
	}

	public static ArrayList<SupplierOrder> getData() {
		return SupplierOrder.data;
	}

	public static int getMaxID() {
		return Integer
				.parseInt(Queries.queryResult("select ifnull(max(order_Id),0) from s_order;", null).get(0).get(0));
	}

	public static ObservableList<SupplierOrder> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<SupplierOrder> dataList) {
		SupplierOrder.dataList = dataList;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */
	public static void getSupplierOrderData() {
		Supplier.getSupplierData();
		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from S_Order;", null);

		for (int i = 0; i < table.size(); i++) {

			LocalDate orderDate = LocalDate.parse(table.get(i).get(1));
			LocalDate paymentDate = LocalDate.parse(table.get(i).get(4));
			LocalDate recDate = LocalDate.parse(table.get(i).get(8));
			SupplierOrder temp = new SupplierOrder(Integer.parseInt(table.get(i).get(0)), orderDate,
					Double.parseDouble(table.get(i).get(2)), Double.parseDouble(table.get(i).get(3)), paymentDate,
					Integer.parseInt(table.get(i).get(5)), Integer.parseInt(table.get(i).get(6)),
					Integer.parseInt(table.get(i).get(7)), recDate);

			data.add(temp);
		}
		dataList = FXCollections.observableArrayList(data);
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<SupplierOrder> of data
	 */
	public static ArrayList<SupplierOrder> getSupplierOrderData(ArrayList<ArrayList<String>> table) {

		ArrayList<SupplierOrder> tempData = new ArrayList<SupplierOrder>();

		for (int i = 0; i < table.size(); i++) {
			LocalDate orderDate = LocalDate.parse(table.get(i).get(1));
			LocalDate paymentDate = LocalDate.parse(table.get(i).get(4));
			LocalDate recDate = LocalDate.parse(table.get(i).get(8));
			SupplierOrder temp = new SupplierOrder(Integer.parseInt(table.get(i).get(0)), orderDate,
					Double.parseDouble(table.get(i).get(2)), Double.parseDouble(table.get(i).get(3)), paymentDate,
					Integer.parseInt(table.get(i).get(5)), Integer.parseInt(table.get(i).get(6)),
					Integer.parseInt(table.get(i).get(7)), recDate);
			tempData.add(temp);
		}
		return tempData;
	}

	public static void insertSupplierOrder(LocalDate dateOfOrder, double cost, double discount,
			LocalDate dueDateOfPayment, int supplierID, int managerID, int recievedBy, LocalDate recDate) {
		Queries.queryUpdate("Insert into s_order values(?, ?, ?, ?, ?, ?, ?, ?, ?);",
				new ArrayList<>(Arrays.asList((getMaxID() + 1) + "", dateOfOrder.toString(), cost + "", discount + "",
						dueDateOfPayment.toString(), supplierID + "", managerID + "", recievedBy + "",
						recDate.toString())));
	}

	/**
	 * Report All buying from suppliers movements informations on csv file
	 * 
	 * @param path The path of file
	 */
	public static void report(String path) {
		Queries.reportQuery(
				"select s.supplier_name as 'Suppleir Name',so.order_cost as 'Cost',so.order_discount as 'Discount',so.recieved_date as 'Recieved Date',s.supplier_dues as 'Supplier Dues'\r\n"
						+ "from s_order so,supplier s\r\n" + "where s.supplier_Id=so.supplier_ID;",
				path);
	}
}
