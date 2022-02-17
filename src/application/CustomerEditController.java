package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import Relations.*;

/**
 * 
 * @version 30 January 2022
 * @author Aseel Sabri
 *
 */
public class CustomerEditController implements Initializable {

	private CustomerController caller;

	@FXML
	private TableView<Customer> customerTable;

	@FXML
	private TableColumn<Customer, Double> debtColumn;

	@FXML
	private TableColumn<Customer, String> nidColumn;

	@FXML
	private TableColumn<Customer, String> nameColumn;

	@FXML
	private ImageView addCustomer;

	@FXML
	private ImageView saveCustomer;

	@FXML
	private TextField NIDtextField;

	@FXML
	private TextField nameTextField;

	@FXML
	private ListView<String> phoneList;

	@FXML
	private TextField phoneTextField;

	@FXML
	private ImageView addPhone;

	@FXML
	private ImageView deletePhone;

	@FXML
	private ImageView addedIcon;

	@FXML
	private Label addedLabel;

	private Customer customer;
	private String customerNID = "";
	
	public void showAndFade(Node node) {

		Timeline show = new Timeline(
				new KeyFrame(Duration.seconds(0), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)),
				new KeyFrame(Duration.seconds(0.5), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)),
				new KeyFrame(Duration.seconds(1), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)));
		FadeTransition fade = new FadeTransition(Duration.seconds(0.5), node);
		fade.setFromValue(1);
		fade.setToValue(0);
		SequentialTransition blinkFade = new SequentialTransition(node, show, fade);
		blinkFade.play();
	}

	public void phoneTextOnEnter(KeyEvent e) {
		if (e.getCode() == KeyCode.ENTER) {
			String phone = phoneTextField.getText();

			if (phoneList.getItems().contains(phone.replaceAll("-", ""))) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Phone Number Already Exists");
				alert.showAndWait();
			} else if ((phone.replaceAll("-", "")).matches("[0-9]{10}")) {
				phoneList.getItems().add(phone.replaceAll("-", ""));
				phoneTextField.setText("");
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Phone Number Must Contain 10 Digits");
				alert.showAndWait();
			}
		}
	}

	public void addPhoneOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addPhone.setEffect(effect);
		String phone = phoneTextField.getText();

		if (phoneList.getItems().contains(phone.replaceAll("-", ""))) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Already Exists");
			alert.showAndWait();
		} else if ((phone.replaceAll("-", "")).matches("[0-9]{10}")) {
			phoneList.getItems().add(phone.replaceAll("-", ""));
			phoneTextField.setText("");
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Must Contain 10 Digits");
			alert.showAndWait();
		}
	}

	public void addPhoneOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addPhone.setEffect(effect);
	}

	public void addPhoneOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addPhone.setEffect(effect);
	}

	public void addPhoneOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addPhone.setEffect(effect);
	}

	public void saveOnMousePressed() {
		if (customer != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.8);
			saveCustomer.setEffect(effect);
			ArrayList<String> parameters = new ArrayList<>();
			parameters.add(customerTable.getItems().get(0).getNationalID());
			parameters.add(customerTable.getItems().get(0).getName());
			parameters.add("" + customerTable.getItems().get(0).getDebt());
			parameters.add(customerNID);
			Queries.queryUpdate("update Customer set Customer_NID=? , Customer_Name=? , Customer_debt=? "
					+ " where Customer_NID=? ;", parameters);
			
			caller.saveEdits();
			customerNID = customer.getNationalID();
		}

	}

	public void saveOnMouseReleased() {
		if (customer != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			saveCustomer.setEffect(effect);
		}
	}

	public void saveOnMouseEntered() {
		if (customer != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0.4);
			saveCustomer.setEffect(effect);
		}
	}

	public void saveOnMouseExited() {
		if (customer != null) {
			ColorAdjust effect = new ColorAdjust();
			effect.setBrightness(0);
			saveCustomer.setEffect(effect);
		}
	}

	public void addCustomerOnMousePressed(){
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addCustomer.setEffect(effect);
		String NID = NIDtextField.getText();
		String name = nameTextField.getText();
		if ((Queries.queryResult("select * from Customer where Customer_NID=?;", new ArrayList<>(Arrays.asList(NID))))
				.size() != 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Custemer With This National ID Already Exists");
			alert.showAndWait();
		}
		else if (!NID.matches("[0-9]{9}") || !name.matches("[a-z[A-Z]\\s]+")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText("Wrong Input Format");
			alert.setContentText("National ID Must Be 9 Digits \n" + "Name Must Consist of Alphabetical Characters");//
			alert.showAndWait();
		} else {
			Customer.insertCustomer(NID, name, 0, new ArrayList<>(phoneList.getItems()));
			caller.saveEdits();
			showAndFade(addedIcon);
			showAndFade(addedLabel);
			NIDtextField.setText("");
			nameTextField.setText("");
			phoneTextField.setText("");
			phoneList.getItems().clear();
		}
	}

	public void addCustomerOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addCustomer.setEffect(effect);
	}

	public void addCustomerOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addCustomer.setEffect(effect);
	}

	public void addCustomerOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addCustomer.setEffect(effect);
	}

	public void listOnEditCommit(ListView.EditEvent<String> editedPhone) {
		if (phoneList.getItems().contains(editedPhone.getNewValue().replaceAll("-", "")) && !editedPhone.getNewValue()
				.replaceAll("-", "").equals(phoneList.getItems().get(editedPhone.getIndex()))) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Already Exists");
			alert.showAndWait();
		} else if ((editedPhone.getNewValue().replaceAll("-", "")).matches("[0-9]{10}")) {
			phoneList.getItems().set(editedPhone.getIndex(), editedPhone.getNewValue().replaceAll("-", ""));
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Phone Number Must Contain 10 Digits");
			alert.showAndWait();
		}
	}

	public void deletePhoneOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		deletePhone.setEffect(effect);
		String phone = phoneList.getSelectionModel().getSelectedItem();
		phoneList.getItems().remove(phone);
	}

	public void deletePhoneOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deletePhone.setEffect(effect);
	}

	public void deletePhoneOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		deletePhone.setEffect(effect);
	}

	public void deletePhoneOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deletePhone.setEffect(effect);
	}

	public void setRow(Customer customer, CustomerController caller) {
		this.caller = caller;
		this.customer = customer;
		if (customer != null) {
			customerNID = customer.getNationalID();
		}
		customerTable.setItems(FXCollections.observableArrayList(customer));

		if (Employee.hasAccess() && customer != null) {
			nidColumn.setEditable(true);
			nameColumn.setEditable(true);
			customerTable.setEditable(true);
			nidColumn.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
			nameColumn.setCellFactory(TextFieldTableCell.<Customer>forTableColumn());
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		addedIcon.setOpacity(0);
		addedLabel.setOpacity(0);
		phoneList.setEditable(true);
		phoneList.setCellFactory(TextFieldListCell.forListView());
		nidColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("nationalID"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		debtColumn.setCellValueFactory(new PropertyValueFactory<Customer, Double>("debt"));
		nidColumn.setOnEditCommit((CellEditEvent<Customer, String> t) -> {
			if ((Queries.queryResult("select * from Customer where Customer_NID=? ;",
					new ArrayList<>(Arrays.asList(t.getNewValue())))).size() != 0
					&& !t.getOldValue().equals(t.getNewValue())) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Custemer with this National ID already exists");
				alert.showAndWait();
				((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNationalID(t.getOldValue());
			} else if (t.getNewValue().matches("[0-9]{9}") || t.getNewValue().equals("0")) {
				((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNationalID(t.getNewValue());
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("National ID Must Be 9 Digits");
				alert.showAndWait();

				((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setNationalID(t.getOldValue());
			}
			customerTable.refresh();
		});

		nameColumn.setOnEditCommit((CellEditEvent<Customer, String> t) -> {
			if (t.getNewValue().matches("[a-z[A-Z]\\s]+")) {
				((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
			} else {
				((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getOldValue());
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Wrong Input Format");
				alert.setHeaderText(null);
				alert.setContentText("Name Must Consist of Alphabetical Characters");
				alert.showAndWait();
			}
			customerTable.refresh();
		});
	}
}