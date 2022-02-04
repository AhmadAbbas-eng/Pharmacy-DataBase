package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.Data;

/**
 * Batch class where Batch table data have been read and manipulated
 * 
 * @version 26 January 2022
 * @author Aseel Sabri
 */

@Data
public class Batch {
	private int ID;
	private LocalDate productionDate;
	private LocalDate expiryDate;
	private int amount;
	
	/**
	 * Allocates a {@code Batch} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param iD             The ID of the batch.
	 * @param productionDate The date of production of the batch.
	 * @param expiryDate     The expire date of the batch.
	 * @param amount         The amount of the batch.
	 */
	public Batch(int iD, LocalDate productionDate, LocalDate expiryDate, int amount) {
		super();
		ID = iD;
		this.productionDate = productionDate;
		this.expiryDate = expiryDate;
		this.amount = amount;
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<Batch> of data
	 */
	public static ArrayList<Batch> getBatchData(ArrayList<ArrayList<String>> table) {
		ArrayList<Batch> tempData = new ArrayList<Batch>();

		for (int i = 0; i < table.size(); i++) {
			LocalDate proDate = LocalDate.parse(table.get(i).get(1));
			LocalDate expDate = LocalDate.parse(table.get(i).get(2));
			Batch temp = new Batch(Integer.parseInt(table.get(i).get(0)), proDate, expDate,
					Integer.parseInt(table.get(i).get(3)));
			tempData.add(temp);

		}
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
		Queries.queryUpdate("Insert into Batch values (? ,? ,? ,?);",
				new ArrayList<>(Arrays.asList(ID + "", "" + productionDate, "" + expiryDate, "" + amount)));
	}

	/**
	 * Report Batches informations on csv file
	 * 
	 * @param path The path of file
	 */
	public static void report(String path) {
		Queries.reportQuery(
				"select p.product_name as 'Product Name',b.batch_production_date as 'Production Date',b.batch_expiry_date as 'Expire Date',b.batch_amount as 'amount'\r\n"
						+ "from product p,batch b\r\n"
						+ "where p.product_ID=b.product_Id and b.batch_production_date <> '1111-01-01';",
				path);
	}
}
