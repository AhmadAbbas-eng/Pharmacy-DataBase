package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Tax class where all Tax' operations are occurred
 * 
 * @version 26 January 2022
 * @author Loor Sawalhi
 */
public class Tax {

	private static ArrayList<Tax> data = new ArrayList<Tax>();
	private static ObservableList<Tax> dataList;
	private String ID;
	private LocalDate date;
	private double value;

	/**
	 * Allocates a {@code Tax} object and initializes it to represent the specified
	 * parameters.
	 * 
	 * @param iD    The ID of the Tax.
	 * @param date  The date of the Tax.
	 * @param value The value of the Tax.
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

	public static ObservableList<Tax> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<Tax> dataList) {
		Tax.dataList = dataList;
	}

	public static void insertTax(String ID, LocalDate date, double value) {
		Queries.queryUpdate("Insert into Tax values (?, ?, ?);",
				new ArrayList<>(Arrays.asList(ID, date.toString(), value + "")));
	}
	
	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */
	public static void getTaxData(){

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

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Tax> of data
	 */
	public static ArrayList<Tax> getTaxData(ArrayList<ArrayList<String>> table){
		ArrayList<Tax> tempData = new ArrayList<Tax>();

		for (int i = 0; i < table.size(); i++) {
			LocalDate taxDate = LocalDate.parse(table.get(i).get(1));
			Tax temp = new Tax(table.get(i).get(0), taxDate, Double.parseDouble(table.get(i).get(2)));
			tempData.add(temp);
		}

		return tempData;
	}


	/**
	 * Report Taxes informations on csv file
	 * 
	 * @param path The path of file
	 */
	public void report(String path) {
		Queries.reportQuerey(
				"select t.tax_id,t.tax_Date,e.employee_name,p.payment_amount\r\n"
						+ "from employee e , tax t, payment p, taxes_payment tp\r\n"
						+ "where  tp.manager_ID=e.employee_ID and tp.payment_ID=p.payment_ID and t.tax_id=tp.tax_id;",
				path);
	}
}
