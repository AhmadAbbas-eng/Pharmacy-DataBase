package Relations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Payment class here where all Payments' operations are occurred
 * 
 * @version 26 January 2022
 * @author Loor Sawalhi
 *
 */

@Data
public class Payment {
	
	@Getter
	@Setter
	private static ArrayList<Payment> data = new ArrayList<Payment>();
	private int ID;
	private LocalDate date;
	private double amount;
	private String method;

	/**
	 * Allocates a {@code Payment} object and initializes it to represent the
	 * specified parameters.
	 * 
	 * @param iD     The ID of the payment.
	 * @param date   The date of payment.
	 * @param amount The value paid on the payment.
	 * @param method The method of payment ( Cash or Cheque).
	 */
	public Payment(int iD, LocalDate date, double amount, String method) {
		super();
		ID = iD;
		this.date = date;
		this.amount = amount;
		this.method = method;
	}

	public static int getMaxID() {
		return Integer.parseInt(Queries.queryResult("select max(payment_ID) from payment;", null).get(0).get(0));
	}

	/**
	 * Read from data base and fill the ArrayList
	 * 
	 */
	public static void getPaymentData() {

		data.clear();
		ArrayList<ArrayList<String>> table = Queries.queryResult("select * from Payment;", null);

		for (int i = 0; i < table.size(); i++) {

			LocalDate paymentDate = LocalDate.parse(table.get(i).get(1));

			Payment temp = new Payment(Integer.parseInt(table.get(i).get(0)), paymentDate,
					Double.parseDouble(table.get(i).get(2)), table.get(i).get(3));
			data.add(temp);
		}
	}

	public static void insertPayment(LocalDate date, double amount, String method) {
		Queries.queryUpdate("Insert into Payment values (?, ?, ?, ?);",
				new ArrayList<>(Arrays.asList((getMaxID()+1) + "", date.toString(), amount + "", method)));
	}

	/**
	 * Report All payments movement informations on csv file
	 * 
	 * @param path The path of file
	 */
	public static void report(String path) {
		Queries.reportQuery("select * from payment;", path);
	}
}
