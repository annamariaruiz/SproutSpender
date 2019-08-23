package pro100.group10.sproutspender.controllers;

import java.sql.SQLException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pro100.group10.sproutspender.app.Main;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.views.Credentials;
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
	private Label createNewUser;

	public void initialize() {
		
		createNewUser.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				createNewUser.setTextFill(Color.web("#158032"));
			}
		});

		createNewUser.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				createNewUser.setTextFill(Color.web("#000"));
			}
		});
	}

	@FXML
	public void init() {
		Manager m = new Manager(null, null);
		boolean valid = m.isValid(dbName.getText().trim());
		boolean empty = dbName.getText().trim().isEmpty();

		Stage stage = Main.getStage();

		// String username, String password, String server, int port, String dbName
		Database db = null;
		try {
			db = new Database(username.getText().trim(), password.getText().trim(), "localhost", 1433,
					dbName.getText().trim());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (valid && !empty) {
			alert.setText("");

			// create database
			try {
				db.setConnection(username.getText().trim(), password.getText().trim(), dbName.getText().trim());
				db.canConnect();
				manager = new Manager(db, dbName.getText().trim());
				manager.update(db);
				// call to open table
			} catch (RuntimeException e) {
				alert.setText("Login failed");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		} else if (valid && empty) {
			alert.setText("Database name must not be empty");
		} else if (!valid) {
			alert.setText("Database name format incorrect");
		}
		try {
			FXMLLoader tableLoader = new FXMLLoader();
			tableLoader.setLocation(getClass().getResource("../views/TableStage.fxml"));
			Table table = new Table();
			table.setDB(db);
			tableLoader.setController(table);
			Parent root = tableLoader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setOnHidden(eh -> table.cleanUp());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openCredentials() {
		Credentials c = new Credentials();
		c.init();
	}
}
