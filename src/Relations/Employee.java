package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Employee class here where all Drugs' operations are occurred
 * 
 * @version 26 January 2022
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
	private String nationalID;
	private LocalDate dateOfWork;
	private double hourlyPaid;
	private double paid;
	private ArrayList<String> phones;
	private String password;
	private String isManager;
	private String isActive;

	/**
	 * Allocates a {@code Employee} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param iD         The id of the employee.
	 * @param name       The name of the employee.
	 * @param nationalID The national id ID of the employee.
	 * @param dateOfWork The date of work of the employee.
	 * @param hourlyPaid The hour wage of the employee.
	 * @param phones     The list of phones of the employee.
	 * @param password   The password of the employee.
	 * @param isManager  The condition of being manager or not.
	 * @param isActive   The condition of being an active employee.
	 */
	public Employee(int iD, String name, String nationalID, LocalDate dateOfWork, double hourlyPaid,
			ArrayList<String> phones, String password, String isManager, String isActive) {
		super();
		ID = iD;
		this.name = name;
		this.nationalID = nationalID;
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

	public LocalDate getDateOfWork() {
		return dateOfWork;
	}

	public void setDateOfWork(LocalDate dateOfWork) {
		this.dateOfWork = dateOfWork;
	}

	public String getHourlyPaid() {
		return hourlyPaid+"";
	}
	public double getHourlyPaid1() {
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

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */
	public static void getEmployeeData() {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Employee where Employee_ID<>-1;", null);

		for (int i = 0; i < table.size(); i++) {
			LocalDate date = LocalDate.parse(table.get(i).get(3));
			Employee temp = new Employee(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					table.get(i).get(2), date, Double.parseDouble(table.get(i).get(4)), null,
					decryptPassword(table.get(i).get(1), table.get(i).get(5)), table.get(i).get(6),
					table.get(i).get(7));

			getEmployeePhone(temp);
			data.add(temp);
		}
		maxID = Integer.parseInt(Queries.queryResult("select max(employee_ID) from employee;", null).get(0).get(0));
		dataList = FXCollections.observableArrayList(data);
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Employee> of data
	 * 
	 */
	public static ArrayList<Employee> getEmployeeData(ArrayList<ArrayList<String>> table) {

		ArrayList<Employee> tempData = new ArrayList<Employee>();

		for (int i = 0; i < table.size(); i++) {
			LocalDate date = LocalDate.parse(table.get(i).get(3));

			Employee temp = new Employee(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					table.get(i).get(2), date, Double.parseDouble(table.get(i).get(4)), null,
					decryptPassword(table.get(i).get(1), table.get(i).get(5)), table.get(i).get(6),
					table.get(i).get(7));
			getEmployeePhone(temp);
			tempData.add(temp);
		}
		return tempData;
	}

	public static void getEmployeePhone(Employee employee) {
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
			Queries.queryUpdate("delete from  Employee_Phone where Employee_ID=? ;",
					new ArrayList<>(Arrays.asList(getID() + "")));
		}
	}

	public static void insertEmployeePhone(String phone, int employeeID) {

		Queries.queryUpdate("Insert into Employee_Phone (Employee_ID, Phone) values(?, ?);",
				new ArrayList<>(Arrays.asList(employeeID + "", phone)));
	}

	public static void insertEmployeePhone(ArrayList<String> phones, int employeeID) {
		for (String phone : phones) {
			insertEmployeePhone(phone, employeeID);
		}
	}

	public static void insertEmployee(String name, String nID, LocalDate dateOfWork, double hourlyPaid,
			ArrayList<String> phones, String password, String isManager, String isActive) {
		Queries.queryUpdate("insert into Employee values (?, ?, ?, ?, ?, ?, ?, ?);",
				new ArrayList<>(Arrays.asList((++maxID) + "", name, nID, dateOfWork.toString(), hourlyPaid + "",
						encryptPassword(name, password), isManager, isActive)));
		if (phones != null) {
			insertEmployeePhone(phones, maxID);
		}
	}

	public static String encryptPassword(String name, String password) {
		StringBuilder hashed = new StringBuilder();
		for (int j = 0; j < password.length(); j++) {
			int temp = (name.charAt(0) + name.charAt(name.length() - 1)) * (j + 1) + password.charAt(j);
			StringBuilder tempString = new StringBuilder(Integer.toHexString(temp));
			while (tempString.length() < 3) {
				tempString.insert(0, '0');
			}
			hashed.append(tempString.toString());
		}
		return hashed.toString();
	}

	public static String decryptPassword(String name, String hashed) {
		StringBuilder password = new StringBuilder();
		int i = 0;
		for (int j = 0; j < hashed.length(); j += 3) {
			int temp = Integer.parseInt(hashed.substring(j, j + 3), 16)
					- (name.charAt(0) + name.charAt(name.length() - 1)) * (i + 1);
			i++;
			password.append((char) temp);
		}

		return password.toString();
	}

	/**
	 * Report Employees informations on csv file
	 * 
	 * @param path The path of file
	 */
	public void report(String path) {
		Queries.reportQuery("select employee_ID as 'Employee ID',employee_name as 'Employee Name',employee_national_Id as 'Employee National ID',employee_date_of_work as 'Employee Date Of Work'\r\n"
				+ "employee_hourly_paid,isManager,isActive\r\n" + "from employee;", path);
	}
}
