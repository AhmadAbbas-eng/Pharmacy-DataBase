package Relations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Supplier class here where all Suppliers' operations are occurred
 * 
 * @version 12 January 2022
 * @author Loor Sawalhi
 *
 */
public class Supplier {

	private static ArrayList<Supplier> data = new ArrayList<Supplier>();
	private static ObservableList<Supplier> dataList;
	private static int maxID = 0;
	private int ID;
	private String name;
	private String address;
	private String email;
	private double dues;
	private ArrayList<String> phones;

	/**
	 * Supplier constructor
	 * 
	 * @param sid
	 * @param sname
	 * @param address
	 * @param email
	 * @param dues
	 * @param phones
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

	public static void setDataList(ObservableList<Supplier> dataList) {
		Supplier.dataList = dataList;
	}

	public static void getSupplierData() throws ClassNotFoundException, SQLException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Supplier;", null);

		for (int i = 0; i < table.size(); i++) {

			Supplier temp = new Supplier(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					table.get(i).get(2), table.get(i).get(3), Double.parseDouble(table.get(i).get(4)), null);

			getSupplierPhone(temp);
			data.add(temp);
		}
		maxID = Integer.parseInt(Queries.queryResult("select max(supplier_ID) from supplier;", null).get(0).get(0));
		dataList = FXCollections.observableArrayList(data);
	}

	public static ArrayList<Supplier> getSupplierData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException {

		ArrayList<Supplier> tempData = new ArrayList<Supplier>();

		for (int i = 0; i < table.size(); i++) {
			Supplier temp = new Supplier(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					table.get(i).get(2), table.get(i).get(3), Double.parseDouble(table.get(i).get(4)), null);

			getSupplierPhone(temp);
			tempData.add(temp);
		}

		return tempData;
	}

	public static void getSupplierPhone(Supplier supplier) throws SQLException, ClassNotFoundException {
		Connection getPhoneConnection = Queries.dataBaseConnection();
		String query = "select Supplier_Phone from Supplier_Phone where Supplier_ID=" + supplier.getID() + ";";
		Statement getPhoneStmt = getPhoneConnection.createStatement();
		ResultSet getPhoneSet = getPhoneStmt.executeQuery(query);
		ArrayList<String> phones = new ArrayList<String>();

		while (getPhoneSet.next())
			phones.add(getPhoneSet.getString(1));

		getPhoneConnection.close();
		getPhoneStmt.close();
		getPhoneSet.close();
		supplier.setPhones(phones);

	}

	public void clearSupplierPhones() {
		for (int i = 0; i < getPhones().size(); i++) {
			try {
				Queries.queryResult("delete from Supplier_Phone where Supplier_ID=? ;",
						new ArrayList<>(Arrays.asList(getID()+"")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertSupplierPhone(String phone, int sID) {

		try {
			Queries.queryUpdate("Insert into Supplier_Phone (Supplier_ID, Supplier_Phone) values(?, ?);",
					new ArrayList<>(Arrays.asList(sID+"", phone)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertSupplierPhone(ArrayList<String> phones, int Sid) {
		for (String phone : phones) {
			insertSupplierPhone(phone, Sid);
		}
	}

	public static void insertSupplier(String sname, String address, String email, double dues,
			ArrayList<String> phones) {
		try {
			Queries.queryUpdate("insert into Supplier values (?, ?, ?, ?, ?);",
					new ArrayList<>(Arrays.asList((++maxID) + "", sname, address, email, dues + "")));
			if (phones != null) {
				insertSupplierPhone(phones, maxID);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report Suppliers informations on csv file
	 * 
	 * @param path The path of file
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 * @throws IOException
	 */
	public static void report(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey(" select * from supplier;", path);
	}

	public static int getMaxID() {
		return maxID;
	}

	public static void setMaxID(int maxID) {
		Supplier.maxID = maxID;
	}

}
