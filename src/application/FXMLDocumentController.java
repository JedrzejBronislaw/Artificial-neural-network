package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private ANNController annController;

    @FXML
    private void selectFile(ActionEvent event){
    	String dataFileName = "diamenty.csv";
    	annController = new ANNController(dataFileName);
    	selectedFileName.setText(dataFileName);
    	loadDataPane.setDisable(false);
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
