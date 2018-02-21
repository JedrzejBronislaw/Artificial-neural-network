package application;


import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class NetworkParametersController {

	@FXML private AnchorPane networkParametersPane;
	@FXML private TextField learnigRate;
	@FXML private TextField numberOfEpochs;
	@FXML private CheckBox shuffle;

	@FXML private TextField architecture;
	@FXML private Label layers;
	@FXML private Label neurons;
	@FXML private Label connections;

	public NetworkParametersController() {
	}

	public int getNumberOfEpoches(){
		int result;
		
		try{
			result = Integer.parseInt(numberOfEpochs.getText());
			architecture.setStyle("-fx-background-color: white");
		} catch(NumberFormatException e) {
			result = 1;
			architecture.setStyle("-fx-background-color: red");
		}
		
		return result;
	}

	public float learnigRate(){
		float result;
		
		try {
			result = Float.parseFloat(learnigRate.getText());
			architecture.setStyle("-fx-background-color: white");
		} catch (NumberFormatException e) {
			result = 0;
			architecture.setStyle("-fx-background-color: red");
		}
		
		return result;
	}


	@FXML
	private void changeArchitecture(KeyEvent event){
		String text = architecture.getText()+event.getText();
		text = text.replace(' ', ',');
		text = text.replace('|', ',');
		text = text.replace('.', ',');
		String[] split = text.split(",");
		int n = split.length;
		int[] splitInt = new int[n];

		for(int i=0; i<n; i++){
			try{
				splitInt[i] = Integer.parseInt(split[i]);
				architecture.setStyle("-fx-background-color: white");
			} catch(NumberFormatException e) {
				layers.setText("");
				neurons.setText("");
				connections.setText("");
				architecture.setStyle("-fx-background-color: red");
				return;
			}
		}

		int neuronsCount = 0;
		for(int i=0; i<n; i++)
			neuronsCount += splitInt[i];

		int connectionsCount = 0;
		for(int i=0; i<n-1; i++)
			connectionsCount += splitInt[i]*splitInt[i+1]+splitInt[i+1];

		layers.setText(n+"");
		neurons.setText(neuronsCount+"");
		connections.setText(connectionsCount+"");
	}
}
