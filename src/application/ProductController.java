package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Relations.*;

/**
 * 
 * @version 27 January 2022
 * @author Aseel Sabri
 */
public class ProductController implements Initializable {

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

	private FXMLLoader editLoader;

	private String stringToSearch = "";

	@FXML
	private Button showBatchButton;

	@FXML
	private Button showSoldButton;

	public void showSoldOnAction() {
		if (productTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Product Must Be Chosen First");
			alert.setContentText("Choose A Product From The Table");
			alert.showAndWait();
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SoldProducts.fxml"));
			Parent sold = (Parent) loader.load();
			SoldProductsController show = loader.getController();
			show.setProduct(productTable.getSelectionModel().getSelectedItem().get(0),
					productTable.getSelectionModel().getSelectedItem().get(1));

			Stage batchStage = new Stage();
			Scene scene = new Scene(sold);
			batchStage.setScene(scene);
			batchStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showOnAction() {
		if (productTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Product Must Be Chosen First");
			alert.setContentText("Choose A Product From The Table");
			alert.showAndWait();
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Batch.fxml"));
			Parent batch = (Parent) loader.load();
			BatchController show = loader.getController();
			show.setProductID(productTable.getSelectionModel().getSelectedItem().get(0));
			Stage batchStage = new Stage();
			Scene scene = new Scene(batch);
			batchStage.setScene(scene);
			batchStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dangerSelection() {
		if (isDanger.isSelected()) {
			isDrug.setSelected(true);
			isControlled.setSelected(false);
		}
		filterList();
	}

	public void controlledSelection() {
		if (isControlled.isSelected()) {
			isDrug.setSelected(true);
			isDanger.setSelected(false);
		}
		filterList();
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
		filterList();
	}

	public void editOnMousePressed() {
		if (!Employee.hasAccess()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Access Denied");
			alert.setContentText("You Do Not Have Access To Edit This Table");
			alert.showAndWait();
			return;
		}
		try {
			Relations.Product.getProductData();
		} catch (ClassNotFoundException | SQLException | ParseException e1) {
			e1.printStackTrace();
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		editButton.setEffect(effect);
		try {
			editLoader = new FXMLLoader(getClass().getResource("ProductEdit.fxml"));
			Parent editPane = (Parent) editLoader.load();
			ProductEditController edit = editLoader.getController();
			ArrayList<ArrayList<String>> temp = new ArrayList<>();
			temp.add(productTable.getSelectionModel().getSelectedItem());
			if (productTable.getSelectionModel().getSelectedItem() != null) {
				edit.setRow(temp, this);
			} else {
				edit.setRow(null, this);
			}
			Scene scene = new Scene(editPane);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage editStage = new Stage();
			editStage.setResizable(false);
			editStage.setScene(scene);
			editStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void editOnMouseReleased() {
		if (!Employee.hasAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		editButton.setEffect(effect);
	}

	public void editOnMouseEntered() {
		if (!Employee.hasAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		editButton.setEffect(effect);
	}

	public void editOnMouseExited() {
		if (!Employee.hasAccess()) {
			return;
		}
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		editButton.setEffect(effect);
	}

	public void tableOnMousePressed() {
		if (editLoader != null && productTable.getSelectionModel().getSelectedItem() != null) {
			ProductEditController edit = editLoader.getController();
			ArrayList<ArrayList<String>> temp = new ArrayList<>();
			temp.add(productTable.getSelectionModel().getSelectedItem());
			edit.setRow(temp, this);
		}
	}

	public void saveEdits() {
		filterList();
	}

	public void filterList() {
		ArrayList<ArrayList<String>> filteredList = new ArrayList<>();
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add("%" + stringToSearch + "%");
		if (stringToSearch == null || stringToSearch.isEmpty() || stringToSearch.isBlank()) {
			try {
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
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Product ID") {
			try {
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
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Product Name") {
			try {
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
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Manufacturer") {
			try {
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
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Scientific Name") {
			try {
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
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Risk Pregnency Category") {
			try {
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
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else if (fieldSelector.getSelectionModel().getSelectedItem() == "Drug Category") {
			try {
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
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
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
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		productTable.getItems().clear();
		productTable.setItems(FXCollections.observableArrayList(filteredList));

	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
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

		filterList();

		searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
			this.stringToSearch = newValue;
			filterList();

		});

	}
}