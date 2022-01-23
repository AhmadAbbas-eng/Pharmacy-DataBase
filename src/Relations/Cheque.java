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
 * Cheque class where all Cheques' operations are occurred
 * 
 * @version 12 January 2022
 * @author Ahmad Abbas
 *
 */
public class Cheque {
	private static ArrayList<Cheque> data = new ArrayList<Cheque>();
	private static ObservableList<Cheque> dataList;
	private String ID;
	private String name;
	private LocalDate dateOFWriting;
	private LocalDate dateOfCashing;
	private int paymentID;
	private int managerID;

	/**
	 * Cheque Constructor
	 * 
	 * @param iD
	 * @param name
	 * @param dateOFWriting
	 * @param dateOfCashing
	 * @param paymentID
	 * @param managerID
	 */
	public Cheque(String iD, String name, LocalDate dateOFWriting, LocalDate dateOfCashing, int paymentID,
			int managerID) {
		super();
		ID = iD;
		this.name = name;
		this.dateOFWriting = dateOFWriting;
		this.dateOfCashing = dateOfCashing;
		this.paymentID = paymentID;
		this.managerID = managerID;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDateOFWriting() {
		return dateOFWriting;
	}

	public void setDateOFWriting(LocalDate dateOFWriting) {
		this.dateOFWriting = dateOFWriting;
	}

	public LocalDate getDateOfCashing() {
		return dateOfCashing;
	}

	public void setDateOfCashing(LocalDate dateOfCashing) {
		this.dateOfCashing = dateOfCashing;
	}

	public int getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(int paymentID) {
		this.paymentID = paymentID;
	}

	public int getManagerID() {
		return managerID;
	}

	public void setManagerID(int managerID) {
		this.managerID = managerID;
	}

	public static ArrayList<Cheque> getData() {
		return Cheque.data;
	}

	public static ObservableList<Cheque> getDataList() {
		return Cheque.dataList;
	}

	public static void setDataList(ObservableList<Cheque> dataList) {
		Cheque.dataList = dataList;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 */
	public static void getChequeData() throws ClassNotFoundException, SQLException, ParseException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Cheque;", null);

		for (int i = 0; i < table.size(); i++) {
			
			LocalDate writingDate = LocalDate.parse(table.get(i).get(2));
			LocalDate cashingDate = LocalDate.parse(table.get(i).get(3));
			Cheque temp = new Cheque(table.get(i).get(0), table.get(i).get(1), writingDate, cashingDate,
					Integer.parseInt(table.get(i).get(4)), Integer.parseInt(table.get(i).get(5)));

			data.add(temp);
		}

		dataList = FXCollections.observableArrayList(data);
	}

	public static ArrayList<Cheque> getChequeData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<Cheque> tempData = new ArrayList<Cheque>();
		Connection getPhoneConnection = Queries.dataBaseConnection();

		for (int i = 0; i < table.size(); i++) {
			LocalDate writingDate = LocalDate.parse(table.get(i).get(1));
			LocalDate cashingDate = LocalDate.parse(table.get(i).get(2));
			Cheque temp = new Cheque(table.get(i).get(0), table.get(i).get(1), writingDate, cashingDate,
					Integer.parseInt(table.get(i).get(3)), Integer.parseInt(table.get(i).get(4)));

			tempData.add(temp);
		}

		getPhoneConnection.close();
		return tempData;
	}

	public static void insertCheque(String chequeID, String name, LocalDate dateOFWriting, LocalDate dateOfCashing,
			int paymentID, int managerID) {
		try {
			Queries.queryUpdate("Insert into Cheque values (?, ?, ? ,? ,?, ?);", new ArrayList<>(Arrays.asList(chequeID,
					name, dateOFWriting.toString(), dateOfCashing.toString(), paymentID + "", managerID + "")));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report Cheques informations on csv file
	 * 
	 * @param path The path of file
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 * @throws IOException
	 */
	public void report(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey("select c.cheque_ID,e.employee_name,c.due_date_of_cashing,p.payment_amount\r\n"
				+ "from employee e , cheque c, payment p\r\n"
				+ "where  c.manager_ID=e.employee_ID and c.payment_ID=p.payment_ID ;", path);
	}
}
