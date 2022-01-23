package Relations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * SupplierOrder class represents the relation between the suppliers' and their
 * orders
 * 
 * @version 12 January 2022
 * @author Loor Sawalhi
 *
 */
public class SupplierOrder {

	private static ArrayList<SupplierOrder> data = new ArrayList<SupplierOrder>();
	private static ObservableList<SupplierOrder> dataList;
	private static int maxID = 0;
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
	 * SupplierOrder Constructor
	 * 
	 * @param iD
	 * @param dateOfOrder
	 * @param cost
	 * @param discount
	 * @param dueDateOfPayment
	 * @param supplierID
	 * @param managerID
	 * @param recievedBy
	 * @param recDate
	 */
	public SupplierOrder(int iD, LocalDate dateOfOrder, double cost, double discount, LocalDate dueDateOfPayment,
			int supplierID, int managerID, int recievedBy, LocalDate recDate) {
		super();
		ID = iD;
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
		return maxID;
	}

	public static int setMaxID(int maxID) {
		SupplierOrder.maxID = maxID;
		return maxID;
	}
	public static void getSupplierOrderData() throws ClassNotFoundException, SQLException, ParseException {

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
		maxID = Integer.parseInt(Queries.queryResult("select max(order_Id) from s_order;", null).get(0).get(0));
		dataList = FXCollections.observableArrayList(data);
	}

	public static ArrayList<SupplierOrder> getSupplierOrderData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<SupplierOrder> tempData = new ArrayList<SupplierOrder>();
		Connection getPhoneConnection = Queries.dataBaseConnection();

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

		getPhoneConnection.close();
		return tempData;
	}

	public static ObservableList<SupplierOrder> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<SupplierOrder> dataList) {
		SupplierOrder.dataList = dataList;
	}

	public static void insertSupplierOrder(LocalDate dateOfOrder, double cost, double discount,
			LocalDate dueDateOfPayment, int supplierID, int managerID, int recievedBy, LocalDate recDate) {
		try {
			Queries.queryUpdate("Insert into s_order values(?, ?, ?, ?, ?, ?, ?, ?, ?);",
					new ArrayList<>(Arrays.asList((setMaxID(getMaxID() + 1)) + "", dateOfOrder.toString(), cost + "",
							discount + "", dueDateOfPayment.toString(), supplierID+"", managerID+"", recievedBy+"",
							recDate.toString())));

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report All buying from suppliers movements informations on csv file
	 * 
	 * @param path The path of file
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 * @throws IOException
	 */
	public void report(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey(
				"select s.supplier_name,so.order_cost,so.order_discount,so.recieved_date,s.supplier_dues \r\n"
						+ "from s_order so,supplier s\r\n" + "where s.supplier_Id=so.supplier_ID;",
				path);
	}

	
}
