package pro100.group10.sproutspender.controllers;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pro100.group10.sproutspender.app.Main;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.views.Table;

public class HomeController {
	
	public static Manager manager;
	
	@FXML
	private Button start;
	@FXML
	private TextField dbName;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private Label alert;
	
	@FXML
	public void init() {
		Manager m = new Manager(null, null);
		boolean valid = m.isValid(dbName.getText().trim());
		boolean empty = dbName.getText().trim().isEmpty();
		
		Stage stage = Main.getStage();
		
		//String username, String password, String server, int port, String dbName
		Database db = null;
		try {
			db = new Database(username.getText().trim(), password.getText().trim(), "localhost", 1433, dbName.getText().trim());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(valid && !empty) {
			alert.setText("");
			
			//create database
			try {
				db.setConnection(username.getText().trim(), password.getText().trim(), dbName.getText().trim());
				db.canConnect();
				manager = new Manager(db, dbName.getText().trim());
				//call to open table
				try {
					FXMLLoader tableLoader = new FXMLLoader();
					tableLoader.setLocation(getClass().getResource("../views/TableStage.fxml"));
					Table table = new Table();
					table.setDB(db);
					tableLoader.setController(table);
					Parent root = tableLoader.load();
					Scene scene = new Scene(root, 750, 450);
					stage.setScene(scene);
					stage.setOnHidden(eh -> table.cleanUp());
					stage.show();
				} catch(Exception e) {
					e.printStackTrace();
				}	
			} catch (RuntimeException e) {
				alert.setText("Login failed");
			}
			
		} else if (valid && empty) {
			alert.setText("Database name must not be empty");
		} else if (!valid){
			alert.setText("Database name format incorrect");
		} else {
			System.out.println("Idk, something broke");
		}
		
	}
}
