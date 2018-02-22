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
	@FXML private TextField numberOfInitEpochs;
	@FXML private CheckBox shuffle;

	@FXML private TextField architecture;
	@FXML private Label layers;
	@FXML private Label neurons;
	@FXML private Label connections;

	private ColumnController columnController = null;

	public void setColumnController(ColumnController columnController) {
		this.columnController = columnController;
	}

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

	public int getNumberOfInitEpoches(){
		int result;

		try{
			result = Integer.parseInt(numberOfInitEpochs.getText());
			architecture.setStyle("-fx-background-color: white");
		} catch(NumberFormatException e) {
			result = 1;
			architecture.setStyle("-fx-background-color: red");
		}

		return result;
	}

	public float getLearnigRate(){
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

	public boolean getShuffle(){
		return shuffle.isSelected();
	}

	public int[] getArchitecture(){
		int inputSize = 0;
		if (columnController != null) inputSize = columnController.selectedColumn().size()-1;

		String text = architecture.getText();
		text = text.replace(' ', ',');
		text = text.replace('|', ',');
		text = text.replace('.', ',');
		String[] split = text.split(",");
		int n = split.length + 2;
		if (text.equals("")) n--;
		int[] splitInt = new int[n];

		for(int i=1; i<n-1; i++){
			try{
				splitInt[i] = Integer.parseInt(split[i-1]);
				architecture.setStyle("-fx-background-color: white");
			} catch(NumberFormatException e) {
				layers.setText("");
				neurons.setText("");
				connections.setText("");
				architecture.setStyle("-fx-background-color: red");
				return null;
			}
		}

		splitInt[0] = inputSize;
		splitInt[n-1] = 1;

		return splitInt;
	}


	@FXML
	private void changeArchitecture(KeyEvent event){


		int[] archi = getArchitecture();

		if(archi != null){
				architecture.setStyle("-fx-background-color: white");
			} else {
				layers.setText("");
				neurons.setText("");
				connections.setText("");
				architecture.setStyle("-fx-background-color: red");
				return;
			}

		int n = archi.length;

		int neuronsCount = 0;
		for(int i=0; i<n; i++)
			neuronsCount += archi[i];

		int connectionsCount = 0;
		for(int i=0; i<n-1; i++)
			connectionsCount += archi[i]*archi[i+1]+archi[i+1];

		layers.setText(n+"");
		neurons.setText(neuronsCount+"");
		connections.setText(connectionsCount+"");
	}
}
