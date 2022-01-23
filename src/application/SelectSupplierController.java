package application;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.Queries;
import Relations.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SelectSupplierController implements Initializable {
	

	ObservableList<String> Choices = FXCollections.observableArrayList("Name", "Email","Address");

	@FXML
	private TableView<Supplier> Table;

	@FXML
	private TextField search;

	@FXML
	private Button cancel;

	@FXML
	private Button selectValue;

	@FXML
	private TableColumn<Supplier, String> Address;

	@FXML
	private TableColumn<Supplier, String> Email;

	@FXML
	private TableColumn<Supplier, String> Name;
	
	@FXML
	private ComboBox<String> SearchOp;
	
	public void cancelButton(ActionEvent e) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SearchOp.setValue("Select");
		SearchOp.setItems(Choices);
		Name.setCellValueFactory(new PropertyValueFactory<Supplier, String>("sname"));
		Email.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
		Address.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));

		Table.setItems(Supplier.getDataList());
		
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<Supplier> filteredList = new  ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult("select * from Supplier;",null));
					
				} catch (ClassNotFoundException | SQLException e ) {
					e.printStackTrace();
				}
			}
			else if(SearchOp.getSelectionModel().getSelectedItem()=="Email"){
				try {
					
					filteredList = Supplier.getSupplierData(Queries.queryResult("select * from Supplier where where Supplier_Email like\"%?%\" ;"
			                  ,new ArrayList<>(Arrays.asList(newValue))));
					
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			else if(SearchOp.getSelectionModel().getSelectedItem()=="Name") {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult("select * from Supplier where where Supplier_Name like\"%?%\" ;"
			                  ,new ArrayList<>(Arrays.asList(newValue))));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				
			}
			
		
			else if(SearchOp.getSelectionModel().getSelectedItem()=="Address") {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult("select * from Supplier where Supplier_Address like\"%?%\" ;"
			                  ,new ArrayList<>(Arrays.asList(newValue))));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				
			}else {
				try {
					filteredList = Supplier.getSupplierData(Queries.queryResult("select * from Supplier where Supplier_Address like\"%?%\" or Supplier_Name like\"%?%\" or"
							+ "  supplier_email like \"%?%\";"
			                  ,new ArrayList<>(Arrays.asList(newValue,newValue,newValue))));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
			}
			
			Table.setItems(FXCollections.observableArrayList(filteredList));
		});
		
	}

}