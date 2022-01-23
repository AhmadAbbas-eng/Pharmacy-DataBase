package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SelectProductController implements Initializable {

	static double totalCost = 0.0;

    @FXML
    private ImageView addProduct;
	
	@FXML
	private TableView<Drug> Table;

	private SupplierOrderController caller;

	@FXML
	private TableView<Product> Table2;

	@FXML
	private TableColumn<Product, String> cname;

	@FXML
	private Button confirm;

	@FXML
	private Button cancel;

	@FXML
	private TextField cost;

	@FXML
	private ListView<String> listOrder;

	@FXML
	private DatePicker orderedDate;

	@FXML
	private TextField quantity;

	@FXML
	private TextField search;

	@FXML
	private ComboBox<String> searchOP;

	@FXML
	private TableColumn<Drug, String> sname;

	@FXML
	private ImageView addDrug;

	@FXML
	private Label warning;

	@FXML
	private ImageView delete;

	public void deleteOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		if (listOrder.getItems().isEmpty()) {
			effect.setBrightness(0);
			warning.setText("");
		} else {
			effect.setBrightness(0.8);
			String order = listOrder.getSelectionModel().getSelectedItem();
			listOrder.getItems().remove(order);
			warning.setText("");
		}
		delete.setEffect(effect);

	}

	public void deleteOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		delete.setEffect(effect);
	}

	public void deleteOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		if (listOrder.getItems().isEmpty()) {
			effect.setBrightness(0);
		} else
			effect.setBrightness(0.4);
		delete.setEffect(effect);
	}

	public void deleteOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		delete.setEffect(effect);
	}

	public void addProductOnMousePressed() throws IOException {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addDrug.setEffect(effect);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("addProduct.fxml"));
		Parent root1 = (Parent) loader.load();
		Stage stage2 = new Stage();
		stage2.setScene(new Scene(root1));
		stage2.show();
	}
	
	public void addOnMousePressed() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.8);
		addDrug.setEffect(effect);
		try {
			
			double Cost = Double.parseDouble(cost.getText().toString());
			int Quantity = Integer.parseInt(quantity.getText().toString());
			
			if (Table2.getSelectionModel().getSelectedItem() != null) {
				if (Quantity > 0 && Cost > 0) {
					
				if(!listOrder.getItems().isEmpty()) {
					ArrayList<String> temp = new ArrayList<String>(listOrder.getItems());
					
					for(int i=0; i< temp.size();++i) {
						String s = temp.get(i);
						String[] data = s.split(",");
						String[] idTemp = data[0].split("=");
						System.out.println(idTemp[0] +Table2.getSelectionModel().getSelectedItem().getID() );
						if(Table2.getSelectionModel().getSelectedItem().getID()+"".compareTo(idTemp[1]) == 0) {
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
					listOrder.getItems().add("ID ="+Table2.getSelectionModel().getSelectedItem().getID() + ",Product Name ="+Table2.getSelectionModel().getSelectedItem().getName() + ", Quantity="
							+ Quantity + ", Cost/Peice=" + Cost);
					warning.setText("");
					cost.clear();
					quantity.clear();
					Table.getSelectionModel().clearSelection();

				} else {
					cost.clear();
					quantity.clear();
					Table.getSelectionModel().clearSelection();
					warning.setText("The Cost and Quantity Cannot Be Negative!!");
				}
			} else {
				warning.setText("Select The Commertial Name");
			}

		} catch (NumberFormatException e1) {
			cost.clear();
			quantity.clear();
			Table.getSelectionModel().clearSelection();
			warning.setText("Fill the cost and quantity fields");
		}

	}

	public void addOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addDrug.setEffect(effect);
	}

	public void addOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addDrug.setEffect(effect);
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
		addDrug.setEffect(effect);
	}

	public void addDrugList(ActionEvent event) {
		if (listOrder.getItems().isEmpty()) {
			warning.setText("You Have To Add Something!!");
		} else {
			sendProductList();
			Stage stage = (Stage) cancel.getScene().getWindow();
			stage.close();
		}
	}
	
	

	public void addProductOnMouseReleased() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addDrug.setEffect(effect);
	}

	public void addProductOnMouseEntered() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0.4);
		addDrug.setEffect(effect);
	}

	public void addProductOnMouseExited() {
		ColorAdjust effect = new ColorAdjust();
		effect.setBrightness(0);
		addDrug.setEffect(effect);
	}

	public void cancelButton(ActionEvent e) {
		Stage stage = (Stage) cancel.getScene().getWindow();
		stage.close();
	}

	ObservableList<String> Choices = FXCollections.observableArrayList("Sientific Name", "Commerical Name");

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		searchOP.setValue("Select");
		searchOP.setItems(Choices);
		sname.setCellValueFactory(new PropertyValueFactory<Drug, String>("scientificName"));
		cname.setCellValueFactory(new PropertyValueFactory<Product, String>("Name"));
		
		Table.setItems(Drug.getDataListDrug());
		Table2.setItems(Product.getDataList());

		search.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<Drug> filteredList = new ArrayList<>();
			ArrayList<Product> filteredList2 = new ArrayList<>();

			if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
				try {
					
					filteredList = Drug.getDrugData(Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
							+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,D.Drug_Pharmacetical_Category"
							+ " from Drug D,Product P,Name_manu m "
							+ "where D.Product_ID=P.Product_ID and P.product_name=m.product_name;"
							,null));
					
					filteredList2 = Product.getProductData( Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer"
							+ " from product p,Name_manu m"
							+ " where P.product_name=m.product_name;"
							,null));

				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Commerical Name") {
				try {
					
					
					filteredList = Drug.getDrugData(Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
							+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,D.Drug_Pharmacetical_Category"
							+ " from Drug D,Product P,Name_manu m "
							+ "where D.Product_ID=P.Product_ID and P.product_name=m.product_name and P.product_name like ? ;"
							,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
					
					
					filteredList2 = Product.getProductData( Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer"
							+ " from product p,Name_manu m"
							+ " where P.product_name=m.product_name and p.product_name like ? ;"
			                  ,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));

				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}
			} else if (searchOP.getSelectionModel().getSelectedItem() == "Sientific Name") {
				try {
					
					filteredList2 = Product.getProductData(Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer"
							+ " from  drug d,product p,Name_manu m"
							+ " where P.product_name=m.product_name and D.Product_ID=P.Product_ID and D.Drug_Scientific_Name like ? ;"
			                  ,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
					
					filteredList = Drug.getDrugData( Queries.queryResult("select P.Product_ID, P.Product_Name,P.Product_Price,m.Product_Manufactrer,D.Drug_Scientific_Name,"
							+ "D.Drug_Risk_Pregnency_Category,D.Drug_Dosage,D.Drug_Category,D.Drug_Dosage_Form,D.Drug_Pharmacetical_Category"
							+ " from Drug D,Product P,Name_manu m "
							+ "where D.Product_ID=P.Product_ID and P.product_name=m.product_name and D.Drug_Scientific_Name like ? ;"
							,new ArrayList<>(Arrays.asList("%" + newValue + "%"))));
					
				} catch (ClassNotFoundException | SQLException | ParseException e) {
					e.printStackTrace();
				}

			}

			Table2.setItems(FXCollections.observableArrayList(filteredList2));
			Table.setItems(FXCollections.observableArrayList(filteredList));
		});

	}

}
