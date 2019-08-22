package pro100.group10.sproutspender.views;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Budget.CategoryType;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.models.FloatEditingCell;
import pro100.group10.sproutspender.models.WeeklyPlanner;

public class Table {

	private final String BUDGET_POP_OUT_FXML_LOC = "../views/BudgetPopOut.fxml";
	private ObservableList<CategoryType> enums = FXCollections.observableArrayList(
			Budget.CategoryType.FOOD, 
			Budget.CategoryType.ENTERTAINMENT, 
			Budget.CategoryType.MISCELLANEOUS, 
			Budget.CategoryType.TRANSPORTATION);
	
	@FXML
	private TableView<WeeklyPlanner> tableView;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day1Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day2Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day3Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day4Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day5Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day6Col;
	@FXML
	private TableColumn<WeeklyPlanner, Budget> day7Col;
	TableColumn<WeeklyPlanner, Budget>[] columns;
	
	private int selectedID;
	private boolean createMode = true;
	@FXML
	private DatePicker makeNewDate;
	@FXML
	private TextField makeNewLimit;
	@FXML
	private ComboBox<Budget.CategoryType> makeNewCat;
	@FXML
	private TextField makeNewCurrentAmount;
	
	@FXML
	private Button settings;
	@FXML
	private Button bills;
	@FXML
	private Button graphs;
	@FXML
	private DatePicker goToDate;
	
	private boolean hasInitialized = false;
	private boolean tableIsEditable = false;
	private Date endDate = Date.valueOf(LocalDate.now());
	
	private Database db;
	
	public Database getDB() {
		return db;
	}

	public void setDB(Database db) {
		this.db = db;
	}

	@SuppressWarnings({ "unchecked"})
	@FXML
	private void initialize() {
		if(!hasInitialized) {
			hasInitialized = true;
			tableView.setEditable(tableIsEditable);
			
			columns = (TableColumn<WeeklyPlanner, Budget>[]) new TableColumn[7];
			columns[0] = day1Col;
			columns[1] = day2Col;
			columns[2] = day3Col;
			columns[3] = day4Col;
			columns[4] = day5Col;
			columns[5] = day6Col;
			columns[6] = day7Col;
			
			Callback<TableColumn<WeeklyPlanner, Budget>, TableCell<WeeklyPlanner, Budget>> floatCellFactory =
					cb -> new FloatEditingCell();
			
			for(TableColumn tc : columns) {
				tc.setResizable(false);
				tc.setSortable(false);
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			Date dateToGrab = null;
			
			for(int i = 6; i >= 0; i--) {
				dateToGrab = new Date(calendar.getTime().getTime());
				columns[i].setText(new SimpleDateFormat("EEE, MMM d ").format(dateToGrab));
				calendar.add(Calendar.DATE, -1);
				
				final int index = i;
				columns[i].setCellValueFactory(cellData -> {
					Budget budg = cellData.getValue().getDay(index + 1);						
					return new SimpleObjectProperty<Budget>(budg);
				});
				
				columns[i].setCellFactory(floatCellFactory);
				
				columns[i].setOnEditCommit(new EventHandler<CellEditEvent<WeeklyPlanner, Budget>>() {
					@Override
					public void handle(CellEditEvent<WeeklyPlanner, Budget> t) {
						WeeklyPlanner wp = (WeeklyPlanner) t.getTableView().getItems().get(
								t.getTablePosition().getRow());
						
						try {	
							Budget budg = wp.getDay(index + 1);
							if(budg != null) {
								budg.setCurrentAmount(t.getNewValue().getCurrentAmount());
								db.update(budg);
								calculateTotals();
							}
						} catch(SQLException sqle) {
							Alert alert = new Alert(AlertType.ERROR, "The budget could not be updated in the M.S. S.Q.L. database.\n" + sqle, ButtonType.CLOSE);
							Optional<ButtonType> response = alert.showAndWait();
						}
					}
				});
			}
			
			tableView.getSelectionModel().setCellSelectionEnabled(true);
			
			refreshTableView();
			calculateTotals();
		}
	}
	
	@FXML
	public void cleanUp() {
		db.close();
	}

	@FXML
	private void onMenuItemMakeNew(ActionEvent ae) {
		createMode = true;
		openDetailedEditWindow();
	}
	
	@FXML
	private void onMenuItemEditDetails(ActionEvent ae) {
		createMode = false;
		selectedID = tableView.getSelectionModel().getSelectedItem().getDay(
				tableView.getSelectionModel().getSelectedCells().get(0).getColumn() + 1).getID();
		openDetailedEditWindow();
	}
	
	private void openDetailedEditWindow() {
		FXMLLoader budgetPopOutLoader = new FXMLLoader();
		budgetPopOutLoader.setLocation(getClass().getResource(BUDGET_POP_OUT_FXML_LOC));
		GridPane budgetPopOutRoot = null;
		final String MAKE_NEW_BUDGET_TITLE = "Create/Edit Budget";
		budgetPopOutLoader.setController(this);
		

		
		try {
			budgetPopOutRoot = (GridPane) budgetPopOutLoader.load();
			makeNewCat.getItems().removeAll(makeNewCat.getItems());
			makeNewCat.getItems().addAll(enums);
			makeNewCat.getSelectionModel().select(enums.get(0));
		} catch(IOException ioe) {
			//TODO write catch block
		}
		
		Stage budgetPopOutStage = new Stage();
		budgetPopOutStage.setTitle(MAKE_NEW_BUDGET_TITLE);
		if(budgetPopOutRoot != null) budgetPopOutStage.setScene(new Scene(budgetPopOutRoot));
		budgetPopOutStage.show();
	}
	
	@FXML
	private void onMenuItemExit(ActionEvent ae) {
		((Stage) tableView.getScene().getWindow()).close();
	}
	
	@FXML
	private void onMenuItemEditMode(ActionEvent ae) {
		tableIsEditable = !tableIsEditable;
		tableView.setEditable(tableIsEditable);
	}
	
	@FXML
	private void onMenuItemRemove(ActionEvent ae) {
		int day = tableView.getFocusModel().getFocusedCell().getColumn() + 1;
		int row = tableView.getFocusModel().getFocusedCell().getRow();
		int id = tableView.getFocusModel().getFocusedItem().getDay(day).getID();
		WeeklyPlanner wp = tableView.getItems().get(row);
		wp.setDay(day, null);
		tableView.getItems().set(row, wp);
		try {
			db.remove(id);
		} catch (SQLException sqle) {
			// TODO write catch block
		}
		calculateTotals();
	}
	
	@FXML
	private void onLastButtonClick(ActionEvent ae) {
		changeEndDate(-7);
		refreshTableView();
		calculateTotals();
	}
	
	@FXML
	private void onNextButtonClick(ActionEvent ae) {
		changeEndDate(7);
		refreshTableView();
		calculateTotals();
	}
	
	private void refreshTableView() {
		ObservableList<WeeklyPlanner> wpList = FXCollections.observableArrayList();
		for(CategoryType cat : Budget.categoryRank) {
			wpList.add(parseThisWeek(cat));			
		}
		tableView.setItems(wpList);
	}
	
	private void changeEndDate(int days) {
 		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.DATE, days);
		endDate = new Date(calendar.getTime().getTime());
		Date dateToGrab = null;
		
		for(int i = 6; i >= 0; i--) {
			dateToGrab = new Date(calendar.getTime().getTime());
			columns[i].setText(new SimpleDateFormat("EEE, MMM d ").format(dateToGrab));
			calendar.add(Calendar.DATE, -1);
		}
	}
	
	@FXML
	private void changeEndDate() {
		Date goTo = java.sql.Date.valueOf(goToDate.getValue());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(goTo);
		endDate = new Date(calendar.getTime().getTime());
		Date dateToGrab = null;
		
		for(int i = 6; i >= 0; i--) {
			dateToGrab = new Date(calendar.getTime().getTime());
			columns[i].setText(dateToGrab.toString());
			calendar.add(Calendar.DATE, -1);
		}
		refreshTableView();
		calculateTotals();
	}
	
	private WeeklyPlanner parseThisWeek(CategoryType category) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		Date dateToGrab = null;
		WeeklyPlanner thisWeek = new WeeklyPlanner();
		Budget budg = null;
		
		for(int i = 6; i >= 0; i--) {
			dateToGrab = new Date(calendar.getTime().getTime());
			try {
				budg = db.lookUpByDayAndCat(dateToGrab, category);
			} catch(SQLException sqle) {
				//TODO write catch block
			}
			
			if(budg != null) thisWeek.setDay(i + 1, budg);
			calendar.add(Calendar.DATE, -1);
		}
		return thisWeek;
	}
	
	private void calculateTotals() {
		ObservableList<WeeklyPlanner> wpList = tableView.getItems();
		WeeklyPlanner genWP = new WeeklyPlanner();
		for(int i = 0; i < columns.length; i++) {
			genWP.setDay(i + 1, new Budget());
			Budget genBudgForDay = genWP.getDay(i + 1);
			genBudgForDay.setCategory(CategoryType.GENERAL);
			for(int j = 0; j < Budget.categoryRank.length - 1; j++) {
				Budget catBudgForDay = columns[i].getCellData(j);
				if(catBudgForDay != null) {
					genBudgForDay.setCurrentAmount(genBudgForDay.getCurrentAmount() + catBudgForDay.getCurrentAmount());
					genBudgForDay.setLimit(genBudgForDay.getLimit() + catBudgForDay.getLimit());
				}
			}
		}
		
		if(wpList.size() > Budget.categoryRank.length - 1) {
			wpList.remove(Budget.categoryRank.length - 1);
		}
		
		wpList.add(genWP);
	}
	
	@FXML
	private void onSettingsButtonClick(ActionEvent ae) {
		Settings s = new Settings();
		s.init();
	}
	
	@FXML
	private void onGraphButtonClick(ActionEvent ae) {
		//open graphs window
		Graphs g = new Graphs();
		g.init();
	}
	
	@FXML
	private void onBillsButtonClick(ActionEvent ae) {
		Bills b = new Bills();
		Bills.db = db;
		b.init();
	}

	@FXML
	private void onPopOutCancel(ActionEvent ae) {
		((Stage) ((Button) ae.getSource()).getScene()
				.getWindow()).close();
	}
	
	@FXML
	private void onPopOutSubmit(ActionEvent ae) {
		boolean missingReqField = false;
		
		if(makeNewDate.getValue() == null) missingReqField = true;
		if(makeNewLimit.getText().trim().isEmpty()) missingReqField = true;
		if(makeNewCat.getSelectionModel().isEmpty()) missingReqField = true;
		if(makeNewCurrentAmount.getText().trim().isEmpty()) makeNewCurrentAmount.setText("0.0");
		
		if(missingReqField) {
			//TODO write missing required fields alert
		} else {
			try {
				String newLimit = makeNewLimit.getText().trim();
				newLimit = newLimit.replace("$", "");
				newLimit = newLimit.replace(",", "");
				Budget budg = new Budget(
					Float.parseFloat(newLimit),
					makeNewCat.getValue(),
					Date.valueOf(makeNewDate.getValue())
				);
				
				budg.setCurrentAmount(Float.parseFloat(makeNewCurrentAmount.getText().trim()));
				budg.setID(selectedID);
				
				if(createMode) {
					db.store(budg);
				} else {
					db.update(budg);
				}
			} catch(NumberFormatException nfe) {
				//TODO write catch block
			} catch(SQLException sqle) {
				//TODO write catch block
			}
		}
		
		refreshTableView();
		calculateTotals();
		onPopOutCancel(ae);
 	}
}
