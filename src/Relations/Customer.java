package Relations;

import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Customer class where Customer's operations are occurred
 * 
 * @version 26 January 2022
 * @author Aseel Sabri
 *
 */
public class Customer {

	private static ArrayList<Customer> data = new ArrayList<Customer>();
	private static ObservableList<Customer> dataList;
	private String nationalID;
	private String name;
	private double debt;
	private ArrayList<String> phones;

	/**
	 * Allocates a {@code Customer} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param nationalID The national ID of the Customer.
	 * @param name       Customer name.
	 * @param debt       Customer debts to the Pharmacy.
	 * @param phones     The list of Customer's phones.
	 */
	public Customer(String nationalID, String name, double debt, ArrayList<String> phones) {
		super();
		this.nationalID = nationalID;
		this.name = name;
		this.debt = debt;
		this.phones = phones;
	}

	public Customer(String nID, String name, double debt) {
		super();
		nationalID = nID;
		this.name = name;
		this.debt = debt;
	}

	public String getNID() {
		return getNationalID();
	}

	public String getNationalID() {
		return nationalID;
	}

	public void setNID(String nID) {
		setNationalID(nID);
	}

	public void setNationalID(String nID) {
		nationalID = nID;
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
	 */
	public static void getCustomerData() {
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

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Customer> of data
	 */
	public static ArrayList<Customer> getCustomerData(ArrayList<ArrayList<String>> table) {

		ArrayList<Customer> tempData = new ArrayList<Customer>();
		for (int i = 0; i < table.size(); i++) {
			Customer temp = new Customer(table.get(i).get(0), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), null);

			getCustomerPhone(temp);
			tempData.add(temp);
		}
		return tempData;
	}

	public static void getCustomerPhone(Customer customer) {
		ArrayList<ArrayList<String>> phonesSet = Queries.queryResult(
				"select Phone from Customer_Phone where Customer_NID=? ;",
				new ArrayList<>(Arrays.asList(customer.getNationalID())));

		ArrayList<String> phones = new ArrayList<String>();

		for (int i = 0; i < phonesSet.size(); i++) {
			phones.add(phonesSet.get(i).get(0));
		}
		customer.setPhones(phones);
	}

	public static void insertCustomerPhone(String phone, String customerNID) {
		Queries.queryUpdate("Insert into Customer_Phone (Customer_NID, Phone) values(? ,?);",
				new ArrayList<>(Arrays.asList(customerNID, phone)));
	}

	public static void insertCustomerPhone(ArrayList<String> phones, String customerNID) {
		for (String phone : phones) {
			insertCustomerPhone(phone, customerNID);
		}
	}

	public void clearCustomerPhones() {
		for (int i = 0; i < getPhones().size(); i++) {
			Queries.queryUpdate("delete from Customer_Phone where Customer_NID=? ;",
					new ArrayList<>(Arrays.asList(getNationalID())));
		}
	}

	public static void insertCustomer(String NID, String name, double debt, ArrayList<String> phones) {
		Queries.queryUpdate("insert into Customer values (?, ?, ?);",
				new ArrayList<>(Arrays.asList(NID, name, debt + "")));
		if (phones != null) {
			insertCustomerPhone(phones, NID);
		}
	}


}
