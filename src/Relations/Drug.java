package Relations;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Drug class inherits Product Class and here where all Drugs' operations are
 * occurred
 * 
 * @version 26 January 2022
 * @author Aseel Sabri
 *
 */
public class Drug extends Product {
	private static ArrayList<Drug> data = new ArrayList<Drug>();
	private static ObservableList<Drug> dataList;
	private String scientificName;
	private String riskPregnancy;
	private String dosage;
	private String category;
	private String dosageForm;
	private String pharmaceuticalCategory;

	/**
	 * Allocates a {@code Drug} object and initializes it to represent the specified
	 * parameters.
	 * 
	 * @param iD                     The product ID of the drug.
	 * @param name                   The commercial name of the drug.
	 * @param price                  The price of the drug.
	 * @param manufacturer           The manufacturer of the drug.
	 * @param scientificName         The scientific name of the drug.
	 * @param riskPregnancy          The risk pregnancy of the drug.
	 * @param dosage                 The dosage in mg for the drug.
	 * @param category               The category of the drug (
	 *                               Antibiotics,Antacids,...).
	 * @param dosageForm             The dosage category of the drug (
	 *                               Tablet,Cream,Spray,...).
	 * @param pharmaceuticalCategory The dosage pharmaceutical category of the drug
	 *                               ( Tablet,Cream,Spray,...).
	 */
	public Drug(int iD, String name, double price, String manufacturer, String scientificName, String riskPregnency,
			String dosage, String category, String dosageForm, String pharmaceticalCategory) {
		super(iD, name, price, manufacturer);
		this.scientificName = scientificName;
		this.riskPregnancy = riskPregnency;
		this.dosage = dosage;
		this.category = category;
		this.dosageForm = dosageForm;
		this.pharmaceuticalCategory = pharmaceticalCategory;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getRiskPregnency() {
		return getRiskPregnancy();
	}

	public String getRiskPregnancy() {
		return riskPregnancy;
	}

	public void setRiskPregnency(String riskPregnency) {
		setRiskPregnancy(riskPregnency);
	}

	public void setRiskPregnancy(String riskPregnency) {
		this.riskPregnancy = riskPregnency;
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
		return getPharmaceuticalCategory();
	}

	public String getPharmaceuticalCategory() {
		return pharmaceuticalCategory;
	}

	public void setPharmaceticalCategory(String pharmaceticalCategory) {
		setPharmaceuticalCategory(pharmaceticalCategory);
	}

	public void setPharmaceuticalCategory(String pharmaceticalCategory) {
		this.pharmaceuticalCategory = pharmaceticalCategory;
	}

	public static ArrayList<Drug> getDataDrug() {
		return Drug.data;
	}

	public static ObservableList<Drug> getDataListDrug() {
		return dataList;
	}

	public static void setDataListDrug(ObservableList<Drug> dataList) {
		Drug.dataList = dataList;
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */
	public static void getDrugData() {

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

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Drug> of data
	 */
	public static ArrayList<Drug> getDrugData(ArrayList<ArrayList<String>> table) {

		ArrayList<Drug> tempData = new ArrayList<Drug>();
		for (int i = 0; i < table.size(); i++) {
			Drug temp = new Drug(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3), table.get(i).get(4),
					table.get(i).get(5), table.get(i).get(6), table.get(i).get(7), table.get(i).get(8),
					table.get(i).get(9));
			tempData.add(temp);
		}

		return tempData;
	}

	public static void insertDrug(String name, double price, String scientificName, String riskPregnency, String dosage,
			String category, String dosageForm, String pharmaceticalCategory) {
		Queries.queryUpdate("Insert into Product values(?, ?, ?);",
				new ArrayList<>(Arrays.asList((getMaxID()+1) + "", name, price + "")));

		Queries.queryUpdate("Insert into Drug values (?, ?, ?, ?, ?, ?,?);", new ArrayList<>(Arrays
				.asList(getMaxID() + "", scientificName, riskPregnency,dosage, category, dosageForm, pharmaceticalCategory)));
	}
}
