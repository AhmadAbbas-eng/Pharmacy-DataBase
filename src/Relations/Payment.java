package Relations;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * Payment class here where all Payments' operations are occurred
 * 
 * @version 26 January 2022
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
	 * Allocates a {@code Payment} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param iD     The ID of the payment.
	 * @param date   The date of payment.
	 * @param amount The value paid on the payment.
	 * @param method The method of payment ( Cash or Cheque).
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

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */
	public static void getPaymentData() {

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

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Payment> of data
	 */
	public static ArrayList<Payment> getPaymentData(ArrayList<ArrayList<String>> table) {

		try {
			ArrayList<Payment> tempData = new ArrayList<Payment>();

			Connection getPaymentConnection = Queries.dataBaseConnection();

			for (int i = 0; i < table.size(); i++) {
				LocalDate paymentDate = LocalDate.parse(table.get(i).get(1));

				Payment temp = new Payment(Integer.parseInt(table.get(i).get(0)), paymentDate,
						Double.parseDouble(table.get(i).get(2)), table.get(i).get(3));
				tempData.add(temp);
			}
			getPaymentConnection.close();
			return tempData;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Loding Error");
			alert.setHeaderText("Can't load data from Payment");
			alert.setContentText(null);
			alert.showAndWait();
			return null;
		}

	}

	public static ObservableList<Payment> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<Payment> dataList) {
		Payment.dataList = dataList;
	}

	public static void insertPayment(LocalDate date, double amount, String method) {
		Queries.queryUpdate("Insert into Payment values (?, ?, ?, ?);",
				new ArrayList<>(Arrays.asList((++maxID) + "", date.toString(), amount + "", method)));
	}

	/**
	 * Report All payments movement informations on csv file
	 * 
	 * @param path The path of file
	 */
	public static void report(String path) {
		Queries.reportQuerey("select * from payment;", path);
	}
}
