package application;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import Relations.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
			Scene scene = new Scene(root, 712, 497);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ParseException {

		System.out.println("Start reading");
		Main.readingData();
		launch(args);
	}

	public static void readingData() throws ClassNotFoundException, SQLException, ParseException, IOException {
		long startTime = System.nanoTime();

		Batch.getBatchData();
		Cheque.getChequeData();
		Customer.getCustomerData();
		CustomerOrder.getCustomerOrderData();
		Drug.getDrugData();
		Employee.getEmployeeData();
		Payment.getPaymentData();
		Product.getProductData();
		Supplier.getSupplierData();
		SupplierOrder.getSupplierOrderData();
		Tax.getTaxData();
		
		long stopTime = System.nanoTime();
		System.out.println(stopTime - startTime);
	}

}
