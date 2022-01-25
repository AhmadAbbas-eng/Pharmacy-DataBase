package application;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import 	java.security.SecureRandom;
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

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, ParseException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {

	   // System.out.println(encryptPassword("Abedelkareem", "123456789123456789"));
		Main.readingData();
		launch(args);
	}

	public static String encryptPassword(String name ,String password) {
		StringBuilder hashed = new StringBuilder();
			for(int j=0;j<password.length();j++) {
				int temp= (name.charAt(0)+name.charAt(name.length()-1))*(j+1)+password.charAt(j);				
				hashed.append(Integer.toHexString(temp));
			}
		return hashed.toString();
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
