package pro100.group10.sproutspender.views;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pro100.group10.sproutspender.models.Budget;

public class Table {

	@FXML
	private TableView<Budget> tableView;
	@FXML
	private TableColumn<Budget, Integer> day1Col;
	@FXML
	private TableColumn<Budget, Integer> day2Col;
	@FXML
	private TableColumn<Budget, Integer> day3Col;
	@FXML
	private TableColumn<Budget, Integer> day4Col;
	@FXML
	private TableColumn<Budget, Integer> day5Col;
	@FXML
	private TableColumn<Budget, Integer> day6Col;
	@FXML
	private TableColumn<Budget, Integer> day7Col;
	
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
	
	private boolean tableIsEditable;
	
	
	private void makeTablePopulatable() {
		
	}
	
	private void linkTableToBackend() {
		
	}
	
	private void onMenuItemEditMode() {
		
	}
	
	private void onLastButtonClick(ActionEvent ae) {
		
	}
	
	private void onNextButtonClick(ActionEvent ae) {
		
	}
	
	private ObservableList<Budget> parseLastHundredContacts() {
		return null;
		
	}
	
	private ObservableList<Budget> parseNextHundredContacts() {
		return null;
		
	}
}
