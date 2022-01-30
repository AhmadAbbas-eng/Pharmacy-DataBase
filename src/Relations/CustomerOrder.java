package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
		return Integer.parseInt(Queries.queryResult("select max(order_ID) from c_order;", null).get(0).get(0));
	}

	public int getEmployeeID() {
		return employeeID;
	}

	public static void setData(ArrayList<CustomerOrder> data) {
		CustomerOrder.data = data;
	}

	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */
	public static void getCustomerOrderData() {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from C_Order;", null);

		for (int i = 0; i < table.size(); i++) {

			LocalDate orderDate = LocalDate.parse(table.get(i).get(1));
			CustomerOrder temp = new CustomerOrder(Integer.parseInt(table.get(i).get(0)), orderDate,
					Double.parseDouble(table.get(i).get(2)), Double.parseDouble(table.get(i).get(3)),
					Integer.parseInt(table.get(i).get(4)));

			data.add(temp);
		}
		dataList = FXCollections.observableArrayList(data);
	}

	public static void insertCustomerOrder(String date, double price, double discount, double paid, int employeeID,
			String customerNID) {
		Queries.queryUpdate("Insert into C_order values (? ,?, ?, ?, ?); ", new ArrayList<>(
				Arrays.asList((getMaxID() + 1) + "", date, price + "", discount + "", employeeID + "")));

		Queries.queryUpdate("Insert into customer2order values(?, ?);",
				new ArrayList<>(Arrays.asList(customerNID, (getMaxID()) + "")));

		Queries.queryUpdate("update Customer set Customer_Debt=customer_Debt+ ? where customer_nid=? ;",
				new ArrayList<>(Arrays.asList((price - discount - paid) + "", customerNID)));
	}

	public static void insertCustomerOrderBatch(String oID, String pID, String productionDate, String expiryDate,
			int amount) {
		Queries.queryUpdate("Insert into C_order_batch values (? ,?, ?, ?, ?);",
				new ArrayList<>(Arrays.asList(oID, pID, productionDate, expiryDate, amount + "")));

		Queries.queryUpdate(
				"update batch set Batch_Amount=Batch_Amount- ? where Product_ID=? and Batch_Production_Date=? "
						+ " and Batch_Expiry_Date=? ;",
				new ArrayList<>(Arrays.asList(amount + "", pID + "", productionDate, expiryDate)));
	}

	/**
	 * Report Customer selling informations on csv file
	 * 
	 * @param path The path of file
	 */
	public static void report(String path) {
		Queries.reportQuerey(
				"select c.customer_name as 'Customer Name',co.order_date as 'Order Date',e.employee_name as 'Employee name', p.product_name as 'Product Name'\r\n"
						+ "from customer c,customer2order c2o,c_order co,c_order_batch cob,product p,employee e \r\n"
						+ "where co.employee_Id=e.employee_Id and c.customer_NID=c2o.customer_NID and c2o.order_ID=co.order_ID and co.order_Id=cob.order_Id and p.product_Id=cob.product_ID;",
				path);
	}
}
