package pro100.group10.sproutspender.views;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.models.FloatEditingCell;
import pro100.group10.sproutspender.models.WeeklyPlanner;
import pro100.group10.sproutspender.models.Budget.CategoryType;

public class Table {

	@FXML
	private TableView<WeeklyPlanner> tableView;
	@FXML
	private TableColumn<WeeklyPlanner, Float> day1Col;
	@FXML
	private TableColumn<WeeklyPlanner, Float> day2Col;
	@FXML
	private TableColumn<WeeklyPlanner, Float> day3Col;
	@FXML
	private TableColumn<WeeklyPlanner, Float> day4Col;
	@FXML
	private TableColumn<WeeklyPlanner, Float> day5Col;
	@FXML
	private TableColumn<WeeklyPlanner, Float> day6Col;
	@FXML
	private TableColumn<WeeklyPlanner, Float> day7Col;
	
	@FXML
	private TextField makeNewDay1;
	@FXML
	private TextField makeNewDay2;
	@FXML
	private TextField makeNewDay3;
	@FXML
	private TextField makeNewDay4;
	@FXML
	private TextField makeNewDay5;
	@FXML
	private TextField makeNewDay6;
	@FXML
	private TextField makeNewDay7;
	
	@FXML
	private Button settings;
	@FXML
	private Button bills;
	@FXML
	private Button graphs;
	
	private boolean hasInitialized = false;
	private int lastIDViewed = 0;
	private boolean tableIsEditable = false;
	private Date endDate = Date.valueOf(LocalDate.now());
	
	private Database db;
	
	@FXML
	private void initialize() {
		if(!hasInitialized) {
			hasInitialized = true;
//			db = new Database();
//			db.setConnection("sproutspender", "sproutspender");
//			db.canConnect();
			tableView.setEditable(tableIsEditable);
			
			@SuppressWarnings("unchecked")
			TableColumn<WeeklyPlanner, Float>[] columns = (TableColumn<WeeklyPlanner, Float>[]) new TableColumn[7];
			columns[0] = day1Col;
			columns[1] = day2Col;
			columns[2] = day3Col;
			columns[3] = day4Col;
			columns[4] = day5Col;
			columns[5] = day6Col;
			columns[6] = day7Col;
			
			Callback<TableColumn<WeeklyPlanner, Float>, TableCell<WeeklyPlanner, Float>> floatCellFactory =
					new Callback<TableColumn<WeeklyPlanner, Float>, TableCell<WeeklyPlanner, Float>>() {
				public TableCell<WeeklyPlanner, Float> call(TableColumn<WeeklyPlanner, Float> p) {
					return new FloatEditingCell();
				}
			};
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			Date dateToGrab = null;
			
			for(int i = 6; i >= 0; i--) {
				dateToGrab = new Date(calendar.getTime().getTime());
				columns[i].setText(dateToGrab.toString());
				calendar.add(Calendar.DATE, -1);
				
				final int index = i;
				columns[i].setCellValueFactory(cellData -> {
					Method method = null;
					try {
						method = cellData.getValue().getClass().getMethod("getDay" + (index+1));
					} catch (NoSuchMethodException nsme) {
						// TODO write catch block
						nsme.printStackTrace();
					} catch (SecurityException se) {
						// TODO write catch block
						se.printStackTrace();
					}
					
					Float currentAmount = null;
					try {
						Budget b = (((Budget) method.invoke(cellData.getValue())));
						if(b != null) {
							currentAmount = b.getCurrentAmount();							
						}
					} catch (IllegalAccessException iae1) {
						// TODO write catch block
						iae1.printStackTrace();
					} catch (IllegalArgumentException iae2) {
						// TODO write catch block
						iae2.printStackTrace();
					} catch (InvocationTargetException ite) {
						// TODO write catch block
						ite.printStackTrace();
					}
					
					return new SimpleObjectProperty<>(currentAmount);
				});
				columns[i].setCellFactory(floatCellFactory);
				
				columns[i].setOnEditCommit(new EventHandler<CellEditEvent<WeeklyPlanner, Float>>() {
					@Override
					public void handle(CellEditEvent<WeeklyPlanner, Float> t) {
						WeeklyPlanner wp = (WeeklyPlanner) t.getTableView().getItems().get(
								t.getTablePosition().getRow());
						
						Method method = null;
						try {
							method = wp.getClass().getMethod("getDay" + (index+1));
						} catch (NoSuchMethodException nsme) {
							// TODO write catch block
							nsme.printStackTrace();
						} catch (SecurityException se) {
							// TODO write catch block
							se.printStackTrace();
						}
						
						try {
							Budget b = ((Budget) method.invoke(wp));
							if(b != null) {
								b.setCurrentAmount(t.getNewValue());
								db.update(b);
							}
						} catch (IllegalAccessException iae1) {
							// TODO write catch block
							iae1.printStackTrace();
						} catch (IllegalArgumentException iae2) {
							// TODO write catch block
							iae2.printStackTrace();
						} catch (InvocationTargetException ite) {
							// TODO write catch block
							ite.printStackTrace();
						} catch(SQLException sqle) {
							Alert alert = new Alert(AlertType.ERROR, "The budget could not be updated in the M.S. S.Q.L. database.\n" + sqle, ButtonType.CLOSE);
							Optional<ButtonType> response = alert.showAndWait();
						}
						
//						b.getDay1().setCurrentAmount(t.getNewValue());
						
//						try {
//							db.update(b.getDay1());
//						} catch(SQLException sqle) {
//							Alert alert = new Alert(AlertType.ERROR, "The budget with I.D. " + b.getDay1().getID() + " could not be updated in the M.S. S.Q.L. database.\n" + sqle, ButtonType.CLOSE);
//							Optional<ButtonType> response = alert.showAndWait();
//						}
					}
				});
			}
		}
	}
	
	@FXML
	private void onMenuItemExit(ActionEvent ae) {
		
	}
	
	@FXML
	private void onMenuItemEditMode(ActionEvent ae) {
		tableIsEditable = !tableIsEditable;
		tableView.setEditable(tableIsEditable);
	}
	
	@FXML
	private void onLastButtonClick(ActionEvent ae) {
//		if(lastIDViewed > 0) {
//			ObservableList<Budget> lastHundredBudgets = parseLastWeek();
//			
//			if(lastHundredBudgets != null && !lastHundredBudgets.isEmpty()) {
//				tableView.setItems(lastHundredBudgets);
//			}	
//		}
	}
	
	@FXML
	private void onNextButtonClick(ActionEvent ae) {
//		try {
//			if(lastIDViewed < db.size()) {
//				ObservableList<Budget> nextHundredBudgets = parseNextWeek(); 
//			
//				if(nextHundredBudgets != null && !nextHundredBudgets.isEmpty()) {
//					tableView.setItems(nextHundredBudgets);			
//				}
//			}
//		} catch(SQLException sqle) {
//			Alert alert = new Alert(AlertType.ERROR, "The size of the S.Q.L. database could not be determined.\n" + sqle, ButtonType.CLOSE);
//			Optional<ButtonType> response = alert.showAndWait();
//			sqle.printStackTrace();
//		}
		
		// TODO set day and thus column of budget based on date inside budget object.
		WeeklyPlanner wp = new WeeklyPlanner();
		Budget b1 = new Budget(300, CategoryType.GENERAL, Date.valueOf(LocalDate.now()));
		b1.setCurrentAmount(200);
		
		Budget b2 = new Budget(500, CategoryType.GENERAL, Date.valueOf(LocalDate.now()));
		b2.setCurrentAmount(400);
		
		wp.setDay1(b1);
		wp.setDay7(b2);
		tableView.getItems().add(wp);
	}
	
	private ObservableList<Budget> parseThisWeek() {
//		tableView.getColumns().setAll(db.lookUpByDay(new Date(2019, 5, 7)));
		return null;
	}
	
	@FXML
	private void onSettingsButtonClick(ActionEvent ae) {
		Settings s = new Settings();
		s.init();
	}
	
	@FXML
	private void onGraphButtonClick(ActionEvent ae) {
		//open graphs window
		System.out.println("graph");
	}
	
	@FXML
	private void onBillsButtonClick(ActionEvent ae) {
		//open bills window
		System.out.println("bill was here");
	}
	
	private ObservableList<Budget> parseLastWeek() {
		
		
		return null;
	}
	
	private ObservableList<Budget> parseNextWeek() {
		return null;
	}
}
