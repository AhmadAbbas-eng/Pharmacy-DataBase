package Relations;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;

import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.PreparedStatement;

import javafx.scene.control.Alert;

/**
 * 
 * Queries class controls all SQL connections and queries executions
 * 
 * @version 12 January 2022
 * @author Ahmad Abbas
 */
public class Queries {

	// All information needed to connect the date base with java
	private static String userName = "root";
	private static String password = "root123";
	private static String host = "127.0.0.1";
	private static String port = "3306";
	private static String dataBaseName = "Pharmacy";
	private static String ConnectionURL = "jdbc:mysql://" + host + ":" + port + "/" + dataBaseName
			+ "?verifyServerCertificate=false";

	/**
	 * dataBaseConnection: a method that creates the connection to the data base
	 * 
	 * @return The connection object
	 */
	static Connection dataBaseConnection() {

		// The link of connection contains all required data in order to connect
		ConnectionURL = "jdbc:mysql://" + host + ":" + port + "/" + dataBaseName + "?verifyServerCertificate=false";
		// Define the values for data base connection
		Properties setting = new Properties();
		setting.setProperty("user", userName);
		setting.setProperty("password", password);
		setting.setProperty("useSSL", "false");
		setting.setProperty("autoReconnect", "true");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return (Connection) DriverManager.getConnection(ConnectionURL, setting);
		} catch (ClassNotFoundException | SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Connection Error");
			alert.setHeaderText("Can't connect with database");
			alert.setContentText(null);
			alert.showAndWait();
			return null;
		}
	}

	/**
	 * queryResult:a method to get the execution result of query from SQL
	 * 
	 * @param SQL the query we want to execute
	 * @return the result of query as ArrayList<ArrayList<String>>
	 */
	public static ArrayList<ArrayList<String>> queryResult(String statment, ArrayList<String> parameters) {
		try {
			// Declare the ArrayList to add result on it
			ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
			Connection tempConnection = dataBaseConnection(); // Creates connection to data base
			// System.out.println(SQL);
			PreparedStatement preparedStatement;
			preparedStatement = tempConnection.prepareStatement(statment);
			// pass parameters to the query
			if (parameters != null) {
				for (int i = 0; i < parameters.size(); i++) {
					preparedStatement.setString(i + 1, parameters.get(i));
				}
			}
			System.out.println(preparedStatement);
			ResultSet executionResult = preparedStatement.executeQuery(); // Get the results on execution on result set
			ResultSetMetaData rsmd = executionResult.getMetaData(); // Create meta data of the result to get its size
			int size = rsmd.getColumnCount(); // The number of columns of the result

			// While loop to save all results
			while (executionResult.next()) {
				// temporary ArrayList to save one row data
				ArrayList<String> temp = new ArrayList<String>();

				// for loop to walk through all cells
				for (int i = 0; i < size; i++) {
					temp.add(executionResult.getString(i + 1));
				}
				table.add(temp); // Add the row into result ArrayList
			}
			// Close all connection resources
			tempConnection.close();
			preparedStatement.close();
			executionResult.close();
			return table;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	public static void queryUpdate(String statment, ArrayList<String> parameters) {

		// Declare the ArrayList to add result on it
		Connection tempConnection;
		try {
			tempConnection = dataBaseConnection();
			// Creates connection to data base
			// System.out.println(SQL);
			// Creates the statement that which executes the query
			PreparedStatement preparedStatement = tempConnection.prepareStatement(statment);

			// pass parameters to the query
			if (parameters != null) {
				for (int i = 0; i < parameters.size(); i++) {
					preparedStatement.setString(i + 1, parameters.get(i));
				}
			}

			System.out.println(preparedStatement);
			preparedStatement.executeUpdate(); // Get the results on execution on result set
			tempConnection.close();
		} catch (SQLException e) {
			String arr[] = statment.split(" ");

			StringBuilder warning = new StringBuilder();
			StringBuilder massege = new StringBuilder();
			if (arr[0].toLowerCase().equals("insert")) {
				warning.append("Adding Error");
				massege.append("Can't add enrty");
			} else if (arr[0].toLowerCase().equals("update")) {
				warning.append("Updating Error");
				massege.append("Can't update ");
			}
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(warning.toString());
			alert.setHeaderText(massege.toString());
			alert.setContentText("Reasons:\n1-Duplicate Data entry\n2-Data too long\n3-Wrong input fromat");
			alert.showAndWait();
		}
	}

	/**
	 * reportQuerey: report data into file
	 * 
	 * @param query    the query we want to report
	 * @param filePath path of the file we want to save into
	 */
	public static void reportQuery(String query, String filePath) {

		try {
			Connection tempConnection = dataBaseConnection(); // Create connection to the data base
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"); // format date as current date
			LocalDateTime now = LocalDateTime.now();
			FileWriter fileWriter;
			fileWriter = new FileWriter(filePath + dtf.format(now) + ".csv");
			// Write on the path the user asked
			// for
			System.out.println(query);
			Statement creation = tempConnection.createStatement(); // Creates the statement that which executes the
																	// query
			ResultSet executioResult = creation.executeQuery(query); // Get the results on execution on result set
			ResultSetMetaData rsmd = executioResult.getMetaData(); // Create meta data of the result to get its size
			int size = rsmd.getColumnCount(); // The number of columns of the result

			// Write the names of attributes on file
			for (int i = 1; i <= size; i++) {
				fileWriter.write(rsmd.getColumnLabel(i));
				if (i != size)
					fileWriter.write(",");
				else
					fileWriter.write("\n");
			}

			// Write data on file
			while (executioResult.next()) {
				for (int i = 1; i <= size; i++) {
					fileWriter.write(executioResult.getString(i));
					if (i != size)
						fileWriter.write(",");
					else
						fileWriter.write("\n");
				}

			}
			// Close connection
			fileWriter.close();
			tempConnection.close();
			creation.close();
			executioResult.close();
		} catch (IOException | SQLException e) {
			System.out.println(e.getMessage());
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Loding Error");
			alert.setHeaderText("Can't load data");
			alert.setContentText(null);
			alert.showAndWait();
		}
	}

	public static double getNetProfit(int month, int year) {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("%" + year + "%");
		parameters.add("%" + month + "%");
		parameters.add("%" + year + "%");
		parameters.add("%" + month + "%");
		if (queryResult("select sum(I.income_amount)\r\n" + "-(select sum(p.payment_amount)\r\n"
				+ "from payment p where year(p.payment_Date) like ? and month(p.payment_Date) like ?)\r\n"
				+ "from income I where year(i.income_Date) like ? and month(i.income_Date) like ?;", parameters).get(0)
						.get(0) == null) {
			return 0.0;
		}
		return Double.parseDouble(queryResult("select sum(I.income_amount)\r\n" + "-(select sum(p.payment_amount)\r\n"
				+ "from payment p where year(p.payment_Date) like ? and month(p.payment_Date) like ?)\r\n"
				+ "from income I where year(i.income_Date) like ? and month(i.income_Date) like ?;", parameters).get(0)
						.get(0));
	}



	/**
	 * expiryDate: to get drugs with close expire date
	 * 
	 * @return ArrayList<ArrayList<String>> contains all data of the drugs
	 */
	public static ArrayList<ArrayList<String>> expiryDate() {
		return queryResult(
				"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
						+ "from batch b,product p\r\n"
						+ "where b.batch_amount>0 and b.product_ID=p.product_ID and b.batch_production_date <>'1111-01-01' and b.batch_expiry_date <DATE_ADD(DATE(NOW()), INTERVAL 30 DAY)\r\n"
						+ "order by b.batch_expiry_date;",
				null);
	}

	/**
	 * amountToFinish: get the data of batches that are about to finish
	 * 
	 * @return ArrayList<ArrayList<String>> contains all data of the batches
	 */
	public static ArrayList<ArrayList<String>> amountToFinish(){
		return queryResult("SELECT P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
				+ "from batch b,product p,drug d\r\n"
				+ "where b.batch_amount>0 and b.product_ID=p.product_ID and d.product_ID=p.product_Id and b.batch_production_date <>'1111-01-01' \r\n"
				+ "and ((d.drug_pharmacetical_category like 'non' and b.batch_amount<10 and b.batch_amount>0) \r\n"
				+ "or ((d.drug_pharmacetical_category like '%d%' and b.batch_amount<5 and b.batch_amount>0)))\r\n"
				+ "union(SELECT P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
				+ "from batch b,product p\r\n"
				+ "where b.product_ID=p.product_ID and b.batch_amount<10 and b.batch_amount>0 and b.batch_production_date <>'1111-01-01')\r\n"
				+ "order by batch_expiry_date;", null);
	}

}
