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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pro100.group10.sproutspender.app.Main;
import pro100.group10.sproutspender.models.Database;
import pro100.group10.sproutspender.models.User;
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
		password.setOnKeyPressed(key -> {
			if(key.getCode().equals(KeyCode.ENTER)) {
				init();
			}
		});
	}

	@FXML
	public void init() {
		Manager m = new Manager();
		boolean valid = m.isValid(dbName.getText().trim());
		boolean empty = dbName.getText().trim().isEmpty();

		Stage stage = Main.getStage();

		// String username, String password, String server, int port, String dbName   password.getText().trim()
		Database db = null;
		try {
			db = new Database("admin", "admin" , "localhost", 1433,
					"SproutSpenderDB");
			db.setConnection("admin", "admin", "SproutSpenderDB");
			db.canConnect();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if (valid && !empty) {
			alert.setText("");

			// create database
			try {
				User u = db.login(username.getText().trim(), password.getText().trim());
				Manager mana = new Manager(db, dbName.getText().trim(), u);
				manager.db.setConnection(db.getConnection());
				mana.db.setConnection(db.getConnection());
				mana.setName(dbName.getText().trim());
				if(u != null ) {
					mana.update(db);
					mana.setUserID(u.getId());
					mana.setID(db.importManager(dbName.getText().trim(), u).getID());
				}
				if(mana.getID() == 0) {
					db.createManager(mana);
					mana.setID(db.importManager(dbName.getText().trim(), u).getID());
					manager = mana;
				}
				// call to open table
			} catch (RuntimeException | SQLException e) {
				e.printStackTrace();
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
			scene.getStylesheets().add(getClass().getResource("../views/table.css").toString());
			stage.setScene(scene);
			stage.setOnHidden(eh -> table.cleanUp());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openCredentials() {
		Database db = null;
		try {
			db = new Database("admin", "admin" , "localhost", 1433,
					"master");
			db.setConnection("admin", "admin", "master");
			db.canConnect();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Credentials c = new Credentials();
		c.init(db);
	}
}
