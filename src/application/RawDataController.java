package application;

import java.awt.font.NumericShaper;

import javax.swing.text.TabableView;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.CSVFile;

public class RawDataController{

	private TableView<NumberColumnItem> dataTable;
	private Label[] columnsNames;
//	private String path;
	private CSVFile csvData;

	public RawDataController(TableView<NumberColumnItem> dataTable) {
		this.dataTable = dataTable;

		TableColumn<NumberColumnItem, String> col1 = new TableColumn<>("Name");
		col1.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<NumberColumnItem, Double> col2 = new TableColumn<>("Min");
		col2.setCellValueFactory(new PropertyValueFactory<>("min"));
		TableColumn<NumberColumnItem, Double> col3 = new TableColumn<>("Max");
		col3.setCellValueFactory(new PropertyValueFactory<>("max"));

		this.dataTable.getColumns().addAll(col1, col2, col3);
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
//			dataTable.getItems().add(kolumnsNames[i]);
//			pane.getChildren().add(this.columnsNames[i]);
		}
	}

	public void fill(NumberColumn[] numberColumns) {
		int n = numberColumns.length;
//		this.columnsNames = new Label[n];

		for(int i=0; i<n; i++){
//			this.columnsNames[i] = new Label(kolumnsNames[i]);
//			this.columnsNames[i].setLayoutX(5);
//			this.columnsNames[i].setLayoutY(22*i+5);
//			System.out.println("-> "+numberColumns[i].getName());
			dataTable.getItems().add(new NumberColumnItem(numberColumns[i]));
//			pane.getChildren().add(this.columnsNames[i]);
		}
	}

}
