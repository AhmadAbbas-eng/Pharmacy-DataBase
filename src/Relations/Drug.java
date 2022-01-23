package Relations;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Drug class inherits Product Class and here where all Drugs' operations are
 * occurred
 * 
 * @version 12 January 2022
 * @author Aseel Sabri
 *
 */
public class Drug extends Product {
	private static ArrayList<Drug> data = new ArrayList<Drug>();
	private static ObservableList<Drug> dataList;
	private String scientificName;
	private String riskPregnency;
	private String dosage;
	private String category;
	private String dosageForm;
	private String pharmaceticalCategory;

	/**
	 * Drug constructor
	 * 
	 * @param iD
	 * @param name
	 * @param price
	 * @param manufacturer
	 * @param scientificName
	 * @param riskPregnency
	 * @param dosage
	 * @param category
	 * @param dosageForm
	 * @param pharmaceticalCategory
	 */
	public Drug(int iD, String name, double price, String manufacturer, String scientificName, String riskPregnency,
			String dosage, String category, String dosageForm, String pharmaceticalCategory) {
		super(iD, name, price, manufacturer);
		this.scientificName = scientificName;
		this.riskPregnency = riskPregnency;
		this.dosage = dosage;
		this.category = category;
		this.dosageForm = dosageForm;
		this.pharmaceticalCategory = pharmaceticalCategory;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getRiskPregnency() {
		return riskPregnency;
	}

	public void setRiskPregnency(String riskPregnency) {
		this.riskPregnency = riskPregnency;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDosageForm() {
		return dosageForm;
	}

	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}

	public String getPharmaceticalCategory() {
		return pharmaceticalCategory;
	}

	public void setPharmaceticalCategory(String pharmaceticalCategory) {
		this.pharmaceticalCategory = pharmaceticalCategory;
	}

	public static ArrayList<Drug> getDataDrug() {
		return Drug.data;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 * @throws ClassNotFoundException If com.mysql.jdbc.Driver was not found
	 * @throws SQLException           If any connection exceptions occurred
	 * @throws ParseException         If any exception data type parsing occurred
	 */
	public static void getDrugData() throws ClassNotFoundException, SQLException, ParseException {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult(
				"select P.Product_ID, P.Product_Name, P.Product_Price, m.Product_Manufactrer, D.Drug_Scientific_Name,"
						+ " D.Drug_Risk_Pregnency_Category, D.Drug_Dosage, D.Drug_Category,"
						+ " D.Drug_Dosage_Form, D.Drug_Pharmacetical_Category "
						+ " from Drug D, Product P, Name_manu m "
						+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name;",
				null);
		for (int i = 0; i < table.size(); i++) {
			Product.getData().add(new Product(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3)));
			Drug temp = new Drug(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3), table.get(i).get(4),
					table.get(i).get(5), table.get(i).get(6), table.get(i).get(7), table.get(i).get(8),
					table.get(i).get(9));

			data.add(temp);
		}
		dataList = FXCollections.observableArrayList(data);

	}

	public static ArrayList<Drug> getDrugData(ArrayList<ArrayList<String>> table)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<Drug> tempData = new ArrayList<Drug>();
		Connection getPhoneConnection = Queries.dataBaseConnection();

		for (int i = 0; i < table.size(); i++) {
			Drug temp = new Drug(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3), table.get(i).get(4),
					table.get(i).get(5), table.get(i).get(6), table.get(i).get(7), table.get(i).get(8),
					table.get(i).get(9));
			tempData.add(temp);
		}

		getPhoneConnection.close();
		return tempData;
	}

	public static ObservableList<Drug> getDataListDrug() {
		return dataList;
	}

	public static void setDataListDrug(ObservableList<Drug> dataList) {
		Drug.dataList = dataList;
	}

	public static void insertDrug(String name, double price, String scientificName, String riskPregnency, String dosage,
			String category, String dosageForm, String pharmaceticalCategory) {
		try {
			setMaxID(getMaxID() + 1);
			Queries.queryUpdate("Insert into Product values(?, ?, ?);",
					new ArrayList<>(Arrays.asList(getMaxID() + "", name, price + "")));

			Queries.queryUpdate("Insert into Drug values (?, ?, ?, ?, ?, ?);",
					new ArrayList<>(Arrays.asList(getMaxID() + "", scientificName, riskPregnency, category, dosageForm,
							pharmaceticalCategory)));

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
