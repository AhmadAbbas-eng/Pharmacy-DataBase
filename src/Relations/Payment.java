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
 * Payment class here where all Payments' operations are occurred
 * 
 * @version 12 January 2022
 * @author Loor Sawalhi
 *
 */
public class Payment {

	private static ArrayList<Payment> data = new ArrayList<Payment>();
	private static ObservableList<Payment> dataList;
	private static int maxID = 0;
	private int ID;
	private LocalDate date;
	private double amount;
	private String method;

	/**
	 * Payment constructor
	 * 
	 * @param iD
	 * @param date
	 * @param amount
	 * @param method
	 */
	public Payment(int iD, LocalDate date, double amount, String method) {
		super();
		ID = iD;
		this.date = date;
		this.amount = amount;
		this.method = method;
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public static ArrayList<Payment> getData() {
		return Payment.data;
	}

	public static int getMaxID() {
		return maxID;
	}

	public static void setMaxID(int maxID) {
		Payment.maxID = maxID;
	}

	public static void getPaymentData() throws ClassNotFoundException, SQLException, ParseException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Payment;", null);

		for (int i = 0; i < table.size(); i++) {

			LocalDate paymentDate = LocalDate.parse(table.get(i).get(1));

			Payment temp = new Payment(Integer.parseInt(table.get(i).get(0)), paymentDate,
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3));
			data.add(temp);
		}
		maxID = Integer.parseInt(Queries.queryResult("select max(payment_ID) from payment;", null).get(0).get(0));
		dataList = FXCollections.observableArrayList(data);
	}

	public static ArrayList<Payment> getPaymentData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<Payment> tempData = new ArrayList<Payment>();

		Connection getPhoneConnection = Queries.dataBaseConnection();

		for (int i = 0; i < table.size(); i++) {
			LocalDate paymentDate = LocalDate.parse(table.get(i).get(1));

			Payment temp = new Payment(Integer.parseInt(table.get(i).get(0)), paymentDate,
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3));

			tempData.add(temp);
		}

		getPhoneConnection.close();
		return tempData;
	}

	public static ObservableList<Payment> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<Payment> dataList) {
		Payment.dataList = dataList;
	}

	public static void insertPayment(LocalDate date, double amount, String method) {
		try {
			Queries.queryUpdate("Insert into Payment values (?, ?, ?, ?);",
					new ArrayList<>(Arrays.asList((++maxID) + "", date.toString(), amount + "", method)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report All payments movement informations on csv file
	 * 
	 * @param path The path of file
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 * @throws IOException
	 */
	public static void report(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey("select * from payment;", path);
	}
}
