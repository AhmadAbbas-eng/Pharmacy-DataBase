package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.util.StringConverter;
import javafx.scene.control.TableView;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Relations.*;

/**
 * 
 * @version 27 January 2022
 * @author Aseel Sabri
 */
public class BatchController implements Initializable {

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

	String productID;

	public void setProductID(String productID) {
		this.productID = productID;
		title.setText("Batch For Product " + productID);
		try {
			filterData();
			ArrayList<String> parameters = new ArrayList<>();
			parameters.add(productID);
			ArrayList<ArrayList<String>> totalAmount = Queries.queryResult(
					"select sum(Batch_Amount) from Batch where Product_ID=? group by(Product_ID) ;", parameters);

			if (totalAmount.size() > 0 && totalAmount.get(0).size() > 0) {
				totalQuantityLable.setText(totalAmount.get(0).get(0));
			} else {
				totalQuantityLable.setText("0");
			}
		} catch (ClassNotFoundException | SQLException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Filtering the data to show depending on search text field
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void filterData() throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<String> parameters = new ArrayList<>();
		parameters.add(productID);
		if (datePicker.getValue() == null || datePicker.getValue().toString().isBlank()
				|| datePicker.getValue().toString().isEmpty()) {
			batchTable
					.setItems(FXCollections.observableArrayList(Batch.getBatchData(Queries.queryResult(
							"select * from Batch where Product_ID=? and Batch_Amount>0"
									+ " and Batch_Production_Date<>'1111-01-01' order by Batch_Expiry_Date;",
							parameters))));

		} else {
			String date = datePicker.getValue().toString();
			if (dateSelector.getSelectionModel().getSelectedItem().equals("Expires On")) {
				parameters.add(date);
				batchTable.setItems(FXCollections.observableArrayList(Batch.getBatchData(Queries.queryResult(
						"select * from Batch where Product_ID=? and Batch_Production_Date<>'1111-01-01' "
								+ " and Batch_Expiry_Date=? and Batch_Amount>0 order by Batch_Expiry_Date;",
						parameters))));

			} else if (dateSelector.getSelectionModel().getSelectedItem().equals("Expires Before")) {
				parameters.add(date);
				batchTable.setItems(FXCollections.observableArrayList(Batch.getBatchData(Queries.queryResult(
						"select * from Batch where Product_ID=? and Batch_Production_Date<>'1111-01-01' "
								+ " and Batch_Expiry_Date<? and Batch_Amount>0 order by Batch_Expiry_Date;",
						parameters))));

			} else { // Expires After
				parameters.add(date);
				batchTable.setItems(FXCollections.observableArrayList(Batch.getBatchData(Queries.queryResult(
						"select * from Batch where Product_ID=? and Batch_Production_Date<>'1111-01-01' "
								+ " and Batch_Expiry_Date>? and Batch_Amount>0 order by Batch_Expiry_Date;",
						parameters))));

			}
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

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