package pro100.group10.sproutspender.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Bills {
	
	private Stage bills = new Stage();

	public void init() {
		bills.setTitle("View Your Bills");
		bills.setResizable(false);
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../views/Bills.fxml"));

			Scene scene = new Scene(root, 600, 400);
			bills.setResizable(false);
			bills.setScene(scene);
			bills.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
