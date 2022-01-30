package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import Relations.*;

/**
 * 
 * @version 30 January 2022
 * @author Aseel Sabri
 *
 */
public class ChooseProductController implements Initializable {

	@FXML
	private TableColumn<ArrayList<String>, String> idColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> nameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> priceColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> manufacturerColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> scientificNameColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> pregnencyCategoryColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> dosageColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> categoryColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> dosageFormColumn;

	@FXML
	private TableColumn<ArrayList<String>, String> pharmaceticalCategoryColumn;

	@FXML
	private TableView<ArrayList<String>> productTable;

	@FXML
	private TextField searchBox;

	@FXML
	private ImageView editButton;

	@FXML
	private ComboBox<String> fieldSelector;

	@FXML
	private CheckBox isControlled;

	@FXML
	private CheckBox isDanger;

	@FXML
	private CheckBox isDrug;

	@FXML
	private Button showBatchButton;

	@FXML
	private Button addButton;

	@FXML
	private TableView<Batch> batchTable;

	@FXML
	private TableColumn<Batch, LocalDate> productionDateColumn;

	@FXML
	private TableColumn<Batch, LocalDate> expiryDateColumn;

	@FXML
	private TableColumn<Batch, LocalDate> amountColumn;

	@FXML
	private ComboBox<String> dateSelector;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Label title;

	@FXML
	private Label totalQuantityLable;

	@FXML
	private ImageView addedIcon;

	@FXML
	private Label addedLabel;

	@FXML
	private TextField amountTextField;

	SellController caller;
	private String stringToSearch = "";
	private ObservableList<ArrayList<String>> chosenProducts;

	public void addProductOnAction() {
		int numOfChosenProducts = (chosenProducts == null ? 0 : chosenProducts.size());
		String amountStr = amountTextField.getText();

		if (batchTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("No Batch Was Selected");
			alert.showAndWait();
		} else if (amountStr == null || amountStr.isBlank() || amountStr.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Missing Info");
			alert.setHeaderText(null);
			alert.setContentText("Amount Must Be Specified");
			alert.showAndWait();
		} else if (!amountStr.matches("[1-9][0-9]*")) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Wrong Input Format");
			alert.setHeaderText(null);
			alert.setContentText("Amount Must Be An Integer Number");
			alert.showAndWait();
		} else if (Integer.parseInt(amountStr) > batchTable.getSelectionModel().getSelectedItem().getAmount()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("There Isn't Enough Amount");
			alert.showAndWait();
		} else {
			ArrayList<String> tempProduct = new ArrayList<>(
					Arrays.asList(productTable.getSelectionModel().getSelectedItem().get(0),
							productTable.getSelectionModel().getSelectedItem().get(1),
							batchTable.getSelectionModel().getSelectedItem().getProductionDate().toString(),
							batchTable.getSelectionModel().getSelectedItem().getExpiryDate().toString(),
							productTable.getSelectionModel().getSelectedItem().get(2), amountStr));

			boolean flag = true;
			Batch batch = batchTable.getSelectionModel().getSelectedItem();

			for (int j = 0; chosenProducts != null && j < chosenProducts.size(); j++) {
				if (chosenProducts.get(j).get(0).equals(batch.getID() + "")
						&& LocalDate.parse(chosenProducts.get(j).get(2)).compareTo(batch.getProductionDate()) == 0
						&& LocalDate.parse(chosenProducts.get(j).get(3)).compareTo(batch.getExpiryDate()) == 0) {

					flag = false;
					chosenProducts.get(j).set(5,
							"" + (Integer.parseInt(chosenProducts.get(j).get(5)) + Integer.parseInt(amountStr)));

					break;
				}
			}

			if (flag) {
				chosenProducts.add(tempProduct);
			}
			caller.updateSelectedProducts();

			filterBatchList();
			showAndFade(addedIcon);
			showAndFade(addedLabel);
			if (numOfChosenProducts < chosenProducts.size()
					&& productTable.getSelectionModel().getSelectedItem().size() > 9
					&& productTable.getSelectionModel().getSelectedItem().get(9).toLowerCase().equals("danger")) {

				caller.numbrOfDangerDrugs++;
			}
			amountTextField.setText("");
		}
	}

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

	public void filterBatchList(){
		ArrayList<Batch> batch = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		if (productTable.getSelectionModel().getSelectedItem() != null) {
			String productID = productTable.getSelectionModel().getSelectedItem().get(0);
			parameters.add(productID);
			if (datePicker.getValue() == null || datePicker.getValue().toString().isBlank()
					|| datePicker.getValue().toString().isEmpty()) {
				batch = Batch
						.getBatchData(Queries.queryResult(
								"select * from Batch where Product_ID=? and Batch_Amount>0 "
										+ " and Batch_Production_Date<>'1111-01-01' order by Batch_Expiry_Date;",
								parameters));

			} else {
				String date = datePicker.getValue().toString();
				parameters.add(date);
				if (dateSelector.getSelectionModel().getSelectedItem().equals("Expires On")) {
					batch = Batch.getBatchData(Queries.queryResult(
							"select * from Batch where Product_ID=? and Batch_Production_Date<>'1111-01-01' "
									+ " and Batch_Expiry_Date=? and Batch_Amount>0 order by Batch_Expiry_Date;",
							parameters));

				} else if (dateSelector.getSelectionModel().getSelectedItem().equals("Expires Before")) {
					batch = Batch.getBatchData(Queries.queryResult(
							"select * from Batch where Product_ID=? and Batch_Production_Date<>'1111-01-01' "
									+ " and Batch_Expiry_Date<? and Batch_Amount>0 order by Batch_Expiry_Date;",
							parameters));

				} else {// Expires After
					batch = Batch.getBatchData(Queries.queryResult(
							"select * from Batch where Product_ID=? and Batch_Production_Date<>'1111-01-01' "
									+ " and Batch_Expiry_Date>? and Batch_Amount>0 order by Batch_Expiry_Date;",
							parameters));
				}
			}
		}
		for (int i = 0; i < batch.size(); i++) {
			for (int j = 0; chosenProducts != null && j < chosenProducts.size(); j++) {
				if (chosenProducts.get(j).get(0).equals(batch.get(i).getID() + "")
						&& LocalDate.parse(chosenProducts.get(j).get(2))
								.compareTo(batch.get(i).getProductionDate()) == 0
						&& LocalDate.parse(chosenProducts.get(j).get(3)).compareTo(batch.get(i).getExpiryDate()) == 0) {
					batch.get(i).setAmount(batch.get(i).getAmount() - Integer.parseInt(chosenProducts.get(j).get(5)));
				}
			}
		}
		batchTable.setItems(FXCollections.observableArrayList(batch));
	}

	public void dangerSelection() {
		if (isDanger.isSelected()) {
			isDrug.setSelected(true);
			isControlled.setSelected(false);
		}
		batchTable.getItems().clear();
		filterProductList();
	}

	public void controlledSelection() {
		if (isControlled.isSelected()) {
			isDrug.setSelected(true);
			isDanger.setSelected(false);
		}
		batchTable.getItems().clear();
		filterProductList();
	}

	public void drugSelection() {
		if (!isDrug.isSelected()) {
			isControlled.setSelected(false);
			isDanger.setSelected(false);
			if (fieldSelector.getSelectionModel().getSelectedItem() != null && fieldSelector.getSelectionModel()
					.getSelectedItem().matches("(Scientific Name|Risk Pregnency Category|Drug Category)")) {
				fieldSelector.setValue("-Specify Field-");
			}
		}
		batchTable.getItems().clear();
		filterProductList();
	}

	public void productTableOnMousePressed() {
		if (productTable.getSelectionModel().getSelectedItem() != null) {
			filterBatchList();
		}
	}

	public void filterProductList() {
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("%" + stringToSearch + "%");
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			if (!isDrug.isSelected()) {
				filteredList = (Queries
						.queryResult("select P.Product_ID, P.Product_Name, P.Product_Price, NM.Product_Manufactrer"
								+ " from product P, Name_Manu NM" + " where P.Product_Name = NM.Product_Name"
								+ " and P.Product_ID not in (select D.Product_ID from Drug D);", null));

			}
			if (!isDanger.isSelected() && !isControlled.isSelected()) { // All Drugs
				filteredList.addAll(Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name;",
						null));

			} else if (isDanger.isSelected()) { // Danger Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Danger';",
						null);

			} else { // Controlled Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Controlled';",
						null);

			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Product ID") {
			if (!isDrug.isSelected()) {
				filteredList.addAll(Queries
						.queryResult("select P.Product_ID, P.Product_Name, P.Product_Price, NM.Product_Manufactrer"
								+ " from product P, Name_Manu NM" + " where P.Product_Name = NM.Product_Name"
								+ " and P.Product_ID not in (select D.Product_ID from Drug D)"
								+ " and P.Product_ID like ?;", parameters));

			}
			if (!isDanger.isSelected() && !isControlled.isSelected()) { // All Drugs
				filteredList.addAll(Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and P.Product_ID like ?;",
						parameters));

			} else if (isDanger.isSelected()) { // Danger Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Danger'" + " and P.Product_ID like ?;",
						parameters);

			} else { // Controlled Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Controlled' and P.Product_ID like ?;",
						parameters);

			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Product Name") {
			if (!isDrug.isSelected()) {
				filteredList.addAll(Queries
						.queryResult("select P.Product_ID, P.Product_Name, P.Product_Price, NM.Product_Manufactrer"
								+ " from product P, Name_Manu NM" + " where P.Product_Name = NM.Product_Name"
								+ " and P.Product_ID not in (select D.Product_ID from Drug D)"
								+ " and P.Product_Name like ?;", parameters));

			}
			if (!isDanger.isSelected() && !isControlled.isSelected()) { // All Drugs
				filteredList.addAll(Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and P.Product_Name like ?;",
						parameters));

			} else if (isDanger.isSelected()) { // Danger Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Danger' and P.Product_Name like ?;",
						parameters);

			} else { // Controlled Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Controlled' and P.Product_Name like ?;",
						parameters);

			}

		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Manufacturer") {
			if (!isDrug.isSelected()) {
				filteredList.addAll(Queries
						.queryResult("select P.Product_ID, P.Product_Name, P.Product_Price, NM.Product_Manufactrer"
								+ " from product P, Name_Manu NM" + " where P.Product_Name = NM.Product_Name"
								+ " and P.Product_ID not in (select D.Product_ID from Drug D)"
								+ " and NM.Product_Manufactrer like ?;", parameters));

			}
			if (!isDanger.isSelected() && !isControlled.isSelected()) { // All Drugs
				filteredList.addAll(Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,NM.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu NM"
								+ " where D.Product_ID=P.Product_ID and P.product_name=NM.product_name"
								+ " and NM.Product_Manufactrer like ?;",
						parameters));

			} else if (isDanger.isSelected()) { // Danger Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Danger'"
								+ " and NM.Product_Manufactrer like ?;",
						parameters);

			} else { // Controlled Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Controlled'"
								+ " and NM.Product_Manufactrer like ?;",
						parameters);

			}

		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Scientific Name") {
			isDrug.setSelected(true);
			if (!isDanger.isSelected() && !isControlled.isSelected()) { // All Drugs
				filteredList.addAll(Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,NM.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu NM"
								+ " where D.Product_ID=P.Product_ID and P.product_name=NM.product_name"
								+ " and D.Drug_Scientific_Name like ?;",
						parameters));

			} else if (isDanger.isSelected()) { // Danger Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Danger'"
								+ " and D.Drug_Scientific_Name like ?;",
						parameters);

			} else { // Controlled Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Controlled'"
								+ " and D.Drug_Scientific_Name like ?;",
						parameters);

			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Risk Pregnency Category") {
			isDrug.setSelected(true);
			if (!isDanger.isSelected() && !isControlled.isSelected()) { // All Drugs
				filteredList.addAll(Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,NM.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu NM"
								+ " where D.Product_ID=P.Product_ID and P.product_name=NM.product_name"
								+ " and D.Drug_Risk_Pregnency_Category like ?;",
						parameters));

			} else if (isDanger.isSelected()) { // Danger Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Danger'"
								+ " and D.Drug_Risk_Pregnency_Category like ?;",
						parameters);

			} else { // Controlled Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Controlled'"
								+ " and D.Drug_Risk_Pregnency_Category like ?;",
						parameters);

			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Drug Category") {
			isDrug.setSelected(true);
			if (!isDanger.isSelected() && !isControlled.isSelected()) { // All Drugs
				filteredList.addAll(Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,NM.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu NM"
								+ " where D.Product_ID=P.Product_ID and P.product_name=NM.product_name"
								+ " and D.Drug_Category like ?;",
						parameters));

			} else if (isDanger.isSelected()) { // Danger Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Danger' and D.Drug_Category like ?;",
						parameters);

			} else { // Controlled Drugs
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu m"
								+ " where D.Product_ID=P.Product_ID and P.product_name=m.product_name"
								+ " and D.Drug_Pharmacetical_Category='Controlled' and D.Drug_Category like ?;",
						parameters);

			}
		} else {
			if (!isDrug.isSelected()) {
				parameters.add("%" + stringToSearch + "%");
				parameters.add("%" + stringToSearch + "%");
				filteredList.addAll(Queries
						.queryResult("select P.Product_ID, P.Product_Name, P.Product_Price, NM.Product_Manufactrer"
								+ " from product P, Name_Manu NM" + " where P.Product_Name = NM.Product_Name"
								+ " and P.Product_ID not in (select D.Product_ID from Drug D)"
								+ " and (P.Product_ID like ? or" + " P.Product_Name like ? or"
								+ " NM.Product_Manufactrer like ?);", parameters));

			}
			if (!isDanger.isSelected() && !isControlled.isSelected()) { // All Drugs
				while (parameters.size() < 6) {
					parameters.add("%" + stringToSearch + "%");
				}
				filteredList.addAll(Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,NM.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu NM"
								+ " where D.Product_ID=P.Product_ID and P.product_name=NM.product_name"
								+ " and (P.Product_ID like ? or P.Product_Name like ? "
								+ " or NM.Product_Manufactrer like ? " + " or D.Drug_Scientific_Name like ? or "
								+ " D.Drug_Risk_Pregnency_Category like ? or " + " D.Drug_Category like ? );",
						parameters));

			} else if (isDanger.isSelected()) { // Danger Drugs
				while (parameters.size() < 6) {
					parameters.add("%" + stringToSearch + "%");
				}
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,NM.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu NM"
								+ " where D.Product_ID=P.Product_ID and P.product_name=NM.product_name"
								+ " and D.Drug_Pharmacetical_Category='Danger'" + " and (P.Product_ID like ? "
								+ " or P.Product_Name like ? or" + " NM.Product_Manufactrer like ? or"
								+ " D.Drug_Scientific_Name like ? or" + " D.Drug_Risk_Pregnency_Category like ? or"
								+ " D.Drug_Category like ?);",
						parameters);

			} else { // Controlled Drugs
				while (parameters.size() < 6) {
					parameters.add("%" + stringToSearch + "%");
				}
				filteredList = Queries.queryResult(
						"select P.Product_ID, P.Product_Name,P.Product_Price,NM.Product_Manufactrer,D.Drug_Scientific_Name,"
								+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,"
								+ "D.Drug_Pharmacetical_Category from Drug D,Product P,Name_manu NM"
								+ " where D.Product_ID=P.Product_ID and P.product_name=NM.product_name"
								+ " and D.Drug_Pharmacetical_Category='Controlled'" + " and (P.Product_ID like ? or"
								+ " P.Product_Name like ? or" + " NM.Product_Manufactrer like ? or"
								+ " D.Drug_Scientific_Name like ? or" + " D.Drug_Risk_Pregnency_Category like ? or"
								+ " D.Drug_Category like ?);",
						parameters);

			}
		}
		productTable.getItems().clear();
		productTable.setItems(FXCollections.observableArrayList(filteredList));
	}

	public void getChosenProducts(ObservableList<ArrayList<String>> chosenProducts, SellController caller) {
		this.caller = caller;
		this.chosenProducts = chosenProducts;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		addedIcon.setOpacity(0);
		addedLabel.setOpacity(0);
		fieldSelector.setItems(FXCollections.observableArrayList("-Specify Field-", "Product ID", "Product Name",
				"Manufacturer", "Scientific Name", "Risk Pregnency Category", "Drug Category"));
		idColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 0) {
							return new SimpleStringProperty(x.get(0));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		nameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 1) {
							return new SimpleStringProperty(x.get(1));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		priceColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 2) {
							return new SimpleStringProperty(x.get(2));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		manufacturerColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 3) {
							return new SimpleStringProperty(x.get(3));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		scientificNameColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 4) {
							return new SimpleStringProperty(x.get(4));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		pregnencyCategoryColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 5) {
							return new SimpleStringProperty(x.get(5));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		dosageColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 6) {
							return new SimpleStringProperty(x.get(6));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		categoryColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 7) {
							return new SimpleStringProperty(x.get(7));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		dosageFormColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 8) {
							return new SimpleStringProperty(x.get(8));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		pharmaceticalCategoryColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
						ArrayList<String> x = p.getValue();
						if (x != null && x.size() > 9) {
							return new SimpleStringProperty(x.get(9));
						} else {
							return new SimpleStringProperty("-");
						}
					}
				});

		filterProductList();

		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			this.stringToSearch = newValue;
			filterProductList();

		});

		dateSelector.setItems(FXCollections.observableArrayList("Expires On", "Expires After", "Expires Before"));
		dateSelector.setValue("Expires On");
		productionDateColumn.setCellValueFactory(new PropertyValueFactory<Batch, LocalDate>("productionDate"));
		expiryDateColumn.setCellValueFactory(new PropertyValueFactory<Batch, LocalDate>("expiryDate"));
		amountColumn.setCellValueFactory(new PropertyValueFactory<Batch, LocalDate>("amount"));

		datePicker.setConverter(new StringConverter<LocalDate>() {
			private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null)
					return "";
				return dateTimeFormatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, dateTimeFormatter);
			}
		});

	}
}