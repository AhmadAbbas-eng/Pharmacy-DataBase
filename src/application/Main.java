package application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

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
			// primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ParseException,
			NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
		Main.readingData();
		launch(args);
	}


	public static void readingData() throws ClassNotFoundException, SQLException, ParseException, IOException {


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

	}

}
