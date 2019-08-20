package pro100.group10.sproutspender.views;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Budget;
import pro100.group10.sproutspender.models.Database;

public class Graphs {
	
	private Stage graphs = new Stage();

	public void init() {
		
		graphs.setTitle("Progress");
		graphs.setResizable(false);
		
		GridPane grid = new GridPane();
		GridPane upperGra = new GridPane();
		
		ArrayList<String> categories = new ArrayList<String>();
		Manager man = HomeController.manager;
		Database db = man.db;
		man.update(db);
		float values[] = new float[5];
		
		Budget[] budg = man.getBudgets();
		float current = 0;
		float limit = 0;
		float remainder = 0;
		int i = 0;
		for(Budget b : budg) {
			if(b != null) {
				categories.add(b.getCategory().toString());
				values[i] = b.getCurrentAmount();
				limit += b.getLimit();
				current += b.getCurrentAmount();
				i++;
			}
		}
			remainder = limit - current;
			
			values[i] = remainder;
		
		
		PieChart.Data pieData[] = new PieChart.Data[categories.size() + 1];
		for (i = 0; i <= categories.size(); i++) {
			if(i == categories.size()) {
				pieData[i] = new PieChart.Data("AVAILABLE", values[i]);
			} else {
				pieData[i] = new PieChart.Data(categories.get(i), values[i]);
			}
			
		}
		PieChart chart = new PieChart(FXCollections.observableArrayList(pieData));
		chart.setLabelsVisible(false);
		chart.setLegendSide(Side.BOTTOM);
		
		Label caption = new Label("");
		caption.setTextFill(Color.DARKORANGE);
		caption.setStyle("-fx-font: 24 arial;");

		grid.add(chart, 0, 0);
		
		Scene scene = new Scene(grid, 800, 500);
		graphs.setResizable(false);
		graphs.setScene(scene);
		graphs.show();
	}
	
//	private void createBigGraph(String[][] data) {
//		String categories[] = { "Groceries", "Idk", "AnotherCategoryUwU", "anotha one" };
//		double values[] = { 30.5, 6, 10, 3.55 };
//		PieChart.Data pieData[] = new PieChart.Data[categories.length];
//		for (int i = 0; i < categories.length; i++) {
//			pieData[i] = new PieChart.Data(categories[i], values[i]);
//		}
//		PieChart chart = new PieChart(FXCollections.observableArrayList(pieData));
//
//		// Make the labels (on lines) not visible
//		chart.setLabelsVisible(false);
//
//		// Change legend location
//		chart.setLegendSide(Side.TOP);
//
//		Label caption = new Label("");
//		caption.setTextFill(Color.DARKORANGE);
//		caption.setStyle("-fx-font: 24 arial;");
//
//		root.getChildren().addAll(chart, caption);
//
//		for (final PieChart.Data d : pieData) {
//			d.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
//				@Override
//				public void handle(MouseEvent e) {
//					caption.setTranslateX(e.getSceneX());
//					caption.setTranslateY(e.getSceneY());
//					caption.setText(String.valueOf(d.getPieValue()));
//				}
//			});
//		}
//	}
}
