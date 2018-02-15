package application;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.CSVFile;

public class RawDataController{

	private AnchorPane pane;
	private Label[] columnsNames;
//	private String path;
	private CSVFile csvData;

	public RawDataController(AnchorPane pane) {
		this.pane = pane;
	}

	public void loadCSVData(String path){
//		this.path = path;
//		try{
			csvData = new CSVFile(path);
			//TODO
//		} catch(IOException e){
//			csvData = null;
//		}
	}

	public void setColumnsNames() {
		setColumnsNames(csvData.getNazwyKolumn());
	}

	public void setColumnsNames(String[] kolumnsNames) {
		int n = kolumnsNames.length;
		this.columnsNames = new Label[n];

		for(int i=0; i<n; i++){
			this.columnsNames[i] = new Label(kolumnsNames[i]);
			this.columnsNames[i].setLayoutX(5);
			this.columnsNames[i].setLayoutY(22*i+5);
			pane.getChildren().add(this.columnsNames[i]);
		}
	}

}
