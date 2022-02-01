package application;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import Relations.Cheque;
import Relations.Customer;
import Relations.CustomerOrder;
import Relations.Employee;
import Relations.Payment;
import Relations.Queries;
import Relations.Tax;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
			Scene scene = new Scene(root, 712, 497);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("file:Icon.jpg"));
			primaryStage.getIcons().add(new Image("file:Icon.jpg"));
			
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		generateS_order();
//		c_order_generate();
//		try {
//			payment();
//		} catch (ClassNotFoundException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// Main.readingData();
		System.out.println(Employee.decryptPassword("Omar", "0f11b32743353f6"));
		launch(args);
	}

	public static void readingData() {
		Cheque.getChequeData();
		Customer.getCustomerData();
		CustomerOrder.getCustomerOrderData();

		Employee.getEmployeeData();
		Payment.getPaymentData();

		Tax.getTaxData();
	}

	public static void generateS_order() {
		ArrayList<String> s_order = new ArrayList<String>();
		ArrayList<String> s_order_batch = new ArrayList<String>();
		ArrayList<String> batch = new ArrayList<String>();
		int sOrderId = 1;
		LocalDate orderDate = LocalDate.parse("2021-04-01");
		while (orderDate.compareTo(LocalDate.now()) < 0) {
			StringBuilder s_order_string = new StringBuilder();
			s_order_string.append(sOrderId + ",'" + orderDate.toString() + "',");
			int batchCapacity = (int) Math.floor(Math.random() * (10 - 1 + 1) + 1);
			int total = 0;

			for (int i = 0; i < batchCapacity; i++) {
				int productID = (int) Math.floor(Math.random() * (20 - 1 + 1) + 1);
				ArrayList<String> parameters = new ArrayList<>();
				parameters.add(productID + "");
				int price = Integer.parseInt(
						Queries.queryResult("select product_price from product where product_Id=?;\r\n", parameters)
								.get(0).get(0));

				int quantity = 5 * (int) Math.floor(Math.random() * (3 - 1 + 1) + 1);
				total += (int) Math.floor(price - 0.5 * price) * quantity;
				long minDay = LocalDate.of(2021, 1, 1).toEpochDay();
				long maxDay = LocalDate.of(orderDate.getYear(), orderDate.getMonth(), orderDate.getDayOfMonth())
						.toEpochDay();
				long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
				LocalDate productionDate = LocalDate.ofEpochDay(randomDay);
				LocalDate expire = orderDate.plusMonths(9);
				minDay = LocalDate.of(expire.getYear(), expire.getMonth(), expire.getDayOfMonth()).toEpochDay();
				maxDay = LocalDate.of(2023, 1, 1).toEpochDay();
				randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
				LocalDate expiryDate = LocalDate.ofEpochDay(randomDay);
				s_order_batch
						.add(sOrderId + "," + productID + ",'" + productionDate + "','" + expiryDate + "'," + quantity);
				batch.add(productID + ",'" + productionDate + "','" + expiryDate + "'," + quantity);
			}

			int employeeID = (int) Math.floor(Math.random() * (3 - 1 + 1) + 1);
			int supplierID = (int) Math.floor(Math.random() * (6 - 1 + 1) + 1);

			Queries.queryUpdate("update Supplier set supplier_dues = supplier_dues + ? where Supplier_ID =?;",
					new ArrayList<>(Arrays.asList((total - (int) 0.15 * total) + "", supplierID + "")));

			s_order_string.append(total + "," + (int) (total * 0.15) + ",'" + orderDate.plusMonths(3).toString() + "',"
					+ supplierID + ",1," + employeeID + ",'" + orderDate.plusDays(3).toString() + "'");
			s_order.add(s_order_string.toString());
			sOrderId++;
			orderDate = orderDate.plusDays(15);
		}
		for (String s : batch) {
			Queries.queryUpdate("INSERT INTO `batch` VALUES (" + s + ");\n", null);
		}
		for (String s : s_order) {
			Queries.queryUpdate("INSERT INTO `s_order` VALUES (" + s + ");\n", null);

		}
		for (String s : s_order_batch) {
			Queries.queryUpdate("INSERT INTO `s_order_batch` VALUES (" + s + ");\n", null);

		}
		System.out.println("done");
	}

	public static void c_order_generate() {

		ArrayList<String> c_order = new ArrayList<String>();
		ArrayList<String> c_order_batch = new ArrayList<String>();
		ArrayList<String> income = new ArrayList<String>();
		ArrayList<String> customer2order = new ArrayList<String>();
		int cOrderId = 1;
		int incomeID = 1;
		LocalDate orderDate = LocalDate.parse("2021-06-01");
		while (!orderDate.equals(LocalDate.now().plusDays(1))) {

			int n = (int) Math.floor(Math.random() * (7 - 5 + 1) + 5);
			int employeeID = (int) Math.floor(Math.random() * (3 - 1 + 1) + 1);

			while (n != 0) {
				ArrayList<String> parameters = new ArrayList<>();
				parameters.add(orderDate.toString());
				ArrayList<ArrayList<String>> Drugs = Queries.queryResult(
						"select b.product_ID,b.batch_production_date,b.batch_expiry_date,p.product_price,d.Drug_Pharmacetical_Category from product p,batch b,drug d\r\n"
								+ "where p.product_Id=b.product_Id and  p.product_Id=d.product_Id and b.batch_production_date < ?;",
						parameters);

				StringBuilder c_order_string = new StringBuilder();
				c_order_string.append(cOrderId + ",'" + orderDate.toString() + "',");
				int m = (int) Math.floor(Math.random() * (3 - 1 + 1) + 1);
				int total = 0;
				boolean flag = true;
				while (m != 0) {
					StringBuilder c_order_batch_string = new StringBuilder();
					c_order_batch_string.append(cOrderId + ",");
					int index = (int) (Math.random() * Drugs.size());
					c_order_batch_string.append(Drugs.get(index).get(0) + ",'");
					c_order_batch_string.append(Drugs.get(index).get(1) + "','");
					c_order_batch_string.append(Drugs.get(index).get(2) + "'," + 1);
					c_order_batch.add(c_order_batch_string.toString());
					total += Integer.parseInt(Drugs.get(index).get(3));
					if (Drugs.get(index).get(4).toLowerCase().equals("danger")
							|| Drugs.get(index).get(4).toLowerCase().equals("controlled")) {
						flag = false;
					}
					Drugs.remove(index);
					m--;
				}
				c_order_string.append(total + "," + (int) (0.1 * total) + "," + employeeID);
				c_order.add(c_order_string.toString());

				if (flag) {
					customer2order.add(0 + "," + cOrderId);
					income.add(incomeID + "," + (total - (int) (0.1 * total)) + ",'" + orderDate.toString() + "',"
							+ employeeID + ",0");
				} else {

					ArrayList<ArrayList<String>> customers = Queries
							.queryResult("select customer_nid from customer d\r\n" + "where customer_nid<>'0';", null);
					int index = (int) (Math.random() * customers.size());
					customer2order.add(customers.get(index).get(0) + "," + cOrderId);
					income.add(incomeID + "," + (total - (int) (0.1 * total)) + ",'" + orderDate.toString() + "',"
							+ employeeID + "," + customers.get(index).get(0));
				}
				cOrderId++;
				incomeID++;
				n--;
			}
			orderDate = orderDate.plusDays(1);
		}
		for (String s : income) {

			Queries.queryUpdate(
					"INSERT INTO `income` (`Income_ID`,`Income_amount`,`Income_Date`,`Employee_ID`,`Customer_NID`) VALUES("
							+ s + ");\n",
					null);
		}

		for (String s : c_order) {
			Queries.queryUpdate("INSERT INTO `c_order` VALUES(" + s + ");\n", null);
		}

		for (String s : c_order_batch) {

			Queries.queryUpdate("INSERT INTO `c_order_batch` VALUES(" + s + ");", null);
		}

		for (String s : customer2order) {
			Queries.queryUpdate("INSERT INTO `customer2order` VALUES(" + s + ");", null);
		}
		System.out.println("done");
	}

	public static void payment() throws ClassNotFoundException, SQLException {

		ArrayList<String> payment = new ArrayList<String>();
		ArrayList<String> cheque = new ArrayList<String>();
		ArrayList<String> tax = new ArrayList<String>();
		ArrayList<String> tax2pay = new ArrayList<String>();
		ArrayList<String> salaries = new ArrayList<String>();
		ArrayList<String> supplier = new ArrayList<String>();
		ArrayList<String> banks = new ArrayList<>();
		banks.add("'Arabic Bank'");
		banks.add("'Palestine Bank'");
		banks.add("'Jordan Bank'");
		banks.add("'El Eskan Bank'");
		LocalDate orderDate = LocalDate.parse("2021-06-01");
		int month=6,year=2021;
		int op = 1, paymentId = 1, taxId = 1, chequeId = 1;
		int firstEmployeeSalary = Integer.parseInt(
				Queries.queryResult("select employee_hourly_paid from employee where employee_ID=2", null).get(0).get(0));
		int secondEmployeeSalary = Integer.parseInt(
				Queries.queryResult("select employee_hourly_paid from employee where employee_ID=2", null).get(0).get(0));
		
		
		while (orderDate.compareTo(LocalDate.now()) < 0) {

			// Salary
			if (op == 1) {
				int m = (int) Math.floor(Math.random() * (60 - 50 + 1) + 50);
				Queries.queryUpdate("INSERT INTO `work_hours` VALUES(2,'" + month + "','"
						+ year + "'," + m + ");", null);
				payment.add(paymentId + ",'" + orderDate.toString() + "'," + (m*firstEmployeeSalary) + "," + "'Cash'");
				salaries.add(1 + "," + 2 + "," + paymentId);
				paymentId++;
		/**/		m = (int) Math.floor(Math.random() * (60 - 50 + 1) + 50);
				Queries.queryUpdate("INSERT INTO `work_hours` VALUES(3,'" + month++ + "','"
						+ year + "'," + m + ");", null);
				payment.add(paymentId + ",'" + orderDate.toString() + "'," + (m*secondEmployeeSalary) + "," + "'Cash'");
				salaries.add(1 + "," + 3 + "," + paymentId);
				if(month==13) {
					month=1;year++;
				}
				paymentId++;
				op++;
			}
			// tax
			else if (op == 2) {
				int m = (int) Math.floor(Math.random() * (1500 - 750 + 1) + 750);
				payment.add(paymentId + ",'" + orderDate.toString() + "'," + m + "," + "'Cash'");
				tax.add(taxId + ",'" + orderDate.toString() + "'," + m);
				tax2pay.add(paymentId + "," + taxId + "," + 1);
				paymentId++;
				taxId++;
				op++;
			}

			// supplier payment
			else {

				ArrayList<String> parameters = new ArrayList<>();
				parameters.add(orderDate.toString());
				ArrayList<ArrayList<String>> suppliers = Queries.queryResult(
						"select s.supplier_id,s.supplier_dues,(so.order_cost-so.order_discount),so.date_of_order from supplier s,s_order so \r\n"
								+ "where s.supplier_Id=so.supplier_Id and s.supplier_dues>=(so.order_cost-so.order_discount) and so.date_of_order<?;",
						parameters);
				while (suppliers == null) {
					parameters.clear();
					orderDate = orderDate.plusDays(1);
					parameters.add(orderDate.toString());
					suppliers = Queries.queryResult(
							"select s.supplier_id,s.supplier_dues,(so.order_cost-so.order_discount),so.date_of_order from supplier s,s_order so \r\n"
									+ "where s.supplier_Id=so.supplier_Id and s.supplier_dues>=(so.order_cost-so.order_discount) and so.date_of_order<?;",
							parameters);
				}
				int index = (int) (Math.random() * suppliers.size());
				for (int i = 0; i < 3 && suppliers.size() != 0; i++) {
					int bankIndex = (int) (Math.random() * banks.size());

					payment.add(paymentId + ",'" + orderDate.toString() + "'," + suppliers.get(index).get(2) + ","
							+ "'Cheque'");
					supplier.add(suppliers.get(index).get(0) + ",1," + paymentId);
					cheque.add(chequeId++ + "," + banks.get(bankIndex) + ",'" + orderDate.toString() + "','"
							+ orderDate.plusMonths(1).toString() + "'," + paymentId + ",1");
					Queries.queryUpdate("update Supplier set supplier_dues = supplier_dues - ? where Supplier_ID =?;",
							new ArrayList<>(
									Arrays.asList(suppliers.get(index).get(2) + "", suppliers.get(index).get(0) + "")));
					suppliers.remove(index);
					index = (int) (Math.random() * suppliers.size());
					paymentId++;
				}
				op = 1;
			}
			orderDate = orderDate.plusDays(10);
		}

		for (String s : payment) {
			Queries.queryUpdate("INSERT INTO `payment` VALUES(" + s + ");", null);
		}
		for (String s : cheque) {
			Queries.queryUpdate("INSERT INTO `cheque` VALUES(" + s + ");", null);
		}
		for (String s : tax) {
			Queries.queryUpdate("INSERT INTO `tax` VALUES(" + s + ");", null);
		}
		for (String s : tax2pay) {
			Queries.queryUpdate("INSERT INTO `taxes_payment` VALUES(" + s + ");", null);
		}
		for (String s : salaries) {
			Queries.queryUpdate("INSERT INTO `e_salary` VALUES(" + s + ");", null);
		}
		for (String s : supplier) {
			Queries.queryUpdate("INSERT INTO `supplier_payment` VALUES(" + s + ");", null);
		}
		System.out.println("done");
		System.gc();
	}

}
