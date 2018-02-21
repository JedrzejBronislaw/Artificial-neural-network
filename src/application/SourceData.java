package application;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.image.impl.ByteIndexed.Getter;

import main.CSVFile;

public class SourceData {

	public enum ColumnTypes {Number, Class, Text}

	private CSVFile file;
	private ColumnTypes[] columnTypes;
//	private ColumnStatstics[] columnStatistics;
	private NumberColumn[] numberColumns;
	private int predictionColumnIndex;

	public SourceData(String path) {
		file = new CSVFile(path);
		predictionColumnIndex = numberOfColumns()-1;
		setColumnTypes();
		removeNonNumberColumns();
		createNumeralColumns();
		setStatistics();
	}

	private void createNumeralColumns() {
		//zalozenie: pozostaly tylko kolumny z numerami
		int n = numberOfColumns();
		numberColumns = new NumberColumn[n];

		for(int i=0; i<n; i++)
			numberColumns[i] = new NumberColumn(file, i);
	}

	private void removeNonNumberColumns() {
		for(int i=numberOfColumns()-1; i>=0; i--)
			if(columnTypes[i] != ColumnTypes.Number)
				removeColumn(i);
	}

	private void setStatistics() {
//		columnStatistics = new ColumnStatstics[numberOfColumns()];
//
//		for(int i=0; i<numberOfColumns(); i++){
//			columnStatistics[i] = new ColumnStatstics(this, i, predictionColumnIndex);
//		}
		for(int i=0; i<numberOfColumns(); i++){
			numberColumns[i].setStatistics();
			//columnStatistics[i] = new ColumnStatstics(this, i, predictionColumnIndex);
		}

	}

	public ColumnStatstics getColumnStatistics(int index) {
		return numberColumns[index].getStatistics();
	}

	public NumberColumn getColumn(int index){
		return numberColumns[index];
	}

	public NumberColumn[] getColumns(){
		return numberColumns;
	}

//	public double[] getColumnData(int index){
//		return numberColumns[index].getData();
//	}

	public double getValue(int column, int record){
		return numberColumns[column].value(record);
	}


	public ColumnTypes getColumnType(int index) {
		return columnTypes[index];
	}

	private void setColumnTypes() {
		String[] column;
		columnTypes = new ColumnTypes[numberOfColumns()];

		for(int i=0; i<numberOfColumns(); i++){
			column = file.getFirstRecordsFromColumn(i);
			columnTypes[i] = checkColumnType(column);
		}
	}

	private ColumnTypes checkColumnType(String[] column) {
		ColumnTypes result = ColumnTypes.Number;

		for(int i=0; i<column.length; i++)
			try{
				Double.parseDouble(column[i]);
			} catch(NumberFormatException e) {
				result = ColumnTypes.Class;
				break;
			}

		return result;
	}

	public int numberOfColumns(){
		return file.liczbaKolumn();
	}

	public void removeColumn(int index){
		ColumnTypes[] newColumnTypes = new ColumnTypes[numberOfColumns()-1];
		int j=0;

		for(int i=0; i<numberOfColumns()-1; i++)
			if(i!=index)
				newColumnTypes[j++] = columnTypes[i];

		file.usunKolumne(index);

		if(index == predictionColumnIndex)
			predictionColumnIndex = numberOfColumns()-1;
		else
			predictionColumnIndex--;
	}

	public void selectPredictColumn(int index){
		predictionColumnIndex = index;
//		file.setKolumnaWynikow(index);
	}

	public int getPredictionColumnIndex() {
		return predictionColumnIndex;
	}

	public String[] getNamesOfColumns(){
		return file.getNazwyKolumn();
	}

	public String getColumnName(int index){
		if (index>=0 && index<numberOfColumns())
			return file.getNazwyKolumn()[index];
		else
			return "";
	}

	public int numberOfRecords() {
		return file.liczbaRekordow();
	}

//	public List getColumn(int columnIndex) {
//		ArrayList column;
//
//		if(getColumnType(columnIndex) == ColumnTypes.Number){
//			column = new ArrayList<Double>();
//			for(int i=0; i<file.liczbaRekordow(); i++)
//				column.add(Double.parseDouble(file.getValue(i, columnIndex)));
//		} else {
//			column = new ArrayList<String>();
//
//			for(int i=0; i<file.liczbaRekordow(); i++)
//				column.add(file.getValue(i, columnIndex));
//		}
//		return column;
//	}


	public float[][] getData(ColumnController columnController) {
		List<NumberColumn> columns = columnController.selectedColumn();
		float[][] data = new float[numberOfRecords()][columns.size()];

//		file.setKolumnaWynikow(columnController.getPredicColumnIndex());

//		for(float[] record : data)
		for(int r=0; r<data.length; r++){
//			int cc=0;
			for(int c=0; c<data[r].length; c++){
//				if (columnController.)
				data[r][c] = (float) columns.get(c).value(r);//getData()[r];//Float.parseFloat(file.getValue(r, c));
			}
		}


		return data;
	}
}
