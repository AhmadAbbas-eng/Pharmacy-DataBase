package Relations;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Product class here where all Products' operations are occurred
 * 
 * @version 26 January 2022
 * @author Aseel Sabri
 *
 */
public class Product {

	private static ArrayList<Product> data = new ArrayList<Product>();
	private static ObservableList<Product> dataList;
	private int ID;
	private String Name;
	private double Price;
	private String manufacturer;

	/**
	 * Allocates a {@code Product} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param iD           The product ID.
	 * @param name         The commercial name of the product.
	 * @param price        The price of the Product.
	 * @param manufacturer The manufacturer of the product.
	 */
	public Product(int iD, String name, double price, String manufacturer) {
		super();
		ID = iD;
		Name = name;
		Price = price;
		this.setManufacturer(manufacturer);
	}

	public static ArrayList<Product> getData() {
		return Product.data;
	}

	public static ObservableList<Product> getDataList() {
		return dataList;
	}

	public static void setDataList(ObservableList<Product> dataList) {
		Product.dataList = dataList;
	}

	public static int getMaxID() {
		return Integer.parseInt(Queries.queryResult("select max(product_ID) from product;", null).get(0).get(0));
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public double getPrice() {
		return Price;
	}

	public void setPrice(double price) {
		Price = price;
	}

	public static void setData(ArrayList<Product> data) {
		Product.data = data;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * Read from data base and fill the ArrayList
	 */
	public static void getProductData() {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries
				.queryResult("select P.Product_ID, P.Product_Name, P.Product_Price, m.Product_Manufactrer "
						+ "from product p, Name_manu m where P.product_name=m.product_name;", null);

		for (int i = 0; i < table.size(); i++) {

			Product temp = new Product(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3));

			data.add(temp);
		}
		dataList = FXCollections.observableArrayList(data);

	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Product> of data
	 */
	public static ArrayList<Product> getProductData(ArrayList<ArrayList<String>> table) {
		ArrayList<Product> tempData = new ArrayList<Product>();

		for (int i = 0; i < table.size(); i++) {
			Product temp = new Product(Integer.parseInt(table.get(i).get(0)), table.get(i).get(1),
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3));

			tempData.add(temp);
		}
		return tempData;
	}

	public static void insertProduct(String name, double price) {
		Queries.queryUpdate("Insert into Product values (?, ?, ?);",
				new ArrayList<>(Arrays.asList((getMaxID()+1) + "", name, price + "")));
	}

	/**
	 * Report prodcuts informations on csv file
	 * 
	 * @param path The path of file
	 */
	public static void report(String path) {
		Queries.reportQuerey("select P.Product_ID as 'Product ID', P.Product_Name as 'Product Name',P.Product_Price as 'Product Price',m.Product_Manufactrer as 'Product Manufactrer'\r\n"
				+ "from product p,Name_manu m\r\n" + "where P.product_name=m.product_name;", path);
	}

}
