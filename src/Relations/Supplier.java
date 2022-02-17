package Relations;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Supplier class here where all Suppliers' operations are occurred
 * 
 * @version 26 January 2022
 * @author Ahmad Abbas
 *
 */
public class Supplier {

	private static ArrayList<Supplier> data = new ArrayList<Supplier>();
	private static ObservableList<Supplier> dataList;
	private int ID;
	private String name;
	private String address;
	private String email;
	private double dues;
	private ArrayList<String> phones;

	/**
	 * Allocates a {@code Supplier} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param sid     The ID of the supplier.
	 * @param sname   The name of the supplier.
	 * @param address The address of the supplier.
	 * @param email   The email of the supplier.
	 * @param dues    The dues of the supplier.
	 * @param phones  The list of supplier's phones.
	 */
	public Supplier(int sid, String sname, String address, String email, double dues, ArrayList<String> phones) {
		super();
		this.ID = sid;
		this.name = sname;
		this.address = address;
		this.email = email;
		this.dues = dues;
		this.phones = phones;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getSname() {
		return name;
	}

	public void setSname(String sname) {
		this.name = sname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getDues() {
		return dues;
	}

	public void setDues(double dues) {
		this.dues = dues;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void setData(ArrayList<Supplier> data) {
		Supplier.data = data;
	}

	public static ArrayList<Supplier> getData() {
		return Supplier.data;
	}

	public static ObservableList<Supplier> getDataList() {
		return dataList;
	}

	public static int getMaxID() {
		return Integer.parseInt(Queries.queryResult("select max(supplier_ID) from supplier;", null).get(0).get(0));
	}
	
	public static void setDataList(ObservableList<Supplier> dataList) {
		Supplier.dataList = dataList;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */
	public static void getSupplierData() {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Supplier;", null);

		for (int i = 0; i < table.size(); i++) {

			Supplier temp = new Supplier(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					table.get(i).get(2), table.get(i).get(3), Double.parseDouble(table.get(i).get(4)), null);

			getSupplierPhone(temp);
			data.add(temp);
		}
		
		dataList = FXCollections.observableArrayList(data);
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Supplier> of data
	 */
	public static ArrayList<Supplier> getSupplierData(ArrayList<ArrayList<String>> table){

		ArrayList<Supplier> tempData = new ArrayList<Supplier>();

		for (int i = 0; i < table.size(); i++) {
			Supplier temp = new Supplier(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					table.get(i).get(2), table.get(i).get(3), Double.parseDouble(table.get(i).get(4)), null);

			getSupplierPhone(temp);
			tempData.add(temp);
		}

		return tempData;
	}

	public static void getSupplierPhone(Supplier supplier){
		ArrayList<ArrayList<String>> phonesSet = Queries.queryResult(
				"select Supplier_Phone from Supplier_Phone where Supplier_ID=? ;",
				new ArrayList<>(Arrays.asList(supplier.getID() + "")));

		ArrayList<String> phones = new ArrayList<String>();

		for (int i = 0; i < phonesSet.size(); i++) {
			phones.add(phonesSet.get(i).get(0));
		}

		supplier.setPhones(phones);
	}

	public void clearSupplierPhones() {
		for (int i = 0; i < getPhones().size(); i++) {
			Queries.queryUpdate("delete from Supplier_Phone where Supplier_ID=? ;",
					new ArrayList<>(Arrays.asList(getID() + "")));
		}
	}

	public static void insertSupplierPhone(String phone, int sID) {

		Queries.queryUpdate("Insert into Supplier_Phone (Supplier_ID, Supplier_Phone) values(?, ?);",
				new ArrayList<>(Arrays.asList(sID + "", phone)));
	}

	public static void insertSupplierPhone(ArrayList<String> phones, int Sid) {
		for (String phone : phones) {
			insertSupplierPhone(phone, Sid);
		}
	}

	public static void insertSupplier(String sname, String address, String email, double dues,
			ArrayList<String> phones) {
		Queries.queryUpdate("insert into Supplier values (?, ?, ?, ?, ?);",
				new ArrayList<>(Arrays.asList((getMaxID()+1) + "", sname, address, email, dues + "")));
		if (phones != null) {
			insertSupplierPhone(phones, getMaxID());
		}
	}
}
