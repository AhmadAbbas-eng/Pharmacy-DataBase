package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Relations.*;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * 
 * @version 30 January 2022
 * @author Aseel Sabri
 *
 */
public class ChooseCustomerController implements Initializable {

	@FXML
	private TableView<Customer> customerTable;

	@FXML
	private TextField NIDtextField;

	@FXML
	private ImageView addCustomer;

	@FXML
	private ImageView addPhone;

	@FXML
	private TableColumn<Customer, Double> debtColumn;

	@FXML
	private ImageView deletePhone;

	@FXML
	private ComboBox<String> fieldSelector;

	@FXML
	private TableColumn<Customer, String> nameColumn;

	@FXML
	private TextField nameTextField;

	@FXML
	private TableColumn<Customer, String> nidColumn;

	@FXML
	private ListView<String> phoneList;

	@FXML
	private TextField phoneTextField;

	@FXML
	private TextField searchBox;

	@FXML
	private Button selectButton;

	@FXML
	private ImageView addedIcon;

	@FXML
	private Label addedLabel;

	private SellController caller;
	private String stringToSearch;

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

	public void selectCustomerOnAction() {
		if (customerTable.getSelectionModel().getSelectedItem() != null) {
			caller.setCustomerNID(customerTable.getSelectionModel().getSelectedItem().getNationalID());
			showAndFade(addedIcon);
			showAndFade(addedLabel);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Customer Must Be Chosen First");
			alert.showAndWait();
		}
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

	public void addCustomerOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addCustomer.setEffect(effect);
		Double debt = 0.0;
		String NID = NIDtextField.getText();
		String name = nameTextField.getText();
		ArrayList<String> parameter = new ArrayList<>();
		parameter.add(NID);

		if ((Queries.queryResult("select * from Customer where Customer_NID=? ;", parameter)).size() != 0) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Custemer With This National ID Already Exists");
			alert.showAndWait();
		} else if (!NID.matches("[0-9]{9}") || !name.matches("[a-z[A-Z]\\s]+")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText("Wrong Input Format");
			alert.setContentText("National ID Must Be 9 Digits \n" + "Name Must Consist of Alphabetical Characters");//
			alert.showAndWait();
		} else {
			Customer.insertCustomer(NID, name, debt, new ArrayList<>(phoneList.getItems()));
			filterList();
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

	/**
	 * Filter the data shown on screen in terms of search test field
	 */
	public void filterList() {
		ArrayList<Customer> filteredList = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("%" + stringToSearch + "%");
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			filteredList = Customer.getCustomerData(Queries
					.queryResult("select * from Customer where Customer_NID<>'0' order by Customer_name;", null));
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "National ID") {
			filteredList = Customer.getCustomerData(Queries.queryResult("select * from Customer"
					+ " where Customer_NID<>'0' and Customer_NID like ? " + " order by Customer_name;", parameters));
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Name") {
			filteredList = Customer.getCustomerData(Queries.queryResult("select * from Customer"
					+ " where Customer_NID<>'0' and Customer_Name like ? " + " order by Customer_name;", parameters));

		} else {
			parameters.add("%" + stringToSearch + "%");
			filteredList = Customer.getCustomerData(
					Queries.queryResult("select * from Customer " + " where Customer_NID<>'0' and Customer_Name like ? "
							+ " or Customer_NID like ? " + " order by Customer_name;", parameters));
		}
		customerTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		addedIcon.setOpacity(0);
		addedLabel.setOpacity(0);
		fieldSelector.setItems(FXCollections.observableArrayList("-Specify Field-", "National ID", "Name"));
		nidColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("NID"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		debtColumn.setCellValueFactory(new PropertyValueFactory<Customer, Double>("debt"));
		filterList();
		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch = newValue;
			filterList();
		});

	}

	public void setCaller(SellController caller) {
		this.caller = caller;
	}
}
