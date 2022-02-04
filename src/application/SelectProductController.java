package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Relations.Drug;
import Relations.Product;
import Relations.Queries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @version 30 January 2022
 * @author Ahmad Abbas
 * @author Loor Sawalhi
 */
public class SelectProductController implements Initializable {

	private String stringToSearch = "";

	@FXML
	private ImageView addProductIcon;

	@FXML
	private TableView<Drug> scientificNameTable;

	@FXML
	private TableView<Product> commercialNameTable;

	@FXML
	private TableColumn<Product, String> commercialNameColumn;

	@FXML
	private Button confirmButton;

	@FXML
	private Button cancelButton;

	@FXML
	private TextField costTextField;

	@FXML
	private ListView<String> listOrder;

	@FXML
	private DatePicker orderedDate;

	@FXML
	private TextField quantityTextField;

	@FXML
	private TextField searchTextField;

	@FXML
	private ComboBox<String> searchOperationComboBox;

	@FXML
	private TableColumn<Drug, String> scientificNameColumn;

	@FXML
	private ImageView addDrugIcon;

	@FXML
	private ImageView deleteIcon;

	static double totalCost = 0.0;
	
	private SupplierOrderController caller;

	public void deleteOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		if (listOrder.getItems().isEmpty()) {
			effect.setBrightness(0);
		} else {
			effect.setBrightness(0.8);
			if (listOrder.getSelectionModel().getSelectedItem() != null) {
				String order = listOrder.getSelectionModel().getSelectedItem();
				String arr[] = order.split(",");
				totalCost -= Double.parseDouble(arr[2].split("=")[1]) * Double.parseDouble(arr[3].split("=")[1]);
				listOrder.getItems().remove(order);
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(null);
				alert.setHeaderText(null);
				alert.setContentText("Select item to delete ");
				alert.showAndWait();
			}

		}
		deleteIcon.setEffect(effect);
	}

	public void deleteOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deleteIcon.setEffect(effect);
	}

	public void deleteOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		if (listOrder.getItems().isEmpty()) {
			effect.setBrightness(0);
		} else
			effect.setBrightness(0.4);
		deleteIcon.setEffect(effect);
	}

	public void deleteOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		deleteIcon.setEffect(effect);
	}

	public void addProductOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addProductIcon.setEffect(effect);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("addProduct.fxml"));
		Parent root1;
		try {
			root1 = (Parent) loader.load();

			Stage stage2 = new Stage();
			stage2.setScene(new Scene(root1));
			stage2.initModality(Modality.APPLICATION_MODAL);
			stage2.setResizable(false);
			stage2.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addDrugIcon.setEffect(effect);
		try {

			double Cost = Double.parseDouble(costTextField.getText().toString());
			int Quantity = Integer.parseInt(quantityTextField.getText().toString());

			if (commercialNameTable.getSelectionModel().getSelectedItem() != null) {
				if (Quantity > 0 && Cost > 0) {

					if (!listOrder.getItems().isEmpty()) {
						ArrayList<String> temp = new ArrayList<String>(listOrder.getItems());

						for (int i = 0; i < temp.size(); ++i) {
							String s = temp.get(i);
							String[] data = s.split(",");
							String[] idTemp = data[0].split("=");

							if (commercialNameTable.getSelectionModel().getSelectedItem().getID() == Integer
									.parseInt(idTemp[1])) {
								String[] quantityTemp = data[2].split("=");
								Quantity += Integer.parseInt(quantityTemp[1]);
								listOrder.getSelectionModel().select(i);
								String toDelete = listOrder.getSelectionModel().getSelectedItem();
								listOrder.getItems().remove(toDelete);
								break;
							}
						}
					}

					totalCost += Cost * Quantity;
					listOrder.getItems().add("ID =" + commercialNameTable.getSelectionModel().getSelectedItem().getID()
							+ ",Product Name =" + commercialNameTable.getSelectionModel().getSelectedItem().getName()
							+ ", Quantity=" + Quantity + ", Cost/Peice=" + Cost);

					costTextField.clear();
					quantityTextField.clear();
					scientificNameTable.getSelectionModel().clearSelection();
					commercialNameTable.getSelectionModel().clearSelection();

				} else {
					costTextField.clear();
					quantityTextField.clear();
					scientificNameTable.getSelectionModel().clearSelection();
					commercialNameTable.getSelectionModel().clearSelection();
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle(null);
					alert.setHeaderText(null);
					alert.setContentText("The Cost and Quantity Cannot Be Negative!!");
					alert.showAndWait();
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle(null);
				alert.setHeaderText(null);
				alert.setContentText("Select The Commertial Name");
				alert.showAndWait();
			}

		} catch (NumberFormatException e1) {
			costTextField.clear();
			quantityTextField.clear();
			scientificNameTable.getSelectionModel().clearSelection();
			commercialNameTable.getSelectionModel().clearSelection();
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("Fill the cost and quantity fields");
			alert.showAndWait();
		}
	}

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addDrugIcon.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addDrugIcon.setEffect(effect);
	}

	public void getProductList(ArrayList<String> data, SupplierOrderController caller) {
		listOrder.setItems(FXCollections.observableArrayList(data));
		this.caller = caller;
	}

	public void sendProductList() {
		caller.setProductList(new ArrayList<String>(listOrder.getItems()), totalCost);
	}

	public void addOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addDrugIcon.setEffect(effect);
	}

	public void addDrugList(ActionEvent event) {
		if (listOrder.getItems().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("You Have To Add Something!");
			alert.showAndWait();
		} else {
			sendProductList();
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		}
	}

	public void addProductOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addProductIcon.setEffect(effect);
	}

	public void addProductOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addProductIcon.setEffect(effect);
	}

	public void addProductOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addProductIcon.setEffect(effect);
	}

	public void cancelButton(ActionEvent e) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	public void filterList() {
		ArrayList<Drug> scientificNameArrayList = new ArrayList<>();
		ArrayList<Product> commertialNameArrayList = new ArrayList<>();

		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			scientificNameArrayList = Drug.getDrugData(Queries.queryResult(
					"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
							+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,D.Drug_Pharmacetical_Category"
							+ " from Drug D,Product P,Name_manu m "
							+ "where D.Product_ID=P.Product_ID and P.product_name=m.product_name Group by(D.Drug_Scientific_Name);",
					null));

			commertialNameArrayList = Product.getProductData(
					Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer"
							+ " from product p,Name_manu m" + " where P.product_name=m.product_name;", null));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Commerical Name") {
			scientificNameArrayList = Drug.getDrugData(Queries.queryResult(
					"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
							+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,D.Drug_Pharmacetical_Category"
							+ " from Drug D,Product P,Name_manu m "
							+ "where D.Product_ID=P.Product_ID and P.product_name=m.product_name and P.product_name like ?  Group by(D.Drug_Scientific_Name);",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

			commertialNameArrayList = Product.getProductData(Queries.queryResult(
					"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer"
							+ " from product p,Name_manu m"
							+ " where P.product_name=m.product_name and p.product_name like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));
		} else if (searchOperationComboBox.getSelectionModel().getSelectedItem() == "Sientific Name") {
			commertialNameArrayList = Product.getProductData(Queries.queryResult(
					"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer"
							+ " from  drug d,product p,Name_manu m"
							+ " where P.product_name=m.product_name and D.Product_ID=P.Product_ID and D.Drug_Scientific_Name like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

			scientificNameArrayList = Drug.getDrugData(Queries.queryResult(
					"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
							+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,D.Drug_Pharmacetical_Category"
							+ " from Drug D,Product P,Name_manu m "
							+ "where D.Product_ID=P.Product_ID and P.product_name=m.product_name and D.Drug_Scientific_Name like ?  Group by(D.Drug_Scientific_Name);",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		} else {

			commertialNameArrayList = Product.getProductData(Queries.queryResult(
					"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer"
							+ " from product p,Name_manu m"
							+ " where P.product_name=m.product_name and p.product_name like ? ;",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

			scientificNameArrayList = Drug.getDrugData(Queries.queryResult(
					"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
							+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,D.Drug_Pharmacetical_Category"
							+ " from Drug D,Product P,Name_manu m "
							+ "where D.Product_ID=P.Product_ID and P.product_name=m.product_name and D.Drug_Scientific_Name like ? Group by(D.Drug_Scientific_Name);",
					new ArrayList<>(Arrays.asList("%" + stringToSearch + "%"))));

		}
		commercialNameTable.setItems(FXCollections.observableArrayList(commertialNameArrayList));
		scientificNameTable.setItems(FXCollections.observableArrayList(scientificNameArrayList));
	}

	public void scientificNameTableOnSelected() {
		if (scientificNameTable.getSelectionModel().getSelectedIndex() != -1) {
			String scientificName = scientificNameTable.getSelectionModel().getSelectedItem().getScientificName();
			commercialNameTable.setItems(FXCollections.observableArrayList(Product.getProductData(Queries.queryResult(
					"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer"
							+ " from  drug d,product p,Name_manu m"
							+ " where P.product_name=m.product_name and D.Product_ID=P.Product_ID and D.Drug_Scientific_Name = ? ;",
					new ArrayList<>(Arrays.asList(scientificName))))));
		}
	}

	ObservableList<String> Choices = FXCollections.observableArrayList("Sientific Name", "Commerical Name");

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Drug.getDrugData();
		Product.getProductData();
		searchOperationComboBox.setValue("-Specify Field-");
		searchOperationComboBox.setItems(Choices);
		scientificNameColumn.setCellValueFactory(new PropertyValueFactory<Drug, String>("scientificName"));
		commercialNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Name"));
		scientificNameTable.setItems(Drug.getDataListDrug());
		commercialNameTable.setItems(Product.getDataList());

		searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			stringToSearch = newValue;
			filterList();

		});

	}
}
