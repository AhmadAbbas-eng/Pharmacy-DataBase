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
	private static String password = "k2m36htg";
	private static String host = "127.0.0.1";
	private static String port = "3306";
	private static String dataBaseName = "pharmacy";
	private static String ConnectionURL = "jdbc:mysql://" + host + ":" + port + "/" + dataBaseName
			+ "?verifyServerCertificate=false";

	/**
	 * dataBaseConnection: a method that creates the connection to the data base
	 * 
	 * @return The connection object
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 */
	static Connection dataBaseConnection() throws ClassNotFoundException, SQLException {

		// The link of connection contains all required data in order to connect
		ConnectionURL = "jdbc:mysql://" + host + ":" + port + "/" + dataBaseName + "?verifyServerCertificate=false";

		// Define the values for data base connection
		Properties setting = new Properties();
		setting.setProperty("user", userName);
		setting.setProperty("password", password);
		setting.setProperty("useSSL", "false");
		setting.setProperty("autoReconnect", "true");
		Class.forName("com.mysql.jdbc.Driver");
		return (Connection) DriverManager.getConnection(ConnectionURL, setting);
	}

	/**
	 * queryResult:a method to get the execution result of query from SQL
	 * 
	 * @param SQL the query we want to execute
	 * @return the result of query as ArrayList<ArrayList<String>>
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 */
	public static ArrayList<ArrayList<String>> queryResult(String statment, ArrayList<String> parameters)
			throws ClassNotFoundException, SQLException {

		// Declare the ArrayList to add result on it
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		Connection tempConnection = dataBaseConnection(); // Creates connection to data base
		// System.out.println(SQL);
		PreparedStatement preparedStatement = tempConnection.prepareStatement(statment); // Creates the statement that
																							// which executes the query

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
	}

	
	public static void queryUpdate(String statment, ArrayList<String> parameters)
			throws ClassNotFoundException, SQLException {

		// Declare the ArrayList to add result on it
		Connection tempConnection = dataBaseConnection(); // Creates connection to data base
		// System.out.println(SQL);
		PreparedStatement preparedStatement = tempConnection.prepareStatement(statment); // Creates the statement that
																							// which executes the query

		// pass parameters to the query
		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				preparedStatement.setString(i + 1, parameters.get(i));
			}
		}

		System.out.println(preparedStatement);

		 preparedStatement.executeUpdate(); // Get the results on execution on result set
	}

	
	/**
	 * reportQuerey: report data into file
	 * 
	 * @param query    the query we want to report
	 * @param filePath path of the file we want to save into
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws IOException            If there are any errors on inputs or outputs
	 *                                of the file
	 */
	public static void reportQuerey(String query, String filePath)
			throws ClassNotFoundException, SQLException, IOException {

		Connection tempConnection = dataBaseConnection(); // Create connection to the data base
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"); // format date as current date
		LocalDateTime now = LocalDateTime.now();

		FileWriter fileWriter = new FileWriter(filePath + dtf.format(now) + ".csv"); // Write on the path the user asked
																						// for
		System.out.println(query);
		Statement creation = tempConnection.createStatement(); // Creates the statement that which executes the query
		ResultSet executioResult = creation.executeQuery(query); // Get the results on execution on result set
		ResultSetMetaData rsmd = executioResult.getMetaData(); // Create meta data of the result to get its size
		int size = rsmd.getColumnCount(); // The number of columns of the result

		// Write the names of attributes on file
		for (int i = 1; i <= size; i++) {
			fileWriter.write(rsmd.getColumnName(i));
			if (i != size)
				fileWriter.write(",");
			else
				fileWriter.write("\n");
		}

		// Write data om file
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
	}

	/**
	 * getNetProfit get total profit
	 * 
	 * @return the profit of the pharmacy
	 * @throws NumberFormatException  If there are any formatting exception
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 */
	public static int getNetProfit() throws NumberFormatException, ClassNotFoundException, SQLException {
		return Integer
				.parseInt(
						queryResult(
								"\r\n" + "select sum(c.order_price)\r\n"
										+ "-(select sum(s.supplier_dues) +(select sum(c.payment_amount)\r\n"
										+ "from payment c)\r\n" + "from supplier s, payment p)\r\n" + "from c_order c;",
								null).get(0).get(0));
	}

	/**
	 * getPaymentAmount: calculate the money paid on certain month
	 * 
	 * @param month the month of required date
	 * @param year  the year of required date
	 * @return the total paid amount in this month
	 * @throws NumberFormatException  If there are any formatting exception
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 */
	public static int getPaymentAmount(int month, int year)
			throws NumberFormatException, ClassNotFoundException, SQLException {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("%" + month + "%");
		parameters.add("%" + year + "%");

		return Integer.parseInt(queryResult("select sum(p.payment_amount) from payment p "
				+ "where month(Payment_Date) like ? and year(Payment_Date) like ?;", parameters).get(0).get(0));
	}

	/**
	 * expiryDate: to get drugs with close expire date
	 * 
	 * @return ArrayList<ArrayList<String>> contains all data of the drugs
	 * @throws NumberFormatException
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 */
	public static ArrayList<ArrayList<String>> expiryDate() throws ClassNotFoundException, SQLException {
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
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 */
	public static ArrayList<ArrayList<String>> amountToFinish() throws ClassNotFoundException, SQLException {
		return queryResult(
				"SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
						+ "from batch b,product p,drug d\r\n"
						+ "where b.batch_amount>0 and b.product_ID=p.product_ID and d.product_ID=p.product_Id and b.batch_production_date <>'1111-01-01' \r\n"
						+ "and ((d.drug_pharmacetical_category like 'non' and b.batch_amount<10 and b.batch_amount>0) \r\n"
						+ "or ((d.drug_pharmacetical_category like '%d%' and b.batch_amount<5 and b.batch_amount>0)))\r\n"
						+ "union(SELECT P.product_ID,P.product_name,b.batch_production_date,b.batch_expiry_date,b.batch_amount\r\n"
						+ "from batch b,product p\r\n" + "where b.product_ID=p.product_ID and b.batch_amount<10 and b.batch_amount>0 and b.batch_production_date <>'1111-01-01')\r\n"
						+ "order by batch_expiry_date;",
				null);
	}

	/**
	 * closePayments: get all near due date for payments
	 * 
	 * @return ArrayList<ArrayList<String>> contains all data of the payments
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 */
	public static ArrayList<ArrayList<String>> closePayments() throws ClassNotFoundException, SQLException {
		return queryResult("select o.order_id,s.supplier_name,o.due_date_for_payment,o.order_cost\r\n"
				+ "from s_order o,supplier s\r\n"
				+ "where o.supplier_Id=s.supplier_ID and o.due_date_for_payment >DATE_ADD(DATE(NOW()), INTERVAL 30 DAY);",
				null);
	}

}
