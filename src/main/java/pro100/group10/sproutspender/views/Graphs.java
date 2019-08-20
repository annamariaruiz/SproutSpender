package pro100.group10.sproutspender.views;

import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pro100.group10.sproutspender.controllers.HomeController;
import pro100.group10.sproutspender.controllers.Manager;
import pro100.group10.sproutspender.models.Budget;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.geometry.Side;

public class Graphs {
	
	private Stage graphs = new Stage();

	public void init() {
		graphs.setTitle("Progress");
		graphs.setResizable(false);
		
		GridPane grid = new GridPane();
		GridPane upperGra = new GridPane();
		
		String categories[] = { "Food", "Transportation", "Entertainment", "Miscellaneous", "Remainder" };
		Manager man = HomeController.manager;
		float values[] = new float[5];
		
		Budget[] budg = man.getBudgets();
		float current = 0;
		float limit = 0;
		int i = 0;
		for(Budget b : budg) {
			values[i] = b.getCurrentAmount();
			limit += b.getLimit();
			current += b.getCurrentAmount();
			i++;
		}
		float remainder = limit - current;
		values[4] = current;
		
		PieChart.Data pieData[] = new PieChart.Data[categories.length];
		for (i = 0; i < categories.length; i++) {
			pieData[i] = new PieChart.Data(categories[i], values[i]);
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
