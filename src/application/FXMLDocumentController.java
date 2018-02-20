package application;

import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

    private ANNController annController;
    private RawDataController rawDataController;

    @FXML
    private void selectFile(ActionEvent event){
    	String dataFileName = "data\\diamonds.csv";

    	SourceData rd = new SourceData(dataFileName);
    	int n = rd.numberOfColumns();
    	for(int i=0; i<n; i++){
    		System.out.println(rd.getColumnName(i) + ": " + rd.getColumnType(i));
    		System.out.println(rd.getColumnStatistics(i));
    	}

    	annController = new ANNController(dataFileName);
    	rawDataController = new RawDataController(pane);
    	selectedFileName.setText(dataFileName);
    	loadDataPane.setDisable(false);

    	rawDataController.loadCSVData(dataFileName);
		rawDataController.setColumnsNames();

		wykres(rd);
    }

    private void wykres(SourceData rd) {
    	ColumnStatstics statPredCol = rd.getColumn(rd.getPredictionColumnIndex()).getStatistics();
    	ColumnStatstics statValue = rd.getColumn(0).getStatistics();

    	System.out.println(statPredCol.getMin());
    	System.out.println(statPredCol.getMax());
    	System.out.println(statValue.getMin());
    	System.out.println(statValue.getMax());

    	NumberAxis yAxis = new NumberAxis("Oœ Y", Math.floor(statPredCol.getMin()), Math.ceil(statPredCol.getMax()), 1);
    	NumberAxis xAxis = new NumberAxis("Oœ X", Math.floor(statValue.getMin()), Math.ceil(statValue.getMax()), 1);
		ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
		sc.setTitle("Wykres");

		XYChart.Series<Number, Number> seria = new Series<>();
		seria.setName("Seria 1");
//		seria.getData().add(new XYChart.Data<Number, Number>(2,5));
//		seria.getData().add(new XYChart.Data<Number, Number>(-2,10));
//		seria.getData().add(new XYChart.Data<Number, Number>(17,52));
//		seria.getData().add(new XYChart.Data<Number, Number>(1,45));

		Random r = new Random();
		int x;
		for (int i=0; i<100;i++){
//			if(i%10==0)
			x = r.nextInt(rd.numberOfRecords());
//			if (rd.getValue(0, x) > 3)
			if (rd.getValue(rd.getPredictionColumnIndex(), x) > 8)
				seria.getData().add(new XYChart.Data<Number, Number>(rd.getValue(0, x), rd.getValue(rd.getPredictionColumnIndex(), x)));
			else
				i--;
//			if(i%1000 == 0)
				System.out.println((float)i/rd.numberOfRecords()*100 + "%");
		}

		sc.getData().add(seria);

		feedFPane.getChildren().add(sc);
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
