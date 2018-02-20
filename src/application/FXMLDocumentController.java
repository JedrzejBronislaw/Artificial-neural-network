package application;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.text.TabableView;

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
import javafx.scene.layout.VBox;

public class FXMLDocumentController implements Initializable{


    @FXML private AnchorPane feedFPane;
    @FXML private Button inputData;
    @FXML private TextField inKaraty;
    @FXML private TextField inX;
    @FXML private TextField inY;
    @FXML private TextField inZ;
    @FXML private Button feedForward;
    @FXML private Label feedFResult;

    @FXML private AnchorPane selectFilePane;
    @FXML private Button selectFile;
    @FXML private Label selectedFileName;

    @FXML private AnchorPane loadDataPane;
    @FXML private Label inputDataStatus;
    @FXML private Label learningTime;
    @FXML private Label learningPercent;

    @FXML private AnchorPane pane;

    @FXML private Button showChartsButton;
    @FXML private AnchorPane chartsPane;

    @FXML private AnchorPane tablePane;
    @FXML private TableView<NumberColumnItem> dataTable;


    private ANNController annController;
    private RawDataController rawDataController;

    String dataFileName;
	SourceData rd;

    @FXML
    private void selectFile(ActionEvent event){
    	dataFileName = "data\\diamonds.csv";
    	rd = new SourceData(dataFileName);

    	annController = new ANNController(dataFileName);
    	rawDataController = new RawDataController(dataTable);
//    	rawDataController = new RawDataController(pane);
    	selectedFileName.setText(dataFileName);
    	loadDataPane.setDisable(false);

    	rawDataController.loadCSVData(dataFileName);
//		rawDataController.setColumnsNames(rd.getNamesOfColumns());
    	rawDataController.fill(rd.getColumns());

		showChartsButton.setDisable(false);
    }

    @FXML
    private void showCharts(ActionEvent event){

    	int n = rd.numberOfColumns();
//    	for(int i=0; i<n; i++){
//    		System.out.println(rd.getColumnName(i) + ": " + rd.getColumnType(i));
//    		System.out.println(rd.getColumnStatistics(i));
//    	}

    	rd.selectPredictColumn(3);

    	ScrollPane scrollPane = new ScrollPane();
    	scrollPane.setPrefSize(600, 600);
    	chartsPane.getChildren().add(scrollPane);
    	VBox vBox = new VBox();

		for(int i=0;i<rd.numberOfColumns();i++){
			ScatterChart<Number, Number> sc = chart(rd,i);
			sc.setLayoutY(i*400);
			vBox.getChildren().add(sc);
		}
		scrollPane.setContent(vBox);
    }

    private ScatterChart<Number, Number> chart(SourceData rd, int columnIndex) {
    	final int maxNumberOfPoints = 100;

    	ColumnStatstics statPredCol = rd.getColumn(rd.getPredictionColumnIndex()).getStatistics();
    	ColumnStatstics statValue = rd.getColumn(columnIndex).getStatistics();

    	int yMin = (int) Math.floor(statPredCol.getMin());
    	int yMax = (int) Math.ceil(statPredCol.getMax());
    	int xMin = (int) Math.floor(statValue.getMin());
    	int xMax = (int) Math.ceil(statValue.getMax());

    	int xUnit = (int) Math.ceil((xMax-xMin)/10f);
    	int yUnit = (int) Math.ceil((yMax-yMin)/10f);

    	String xLabel = rd.getColumnName(columnIndex);
    	String yLabel = rd.getColumnName(rd.getPredictionColumnIndex());

    	NumberAxis yAxis = new NumberAxis(yLabel, yMin, yMax, yUnit);
    	NumberAxis xAxis = new NumberAxis(xLabel, xMin, xMax, xUnit);
		ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
//		sc.setTitle("Wykres");

		XYChart.Series<Number, Number> seria = new Series<>();
		seria.setName(xLabel);

		Random r = new Random();
		int x;
		int n;
		n = Math.min(rd.numberOfRecords(), maxNumberOfPoints);
		for (int i=0; i<n;i++){
			x = r.nextInt(rd.numberOfRecords());
				seria.getData().add(new XYChart.Data<Number, Number>(rd.getValue(columnIndex, x), rd.getValue(rd.getPredictionColumnIndex(), x)));
		}

		sc.getData().add(seria);

		return sc;
	}

	@FXML
    private void hadnleButtonInputData(ActionEvent event){
    		try{
	    		inputDataStatus.setText("Uczenie...");


	    		annController.uczODiamentach((percentage)->{
	    			Platform.runLater(()->{
	    				learningPercent.setText(percentage*100+"%");
	    			});
	    		}, (time)->{
	    			Platform.runLater(()->{
				   		feedFPane.setDisable(false);
				   		inputDataStatus.setText("Nauczona");
				   		learningTime.setText("Time: " + (time/1000000/(float)1000) + " s");
		    		});
	    		});
    		} catch (Exception e) {
    			inputDataStatus.setText("Nie nauczona");
    			feedFPane.setDisable(true);
    		}
    }

    @FXML
    private void hadnleButtonFeedForward(ActionEvent event){
    		float[] input = new float[4];
    		try{
	    		input[0] = Float.parseFloat(inKaraty.getText());
	    		input[1] = Float.parseFloat(inX.getText());
	    		input[2] = Float.parseFloat(inY.getText());
	    		input[3] = Float.parseFloat(inZ.getText());
	    		feedFResult.setText("Result: " + annController.ANNResult(input));
    		} catch (NumberFormatException e) {
    			//TODO
    		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}





}
