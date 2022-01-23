package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TableView;

import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Relations.Supplier;

@SuppressWarnings("unused")
public class SampleController implements Initializable {
	
    @FXML
    private Button addSupplier;

    @FXML
    private Button backSupplier;

    @FXML
    private Button listSuppliers;

    @FXML
    private Button searchSupplier;
	@FXML
	private TableView<Supplier> tableView;
	@FXML
	private TableView<Supplier> supplier;
	@FXML
	private TableColumn<Supplier, Integer> ID;
	@FXML
	private TableColumn<Supplier, String> Name;
	@FXML
	private TableColumn<Supplier, String> Address;
	@FXML
	private TableColumn<Supplier, String> Phone;
	@FXML
	private TableColumn<Supplier, Double> Dues;
	@FXML
	private TableColumn<Supplier, String> Email;
	@FXML
	private Button cancel;
	@FXML
	private Label warning;
	@FXML
	private Button confirm;
	@FXML
	private TextField user;
	@FXML
	private PasswordField password;
	@FXML
	private Button add;
	@FXML
	private TextField address;
	@FXML
	private TextField dues;

	@FXML
	private TextField email;

	@FXML
	private TextField name;

	@FXML
	private TextField phone;

	public void confirmOnAction(ActionEvent e) {
		if (user.getText().isBlank() == false && password.getText().isBlank() == false) {
			//warning.setText("You are trying to login");
		} else {
			warning.setText("You have to enter the user name and password");
		}
	}

	public void cancelOnAction(ActionEvent e) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}
	public void searchSupplierOnAction(ActionEvent e) {
		try {
			Stage stage = (Stage) cancel.getScene().getWindow();
			stage.close();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("addSupplier.fxml"));
		Parent root1 = (Parent)loader.load();
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();
			root1 = loader.load();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	/*public void addOnAction(ActionEvent e) throws SQLException, ClassNotFoundException {
		if( address.getText().isBlank()== false && email.getText().isBlank() == false && name.getText().isBlank() == false) {
			Main.connectDB();
			double tempDues = 0.0;
			if(dues.getText().isBlank()== false){
				tempDues = Double.parseDouble(dues.getText());
			}
			Supplier temp = new Supplier(
					Supplier.getCounter(),
					name.getText(),
					address.getText(),
					email.getText(),
					tempDues, null);
			Main.data.add(temp);
			Main.dataList.add(temp);
			//tableView.getItems().add(temp); 
			String SQL =  "insert into supplier values("+Supplier.getCounter()+",'"+name.getText()+"','"+address.getText()+"','"+email.getText()+"',"+tempDues+");";
			name.clear();
            address.clear();
            email.clear();
            dues.clear();
          
        	Statement stmt = Main.con.createStatement();
        	stmt.executeUpdate(SQL);
    		warning.setText("Adding a new supplier ...");
            Main.con.close();
		}else {
			warning.setText("Not being able to add the new supplier! -_-");
		}
	}*/

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		/*ID.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("sid"));
		Name.setCellValueFactory(new PropertyValueFactory<Supplier, String>("sname"));
		Address.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
		Email.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
		Dues.setCellValueFactory(new PropertyValueFactory<Supplier, Double>("dues"));

		tableView.setItems(Main.dataList);
		System.out.println("Test");*/
	}

}