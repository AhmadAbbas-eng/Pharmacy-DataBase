package Relations;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * CustomerOrder class represents the relation between the customers' and their
 * orders
 * 
 * @version 26 January 2022
 * @author Aseel Sabri
 *
 */
public class CustomerOrder {

	private static ArrayList<CustomerOrder> data = new ArrayList<CustomerOrder>();
	private static ObservableList<CustomerOrder> dataList;
	private static int maxID = 0;
	private int ID;
	private LocalDate date;
	private double price;
	private double discount;
	private int employeeID;

	/**
	 * Allocates a {@code CustomerOrder} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param iD         The ID of the order.
	 * @param date       The date of the order.
	 * @param price      The price of orders' drugs.
	 * @param discount   The discount of the order.
	 * @param employeeID The employee who sold the order.
	 */
	public CustomerOrder(int iD, LocalDate date, double price, double discount, int employeeID) {
		super();
		ID = iD;
		this.date = date;
		this.price = price;
		this.discount = discount;
		this.employeeID = employeeID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public static ArrayList<CustomerOrder> getData() {
		return CustomerOrder.data;
	}

	public static ObservableList<CustomerOrder> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<CustomerOrder> dataList) {
		CustomerOrder.dataList = dataList;
	}

	public static int getMaxID() {
		return maxID;
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public static void setData(ArrayList<CustomerOrder> data) {
		CustomerOrder.data = data;
	}

	public static void setMaxID(int maxID) {
		CustomerOrder.maxID = maxID;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 */
	public static void getCustomerOrderData() throws ClassNotFoundException, SQLException, ParseException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from C_Order;", null);

		for (int i = 0; i < table.size(); i++) {

			LocalDate orderDate = LocalDate.parse(table.get(i).get(1));
			CustomerOrder temp = new CustomerOrder(Integer.parseInt(table.get(i).get(0)), orderDate,
					Double.parseDouble(table.get(i).get(2)), Double.parseDouble(table.get(i).get(3)),
					Integer.parseInt(table.get(i).get(4)));

			data.add(temp);
		}
		System.out.println(maxID);
		maxID = Integer.parseInt(Queries.queryResult("select max(order_ID) from c_order;", null).get(0).get(0));
		dataList = FXCollections.observableArrayList(data);
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<CustomerOrder> of data
	 */
	public static ArrayList<CustomerOrder> getCustomerOrderData(ArrayList<ArrayList<String>> table) {

		

		try {ArrayList<CustomerOrder> tempData = new ArrayList<CustomerOrder>();
		Connection getPhoneConnection = Queries.dataBaseConnection();

		for (int i = 0; i < table.size(); i++) {
			LocalDate orderDate = LocalDate.parse(table.get(i).get(1));
			CustomerOrder temp = new CustomerOrder(Integer.parseInt(table.get(i).get(0)), orderDate,
					Double.parseDouble(table.get(i).get(2)), Double.parseDouble(table.get(i).get(3)),
					Integer.parseInt(table.get(i).get(4)));

			tempData.add(temp);
		}
			getPhoneConnection.close();
			return tempData;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Loding Error");
			alert.setHeaderText("Can't load data from c_order");
			alert.setContentText(null);
			alert.showAndWait();
			return null;		}
		
	}

	public static void insertCustomerOrder(String date, double price, double discount, double paid, int employeeID,
			String customerNID) {
		Queries.queryUpdate("Insert into C_order values (? ,?, ?, ?, ?); ",
				new ArrayList<>(Arrays.asList((++maxID) + "", date, price + "", discount + "", employeeID + "")));

		Queries.queryUpdate("Insert into customer2order values(?, ?);",
				new ArrayList<>(Arrays.asList(customerNID, maxID + "")));

		Queries.queryUpdate("update Customer set Customer_Debt=customer_Debt+ ? where customer_nid=? ;",
				new ArrayList<>(Arrays.asList((price - discount - paid) + "", customerNID)));
	}

	/**
	 * Report Customer selling informations on csv file
	 * 
	 * @param path The path of file
	 */
	public static void report(String path){
		Queries.reportQuerey("select c.customer_name ,co.order_date,co.employee_Id, p.product_name\r\n"
				+ "from customer c,customer2order c2o,c_order co,c_order_batch cob,product p \r\n"
				+ "where c.customer_NID=c2o.customer_NID and c2o.order_ID=co.order_ID and co.order_Id=cob.order_Id and p.product_Id=cob.product_ID;",
				path);
	}
}
