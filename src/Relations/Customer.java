package Relations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Customer class where Customer' operations are occurred
 * 
 * @version 12 January 2022
 * @author Aseel Sabri
 *
 */
public class Customer {

	private static ArrayList<Customer> data = new ArrayList<Customer>();
	private static ObservableList<Customer> dataList;
	private String NID;
	private String name;
	private double debt;
	private ArrayList<String> phones;

	/**
	 * Customer Constructor
	 * 
	 * @param nID
	 * @param name
	 * @param debt
	 * @param phones
	 */
	public Customer(String nID, String name, double debt, ArrayList<String> phones) {
		super();
		NID = nID;
		this.name = name;
		this.debt = debt;
		this.phones = phones;
	}

	public Customer(String nID, String name, double debt) {
		super();
		NID = nID;
		this.name = name;
		this.debt = debt;
	}

	public String getNID() {
		return NID;
	}

	public void setNID(String nID) {
		NID = nID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDebt() {
		return debt;
	}

	public void setDebt(double debt) {
		this.debt = debt;
	}

	public ArrayList<String> getPhones() {
		return phones;
	}

	public void setPhones(ArrayList<String> phones) {
		this.phones = phones;
	}

	public void addPhone(String phone) {
		this.phones.add(phone);
	}

	public static ArrayList<Customer> getData() {
		return Customer.data;
	}

	public static ObservableList<Customer> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<Customer> dataList) {
		Customer.dataList = dataList;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 */
	public static void getCustomerData() throws ClassNotFoundException, SQLException {
		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Customer order by Customer_name;",
				null);

		for (int i = 0; i < table.size(); i++) {
			Customer temp = new Customer(table.get(i).get(0), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), null);

			getCustomerPhone(temp);
			data.add(temp);
		}
		dataList = FXCollections.observableArrayList(data);
	}

	public static ArrayList<Customer> getCustomerData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException {

		ArrayList<Customer> tempData = new ArrayList<Customer>();
		for (int i = 0; i < table.size(); i++) {
			Customer temp = new Customer(table.get(i).get(0), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), null);

			getCustomerPhone(temp);
			tempData.add(temp);
		}
		return tempData;
	}

	public static void getCustomerPhone(Customer customer) throws SQLException, ClassNotFoundException {
		ArrayList<ArrayList<String>> phonesSet = Queries.queryResult(
				"select Phone from Customer_Phone where Customer_NID=? ;",
				new ArrayList<>(Arrays.asList(customer.getNID())));

		ArrayList<String> phones = new ArrayList<String>();

		for (int i = 0; i < phonesSet.size(); i++) {
			phones.add(phonesSet.get(i).get(0));
		}

		customer.setPhones(phones);
	}

	public static void insertCustomerPhone(String phone, String customerNID) {

		try {
			Queries.queryUpdate("Insert into Customer_Phone (Customer_NID, Phone) values(? ,?);",
					new ArrayList<>(Arrays.asList(customerNID, phone)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertCustomerPhone(ArrayList<String> phones, String customerNID) {
		for (String phone : phones) {
			insertCustomerPhone(phone, customerNID);
		}
	}

	public void clearCustomerPhones() {
		for (int i = 0; i < getPhones().size(); i++) {
			try {
				Queries.queryUpdate("delete from Customer_Phone where Customer_NID=? ;",
						new ArrayList<>(Arrays.asList(getNID())));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertCustomer(String NID, String name, double debt, ArrayList<String> phones) {
		try {
			Queries.queryUpdate("insert into Customer values (?, ?, ?);",
					new ArrayList<>(Arrays.asList(NID, name, debt + "")));
			if (phones != null) {
				insertCustomerPhone(phones, NID);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------------------------------------------------------------------------------
	public static void deleteCustomer(String NID) {
		try {
			Queries.queryResult("delete from Customer where Customer_NID =? ;", new ArrayList<>(Arrays.asList(NID)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report Customers information on csv file
	 * 
	 * @param path The path of file
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 * @throws IOException
	 */
	public static void report(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey(" select c.*,sum(co.order_price) \r\n"
				+ "	 from customer c,customer2order c2o,c_order co\r\n"
				+ "	 where c.customer_NID=c2o.customer_NID and c2o.order_ID=co.order_ID\r\n"
				+ "	 group by c.customer_name \r\n" + "	 having sum(co.order_price) >0\r\n"
				+ "	 union (select *,customer_debt\r\n" + "	 from customer\r\n" + "	 where customer_NID not in (\r\n"
				+ "	 select customer2order.customer_NID\r\n" + "	 from customer2order))\r\n" + "	 order by 2;",
				path);
	}

	public static void reportDangerDrugCustomer(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey(
				"select c.customer_name, p.product_name,d.Drug_Scientific_Name,d.drug_pharmacetical_category\r\n"
						+ "from customer c, c_order_batch co,drug d , customer2order c2o,product p\r\n"
						+ "where p.product_ID=d.product_ID and c.customer_NID=c2o.customer_NID and c2o.order_ID=co.Order_ID \r\n"
						+ "and co.Order_ID=c2o.order_ID and co.product_ID=d.product_ID and  ( not d.drug_pharmacetical_category like 'non' and\r\n"
						+ "d.drug_pharmacetical_category like '%%') \r\n" + "order by 1;\r\n" + "",
				path);
	}
}
