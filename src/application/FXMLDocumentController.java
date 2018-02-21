package application;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class FXMLDocumentController implements Initializable {

	@FXML
	private AnchorPane feedFPane;
	@FXML
	private Button teachNetwork;
//	@FXML
//	private TextField inKaraty;
//	@FXML
//	private TextField inX;
//	@FXML
//	private TextField inY;
//	@FXML
//	private TextField inZ;
	@FXML
	private Button feedForward;
	@FXML
	private Label feedFResult;

	@FXML
	private AnchorPane selectFilePane;
	@FXML
	private Button selectFile;
	@FXML
	private Label selectedFileName;

	@FXML
	private AnchorPane loadDataPane;
	@FXML
	private Label inputDataStatus;
	@FXML
	private Label learningTime;
	@FXML
	private Label learningPercent;

	@FXML
	private AnchorPane pane;

	@FXML
	private Button showChartsButton;
	@FXML
	private ScrollPane chartsPane;

	@FXML
	private ScrollPane tablePane;
	@FXML
	private TableView<NumberColumnItem> dataTable;

	private ANNController annController;
	private ColumnController columnController;

	String dataFileName;
	SourceData sourceData;

	@FXML
	private void selectFile(ActionEvent event) {
		dataFileName = "data\\diamonds.csv";
		sourceData = new SourceData(dataFileName);

		columnController = new ColumnController(dataTable, sourceData.getColumns());
		columnController.setPredictColumnIndex(3);
		selectedFileName.setText(dataFileName);
		loadDataPane.setDisable(false);
		;

		showChartsButton.setDisable(false);
	}

	@FXML
	private void showCharts(ActionEvent event) {
		//
		// ScrollPane scrollPane = new ScrollPane();
		// scrollPane.setPrefSize(600, 600);
		// chartsPane.getChildren().add(scrollPane);
		VBox vBox = new VBox();

		for (int i = 0; i < sourceData.numberOfColumns(); i++) {
			ScatterChart<Number, Number> sc = chart(sourceData, i, columnController.getPredictColumnIndex());
			sc.setLayoutY(i * 400);
			sc.setPrefSize(200, 200);
			vBox.getChildren().add(sc);
		}
		chartsPane.setContent(vBox);
	}

	private ScatterChart<Number, Number> chart(SourceData sourceData, int columnIndex, int predictColumnIndex) {
		final int maxNumberOfPoints = 100;

		ColumnStatstics statPredCol = sourceData.getColumn(predictColumnIndex).getStatistics();
		ColumnStatstics statValue = sourceData.getColumn(columnIndex).getStatistics();

		int yMin = (int) Math.floor(statPredCol.getMin());
		int yMax = (int) Math.ceil(statPredCol.getMax());
		int xMin = (int) Math.floor(statValue.getMin());
		int xMax = (int) Math.ceil(statValue.getMax());

		int xUnit = (int) Math.ceil((xMax - xMin) / 10f);
		int yUnit = (int) Math.ceil((yMax - yMin) / 10f);

		String xLabel = sourceData.getColumnName(columnIndex);
		String yLabel = sourceData.getColumnName(predictColumnIndex);

		NumberAxis yAxis = new NumberAxis(yLabel, yMin, yMax, yUnit);
		NumberAxis xAxis = new NumberAxis(xLabel, xMin, xMax, xUnit);
		ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
		// sc.setTitle("Wykres");

		XYChart.Series<Number, Number> seria = new Series<>();
		seria.setName(xLabel);

		Random r = new Random();
		int x;
		int n;
		n = Math.min(sourceData.numberOfRecords(), maxNumberOfPoints);
		for (int i = 0; i < n; i++) {
			x = r.nextInt(sourceData.numberOfRecords());
			seria.getData().add(new XYChart.Data<Number, Number>(sourceData.getValue(columnIndex, x),
					sourceData.getValue(predictColumnIndex, x)));
		}

		sc.getData().add(seria);

		return sc;
	}

	@FXML
	private void hadnleButtonTeachNetwork(ActionEvent event) {
		annController = new ANNController(sourceData.getData(columnController));

		System.out.println("Dane: ");
		for (float f : sourceData.getData(columnController)[0])
			System.out.print(f + " | ");
		System.out.println();

		try {
			inputDataStatus.setText("Uczenie...");

			annController.uczODiamentach((percentage) -> {
				Platform.runLater(() -> {
					learningPercent.setText((int) (percentage * 10000) / 100f + "%");
				});
			}, (time) -> {
				Platform.runLater(() -> {
					updateFeedForwadPane();
					feedFPane.setDisable(false);
					inputDataStatus.setText("Nauczona");
					learningTime.setText("Time: " + (time / 1000000 / (float) 1000) + " s");
				});
			});
		} catch (Exception e) {
			inputDataStatus.setText("Nie nauczona");
			feedFPane.setDisable(true);
		}
	}

	private void updateFeedForwadPane() {
		List<NumberColumn> column = columnController.selectedColumn();
		NumberColumn predictColumn = columnController.getPredictColumn();

		int n = column.size()-1;
		Label[] labels = new Label[n];
		TextField[] textFields = new TextField[n];
//		int maxLabelWidth = 0;
		int secondColumnX = 50;
		
		feedFPane.getChildren().clear();

		for(int i=0; i<n; i++)
			if(column.get(i) != predictColumn) {
				labels[i] = new Label();
				textFields[i] = new TextField();

				labels[i].setText(column.get(i).getName());

				labels[i].setLayoutX(5);
				labels[i].setLayoutY(30*i + 10);
				textFields[i].setLayoutX(secondColumnX);
				textFields[i].setLayoutY(30*i +5);
				
				textFields[i].setMaxWidth(30);

				feedFPane.getChildren().add(labels[i]);
				feedFPane.getChildren().add(textFields[i]);
				
//				if (labels[i].getWidth() >= maxLabelWidth) maxLabelWidth = (int) labels[i].getWidth();
		}
//		secondColumnX = 5 + maxLabelWidth + 10;
		
//		for(int i=0; i<n; i++)
//			textFields[i].setLayoutX(secondColumnX);
		
			
		
		feedForward.setLayoutY(30*(n) + 10);
		feedFResult.setLayoutY(30*(n) + 10);
		feedFResult.setLayoutX(secondColumnX);
		feedFPane.getChildren().add(feedForward);
		feedFPane.getChildren().add(feedFResult);

		feedForward.setOnAction((event)->
		{
			float[] input = new float[n];
			for(int i=0; i<n; i++)
				try {
					input[i] = Float.parseFloat(textFields[i].getText());
					textFields[i].setStyle("-fx-background-color: white");
				} catch(NumberFormatException e) {
					input[i] = 0;
					textFields[i].setStyle("-fx-background-color: red");
				}
			feedFResult.setText(predictColumn.getName() + ": " + annController.ANNResult(input));
		});
	}

//	@FXML
//	private void hadnleButtonFeedForward(ActionEvent event) {
//		float[] input = new float[4];
//		try {
//			input[0] = Float.parseFloat(inKaraty.getText());
//			input[1] = Float.parseFloat(inX.getText());
//			input[2] = Float.parseFloat(inY.getText());
//			input[3] = Float.parseFloat(inZ.getText());
//			feedFResult.setText("Result: " + annController.ANNResult(input));
//		} catch (NumberFormatException e) {
//			// TODO
//		}
//	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}
