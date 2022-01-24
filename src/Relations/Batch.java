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
 * Batch class where Batch table data have been read and manipulated
 * 
 * @version 12 January 2022
 * @author Aseel Sabri
 */
public class Batch {
	private static ArrayList<Batch> data = new ArrayList<Batch>(); // ArrayList to Store data
	private static ObservableList<Batch> dataList; // Observable ArrayList to show data
	private int ID;
	private LocalDate productionDate;
	private LocalDate expiryDate;
	private int amount;

	/**
	 * Class constructor
	 * 
	 * @param iD
	 * @param productionDate
	 * @param expiryDate
	 * @param amount
	 */
	public Batch(int iD, LocalDate productionDate, LocalDate expiryDate, int amount) {
		super();
		ID = iD;
		this.productionDate = productionDate;
		this.expiryDate = expiryDate;
		this.amount = amount;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public LocalDate getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(LocalDate productionDate) {
		this.productionDate = productionDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public static ArrayList<Batch> getData() {
		return Batch.data;
	}

	public static ObservableList<Batch> getDataList() {
		return Batch.dataList;
	}

	public static void setDataList(ObservableList<Batch> dataList) {
		Batch.dataList = dataList;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 */
	public static void getBatchData() throws ClassNotFoundException, SQLException, ParseException {

		// Clear data to read it again
		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Batch;", null);

		// Get each row of data and fill ArrayList data
		for (int i = 0; i < table.size(); i++) {

			LocalDate proDate = LocalDate.parse(table.get(i).get(1));
			LocalDate expDate = LocalDate.parse(table.get(i).get(2));

			// Create temporary object to add on data
			Batch temp = new Batch(Integer.parseInt(table.get(i).get(0)), proDate, expDate,
					Integer.parseInt(table.get(i).get(3)));
			data.add(temp);
		}

		// Fill the observable arrayList
		dataList = FXCollections.observableArrayList(data);
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Batch> of data
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 */
	public static ArrayList<Batch> getBatchData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Batch> tempData = new ArrayList<Batch>();
		Connection getPhoneConnection = Queries.dataBaseConnection();

		for (int i = 0; i < table.size(); i++) {
			LocalDate proDate = LocalDate.parse(table.get(i).get(1));
			LocalDate expDate = LocalDate.parse(table.get(i).get(2));
			Batch temp = new Batch(Integer.parseInt(table.get(i).get(0)), proDate, expDate,
					Integer.parseInt(table.get(i).get(3)));
			tempData.add(temp);
		}

		getPhoneConnection.close();
		return tempData;
	}

	/**
	 * Insert on data base
	 * 
	 * @param iD
	 * @param productionDate
	 * @param expiryDate
	 * @param amount
	 */
	public static void insertBatch(int ID, LocalDate productionDate, LocalDate expiryDate, int amount) {
		try {
			Queries.queryUpdate("Insert into Batch values (? ,? ,? ,?);",
					new ArrayList<>(Arrays.asList(ID+"", "" + productionDate, "" + expiryDate, "" + amount)));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report Batches informations on csv file
	 * 
	 * @param path The path of file
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 * @throws IOException
	 */
	public static void report(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey("select p.product_name,b.batch_production_date,b.batch_expiry_date\r\n"
				+ "from product p,batch b\r\n" + "where p.product_ID=b.product_Id and b.batch_production_date <> '1111-01-01';", path);
	}
}
