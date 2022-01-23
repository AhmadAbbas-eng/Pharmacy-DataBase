package application;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.*;
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

public class SelectEmployeeController  implements Initializable {
	ObservableList<String> Choices = FXCollections.observableArrayList("National ID", "Name");
    @FXML
    private ComboBox<String> searchOp;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Employee,String> NationalID;

    @FXML
    private TableColumn<Employee,String> Name;

    @FXML
    private TableView<Employee> Table;

    @FXML
    private Button cancel;

    @FXML
    private Button selectValue;

    @FXML
    private Button select;
    
    public void Cancel(ActionEvent event) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
    }
    
    public void selectEmployee(ActionEvent event) throws IOException {
		Stage stage = (Stage) select.getScene().getWindow();
		stage.close();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		searchOp.setValue("select");
		searchOp.setItems(Choices);
		Name.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		NationalID.setCellValueFactory(new PropertyValueFactory<Employee, String>("NID"));
		
		Table.setItems(Employee.getDataList());
		
		search.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<Employee> filteredList = new  ArrayList<>();
			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					
					filteredList = Employee.getEmployeeData(Queries.queryResult("select * from employee ;",null));
				} catch (ClassNotFoundException | SQLException e ) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else if(searchOp.getSelectionModel().getSelectedItem()=="National ID"){
				try {
					filteredList = Employee.getEmployeeData(Queries.queryResult("select * from employee where Employee_National_ID like \"%?%\";"
							,new ArrayList<>(Arrays.asList(newValue))));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else if(searchOp.getSelectionModel().getSelectedItem()=="Name") {
				try {
					filteredList = Employee.getEmployeeData(Queries.queryResult("select * from employee where Employee_name like \"%?%\";"
							,new ArrayList<>(Arrays.asList(newValue))));
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
			Table.setItems(FXCollections.observableArrayList(filteredList));
		});
		
	}

}
