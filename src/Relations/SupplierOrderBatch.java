package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * SupplierOrderBatch represents the details of each supplier's order
 * 
 * @version 26 January 2022
 * @author Loor Sawalhi
 */

@Data
public class SupplierOrderBatch {
	@Getter
	@Setter
	private static ArrayList<SupplierOrderBatch> data = new ArrayList<SupplierOrderBatch>();
	@Getter
	@Setter
	private int orderID;
	private int productID;
	private LocalDate productionDate;
	private LocalDate expiryDate;
	private int amount;

	/**
	 * Allocates a {@code SupplierOrderBatch} object and initializes it to represent
	 * the specified parameters.
	 * 
	 * @param orderID        The order ID.
	 * @param productID      The product ID.
	 * @param productionDate The batch production Date.
	 * @param expiryDate     The batch expire date.
	 * @param amount         The amount of the product on the order.
	 */
	public SupplierOrderBatch(int orderID, int productID, LocalDate productionDate, LocalDate expiryDate, int amount) {
		super();
		this.orderID = orderID;
		this.productID = productID;
		this.productionDate = productionDate;
		this.expiryDate = expiryDate;
		this.amount = amount;
	}

	/**
	 * Fill the an ArrayList from specific ArrayList<ArrayList<String>> entry
	 * 
	 * @param table ArrayList<ArrayList<String>> to fill data with
	 * @return ArrayList<SupplierOrderBatch> of data
	 */
	public static ArrayList<SupplierOrderBatch> getSupplierOrderBatchData(ArrayList<ArrayList<String>> table){
		ArrayList<SupplierOrderBatch> tempData = new ArrayList<SupplierOrderBatch>();
		for (int i = 0; i < table.size(); i++) {
			LocalDate writingDate = LocalDate.parse(table.get(i).get(2));
			LocalDate cashingDate = LocalDate.parse(table.get(i).get(3));
			SupplierOrderBatch temp = new SupplierOrderBatch(Integer.parseInt(table.get(i).get(0)),
					Integer.parseInt(table.get(i).get(1)), writingDate, cashingDate,
					Integer.parseInt(table.get(i).get(4)));

			tempData.add(temp);
		}
			return tempData;	
	}
	
	public static void insertSupplierOrderBatch(String oID, String pID, LocalDate productionDate, LocalDate expiryDate,
			int amount) {
		Queries.queryUpdate("Insert into S_order_batch values (?, ?, ?, ?, ?);", new ArrayList<>(
				Arrays.asList(oID, pID, productionDate.toString(), expiryDate.toString(), amount + "")));
	}
}
