package Relations;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Tax class where all Tax' operations are occurred
 * 
 * @version 12 January 2022
 * @author Ahmad Abbas
 */
public class Tax {

	private static ArrayList<Tax> data = new ArrayList<Tax>();
	private static ObservableList<Tax> dataList;
	private String ID;
	private LocalDate date;
	private double value;

	/**
	 * Tax Constructor
	 * 
	 * @param iD
	 * @param date
	 * @param value
	 */
	public Tax(String iD, LocalDate date, double value) {
		super();
		ID = iD;
		this.date = date;
		this.value = value;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public static ArrayList<Tax> getData() {
		return Tax.data;
	}

	public static void getTaxData() throws ClassNotFoundException, SQLException, ParseException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Tax;", null);

		for (int i = 0; i < table.size(); i++) {
			LocalDate taxDate = LocalDate.parse(table.get(i).get(1));
			System.out.println(table.get(i).get(0) + " " + taxDate + " " + Double.parseDouble(table.get(i).get(2)));
			Tax temp = new Tax(table.get(i).get(0), taxDate, Double.parseDouble(table.get(i).get(2)));

			data.add(temp);
		}

		dataList = FXCollections.observableArrayList(data);
	}

	public static ArrayList<Tax> getTaxData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<Tax> tempData = new ArrayList<Tax>();

		for (int i = 0; i < table.size(); i++) {
			LocalDate taxDate = LocalDate.parse(table.get(i).get(1));
			Tax temp = new Tax(table.get(i).get(0), taxDate, Double.parseDouble(table.get(i).get(2)));

			tempData.add(temp);
		}

		return tempData;
	}

	public static ObservableList<Tax> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<Tax> dataList) {
		Tax.dataList = dataList;
	}

	public static void insertTax(String ID,LocalDate date, double value) {
		try {
			Queries.queryUpdate("Insert into Tax values (?, ?, ?);",
					new ArrayList<>(Arrays.asList(ID, date.toString(), value + "")));
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Report Taxes informations on csv file
	 * 
	 * @param path The path of file
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 * @throws IOException
	 */
	public void report(String path) throws ClassNotFoundException, SQLException, IOException {
		Queries.reportQuerey(
				"select t.tax_id,t.tax_Date,e.employee_name,p.payment_amount\r\n"
						+ "from employee e , tax t, payment p, taxes_payment tp\r\n"
						+ "where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
				path);
	}
}
