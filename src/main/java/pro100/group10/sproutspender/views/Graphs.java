package pro100.group10.sproutspender.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class Graphs {
	
	@FXML
	private PieChart pieChart;
	
	private Stage graphs = new Stage();

	public void init() {
		graphs.setTitle("Progress");
		graphs.setResizable(false);
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../views/Graphs.fxml"));

			Scene scene = new Scene(root, 800, 500);
			graphs.setResizable(false);
			graphs.setScene(scene);
			graphs.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
