package Relations;

import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Customer class where Customer's operations are occurred
 * 
 * @version 26 January 2022
 * @author Aseel Sabri
 *
 */

@Data
public class Customer {

	@Getter
	@Setter
	private static ArrayList<Customer> data = new ArrayList<Customer>();
	@Getter
	@Setter
	private static ObservableList<Customer> dataList;
	private String NID;
	private String name;
	private double debt;
	private ArrayList<String> phones;

	/**
	 * Allocates a {@code Customer} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param NID The national ID of the Customer.
	 * @param name       Customer name.
	 * @param debt       Customer debts to the Pharmacy.
	 * @param phones     The list of Customer's phones.
	 */
	public Customer(String nationalID, String name, double debt, ArrayList<String> phones) {
		super();
		this.NID = nationalID;
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
				new ArrayList<>(Arrays.asList(customer.getNID())));

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
					new ArrayList<>(Arrays.asList(getNID())));
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
