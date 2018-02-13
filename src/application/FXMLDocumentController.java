package application;

import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.DzielDane;
import main.Main;
import siec.ANN;
import siec.ANNBledy;
import siec.ANNPreview;
import siec.ANN_bledy;
import siec.ANN_matrix_preview;
import siec.UczenieSieci;

public class FXMLDocumentController implements Initializable{


//    @FXML
//    private Label label;
//    @FXML
//    private Button button;
    @FXML
    private AnchorPane feedFPane;
    @FXML
    private Button inputData;
    @FXML
    private Label inputDataStatus;
    @FXML
    private TextField inKaraty;
    @FXML
    private TextField inX;
    @FXML
    private TextField inY;
    @FXML
    private TextField inZ;
    @FXML
    private Button feedForward;
    @FXML
    private Label feedFResult;

//    Function<Integer[], Double>
    Consumer<int[]> learningNetwork;
    Function<float[], Float> feedF;


//    @FXML
//    private void handleButtonAction(ActionEvent event) {
//        System.out.println("You clicked me!");
//        label.setText("Hello World!");
//    }

    @FXML
    private void hadnleButtonInputData(ActionEvent event){
    	if (learningNetwork != null){
    		try{
	    		inputDataStatus.setText("Uczenie...");
	    		learningNetwork.accept(new int[1]);
	    		feedFPane.setDisable(false);
	    		inputDataStatus.setText("Nauczona");
    		} catch (Exception e) {
    			inputDataStatus.setText("Nie nauczona");
    			feedFPane.setDisable(true);
    		}
    	}
    }

    @FXML
    private void hadnleButtonFeedForward(ActionEvent event){
    	if (feedF != null){
    		float[] input = new float[4];
    		try{
	    		input[0] = Float.parseFloat(inKaraty.getText());
	    		input[1] = Float.parseFloat(inX.getText());
	    		input[2] = Float.parseFloat(inY.getText());
	    		input[3] = Float.parseFloat(inZ.getText());
	    		feedFResult.setText(feedF.apply(input)+"");
    		} catch (NumberFormatException e) {

    		}
    	}

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		learningNetwork = (x)->diamenty();
		feedF = (wejscie)-> siec.licz(wejscie);
	}


	ANN siec = new ANN(new int[]{4,4,1});
	private void diamenty() {
		UczenieSieci uczenieSieci = new UczenieSieci(siec);
		ANNPreview preview = new ANN_matrix_preview(siec);
		ANNBledy bledy = new ANN_bledy(siec);
		float[][] dane = main.Main.diamentyDane();
		float[][] daneT, daneU;
		long time;

		DzielDane dzielDane = new DzielDane(dane, 0.3f);
		daneT = dzielDane.getDaneT();
		daneU = dzielDane.getDaneU();

		uczenieSieci.setPrzykladyTestowe(daneT);
		uczenieSieci.setPrzykladyUczace(daneU);
		uczenieSieci.setWspolczynnykUczenia(0.1f);
		uczenieSieci.setTasowanie(true);
		uczenieSieci.setZapisDoPliku(true);
		uczenieSieci.setZapisDoPlikuWag(true);

		System.out.println("똱edni bl퉐 T: " + bledy.bladSredni(daneT));
		System.out.println("똱edni bl퉐 U: " + bledy.bladSredni(daneU));

		System.out.println();

		System.out.print("Uczenie... ");
		time = System.nanoTime();
//		uczenieSieci.uczWstepnie(10,0.01f);
//		uczenieSieci.uczWstepnie(25,0.001f);
//		uczenieSieci.uczWstepnie(25,0.0001f);
//		uczenieSieci.uczWstepnie(25,0.00001f);
//		uczenieSieci.uczWstepnie(25,0.000001f);
//		uczenieSieci.uczWstepnie(25,0.0000001f);
//		uczenieSieci.uczWstepnie(25,0.00000001f);
		uczenieSieci.ucz(10);
		time = System.nanoTime() - time;
		System.out.println((time/1000000)/1000f + " s");

		System.out.println();

		System.out.println("똱edni bl퉐 T: " + bledy.bladSredni(daneT));
		System.out.println("똱edni bl퉐 U: " + bledy.bladSredni(daneU));

		System.out.println();

		preview.showWeights();

		Main.pokazWykres("bledyTemp.txt");
		Main.pokazWykres("wagiTemp.txt");

	}


}
