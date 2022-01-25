package Relations;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Employee class here where all Drugs' operations are occurred
 * 
 * @version 12 January 2022
 * @author Ahmad Abbas
 *
 */
public class Employee {

	private static ArrayList<Employee> data = new ArrayList<Employee>();
	private static ObservableList<Employee> dataList;
	private static int maxID = 0;
	private static boolean access = true;
	private static int currentID = 0;
	private static String EmployeeName;
	private int ID;
	private String name;
	private String NID;
	private LocalDate dateOfWork;
	private double hourlyPaid;
	private double paid;
	private ArrayList<String> phones;
	private String password;
	private String isManager;
	private String isActive;

	/**
	 * Employee constructor
	 * 
	 * @param iD
	 * @param name
	 * @param nID
	 * @param dateOfWork
	 * @param hourlyPaid
	 * @param phones
	 * @param password
	 * @param isManager
	 * @param isActive
	 */
	public Employee(int iD, String name, String nID, LocalDate dateOfWork, double hourlyPaid, ArrayList<String> phones,
			String password, String isManager, String isActive) {
		super();
		ID = iD;
		this.name = name;
		NID = nID;
		this.dateOfWork = dateOfWork;
		this.hourlyPaid = hourlyPaid;
		this.phones = phones;
		this.password = password;
		this.isManager = isManager;
		this.isActive = isActive;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNID() {
		return NID;
	}

	public void setNID(String nID) {
		NID = nID;
	}

	public LocalDate getDateOfWork() {
		return dateOfWork;
	}

	public void setDateOfWork(LocalDate dateOfWork) {
		this.dateOfWork = dateOfWork;
	}

	public double getHourlyPaid() {
		return hourlyPaid;
	}

	public void setHourlyPaid(double hourlyPaid) {
		this.hourlyPaid = hourlyPaid;
	}

	public double getPaid() {
		return paid;
	}

	public void setPaid(double paid) {
		this.paid = paid;
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

	public static ArrayList<Employee> getData() {
		return Employee.data;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String isManager() {
		return isManager;
	}

	public void setManager(String isManager) {
		this.isManager = isManager;
	}

	public static ObservableList<Employee> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<Employee> dataList) {
		Employee.dataList = dataList;
	}

	public static boolean hasAccess() {
		return access;
	}

	public static void setAccess(boolean access) {
		Employee.access = access;
	}

	public String getIsManager() {
		return isManager;
	}

	public void setIsManager(String isManager) {
		this.isManager = isManager;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public static int getCurrentID() {
		return currentID;
	}

	public static void setCurrentID(int currentID) {
		Employee.currentID = currentID;
	}

	public static int getMaxID() {
		return maxID;
	}

	public static void setMaxID(int maxID) {
		Employee.maxID = maxID;
	}

	public static boolean isAccess() {
		return access;
	}

	public String isActive() {
		return isActive;
	}

	public static void setData(ArrayList<Employee> data) {
		Employee.data = data;
	}

	public static String getEmployeeName() {
		return EmployeeName;
	}

	public static void setEmployeeName(String employeeName) {
		EmployeeName = employeeName;
	}

	public static void getEmployeeData() throws ClassNotFoundException, SQLException, ParseException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Employee where Employee_ID<>-1;", null);

		for (int i = 0; i < table.size(); i++) {
			LocalDate date = LocalDate.parse(table.get(i).get(3));

			Employee temp = new Employee(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					table.get(i).get(2), date, Double.parseDouble(table.get(i).get(4)), null, table.get(i).get(5),
					table.get(i).get(6), table.get(i).get(7));

			getEmployeePhone(temp);
			data.add(temp);
		}
		maxID = Integer.parseInt(Queries.queryResult("select max(employee_ID) from employee;", null).get(0).get(0));
		dataList = FXCollections.observableArrayList(data);
	}

	public static ArrayList<Employee> getEmployeeData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<Employee> tempData = new ArrayList<Employee>();

		for (int i = 0; i < table.size(); i++) {
			LocalDate date = LocalDate.parse(table.get(i).get(3));

			Employee temp = new Employee(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					table.get(i).get(2), date, Double.parseDouble(table.get(i).get(4)), null, table.get(i).get(5),
					table.get(i).get(6), table.get(i).get(7));
			getEmployeePhone(temp);
			tempData.add(temp);
		}
		return tempData;
	}

	public static void getEmployeePhone(Employee employee) throws SQLException, ClassNotFoundException {
		ArrayList<ArrayList<String>> phonesSet = Queries.queryResult(
				"select Phone from Employee_Phone where Employee_ID=? ;",
				new ArrayList<>(Arrays.asList(employee.getID() + "")));

		ArrayList<String> phones = new ArrayList<String>();

		for (int i = 0; i < phonesSet.size(); i++) {
			phones.add(phonesSet.get(i).get(0));
		}

		employee.setPhones(phones);
	}

	public void clearEmployeePhones() {
		for (int i = 0; i < getPhones().size(); i++) {
			try {
				Queries.queryResult("delete from  Employee_Phone where Employee_ID=? ;",
						new ArrayList<>(Arrays.asList(getID() + "")));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void insertEmployeePhone(String phone, int employeeID) {

		try {
			Queries.queryUpdate("Insert into Employee_Phone (Employee_ID, Phone) values(?, ?);",
					new ArrayList<>(Arrays.asList(employeeID + "", phone)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertEmployeePhone(ArrayList<String> phones, int employeeID) {
		for (String phone : phones) {
			insertEmployeePhone(phone, employeeID);
		}
	}

	public static void insertEmployee(String name, String nID, LocalDate dateOfWork, double hourlyPaid,
			ArrayList<String> phones, String password, String isManager, String isActive) {
		try {
			Queries.queryUpdate("insert into Employee values (?, ?, ?, ?, ?, ?, ?, ?);",
					new ArrayList<>(Arrays.asList((++maxID) + "", name, nID, dateOfWork.toString(), hourlyPaid + "",
							password, isManager, isActive)));
			if (phones != null) {
				insertEmployeePhone(phones, maxID);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report Employees informations on csv file
	 * 
	 * @param path The path of file
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 * @throws IOException
	 */
	public void report(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey("select employee_ID,employee_name,employee_national_Id,employee_date_of_work\r\n"
				+ "employee_hourly_paid,isManager,isActive\r\n" + "from employee;", path);
	}
}
