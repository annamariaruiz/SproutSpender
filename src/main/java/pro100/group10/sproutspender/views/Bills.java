package pro100.group10.sproutspender.views;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Bill;
import pro100.group10.sproutspender.models.Bill.TimeFrame;
import pro100.group10.sproutspender.models.Database;

public class Bills {
	
	private static Stage window = new Stage();
	private static Scene primScene;
	private TableView<Bill> tableView = new TableView<>();
	public static Database db;
	private Manager man = HomeController.manager;
	private static ObservableList<Bill> listedBills = FXCollections.observableArrayList();
	private ObservableList<TimeFrame> enums = FXCollections.observableArrayList(Bill.TimeFrame.values());
	private static ObservableList<Bill> currentSelected;
	private static boolean edit = false;
	
	@FXML
	private TextField nameOfBill;
	@FXML
	private TextField amount;
	@FXML
	private DatePicker nextDate;
	@FXML
	private CheckBox paid;
	@FXML
	private Button saveNewBill;
	@FXML
	private Button saveEditBill;
	@FXML
	private Label error;
	@FXML
    private ComboBox<Bill.TimeFrame> timetype;
	
	public void initialize() {
		timetype.getItems().removeAll(timetype.getItems());
	    timetype.getItems().addAll(enums);
	    timetype.getSelectionModel().select(enums.get(0));
	    
	    if(edit) {
	    	for(Bill bill : currentSelected) {
	    		nameOfBill.setText(bill.getName());
	    		amount.setText(bill.getAmount() + "");
	    		nextDate.setValue(bill.getDate().toLocalDate());
	    		paid.setSelected(bill.isPaid());
	    		timetype.setValue(bill.getTimeFrame());	
	    	}	    	
	    }
	}
	
	public void init() {
		window.setTitle("View Your Bills");
		window.setResizable(false);
		
		TableColumn<Bill, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.setMinWidth(250);
		name.setResizable(false);
		name.setSortable(false);
		
		TableColumn<Bill, Float> amount = new TableColumn<>("Amount");
		amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		amount.setMinWidth(75);
		amount.setResizable(false);
		amount.setSortable(false);
		
		TableColumn<Bill, Date> date = new TableColumn<>("Date");
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		date.setMinWidth(100);
		date.setResizable(false);
		date.setSortable(false);
		
		TableColumn<Bill, TimeFrame> timeFrame = new TableColumn<>("Time Frame");
		timeFrame.setCellValueFactory(new PropertyValueFactory<>("timeFrame"));
		timeFrame.setMinWidth(100);
		timeFrame.setResizable(false);
		timeFrame.setSortable(false);
		
		TableColumn<Bill, Boolean> paid = new TableColumn<>("Paid");
		paid.setCellValueFactory(new PropertyValueFactory<>("paid"));
		paid.setMinWidth(25);
		paid.setResizable(false);
		paid.setSortable(false);
		
		tableView.getColumns().addAll(name, amount, date, timeFrame, paid);
		tableView.setItems(getBills());
		tableView.setEditable(true);
		tableView.setMinHeight(50);
		
		//add and delete
		Button add = new Button("Add");
		add.setOnAction(e -> {
			addBill();
		});
		
		Button delete = new Button("Delete");
		delete.setOnAction(e -> {
			deleteBill();
		});
		
		Button edit = new Button("Edit");
		edit.setOnAction(e -> {
			editBill();
		});
		
		HBox buttons = new HBox();
		buttons.setPadding(new Insets(10, 20, 10, 20));
		buttons.setSpacing(10);
		buttons.getChildren().addAll(add, delete, edit);
		
		VBox vertical = new VBox();
		vertical.getChildren().addAll(tableView, buttons);
		
		primScene = new Scene(vertical, 650, 400);

		primScene.getStylesheets().add(getClass().getResource("../views/bills.css").toString());
		window.setScene(primScene);
		window.show();
	}
	
	private void editBill() {	
		edit = true;
	    currentSelected = tableView.getSelectionModel().getSelectedItems();
		window.setTitle("Edit Bill");
		window.setResizable(false);
		try {
			GridPane root = (GridPane)FXMLLoader.load(getClass().getResource("../views/EditBill.fxml"));
			Scene scene = new Scene(root,500,375);
			scene.getStylesheets().add(getClass().getResource("../views/settings.css").toString());
			window.setScene(scene);
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void saveEditBill() throws SQLException {
		if (!nameOfBill.getText().isEmpty() && !amount.getText().isEmpty() && nextDate.getValue() != null && timetype.getValue() != null) {
			
	    	for(Bill bill : currentSelected) {
				String newAmount = amount.getText();
				newAmount = newAmount.replace("$", "");
				newAmount = newAmount.replace(",", "");
				float floatAmount = Float.valueOf(newAmount.trim()).floatValue();
				Date date = java.sql.Date.valueOf(nextDate.getValue());
				
				bill.setName(nameOfBill.getText());
				bill.setAmount(floatAmount);
				bill.setDate((java.sql.Date) date);
				bill.setTimeFrame(timetype.getValue());
				bill.setPaid(paid.isSelected());
				db.updateBill(bill);
	    	}	
			
			tableView.setItems(getBills());
			window.setScene(primScene);
			window.show();
		} else {
			error.setText("Values must not be empty");
		}
		man.nextCycleBi();
	}

	private void deleteBill(){
		ObservableList<Bill> billSelected, allBills;
		allBills = tableView.getItems();
		billSelected = tableView.getSelectionModel().getSelectedItems();
		
		for(Bill bill : billSelected) {
			bill.getId();
			try {
				db.removeBill(bill.getId());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			man.update(db);
		}

		tableView.setItems(getBills());
		billSelected.forEach(allBills::remove);
	}

	private void addBill() {
		edit = false;
		window.setTitle("Add Bill");
		window.setResizable(false);
		try {
			GridPane root = (GridPane)FXMLLoader.load(getClass().getResource("../views/AddBill.fxml"));
			Scene scene = new Scene(root,500,375);
			scene.getStylesheets().add(getClass().getResource("../views/settings.css").toString());
			window.setScene(scene);
			window.show();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@FXML
	private void saveNewBill() throws SQLException {
		//check if fields are empty
		if (!nameOfBill.getText().isEmpty() && !amount.getText().isEmpty() && nextDate.getValue() != null && timetype.getValue() != null) {			
			//store values
			String newName = nameOfBill.getText().trim();
			String newAmount = amount.getText();
			newAmount = newAmount.replace("$", "");
			newAmount = newAmount.replace(",", "");
			float floatAmount = Float.valueOf(newAmount.trim()).floatValue();
			Date date = java.sql.Date.valueOf(nextDate.getValue());
			Bill.TimeFrame type = timetype.getValue();
			
			Bill nb = new Bill(floatAmount, (java.sql.Date) date, newName, type, false);
			
			//save to database
			db.createBi(nb);
			tableView.setItems(getBills());
			man.update(db);
			window.setScene(primScene);
			window.show();
		} else {
			error.setText("Values must not be empty");
		}
		
	}

	private ObservableList<Bill> getBills() {
		listedBills.clear();
		HashMap<String, Bill> temp = db.selectAllBills();
		
		for (String bill : temp.keySet()) {
			listedBills.add(temp.get(bill));
		}
		
		return listedBills;
	}
}
